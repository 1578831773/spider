package com.example.demo.parser;

import com.example.demo.bean.CommArticle;
import com.example.demo.util.PropertiesUtil;
import us.codecraft.xsoup.Xsoup;

import java.util.ArrayList;
import java.util.List;

public class CommParser {
    private String html;
    private CommArticle commArticle;
    private List<String> urls;
    private List<String > articles;

    public CommArticle getCommArticle() {
        return commArticle;
    }

    public void setCommArticle(CommArticle commArticle) {
        this.commArticle = commArticle;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getArticles() {
        return articles;
    }

    public void setArticles(List<String> articles) {
        this.articles = articles;
    }

    public CommParser(){}
    public CommParser(String html){
        this.html = html;
        parseUrlsAndArticles();
        doParse();
    }


    private void doParse(){
        commArticle = new CommArticle();
        commArticle.setContent(parseToUnionString(html, PropertiesUtil.getComContentXpath(), ","));
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

    public void parseUrlsAndArticles() {
        List<String> urls1 =  Xsoup.select(html, PropertiesUtil.getComUrlXpath()).list();
        List<String> articles1 = Xsoup.select(html, PropertiesUtil.getComArticleXpath()).list();
        urls = new ArrayList<>();
        articles = new ArrayList<>();
        for(int i = 0; i < urls1.size();i ++){
            String url = urls1.get(i);
            String article = articles1.get(i);
            System.out.println(url);
            System.out.println(article);
            if(url.startsWith("http")){
                urls.add(url);
                articles.add(article);
            }else if(url.startsWith("//")){
                urls.add("http:" + url);
                articles.add(article);
            }
        }

    }
}
