package com.rz.circled.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.circled.adapter.MyFragmentPagerAdapter;
import com.rz.circled.adapter.NewsInteractiveNavigatorAdapter;
import com.rz.circled.ui.fragment.NewsCommonFragment;
import com.rz.common.cache.preference.Session;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.NewsInteractiveTabBean;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

import static com.rz.circled.event.EventConstant.NEWS_UNREAD_CHANGE;

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
        setTitleText(R.string.interactive_information);
        initTabTitle();
        initViewpagerGroup();
        initIndicatorGroup();
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
            case NEWS_UNREAD_CHANGE:
                for (NewsInteractiveTabBean item :
                        mDataList) {
                    switch (item.getId()) {
                        case NewsCommonFragment.NEWS_COMMENT:
                            item.setUnReadNum(Session.getNewsCommentNum());
                            break;
                        case NewsCommonFragment.NEWS_QA:
                            item.setUnReadNum(Session.getNewsQaNum());
                            break;
                        case NewsCommonFragment.NEWS_PRIVATE_GROUP:
                            item.setUnReadNum(Session.getNewsGroupNum());
                            break;
                        case NewsCommonFragment.NEWS_ACTIVITY:
                            item.setUnReadNum(Session.getNewsActivityNum());
                            break;
                    }
                }
                newsInteractiveNavigatorAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void initTabTitle() {
        mDataList = new ArrayList<>();
        for (int type : mTypes) {
            switch (type) {
                case NewsCommonFragment.NEWS_COMMENT:
                    mDataList.add(new NewsInteractiveTabBean(type, getString(R.string.news_interactive_tab_comment), Session.getNewsCommentNum()));
                    break;
                case NewsCommonFragment.NEWS_QA:
                    mDataList.add(new NewsInteractiveTabBean(type, getString(R.string.news_interactive_tab_qa), Session.getNewsQaNum()));
                    break;
                case NewsCommonFragment.NEWS_PRIVATE_GROUP:
                    mDataList.add(new NewsInteractiveTabBean(type, getString(R.string.news_interactive_tab_group), Session.getNewsGroupNum()));
                    break;
                case NewsCommonFragment.NEWS_ACTIVITY:
                    mDataList.add(new NewsInteractiveTabBean(type, getString(R.string.news_interactive_tab_activity), Session.getNewsActivityNum()));
                    break;
            }
        }
    }

    private void initViewpagerGroup() {
        List<Fragment> mFragments = new ArrayList<>();
        for (int type : mTypes) {
            BaseFragment newsCommonFragment = NewsCommonFragment.newInstance(type);
            mFragments.add(newsCommonFragment);
        }
        viewpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments));
        viewpager.setOffscreenPageLimit(4);
    }

    private void initIndicatorGroup() {
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(newsInteractiveNavigatorAdapter = new NewsInteractiveNavigatorAdapter(mContext, viewpager, mDataList));
        commonNavigator.setAdjustMode(true);
        indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicator, viewpager);
    }


    @Override
    public void refreshPage() {

    }
}
