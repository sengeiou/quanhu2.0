package com.rz.httpapi.bean;


/**
 * 好友信息
 */
public class FriendInfoModel extends BaseInfo {

    private String custQr;

    private String custLevel;

    public String getCustLevel() {
        return custLevel;
    }

    public void setCustLevel(String custLevel) {
        this.custLevel = custLevel;
    }

    public String getCustQr() {
        return custQr;
    }

    public void setCustQr(String custQr) {
        this.custQr = custQr;
    }

}
