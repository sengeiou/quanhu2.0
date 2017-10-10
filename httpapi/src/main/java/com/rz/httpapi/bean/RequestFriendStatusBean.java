package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/9 0009.
 */

public class RequestFriendStatusBean implements Serializable {


    /**
     * rid : 3858
     * status : 0    申请状态 0等待同意，1已经同意，2已经拒绝 3:未提交申请
     * isRequire : 1
     * createTime : 1507519343000
     */

    private int rid;
    private int status;
    private int isRequire;
    private long createTime;

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

    public int getIsRequire() {
        return isRequire;
    }

    public void setIsRequire(int isRequire) {
        this.isRequire = isRequire;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
