package com.rz.httpapi.bean;

/**
 * Created by Administrator on 2017/6/8/008.
 */

public class FamousModel {

    /**
     * appId : 测试内容7t54
     * starDesc : 测试内容oh51
     * starImg : 测试内容dw7y
     * starName : 测试内容g9b7
     * starTag : 测试内容7j7q
     * url : 测试内容j18s
     */

    private String appId;
    private String starDesc;
    private String starImg;
    private String starName;
    private String starTag;
    private String url;

    @Override
    public String toString() {
        return "FamousModel{" +
                "appId='" + appId + '\'' +
                ", starDesc='" + starDesc + '\'' +
                ", starImg='" + starImg + '\'' +
                ", starName='" + starName + '\'' +
                ", starTag='" + starTag + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getStarDesc() {
        return starDesc;
    }

    public void setStarDesc(String starDesc) {
        this.starDesc = starDesc;
    }

    public String getStarImg() {
        return starImg;
    }

    public void setStarImg(String starImg) {
        this.starImg = starImg;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    public String getStarTag() {
        return starTag;
    }

    public void setStarTag(String starTag) {
        this.starTag = starTag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
