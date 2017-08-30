package com.yryz.yunxinim.session.extension;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/4/10.
 */
public class ArticleAttachment extends CustomAttachment {

    public static final String KEY_TITLE = "title";
    public static final String KEY_DESC = "detailTitle";
    public static final String KEY_IMG_URL = "imageUrl";
    public static final String KEY_URL = "uid";
    public static final String KEY_ID = "workId";

    private String url;
    private String title;
    private String desc;
    private String imgUrl;
    private String id;

    public ArticleAttachment() {
        super(CustomAttachmentType.Article);
    }

    public ArticleAttachment(JSONObject data) {
        this();
        load(data);
    }

    @Override
    protected void parseData(JSONObject data) {
        this.url = data.getString(KEY_URL);
        this.title = data.getString(KEY_TITLE);
        this.desc = data.getString(KEY_DESC);
        this.imgUrl = data.getString(KEY_IMG_URL);
        this.id = data.getString(KEY_ID);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_URL, url);
        data.put(KEY_TITLE, title);
        data.put(KEY_DESC, desc);
        data.put(KEY_IMG_URL, imgUrl);
        data.put(KEY_ID, id);
        return data;
    }

    private void load(JSONObject data) {
        this.url = data.getString(KEY_URL);
        this.title = data.getString(KEY_TITLE);
        this.desc = data.getString(KEY_DESC);
        this.imgUrl = data.getString(KEY_IMG_URL);
        this.id = data.getString(KEY_ID);
    }

    public void checkData() {
        if (TextUtils.equals(url, "null")) {
            url = "";
        }
        if (TextUtils.equals(title, "null")) {
            title = "";
        }
        if (TextUtils.equals(desc, "null")) {
            desc = "";
        }
        if (TextUtils.equals(imgUrl, "null")) {
            imgUrl = "";
        }
        if (TextUtils.equals(id, "null")) {
            id = "";
        }
    }


    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "DefaultCustomAttachment{" +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
