package com.rz.circled.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.pay.WXPayResult;
import com.rz.circled.presenter.impl.PayPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.Currency;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiayumo on 16/8/22.
 * 充值,微信，支付宝
 */
public class RechargePayAty extends BaseActivity implements WXPayResult.WXPayCallback {

    //支付宝
    @BindView(R.id.id_ali_check_img)
    ImageView aliCheckImg;
    //微信
    @BindView(R.id.id_weixin_check_img)
    ImageView weixinCheckImg;
    //充值的金额
    @BindView(R.id.id_pay_amount)
    TextView amountText;

    //3阿里，4微信
    String PAY_ALI_TYPE = Type.TYPE_ALI_PAY;

    WXPayResult result;

    //充值金额
    private String mRechargeMoney;
    private PayPresenter presenter;

    //充值金额
    public static void startRechargeAty(Activity activity, String money) {
        Intent intent = new Intent(activity, RechargePayAty.class);
        intent.putExtra(IntentKey.EXTRA_MONEY, money);
        activity.startActivity(intent);
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }


    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_recharge_pay, null);
    }

    @Override
    public void initPresenter() {
        presenter = new PayPresenter(true);
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.account_recharge));
        mRechargeMoney = getIntent().getStringExtra(IntentKey.EXTRA_MONEY);
        amountText.setText(Currency.returnDollar(Currency.RMB, (Double.parseDouble(mRechargeMoney) * 100) + "", 0).replace("元", ""));
    }

    @Override
    public void initData() {
        result = new WXPayResult();
        result.setPayCallback(this);
    }

    @Override
    public void onLoadingStatus(int loadingStatus) {
        super.onLoadingStatus(loadingStatus);
        switch (loadingStatus) {
            case CommonCode.PayCode.PAY_SUCCESS:
                SVProgressHUD.dismissImmediately(mContext);
                SVProgressHUD.showSuccessWithStatus(this, "支付成功");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaseEvent event = new BaseEvent();
                        event.info = "0";
                        EventBus.getDefault().post(event);
                        finish();
                    }
                }, 2000);
                break;
            case CommonCode.PayCode.PAY_FAIL:
                SVProgressHUD.dismissImmediately(mContext);
                SVProgressHUD.showErrorWithStatus(this, " 支付失败");
                break;
            case CommonCode.PayCode.PAY_ABNORMAL:
                SVProgressHUD.dismissImmediately(mContext);
                SVProgressHUD.showErrorWithStatus(this, " 支付异常");
                break;
            case CommonCode.PayCode.PAY_CANDEL:
                SVProgressHUD.dismissImmediately(mContext);
                SVProgressHUD.showErrorWithStatus(this, " 支付取消");
                break;
        }
    }

    //处理微信回调
    @Override
    public void callbackState(int status) {
        switch (status) {
            case CommonCode.PayCode.PAY_SUCCESS:
                SVProgressHUD.dismissImmediately(mContext);
                SVProgressHUD.showSuccessWithStatus(this, "支付成功");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaseEvent event = new BaseEvent();
                        event.info = "0";
                        EventBus.getDefault().post(event);
                        finish();
                    }
                }, 2000);
                break;
            case CommonCode.PayCode.PAY_FAIL:
                SVProgressHUD.showErrorWithStatus(this, " 支付失败");
                break;
            case CommonCode.PayCode.PAY_ABNORMAL:
                SVProgressHUD.showErrorWithStatus(this, " 支付异常");
                break;
            case CommonCode.PayCode.PAY_CANDEL:
                SVProgressHUD.showErrorWithStatus(this, " 支付取消");
                break;
        }

    }


    @OnClick({R.id.id_ali_pay_layout, R.id.id_weixin_pay_layout, R.id.id_pay_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            //支付宝
            case R.id.id_ali_pay_layout:
                aliCheckImg.setImageResource(R.drawable.ic_pay_checked);
                weixinCheckImg.setImageResource(R.drawable.ic_pay_unchecked);
                PAY_ALI_TYPE = Type.TYPE_ALI_PAY;
                break;
            //微信支付
            case R.id.id_weixin_pay_layout:
                aliCheckImg.setImageResource(R.drawable.ic_pay_unchecked);
                weixinCheckImg.setImageResource(R.drawable.ic_pay_checked);
                PAY_ALI_TYPE = Type.TYPE_WX_PAY;
                break;
            //充值
            case R.id.id_pay_btn:
                if (PAY_ALI_TYPE == Type.TYPE_WX_PAY) {
                    SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
                    UMShareAPI mShareAPI = UMShareAPI.get(aty);
                    boolean install = mShareAPI.isInstall(aty, platform);
                    if (install) {
                        rechargeMoney();
                    } else {
                        SVProgressHUD.showInfoWithStatus(aty, getString(R.string.no_install_wx));
                    }
                } else {
                    rechargeMoney();
                }
                break;
        }
    }

    public void rechargeMoney() {
        if (CountDownTimer.isFastClick()) {
            return;
        }
        if (!StringUtils.isEmpty(mRechargeMoney)) {
            ((PayPresenter) presenter).pay(Session.getUserId(), PAY_ALI_TYPE, (Double.parseDouble(mRechargeMoney) * 100 + ""), Currency.RMB);
        } else {
            SVProgressHUD.showInfoWithStatus(aty, "充值金额有误");
        }
    }
}
