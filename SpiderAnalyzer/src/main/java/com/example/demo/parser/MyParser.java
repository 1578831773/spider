package com.example.demo.parser;

import com.example.demo.util.GetBeanUtil;
import com.example.demo.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MyParser {
    private ExecutorService threadPool;

    @Autowired
    private RedisUtil redisUtil;

    public void start(){
        startTheadPool();
        while (true){
            if(redisUtil.hasKey("html")){
                String path = redisUtil.getFirst("html");
                if(path != null){
                    redisUtil.delFirst("html", path);
                    ParserTask parserTask = GetBeanUtil.getBean(ParserTask.class);
                    parserTask.setPath(path);
                    threadPool.execute(parserTask);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
