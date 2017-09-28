package com.rz.common.utils;

import android.net.TrafficStats;

import com.rz.common.application.BaseApplication;

/**
 * Created by Gsm on 2017/9/28.
 */
public class NetWorkSpeedUtils {

    public static long getTotalRxBytes() {
        return TrafficStats.getUidRxBytes(BaseApplication.getInstance().getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);//转为KB
    }
}
