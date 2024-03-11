package com.example.demo.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MyChromeDriver {
    private static ChromeDriverService service;

    /**
     * 创建一个浏览器实例
     * @return　webDriver
     */

    public static WebDriver getWebDriver(){
        System.setProperty("webdriver.chrome.driver","/usr/bin/chromedriver.exe");
        //创建一个　ChromeDriver 接口
        service = new ChromeDriverService.Builder().usingDriverExecutable(new File("/usr/bin/chromedriver.exe")).usingAnyFreePort().build();
        try {
            service.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ChromeDriverService启动异常");
        }
        //创建一个　chrome 浏览器实例
        return new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
    }

    public static ChromeDriver getChromeDriver(){
        System.setProperty("webdriver.chrome.driver","/usr/bin/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        //首选项
        HashMap<String,Object> opts = new HashMap<String, Object>();
        //禁止加载图片
        opts.put("profile.managed_default_content_settings.images",2);
        //禁止cookies
        opts.put("profile.default_content_settings.cookies",2);
        options.setExperimentalOption("prefs",opts);
        options.addArguments("headless");
        options.addArguments("no-sandbox");
        options.addArguments("disable-dev-shm-usage");
//        options.addArguments("headless");
//    // 设置禁止加载项
//        Map<String, Object> prefs = new HashMap<String, Object>();
//    // 禁止加载js
//        prefs.put("profile.default_content_settings.javascript", 2); // 2就是代表禁止加载的意思
//        // 禁止加载css
//        prefs.put("profile.default_content_settings.images", 2); // 2就是代表禁止加载的意思
//        options.setExperimentalOption("prefs", prefs);
        ChromeDriver driver = new ChromeDriver(options);
        return driver;
    }
}
