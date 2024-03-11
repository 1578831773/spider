package com.example.demo.bean;

import lombok.Data;

@Data
public class Template {
    private int templateId;
    private String templateName;
    private String desc;
    private String addTime;
    private int useCount;
    private int status;
}
