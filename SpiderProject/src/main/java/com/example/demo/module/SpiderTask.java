package com.example.demo.module;

import com.example.demo.bean.CommArticle;
import com.example.demo.bean.WeiboArticle;
import com.example.demo.bean.WeiboTopArticle;
import com.example.demo.enums.SpiderConstants;
import com.example.demo.parser.CommParser;
import com.example.demo.parser.WeiboParser;
import com.example.demo.parser.sohu.SohuArticleParser;
import com.example.demo.parser.sohu.SohuPageParser1;
import com.example.demo.service.CommService;
import com.example.demo.service.RedisService;
import com.example.demo.service.SohuService;
import com.example.demo.service.WeiboService;
import com.example.demo.util.PropertiesUtil;
import com.example.demo.util.SiteUtil;
import com.example.demo.util.UrlUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 爬虫任务
 */
@Component
@Scope("prototype")
public class SpiderTask implements Runnable{
    DownLoader downLoader = DownLoader.getInstance();

    private String url;
    private int level;
    private int bootId;
    private int templateId;
    private int spiderNum;
    @Autowired
    private SohuService sohuService;
    @Autowired
    private CommService commService;
    @Autowired
    private WeiboService weiboService;


    public SpiderTask() {
    }
    public SpiderTask(String comboUrl) {
        this.url = UrlUtil.extractUrl(comboUrl);
        this.level = UrlUtil.extractLevel(comboUrl);
    }
    public SpiderTask(String url, int level, int bootId, int templateId){
        this.url = url;
        this.level = level;
        this.bootId = bootId;
        this.templateId  = templateId;
    }

    public int getSpiderNum() {
        return spiderNum;
    }

    public void setSpiderNum(int spiderNum) {
        this.spiderNum = spiderNum;
    }

    /**
     * 开始下载
     */
    @Override
    public void run() {
        //下载前处理
        beforeDownloadProcess();
        //开始下载
        HttpResponse resp = downLoader.doDownload(this);
        //下载后处理
        afterDownloadProcess(resp);
    }

