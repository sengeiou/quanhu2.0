package com.rz.circled.modle;

import java.io.Serializable;

/**
 * 作者：Administrator on 2016/10/27 0027 13:17
 * 功能：点赞列表
 * 说明：
 */
public class RateModel implements Serializable {

    //点赞id
    public String lid;
    //用户id
    public String custId = "";
    //用户昵称
    public String custNname = "";
    //用户备注名
    public String nameNotes = "";
    //用户头像
    public String custImg = "";
    //晒一晒列表id
    public String sid;

    private boolean isMoreZan;

    public boolean isMoreZan() {
        return isMoreZan;
    }

    public void setMoreZan(boolean moreZan) {
        isMoreZan = moreZan;
    }
}
