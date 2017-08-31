package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class OpusTag implements Serializable{

    public String uid;
    public String tagName;

    @Override
    public String toString() {
        return "OpusTag{" +
                "uid='" + uid + '\'' +
                ", tagName='" + tagName + '\'' +
                '}';
    }
}
