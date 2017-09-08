package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class GroupBannerBean implements Serializable {
    private String picUrl;

    private String url;

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
}
