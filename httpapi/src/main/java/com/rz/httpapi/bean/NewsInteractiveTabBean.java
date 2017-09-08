package com.rz.httpapi.bean;

/**
 * Created by rzw2 on 2017/9/7.
 */

public class NewsInteractiveTabBean {
    private String name;
    private int unReadNum;

    public NewsInteractiveTabBean(String name, int unReadNum) {
        this.name = name;
        this.unReadNum = unReadNum;
    }

    public int getUnReadNum() {
        return unReadNum;
    }

    public void setUnReadNum(int unReadNum) {
        this.unReadNum = unReadNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
