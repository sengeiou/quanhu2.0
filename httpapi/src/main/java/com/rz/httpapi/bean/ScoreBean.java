package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/27 0027.
 */

public class ScoreBean implements Serializable {


    /**
     * id : 198032
     * consumeFlag : 0
     * custId : 89w08n6suy
     * eventCode : 15
     * eventName : 签到
     * newScore : 10
     * allScore : 70
     * createTime : 2017-09-25 11:30:24
     * updateTime : 2017-09-25 11:30:24
     */

    private int id;
    private int consumeFlag;
    private String custId;
    private String eventCode;
    private String eventName;
    private int newScore;
    private int allScore;
    private String createTime;
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConsumeFlag() {
        return consumeFlag;
    }

    public void setConsumeFlag(int consumeFlag) {
        this.consumeFlag = consumeFlag;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getNewScore() {
        return newScore;
    }

    public void setNewScore(int newScore) {
        this.newScore = newScore;
    }

    public int getAllScore() {
        return allScore;
    }

    public void setAllScore(int allScore) {
        this.allScore = allScore;
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
}
