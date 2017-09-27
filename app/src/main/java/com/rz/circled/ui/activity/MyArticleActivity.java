package com.rz.circled.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.DynamicAdapter;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.circled.widget.CommomUtils;
import com.rz.circled.widget.MListView;
import com.rz.common.cache.preference.Session;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.CircleDynamic;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class MyArticleActivity extends BaseActivity {

    @BindView(R.id.swipyrefreshlayout)
    SwipyRefreshLayout refreshLayout;
    @BindView(R.id.my_listview)
    MListView mListView;

    private DynamicAdapter dynamicAdapter;
    private List<CircleDynamic> circleDynamicList = new ArrayList<>();
    private IPresenter presenter;

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_article, null);
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.article_info));
        dynamicAdapter = new DynamicAdapter(this, circleDynamicList);
        mListView.setAdapter(dynamicAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (StringUtils.isEmpty(circleDynamicList.get(position).coterieId)||StringUtils.isEmpty(circleDynamicList.get(position).coterieName)) {
                    String circleUrl = CommomUtils.getCircleUrl(circleDynamicList.get(position).circleRoute,circleDynamicList.get(position).moduleEnum, circleDynamicList.get(position).resourceId);
                    WebContainerActivity.startActivity(MyArticleActivity.this, circleUrl);
                } else {
                    String url = CommomUtils.getDymanicUrl(circleDynamicList.get(position).circleRoute,circleDynamicList.get(position).moduleEnum, circleDynamicList.get(position).coterieId, circleDynamicList.get(position).resourceId);
                    WebContainerActivity.startActivity(MyArticleActivity.this, url);
                }
            }
        });
    }

    @Override
    public void initData() {
        initRefresh();
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
        return true;
    }

    @Override
    protected boolean hasDataInPage() {
        return dynamicAdapter != null && dynamicAdapter.getCount() != 0;
    }

    @Override
    public void refreshPage() {
        ((PersonInfoPresenter) presenter).getArticle(false, Session.getUserId() ,"1000");

    }
}
