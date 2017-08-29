package com.rz.httpapi.bean;

/**
 * Created by JS01 on 2016/7/12.
 * QQ,微信，微博，第三方数据
 */
public class SnsLoginBean {

    /**
     * token
     */
    public String accessToken;
    public String platform;
    public String openId;
    /**
     * 用户昵称
     */
    public String userName;
    public String userAvatar;


    @Override
    public String toString() {
        return "SnsLoginModel{" +
                "token='" + accessToken + '\'' +
                ", platform='" + platform + '\'' +
                ", openId='" + openId + '\'' +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                '}';
    }
}
