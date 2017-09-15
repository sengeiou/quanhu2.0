package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Gsm on 2017/9/4.
 */
public class RewardInfoBean implements Serializable {
    private String custId;//*
    private String parentId;
    private String authorId;//*
    private String appId;//*
    private String moduleId;//*
    private String infoId;
    private String infoTitle;
    private String infoDesc;
    private String infoThumbnail;
    private String infoPic;
    private String infoVideo;
    private String infoVideoPic;
    public long price;//*
    private String custNname;
    private int id;
    private String opusId;
    private String circleName;
    private String circleUrl;
    private String infoCreateTime;
    private String resourceId;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getInfoTitle() {
        return infoTitle;
    }

    public void setInfoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
    }

    public String getInfoDesc() {
        return infoDesc;
    }

    public void setInfoDesc(String infoDesc) {
        this.infoDesc = infoDesc;
    }

    public String getInfoThumbnail() {
        return infoThumbnail;
    }

    public void setInfoThumbnail(String infoThumbnail) {
        this.infoThumbnail = infoThumbnail;
    }

    public String getInfoPic() {
        return infoPic;
    }

    public void setInfoPic(String infoPic) {
        this.infoPic = infoPic;
    }

    public String getInfoVideo() {
        return infoVideo;
    }

    public void setInfoVideo(String infoVideo) {
        this.infoVideo = infoVideo;
    }

    public String getInfoVideoPic() {
        return infoVideoPic;
    }

    public void setInfoVideoPic(String infoVideoPic) {
        this.infoVideoPic = infoVideoPic;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getCustNname() {
        return custNname;
    }

    public void setCustNname(String custNname) {
        this.custNname = custNname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpusId() {
        return opusId;
    }

    public void setOpusId(String opusId) {
        this.opusId = opusId;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getCircleUrl() {
        return circleUrl;
    }

    public void setCircleUrl(String circleUrl) {
        this.circleUrl = circleUrl;
    }

    public String getInfoCreateTime() {
        return infoCreateTime;
    }

    public void setInfoCreateTime(String infoCreateTime) {
        this.infoCreateTime = infoCreateTime;
    }

    @Override
    public String toString() {
        return "TransferModule{" +
                "custId='" + custId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", authorId='" + authorId + '\'' +
                ", appId='" + appId + '\'' +
                ", moduleId='" + moduleId + '\'' +
                ", infoId='" + infoId + '\'' +
                ", infoTitle='" + infoTitle + '\'' +
                ", infoDesc='" + infoDesc + '\'' +
                ", infoThumbnail='" + infoThumbnail + '\'' +
                ", infoPic='" + infoPic + '\'' +
                ", infoVideo='" + infoVideo + '\'' +
                ", infoVideoPic='" + infoVideoPic + '\'' +
                ", price=" + price +
                ", custNname='" + custNname + '\'' +
                ", id=" + id +
                ", opusId='" + opusId + '\'' +
                '}';
    }

}
