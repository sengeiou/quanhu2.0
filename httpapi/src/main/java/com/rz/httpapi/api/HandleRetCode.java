package com.rz.httpapi.api;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.constans.ReturnCode;

/**
 * Created by Gsm on 2017/9/22.
 */
public class HandleRetCode {

    public static boolean handler(final Context context, ResponseData responseData) {
        if (responseData.getRet() == ReturnCode.FAIL_REMIND_1||responseData.getRet() == ReturnCode.FAIL_REMIND_2) {
            if (context instanceof Activity) {
                if (TextUtils.isEmpty(responseData.getMsg())) return false;
                Toast.makeText(context, responseData.getMsg(), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }

    public static boolean handlerExpire(ResponseData responseData) {
        if (responseData.getRet() == ReturnCode.USER_EXPIRE || responseData.getRet() == ReturnCode.USER_KICK) {
            return true;
        }
        return false;
    }
}
