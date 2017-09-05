package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import com.rz.circled.R;
import com.rz.circled.adapter.SearchCircleAdapter;
import com.rz.common.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rzw2 on 2017/9/5.
 */

public class ApplyForPrivateGroupBelongActivity extends BaseActivity {
    @BindView(R.id.gv)
    GridView gv;

    private SearchCircleAdapter mAdapter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_apply_for_private_group_belong, null);
    }

    @Override
    public void initView() {
        gv.setAdapter(mAdapter = new SearchCircleAdapter(mContext, R.layout.item_choose_circle));
    }

    @Override
    public void initData() {

    }
}
