package com.rz.httpapi.bean;

/**
 * Created by Administrator on 2017/6/8/008.
 */

public class BannerAddSubjectModel {

    /**
     * picUrl : 测试内容6692
     * url : 测试内容2eel
     * banner
     */

    private String picUrl;
    private String url;
    public String startTime;
    public String endTime;




    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "BannerAddSubjectModel{" +
                "picUrl='" + picUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
