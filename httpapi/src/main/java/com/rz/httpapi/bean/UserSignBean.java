package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class UserSignBean implements Serializable {

    /**
     * id : 12
     * custId : 89w08n6suy
     * eventCode : 15
     * signCount : 1
     * lastSignTime : 2017-09-18 10:56:43
     * createTime : 2017-09-18 10:56:43
     * updateTime : 2017-09-18 10:56:43
     * signFlag : true
     */

    private int id;
    private String custId;
    private String eventCode;
    private int signCount;
    private String lastSignTime;
    private String createTime;
    private String updateTime;
    private boolean signFlag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getSignCount() {
        return signCount;
    }

    public void setSignCount(int signCount) {
        this.signCount = signCount;
    }

    public String getLastSignTime() {
        return lastSignTime;
    }

    public void setLastSignTime(String lastSignTime) {
        this.lastSignTime = lastSignTime;
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

    public boolean isSignFlag() {
        return signFlag;
    }

    public void setSignFlag(boolean signFlag) {
        this.signFlag = signFlag;
    }

}
