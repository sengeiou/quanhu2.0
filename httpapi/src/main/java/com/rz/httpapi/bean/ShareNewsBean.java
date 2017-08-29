package com.rz.httpapi.bean;

/**
 * 作者：Administrator on 2016/8/15 0015 18:19
 * 功能：
 * 说明：
 */
public class ShareNewsBean {

    /**
     * 图片
     */
    private int drawable;

    /**
     * 昵称
     */
    private String name;

    public ShareNewsBean(int drawable, String name) {
        this.drawable = drawable;
        this.name = name;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
