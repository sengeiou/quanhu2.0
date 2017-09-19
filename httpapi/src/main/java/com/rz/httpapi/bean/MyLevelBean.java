package com.rz.httpapi.bean;

/**
 * Created by Gsm on 2017/9/19.
 */
public class MyLevelBean {
    private long allGrow;
    private String createTime;
    private String custId;
    private String eventCode;
    private String eventName;
    private int id;
    private int newGrow;
    private String updateTime;

    public long getAllGrow() {
        return allGrow;
    }

    public void setAllGrow(long allGrow) {
        this.allGrow = allGrow;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNewGrow() {
        return newGrow;
    }

    public void setNewGrow(int newGrow) {
        this.newGrow = newGrow;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
