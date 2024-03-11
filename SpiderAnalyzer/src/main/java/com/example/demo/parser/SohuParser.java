package com.example.demo.parser;

import com.example.demo.bean.SohuArticle;
import com.example.demo.util.PropertiesUtil;
import us.codecraft.xsoup.Xsoup;

import java.util.ArrayList;
import java.util.List;

public class SohuParser {
    private String html;
    private String level;
    public SohuParser(){}
    public SohuParser(String html, String level){
        this.level = level;
        this.html = html;
    }
    public List<String> parseUrls(){
        List<String> urls1 =  Xsoup.select(html, PropertiesUtil.getUrlXpath("sohu", level)).list();
        List<String> urls2 = new ArrayList<>();
        for(String url: urls1){
            System.out.println(url);
            if(url.startsWith("http")){
                urls2.add(url);
            }else if(url.startsWith("//")){
                urls2.add("http:" + url);
            }
        }
        return urls2;
    }

    public SohuArticle parserArticle(){
        SohuArticle sohuArticle = new SohuArticle();
        sohuArticle.setTitle(parseToFirstString(html, PropertiesUtil.getTitleXpath("sohu", level)));
        sohuArticle.setPublishTime(parseToFirstString(html, PropertiesUtil.getPublishTimeXpath("sohu", level)));
        sohuArticle.setTags(parseToUnionString(html, PropertiesUtil.getTagsXpath("sohu", level), "/"));
        sohuArticle.setContent(parseToUnionString(html, PropertiesUtil.getContentXpath("sohu", level), ""));
        sohuArticle.setCount(parseToFirstString(html, PropertiesUtil.getCountXpath("sohu", level)));
        return sohuArticle;
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

}
