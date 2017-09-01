package com.rz.circled.ui.activity;

import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.common.ui.activity.BaseActivity;

import butterknife.OnClick;


/**
 * Created by rzw2 on 2016/6/28.
 * 我的账号
 */
public class MyAccountAty extends BaseActivity {

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_my_account, null);
    }

    @Override
    public void initView() {
        setTitleText(R.string.mine_my_account);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.id_layout_cost_account, R.id.id_layout_award_score, R.id.id_layout_red_bag, R.id.id_layout_bank_card})
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
            //红包记录
            case R.id.id_layout_red_bag:
//                Intent bag = new Intent(aty, MyReceiveMoneyListAty.class);
//                bag.putExtra(IntentKey.KEY_TYPE, Type.TYPE_RECORD_BAG);
//                startActivity(bag);
                break;
            //银行卡
            case R.id.id_layout_bank_card:
                BankCardListAty.startBankCardList(aty, 1);
                break;
        }
    }
}
