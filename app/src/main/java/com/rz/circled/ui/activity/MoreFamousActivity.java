package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
    @BindView(R.id.et_search_keyword_base)
    EditText mEtSearchKeywordBase;
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
        setTitleText("达人");
        View inflate = View.inflate(mContext, R.layout.item_footer, null);
        inflate.findViewById(R.id.view_dd).setVisibility(View.GONE);
        mLvMFamous.addFooterView(inflate);
        mAdapter = new SearchPersonAdapter(this, R.layout.item_search_person);
        mAdapter.setTalentPage(true);
        mLvMFamous.setDividerHeight(0);
        mLvMFamous.setAdapter(mAdapter);
        mLvMFamous.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String custId = moreFamousList.get(position).custInfo.getCustId();
                UserInfoActivity.newFrindInfo(mContext,custId);
            }
        });

    }

    @Override
    public void initData() {
        mEtSearchKeywordBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(SearchActivity.SEARCH_TYPE,SearchActivity.TYPE_PERSON);
//                skipActivity(aty,SearchActivity.class,bundle);
                SearchActivity.stratActivity(mContext,SearchActivity.TYPE_PERSON);
            }
        });

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

    @Override
    public void refreshPage() {

    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean hasDataInPage() {
        return mAdapter!=null&&mAdapter.getCount()!=0;
    }
}
