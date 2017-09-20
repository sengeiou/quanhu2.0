package com.rz.httpapi.bean;

/**
 * Created by xiayumo on 16/8/30.
 */
public class CashModel {

    //百分比
    private double prop;
    //提现名称
    private String bankName;
    //1表示是充值手续费类型，0表示是提现手续费类型
    private int type;
    //支付类型
    private String bankId;
    //固定手续费
    private int regulate;

    public double getProp() {
        return prop;
    }

    public void setProp(double prop) {
        this.prop = prop;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public int getRegulate() {
        return regulate;
    }

    public void setRegulate(int regulate) {
        this.regulate = regulate;
    }
}
