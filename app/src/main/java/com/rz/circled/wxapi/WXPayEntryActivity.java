package com.rz.circled.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.rz.circled.pay.WXPayResult;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * 支付回调
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.WeiXin.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (TextUtils.equals("-1", String.valueOf(resp.errCode))) {
                WXPayResult.setPayStatus(CommonCode.PayCode.PAY_FAIL);
            } else if (TextUtils.equals("-2", String.valueOf(resp.errCode))) {
                WXPayResult.setPayStatus(CommonCode.PayCode.PAY_CANDEL);
            } else if (TextUtils.equals("0", String.valueOf(resp.errCode))) {
                WXPayResult.setPayStatus(CommonCode.PayCode.PAY_SUCCESS);
            }
            finish();
        }
    }
}