package com.example.demo.module;

import com.example.demo.service.RedisService;
import com.example.demo.util.GetBeanUtil;
import com.example.demo.util.SiteUtil;
import com.example.demo.util.UrlUtil;

/**
 * 调度器：单例模式
 * 从redis中取出待爬取的url地址，生成爬虫任务
 */
public class Scheduler {
    private static Scheduler instance = null;

    public static Scheduler getInstance(){
        if(instance != null){
            return instance;
        }
        synchronized (Scheduler.class){
            if(instance == null){
                instance = new Scheduler();
            }
            return instance;
        }
    }

    private Scheduler(){
    }

    /**
     * 获取爬虫任务
     */
    public SpiderTask getTask(String bootUrl, int bootId, int templateId){
        String domain = SiteUtil.getDomainName(bootUrl);
//        String comboUrl = RedisService.getNextUrl(domain);
        String comboUrl = RedisService.getNextUrl(bootId);
        if(comboUrl == null){
            return null;
        }

//        SpiderTask spiderTask = SpringUtil.getBean(SpiderTask.class);
        SpiderTask spiderTask = GetBeanUtil.getBean(SpiderTask.class);
        spiderTask.setUrl(UrlUtil.extractUrl(comboUrl));
        spiderTask.setLevel(UrlUtil.extractLevel(comboUrl));
        spiderTask.setBootId(bootId);
        spiderTask.setTemplateId(templateId);
        return spiderTask;
    }


}
