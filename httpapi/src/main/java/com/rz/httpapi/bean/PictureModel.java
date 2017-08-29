package com.rz.httpapi.bean;

import android.graphics.Bitmap;

/**
 * 图片信息
 */
public class PictureModel {

    /**
     *
     */

    /**
     * 图片路径
     */
    private String mPicPath;

    /**
     * 是否被选中
     */
    private boolean isSelect;

    /**
     * 图片的bitmap
     */
    private Bitmap mBitmap;

    private long lastModifyTime;

    /**
     * 表示是添加按钮 1 表示添加按钮
     */
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public String getmPicPath() {
        return mPicPath;
    }

    public void setmPicPath(String mPicPath) {
        this.mPicPath = mPicPath;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long time) {
        this.lastModifyTime = time;
    }

}
