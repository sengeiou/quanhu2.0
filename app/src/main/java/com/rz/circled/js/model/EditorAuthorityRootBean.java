package com.rz.circled.js.model;

import java.io.Serializable;

/**
 * Created by Gsm on 2017/8/31.
 */
public class EditorAuthorityRootBean implements Serializable {

    //允许推广标识（0；不允许；1：允许）
    private int allowGeneralizeFlag = -1;
    //允许分享标识（0；不允许；1：允许）
    private int allowShareFlag = -1;
    //内容价格
    private int contentPrice = 1;
    //私圈Id
    private String coterieId;

    private EditorAuthorityPriceBean contentPriceData;//价格

    public int getAllowGeneralizeFlag() {
        return allowGeneralizeFlag;
    }

    public void setAllowGeneralizeFlag(int allowGeneralizeFlag) {
        this.allowGeneralizeFlag = allowGeneralizeFlag;
    }

    public int getAllowShareFlag() {
        return allowShareFlag;
    }

    public void setAllowShareFlag(int allowShareFlag) {
        this.allowShareFlag = allowShareFlag;
    }

    public int getContentPrice() {
        return contentPrice;
    }

    public void setContentPrice(int contentPrice) {
        this.contentPrice = contentPrice;
    }

    public String getCoterieId() {
        return coterieId;
    }

    public void setCoterieId(String coterieId) {
        this.coterieId = coterieId;
    }

    public EditorAuthorityPriceBean getContentPriceData() {
        return contentPriceData;
    }

    public void setContentPriceData(EditorAuthorityPriceBean contentPriceData) {
        this.contentPriceData = contentPriceData;
    }
}
