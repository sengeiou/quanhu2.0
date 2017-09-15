package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class RewardModel implements Serializable {


    /**
     * offerId : 169
     * custId : 5dx3jdt8c6
     * contentSource : [{"text":"龙梅子"},{"image":"https://cdn.yryz.com/pic/activity/83c6a46b-3c3d-4681-a199-2a470b0c3729.jpg"}]
     * content : 龙梅子测试
     * imgUrl : https://cdn.yryz.com/pic/activity/83c6a46b-3c3d-4681-a199-2a470b0c3729.jpg
     * price : 2
     * duration : 1
     * complete : 1
     * orderId : 2017091410580074
     * payedFlag : 1
     * terminalTime : 2017-09-16 00:00:00
     * location :
     * replyNum : 3
     * createTime : 2017-09-14 18:53:55
     * updateTime : 2017-09-14 20:50:11
     * isComplete : 0
     */

    private int offerId;
    private String custId;
    private String contentSource;
    private String content;
    private String imgUrl;
    private int price;
    private int duration;
    private int complete;
    private String orderId;
    private int payedFlag;
    private String terminalTime;
    private String location;
    private int replyNum;
    private String createTime;
    private String updateTime;
    private int isComplete;

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getContentSource() {
        return contentSource;
    }

    public void setContentSource(String contentSource) {
        this.contentSource = contentSource;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getPayedFlag() {
        return payedFlag;
    }

    public void setPayedFlag(int payedFlag) {
        this.payedFlag = payedFlag;
    }

    public String getTerminalTime() {
        return terminalTime;
    }

    public void setTerminalTime(String terminalTime) {
        this.terminalTime = terminalTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }
}
