package com.yryz.yunxinim.session.extension;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/4/10.
 */
public class LuckyAttachment extends CustomAttachment {

    public static final String KEY_ID = "workId";
    public static final String KEY_TITLE = "detailTitle";
    public static final String KEY_USER_ID = "uid";
    public static final String KEY_USER_NAME = "title";
    public static final String KEY_TYPE = "type";
    public static final String KEY_HEAD_IMG = "imageUrl";
    public static final String KEY_SCENE = "scene";
    public static final String KEY_RENCENT_ID = "recentId";

    private String id;
    private String title;
    private String userId;
    private String userName;
    private String luckyType;
    private String headUrl;
    private String scene;
    private String recentId;

    public LuckyAttachment() {
        super(CustomAttachmentType.Lucky);
    }

    public LuckyAttachment(JSONObject data) {
        this();
        load(data);
    }

    @Override
    protected void parseData(JSONObject data) {
        this.id = data.getString(KEY_ID);
        this.title = data.getString(KEY_TITLE);
        this.userId = data.getString(KEY_USER_ID);
        this.userName = data.getString(KEY_USER_NAME);
        this.luckyType = data.getString(KEY_TYPE);
        this.headUrl = data.getString(KEY_HEAD_IMG);
        this.scene = data.getString(KEY_SCENE);
        this.recentId = data.getString(KEY_RENCENT_ID);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_ID, id);
        data.put(KEY_TITLE, title);
        data.put(KEY_USER_ID, userId);
        data.put(KEY_USER_NAME, userName);
        data.put(KEY_TYPE, luckyType);
        data.put(KEY_HEAD_IMG, headUrl);
        data.put(KEY_SCENE, scene);
        data.put(KEY_RENCENT_ID, recentId);
        return data;
    }

    private void load(JSONObject data) {
        this.id = data.getString(KEY_ID);
        this.title = data.getString(KEY_TITLE);
        this.userId = data.getString(KEY_USER_ID);
        this.userName = data.getString(KEY_USER_NAME);
        this.luckyType = data.getString(KEY_TYPE);
        this.headUrl = data.getString(KEY_HEAD_IMG);
        this.scene = data.getString(KEY_SCENE);
        this.recentId = data.getString(KEY_RENCENT_ID);
    }

    public void checkData() {
        if (TextUtils.equals(id, "null")) {
            id = "";
        }
        if (TextUtils.equals(title, "null")) {
            title = "";
        }
        if (TextUtils.equals(userId, "null")) {
            userId = "";
        }
        if (TextUtils.equals(userName, "null")) {
            userName = "";
        }
        if (TextUtils.equals(luckyType, "null")) {
            luckyType = "";
        }
        if (TextUtils.equals(headUrl, "null")) {
            headUrl = "";
        }
        if (TextUtils.equals(scene, "null")) {
            scene = "";
        }
        if (TextUtils.equals(recentId, "null")) {
            recentId = "";
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getLuckyType() {
        return luckyType;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public String getScene() {
        return scene;
    }

    public String getRecentId() {
        return recentId;
    }
}
