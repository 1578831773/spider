package com.example.demo.bean;

import lombok.Data;

@Data
public class SohuArticle extends Article{
    private String url;
    private String title;
    private String publishTime;
    private String tags;
    private String count;
    public SohuArticle(){super();}
    public SohuArticle(int recordId, String url){
        super();
        this.setBootId(recordId);
        this.url = url;
    }
}
