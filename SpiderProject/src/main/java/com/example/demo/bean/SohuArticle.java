package com.example.demo.bean;

public class SohuArticle extends Article{
    private String url;
    private String title;
    private String publishTime;
    private String tags;
    private String count;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private int articleId;

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "SohuArticle{" +
                "title='" + title + '\'' +
                ", publishTime='" + publishTime + '\'' +
                ", tags='" + tags + '\'' +
                ", count='" + count + '\'' +
                ", articleId=" + articleId +
                '}';
    }
}
