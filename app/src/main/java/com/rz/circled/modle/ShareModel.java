package com.rz.circled.modle;

/**
 * Created by rzw2 on 2016/12/28.
 */

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import java.io.Serializable;

/**
 * 跳转转发
 */

public class ShareModel implements Serializable {
    private String appId;
    private String title;
    private String desc;
    private String url;
    private String tumb;
    //作品id
    private String id;
    //圈子id
    private String clubId;
    private int defaultPic;
    private boolean showReport;
    private int type;
    private int mediaType; //2，音频，3，视频  //1为文字2为图片3为视频
    private boolean isCircle; //是否是圈子

    private String userId;//分享对象id
    private String userAvater;//分享对象头像
    public int userAvaterRes;
    private String userName;//分享对象名称
    private SessionTypeEnum typeEnum;//分享对象类型
    private int fromPage;

    public ShareModel() {

    }

    /**
     * 构造方法---->添加好友、首页banner、通知
     *
     * @param url
     */
    public ShareModel(String title, String desc, String url) {
        this.title = title;
        this.desc = desc;
        this.url = url;
    }

    //随手晒分享到im
    public ShareModel(String clubId, String title, String tumb, int defaultPic, int type, int fromPage) {
        this.title = title;
        this.clubId = clubId;
        this.tumb = tumb;
        this.defaultPic = defaultPic;
        this.type = type;
        this.fromPage = fromPage;
    }

    //圈子分享到im
    public ShareModel(String title, String desc, String url, String tumb) {
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.tumb = tumb;
    }

    //圈子分享到im
    public ShareModel(String appId, String title, String desc, String url, String tumb) {
        this.appId = appId;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.tumb = tumb;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTumb() {
        return tumb;
    }

    public void setTumb(String tumb) {
        this.tumb = tumb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public boolean isCircle() {
        return isCircle;
    }

    public void setCircle(boolean circle) {
        isCircle = circle;
    }

    public int getDefaultPic() {
        return defaultPic;
    }

    public void setDefaultPic(int defaultPic) {
        this.defaultPic = defaultPic;
    }

    public boolean isShowReport() {
        return showReport;
    }

    public void setShowReport(boolean showReport) {
        this.showReport = showReport;
    }

    public int getFromPage() {
        return fromPage;
    }

    public void setFromPage(int fromPage) {
        this.fromPage = fromPage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAvater() {
        return userAvater;
    }

    public void setUserAvater(String userAvater) {
        this.userAvater = userAvater;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SessionTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(SessionTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    @Override
    public String toString() {
        return "ShareModel{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", url='" + url + '\'' +
                ", tumb='" + tumb + '\'' +
                ", id='" + id + '\'' +
                ", clubId='" + clubId + '\'' +
                ", defaultPic=" + defaultPic +
                ", type=" + type +
                ", mediaType=" + mediaType +
                ", isCircle=" + isCircle +
                ", userId='" + userId + '\'' +
                ", userAvater='" + userAvater + '\'' +
                ", userName='" + userName + '\'' +
                ", typeEnum=" + typeEnum +
                '}';
    }
}
