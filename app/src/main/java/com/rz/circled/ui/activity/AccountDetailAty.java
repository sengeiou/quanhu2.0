package com.rz.circled.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.presenter.impl.PayPresenter;
import com.rz.circled.widget.CommonAdapter;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.Currency;
import com.rz.httpapi.bean.BillDetailModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/20 0020.
 * 消费明细或者收益明细
 */
public class AccountDetailAty extends BaseActivity {

    @BindView(R.id.lv_detail)
    ListView mListView;
    @BindView(R.id.income)
    TextView mIncome;
    @BindView(R.id.produce_type)
    TextView mProduceType;

    private CommonAdapter<BillDetailModel> mAdapter;

    //存储消费明细信息
    List<BillDetailModel> mBillDetails = new ArrayList<BillDetailModel>();

    //类别 1，消费流水；2，收益流水
    private int type;
    private PayPresenter mPresenter;

    /**
     * 消费明细或者收益明细
     *
     * @param activity 上下文
     * @param type     1 表示消费明细  2 表示收益明细
     */
    public static void startAccountDetail(Activity activity, int type) {
        Intent intent = new Intent(activity, AccountDetailAty.class);
        intent.putExtra(IntentKey.KEY_TYPE, type);
        activity.startActivity(intent);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_account_detail, null);
    }

    @Override
    public void initPresenter() {
        mPresenter = new PayPresenter(false);
        mPresenter.attachView(this);
    }

    @Override
    public void initView() {
        type = getIntent().getIntExtra(IntentKey.KEY_TYPE, Constants.DEFAULTVALUE);
        if (type == Type.TYPE_BALANCE) {
            setTitleText(R.string.cost_detail_v3);
            mIncome.setText(R.string.income);
            mProduceType.setText(R.string.mingcheng);
        } else if (type == Type.TYPE_SCORE) {
            setTitleText(R.string.jf_details);
            mIncome.setText(R.string.jifen);
            mProduceType.setText(R.string.shuoming);
        }
        mAdapter = new CommonAdapter<BillDetailModel>(aty, mBillDetails, R.layout.layout_account_detail_item) {
            @Override
            public void convert(ViewHolder helper, BillDetailModel item) {
                helper.setText(R.id.id_tv_name, item.productDesc);
                TextView mPay = (TextView) helper.getViewById(R.id.id_tv_cost);
                //0，扣费；1，加费
                int orderType = item.orderType;
                if (orderType == 0) {
                    mPay.setText("-" + Currency.returnDollar(Currency.RMB, item.cost, 0));
                    mPay.setTextColor(Color.parseColor("#1BC2B8"));
                } else if (orderType == 1) {
                    mPay.setText("+" + Currency.returnDollar(Currency.RMB, item.cost, 0));
                    mPay.setTextColor(Color.parseColor("#FF6060"));
                }
                SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
                String times = sdr.format(new Date(item.createTime * 1000L));
                helper.setText(R.id.id_tv_date, times);
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setDividerHeight(0);
    }

    @Override
    public void initData() {
        mPresenter.requestGetBillList(false, type);
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        if (null != t) {
            List<BillDetailModel> bills = (List<BillDetailModel>) t;
            if (null != bills && !bills.isEmpty()) {
                if (!loadMore) {
                    mBillDetails.clear();
                }
                for (BillDetailModel model : bills) {
//                    if (Double.parseDouble(model.cost) > 0) {
//                        mBillDetails.add(model);
//                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void refreshPage() {

    }
}
