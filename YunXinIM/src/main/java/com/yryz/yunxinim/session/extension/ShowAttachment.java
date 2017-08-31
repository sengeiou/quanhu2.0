package com.yryz.yunxinim.session.extension;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/4/10.
 */
public class ShowAttachment extends CustomAttachment {

    public static final String KEY_DESC = "detailTitle";
    public static final String KEY_MEDIA_TYPE = "type";
    public static final String KEY_IMG_URL = "imageUrl";
    public static final String KEY_IMG_RES = "imgRes";
    public static final String KEY_CLUB_ID = "uid";

    private String club_id;
    private String desc;
    private String mediaType;//1为文字2为图片3为视频
    private String imgUrl;
    private String imgRes;

    public ShowAttachment() {
        super(CustomAttachmentType.Show);
    }

    public ShowAttachment(JSONObject data) {
        this();
        load(data);
    }

    @Override
    protected void parseData(JSONObject data) {
        this.desc = data.getString(KEY_DESC);
        this.mediaType = data.getString(KEY_MEDIA_TYPE);
        this.imgUrl = data.getString(KEY_IMG_URL);
        this.imgRes = data.getString(KEY_IMG_RES);
        this.club_id = data.getString(KEY_CLUB_ID);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_DESC, desc);
        data.put(KEY_MEDIA_TYPE, mediaType);
        data.put(KEY_IMG_URL, imgUrl);
        data.put(KEY_IMG_RES, imgRes);
        data.put(KEY_CLUB_ID, club_id);
        return data;
    }

    private void load(JSONObject data) {
        this.desc = data.getString(KEY_DESC);
        this.mediaType = data.getString(KEY_MEDIA_TYPE);
        this.imgUrl = data.getString(KEY_IMG_URL);
        this.imgRes = data.getString(KEY_IMG_RES);
        this.club_id = data.getString(KEY_CLUB_ID);
    }

    public void checkData() {
        if (TextUtils.equals(desc, "null")) {
            desc = "";
        }
        if (TextUtils.equals(mediaType, "null")) {
            mediaType = "";
        }
        if (TextUtils.equals(imgUrl, "null")) {
            imgUrl = "";
        }
        if (TextUtils.equals(imgRes, "null")) {
            imgRes = "";
        }
        if (TextUtils.equals(club_id, "null")) {
            club_id = "";
        }
    }

    public String getDesc() {
        return desc;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getImgRes() {
        return imgRes;
    }

    public String getClub_id() {
        return club_id;
    }

    @Override
    public String toString() {
        return "DefaultCustomAttachment{" +
                ", club_id='" + club_id + '\'' +
                ", desc='" + desc + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", imgRes='" + imgRes + '\'' +
                '}';
    }
}
