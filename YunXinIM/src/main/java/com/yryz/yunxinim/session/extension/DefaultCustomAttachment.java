package com.yryz.yunxinim.session.extension;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/4/10.
 */
public class DefaultCustomAttachment extends CustomAttachment {

    public DefaultCustomAttachment() {
        super(CustomAttachmentType.Default);
    }

    public DefaultCustomAttachment(JSONObject data) {
        this();
    }

    @Override
    protected void parseData(JSONObject data) {
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        return data;
    }
}
