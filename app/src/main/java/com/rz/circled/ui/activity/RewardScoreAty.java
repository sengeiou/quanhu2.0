package com.rz.circled.ui.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.presenter.impl.PayPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.Type;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.Currency;
import com.rz.httpapi.bean.AccountBean;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * Created by Administrator on 2016/7/19 0019.
 * 奖励悠然币界面
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
        setTitleText(getString(R.string.all_point));
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

    @OnClick({R.id.tv_reward_score_to_rule})
    public void onClick(View view) {
        switch (view.getId()) {
            //平台奖励规则
            case R.id.tv_reward_score_to_rule:
//                CommH5Aty.startCommonH5(aty, H5Address.REWARD_RULE, getString(R.string.yizhi));
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.RechargeMoney.RECHARGE_REQUEST_CODE) {
            if (resultCode == IntentCode.BankCard.BankCard_RESULT_CODE) {
                //重新获取余额
//                ((PayPresenter) presenter).getUserAccount(Session.getUserId(), "");
            }
        }
    }
}