    /**
     * 下载后处理
     * @param resp
     */
    private void afterDownloadProcess(HttpResponse resp) {
        RedisService.delFromDownloadings(this.bootId, this.url, this.level);
        if(resp != null){
            if(resp.getStatusLine().getStatusCode() == 200){
                try{
                    RedisService.addToOks(this.bootId, this.url, this.level);
                    //解析页面
                    HttpEntity e = resp.getEntity();
                    String html = EntityUtils.toString(e);

                    String domain = SiteUtil.getDomainName(this.url);
                    if(templateId == 2){
                        String urlXpath = PropertiesUtil.getComUrlXpath();
                        String articleXpath = PropertiesUtil.getComArticleXpath();
                        String contentXpath = PropertiesUtil.getComContentXpath();
                        CommParser commParser = new CommParser(html, urlXpath, articleXpath, contentXpath);
                        List<String> subUrls = commParser.getUrls();
                        List<String> articles = commParser.getArticles();
                        //处理子链接
                        int num = processSubUrl(subUrls);
                        //处理文章
                        processArticle(articles);
                        RedisService.deSpiderNum(bootId);
                        CommArticle commArticle = commParser.getCommArticle();
                        commArticle.setUrl(url);
                        commArticle.setBootId(bootId);
                        commService.saveArticle(commArticle);
                    }else if(templateId == 1){
                        //一级页面
                        if(level == 1){
                            String urlXpath = PropertiesUtil.getUrlXpath(url, this.level);
                            String articleXpath = PropertiesUtil.getArticleXpath(url, this.level);
                            SohuPageParser1 parser = new SohuPageParser1(html, urlXpath, articleXpath, url, spiderNum);
                            List<String> subUrls = parser.getUrls();
                            List<String> articles = parser.getArticles();
                            //处理子链接
                            int num = processSubUrl(subUrls);
                            RedisService.setSpiderNum(bootId, num);
                            //处理文章
                            processArticle(articles);
                        }else if(level == 2){
                            String titleXpath = PropertiesUtil.getTitleXpath(url, this.level);
                            String publishTimeXpath = PropertiesUtil.getPublishTimeXpath(url, this.level);
                            String tagXpath = PropertiesUtil.getTagsXpath(url, this.level);
                            String contentXpath = PropertiesUtil.getContentXpath(url, this.level);
                            String countXpath = PropertiesUtil.getCountXpath(url, this.level);
                            SohuArticleParser parser = new SohuArticleParser(url,html, titleXpath, publishTimeXpath, tagXpath, contentXpath,countXpath);
                            sohuService.saveArticle(parser.getSohuArticle(), bootId);
                            RedisService.deSpiderNum(bootId);
                        }
                    }else if(templateId == 3){
                        WeiboParser weiboParser = WeiboParser.getInstance();
                        if(level == 1){
                            List<String> urls = weiboParser.parser();
                            int num = processSubUrl(urls);
                            RedisService.setSpiderNum(bootId, num);
                        }else if(level == 2){
                            WeiboTopArticle weiboTopArticle = weiboParser.doParser(this.url);
                            weiboTopArticle.getWeiboArticle().setBootId(bootId);
                            System.out.println(weiboTopArticle.getWeiboArticle().getContent());
                            weiboService.saveTopArticle(weiboTopArticle);
                            RedisService.deSpiderNum(bootId);
                        }
                    }else if(templateId == 4){
                        WeiboParser weiboParser = WeiboParser.getInstance();
                        List<WeiboArticle> weiboArticles = weiboParser.getWeiboArticleList(url, spiderNum, bootId);
                        weiboService.saveArticle(weiboArticles);
                        RedisService.setSpiderNum(bootId, 0);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }else{
                double count = RedisService.incrFails(this.bootId, url);
                if(count < SpiderConstants.SPIDER_FAILURE_RETRIES){
                    RedisService.addToFutures(this.bootId, this.url,this.level);
                }
            }
        }
    }

    /**
     * 处理文章
     */
    private void processArticle(List<String> articles) {
        //TODO
        for(String s: articles){
            System.out.println(s);
        }
    }


    /**
     * 处理解析出来的url地址
     */
    private int processSubUrl(List<String> subUrls) {
        if(subUrls == null || subUrls.isEmpty()){
            return 0;
        }
        int num = 0;
        int level1 = this.level + 1;
        for(String url: subUrls){
            if(!url.contains("http")) continue;
            //处理页面解析出来的子url
            if(RedisService.existsInFutures(bootId, url, level1)
                    || RedisService.existsInOks(bootId, url, level1)
                    || RedisService.existsDownloadings(bootId, url,  level1)){
                continue;
            }
            if(RedisService.existsInFails(bootId,url, level1)){
                int max = RedisService.getFailCounts(bootId, url, level1);
                if(max < SpiderConstants.SPIDER_FAILURE_RETRIES) {
                    RedisService.addToFutures(bootId, url, level1);
                    num ++;
                    continue;
                }
            }
            RedisService.addToFutures(bootId, url, level1);
            num ++;
            if(num == 100) break;
        }
        return num;
    }

    /**
     * 下载前处理
     */
    private void beforeDownloadProcess() {
        //添加下载链接到下载集合
        RedisService.addToDownloadings(bootId, url, level);
    }

    public DownLoader getDownLoader() {
        return downLoader;
    }

    public void setDownLoader(DownLoader downLoader) {
        this.downLoader = downLoader;
    }

    public int getBootId() {
        return bootId;
    }

    public void setBootId(int bootId) {
        this.bootId = bootId;
    }

    public SohuService getSohuService() {
        return sohuService;
    }

    public void setSohuService(SohuService sohuService) {
        this.sohuService = sohuService;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }
}
