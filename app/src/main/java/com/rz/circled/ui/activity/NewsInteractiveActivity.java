package com.rz.circled.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.circled.adapter.MyFragmentPagerAdapter;
import com.rz.circled.adapter.NewsInteractiveNavigatorAdapter;
import com.rz.circled.ui.fragment.NewsCommonFragment;
import com.rz.circled.ui.fragment.PrivateGroupCreateByMyselfFragment;
import com.rz.circled.ui.fragment.PrivateGroupJoinByMyselfFragment;
import com.rz.common.cache.preference.Session;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.NewsInteractiveTabBean;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

import static com.rz.circled.event.EventConstant.NEWS_ACTIVITY_UNREAD_CHANGE;
import static com.rz.circled.event.EventConstant.NEWS_COMMENT_UNREAD_CHANGE;
import static com.rz.circled.event.EventConstant.NEWS_GROUP_UNREAD_CHANGE;
import static com.rz.circled.event.EventConstant.NEWS_QA_UNREAD_CHANGE;

/**
 * Created by rzw2 on 2017/9/7.
 */

public class NewsInteractiveActivity extends BaseActivity {

    @BindView(R.id.indicator)
    MagicIndicator indicator;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private static Integer[] NEWS_TYPES = {
            NewsCommonFragment.NEWS_COMMENT,
            NewsCommonFragment.NEWS_QA,
            NewsCommonFragment.NEWS_PRIVATE_GROUP,
            NewsCommonFragment.NEWS_ACTIVITY};
    private List<Integer> mTypes = Arrays.asList(NEWS_TYPES);

    private NewsInteractiveNavigatorAdapter newsInteractiveNavigatorAdapter;
    private List<NewsInteractiveTabBean> mDataList;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_news_interactive, null, false);
    }

    @Override
    public void initView() {
        initTabTitle();
        initViewpagerGroup();
        initIndicatorGroup();
    }

    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void eventBus(BaseEvent event) {
        switch (event.getType()) {
            case NEWS_COMMENT_UNREAD_CHANGE:
                mDataList.get(0).setUnReadNum(Session.getNewsCommentNum());
                newsInteractiveNavigatorAdapter.notifyDataSetChanged();
                break;
            case NEWS_QA_UNREAD_CHANGE:
                mDataList.get(1).setUnReadNum(Session.getNewsQaNum());
                newsInteractiveNavigatorAdapter.notifyDataSetChanged();
                break;
            case NEWS_GROUP_UNREAD_CHANGE:
                mDataList.get(2).setUnReadNum(Session.getNewsGroupNum());
                newsInteractiveNavigatorAdapter.notifyDataSetChanged();
                break;
            case NEWS_ACTIVITY_UNREAD_CHANGE:
                mDataList.get(3).setUnReadNum(Session.getNewsActivityNum());
                newsInteractiveNavigatorAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void initTabTitle() {
        mDataList = new ArrayList<>();
        mDataList.add(new NewsInteractiveTabBean(getString(R.string.news_interactive_tab_comment), Session.getNewsCommentNum()));
        mDataList.add(new NewsInteractiveTabBean(getString(R.string.news_interactive_tab_qa), Session.getNewsQaNum()));
        mDataList.add(new NewsInteractiveTabBean(getString(R.string.news_interactive_tab_group), Session.getNewsGroupNum()));
        mDataList.add(new NewsInteractiveTabBean(getString(R.string.news_interactive_tab_activity), Session.getNewsActivityNum()));
    }

    private void initViewpagerGroup() {
        List<Fragment> mFragments = new ArrayList<>();
        for (int type : mTypes) {
            BaseFragment newsCommonFragment = NewsCommonFragment.newInstance(type);
            mFragments.add(newsCommonFragment);
        }
        viewpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments));
    }

    private void initIndicatorGroup() {
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(newsInteractiveNavigatorAdapter = new NewsInteractiveNavigatorAdapter(mContext, viewpager, mDataList));
        commonNavigator.setAdjustMode(true);
        indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicator, viewpager);
    }


}
