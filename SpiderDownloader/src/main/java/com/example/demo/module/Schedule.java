package com.example.demo.module;

import com.example.demo.util.GetBeanUtil;
import com.example.demo.util.RedisUtil;
import com.example.demo.util.UrlUtil;

public class Schedule {

    private static Schedule instance = null;

    public static Schedule getInstance(){
        if(instance != null){
            return instance;
        }
        synchronized (Schedule.class){
            if(instance == null){
                instance = new Schedule();
            }
            return instance;
        }
    }

    private Schedule(){
    }

    private static RedisUtil redisUtil = null;

    public DownloaderTask getTask(){
        if(redisUtil == null){
            redisUtil = GetBeanUtil.getBean(RedisUtil.class);
        }
        //使用redis存储待爬取的记录号，key:future_recordId,值格式:recordId_template
        String value = redisUtil.getFirst("future_recordId");
        if(value == null) return null;
        System.out.println("get recordId:" + value);
        String[] values = value.split("_");
        String recordId = values[0];
        String template = values[1];
        String comboUrl = redisUtil.getFirst(recordId + "_future_urls");
        redisUtil.delFirst(recordId + "_future_urls", comboUrl);
        System.out.println("get comboUrl" + comboUrl);
        String url = UrlUtil.extractUrl(comboUrl);
        int level = UrlUtil.extractLevel(comboUrl);
        if(level == 1){

            redisUtil.delFirst("future_recordId", value);
        }
        if(level == 2){
            redisUtil.deSpiderNum(recordId);
            String spiderNum = (String)redisUtil.get(recordId);
            if(spiderNum.equals("0")){
                System.out.println("del recordId " + value);
                redisUtil.delFirst("future_recordId", value);
            }
        }
        DownloaderTask downloaderTask = GetBeanUtil.getBean(DownloaderTask.class);
        downloaderTask.setRecordId(recordId);
        downloaderTask.setTemplateId(template);
        downloaderTask.setUrl(url);
        downloaderTask.setLevel(level);
        return downloaderTask;
    }
}
