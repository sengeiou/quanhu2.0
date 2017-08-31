package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * 打赏礼物model
 */
public class TransferBean implements Serializable {
    /**
     * 礼物ID
     */
    private String giftid;

    /**
     * 礼物图标
     */
    private String img;

    /**
     * 礼物价格（分）
     */
    private String price;

    /**
     * 礼物昵称
     */
    private String name;

    private String icon;

    public int imgResourceId;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGiftid() {
        return giftid;
    }

    public void setGiftid(String giftid) {
        this.giftid = giftid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
