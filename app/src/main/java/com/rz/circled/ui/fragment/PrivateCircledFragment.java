package com.rz.circled.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
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
import com.rz.circled.adapter.MyPagerAdapter;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.GroupBannerBean;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import static com.rz.circled.event.EventConstant.USER_CREATE_PRIVATE_GROUP_NUM;
import static com.rz.circled.event.EventConstant.USER_JOIN_PRIVATE_GROUP_NUM;

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
    @BindView(R.id.tv_create)
    TextView tvCreate;
    @BindView(R.id.btn_create_more)
    TextView btnCreateMore;
    @BindView(R.id.frame_created)
    FrameLayout frameCreated;
    @BindView(R.id.layout_my_create)
    LinearLayout layoutMyCreate;
    @BindView(R.id.tv_join)
    TextView tvJoin;
    @BindView(R.id.btn_join_more)
    TextView btnJoinMore;
    @BindView(R.id.frame_join)
    FrameLayout frameJoin;
    @BindView(R.id.layout_my_join)
    LinearLayout layoutMyJoin;
    @BindView(R.id.layout_my_group)
    LinearLayout layoutMyGroup;
    @BindView(R.id.layout_no_data)
    LinearLayout layoutNoData;
    @BindView(R.id.btn_refresh)
    TextView btnRefresh;
    @BindView(R.id.frame_recommend)
    FrameLayout frameRecommend;
    @BindView(R.id.frame_essence)
    FrameLayout frameEssence;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_private_circle, null);
    }

    @Override
    public void initView() {
        initRecommendGroup();
        initEssenceGroup();
        initCreatedGroup();
        initJoinGroup();
    }

    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void eventBus(BaseEvent event) {
        switch (event.getType()) {
            case USER_CREATE_PRIVATE_GROUP_NUM:
                if ((int) event.getData() != 0) {
                    tvCreate.setText(String.format(getString(R.string.user_create_private_group_num), (int) event.getData()));
                    layoutMyCreate.setVisibility(View.VISIBLE);
                } else {
                    layoutMyCreate.setVisibility(View.GONE);
                }
                checkGroupNull();
                break;
            case USER_JOIN_PRIVATE_GROUP_NUM:
                if ((int) event.getData() != 0) {
                    tvJoin.setText(String.format(getString(R.string.user_join_private_group_num), (int) event.getData()));
                    layoutMyJoin.setVisibility(View.VISIBLE);
                } else {
                    layoutMyJoin.setVisibility(View.GONE);
                }
                checkGroupNull();
                break;
        }
    }

    private void checkGroupNull() {
        if (layoutMyCreate.getVisibility() == View.GONE && layoutMyJoin.getVisibility() == View.GONE) {
            layoutNoData.setVisibility(View.VISIBLE);
        } else {
            layoutNoData.setVisibility(View.GONE);
        }
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

    private void initCreatedGroup() {
        PrivateGroupCreateByMyselfFragment privateGroupCreateByMyselfFragment = PrivateGroupCreateByMyselfFragment.newInstance(PrivateGroupCreateByMyselfFragment.TYPE_PART);
        initFragment(R.id.frame_created, privateGroupCreateByMyselfFragment);
    }

    private void initJoinGroup() {
        PrivateGroupJoinByMyselfFragment privateGroupJoinByMyselfFragment = PrivateGroupJoinByMyselfFragment.newInstance(PrivateGroupJoinByMyselfFragment.TYPE_PART);
        initFragment(R.id.frame_join, privateGroupJoinByMyselfFragment);
    }

    private void initRecommendGroup() {
        PrivateGroupRecommendFragment privateGroupRecommendFragment = PrivateGroupRecommendFragment.newInstance();
        initFragment(R.id.frame_recommend, privateGroupRecommendFragment);
    }

    private void initEssenceGroup() {
        PrivateGroupEssenceFragment privateGroupEssenceFragment = PrivateGroupEssenceFragment.newInstance();
        initFragment(R.id.frame_essence, privateGroupEssenceFragment);
    }

    private void initFragment(int id, BaseFragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @OnClick({R.id.btn_apply_for, R.id.btn_create_more, R.id.btn_join_more, R.id.btn_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_apply_for:
                break;
            case R.id.btn_create_more:
                break;
            case R.id.btn_join_more:
                break;
            case R.id.btn_refresh:
                break;
        }
    }

}
