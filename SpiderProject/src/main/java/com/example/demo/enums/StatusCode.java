package com.example.demo.enums;

public enum StatusCode {
    Success(0,"成功"),

    UserAccountCannotBeNull(-4,"账号不能为空"),

    PasswordCannotBeNull(-5,"密码不能为空"),

    AccountNotExists(-6,"账号不存在"),

    PasswordError(-7,"密码错误"),

    AccountAlreadyExists(-8, "账号已存在"),

    RePasswordError(-9, "两次密码不一致"),

    NotLogin(-10,"请先登录"),

    NoAuthority(-11,"无权限访问"),

    ParamsError(-3, "参数错误"),

    BootUrlExists(-2, "种子已存在"),

    Fail(-1, " 失败");

    StatusCode() {
    }

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
