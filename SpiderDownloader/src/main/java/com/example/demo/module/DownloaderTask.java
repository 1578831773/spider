package com.example.demo.module;

import com.example.demo.bean.CommArticle;
import com.example.demo.bean.SohuArticle;
import com.example.demo.bean.WeiboArticle;
import com.example.demo.downloader.CommDownloader;
import com.example.demo.downloader.SohuDownloader;
import com.example.demo.mapper.ArticleMapper;
import com.example.demo.util.FileUtil;
import com.example.demo.util.RedisUtil;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Data
@Scope("prototype")
public class DownloaderTask implements Runnable{
    @Autowired
    private ArticleMapper articleMapper;
    private String recordId;
    private String templateId;
    private String url;
    private int level;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SohuDownloader sohuDownloader;
    @Autowired
    private CommDownloader commDownloader;

    public DownloaderTask(){}
    public DownloaderTask(String recordId, String templateId, String url, int level){
        this.recordId = recordId;
        this.templateId = templateId;
        this.url = url;
        this.level = level;
    }
    @Override
    public void run() {
        System.out.println("task: " + this.toString());
        String html = "";
        int articleId;
        if(templateId.equals("1")){
            if(level == 1){
                String spiderNum = (String)redisUtil.get(recordId);
                html = sohuDownloader.doDownloadForLevel1(url, Integer.parseInt(spiderNum));
                articleId = Integer.parseInt(recordId);
            }else{
                HttpResponse response =  commDownloader.doDownload(this);
                try {
                    html = EntityUtils.toString(response.getEntity(), "utf-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    response.getEntity().getContent().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SohuArticle sohuArticle = new SohuArticle(Integer.parseInt(recordId), url);
                articleMapper.insertSohuArticle(sohuArticle);
                articleId = sohuArticle.getArticleId();
            }
        }else if(templateId.equals("3")){
            if(level == 1){
                articleId = Integer.parseInt(recordId);
            }else{
                WeiboArticle weiboArticle = new WeiboArticle(Integer.parseInt(recordId), url);
                articleMapper.insertWeiboArticle(weiboArticle);
                articleId = weiboArticle.getArticleId();
            }
        }else if(templateId.equals("4")){
            articleId = Integer.parseInt(recordId);
        }else{
            HttpResponse response =  commDownloader.doDownload(this);
            try {
                html = EntityUtils.toString(response.getEntity(), "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                response.getEntity().getContent().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            CommArticle commArticle = new CommArticle(Integer.parseInt(recordId), url);
            articleMapper.insertCommArticle(commArticle);
            articleId = commArticle.getArticleId();

        }
        FileUtil.saveAsFileWriter(html,  articleId + "_" + templateId + "_" + level);
        Object analyzeNum = redisUtil.get("analyze_" + recordId);
        if(analyzeNum == null){
            redisUtil.set("analyze_" + recordId, "1");
        }else{
            redisUtil.set("analyze_" + recordId, String.valueOf(Integer.parseInt((String)analyzeNum)+1));
        }
        redisUtil.zset("html", articleId + "_" + templateId + "_" + level, System.currentTimeMillis());
        System.out.println("save html");

    }


}
