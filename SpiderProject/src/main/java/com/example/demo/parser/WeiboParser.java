package com.example.demo.parser;
import com.example.demo.bean.WeiboArticle;
import com.example.demo.bean.WeiboTopArticle;
import com.example.demo.util.CommonUtil;
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


public class WeiboParser{
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

    /**
     * 进行爬取
     * @param key　用于获取正确的微博链接：http://s.weibo.com/weibo/%25E9%2598%259A%25E6%25B8%2585%25E5%25AD%2590%25E5%259B%259E%25E5%25BA%2594%25E5%2588%2586%25E6%2589%258B%25E4%25BC%25A0%25E9%2597%25BB
     */
    public void search(String key) throws InterruptedException {
        chromeDriver.get("http://s.weibo.com/");
        WebElement elementKey = chromeDriver.findElement(By.className("search-input"));
        elementKey.sendKeys(key);
        WebElement elementClick = chromeDriver.findElement(By.className("s-btn-b"));

        elementClick.click();

//        //搜索特定日期的微博内容
//        LocalDate localDate = LocalDate.now();
//        String currentUrl = webDriver.getCurrentUrl().split("&")[0];
//        System.out.println("currentUrl: " + currentUrl);
//        String url = currentUrl + "&typeall=1&suball=1&timescope=custom:" + localDate + ":&Refer=g";
//        webDriver.get(url);

        //处理当前页面内容
        handlePage();
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
        chromeDriver.close();
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
        chromeDriver.close();
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

    //页面处理
    public void handlePage(){
        while (true){
            //sleep的作用是对付微博的反爬虫机制，抓取太快可能会判定为机器人，需要输入验证码
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //先判断是否有内容
            if (checkContent()){
                getContent();
                //判断是否有下一页按钮
                if (checkButton()){
                    //拿到下一页按钮
                    WebElement elementButton = chromeDriver.findElement(By.xpath("//a[@class='page next S_txt1 S_line1']"));
                    elementButton.click();
                }else {
                    System.out.println("没有下一页");
                    break;
                }
            }else {
                System.out.println("内容搜索完毕");
                break;
            }

        }
    }

    /**
     * 检查页面是否还有内容
     * @return　
     */
    public Boolean checkContent(){
        boolean flag;
        try {
            chromeDriver.findElement(By.xpath("//div[@class='pl_noresult']"));
            flag = false;
        }catch (Exception e){
            flag = true;
        }
        return flag;
    }

    /**
     * 检查是否有下一页
     * @return
     */
    public Boolean checkButton(){
        boolean flag;
        try {
            chromeDriver.findElement(By.xpath("//a[@class='page next S_txt1 S_line1']"));
            flag = true;
        }catch (Exception e){
            flag = false;
        }
        return flag;
    }


    public void getContent(){
        List<WebElement> elementNodes = chromeDriver.findElements(By.xpath("//div[@class='WB_cardwrap S_bg2 clearfix']"));
        //在运行过程中微博数==0的情况，可能是微博反爬机制，需要输入验证码
        if (elementNodes == null){
            String url = chromeDriver.getCurrentUrl();
            chromeDriver.get(url);
            getContent();
            return;
        }
        for (WebElement element : elementNodes){
            String bz_name = element.findElement(By.xpath(".//div[@class='feed_content wbcon']/a[@class='W_texta W_fb']")).getText();
            System.out.println("博主昵称：　" + bz_name);

            String bz_homePage = element.findElement(By.xpath(".//div[@class='feed_content wbcon']/a[@class='W_texta W_fb']")).getAttribute("href");
            System.out.println("博主主页：　" + bz_homePage);

            String wb_approve;
            try {
                wb_approve = element.findElement(By.xpath(".//div[@class='feed_content wbcon']/a[@class='approve_co']")).getAttribute("title");
            }catch (Exception e){
                wb_approve = "";
            }
            System.out.println("微博认证：　" + wb_approve);

            String wb_intelligent;
            try {
                wb_intelligent = element.findElement(By.xpath(".//div[@class='feed_content wbcon']/a[@class='ico_club']")).getAttribute("title");
            }catch (Exception e){
                wb_intelligent = "";
            }
            System.out.println("微博达人：　" + wb_intelligent);

            String wb_content;
            try {
                wb_content = element.findElement(By.xpath(".//div[@class='feed_content wbcon']/p[@class='comment_txt']")).getText();
            }catch (Exception e){
                wb_content = "";
            }
            System.out.println("微博内容：　" + wb_content);

            String publishTime;
            try {
                publishTime = element.findElement(By.xpath(".//div[@class='feed_from W_textb']/a[@class='W_textb']")).getText();
            }catch (Exception e){
                publishTime = "";
            }
            System.out.println("发布时间：　" + publishTime);

            String wb_address;
            try {
                wb_address = element.findElement(By.xpath(".//div[@class='feed_from W_textb']/a[@class='W_textb']")).getAttribute("href");
            }catch (Exception e){
                wb_address = "";
            }
            System.out.println("微博地址：　" + wb_address);

            String wb_source;
            try {
                wb_source = element.findElement(By.xpath(".//div[@class='feed_from W_textb']/a[@rel]")).getText();
            }catch (Exception e){
                wb_source = "";
            }
            System.out.println("微博来源：　" + wb_source);

            String transmitText;
            int transmitNum = 0;
            try {
                transmitText = element.findElement(By.xpath(".//a[@action-type='feed_list_forward']//em")).getText();
                transmitNum = Integer.parseInt(transmitText);
            }catch (Exception e){

            }
            System.out.println("转发次数：　" + transmitNum);

            int commentNum = 0;
            try {
                String commentText = element.findElement(By.xpath(".//a[@action-type='feed_list_comment']//em")).getText();
                commentNum = Integer.parseInt(commentText);
            }catch (Exception e){

            }
            System.out.println("评论次数：　" + commentNum);

            int praiseNum = 0;
            try {
                String praiseText = element.findElement(By.xpath(".//a[@action-type='feed_list_like']//em")).getText();
                praiseNum = Integer.parseInt(praiseText);
            }catch (Exception e){

            }
            System.out.println("点赞次数：　" + praiseNum);

            System.out.print("-----------------------------------------------------------");
            System.out.println();
        }
    }

}
