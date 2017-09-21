package com.rz.httpapi.bean;

/**
 * Created by rzw2 on 2017/3/28.
 */

public class FriendRequireModel {
    //申请id
    private int rid;
    // 是否申请发起 0:否 1:是
    private int isRequire;
    //0等待同意，1已经同意，2已经拒绝
    private int status;
    //申请说明
    private String msg;
    //创建时间
    private String createTime;

    private FriendInformationBean user;

    public FriendInformationBean getUser() {
        return user;
    }

    public void setUser(FriendInformationBean user) {
        this.user = user;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public int getIsRequire() {
        return isRequire;
    }

    public void setIsRequire(int isRequire) {
        this.isRequire = isRequire;
    }

}
