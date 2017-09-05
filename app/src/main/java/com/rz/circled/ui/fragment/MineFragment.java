package com.rz.circled.ui.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.ui.activity.LoginActivity;
import com.rz.common.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Gsm on 2017/8/29.
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.login_btn)
    Button loginBtn;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_mine, null);
    }

    @Override
    public void initView() {




    }

    @Override
    public void initData() {

    }


    @OnClick(R.id.login_btn)
    public void qqLogin() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

}
