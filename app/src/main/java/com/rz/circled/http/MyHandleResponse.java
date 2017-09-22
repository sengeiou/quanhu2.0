package com.rz.circled.http;

import com.rz.common.event.KickEvent;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.HandleRetCode;
import com.rz.httpapi.api.ResponseData.ResponseData;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Response;

/**
 * Created by Gsm on 2017/9/22.
 */
public class MyHandleResponse implements BaseCallback.interceptorResponse {

    public static MyHandleResponse handleResponse;

    public MyHandleResponse() {

    }

    public static MyHandleResponse newInstance() {
        if (handleResponse == null)
            handleResponse = new MyHandleResponse();
        return handleResponse;
    }

    @Override
    public void handleReson(Response response) {
        if (response != null) {
            Object object = response.body();
            if (object != null && object instanceof ResponseData) {
                ResponseData responseData = (ResponseData) object;
                boolean needProcess = HandleRetCode.handlerExpire(responseData);
                if (needProcess) {
                    EventBus.getDefault().post(new KickEvent(responseData.getRet()));
                }
            }
        }
    }
}
