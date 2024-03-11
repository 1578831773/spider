package com.example.demo.parser.sohu;

import com.example.demo.bean.SohuArticle;
import us.codecraft.xsoup.Xsoup;

import java.util.List;

public class SohuArticleParser{

    private String html;
    private String titleXpath;
    private String publishTimeXpath;
    private String tagsXpath;
    private String contentXpath;
    private String countXpath;
    private String url;

    private SohuArticle sohuArticle;


    public SohuArticleParser(){}

    public SohuArticle getSohuArticle() {
        return sohuArticle;
    }

    public SohuArticleParser(String url, String html, String titleXpath, String publishTimeXpath, String tagsXpath, String contentXpath, String countXpath) {
        this.url = url;
        this.html = html;
        this.titleXpath = titleXpath;
        this.publishTimeXpath = publishTimeXpath;
        this.tagsXpath = tagsXpath;
        this.contentXpath = contentXpath;
        this.countXpath = countXpath;
        //进行解析
        doParse();
    }

    private void doParse(){
        sohuArticle = new SohuArticle();
        sohuArticle.setUrl(url);
        sohuArticle.setTitle(parseToFirstString(html, titleXpath));
        sohuArticle.setPublishTime(parseToFirstString(html, publishTimeXpath));
        sohuArticle.setTags(parseToUnionString(html, tagsXpath, "/"));
        sohuArticle.setContent(parseToUnionString(html, contentXpath, ""));
        sohuArticle.setCount(parseToFirstString(html, countXpath));
    }

    /**
     * 将所有元素连接起来使用特定的分隔符，输出一个整串
     */
    private String parseToUnionString(String html, String xpath, String sep){
        StringBuffer buffer = new StringBuffer();
        List<String> list = Xsoup.select(html, xpath).list();
        if(list != null && !list.isEmpty()){
            for(String s: list){
                buffer.append(s + sep);
            }
        }
        return buffer.toString();
    }

    /**
     * 解析一个元素
     */
    private String parseToFirstString(String html, String xpath){
        StringBuffer buffer = new StringBuffer();
        List<String> list = Xsoup.select(html, xpath).list();
        if(list != null && !list.isEmpty()){
            return list.get(0);
        }
        return "";
    }

    /**
     * 解析第一个元素成为整数
     */
    private int parseToFirstInt(String html, String xpath){
        String str = parseToFirstString(html, xpath);
        try{
            return Integer.parseInt(str);
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


}
