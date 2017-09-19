package com.rz.circled.ui.fragment;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.DynamicAdapter;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.circled.widget.MListView;
import com.rz.common.cache.preference.Session;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.CircleDynamic;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class MyArticleFragment extends BaseFragment {

    @BindView(R.id.swipyrefreshlayout)
    SwipyRefreshLayout refreshLayout;
    @BindView(R.id.my_listview)
    MListView mListView;

    private DynamicAdapter dynamicAdapter;
    private List<CircleDynamic> circleDynamicList = new ArrayList<>();
    private IPresenter presenter;

    public static MyArticleFragment newInstance() {
        MyArticleFragment frg = new MyArticleFragment();
        return frg;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_article, null);
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new PersonInfoPresenter();
        presenter.attachView(this);

        ((PersonInfoPresenter) presenter).getArticle(false, Session.getUserId() ,"1000");

    }
    private void initRefresh() {
        refreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    ((PersonInfoPresenter) presenter).getArticle(false, Session.getUserId() ,"1000");
                } else {
                    ((PersonInfoPresenter) presenter).getArticle(true, Session.getUserId() ,"1000");
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void initView() {
        dynamicAdapter = new DynamicAdapter(mActivity, circleDynamicList);
        mListView.setAdapter(dynamicAdapter);
    }

    @Override
    public void initData() {
        initRefresh();
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        List<CircleDynamic> mDatas = (List<CircleDynamic>) t;
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
        return false;
    }

    @Override
    protected boolean hasDataInPage() {
        return dynamicAdapter != null && dynamicAdapter.getCount() != 0;
    }

}
