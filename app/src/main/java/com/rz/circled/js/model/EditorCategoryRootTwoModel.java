package com.rz.circled.js.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by KF on 2017/5/31.
 */
public class EditorCategoryRootTwoModel implements Serializable {
    private long defaultId;
    private String defaultText;
    private ArrayList<EditorCategoryTwoModel> data;

    public long getDefaultId() {
        return defaultId;
    }

    public void setDefaultId(long defaultId) {
        this.defaultId = defaultId;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public ArrayList<EditorCategoryTwoModel> getData() {
        return data;
    }

    public void setData(ArrayList<EditorCategoryTwoModel> data) {
        this.data = data;
    }
}
