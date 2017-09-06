package com.rz.circled.event;

import com.rz.httpapi.api.ResponseData.ResponseData;

/**
 * Created by Administrator on 2016/8/27 0027.
 */
public class ResponseDataEven {

    public int act;
    public ResponseData responseData;

    public ResponseDataEven(int act, ResponseData responseData) {
        this.act = act;
        this.responseData = responseData;
    }

    public ResponseDataEven() {
    }
}
