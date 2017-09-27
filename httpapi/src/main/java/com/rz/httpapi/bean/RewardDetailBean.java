package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/27 0027.
 */

public class RewardDetailBean implements Serializable {


    /**
     * createTime : 1506420077000
     * amount : -3000
     * rewardDesc : 打赏
     * rewardId : 551804959969337344
     */

    private String createTime;
    private int amount;
    private String rewardDesc;
    private String rewardId;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getRewardDesc() {
        return rewardDesc;
    }

    public void setRewardDesc(String rewardDesc) {
        this.rewardDesc = rewardDesc;
    }

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }
}
