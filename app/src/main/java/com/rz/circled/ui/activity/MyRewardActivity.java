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
import com.rz.circled.ui.fragment.MyRewardFragment;
import com.rz.common.cache.preference.Session;
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
 * Created by Administrator on 2017/9/16 0016.
 */

public class MyRewardActivity extends BaseActivity {

    @BindView(R.id.indicator_tab)
    MagicIndicator indicatorTab;
    @BindView(R.id.viewpager_group)
    ViewPager viewpagerGroup;

    private List<String> mDataList;
    private PrivateGroupNavigatorAdapter groupNavigatorAdapter;

    public static void startMyPrivateGroup(Context context, int index) {
        Intent i = new Intent(context, MyPrivateGroupActivity.class);
        i.putExtra(IntentKey.EXTRA_POSITION, index);
        context.startActivity(i);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_my_reward, null, false);
    }

    @Override
    public void initView() {
        setTitleText("悬赏");
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
//            case USER_CREATE_PRIVATE_GROUP_NUM:
//                mDataList.set(0, String.format(getString(R.string.my_create_reward)));
//                groupNavigatorAdapter.notifyDataSetChanged();
//                break;
//            case USER_JOIN_PRIVATE_GROUP_NUM:
//                mDataList.set(1, String.format(getString(R.string.my_answer_reward)));
//                groupNavigatorAdapter.notifyDataSetChanged();
//                break;
        }
    }

    private void initTabTitle() {
        mDataList = new ArrayList<>();
        mDataList.add(String.format(getString(R.string.my_create_reward)));
        mDataList.add(String.format(getString(R.string.my_answer_reward)));
    }

    private void initViewpagerGroup() {
        List<Fragment> mFragments = new ArrayList<>();
        BaseFragment privateGroupJoinByMyselfFragment = MyRewardFragment.newInstance("1");
        BaseFragment privateGroupCreateByMyselfFragment = MyRewardFragment.newInstance("0");
        mFragments.add(privateGroupCreateByMyselfFragment);
        mFragments.add(privateGroupJoinByMyselfFragment);
        viewpagerGroup.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments));
    }

    private void initIndicatorGroup() {
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(groupNavigatorAdapter = new PrivateGroupNavigatorAdapter(mContext, viewpagerGroup, mDataList));
        commonNavigator.setAdjustMode(true);
        commonNavigator.setIndicatorOnTop(true);
        indicatorTab.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicatorTab, viewpagerGroup);
    }

    @Override
    public void refreshPage() {

    }
}
