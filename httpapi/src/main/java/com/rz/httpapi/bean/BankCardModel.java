package com.rz.httpapi.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 银行卡信息
 */
public class BankCardModel implements Serializable {

    //银行卡ID
    public String cust2BankId;
    //用户ID
    public String custId;
    //银行卡号
    public String bankCardNo;
    //是否为默认银行卡 1 是的
    public int defaultCard;
    //绑卡日期
    public String createDate;
    //银行卡户主
    public String name;
    //银行卡所属银行名称
    public String bankCode;

    //默认添加银行卡按钮
    public boolean isShowAdd;

    public boolean isSelect;


    public String getCust2BankId() {
        return cust2BankId;
    }

    public void setCust2BankId(String cust2BankId) {
        this.cust2BankId = cust2BankId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getBankCardNo() {
        if (TextUtils.isEmpty(bankCardNo)) {
            return "";
        }
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public int getDefaultCard() {
        return defaultCard;
    }

    public void setDefaultCard(int defaultCard) {
        this.defaultCard = defaultCard;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankCode() {
        if (TextUtils.isEmpty(bankCardNo)) {
            bankCode = "";
        }
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public boolean isShowAdd() {
        return isShowAdd;
    }

    public void setShowAdd(boolean showAdd) {
        isShowAdd = showAdd;
    }
}
