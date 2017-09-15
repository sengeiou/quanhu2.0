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
 * Created by rzw2 on 2016/6/28.
 * 消费账户
 */
public class MyBalanceAty extends BaseActivity {

    //账户余额
    @BindView(R.id.id_account_money_txt)
    TextView mTxtAccount;
    private PayPresenter mPresenter;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_my_balance, null);
    }
    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    public void initPresenter() {
        mPresenter = new PayPresenter(false);
        mPresenter.attachView(this);
    }

    @Override
    public void initView() {
        //消费明细
        setTitleRightText(getString(R.string.cost_detail_v3));
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                AccountDetailAty.startAccountDetail(aty, Type.TYPE_BALANCE);
            }
        });
        setTitleRightTextColor(R.color.color_main);
    }

    @Override
    public void initData() {
        mPresenter.getUserAccount(Session.getUserId(), getString(R.string.data_loading));
    }

    @OnClick(R.id.id_submit_btn)
    public void onClick() {
        //充值
//        Intent intent = new Intent(aty, RechargeMoneyAty.class);
//        startActivityForResult(intent, IntentCode.RechargeMoney.RECHARGE_REQUEST_CODE);
    }

    @Override
    public <T> void updateView(final T t) {
        if (null != t) {
            AccountBean model = (AccountBean) t;
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
//                ((PayPresenter) presenter).getUserAccount(Session.getUserId(), "");
            }
        }
    }
}
