package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Gsm on 2017/9/16.
 */
public class ProveInfoBean implements Serializable {
    private int authType;//认证类型 0:个人认证 1:企业/机构认证
    private String contactCall;//联系电话或者手机号
    private String custId;//用户id
    private String idCard;//省份证号
    private String location;//所在地区
    private String organizationName;//机构名称
    private String organizationPaper;//机构证件
    private String ownerAppId;//自媒体账户
    private String realName;//用户真实姓名或者运营者姓名
    private String resourceDesc;//可提供的资源或者拥有的资源
    private String tradeField;//行业以及领域

    public int getAuthType() {
        return authType;
    }

    public void setAuthType(int authType) {
        this.authType = authType;
    }

    public String getContactCall() {
        return contactCall;
    }

    public void setContactCall(String contactCall) {
        this.contactCall = contactCall;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationPaper() {
        return organizationPaper;
    }

    public void setOrganizationPaper(String organizationPaper) {
        this.organizationPaper = organizationPaper;
    }

    public String getOwnerAppId() {
        return ownerAppId;
    }

    public void setOwnerAppId(String ownerAppId) {
        this.ownerAppId = ownerAppId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getResourceDesc() {
        return resourceDesc;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

    public String getTradeField() {
        return tradeField;
    }

    public void setTradeField(String tradeField) {
        this.tradeField = tradeField;
    }
}
