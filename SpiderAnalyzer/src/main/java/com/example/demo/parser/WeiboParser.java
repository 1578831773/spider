package com.example.demo.parser;

import com.example.demo.bean.WeiboArticle;
import com.example.demo.bean.WeiboTopArticle;
import com.example.demo.util.CommonUtil;
import com.example.demo.util.MyChromeDriver;
import com.example.demo.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import us.codecraft.xsoup.Xsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeiboParser {
    private static WeiboParser weiboParser = null;
    private static ChromeDriver chromeDriver = MyChromeDriver.getChromeDriver();

    private WeiboParser(){
    }

    public static synchronized WeiboParser getInstance(){
        if(weiboParser == null){
            weiboParser = new WeiboParser();
        }
        return weiboParser;
    }

    /**
     * 模拟新浪微博登录
     * @param name　用户名
     * @param password　密码
     */
    public void login(String name,String password){
        chromeDriver.get("http://login.sina.com.cn/");
        WebElement elementName = chromeDriver.findElement(By.name("username"));
        elementName.sendKeys(name);
        WebElement elementPassword = chromeDriver.findElement(By.name("password"));
        elementPassword.sendKeys(password);
        WebElement elementClick = chromeDriver.findElement(By.xpath("//*[@id=\"vForm\"]/div[2]/div/ul/li[7]/div[1]/input"));
        elementClick.click();
    }


    private void getMore(){
        WebElement element1 = chromeDriver.findElements(By.className("content")).get(0);
        WebElement element2 = element1.findElements(By.tagName("p")).get(1);
        try{
            WebElement element3 = element2.findElement(By.className("wbicon"));
            element3.click();
        }catch (Exception ignored){
        }
    }

    //解析出每个热搜的url
    public List<String> parser() throws InterruptedException {
        String url = "https://s.weibo.com/top/summary/";
        chromeDriver.get(url);
        Thread.sleep(10000);
//        WebElement element1 = webDriver.findElement(By.className("m-main"));
//        WebElement element2 = webDriver.findElement(By.id(""))
        WebElement element0 = chromeDriver.findElement(By.id("pl_top_realtimehot"));
        List<WebElement> elements = element0.findElements(By.className("td-02"));
        System.out.println(elements.size());
        List<String> urls = new ArrayList<>();
        for(WebElement element : elements) {
            WebElement webElement = element.findElement(By.tagName("a"));
            String href = webElement.getAttribute("href");
            urls.add(href);
            System.out.println(href);
            System.out.println(webElement.getText());
        }
        return urls;
    }

    //解析网页的第一条微博
    public synchronized WeiboTopArticle doParser(String url){
        chromeDriver.get(url);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("sleep wrong");
            e.printStackTrace();
        }finally {
            getMore();
            String html = chromeDriver.getPageSource();
            return parserTopArticle(html, url);
        }
    }

    public List<WeiboArticle> getWeiboArticleList(String url, int spiderNum, int bootId){
        chromeDriver.get(url);
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        List<WebElement> webElements = chromeDriver.findElements(By.className("vue-recycle-scroller__item-view"));
        System.out.println(webElements.size());
        HashMap<String, WebElement> webElementHashMap = new HashMap<>();
        for(WebElement webElement: webElements){
            String key = webElement.findElement(By.className("wbpro-scroller-item")).getAttribute("data-index");
            webElementHashMap.put(key, webElement);
        }
        while(webElementHashMap.size() < spiderNum){
            try {
                scroll(url);
                Thread.sleep(2000);
                List<WebElement> newWebElements = chromeDriver.findElements(By.className("vue-recycle-scroller__item-view"));
                for(WebElement webElement: newWebElements){
                    String index = webElement.findElement(By.className("wbpro-scroller-item")).getAttribute("data-index");
                    if(!webElementHashMap.containsKey(index)){
                        webElementHashMap.put(index, webElement);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        List<WeiboArticle> weiboArticles = new ArrayList<>();
        int num = 0;
        for(String key : webElementHashMap.keySet()){
            if(num == spiderNum) break;
            WeiboArticle weiboArticle = parserArticle(webElementHashMap.get(key), bootId);
            if(StringUtils.isNotBlank(weiboArticle.getNickName())){
                weiboArticles.add(weiboArticle);
            }
        }
        return weiboArticles;
    }

    private WeiboArticle parserArticle(WebElement element, int bootId){
        String nickName = element.findElement(By.className("head_name_24eEB")).findElement(By.tagName("span")).getText();
        String publishTime = element.findElement(By.className("head-info_time_6sFQg")).getText();
        String content = element.findElement(By.className("detail_wbtext_4CRf9")).getText();
        String commentNum = element.findElement(By.className("toolbar_num_JXZul")).getText();
        String likeNum = element.findElement(By.className("woo-like-count")).getText();
        WeiboArticle weiboArticle = new WeiboArticle(nickName, publishTime,  content.replaceAll("[^A-Za-z0-9 \\u4e00-\\u9fa5]", ","), commentNum, likeNum);
        weiboArticle.setSpiderTime(CommonUtil.getNowTime());
        weiboArticle.setBootId(bootId);
        return weiboArticle;
    }

    public WeiboTopArticle parserTopArticle(String html, String url){
        String title = "";
        String intro = "";
        String readNum = "";
        String discussNum= "";
        try{
            title = Xsoup.select(html, PropertiesUtil.getWeiboTitleXpath()).list().get(0);
            intro = Xsoup.select(html, PropertiesUtil.getWeiboIntroXpath()).list().get(0);
            readNum = Xsoup.select(html, PropertiesUtil.getWeiboReadNumXpath()).list().get(0);
            discussNum =  Xsoup.select(html, PropertiesUtil.getWeiboReadNumXpath()).list().get(1);
        }catch (Exception ignored){
        }finally {
            String nickName = Xsoup.select(html, PropertiesUtil.getWeiboNickNameXpath()).list().get(0);
            String content = Xsoup.select(html, PropertiesUtil.getWeiboContentXpath()).list().get(0);
            String commentNum = Xsoup.select(html, PropertiesUtil.getWeiboCommentNum1Xpath()).list().get(1);
            String likeNum = Xsoup.select(html, PropertiesUtil.getWeibolikeNumXpath()).list().get(0);
            WeiboTopArticle weiboTopArticle = new WeiboTopArticle(title, intro, readNum, discussNum, nickName, CommonUtil.getNowTime(),  content.replaceAll("[^A-Za-z0-9 \\u4e00-\\u9fa5]", ","), commentNum, likeNum);
            weiboTopArticle.setUrl(url);
            System.out.println(weiboTopArticle.toString());
            return weiboTopArticle;
        }
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

    }
}
