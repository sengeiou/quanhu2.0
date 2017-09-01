package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class CircleDynamic implements Serializable {

    public static class User implements Serializable {

        public String custId;
        public String custNname;//用户昵称

        public String nameNotes;//好友备注名称
        public String custImg;//用户头像地址

        public String custPhone;

        public String custSignature;

        public String custNameNote;

        @Override
        public String toString() {
            return "User{" +
                    "custId='" + custId + '\'' +
                    ", custNname='" + custNname + '\'' +
                    ", nameNotes='" + nameNotes + '\'' +
                    ", custImg='" + custImg + '\'' +
                    ", custPhone='" + custPhone + '\'' +
                    ", custSignature='" + custSignature + '\'' +
                    ", custNameNote='" + custNameNote + '\'' +
                    '}';
        }
    }

//    appId
//            String
//
//    圈子ID
//    moduleId	String	 	栏目Id
//    infoId	String	 	信息Id
//    circleName	String	 	圈子名称
//    circleUrl	String	 	圈子主页地址
//
//            infoTitle
//    String
//
//            信息标题
//    infoDesc	String	 	信息简介
//    infoThumbnail	String	 	信息缩略图
//    infoPic	String	 	图片列表，以逗号分隔
//    infoVideo	String	 	视频地址
//    infoVideoPic	String	 	视频预览图片
//    circleUrl	String	 	详情跳转地址，不带域名地址格式
//    reateTime	String	 	创建时间
//
//    leftIcon	String	 	左侧图标
//    leftText	String	 	左侧文本
//    rightIcon1	String
//    rightText1	String
//    rightIcon2	String
//    rightText2	String
//    rightIcon3	String
//    rightText3	String


    public User user;

    public String appId;
    //圈子ID
    public String circleName;
    //所属圈子名称（未完成）
    public String infoId;//信息Id

    public String moduleId;
    public String infoTitle;
    // 信息标题
    public String infoDesc;    //信息简介
    public String infoThumbnail;    //信息缩略图
    public String infoPic;    //图片列表，以逗号分隔
    public String infoVideo;    //视频地址
    public String infoVideoPic;//视频预览图片
    public String circleUrl;    //详情跳转地址，不带域名地址格式
    public String createTime;    //创建时间
    public String leftIcon;    //左侧图标
    public String leftText;    //左侧文本
    public String rightIcon1;
    public String rightText1;
    public String rightIcon2;
    public String rightText2;
    public String rightIcon3;
    public String rightText3;


    @Override
    public String toString() {
        return "CircleDynamic{" +
                "user=" + user +
                ", appId='" + appId + '\'' +
                ", circleName='" + circleName + '\'' +
                ", infoId='" + infoId + '\'' +
                ", moduleId='" + moduleId + '\'' +
                ", infoTitle='" + infoTitle + '\'' +
                ", infoDesc='" + infoDesc + '\'' +
                ", infoThumbnail='" + infoThumbnail + '\'' +
                ", infoPic='" + infoPic + '\'' +
                ", infoVideo='" + infoVideo + '\'' +
                ", infoVideoPic='" + infoVideoPic + '\'' +
                ", circleUrl='" + circleUrl + '\'' +
                ", createTime='" + createTime + '\'' +
                ", leftIcon='" + leftIcon + '\'' +
                ", leftText='" + leftText + '\'' +
                ", rightIcon1='" + rightIcon1 + '\'' +
                ", rightText1='" + rightText1 + '\'' +
                ", rightIcon2='" + rightIcon2 + '\'' +
                ", rightText2='" + rightText2 + '\'' +
                ", rightIcon3='" + rightIcon3 + '\'' +
                ", rightText3='" + rightText3 + '\'' +
                '}';
    }
}
