package com.rz.circled.modle;

import java.io.Serializable;

/**
 * Created by xiayumo on 16/9/3.
 */
public class BaseInfo implements Serializable {

    private String custId;
    //用户头像
    private String custImg;
    //用户电话
    private String custPhone;
    private String custNo;
    private String firstLetter;
    private String custName;
    private String custNname;
    private String custSignature;
    private String nameNotes;
    private String custLocation;
    //个人简介
    private String custDesc;
    private String custSex;
    private Integer relation;//1是关注了该粉丝，0是没关注

    private boolean disable;
    private boolean isSelect;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustNname() {
        return custNname;
    }

    public void setCustNname(String custNname) {
        this.custNname = custNname;
    }

    public String getCustImg() {
        return custImg;
    }

    public void setCustImg(String custImg) {
        this.custImg = custImg;
    }

    public String getCustSignature() {
        return custSignature;
    }

    public void setCustSignature(String custSignature) {
        this.custSignature = custSignature;
    }

    public String getNameNotes() {
        return nameNotes;
    }

    public void setNameNotes(String nameNotes) {
        this.nameNotes = nameNotes;
    }

    public Integer getRelation() {
        if (null == relation) {
            return -1;
        }
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
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

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getCustLocation() {
        return custLocation;
    }

    public void setCustLocation(String custLocation) {
        this.custLocation = custLocation;
    }

    @Override
    public String toString() {
        return "BaseInfo{" +
                "custId='" + custId + '\'' +
                ", custImg='" + custImg + '\'' +
                ", custPhone='" + custPhone + '\'' +
                ", custNo='" + custNo + '\'' +
                ", firstLetter='" + firstLetter + '\'' +
                ", custName='" + custName + '\'' +
                ", custNname='" + custNname + '\'' +
                ", custSignature='" + custSignature + '\'' +
                ", nameNotes='" + nameNotes + '\'' +
                ", custLocation='" + custLocation + '\'' +
                ", custDesc='" + custDesc + '\'' +
                ", custSex='" + custSex + '\'' +
                ", relation=" + relation +
                ", disable=" + disable +
                ", isSelect=" + isSelect +
                '}';
    }
}
