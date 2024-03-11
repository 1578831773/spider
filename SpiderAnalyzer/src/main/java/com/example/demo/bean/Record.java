package com.example.demo.bean;

public class Record {
    private int bootId;
    private int userId;
    private String bootUrl;
    private String createTime;
    private String updateTime;
    private int count;
    private int templateId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public Record() {
    }

    public Record(String bootUrl, String templateId) {
        this.bootUrl = bootUrl;
        this.templateId = Integer.parseInt(templateId);
    }

    public int getBootId() {
        return bootId;
    }

    public void setBootId(int bootId) {
        this.bootId = bootId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getBootUrl() {
        return bootUrl;
    }

    public void setBootUrl(String bootUrl) {
        this.bootUrl = bootUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Record{" +
                "bootId=" + bootId +
                ", userId=" + userId +
                ", bootUrl='" + bootUrl + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", count=" + count +
                ", templateId=" + templateId +
                '}';
    }
}
