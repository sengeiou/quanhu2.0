package com.rz.common.application;

import com.rz.common.ui.activity.BaseActivity;

import java.util.LinkedList;

/**
 * Created by Gsm on 2017/10/19.
 */
public class MyActivityManager {

    static LinkedList<BaseActivity> activites = new LinkedList<BaseActivity>();
    public static final String mainClass = "ui.activity.MainActivity";

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
            if (activite == null) continue;
            if (activite.isFinishing()) continue;
            activite.finish();
        }
    }

    public static void finishAllUnIncludeMian() {
        for (BaseActivity activite : activites) {
            if (activite == null) continue;
            if (activite.isFinishing()) continue;
            if (activite.getLocalClassName().equalsIgnoreCase(mainClass)) continue;
            activite.finish();
        }
    }

}
