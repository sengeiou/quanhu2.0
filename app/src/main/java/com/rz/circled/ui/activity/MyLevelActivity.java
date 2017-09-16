package com.rz.circled.ui.activity;

import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.common.ui.activity.BaseActivity;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class MyLevelActivity extends BaseActivity {
    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.my_level_layout,null);
    }

    @Override
    public void initPresenter() {
        CirclePresenter presenter =new CirclePresenter();
        presenter.attachView(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
