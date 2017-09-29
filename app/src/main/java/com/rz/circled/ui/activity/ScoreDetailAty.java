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
import com.rz.httpapi.bean.ScoreBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rz.common.constant.H5Address.INTEGRAL_STATEGY;

/**
 * Created by Administrator on 2016/7/20 0020.
 * 消费明细或者收益明细
 */
public class ScoreDetailAty extends BaseActivity {

    @BindView(R.id.lv_detail)
    ListView mListView;
    @BindView(R.id.income)
    TextView mIncome;
    @BindView(R.id.produce_type)
    TextView mProduceType;
    @BindView(R.id.refresh)
    SwipyRefreshLayoutBanner mRefresh;

    private CommonAdapter<ScoreBean> mAdapter;

    //存储消费明细信息
    List<ScoreBean> mBillDetails = new ArrayList<ScoreBean>();

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
        Intent intent = new Intent(activity, ScoreDetailAty.class);
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
            setTitleText(R.string.jf_details);
            mIncome.setText(R.string.income); mProduceType.setText(R.string.mingcheng);
        } else if (type == Type.TYPE_SCORE) {
            setTitleRightText("积分获取攻略");
            setTitleRightTextColor(R.color.black);
            setTitleRightListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonH5Activity.startCommonH5(mContext,"",INTEGRAL_STATEGY);
                }
            });
            setTitleText(R.string.jf_details);
            mIncome.setText(R.string.jifen);
            mProduceType.setText(R.string.shuoming);
        }
        mAdapter = new CommonAdapter<ScoreBean>(aty, mBillDetails, R.layout.layout_account_detail_item) {
            @Override
            public void convert(ViewHolder helper, ScoreBean item) {
                helper.setText(R.id.id_tv_name, item.getEventName());
                TextView mPay = (TextView) helper.getViewById(R.id.id_tv_cost);
                //0，扣费；1，加费
//                int orderType = item.orderType;
//                if (orderType == 0) {
//                    mPay.setText("-" + Currency.returnDollar(Currency.RMB, item.getAllScore()+"", 0));
//                    mPay.setTextColor(Color.parseColor("#FF6060"));
//                } else if (orderType == 1) {
                    mPay.setText("+" + item.getNewScore()+"");
                    mPay.setTextColor(Color.parseColor("#0185ff"));
//                }
//                SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
                String data = item.getCreateTime().substring(0, 10);
                helper.setText(R.id.id_tv_date, data);
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setDividerHeight(0);
    }

    @Override
    public void initData() {
        mPresenter.requestGetScoreList(false);

        mRefresh.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
        mRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                mPresenter.requestGetRewardList(direction != SwipyRefreshLayoutDirection.TOP);
                mRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        if (null != t) {
            List<ScoreBean> bills = (List<ScoreBean>) t;
            if (null != bills && !bills.isEmpty()) {
                if (!loadMore) {
                    mBillDetails.clear();
                }
                for (ScoreBean model : bills) {
                    if (model.getAllScore() > 0) {
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
        mPresenter.requestGetScoreList(false);
    }
}
