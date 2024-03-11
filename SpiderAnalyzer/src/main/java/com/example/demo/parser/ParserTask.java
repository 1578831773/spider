package com.example.demo.parser;

import com.example.demo.bean.CommArticle;
import com.example.demo.bean.WeiboArticle;
import com.example.demo.bean.WeiboTopArticle;
import com.example.demo.service.CommService;
import com.example.demo.service.RecordService;
import com.example.demo.service.SohuService;
import com.example.demo.service.WeiboService;
import com.example.demo.util.FileUtil;
import com.example.demo.util.RedisUtil;
import com.example.demo.util.RedisUtilForUrls;
import com.example.demo.util.SiteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.Sides;
import java.io.IOException;
import java.util.List;

@Component
@Scope("prototype")
public class ParserTask implements Runnable{
    @Autowired
    private SohuService sohuService;
    @Autowired
    private WeiboService weiboService;
    @Autowired
    private CommService commService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private RedisUtil redisUtil;
    private String path;

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public ParserTask(){}
    public ParserTask(String path){
        this.path = path;
        redisUtil = new RedisUtil();
    }

    @Override
    public void run() {
        try {
            String html = FileUtil.LoadContentByPath(path);
            //记录号_模板号_级别
            String[] str = path.split("_");
            String recordId = str[0];
            String templateId = str[1];
            String level = str[2];
            int recordId0 = Integer.parseInt(recordId);
            System.out.println("start to analyze " + recordId);
            switch (templateId) {
                case "1":
                    SohuParser sohuParser = new SohuParser(html, level);
                    if (level.equals("1")) {
                        recordId0 = Integer.parseInt(recordId);
                        List<String> subUrls = sohuParser.parseUrls();
                        int num = processSubUrl(subUrls, Integer.parseInt(level), Integer.parseInt(recordId), 1);
                        redisUtil.set(recordId, String.valueOf(num));
                        redisUtil.zset("future_recordId", recordId+"_"+templateId, System.currentTimeMillis());
                    } else if (level.equals("2")) {
                        sohuService.saveArticle(sohuParser.parserArticle(), Integer.parseInt(recordId));
                        recordId0 = sohuService.getRecordIdByArticleId(Integer.parseInt(recordId));
                    }
                    break;
                case "3":
                    WeiboParser weiboParser = WeiboParser.getInstance();
                    if(level.equals("1")){
                        recordId0 = Integer.parseInt(recordId);
                        List<String> urls = null;
                        try {
                            urls = weiboParser.parser();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int num = processSubUrl(urls, Integer.parseInt(level), Integer.parseInt(recordId), 3);
                        RedisUtilForUrls.setSpiderNum(Integer.parseInt(recordId), num);
                        redisUtil.zset("future_recordId", recordId+"_"+templateId, System.currentTimeMillis());
                    }else if(level.equals("2")){
                        String url = weiboService.getUrlByArticleId(Integer.parseInt(recordId));
                        WeiboTopArticle weiboTopArticle = weiboParser.doParser((String)url);
                        weiboTopArticle.getWeiboArticle().setArticleId(Integer.parseInt(recordId));
                        System.out.println(weiboTopArticle.getWeiboArticle().getContent());
                        weiboService.saveTopArticle(weiboTopArticle, Integer.parseInt(recordId));
                        int record0 = weiboService.getRecordIdByArticleId(Integer.parseInt(recordId));
                        RedisUtilForUrls.deSpiderNum(record0);
                        recordId0 = weiboService.getRecordIdByArticleId(Integer.parseInt(recordId));
                    }
                    break;
                case "4":
                    String url = weiboService.getUrlByRecordId(Integer.parseInt(recordId));
                    WeiboParser weiboParser0 = WeiboParser.getInstance();
                    List<WeiboArticle> weiboArticles = weiboParser0.getWeiboArticleList((String)url, Integer.parseInt((String)redisUtil.get(recordId)), Integer.parseInt(recordId));
                    weiboService.saveArticle(weiboArticles);
                    RedisUtilForUrls.setSpiderNum(Integer.parseInt(recordId), 0);
                    recordId0 = Integer.parseInt(recordId);
                    break;
                default:
                    CommParser commParser = new CommParser(html);
                    List<String> subUrls = commParser.getUrls();
                    List<String> articles = commParser.getArticles();
                    //处理子链接
                    int num = processSubUrl(subUrls, Integer.parseInt(level), Integer.parseInt(recordId), 2);
                    if(num > 0){
                        redisUtil.zset("future_recordId", recordId+"_"+templateId, System.currentTimeMillis());
                    }
                    RedisUtilForUrls.deSpiderNum(Integer.parseInt(recordId));
                    CommArticle commArticle = commParser.getCommArticle();
                    commArticle.setArticleId(Integer.parseInt(recordId));
                    commService.saveArticle(commArticle);
                    if(level.equals("1")){
                        recordId0 = Integer.parseInt(recordId);
                    }else{
                        recordId0 = commService.getRecordIdByArticleId(Integer.parseInt(recordId));
                    }
                    break;
            }
            Object analyzeNum = redisUtil.get("analyze_" + recordId0);
            if(analyzeNum == null){
                redisUtil.set("analyze_" + recordId0, "0");
            }else{
                redisUtil.set("analyze_" + recordId0, String.valueOf(Integer.parseInt((String)analyzeNum)-1));
                if(Integer.parseInt((String)analyzeNum) <= 2){
                    Object spider_num = redisUtil.get(String.valueOf(recordId0));
                    if(spider_num == null || Integer.parseInt((String)spider_num) <= 0){
                        recordService.updateRecordStatus(recordId0, 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("获取"+path+"的html资源失败");
        }

    }

    /**
     * 处理解析出来的url地址
     */
    private int processSubUrl(List<String> subUrls, int level, int bootId, int templateId) {
        if(subUrls == null || subUrls.isEmpty()){
            return 0;
        }
        int num = 0;
        int level1 = level + 1;
        for(String url: subUrls){
            if(!url.contains("http")) continue;
            //处理页面解析出来的子url
            if(RedisUtilForUrls.existsInFutures(bootId, url, level1)
                    || RedisUtilForUrls.existsInOks(bootId, url, level1)
                    || RedisUtilForUrls.existsDownloadings(bootId, url,  level1)){
                continue;
            }
            if(RedisUtilForUrls.existsInFails(bootId,url, level1)){
                int max = RedisUtilForUrls.getFailCounts(bootId, url, level1);
                if(max < 5) {
                    RedisUtilForUrls.addToFutures(bootId, url, level1);
                    num ++;
                    continue;
                }
            }

            RedisUtilForUrls.addToFutures(bootId, url, level1);
            num ++;
            if(num == 100) break;
        }
        return num;
    }
}
