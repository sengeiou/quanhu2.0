package com.rz.httpapi.bean;

/**
 * Created by rzw2 on 2017/9/5.
 */

public class NewsUnreadBean {
    private int type;
    private int label;
    private int num;

    public NewsUnreadBean(int type) {
        this.type = type;
    }

    public NewsUnreadBean(int type, int label, int num) {
        this.type = type;
        this.label = label;
        this.num = num;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
