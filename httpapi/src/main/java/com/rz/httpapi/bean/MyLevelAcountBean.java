package com.rz.httpapi.bean;

/**
 * Created by Gsm on 2017/9/19.
 */
public class MyLevelAcountBean {
    private String createTime;
    private long grow;
    private String growLevel;
    private long score;
    private String updateTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getGrow() {
        return grow;
    }

    public void setGrow(long grow) {
        this.grow = grow;
    }

    public String getGrowLevel() {
        return growLevel;
    }

    public void setGrowLevel(String growLevel) {
        this.growLevel = growLevel;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
