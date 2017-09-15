package com.rz.httpapi.bean;

/**
 * 作者：Administrator on 2016/8/20 0020 16:57
 * 功能：发送验证码返回值
 * 说明：
 */
public class RegisterModel {
    public String veriCode;
    //功能码
    public String code;
    //手机号
    public String phone;
    //备注: 当check=1表示正确，check=0表示错误
    public String check;

    @Override
    public String toString() {
        return "RegisterModel{" +
                "veriCode='" + veriCode + '\'' +
                ", code='" + code + '\'' +
                ", phone='" + phone + '\'' +
                ", check='" + check + '\'' +
                '}';
    }
}
