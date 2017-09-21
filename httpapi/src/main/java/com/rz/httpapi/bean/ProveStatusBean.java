package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Gsm on 2017/9/18.
 */
public class ProveStatusBean implements Serializable {
    public static final int STATUS_ING = 0;
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAIL = 2;
    public static final int STATUS_CANCEL = 3;
    public static final int STATUS_NORMAL = 4;
    private int authStatus;//认证状态 0:审核中 1:成功 2:失败 3:后台取消认证 4:未认证
    private int authType;//认证类型 0:个人认证 1:企业/机构认证
    private int authWay;//认证方式 0:用户申请 1:平台设置
    private String tradeField;
    private String contactCall;
    private String idCard;
    private String location;
    private String organizationName;
    private String organizationPaper;
    private String ownerAppId = "";
    private String realName;
    private String resourceDesc;

    public String getContactCall() {
        return contactCall;
    }

    public void setContactCall(String contactCall) {
        this.contactCall = contactCall;
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

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public int getAuthType() {
        return authType;
    }

    public void setAuthType(int authType) {
        this.authType = authType;
    }

    public int getAuthWay() {
        return authWay;
    }

    public void setAuthWay(int authWay) {
        this.authWay = authWay;
    }

    public String getTradeField() {
        return tradeField;
    }

    public void setTradeField(String tradeField) {
        this.tradeField = tradeField;
    }

    public boolean isOneSelf() {
        return authType == 0;
    }
}
