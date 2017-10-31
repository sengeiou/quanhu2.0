package com.rz.httpapi.api;

import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.constans.ReturnCode;

/**
 * Created by Gsm on 2017/9/22.
 */
public class HandleRetCode {

    public static boolean handlerExpire(ResponseData responseData) {
        if (responseData.getRet() == ReturnCode.USER_EXPIRE || responseData.getRet() == ReturnCode.USER_KICK) {
            return true;
        }
        return false;
    }
}
