package com.rz.httpapi.bean;

/**
 * 作者：Administrator on 2016/8/29 0029 11:30
 * 功能：登录方式
 * 说明：
 */
public class LoginWayModel {

    //是否绑定 有值绑定 没有值 未绑定
//    private String openId;
    private String thirdId;

    //1微信，2微博，3qq，4手机号
    private int type;

    //账号id
//    private String nickName;
    private String nickNname;

    //绑定的时间
    private String createDate;

//    public String getOpenId() {
//        return openId;
//    }
//
//    public void setOpenId(String openId) {
//        this.openId = openId;
//    }


    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNickName() {
        return nickNname;
    }

    public void setNickName(String nickNname) {
        this.nickNname = nickNname;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
