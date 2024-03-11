package com.example.demo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 属性工具类
 */
public class PropertiesUtil {
    private static Properties prop;
    static {
        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("spider.properties");
            prop = new Properties();
            prop.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 字符串属性
     */
    public static String getString(String name){
        return prop.getProperty(name);
    }

    /**
     *int类型
     */
    public static int getInt(String name){
        return Integer.parseInt(prop.getProperty(name));
    }

    /**
     * 获取url xpath
     */
    public static String getUrlXpath(String domain, String level){
        String key = String.format("%s.%s.urlxpath", domain, level);
        return getString(key);
    }

    /**
     * 获取article xpath
     */
    public static String getArticleXpath(String url, int level){
        String domain = SiteUtil.getDomainName(url);
        String key = String.format("%s.%d.articlexpath", domain, level);
        return getString(key);
    }


    /**
     * 获取文章标题
     */
    public static String getTitleXpath(String domain, String level){
        String key = String.format("%s.%s.article.title.xpath", domain, level);
        return getString(key);
    }

    /**
     * 获取发布时间
     */
    public static String getPublishTimeXpath(String domain, String level){
        String key = String.format("%s.%s.article.publishtime.xpath", domain, level);
        return getString(key);
    }

    /**
     * 获取标签
     */
    public static String getTagsXpath(String domain, String level){
        String key = String.format("%s.%s.article.tags.xpath", domain, level);
        return getString(key);
    }

    /**
     * 获取正文
     */
    public static String getContentXpath(String domain, String level){
        String key = String.format("%s.%s.article.content.xpath", domain, level);
        return getString(key);
    }

    /**
     * 获取文章阅读量
     */
    public static String getCountXpath(String domain, String level){
        String key = String.format("%s.%s.article.count.xpath", domain, level);
        return getString(key);
    }

    public static String getComUrlXpath(){
        String key = "com.urlxpath";
        return getString(key);
    }

    public static String getComArticleXpath(){
        String key = "com.articlexpath";
        return getString(key);
    }

    public static String getComContentXpath(){
        String key = "com.contentxpath";
        return getString(key);
    }

    public static String getWeiboTitleXpath(){
        String key = "weibo.top.2.article.title.xpath";
        return getString(key);
    }

    public static String getWeiboIntroXpath(){
        String key = "weibo.top.2.article.intro.xpath";
        return getString(key);
    }

    public static String getWeiboReadNumXpath(){
        String key = "weibo.top.2.article.readNum.xpath";
        return getString(key);
    }

    public static String getWeiboNickNameXpath(){
        String key = "weibo.top.2.article.nickName.xpath";
        return getString(key);
    }

    public static String getWeiboContentXpath(){
        String key = "weibo.top.2.article.content.xpath";
        return getString(key);
    }

    public static String getWeiboCommentNumXpath(){
        String key = "weibo.top.2.article.commentNum.xpath";
        return getString(key);
    }

    public static String getWeiboCommentNum1Xpath(){
        String key = "weibo.top.2.article.commentNum1.xpath";
        return getString(key);
    }

    public static String getWeibolikeNumXpath(){
        String key = "weibo.top.2.article.likeNum.xpath";
        return getString(key);
    }

}
