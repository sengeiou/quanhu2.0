package com.rz.circled.ui.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rz.rz_rrz.R;
import com.rz.rz_rrz.cache.preference.Session;
import com.rz.rz_rrz.constant.IntentCode;
import com.rz.rz_rrz.constant.Type;
import com.rz.rz_rrz.model.AccountModel;
import com.rz.rz_rrz.presenter.impl.PayPresenter;
import com.rz.rz_rrz.utils.Currency;
import com.rz.rz_rrz.view.base.BaseCommonAty;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rzw2 on 2016/6/28.
 * 消费账户
 */
public class MyBalanceAty extends BaseCommonAty {

    //账户余额
    @BindView(R.id.id_account_money_txt)
    TextView mTxtAccount;

    @Override
    public View loadView(LayoutInflater inflater, View childView) {
        return super.loadView(inflater, inflater.inflate(R.layout.aty_my_balance, null));
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    public void initPresenter() {
        presenter = new PayPresenter(false);
    }

    @Override
    public void initView() {
        //消费明细
        setTitleRight(getString(R.string.cost_detail_v3), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountDetailAty.startAccountDetail(aty, Type.TYPE_BALANCE);
            }
        });
        setTitleRightColor(R.color.color_main);
    }

    @Override
    public void initData() {
        ((PayPresenter) presenter).getUserAccount(Session.getUserId(), getString(R.string.data_loading));
    }

    @OnClick(R.id.id_submit_btn)
    public void onClick() {
        //充值
        Intent intent = new Intent(aty, RechargeMoneyAty.class);
        startActivityForResult(intent, IntentCode.RechargeMoney.RECHARGE_REQUEST_CODE);
    }

    @Override
    public <T> void updateView(final T t) {
        if (null != t) {
            AccountModel model = (AccountModel) t;
            if (null != model) {
                Session.setUserMoneyState(model.getAccountState() == 1 ? true : false);
                mTxtAccount.setText(Currency.returnDollar(Currency.RMB, model.getAccountSum(), 0).replace(getString(R.string.yuan), ""));
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.RechargeMoney.RECHARGE_REQUEST_CODE) {
            if (resultCode == IntentCode.RechargeMoney.RECHARGE_RESULT_CODE) {
                //重新获取余额
                ((PayPresenter) presenter).getUserAccount(Session.getUserId(), "");
            }
        }
    }
}
