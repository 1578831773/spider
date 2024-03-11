package com.example.demo.bean;

import lombok.Data;

@Data
public class UserInfo extends UserAccount {
    private String userName;
    private String sex;
    private String headPic;
}
