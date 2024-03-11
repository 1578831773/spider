package com.example.demo.bean;

import lombok.Data;

@Data
public class Record {
    private int bootId;
    private int userId;
    private String bootUrl;
    private String createTime;
    private String updateTime;
    private int count;
    private int templateId;
    private int status;

    public Record() {
    }

    public Record(String bootUrl, String templateId) {
        this.bootUrl = bootUrl;
        this.templateId = Integer.parseInt(templateId);
    }
}
