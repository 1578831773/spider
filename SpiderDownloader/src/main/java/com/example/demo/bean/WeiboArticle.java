package com.example.demo.bean;

import lombok.Data;

@Data
public class WeiboArticle extends Article{
    private String url;
    private String nickName;
    private String publishTime;
    private String commentNum;
    private String likeNum;
    public WeiboArticle(){
        super();
    }
    public WeiboArticle(int bootId, String url){
        super();
        this.url = url;
        this.setBootId(bootId);
    }
    public WeiboArticle(String nickName, String publishTime, String content, String commentNum, String likeNum) {
        super();
        this.nickName = nickName;
        this.publishTime = publishTime;
        this.commentNum = commentNum;
        this.likeNum = likeNum;
        setContent(content);
    }
}
