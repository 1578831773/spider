package com.example.demo.bean;

import lombok.Data;

@Data
public class CommArticle extends Article{
    private String url;
    public CommArticle(){super();}
    public CommArticle(int recordId, String url){
        super();
        this.setBootId(recordId);
        this.url = url;
    }
}
