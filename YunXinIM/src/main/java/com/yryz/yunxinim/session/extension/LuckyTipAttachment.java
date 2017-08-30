package com.yryz.yunxinim.session.extension;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/4/10.
 */
public class LuckyTipAttachment extends CustomAttachment {

    public static final String KEY_ID = "workId";
    public static final String KEY_USER_ID = "sendRedId";
    public static final String KEY_USER_NAME = "detailTitle";
    public static final String KEY_RECEIVE_NAME = "title";
    public static final String KEY_RECEIVE_ID = "uid";

    private String id;
    private String userId;
    private String userName;
    private String receiveName;
    private String receiveId;

    public LuckyTipAttachment() {
        super(CustomAttachmentType.LuckyTip);
    }

    public LuckyTipAttachment(JSONObject data) {
        this();
        load(data);
    }

    @Override
    protected void parseData(JSONObject data) {
        this.id = data.getString(KEY_ID);
        this.userId = data.getString(KEY_USER_ID);
        this.userName = data.getString(KEY_USER_NAME);
        this.receiveName = data.getString(KEY_RECEIVE_NAME);
        this.receiveId = data.getString(KEY_RECEIVE_ID);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_ID, id);
        data.put(KEY_USER_ID, userId);
        data.put(KEY_USER_NAME, userName);
        data.put(KEY_RECEIVE_NAME, receiveName);
        data.put(KEY_RECEIVE_ID, receiveId);
        return data;
    }

    private void load(JSONObject data) {
        this.id = data.getString(KEY_ID);
        this.userId = data.getString(KEY_USER_ID);
        this.userName = data.getString(KEY_USER_NAME);
        this.receiveName = data.getString(KEY_RECEIVE_NAME);
        this.receiveId = data.getString(KEY_RECEIVE_ID);
    }

    public void checkData() {
        if (TextUtils.equals(id, "null")) {
            id = "";
        }
        if (TextUtils.equals(receiveName, "null")) {
            receiveName = "";
        }
        if (TextUtils.equals(userId, "null")) {
            userId = "";
        }
        if (TextUtils.equals(userName, "null")) {
            userName = "";
        }
        if (TextUtils.equals(receiveId, "null")) {
            receiveId = "";
        }
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public String getReceiveId() {
        return receiveId;
    }
}
