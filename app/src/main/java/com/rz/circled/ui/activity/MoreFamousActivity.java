package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.rz.circled.R;
import com.rz.circled.adapter.SearchPersonAdapter;
import com.rz.common.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/4/004.
 */

public class MoreFamousActivity extends BaseActivity {
    @BindView(R.id.lv_m_famous)
    ListView mLvMFamous;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.more_famous_layout, null);
    }

    @Override
    public void initView() {
        SearchPersonAdapter adapter = new SearchPersonAdapter(this, R.layout.item_search_person);
        adapter.setTalentPage(true);
        mLvMFamous.setAdapter(adapter);

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
