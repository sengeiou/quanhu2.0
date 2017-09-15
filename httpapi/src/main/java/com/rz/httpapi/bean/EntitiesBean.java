package com.rz.httpapi.bean;

/**
 * Created by Administrator on 2017/9/14/014.
 */

public class EntitiesBean {
    /**
     * id : 32
     * title : 报蜘蛛
     * coverPlan : https://cdn-qa.yryz.com/pic/qshop/1abb59338c9de62402d62683eaa450e0.jpg
     * activityType : 1
     */

    private int id;
    private String title;
    private String coverPlan;
    private int activityType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverPlan() {
        return coverPlan;
    }

    public void setCoverPlan(String coverPlan) {
        this.coverPlan = coverPlan;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }
}
