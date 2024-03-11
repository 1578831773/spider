package com.example.demo.util;

/**
 * 站点工具类
 */
public class SiteUtil {
    /**
     * 通过url地址抽取域名
     */
    public static String getDomainName(String url){
        String[] arr = url.split("[\\.\\/]");
        for(int i = 0;i < arr.length; i ++){
            if(arr[i].equalsIgnoreCase("com")
                    || arr[i].equalsIgnoreCase("net")
                    || arr[i].equalsIgnoreCase("org")){
                return arr[i - 1] + "." +arr[i];
            }
        }
        return null;
    }
}
