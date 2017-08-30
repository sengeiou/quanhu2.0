package com.rz.circled.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.rz.rz_rrz.R;
import com.rz.rz_rrz.constant.Constants;
import com.rz.rz_rrz.constant.IntentKey;
import com.rz.rz_rrz.constant.Type;
import com.rz.rz_rrz.model.BillDetailModel;
import com.rz.rz_rrz.presenter.impl.PayPresenter;
import com.rz.rz_rrz.utils.Currency;
import com.rz.rz_rrz.view.base.BaseCommonAty;
import com.rz.rz_rrz.widget.CommonAdapter;
import com.rz.rz_rrz.widget.ViewHolder;
import com.rz.rz_rrz.widget.swiperefresh.SwipyRefreshLayout;
import com.rz.rz_rrz.widget.swiperefresh.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.xiaomi.push.thrift.a.R;

/**
 * Created by Administrator on 2016/7/20 0020.
 * 消费明细或者收益明细
 */
public class AccountDetailAty extends BaseCommonAty implements SwipyRefreshLayout.OnRefreshListener, BaseCommonAty.IRefreshPageListener {

    @BindView(R.id.id_comm_listview)
    ListView mListView;

    @BindView(R.id.id_comm_refresh_ll)
    SwipyRefreshLayout mSwipyRefreshLayout;

    private CommonAdapter<BillDetailModel> mAdapter;

    //存储消费明细信息
    List<BillDetailModel> mBillDetails = new ArrayList<BillDetailModel>();

    //类别 1，消费流水；2，收益流水
    private int type;

    /**
     * 消费明细或者收益明细
     *
     * @param activity 上下文
     * @param type     1 表示消费明细  2 表示收益明细
     */
    public static void startAccountDetail(Activity activity, int type) {
        Intent intent = new Intent(activity, AccountDetailAty.class);
        intent.putExtra(IntentKey.General.KEY_TYPE, type);
        activity.startActivity(intent);
    }

    @Override
    public boolean hasDataInPage() {
        return null != mBillDetails && !mBillDetails.isEmpty();
    }

    @Override
    public View loadView(LayoutInflater inflater, View childView) {
        return super.loadView(inflater, inflater.inflate(R.layout.aty_account_detail, null));
    }

    @Override
    public void initPresenter() {
        presenter = new PayPresenter(false);
    }

    @Override
    public void initView() {
        type = getIntent().getIntExtra(IntentKey.General.KEY_TYPE, Constants.DEFAULTVALUE);
        if (type == Type.TYPE_BALANCE) {
            setTitleText(R.string.cost_detail_v3);
        } else if (type == Type.TYPE_SCORE) {
            setTitleText(R.string.account_details);
        }
        // 设置刷新时动画的颜色，可以设置4个
        mSwipyRefreshLayout.setColorSchemeColors(Constants.COLOR_SCHEMES);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(this);
        setOnRefreshDataListener(this);
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
                helper.setText(R.id.id_tv_date, item.createTime);
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setDividerHeight(0);
    }

    public String setProduct(String productType) {
        if (TextUtils.equals("1000", productType)) {
            return getString(R.string.user_dasang);
        } else if (TextUtils.equals("1001", productType)) {
            return getString(R.string.lucky_money);
        } else if (TextUtils.equals("1002", productType)) {
            return getString(R.string.exchange);
        } else if (TextUtils.equals("1003", productType)) {
            return getString(R.string.winning);
        } else if (TextUtils.equals("1004", productType)) {
            return getString(R.string.forwarding);
        } else if (TextUtils.equals("1005", productType)) {
            return getString(R.string.advertise);
        } else if (TextUtils.equals("1006", productType)) {
            return getString(R.string.refunds);
        } else if (TextUtils.equals("1007", productType)) {
            return getString(R.string.withdraw_refunds);
        } else if (TextUtils.equals("1008", productType)) {
            return getString(R.string.red_envelope_expired);
        } else if (TextUtils.equals("2000", productType)) {
            return getString(R.string.recharge);
        } else if (TextUtils.equals("2001", productType)) {
            return getString(R.string.withdraw);
        } else if (TextUtils.equals("2002", productType)) {
            return getString(R.string.recharge_fee);
        } else if (TextUtils.equals("2003", productType)) {
            return getString(R.string.withdraw_fee);
        } else if (TextUtils.equals("3001", productType)) {
            return getString(R.string.register_activities);
        }
        return "";
    }

    @Override
    public void initData() {
        ((PayPresenter) presenter).requestGetBillList(false, type);
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        mSwipyRefreshLayout.setRefreshing(false);
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
    public void onLoadingStatus(int loadingStatus, String string) {
        mSwipyRefreshLayout.setRefreshing(false);
        super.onLoadingStatus(loadingStatus, string);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        ((PayPresenter) presenter).requestGetBillList(direction != SwipyRefreshLayoutDirection.TOP, type);
    }

    @Override
    public void refreshPage() {
        ((PayPresenter) presenter).requestGetBillList(false, type);
    }
}
