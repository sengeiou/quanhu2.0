package com.rz.httpapi.bean;

/**
 * Created by Gsm on 2017/9/18.
 */
public class CouponsBean {

    /**
     * activityInfoId : 33
     * beginTime : 2017-09-05 20:17:22
     * canNum : 15
     * createDate : 2017-09-07 18:02:58
     * createUserId : eldrsm2k
     * endTime : 2017-09-21 20:15:51
     * id : 10
     * lastUpdateDate : 2017-09-18 13:33:29
     * lastUpdateUserId : eldrsm2k
     * onlyCode : a348aca141c44595a97864eacdb46fb5
     * phone : 13207188676
     * prizesName : 代金券
     * prizesNum : 5
     * prizesType : 2
     * prizesUnit : 英镑
     * remark : 我是医生打算都打过
     * status : 1
     */

    private int activityInfoId;
    private String beginTime;
    private int canNum;
    private String createDate;
    private String createUserId;
    private String endTime;
    private int id;
    private String lastUpdateDate;
    private String lastUpdateUserId;
    private String onlyCode;
    private String phone;
    private String prizesName;
    private int prizesNum;
    private int prizesType;
    private String prizesUnit;
    private String remark;
    private int status;//使用情况 （1可使用 2已使用）
    public boolean showDetail = false;

    public int getActivityInfoId() {
        return activityInfoId;
    }

    public void setActivityInfoId(int activityInfoId) {
        this.activityInfoId = activityInfoId;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public int getCanNum() {
        return canNum;
    }

    public void setCanNum(int canNum) {
        this.canNum = canNum;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(String lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    public String getOnlyCode() {
        return onlyCode;
    }

    public void setOnlyCode(String onlyCode) {
        this.onlyCode = onlyCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrizesName() {
        return prizesName;
    }

    public void setPrizesName(String prizesName) {
        this.prizesName = prizesName;
    }

    public int getPrizesNum() {
        return prizesNum;
    }

    public void setPrizesNum(int prizesNum) {
        this.prizesNum = prizesNum;
    }

    public int getPrizesType() {
        return prizesType;
    }

    public void setPrizesType(int prizesType) {
        this.prizesType = prizesType;
    }

    public String getPrizesUnit() {
        return prizesUnit;
    }

    public void setPrizesUnit(String prizesUnit) {
        this.prizesUnit = prizesUnit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
