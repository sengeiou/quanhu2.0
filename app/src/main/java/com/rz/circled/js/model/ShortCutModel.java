package com.rz.circled.js.model;

/**
 * Created by Administrator on 2017/3/14/014.
 */

public class ShortCutModel {

    private String circleImageUrl;
    private String circleName;
    private String circleUrl;

    public String getCircleImageUrl() {
        return circleImageUrl;
    }

    public void setCircleImageUrl(String circleImageUrl) {
        this.circleImageUrl = circleImageUrl;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getCircleUrl() {
        return circleUrl;
    }

    public void setCircleUrl(String circleUrl) {
        this.circleUrl = circleUrl;
    }

    @Override
    public String toString() {
        return "ShortCutModel{" +
                "circleImageUrl='" + circleImageUrl + '\'' +
                ", circleName='" + circleName + '\'' +
                ", circleUrl='" + circleUrl + '\'' +
                '}';
    }
}
