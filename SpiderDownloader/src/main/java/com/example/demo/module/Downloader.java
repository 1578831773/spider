package com.example.demo.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Downloader {
    private ExecutorService threadPool;
    private Schedule schedule = Schedule.getInstance();

    public void start(){
        startTheadPool();
        while(true){
            DownloaderTask downloaderTask = schedule.getTask();
            if(downloaderTask != null){
                threadPool.execute(downloaderTask);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("下载器运行出错");
            }
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
