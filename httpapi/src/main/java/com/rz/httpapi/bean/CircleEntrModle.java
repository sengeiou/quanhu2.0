package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class CircleEntrModle implements Serializable{

//
//    “appId”:””,
//            “circleName”:””,
//            “circleIcon”:””,
//            “circleIntro”:””,
//            “status”:0,
//            “createTime”:5412211
//}]

    public String appId;
    public String circleName;
    public String circleIcon;
    public String circleIntro;
    public String circleUrl;
    public int status;
    public String createTime;
    public int isRecommend;
    public boolean isSeleced;
    public int click;


    public int id;
    public String appSecret;
    public int weight;
    public String operational;
    public String adminUrl;
    public String updateTime;
    public int appType;




    public boolean isSeleced() {
        return isSeleced;
    }

    public void setSeleced(boolean seleced) {
        isSeleced = seleced;
    }


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getCircleIcon() {
        return circleIcon;
    }

    public void setCircleIcon(String circleIcon) {
        this.circleIcon = circleIcon;
    }

    public String getCircleIntro() {
        return circleIntro;
    }

    public void setCircleIntro(String circleIntro) {
        this.circleIntro = circleIntro;
    }

    public String getCircleUrl() {
        return circleUrl;
    }

    public void setCircleUrl(String circleUrl) {
        this.circleUrl = circleUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getOperational() {
        return operational;
    }

    public void setOperational(String operational) {
        this.operational = operational;
    }

    public String getAdminUrl() {
        return adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }
}
