package com.example.demo.bean;

import lombok.Data;

@Data
public class UserAccount {
    private int userId;
    private String account;
    private String password;
    private int authority;
}
