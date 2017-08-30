package com.rz.circled.ui.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.rz.rz_rrz.R;
import com.rz.rz_rrz.cache.preference.Session;
import com.rz.rz_rrz.constant.H5Address;
import com.rz.rz_rrz.constant.IntentCode;
import com.rz.rz_rrz.constant.Type;
import com.rz.rz_rrz.model.AccountModel;
import com.rz.rz_rrz.presenter.impl.PayPresenter;
import com.rz.rz_rrz.utils.Currency;
import com.rz.rz_rrz.utils.StringUtils;
import com.rz.rz_rrz.view.base.BaseCommonAty;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/19 0019.
 * 奖励悠然币界面
 */
public class RewardScoreAty extends BaseCommonAty {

    //奖励悠然币
    @BindView(R.id.tv_score_usable)
    TextView mTvScoreUsable;
    @BindView(R.id.tv_reward_score_to_rule)
    TextView tvToRule;

    //记录用户的悠然币
    private String integralSum;

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    public View loadView(LayoutInflater inflater, View childView) {
        return super.loadView(inflater, inflater.inflate(R.layout.aty_reward_score, null));
    }

    @Override
    public void initPresenter() {
        presenter = new PayPresenter(false);
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.all_point));
        setTitleRight(getString(com.rz.rz_rrz.R.string.account_detail), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountDetailAty.startAccountDetail(aty, Type.TYPE_SCORE);
            }
        });
        setTitleRightColor(R.color.color_main);
    }

    @Override
    public void initData() {
        ((PayPresenter) presenter).getUserAccount(Session.getUserId(), getString(R.string.data_loading));
    }

    @Override
    public <T> void updateView(T t) {
        if (null != t) {
            AccountModel model = (AccountModel) t;
            if (null != model) {
                Session.setUserMoneyState(model.getAccountState() == 1 ? true : false);
                integralSum = model.getIntegralSum();
                mTvScoreUsable.setText(Currency.returnDollar(Currency.RMB, integralSum, 0));
            }
        }
    }

    @OnClick({R.id.id_btn_recharge, R.id.id_btn_to_bank, R.id.tv_reward_score_to_rule})
    public void onClick(View view) {
        switch (view.getId()) {
            //兑换到消费账户
            case R.id.id_btn_recharge:
                if (StringUtils.isEmpty(integralSum) || Double.parseDouble(integralSum) <= 0) {
                    SVProgressHUD.showInfoWithStatus(this, getString(com.rz.rz_rrz.R.string.duihuan_yrb));
                } else {
                    RechargeAty.startRecharge(aty, integralSum);
                }
                break;
            //提现
            case R.id.id_btn_to_bank:
                if (StringUtils.isEmpty(integralSum) || Double.parseDouble(integralSum) <= 0) {
                    SVProgressHUD.showInfoWithStatus(this, getString(com.rz.rz_rrz.R.string.tixian_yrb));
                } else {
                    ToBankCardAty.startBankCard(this, integralSum);
                }
                break;
            //平台奖励规则
            case R.id.tv_reward_score_to_rule:
                CommH5Aty.startCommonH5(aty, H5Address.REWARD_RULE, getString(com.rz.rz_rrz.R.string.yizhi));
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.RechargeMoney.RECHARGE_REQUEST_CODE) {
            if (resultCode == IntentCode.BankCard.BankCard_RESULT_CODE) {
                //重新获取余额
                ((PayPresenter) presenter).getUserAccount(Session.getUserId(), "");
            }
        }
    }
}
