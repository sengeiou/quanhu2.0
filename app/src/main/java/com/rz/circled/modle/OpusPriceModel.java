package com.rz.circled.modle;

/**
 * 作者：Administrator on 2016/11/21 0021 14:22
 * 功能：转发作品定价
 * 说明：
 */
public class OpusPriceModel {

    //唯一标识
    private String uid;
    //单价
    private String price;
    //类型
    private String priceType;
    //状态
    private String priceStatus;
    //创建时间
    private String createTime;

    private boolean showAllBtn;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getPriceStatus() {
        return priceStatus;
    }

    public void setPriceStatus(String priceStatus) {
        this.priceStatus = priceStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isShowAllBtn() {
        return showAllBtn;
    }

    public void setShowAllBtn(boolean showAllBtn) {
        this.showAllBtn = showAllBtn;
    }
}
