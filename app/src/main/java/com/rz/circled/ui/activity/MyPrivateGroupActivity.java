package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.circled.adapter.MyFragmentPagerAdapter;
import com.rz.circled.adapter.PrivateGroupNavigatorAdapter;
import com.rz.circled.ui.fragment.PrivateGroupCreateByMyselfFragment;
import com.rz.circled.ui.fragment.PrivateGroupJoinByMyselfFragment;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.ui.fragment.BaseFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.rz.circled.event.EventConstant.USER_CREATE_PRIVATE_GROUP_NUM;
import static com.rz.circled.event.EventConstant.USER_JOIN_PRIVATE_GROUP_NUM;

/**
 * Created by rzw2 on 2017/9/2.
 */

public class MyPrivateGroupActivity extends BaseActivity {
    @BindView(R.id.indicator_tab)
    MagicIndicator indicatorTab;
    @BindView(R.id.viewpager_group)
    ViewPager viewpagerGroup;

    private List<String> mDataList;
    private PrivateGroupNavigatorAdapter groupNavigatorAdapter;

    public static void startMyPrivateGroup(Context context, int index) {
        startMyPrivateGroup(context, index, -1);
    }

    public static void startMyPrivateGroup(Context context, int index, int flag) {
        Intent intent = new Intent(context, MyPrivateGroupActivity.class);
        intent.putExtra(IntentKey.EXTRA_POSITION, index);
        if (flag != -1) intent.setFlags(flag);
        context.startActivity(intent);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_my_private_group, null, false);
    }

    @Override
    public void initView() {
        setTitleText(R.string.private_group);
        initTabTitle();
        initViewpagerGroup();
        initIndicatorGroup();
        viewpagerGroup.setCurrentItem(getIntent().getIntExtra(IntentKey.EXTRA_POSITION, 0));
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void eventBus(BaseEvent event) {
        switch (event.getType()) {
            case USER_CREATE_PRIVATE_GROUP_NUM:
                mDataList.set(0, String.format(getString(R.string.user_create_private_group_num), (int) event.getData()));
                groupNavigatorAdapter.notifyDataSetChanged();
                break;
            case USER_JOIN_PRIVATE_GROUP_NUM:
                mDataList.set(1, String.format(getString(R.string.user_join_private_group_num), (int) event.getData()));
                groupNavigatorAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void initTabTitle() {
        mDataList = new ArrayList<>();
        mDataList.add(String.format(getString(R.string.user_create_private_group_num), 0));
        mDataList.add(String.format(getString(R.string.user_join_private_group_num), 0));
    }

    private void initViewpagerGroup() {
        List<Fragment> mFragments = new ArrayList<>();
        BaseFragment privateGroupJoinByMyselfFragment = PrivateGroupJoinByMyselfFragment.newInstance(PrivateGroupJoinByMyselfFragment.TYPE_ALL);
        BaseFragment privateGroupCreateByMyselfFragment = PrivateGroupCreateByMyselfFragment.newInstance(PrivateGroupCreateByMyselfFragment.TYPE_ALL);
        mFragments.add(privateGroupCreateByMyselfFragment);
        mFragments.add(privateGroupJoinByMyselfFragment);
        viewpagerGroup.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments));
    }

    private void initIndicatorGroup() {
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(groupNavigatorAdapter = new PrivateGroupNavigatorAdapter(mContext, viewpagerGroup, mDataList));
        commonNavigator.setAdjustMode(true);
        indicatorTab.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicatorTab, viewpagerGroup);
    }

    @Override
    public void refreshPage() {

    }
}
