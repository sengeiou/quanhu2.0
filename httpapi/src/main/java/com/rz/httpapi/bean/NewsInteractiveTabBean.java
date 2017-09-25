package com.rz.httpapi.bean;

/**
 * Created by rzw2 on 2017/9/7.
 */

public class NewsInteractiveTabBean {
    private int id;
    private String name;
    private int unReadNum;

    public NewsInteractiveTabBean(int id, String name, int unReadNum) {
        this.id = id;
        this.name = name;
        this.unReadNum = unReadNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
