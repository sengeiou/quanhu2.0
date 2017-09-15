package com.rz.httpapi.bean;

import java.io.Serializable;


/**
 * 用户信息
 */
public class UserInfoBean implements Serializable {
    /**
     * 用户id
     */
    private String custId;

    /**
     * 用户头像
     */
    private String custImg;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 用户电话
     */
    private String custPhone;

    /**
     * 用户二维码图片地址
     */
    private String custQr;

    /**
     * 用户昵称
     */
    private String custNname;

    /**
     * 用户备注
     */
    private String nameNotes;

    /**
     * 用户签名
     */
    private String custSignature;

    /**
     * 个人简介
     */
    private String custDesc;

    /**
     * 用户性别
     */
    private String custSex;

    /**
     * 用户地址
     */
    private String custLocation;

    /**
     * 是否设置支付密码 0未设置 1设置
     */
    private Integer isPayPassword;

    /**
     * 是否开启免密支付 0未开启 1开启
     */
    private Integer smallNopass;

    //是否设置安全保护信息
    private String phyName;

    //身份证号码
    private String phyCardNo;

    /**
     * 是否设置登录密码
     */
    private int isPwdExist;

    /**
     * 城市定位信息
     */
    private String cityCode;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustImg() {
        return custImg;
    }

    public void setCustImg(String custImg) {
        this.custImg = custImg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCustQr() {
        return custQr;
    }

    public void setCustQr(String custQr) {
        this.custQr = custQr;
    }

    public String getCustNname() {
        return custNname;
    }

    public void setCustNname(String custNname) {
        this.custNname = custNname;
    }

    public String getNameNotes() {
        return nameNotes;
    }

    public void setNameNotes(String nameNotes) {
        this.nameNotes = nameNotes;
    }

    public String getCustSignature() {
        return custSignature;
    }

    public void setCustSignature(String custSignature) {
        this.custSignature = custSignature;
    }

    public String getCustDesc() {
        return custDesc;
    }

    public void setCustDesc(String custDesc) {
        this.custDesc = custDesc;
    }

    public String getCustSex() {
        return custSex;
    }

    public void setCustSex(String custSex) {
        this.custSex = custSex;
    }

    public String getCustLocation() {
        return custLocation;
    }

    public void setCustLocation(String custLocation) {
        this.custLocation = custLocation;
    }

    public Integer getIsPayPassword() {
        if (null == isPayPassword) {
            return 0;
        }
        return isPayPassword;
    }

    public void setIsPayPassword(Integer isPayPassword) {
        this.isPayPassword = isPayPassword;
    }

    public Integer getSmallNopass() {
        if (null == smallNopass) {
            return 0;
        }
        return smallNopass;
    }

    public void setSmallNopass(Integer smallNopass) {
        this.smallNopass = smallNopass;
    }

    public String getPhyName() {
        return phyName;
    }

    public void setPhyName(String phyName) {
        this.phyName = phyName;
    }

    public String getPhyCardNo() {
        return phyCardNo;
    }

    public void setPhyCardNo(String phyCardNo) {
        this.phyCardNo = phyCardNo;
    }

    public int getIsPwdExist() {
        return isPwdExist;
    }

    public void setIsPwdExist(int isPwdExist) {
        this.isPwdExist = isPwdExist;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
