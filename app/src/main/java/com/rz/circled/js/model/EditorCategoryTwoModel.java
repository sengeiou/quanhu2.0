package com.rz.circled.js.model;

import java.io.Serializable;

/**
 * Created by KF on 2017/6/2.
 */
public class EditorCategoryTwoModel implements Serializable {
    private String categoryName;
    private long id;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
