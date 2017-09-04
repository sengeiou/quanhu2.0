package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * 卡券model
 */
public class Ticket implements Serializable {

    private String voucherId;
    //转发券状态 0 可用 1 已使用 2已过期
    private int status;
    //券活动类型 0:转发 1:广告 2:打赏 3:红包
    private int voucherType;
    //卡券名称
    private String voucherTypeName;
    //券名称
    private String voucherDesc;
    //券金额
    private String cost;
    //领取时间 long
    private String receiveTime;
    //使用时间 long
    private String useTime;
    //过期时间 long
    private String expireTime;

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(int voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherDesc() {
        return voucherDesc;
    }

    public void setVoucherDesc(String voucherDesc) {
        this.voucherDesc = voucherDesc;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }
}
