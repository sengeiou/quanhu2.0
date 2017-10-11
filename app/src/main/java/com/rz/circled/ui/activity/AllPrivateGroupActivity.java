package com.rz.circled.ui.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.rz.circled.R;
import com.rz.circled.ui.fragment.PrivateGroupAllFragment;
import com.rz.common.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rzw2 on 2017/9/2.
 */

public class AllPrivateGroupActivity extends BaseActivity {

    @BindView(R.id.layout_frame)
    FrameLayout layoutFrame;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_all_private_group, null, false);
    }

    @Override
    public void initView() {
        setTitleText(R.string.private_group_all);
    }

    @Override
    public void initData() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_frame, PrivateGroupAllFragment.newInstance());
        transaction.commit();
    }

    @OnClick(R.id.tv_search)
    public void onClick() {
        SearchActivity.stratActivity(mContext, SearchActivity.TYPE_PRIVATE);
    }

    @Override
    public void refreshPage() {

    }
}
