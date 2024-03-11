package com.example.demo.downloader;

import com.example.demo.module.MyChromeDriver;
import com.example.demo.util.PropertiesUtil;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;
import us.codecraft.xsoup.Xsoup;

import java.util.ArrayList;
import java.util.List;

@Component
public class SohuDownloader {
    private static ChromeDriver chromeDriver = MyChromeDriver.getChromeDriver();
    public synchronized String doDownloadForLevel1(String url, int spiderNum){
        chromeDriver.get(url);
        String html = chromeDriver.getPageSource();
        String urlXpath = PropertiesUtil.getUrlXpath(url, 1);
        List<String> urls1 =  Xsoup.select(html, urlXpath).list();
        List<String> urls2 = new ArrayList<>();
        for(String url0: urls1){
            System.out.println(url0);
            if(url0.startsWith("http")){
                urls2.add(url0);
            }else if(url0.startsWith("//")){
                urls2.add("http:" + url0);
            }
        }
        int size = urls1.size();
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
                html = chromeDriver.getPageSource();
                chromeDriver.close();
                return html;
            }
        }
        html = chromeDriver.getPageSource();
        return html;
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
