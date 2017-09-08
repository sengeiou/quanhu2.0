package com.rz.circled.modle;

/**
 * Created by Administrator on 2017/5/9/009.
 */

public class CustormServiceModel {
    private int startHour;//上班时间 小时
    private int startMinute;// 	 	上班时间 分钟
    private int endHour;//	 	下班时间 小时
    private int endMinute;    // 	下班时间 分钟
    private int status;//	 	客服功能开关 0：关闭 1:开启
    private String customUrl;//	 	第三方客服地址
    private String messageUrl;//第三方留言地址

    public CustormServiceModel() {
    }

    public CustormServiceModel(int startHour, int startMinute, int endHour, int endMinute, int status, String customUrl, String messageUrl) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.status = status;
        this.customUrl = customUrl;
        this.messageUrl = messageUrl;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public void setMessageUrl(String messageUrl) {
        this.messageUrl = messageUrl;
    }

    @Override
    public String toString() {
        return "CustormServiceModel{" +
                "startHour=" + startHour +
                ", startMinute=" + startMinute +
                ", endHour=" + endHour +
                ", endMinute=" + endMinute +
                ", status=" + status +
                ", customUrl='" + customUrl + '\'' +
                ", messageUrl='" + messageUrl + '\'' +
                '}';
    }
}
