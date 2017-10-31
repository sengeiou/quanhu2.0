package com.rz.circled.ui.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.adapter.MyCircleBannerPagerAdapter;
import com.rz.circled.dialog.GroupLevelLessDialog;
import com.rz.circled.presenter.impl.LevelPresenter;
import com.rz.circled.presenter.impl.PrivateGroupPresenter;
import com.rz.circled.ui.activity.ApplyForCreatePrivateGroupActivity;
import com.rz.circled.ui.activity.CommonH5Activity;
import com.rz.circled.ui.activity.MyPrivateGroupActivity;
import com.rz.circled.widget.SwipyRefreshLayoutBanner;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.H5Address;
import com.rz.common.event.BaseEvent;
import com.rz.common.swiperefresh.SwipyRefreshLayout;
import com.rz.common.swiperefresh.SwipyRefreshLayoutDirection;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.api.ApiPGService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.GroupBannerBean;
import com.rz.httpapi.bean.MyLevelAcountBean;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_ESSENCE_MORE;
import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_TAB_REFRESH;
import static com.rz.circled.event.EventConstant.USER_CREATE_PRIVATE_GROUP_NUM;
import static com.rz.circled.event.EventConstant.USER_JOIN_PRIVATE_GROUP_NUM;
import static com.rz.common.utils.SystemUtils.trackUser;

/**
 * Created by Gsm on 2017/8/29.
 */
public class PrivateCircledFragment extends BaseFragment {
    @BindView(R.id.scroll)
    ScrollView scrollView;
    @BindView(R.id.layout_refresh)
    SwipyRefreshLayoutBanner refreshLayout;
    @BindView(R.id.viewpager)
    AutoScrollViewPager viewpager;
    @BindView(R.id.indicator_banner)
    MagicIndicator indicatorBanner;
    @BindView(R.id.btn_apply_for)
    TextView btnApplyFor;
    @BindView(R.id.line)
    View line;
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
    @BindView(R.id.btn_about)
    TextView btnAbout;
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

    //私圈相关
    private PrivateGroupPresenter mPresenter;
    private LevelPresenter presenter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_private_circle, null);
    }

    @Override
    public void initView() {
        initRefresh();
        initRecommendGroup();
        initEssenceGroup();
        initCreatedGroup();
        initJoinGroup();
    }

    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        mPresenter.privateGroupBanner("3");
    }

    @Override
    public void initPresenter() {
        mPresenter = new PrivateGroupPresenter();
        mPresenter.attachView(this);
        presenter = new LevelPresenter();
        presenter.attachView(this);
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (t instanceof List) {
            List _data = (List) t;
            if (_data.get(0) instanceof GroupBannerBean) {
                List<GroupBannerBean> data = (List<GroupBannerBean>) t;
                initViewpagerBanner(data);
                initIndicatorBanner(data);
            }
        }
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == presenter.FLAG_LEVEL_ACOUNT) {
            if (getFragmentManager().isDestroyed())
                return;
            MyLevelAcountBean acountBean = (MyLevelAcountBean) t;
            if (acountBean == null) return;
            String level = TextUtils.isEmpty(acountBean.getGrowLevel()) ? "0" : acountBean.getGrowLevel();
            if (Integer.parseInt(level) < 5)
                GroupLevelLessDialog.newInstance(level).show(getFragmentManager(), "");
            else
                startActivity(new Intent(getContext(), ApplyForCreatePrivateGroupActivity.class));
        }
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
            case PRIVATE_GROUP_TAB_REFRESH:
                initData();
                break;
        }
    }

    private void checkGroupNull() {
        if (layoutMyCreate.getVisibility() == View.GONE && layoutMyJoin.getVisibility() == View.GONE) {
            layoutNoData.setVisibility(View.VISIBLE);
        } else {
            layoutNoData.setVisibility(View.GONE);
            if (layoutMyCreate.getVisibility() == View.VISIBLE && layoutMyJoin.getVisibility() == View.VISIBLE)
                line.setVisibility(View.VISIBLE);
        }
    }

    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    EventBus.getDefault().post(new BaseEvent(PRIVATE_GROUP_TAB_REFRESH));
                } else {
                    EventBus.getDefault().post(new BaseEvent(PRIVATE_GROUP_ESSENCE_MORE));
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void initViewpagerBanner(List<GroupBannerBean> pics) {
        if (viewpager == null) return;
        List<View> imageViews = new ArrayList<>(); // 滑动的图片集合
        for (int i = 0; i < pics.size(); i++) {
            final GroupBannerBean pic;
            pic = pics.get(i);
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(getContext()).load(pic.getPicUrl()).placeholder(R.mipmap.ic_default_banner).error(R.mipmap.ic_default_banner).into(imageView);
            imageViews.add(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonH5Activity.startCommonH5(mActivity, "", pic.getUrl());
                }
            });
        }
        viewpager.setAdapter(new MyCircleBannerPagerAdapter(imageViews));
        viewpager.startAutoScroll();
        viewpager.setBorderAnimation(true);
        viewpager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
    }

    private void initIndicatorBanner(final List pics) {
        CircleNavigator circleNavigator = new CircleNavigator(getContext());
        circleNavigator.setCircleCount(pics.size());
        circleNavigator.setCircleColor(getResources().getColor(R.color.color_main));
        indicatorBanner.setNavigator(circleNavigator);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (indicatorBanner != null)
                    indicatorBanner.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                if (indicatorBanner != null)
                    indicatorBanner.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (indicatorBanner != null)
                    indicatorBanner.onPageScrollStateChanged(state);
            }
        });
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

    @OnClick({R.id.btn_apply_for, R.id.btn_create_more, R.id.btn_join_more, R.id.btn_refresh, R.id.btn_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_apply_for:
                trackUser("入口", "创建私圈", "");
                String level = TextUtils.isEmpty(Session.getUserLevel()) ? "0" : Session.getUserLevel();
                if (Integer.parseInt(level) < 5) {
                    presenter.getLevelAcount();
                } else
                    startActivity(new Intent(getContext(), ApplyForCreatePrivateGroupActivity.class));

                break;
            case R.id.btn_create_more:
                MyPrivateGroupActivity.startMyPrivateGroup(getContext(), 0);
                break;
            case R.id.btn_join_more:
                MyPrivateGroupActivity.startMyPrivateGroup(getContext(), 1);
                break;
            case R.id.btn_refresh:
                break;
            case R.id.btn_about:
                CommonH5Activity.startCommonH5(getContext(), "", H5Address.PRIVATE_GROUP_ABOUT_AGREEMENT);
                break;
        }
    }

    @Override
    public void refreshPage() {

    }
}
