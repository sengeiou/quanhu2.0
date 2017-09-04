package com.rz.circled.modle;

import java.io.Serializable;

/**
 *
 */
public class PicPublishModel implements Serializable {

    //图片id
    private int id;

    //图片地址
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PicPublishModel{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }
}
