package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageUploadConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("imgconfig");
        registry.addResourceHandler("/static/img/**").addResourceLocations("file:E:\\IdeaProjects\\myspider\\src\\main\\resources\\static\\img\\");
    }
}
