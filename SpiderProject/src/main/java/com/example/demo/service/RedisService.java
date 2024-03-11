package com.example.demo.service;


import com.example.demo.util.CommonUtil;
import com.example.demo.util.RedisUtil0;
import com.example.demo.util.UrlUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RedisService {
    private static final String FUTURE_URLS = "future_urls";
    private static final String OK_URLS = "ok_urls";
    private static final String FAIL_URLS = "fail_urls";
    private static final String DOWNLOADING_URLS = "downloading_urls";


    private static RedisUtil0 redisUtil0 = new RedisUtil0();

    /**
     * 添加到将来集合
     * sorted_set：不重复，有序
     * 以系统时间作为score，实现队列的效果，并且去重
     * 集合中的key是http://www.sohu.com/1/1.html#n，n表示链接级别
     */
    public static void addToFutures(int bootId, String url, int n){
        String key = getKeyName(bootId, FUTURE_URLS);
        redisUtil0.zset(key, url + "#" + n, System.currentTimeMillis());
    }

    /**
     * 操作组合url
     * @param combUrl
     */
    public static void addToFutures(int bootId, String combUrl){
        String url = UrlUtil.extractUrl(combUrl);
        int level = UrlUtil.extractLevel(combUrl);
        addToFutures(bootId, url, level);
    }


    /**
     * 添加到ok集合
     * Set：不重复，无序
     */
    public static void addToOks(int bootId, String comboUrl){
        String url = UrlUtil.extractUrl(comboUrl);
        int level = UrlUtil.extractLevel(comboUrl);
        String key = getKeyName(bootId, OK_URLS);
        redisUtil0.sSet(key, comboUrl);
    }
    public static void addToOks(int bootId, String url, int level){
        addToOks(bootId,url + "#" + level);
    }

    /**
     * 在失败结合中累加1
     * hash：key(url) - value(次数)
     */
    public static double incrFails(int bootId, String comboUrl){
        String url = UrlUtil.extractUrl(comboUrl);
        int level = UrlUtil.extractLevel(comboUrl);
        String key = getKeyName(bootId, FAIL_URLS);
        return redisUtil0.hincr(key, comboUrl, 1);
    }
    public static double incrFails(int bootId, String url, int level){
        return incrFails(bootId, url + "#" + level);
    }

    /**
     * 添加到下载集合
     * Set：不重复，无序
     */
    public static void addToDownloadings(int bootId, String comboUrl){
        String url = UrlUtil.extractUrl(comboUrl);
        int level = UrlUtil.extractLevel(comboUrl);
        String key = getKeyName(bootId, DOWNLOADING_URLS);
        redisUtil0.sSet(key, comboUrl);

    }
    public static void addToDownloadings(int bootId, String url,int level){
        addToDownloadings(bootId, url + "#" + level);

    }

    /**
     *从将来集中剪切出下一个comboUrl地址
     */
    public static String getNextUrl(int bootId){
        String key = getKeyName(bootId, FUTURE_URLS);
        String rel = redisUtil0.getFirst(key);
        if(rel != null){
            redisUtil0.delFirst(key, rel);
        }
        return rel;
    }

    /**
     * 获得key的名称
     */
    public static String getKeyName(int bootId, String keyType){
//        String domain = SiteUtil.getDomainName(url);
        return bootId + "_" + keyType;
    }

    /**
     * 判断将来集是否存在指定url
     */
    public static boolean existsInFutures(int bootId, String comboUrl){
        String url = UrlUtil.extractUrl(comboUrl);
        int level = UrlUtil.extractLevel(comboUrl);
        String key = getKeyName(bootId, FUTURE_URLS);
        return redisUtil0.zisExits(key, comboUrl);
    }
    public static boolean existsInFutures(int bootId, String url, int level){
        return existsInFutures(bootId,url + "#" + level);
    }


    /**
     * 判断ok集中是否存在指定url
     */
    public static boolean existsInOks(int bootId, String comboUrl){
        String url = UrlUtil.extractUrl(comboUrl);
        int level = UrlUtil.extractLevel(comboUrl);
        String key = getKeyName(bootId, OK_URLS);
        return redisUtil0.sHasKey(key, comboUrl);
    }
    public static boolean existsInOks(int bootId, String url, int level){
        return existsInOks(bootId,url + "#" + level);
    }


    /**
     * 判断失败集是否存在指定url
     */
    public static boolean existsInFails(int bootId, String comboUrl){
        String url = UrlUtil.extractUrl(comboUrl);
        int level = UrlUtil.extractLevel(comboUrl);
        String key = getKeyName(bootId, FAIL_URLS);
        return redisUtil0.hHasKey(key, comboUrl);
    }
    public static boolean existsInFails(int bootId, String url, int level){
        return existsInFails(bootId, url + "#" + level);
    }

    /**
     * 判断下载集是否存在指定url
     */
    public static boolean existsDownloadings(int bootId, String comboUrl){
        String url = UrlUtil.extractUrl(comboUrl);
        int level = UrlUtil.extractLevel(comboUrl);
        String key = getKeyName(bootId, DOWNLOADING_URLS);
        return redisUtil0.sHasKey(key, comboUrl);
    }
    public static boolean existsDownloadings(int bootId, String url, int level){
        return existsDownloadings(bootId, url + "#" + level);
    }

    /**
     * 获取指定url的下载失败次数
     */
    public static int getFailCounts(int bootId, String comboUrl) {
        String key = getKeyName(bootId, FAIL_URLS);
        String count = (String)redisUtil0.hget(key, comboUrl);
        return (count == null) ? 0 : Integer.parseInt(count);
    }
    public static int getFailCounts(int bootId, String url, int level) {
        return getFailCounts(bootId, url + "#" + level);
    }

    /**
     * 从下载集中删除url
     * @param url
     */
    public static void delFromDownloadings(int bootId, String url, int level) {
        String key = getKeyName(bootId, DOWNLOADING_URLS);
        redisUtil0.setRemove(key, url + "#" + level);
    }

    public static void setUrlToBootId(String bootUrl, int bootId){
        redisUtil0.set(bootUrl,String.valueOf(bootId));
    }
    public static int getBootId(String bootUrl){
        return Integer.parseInt((String)redisUtil0.get(bootUrl));
    }

    public static void setSpiderNum(int bootId, int num){
        redisUtil0.set(String.valueOf(bootId), String.valueOf(num));
    }
    public static int getSpiderNum(int bootId){
        return Integer.parseInt((String)redisUtil0.get(String.valueOf(bootId)));
    }

    public static void deSpiderNum(int bootId){
        int num = getSpiderNum(bootId);
        redisUtil0.delSpiderNum(String.valueOf(bootId));
        redisUtil0.set(String.valueOf(bootId),String.valueOf(num-1));
    }

    public static void deSpiderNum(int bootId, int count){
        int num = getSpiderNum(bootId);
        redisUtil0.delSpiderNum(String.valueOf(bootId));
        redisUtil0.set(String.valueOf(bootId),String.valueOf(num-count));
    }

    public static boolean ifSpiderBootIdExists(int bootId){
        return redisUtil0.hasKey(String.valueOf(bootId));
    }

    public static Object getSpiderNumber(int bootId){
        return redisUtil0.getKey(String.valueOf(bootId));
    }

    public static void delKey(String key){
        redisUtil0.delSpiderNum(key);
    }

    public static void addFutureRecord(String recordId){
        redisUtil0.zset("future_recordId", recordId, System.currentTimeMillis());
    }

    public static List<Integer> getNums(int bootId){
        List<Integer> nums = new ArrayList<>();
        Object downloadNum = redisUtil0.get(String.valueOf(bootId));
        Object analyzeNum = redisUtil0.get("analyze_"+bootId);
        if(downloadNum == null){
            nums.add(0);
        }else{
            nums.add(Integer.parseInt((String)downloadNum));
        }
        if(analyzeNum == null){
            nums.add(0);
        }else{
            nums.add(Integer.parseInt((String)analyzeNum));
        }
        return nums;
    }
}
