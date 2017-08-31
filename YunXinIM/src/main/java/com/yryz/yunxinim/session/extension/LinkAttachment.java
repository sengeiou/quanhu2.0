package com.yryz.yunxinim.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/4/10.
 */
public class LinkAttachment extends CustomAttachment {

    private String url;

    public LinkAttachment() {
        super(CustomAttachmentType.LINK);
    }

    @Override
    protected void parseData(JSONObject data) {
        url = data.toJSONString();
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = null;
        try {
            data = JSONObject.parseObject(url);
        } catch (Exception e) {

        }
        return data;
    }

    public String getUrl() {
        return url;
    }
}
