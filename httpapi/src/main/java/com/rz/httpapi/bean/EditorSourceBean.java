package com.rz.httpapi.bean;

/**
 * Created by Gsm on 2017/9/23.
 */
public class EditorSourceBean {
    public String text;
    public String image;
    public EditorSourceMedia video;
    public EditorSourceMedia audio;

    public static class EditorSourceMedia {
        public String url;
        public String thumbnailImage;
        public long time;
        public long size;
    }
}
