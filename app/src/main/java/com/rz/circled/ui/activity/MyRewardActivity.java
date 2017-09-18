package com.rz.circled.ui.activity;

import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.circled.adapter.SearchRewardAdapter;
import com.rz.common.ui.activity.BaseActivity;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public class MyRewardActivity extends BaseActivity {

    private SearchRewardAdapter rewardAdapter;


    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_my_private_group, null, false);
    }


    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
