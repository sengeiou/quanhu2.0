package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.rz.circled.R;
import com.rz.circled.dialog.InsufficientBalanceDialog;
import com.rz.circled.presenter.impl.PayPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Type;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.Currency;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.bean.AccountBean;
import com.rz.httpapi.bean.UserInfoModel;
import com.rz.sgt.jsbridge.JsEvent;

import org.json.JSONException;

import java.util.HashMap;

/**
 * Created by rzw2 on 2017/9/18.
 */

public class JsPayOrderActivity extends BaseActivity {
    private static final String EXTRA_DATA = "extra_data";
    private static final String EXTRA_ORDER_ID = "orderId";
    private static final String EXTRA_PAY_MONEY = "payMoney";

    private PayPresenter presenter;
    private String orderId;
    private double payMoney;
    private HashMap<String, String> data;

    public static void startJsPay(Context context, String data) {
        Intent intent = new Intent(context, JsPayOrderActivity.class);
        intent.putExtra(EXTRA_DATA, data);
        context.startActivity(intent);
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    protected boolean needSwipeBack() {
        return false;
    }

    @Override
    public void initPresenter() {
        presenter = new PayPresenter(false);
        presenter.attachView(this);
    }

    @Override
    public void initView() {
        String extra = getIntent().getStringExtra(EXTRA_DATA);
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(extra);
            orderId = jsonObject.getString(EXTRA_ORDER_ID);
            payMoney = jsonObject.getDouble(EXTRA_PAY_MONEY);
            data = new HashMap<>();
            data.put("orderId", String.valueOf(orderId));
            data.put("payMoney", String.valueOf(payMoney));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("jsonException = ", e.toString());
        }
    }

    @Override
    public void initData() {
        //支付密码验证
        presenter.getUserAccount(Session.getUserId(), "");
    }

    @Override
    public void onLoadingStatus(int loadingStatus) {
        if (loadingStatus == CommonCode.General.ERROR_DATA) {
            Toasty.error(aty, "支付失败").show();
            JsEvent.callJsEvent(data, false);
            finish();
        }
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        if (loadingStatus == CommonCode.General.ERROR_DATA) {
            Toasty.error(aty, string).show();
            JsEvent.callJsEvent(data, false);
            finish();
        }
    }

    @Override
    public <T> void updateView(T t) {
        if (null != t) {
            if (t instanceof Integer) {
                if (((Integer) t).intValue() == 1000) {
                    InsufficientBalanceDialog.newInstance().show(getSupportFragmentManager(), "");
                } else {
                    JsEvent.callJsEvent(data, ((Integer) t).intValue() == 1);
                    finish();
                }
            }
            if (t instanceof AccountBean) {
                presenter.pay(orderId, payMoney);
            }
        }
    }

    @Override
    public void refreshPage() {

    }
}
