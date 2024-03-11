package com.example.demo.module;

import com.example.demo.util.SiteUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 下载器
 */
public class DownLoader {
    //饿汉
    private static DownLoader instance = new DownLoader();

    public static DownLoader getInstance(){
        return instance;
    }

    //每个站点维护一个HttpClient对象
    public Map<String, HttpClient> clients = new HashMap<>();

    private DownLoader(){}

    /**
     * 开始下载
     * @param task
     */
    public HttpResponse doDownload(SpiderTask task){
        String url = task.getUrl();
        String domainName = SiteUtil.getDomainName(url);
        HttpClient client = getClient(domainName);
        HttpGet get = new HttpGet(url);
        get.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            HttpResponse resp = client.execute(get);
            return resp;
        } catch (IOException e) {
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

