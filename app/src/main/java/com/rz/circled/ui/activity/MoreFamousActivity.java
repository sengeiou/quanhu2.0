package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.rz.circled.R;
import com.rz.circled.adapter.SearchPersonAdapter;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.httpapi.bean.StarListBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/4/004.
 */

public class MoreFamousActivity extends BaseActivity {
    @BindView(R.id.lv_m_famous)
    ListView mLvMFamous;
    private List<StarListBean> moreFamousList = new ArrayList<>();
    private SearchPersonAdapter mAdapter;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.more_famous_layout, null);
    }

    @Override
    public void initPresenter() {
        CirclePresenter presenter=new CirclePresenter();
        presenter.attachView(this);
        presenter.getMoreFamousList();

    }

    @Override
    public void initView() {
        mAdapter = new SearchPersonAdapter(this, R.layout.item_search_person);
        mAdapter.setTalentPage(true);
        mLvMFamous.setAdapter(mAdapter);

    }

    @Override
    public void initData() {

    }

    @Override
    public <T> void updateView(T t) {
        moreFamousList.addAll((Collection<? extends StarListBean>) t);
        mAdapter.setData(moreFamousList);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
