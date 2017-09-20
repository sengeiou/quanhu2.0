package com.rz.httpapi.bean;

import java.util.ArrayList;

/**
 * Created by Gsm on 2017/9/19.
 */
public class CouponsRootBean {
    private ArrayList<CouponsBean> entities;

    public ArrayList<CouponsBean> getEntities() {
        return entities;
    }

    public void setEntities(ArrayList<CouponsBean> entities) {
        this.entities = entities;
    }
}
