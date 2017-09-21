package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class RewardStatBean implements Serializable {
    private String custId;
    private int totalRewardAmount;      //打赏金额
    private int totalRewardCount;       //打赏次数
    private int totalRewardedAmount;    //被打赏的金额
    private int totalRewardedCount;     //被打赏的次数

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public int getTotalRewardAmount() {
        return totalRewardAmount;
    }

    public void setTotalRewardAmount(int totalRewardAmount) {
        this.totalRewardAmount = totalRewardAmount;
    }

    public int getTotalRewardCount() {
        return totalRewardCount;
    }

    public void setTotalRewardCount(int totalRewardCount) {
        this.totalRewardCount = totalRewardCount;
    }

    public int getTotalRewardedAmount() {
        return totalRewardedAmount;
    }

    public void setTotalRewardedAmount(int totalRewardedAmount) {
        this.totalRewardedAmount = totalRewardedAmount;
    }

    public int getTotalRewardedCount() {
        return totalRewardedCount;
    }

    public void setTotalRewardedCount(int totalRewardedCount) {
        this.totalRewardedCount = totalRewardedCount;
    }
}
