package com.rz.circled.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.adapter.MyFragmentPagerAdapter;
import com.rz.circled.adapter.MyPagerAdapter;
import com.rz.circled.adapter.PrivateGroupNavigatorAdapter;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.GroupBannerBean;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import static net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator.MODE_MATCH_EDGE;
import static net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator.MODE_WRAP_CONTENT;

/**
 * Created by Gsm on 2017/8/29.
 */
public class PrivateCircledFragment extends BaseFragment {

    @BindView(R.id.viewpager)
    AutoScrollViewPager viewpager;
    @BindView(R.id.indicator_banner)
    MagicIndicator indicatorBanner;
    @BindView(R.id.btn_apply_for)
    TextView btnApplyFor;
    @BindView(R.id.indicator_tab)
    MagicIndicator indicatorTab;
    @BindView(R.id.viewpager_group)
    ViewPager viewpagerGroup;
    @BindView(R.id.btn_refresh)
    TextView btnRefresh;
    @BindView(R.id.frame_recommend)
    FrameLayout frameRecommend;
    @BindView(R.id.frame_essence)
    FrameLayout frameEssence;

    private PrivateGroupNavigatorAdapter groupNavigatorAdapter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_private_circle, null);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    private void initViewpagerBanner(List<GroupBannerBean> pics) {
        List<View> imageViews = new ArrayList<>(); // 滑动的图片集合
        for (GroupBannerBean pic : pics) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(getContext()).load(pic.getPic()).into(imageView);
            imageViews.add(imageView);
        }
        viewpager.setAdapter(new MyPagerAdapter(imageViews));
        viewpager.stopAutoScroll();
        viewpager.setBorderAnimation(false);
        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
    }

    private void initIndicatorBanner(List pics) {
        CircleNavigator circleNavigator = new CircleNavigator(getContext());
        circleNavigator.setCircleCount(pics.size());
        circleNavigator.setCircleColor(Color.WHITE);
        circleNavigator.setCircleClickListener(new CircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                viewpager.setCurrentItem(index);
            }
        });
        indicatorBanner.setNavigator(circleNavigator);
        ViewPagerHelper.bind(indicatorBanner, viewpager);
    }

    private void initViewpagerGroup() {
        List<Fragment> mFragments = new ArrayList<>();
        BaseFragment privateGroupJoinByMyselfFragment = PrivateGroupJoinByMyselfFragment.newInstance(PrivateGroupJoinByMyselfFragment.TYPE_PART);
        BaseFragment privateGroupCreateByMyselfFragment = PrivateGroupCreateByMyselfFragment.newInstance(PrivateGroupCreateByMyselfFragment.TYPE_PART);
        mFragments.add(privateGroupJoinByMyselfFragment);
        mFragments.add(privateGroupCreateByMyselfFragment);
        viewpager.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(), mFragments));
    }

    private void initIndicatorGroup() {
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdapter(groupNavigatorAdapter = new PrivateGroupNavigatorAdapter(getContext(), viewpagerGroup));
        commonNavigator.setAdjustMode(true);
        indicatorTab.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicatorTab, viewpagerGroup);
    }

    @OnClick({R.id.btn_apply_for, R.id.btn_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_apply_for:
                break;
            case R.id.btn_refresh:
                break;
        }
    }
}
