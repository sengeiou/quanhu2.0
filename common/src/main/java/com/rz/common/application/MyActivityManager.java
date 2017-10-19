package com.rz.common.application;

import com.rz.common.ui.activity.BaseActivity;

import java.util.LinkedList;

/**
 * Created by Gsm on 2017/10/19.
 */
public class MyActivityManager {

    static LinkedList<BaseActivity> activites = new LinkedList<BaseActivity>();

    public static void addActivity(BaseActivity baseActivity) {
        activites.addFirst(baseActivity);
    }

    public static void removeActivity(BaseActivity baseActivity) {
        activites.remove(baseActivity);
    }

    public static void removeAll() {
        activites.clear();
    }

    public static void finishAll() {
        for (BaseActivity activite : activites) {
            activite.finish();
        }
    }

}
