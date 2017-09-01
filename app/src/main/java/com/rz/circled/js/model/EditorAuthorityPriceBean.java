package com.rz.circled.js.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gsm on 2017/8/31.
 */
public class EditorAuthorityPriceBean implements Serializable {

    private String unit;//单位
    private ArrayList<Integer> data;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ArrayList<Integer> getData() {
        return data;
    }

    public void setData(ArrayList<Integer> data) {
        this.data = data;
    }
}
