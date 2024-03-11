package com.example.demo.downloader;

import com.example.demo.module.DownloaderTask;
import com.example.demo.util.SiteUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CommDownloader {

    //每个站点维护一个HttpClient对象
    public Map<String, HttpClient> clients = new HashMap<>();

    /**
     * 开始下载
     * @param task
     */
    public synchronized HttpResponse doDownload(DownloaderTask task){
        try {
            System.out.println("task:"+task.getUrl()+"start to download");
            String url = task.getUrl();
            String domainName = SiteUtil.getDomainName(url);
            HttpClient client = getClient(domainName);
            System.out.println(client.toString());
            HttpGet get = new HttpGet(url);
            get.setHeader("Content-Type", "application/json;charset=UTF-8");
            HttpResponse resp = client.execute(get);
            return resp;
        } catch (Exception e) {
            //网络失败
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 通过url得到客户端对象
     * @param url
     * @return
     */
    private HttpClient getClient(String url){
        String domainName = SiteUtil.getDomainName(url);
        HttpClient client = clients.get(domainName);
        if(client == null){
            client = HttpClientBuilder.create().build();
            clients.put(domainName,client);
        }
        return client;
    }
}
