package com.rz.circled.ui.activity;

import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.common.ui.activity.BaseActivity;

import butterknife.OnClick;

import static com.rz.common.constant.H5Address.ACCOUNT_RULE;


/**
 * Created by rzw2 on 2016/6/28.
 * 我的账号
 */
public class MyAccountAty extends BaseActivity {

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_my_account, null);
    }

    @Override
    public void initView() {
        setTitleText(R.string.mine_my_account);
        setTitleRightText("账户规则");
        setTitleRightTextColor(R.color.black);
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonH5Activity.startCommonH5(mContext,"",ACCOUNT_RULE);
            }
        });
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.id_layout_cost_account, R.id.id_layout_award_score, R.id.btn_bank})
    public void onClick(View view) {
        switch (view.getId()) {
            //消费账户
            case R.id.id_layout_cost_account:
                showActivity(aty, MyBalanceAty.class);
                break;
            //
            case R.id.id_layout_award_score:
                showActivity(this, RewardScoreAty.class);
                break;
            case R.id.btn_bank:
                BankCardListAty.startBankCardList(aty, 1);
                break;
        }
    }

    @Override
    public void refreshPage() {

    }
}
