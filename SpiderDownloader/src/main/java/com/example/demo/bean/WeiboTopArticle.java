package com.example.demo.bean;

import lombok.Data;

@Data
public class WeiboTopArticle {
    private int weiboTopId;
    private String url;
    private String title;
    private String intro;
    private String readNum;
    private String discussNum;
    private int articleId;
    private WeiboArticle weiboArticle;

    public WeiboTopArticle(){
        super();
    }

    public WeiboTopArticle(String title, String intro, String readNum, String discussNum, String nickName, String publishTime, String content, String commentNum, String likeNum){
        super();
        this.title = title;
        this.intro = intro;
        this.readNum = readNum;
        this.discussNum = discussNum;
        weiboArticle = new WeiboArticle(nickName, publishTime, content, commentNum, likeNum);
    }
}
