package com.example.demo.bean;

import lombok.Data;

@Data
public class LogInfo {
    private int logId;
    private int userId;
    private int kind;
    private String content;
    private String time;

    public LogInfo(int userId, int kind, String content, String time) {
        this.userId = userId;
        this.kind = kind;
        this.content = content;
        this.time = time;
    }
}
