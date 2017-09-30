package com.rz.circled.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.presenter.impl.PayPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.H5Address;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.Type;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.Currency;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.bean.AccountBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/19 0019.
 * 收益账户
 */
public class RewardScoreAty extends BaseActivity {

    //奖励悠然币
    @BindView(R.id.tv_score_usable)
    TextView mTvScoreUsable;
    @BindView(R.id.tv_reward_score_to_rule)
    TextView tvToRule;

    //记录用户的悠然币
    private String integralSum;
    private PayPresenter mPresenter;

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_reward_score, null);
    }

    @Override
    public void initPresenter() {
        mPresenter = new PayPresenter(false);
        mPresenter.attachView(this);
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.income_account));
        setTitleRightText(getString(R.string.account_detail));
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountDetailAty.startAccountDetail(aty, Type.TYPE_SCORE);
            }
        });
        setTitleRightTextColor(R.color.color_main);
    }

    @Override
    public void initData() {
        mPresenter.getUserAccount(Session.getUserId(), getString(R.string.data_loading));
    }

    @Override
    public <T> void updateView(T t) {
        if (null != t) {
            AccountBean model = (AccountBean) t;
            if (null != model) {
                Session.setUserMoneyState(model.getAccountState() == 1 ? true : false);
                integralSum = model.getIntegralSum();
                mTvScoreUsable.setText(Currency.returnDollar(Currency.RMB, integralSum, 0));
            }
        }
    }

    @OnClick({R.id.tv_reward_score_to_rule, R.id.id_btn_recharge, R.id.id_btn_to_bank})
    public void onClick(View view) {
        switch (view.getId()) {
            //兑换到消费账户
            case R.id.id_btn_recharge:
                if (TextUtils.isEmpty(integralSum) || Double.parseDouble(integralSum) <= 0) {
                    SVProgressHUD.showInfoWithStatus(this, getString(R.string.duihuan_yrb));
                } else {
                    RechargeAty.startRecharge(aty, integralSum);
                }
                break;
            //提现
            case R.id.id_btn_to_bank:
                if (TextUtils.isEmpty(integralSum) || Double.parseDouble(integralSum) <= 0) {
                    SVProgressHUD.showInfoWithStatus(this, getString(R.string.tixian_yrb));
                } else {
                    ToBankCardAty.startBankCard(this, integralSum);
                }
                break;
            //平台奖励规则
            case R.id.tv_reward_score_to_rule:
                CommonH5Activity.startCommonH5(aty, "圈乎", H5Address.REWARD_RULE);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.RechargeMoney.RECHARGE_REQUEST_CODE) {
            if (resultCode == IntentCode.BankCard.BankCard_RESULT_CODE) {
                //重新获取余额
                mPresenter.getUserAccount(Session.getUserId(), "");
            }
        }
    }

    @Override
    public void refreshPage() {

    }
}
