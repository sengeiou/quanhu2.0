package com.rz.circled.modle;

/**
 * MineFrag 数据模型
 */
public class MineFragItemModel {
    /*标题*/
    private String name;
    /*图片*/
    private int drawable;
    /*是否是分割*/
    private boolean isDivider;
    /*是否显示右箭头*/
    private boolean isArrow;
    //连接
    private String url;
    //是否有更新
    private boolean isUpdate;
    //是否有关注提醒
    private boolean isContacts;
    //关注数
    private String mFocusNum;

    public int mediaType;

    public int bannerAdsType;

    public String title;

    public String pic;

    public String remark;

    public MineFragItemModel() {
    }

    public MineFragItemModel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public MineFragItemModel(String name, int drawable, boolean isDivider) {
        this.name = name;
        this.drawable = drawable;
        this.isDivider = isDivider;
    }

    public MineFragItemModel(boolean isArrow,String name, int drawable, boolean isDivider) {
        this.name = name;
        this.drawable = drawable;
        this.isDivider = isDivider;
        this.isArrow = isArrow;
    }

    public MineFragItemModel(boolean isArrow,String remark,String name, int drawable, boolean isDivider) {
        this.name = name;
        this.drawable = drawable;
        this.isDivider = isDivider;
        this.remark = remark;
        this.isArrow = isArrow;
    }

    public MineFragItemModel(String name, String remark, int drawable, boolean isDivider) {
        this.name = name;
        this.drawable = drawable;
        this.isDivider = isDivider;
        this.remark = remark;
    }

    public MineFragItemModel(String name, int drawable, boolean isDivider, boolean isUpdate) {
        this.name = name;
        this.drawable = drawable;
        this.isDivider = isDivider;
        this.isUpdate = isUpdate;
    }


    public boolean isUpdate() {
        return isUpdate;
    }

    public boolean isContacts() {
        return isContacts;
    }

    public void setContacts(boolean contacts) {
        isContacts = contacts;
    }

    public String getmFocusNum() {
        return mFocusNum;
    }

    public void setmFocusNum(String mFocusNum) {
        this.mFocusNum = mFocusNum;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public boolean isDivider() {
        return isDivider;
    }

    public void setDivider(boolean divider) {
        isDivider = divider;
    }

    public boolean isArrow() {
        return isArrow;
    }

    public void setArrow(boolean arrow) {
        isArrow = arrow;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
