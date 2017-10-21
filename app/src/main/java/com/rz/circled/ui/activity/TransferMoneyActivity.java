package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.modle.AccountModel;
import com.rz.circled.modle.CircleTransferBean;
import com.rz.circled.modle.TransferModule;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.circled.presenter.impl.PayPresenter;
import com.rz.circled.widget.CommomUtils;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.event.NotifyEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.Currency;
import com.rz.common.utils.Protect;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.UserInfoModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：Administrator on 2016/8/26 0026 17:19
 * 功能：转发去赚钱
 * 说明：
 */
public class TransferMoneyActivity extends BaseActivity {

    //账户余额
    @BindView(R.id.id_user_money_txt)
    TextView mTxtUserMoney;

    //是否有转发券
    @BindView(R.id.id_forward_check_box)
    CheckBox mCheckBox;
    @BindView(R.id.tv_send_to_name)
    TextView tvSendToName;
    @BindView(R.id.tv_transfer_introduce)
    TextView tvTransferIntroduce;
    @BindView(R.id.id_circle_price_ll)
    LinearLayout idCirclePriceLl;
    @BindView(R.id.id_money_total_txt)
    TextView idMoneyTotalTxt;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.id_recharge_txt)
    TextView idRechargeTxt;
    @BindView(R.id.id_go_pay_btn_txt)
    TextView idGoPayBtnTxt;
    @BindView(R.id.iv_gift)
    ImageView ivGift;
    //记录转发券的金额
    private int mRecordCost;
    //记录转发券id
    private String mVoucherId;

    //红包类型
    private int packetType = Constants.DEFAULTVALUE;

    //用户的账户余额
    private double mUserMoney;

    //处理用户账户相关信息
    private PayPresenter mPayPresenter;

    public static final int REQUEST_MAKEMONEY = 2003;

    private boolean isCircle = false;

    @BindView(R.id.id_money_scroll)
    ScrollView mScrollView;

    public TransferModule transferModule;

    private CirclePresenter v3CirclePresenter;

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    protected boolean needSwipeBack() {
        return false;
    }

    public static void startV3TransferMoneyAty(Context context, TransferModule transferModule, boolean isCircle) {
        Intent i = new Intent(context, TransferMoneyActivity.class);
        i.putExtra(IntentKey.EXTRA_SERIALIZABLE, transferModule);
        i.putExtra(IntentKey.EXTRA_BOOLEAN, isCircle);
        context.startActivity(i);
    }


    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_transfer_money, null);
    }

    @Override
    public void initPresenter() {
        mPayPresenter = new PayPresenter(false);
        mPayPresenter.attachView(this);
        v3CirclePresenter = new CirclePresenter();
        v3CirclePresenter.attachView(this);
    }


    /**
     * 匹配转发券
     */
    public void matchVoucher(int cost) {
        if (cost > 0) {
            v3CirclePresenter.matchVoucher(cost + "", 0);
        }
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        transferModule = (TransferModule) intent.getSerializableExtra(IntentKey.EXTRA_SERIALIZABLE);
        isCircle = intent.getBooleanExtra(IntentKey.EXTRA_BOOLEAN, false);
        setTitleText(getString(R.string.pay_title));
        if (transferModule != null) {
            Log.d("yeying", "TransferModule " + transferModule.toString());
            if (transferModule.price > 0) {
                mRecordCost = (int) transferModule.price;
                matchVoucher(mRecordCost);
            }
            tvSendToName.setText(transferModule.custNname);
            tvTransferIntroduce.setText(CommomUtils.getGiftNameFromPrice(this, mRecordCost) + "   " + Currency.returnDollar(transferModule.price + ""));
            idMoneyTotalTxt.setText(Currency.returnDollarNoUnits(Currency.RMB, transferModule.price + "", 0));
            if (Protect.checkLoadImageStatus(this)) {
                Glide.with(this).load(CommomUtils.getGiftUrlFromPrice(this, transferModule.price)).
                        placeholder(R.drawable.v3_gift_flower_big).error(R.drawable.v3_gift_flower_big).into(ivGift);
            }
        }
    }

    @Override
    public void initData() {
        mPayPresenter.getUserAccount(Session.getUserId(), "");
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (null != t) {
            if (t instanceof UserInfoModel) {
                UserInfoModel user = (UserInfoModel) t;
                if (null != user) {
                    if (Type.HAD_SET_PW == user.getIsPayPassword()) {//是否设置支付密码 0未设置 1设置
                        Session.setUserSetpaypw(true);
                    } else {
                        Session.setUserSetpaypw(false);
                    }
                    if (StringUtils.isEmpty(user.getPhyName())) {//是否设置安全保护信息
                        Session.setUserSafetyproblem(false);
                    } else {
                        Session.setUserSafetyproblem(true);
                    }
                    if (Type.OPEN_EASY_PAY == user.getSmallNopass()) {//是否开启免密支付 0未开启 1开启
                        Session.setIsOpenGesture(true);
                    } else {
                        Session.setIsOpenGesture(false);
                    }
                    String total = Currency.returnDollarNoUnits(Currency.RMB, transferModule.price + "", 0);//本次支付的总金额
                    if (mCheckBox.getVisibility() == View.VISIBLE && mCheckBox.isChecked()) {
                        //检测是否开启免密支付
                        // ,单位为分
                        mPayPresenter.checkIsOpenEasyPay(Double.parseDouble(total) * 100, Double.parseDouble(total) * 100 + 1, getString(R.string.pay_amount), 2);
                    } else {
                        mPayPresenter.checkIsOpenEasyPay(Double.parseDouble(total) * 100, mUserMoney, getString(R.string.pay_amount), 0);
                    }
                }
            } else if (t instanceof AccountModel) {
                AccountModel model = (AccountModel) t;
                if (null != model) {
                    mUserMoney = Double.parseDouble(model.getAccountSum() + "");
                    Session.setUserMoney(mUserMoney + "");
                    if (Type.USER_MONEY_NORMAL == model.getAccountState()) {
                        Session.setUserMoneyState(true);
                    } else {
                        Session.setUserMoneyState(false);
                    }
                    mTxtUserMoney.setText(Currency.returnDollar(Currency.RMB, model.getAccountSum() + "", 0));
                }
            } else if (t instanceof String) {
                //去支付
                String pw = (String) t;
                if (StringUtils.isEmpty(pw)) {
                    pw = "";
                }
                if (mCheckBox.getVisibility() == View.VISIBLE && mCheckBox.isChecked()) {
                    //带转发券
                    v3CirclePresenter.circleTransfer(transferModule.parentId, transferModule.authorId, transferModule.appId, transferModule.moduleId, transferModule.infoId, transferModule.infoTitle, transferModule.infoDesc, transferModule.infoThumbnail, transferModule.infoPic, transferModule.infoVideo, transferModule.infoVideoPic, transferModule.price, pw, transferModule.infoCreateTime);
                } else {
                    //圈子转发
                    v3CirclePresenter.circleTransfer(transferModule.parentId, transferModule.authorId, transferModule.appId, transferModule.moduleId, transferModule.infoId, transferModule.infoTitle, transferModule.infoDesc, transferModule.infoThumbnail, transferModule.infoPic, transferModule.infoVideo, transferModule.infoVideoPic, transferModule.price, pw, transferModule.infoCreateTime);
                }
            } else if (t instanceof Integer) {
                Integer transferId = (Integer) t;
                //圈子转发成功
//                if (isFormCircle) {
//                ForwardModel model = (ForwardModel) t;
//                BaseEvent event = new BaseEvent();
//                event.key = "10";
//                event.event = model;
//                EventBus.getDefault().post(event);

                if (0 != transferId) {
                    EventBus.getDefault().post(new NotifyEvent("reward", null, true));
//                    ForwardCircleAty.startForwardCircle(aty, model, model.getUid(), redpacketId, isFormCircle, mUserName, mAuthorId, mShareModel);
//                    showActivity(this, V3TransferSucceedAty.class);
//                    V3TransferSucceedAty.startAty(this, transferModule.price, isCircle);
                    CircleTransferBean v3CircleTransfer = CircleTransferBean.getInstanceFromTransfer(transferId, transferModule);
                    EventBus.getDefault().post(new NotifyEvent(CommonCode.EventType.CIRCLE_FRAGMENT_REFRESH, v3CircleTransfer, true));
                    //TODO 发布完成后发送广播回显列表
                    finish();
                }
            }
//            else if (t instanceof Ticket) {
//                //有转发券
//                Ticket mTicket = (Ticket) t;
//                mCheckBox.setVisibility(View.VISIBLE);
//                mCheckBox.setText(Currency.returnDollar(mTicket.getCost()) + getString(R.string.forward_ticket_pay));
//                mRecordCost = Integer.parseInt(mTicket.getCost());
//                mVoucherId = mTicket.getVoucherId();
//                Log.d("huangqiang", mCheckBox.isChecked() + "[][][][][]11111111");
//                mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                        Log.d("huangqiang", b + "[][][][][]11111111");
////                        if (!b) {
////                            mTxtPayTotal.setText(Currency.returnDollar(Currency.RMB, (mOpusForwardPrice + ""), 0));
////                        } else {
////                            mTxtPayTotal.setText(Currency.returnDollar(Currency.RMB, "", 0));
////                        }
//                    }
//                });
//                mCheckBox.setChecked(true);
////                mTxtPayTotal.setText(Currency.returnDollar(Currency.RMB, "", 0));
//            }
        }
    }


    @OnClick({R.id.id_recharge_txt, R.id.id_go_pay_btn_txt, R.id.id_money_scroll})
    public void onClick(View view) {
        switch (view.getId()) {
            //充值
            case R.id.id_recharge_txt:
//                Intent intent = new Intent(aty, RechargeMoneyAty.class);
//                startActivityForResult(intent, IntentCode.RechargeMoney.RECHARGE_REQUEST_CODE);
                break;
            //支付
            case R.id.id_go_pay_btn_txt:
                if (CountDownTimer.isFastClick()) {
                    return;
                }
                //去支付
                mPayPresenter.isSettingPw(true);
                break;
            case R.id.id_money_scroll:
                mPayPresenter.hideInputMethod();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.RechargeMoney.RECHARGE_REQUEST_CODE) {
            if (resultCode == IntentCode.RechargeMoney.RECHARGE_RESULT_CODE) {
                //重新获取余额
                mPayPresenter.getUserAccount(Session.getUserId(), "");
            }
        }
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        if (TextUtils.equals(string, "error")) {
            if (isFinishing()) {
                return;
            }
            mPayPresenter.showResetDialog();
        } else {
            if (loadingStatus == CommonCode.General.DATA_EMPTY) {
                //没有匹配到转发券
                mCheckBox.setVisibility(View.GONE);
            } else {
                super.onLoadingStatus(loadingStatus, string);
            }
        }
    }

    @Override
    public void refreshPage() {

    }
}
