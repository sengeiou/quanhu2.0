package com.rz.httpapi.bean;

/**
 * Created by rzw2 on 2017/9/5.
 */

public class NewsBean {
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_ACTIVITY = 1;
    public static final int TYPE_USER = 2;
    public static final int TYPE_ARTICLE = 3;
    public static final int TYPE_ANNOUNCEMENT = 4;
    public static final int TYPE_GROUP = 5;
    public static final int TYPE_INTERACTIVE = 6;

    private int viewType;

    public NewsBean(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
