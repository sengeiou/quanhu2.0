package com.yryz.yunxinim.uikit.model;

/**
 * Created by rzw2 on 2017/3/29.
 */

public class CircleTeamModel {
    //群Id
    private String tid;
    //群所属圈子ID
    private String appId;
    //群名称
    private String tname;
    //群主ID
    private String owner;
    //群图标
    private String icon;
    //群公告
    private String announcement;
    //群描述
    private String intro;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
