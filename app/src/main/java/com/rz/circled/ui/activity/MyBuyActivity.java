package com.rz.circled.ui.activity;

import android.view.LayoutInflater;
import android.view.View;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.BuyingAdapter;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.circled.widget.MListView;
import com.rz.common.cache.preference.Session;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.httpapi.bean.MyBuyingModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class MyBuyActivity extends BaseActivity {

    @BindView(R.id.swipyrefreshlayout)
    SwipyRefreshLayout refreshLayout;
    @BindView(R.id.my_listview)
    MListView mListView;

    private BuyingAdapter dynamicAdapter;
    private List<MyBuyingModel> circleDynamicList = new ArrayList<>();
    private IPresenter presenter;

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_article, null);
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.my_purchase));
        dynamicAdapter = new BuyingAdapter(this, circleDynamicList);
        mListView.setAdapter(dynamicAdapter);
    }

    @Override
    public void initData() {
        initRefresh();

        ((PersonInfoPresenter) presenter).getMybuy(false, Session.getUserId());
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new PersonInfoPresenter();
        presenter.attachView(this);

    }

    private void initRefresh() {
        refreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    ((PersonInfoPresenter) presenter).getMybuy(false, Session.getUserId());
                } else {
                    ((PersonInfoPresenter) presenter).getMybuy(true, Session.getUserId());
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        List<MyBuyingModel> mDatas = (List<MyBuyingModel>) t;
        if (null != mDatas && !mDatas.isEmpty()) {
            if (!loadMore) {
                circleDynamicList.clear();
            }
            circleDynamicList.addAll(mDatas);
            dynamicAdapter.notifyDataSetChanged();
        } else {
            if (!loadMore) {
                circleDynamicList.clear();
            }
            dynamicAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean hasDataInPage() {
        return dynamicAdapter != null && dynamicAdapter.getCount() != 0;
    }

    @Override
    protected boolean needSupportRefresh() {
        return true;
    }
    @Override
    public void refreshPage() {
        ((PersonInfoPresenter) presenter).getMybuy(false, Session.getUserId());
    }
}
