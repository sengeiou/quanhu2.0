package com.rz.httpapi.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by JS01 on 2016/6/14.
 * 管理Retrofit请求的Call对象，在onDestory时取消所有请求
 */
public class CallManager {

    private static final String TAG = "CallManager";

    private static List<Call> list;

    static {
        list = new ArrayList<>();
    }

    public static void add(Call call) {
//        if (call != null) {
//            Log.d(TAG, "add call");
//            if (!list.contains(call)) {
//                list.add(call);
//            }
//        }
    }

    public static void remove(Call call) {
        if (call != null) {
            Log.d(TAG, "remove call");
            if (list.contains(call)) {
                list.remove(call);
            }
        }
    }

    public static void cancelAll() {
        if (!list.isEmpty()) {
            Log.d(TAG, "cancel all call");
            for (Call call : list) {
                call.cancel();
            }
        }
    }

    public static void cancelList(List<Call> calls) {
        if (!list.isEmpty() && !calls.isEmpty()) {
            Log.d(TAG, "cancel all call");
            for (Call call : calls) {
                call.cancel();
            }
        }
    }

}
