package com.example.demo.module;

import com.example.demo.enums.SpiderConstants;
import com.example.demo.service.RedisService;
import com.example.demo.util.SiteUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 爬虫客户端
 */
public class SpiderClient {
    private ExecutorService threadPool;
    //初始化站点方法
    private Scheduler sch = Scheduler.getInstance();


    //root url 地址
    private String bootUrl;
    private int bootId;
    private int templateId;
    private int spiderNum;

    //domainname:www.sohu.com
    private String domainName;
    private String xpath;
    //链接级别
    private int level;

    public SpiderClient(String bootUrl, int level, int bootId, int templateId, int spiderNum){
        this.bootUrl = bootUrl;
        this.level = level;
        this.domainName = SiteUtil.getDomainName(bootUrl);
        this.bootId = bootId;
        this.templateId = templateId;
        this.spiderNum = spiderNum;
        //预处理地址
        preprocess(bootUrl + "#" + level);
    }

    /**
     * 预处理地址
     */
    private void preprocess(String combUrl) {
        if(RedisService.existsInFutures(bootId, combUrl)
                || RedisService.existsInOks(bootId, combUrl)
                || RedisService.existsDownloadings(bootId, combUrl)){
            return;
        }
        if(RedisService.existsInFails(bootId, combUrl)){
            int count = RedisService.getFailCounts(bootId, combUrl);
            if(count < SpiderConstants.SPIDER_FAILURE_RETRIES){
                RedisService.addToFutures(bootId, combUrl);
                return;
            }
        }
        RedisService.addToFutures(bootId, combUrl);
    }

    public void start(){
        startTheadPool();
        RedisService.setSpiderNum(bootId, spiderNum);
        while(true){
            try{
                SpiderTask task = sch.getTask(bootUrl, bootId, templateId);
                if(task != null){
                    if(task.getLevel() == 1){
                        task.setSpiderNum(spiderNum);
                    }
                    //execute(异步) submit(同步执行)
                    threadPool.execute(task);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                if(RedisService.ifSpiderBootIdExists(bootId)){
//                    if(RedisService.getSpiderNum(bootId) <= 0){
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        RedisService.delKey(String.valueOf(bootId));
//                        break;
//                    }
//                }
                    Object num = RedisService.getSpiderNumber(bootId);
                    if(num != null){
                        if(Integer.parseInt((String)num) <= 0) break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                threadPool.shutdownNow();
            }

        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
            threadPool.shutdownNow();
        }

    }

    /**
     * 启动线程池
     */
    private void startTheadPool(){
        //创建指定个数的线程池
        threadPool = Executors.newFixedThreadPool(8);
    }
}
