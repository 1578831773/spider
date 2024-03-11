package com.example.demo.parser.sohu;

import com.example.demo.parser.MyChromeDriver;
import com.example.demo.parser.PageParser;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import us.codecraft.xsoup.Xsoup;

import java.util.ArrayList;
import java.util.List;

/**
 * sohu首页页面解析器
 */
public class SohuPageParser1 implements PageParser {
    private String html;
    private String urlXpath;
    private String articleXpath;
    private String url;
    private int spiderNum;

    private List<String> urls;
    private List<String> articles;
    //    private static WebDriver webDriver = MyChromeDriver.getWebDriver();
    private static ChromeDriver chromeDriver = MyChromeDriver.getChromeDriver();

    public SohuPageParser1(){}
    public SohuPageParser1(String html, String urlXpath, String articleXpath,String url, int spiderNum){
        this.html = html;
        this.urlXpath = urlXpath;
        this.articleXpath = articleXpath;
        this.url = url;
        this.spiderNum = spiderNum;
        this.urls = parseUrls();
        this.articles = parseArticle();
    }

    /**
     * 解析url地址
     * @return
     */
    @Override
    public List<String> parseUrls() {
        List<String> urls1 =  Xsoup.select(html, urlXpath).list();
        List<String> urls2 = new ArrayList<>();
        for(String url: urls1){
            System.out.println(url);
            if(url.startsWith("http")){
                urls2.add(url);
            }else if(url.startsWith("//")){
                urls2.add("http:" + url);
            }
        }
        int size = urls1.size();
        if(urls2.size() < spiderNum){
            chromeDriver.get(url);
        }
        while(urls2.size() < spiderNum){
            try {
                scroll(url);
                String html0 = chromeDriver.getPageSource();
                urls1 = Xsoup.select(html0, urlXpath).list();
                for(int i = size;i < urls1.size(); i++){
                    System.out.println(urls1.get(i));
                    if(urls1.get(i).startsWith("http")){
                        urls2.add(urls1.get(i));
                    }else if(urls1.get(i).startsWith("//")){
                        urls2.add("http:" + urls1.get(i));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                chromeDriver.close();
                return urls2;
            }
        }
        chromeDriver.close();
        return urls2;
    }

    @Override
    public List<String> parseArticle() {
        return Xsoup.select(html, articleXpath).list();
    }

    public void scroll(String url) throws InterruptedException {
        Thread.sleep(1000);
        String height = chromeDriver.executeScript("return document.body.scrollHeight;").toString();
        Actions actions = new Actions(chromeDriver);
        actions.sendKeys(Keys.END).perform();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        //下滑完了,再次检查当前页面高度,如果滑到底部,则两次高度相同
//        String newHeight = chromeDriver.executeScript("return document.body.scrollHeight;").toString();
//        if (height.equals(newHeight)) {
//            break;
//            }
//        }

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
}
