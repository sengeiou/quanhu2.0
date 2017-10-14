package com.rz.circled.ui.activity;

import android.content.Context;
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
import com.rz.circled.widget.SwipyRefreshLayoutBanner;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.swiperefresh.SwipyRefreshLayout;
import com.rz.common.swiperefresh.SwipyRefreshLayoutDirection;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.Currency;
import com.rz.httpapi.bean.BillDetailModel;

import java.util.ArrayList;
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
    @BindView(R.id.refresh)
    SwipyRefreshLayoutBanner mRefresh;

    private CommonAdapter<BillDetailModel> mAdapter;

    //存储消费明细信息
    List<BillDetailModel> mBillDetails = new ArrayList<BillDetailModel>();

    //类别 1，消费流水；2，收益流水
    private int type;
    private PayPresenter mPresenter;

    /**
     * 消费明细或者收益明细
     *
     * @param context 上下文
     * @param type    1 表示消费明细  2 表示收益明细
     */
    public static void startAccountDetail(Context context, int type) {
        Intent intent = new Intent(context, AccountDetailAty.class);
        intent.putExtra(IntentKey.KEY_TYPE, type);
        context.startActivity(intent);
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
            setTitleText(R.string.account_detail);
        } else if (type == Type.TYPE_SCORE) {
//            setTitleRightText("积分获取攻略");
//            setTitleRightTextColor(R.color.black);
//            setTitleRightListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    CommonH5Activity.startCommonH5(mContext,"",INTEGRAL_STATEGY);
//                }
//            });
            setTitleText(R.string.account_detail);
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
                    mPay.setTextColor(Color.parseColor("#FF6060"));
                } else if (orderType == 1) {
                    mPay.setText("+" + Currency.returnDollar(Currency.RMB, item.cost, 0));
                    mPay.setTextColor(Color.parseColor("#0185ff"));
                }
//                SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
                String data = item.createTime.substring(0, 10);
                helper.setText(R.id.id_tv_date, data);
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setDividerHeight(0);
    }

    @Override
    public void initData() {
        mPresenter.requestGetBillList(false, type);
        mRefresh.setColorSchemeColors(Constants.COLOR_SCHEMES);
        mRefresh.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                mPresenter.requestGetBillList(direction != SwipyRefreshLayoutDirection.TOP, type);
                mRefresh.setRefreshing(false);
            }
        });
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
                    if (Double.parseDouble(model.cost) > 0) {
                        mBillDetails.add(model);
                    }
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
        mPresenter.requestGetBillList(false, type);
    }

    @Override
    protected boolean hasDataInPage() {
        return mAdapter != null && mAdapter.getCount() != 0;
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }
}
