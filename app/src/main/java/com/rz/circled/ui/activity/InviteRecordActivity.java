package com.rz.circled.ui.activity;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.adapter.InviteRecordAdapter;
import com.rz.circled.presenter.impl.AccountPresenter;
import com.rz.common.constant.Constants;
import com.rz.common.swiperefresh.SwipyRefreshLayout;
import com.rz.common.swiperefresh.SwipyRefreshLayoutDirection;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.httpapi.bean.InviteRecordBean;

import butterknife.BindView;

/**
 * Created by rzw2 on 2017/11/20.
 */

public class InviteRecordActivity extends BaseActivity {
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.refresh)
    SwipyRefreshLayout refresh;

    //私圈相关
    private AccountPresenter mPresenter;
    private InviteRecordAdapter mAdapter;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_invite_record, null);
    }

    @Override
    public void initPresenter() {
        mPresenter = new AccountPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void initView() {
        setTitleText("我的邀请");
        SpannableString msp = new SpannableString("已邀请0位好友");
        //设置字体前景色
        msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_color_blue)), 3, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
        tvNum.setText(msp);
        lv.setAdapter(mAdapter = new InviteRecordAdapter(mContext, R.layout.item_invite_reward));
        refresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(com.rz.common.swiperefresh.SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    loadData(false);
                } else {
                    loadData(true);
                }
            }
        });
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean hasDataInPage() {
        return mAdapter != null && mAdapter.getCount() != 0;
    }

    @Override
    public void initData() {
        loadData(false);
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        if (null != t) {
            if (t instanceof InviteRecordBean) {
                InviteRecordBean data = (InviteRecordBean) t;
                if (data.getTotal() != 0) {
                    SpannableString msp = new SpannableString("已邀请" + data.getTotal() + "位好友");
                    //设置字体前景色
                    msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_color_blue)), 3, 3 + String.valueOf(data.getTotal()).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvNum.setText(msp);
                }
                if (data.getInviterDetail() != null) {
                    if (loadMore) {
                        mAdapter.addData(data.getInviterDetail());
                    } else {
                        mAdapter.setData(data.getInviterDetail());
                    }
                }
            }
        }
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        super.onLoadingStatus(loadingStatus, string);
        refresh.setRefreshing(false);
    }

    private void loadData(final boolean loadMore) {
        mPresenter.getInviteRecord(loadMore ? (hasDataInPage() ? mAdapter.getItem(mAdapter.getCount() - 1).getInviterId() : null) : null, Constants.PAGESIZE);
    }

    @Override
    public void refreshPage() {
        loadData(false);
    }

}
