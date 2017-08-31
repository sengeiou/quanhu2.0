package com.rz.circled.ui.activity;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.rz.circled.R;
import com.rz.circled.ui.fragment.FindFragment;
import com.rz.circled.ui.fragment.HomeFragment;
import com.rz.circled.ui.fragment.MineFragment;
import com.rz.circled.ui.fragment.PrivateCircledFragment;
import com.rz.circled.ui.fragment.RewardFragment;
import com.rz.circled.widget.CustomFragmentTabHost;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.ClickCounter;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener, CustomFragmentTabHost.InterceptTagChanged {

    @Nullable
    @BindView(R.id.tab_main)
    CustomFragmentTabHost tabHost;
    private ClickCounter mCounter;
    private Toast mToast;

    private String[] tabTags = new String[]{"home", "find", "reward", "privateCircle", "mine"};

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_main, null);
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    public void initView() {
        tabHost.setup(this, getSupportFragmentManager(), R.id.fl_main_content);
        tabHost.setOnTabChangedListener(this);
        tabHost.setInterceptTagChanged(this);
        tabHost.getTabWidget().setDividerDrawable(null);

        tabHost.addTab(tabHost.newTabSpec(tabTags[0]).setIndicator(getLayoutInflater().inflate(R.layout.layout_main_tab_home, null)), HomeFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(tabTags[1]).setIndicator(getLayoutInflater().inflate(R.layout.layout_main_tab_find, null)), FindFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(tabTags[2]).setIndicator(getLayoutInflater().inflate(R.layout.layout_main_tab_reward, null)), RewardFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(tabTags[3]).setIndicator(getLayoutInflater().inflate(R.layout.layout_main_tab_private_circle, null)), PrivateCircledFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(tabTags[4]).setIndicator(getLayoutInflater().inflate(R.layout.layout_main_tab_mine, null)), MineFragment.class, null);

        initCounter();
    }

    @Override
    public void initData() {

    }

    private void initCounter() {
        mCounter = new ClickCounter(2, 2000);
        mCounter.setListener(new ClickCounter.OnCountListener() {
            @Override
            public void onCount(int currentTime) {
                mToast = Toast.makeText(mContext, R.string.exit_app_toast, Toast.LENGTH_SHORT);
                mToast.show();
            }

            @Override
            public void onFinish() {
                mToast.cancel();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mCounter != null)
            mCounter.countClick();
    }

    @Override
    public void onTabChanged(String s) {

    }

    @Override
    public boolean intercept(String tabId) {
        return false;
    }
}
