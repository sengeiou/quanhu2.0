package com.rz.circled.ui.activity;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.event.EventConstant;
import com.rz.circled.presenter.impl.PayPresenter;
import com.rz.circled.widget.MyGridView;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.H5Address;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.Currency;
import com.rz.common.utils.DensityUtils;
import com.rz.common.utils.DialogUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.utils.TextViewUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.bean.AccountBean;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：Administrator on 2016/9/9 0009 16:58
 * 功能：选充值金额
 * 说明：
 */
public class RechargeMoneyAty extends BaseActivity implements AdapterView.OnItemClickListener {

    //头像
//    @BindView(R.id.id_user_head_img)
//    ImageView mImgUserHead;

    //姓名
//    @BindView(R.id.id_user_name_txt)
//    TextView mTxtUserName;

    //
    @BindView(R.id.id_recharge_gridV)
    MyGridView myGridView;
    @BindView(R.id.tv_account)
    TextView tvAccount;

    private CommonAdapter<String> mAdapter;

    //记录选择充值的钱
    private String mRemarkMoney;

    //记录金额
    private List<String> mMoney = new ArrayList<String>();
    String[] money = new String[]{"5", "10", "20", "60", "120", "500", "1500", "2000"};

    private Dialog mDialog;
    private long mInvokeid;

    private PayPresenter mPayPresenter;

    @Override
    public void initPresenter() {
        super.initPresenter();
        mPayPresenter = new PayPresenter(false);
        mPayPresenter.attachView(this);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_recharge_money, null, false);
    }

    @Override
    public void initView() {
        final int width = DensityUtils.getScreenW(aty) / 2;
        setTitleText(getString(R.string.recharge_title));
//        if (Protect.checkLoadImageStatus(aty)) {
//            Glide.with(aty).load(Session.getUserPicUrl()).transform(new GlideRoundImage(aty)).
//                    placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(mImgUserHead);
//        }
//
//        mTxtUserName.setText(Session.getUserName());

        mMoney.addAll(Arrays.asList(money));
        mAdapter = new CommonAdapter<String>(this, mMoney, R.layout.adp_recharge_textview) {

            @Override
            public void convert(ViewHolder helper, String item, int position) {
                TextView mTxt = (TextView) helper.getViewById(R.id.id_recharge_money_txt_other);
                TextView mTxt1 = (TextView) helper.getViewById(R.id.id_recharge_money_txt);
                View view = helper.getView(R.id.layout);
                mTxt.setText(item + getString(R.string.rmb_yuan));
                mTxt1.setText(item + "元");
                if (checkMap.get(helper.getPosition(), false)) {
                    mTxt.setSelected(true);
                    view.setSelected(true);
//                    mTxt.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_stork_main));
//                    mTxt.setSelected(true);
//                    mTxt.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    mTxt.setSelected(false);
                    view.setSelected(false);
//                    mTxt.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_stork_prim));
//                    mTxt.setSelected(false);
//                    mTxt.setTextColor(Color.parseColor("#999999"));
                }
            }
        };
        myGridView.setAdapter(mAdapter);
        myGridView.setOnItemClickListener(this);

        tvAccount.setText(Session.getUserMoney() + "悠然币");
    }

    @Override
    public void initData() {
        mInvokeid = getIntent().getLongExtra(IntentKey.EXTRA_INVOKEID, -1);
        mPayPresenter.getUserAccount(Session.getUserId(), "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (null != t) {
            AccountBean model = (AccountBean) t;
            if (null != model) {
                double mUserMoney = Double.parseDouble(model.getAccountSum() + "");
                Session.setUserMoney(mUserMoney + "");
                if (Type.USER_MONEY_NORMAL == model.getAccountState()) {
                    Session.setUserMoneyState(true);
                } else {
                    Session.setUserMoneyState(false);
                }
                tvAccount.setText(Currency.returnDollar(Currency.RMB, model.getAccountSum() + "", 1));
            }
        }
    }


    //记录返回界面的状态
    @Subscribe
    public void onRefreshPageStatus(BaseEvent event) {
        if (EventConstant.PAY_RECHARGE_SUCCESS == event.getType()) {
            //支付成功
            setResult(IntentCode.RechargeMoney.RECHARGE_RESULT_CODE);
            callResult(true);
            finish();
        }
    }


    public void callResult(boolean result) {
        JsEvent.callJsEvent(mInvokeid, "8888", result ? BaseParamsObject.RESULT_CODE_SUCRESS : BaseParamsObject.RESULT_CODE_FAILED);
        finish();
    }


    @OnClick({R.id.id_recharge_btn, R.id.id_expain_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            //充值
            case R.id.id_recharge_btn:
                if (StringUtils.isEmpty(mRemarkMoney)) {
                    SVProgressHUD.showInfoWithStatus(aty, getString(R.string.choose_recharge_money));
                } else {
                    showRechargeDialog();
                }
                break;
            //充值遇到问题？请戳这里
            case R.id.id_expain_txt:
                CommonH5Activity.startCommonH5(aty, getString(R.string.app_name), H5Address.RECHARGE_INTRODUCE);
                break;
        }
    }

    //弹出选择货币弹出框
    public void showRechargeDialog() {
        View dialogView = LayoutInflater.from(aty).inflate(R.layout.dialog_recharge_view, null);
        mDialog = DialogUtils.selfDialog(aty, dialogView, true);
        mDialog.show();
        TextView mRemindTxt = (TextView) dialogView.findViewById(R.id.id_recharge_txt);
        TextView mMoneyTxt = (TextView) dialogView.findViewById(R.id.id_recharge_money_txt);
        mRemindTxt.setText("您是否愿意以 " + mRemarkMoney + "元 购买");
        TextViewUtils.setSpannableStyle((mRemarkMoney + "元悠然币？"), 0, (mRemarkMoney.length() + 4), mMoneyTxt);
        dialogView.findViewById(R.id.id_dialog_recharge_sure_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                RechargePayAty.startRechargeAty(aty, mRemarkMoney);
            }
        });
        dialogView.findViewById(R.id.id_dialog_rehcarge_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mAdapter.setCheckAtPosFalse(i, true);
        mRemarkMoney = (String) mAdapter.getItem(i);
    }

    @Override
    public void refreshPage() {

    }
}
