package com.rz.circled.ui.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.common.ui.fragment.BaseFragment;

import butterknife.OnClick;

/**
 * Created by Gsm on 2017/8/29.
 */
public class HomeFragment extends BaseFragment {

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.tv_home_web)
    public void onClick() {
        startActivity(new Intent(getActivity(), WebContainerActivity.class));
    }
}
