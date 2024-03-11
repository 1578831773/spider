package com.example.demo;

import com.example.demo.module.Downloader;
import com.example.demo.util.GetBeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        Downloader downloader = GetBeanUtil.getBean(Downloader.class);
        downloader.start();
    }

}
