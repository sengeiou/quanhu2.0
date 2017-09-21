package com.rz.circled.ui.activity;

import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.SnsAuthPresenter;
import com.rz.common.ui.activity.BaseActivity;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class LoginCodeActivity extends BaseActivity {

    private IPresenter presenter;

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_login, null);
    }

    @Override
    public void initPresenter() {
        presenter = new SnsAuthPresenter();
        presenter.attachView(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void refreshPage() {

    }
}
