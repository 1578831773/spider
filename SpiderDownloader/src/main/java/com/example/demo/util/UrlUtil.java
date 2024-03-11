package com.example.demo.util;

/**
 * url工具类
 */
public class UrlUtil {
    /**
     *抽取url地址
     */
    public static String extractUrl(String comboUrl){
        return comboUrl.substring(0, comboUrl.lastIndexOf("#"));
    }

    /**
     * 抽取url中的级别
     */
    public static int extractLevel(String comboUrl){
        return Integer.parseInt(comboUrl.substring(comboUrl.lastIndexOf("#") + 1));
    }
}
