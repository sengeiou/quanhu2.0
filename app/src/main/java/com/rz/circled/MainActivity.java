package com.rz.circled;

import android.view.LayoutInflater;
import android.view.View;

import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.rz.circled.presenter.UserPresenter;
import com.rz.common.ui.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_main, null);
    }

    @Override
    public void initView() {
        final UserPresenter userPresenter = new UserPresenter();
        userPresenter.attachView(this);

        findViewById(R.id.tv_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPresenter.login(1003, "15100000001", HexUtil.encodeHexStr(MD5Util.md5("a123456")));
            }
        });
        findViewById(R.id.tv_main_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPresenter.login(1003, "15100000001", "a123456");
            }
        });
    }

    @Override
    public void initData() {

    }
}
