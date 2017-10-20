package com.rz.circled.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.jakewharton.rxbinding.view.RxView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.rz.circled.R;
import com.rz.circled.adapter.DynamicAdapter;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.circled.ui.activity.RecentContactActivity;
import com.rz.circled.ui.activity.SearchActivity;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.AutoRollLayout;
import com.rz.circled.widget.CommomUtils;
import com.rz.circled.widget.SwipyRefreshLayoutBanner;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.Constants;
import com.rz.common.swiperefresh.SwipyRefreshLayout;
import com.rz.common.swiperefresh.SwipyRefreshLayoutDirection;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.bean.BannerAddSubjectModel;
import com.rz.httpapi.bean.CircleDynamic;
import com.rz.httpapi.bean.UserPermissionBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

import static com.rz.circled.BuildConfig.WebHomeBaseUrl;
import static com.rz.common.utils.SystemUtils.trackUser;

/**
 * Created by Gsm on 2017/8/29.
 */
public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    AutoRollLayout mAuto_viewpager;
    @BindView(R.id.id_homefrg_listview)
    ListView mHomeLv;
    @BindView(R.id.layout_content)
    FrameLayout mLayoutContent;
    @BindView(R.id.home_publish)
    ImageView mHomePublish;
    @BindView(R.id.refresh)
    SwipyRefreshLayoutBanner mRefresh;
    private ImageView mUnread;
    private List<BannerAddSubjectModel> bannerList = new ArrayList<>();
    private List<CircleDynamic> circleDynamicList = new ArrayList<>();
    DynamicAdapter dynamicAdapter;
    private CirclePresenter mPresenter;
    UserPermissionBean mUserPermissionBean;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void initPresenter() {
        mPresenter = new CirclePresenter();
        mPresenter.attachView(this);
        mPresenter.getBannerList("2");
        mPresenter.getCircleDynamicList(false);

    }

    @Override
    public void initView() {
        initTitleBar();
        initDynamicLv();
        registerMsgUnreadInfoObserver(true);
    }

    private void initTitleBar() {
        View v = View.inflate(getActivity(), R.layout.titlebar_transparent, null);
        mLayoutContent.addView(v);
        mUnread = (ImageView) v.findViewById(R.id.unread_msg_number);
        RxView.clicks(v.findViewById(R.id.et_search_keyword)).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //跳搜索界面
                trackUser("入口", "首页", "搜索");
                startActivity(new Intent(mActivity, SearchActivity.class));
            }
        });
        RxView.clicks(v.findViewById(R.id.iv_mess)).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                trackUser("入口", "首页", "消息");
                //跳最近联系人界面
                if (NIMClient.getStatus() == StatusCode.LOGINED)
                    startActivity(new Intent(mActivity, RecentContactActivity.class));
                else
                    Toasty.info(mActivity, getString(R.string.im_link_error_hint)).show();
            }
        });
        RxView.clicks(mHomePublish).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //跳发布
                trackUser("入口", "首页", "发布按钮");
                mPresenter.getUserPermession();
            }
        });

    }

    private void initDynamicLv() {
        View v = View.inflate(mActivity, R.layout.banner, null);
        mAuto_viewpager = (AutoRollLayout) v.findViewById(R.id.auto_viewpager);
        mHomeLv.addHeaderView(v);
        dynamicAdapter = new DynamicAdapter(mActivity, circleDynamicList);//泛型要改
        mHomeLv.setAdapter(dynamicAdapter);
        mHomeLv.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        EntityCache<CircleDynamic> entityCache = new EntityCache<>(mActivity, CircleDynamic.class);
        List<CircleDynamic> model = entityCache.getListEntity(CircleDynamic.class);
        List<BannerAddSubjectModel> bannerList = (List<BannerAddSubjectModel>) mACache.getAsObject(Constants.BANNER_CACHE);
        updateViewWithFlag(bannerList, 2);
        updateViewWithLoadMore(model, false);
        mRefresh.setColorSchemeColors(Constants.COLOR_SCHEMES);
        mRefresh.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                mPresenter.getBannerList("2");
                mPresenter.getCircleDynamicList(direction != SwipyRefreshLayoutDirection.TOP);
                mRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == 2 && t != null) {
            bannerList.clear();
            bannerList.addAll((List<BannerAddSubjectModel>) t);
            mACache.put(Constants.BANNER_CACHE, (Serializable) bannerList);
            mAuto_viewpager.setItems(bannerList);
            if (bannerList.size() == 1) {
                mAuto_viewpager.setAutoRoll(false);
                mAuto_viewpager.setScrollble(false);
            } else {
                mAuto_viewpager.setAutoRoll(true);
            }
            mAuto_viewpager.notifyData();
        }
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (t != null) {
            mUserPermissionBean = (UserPermissionBean) t;
            if (!mUserPermissionBean.disTalk)
                WebContainerActivity.startActivity(mActivity, WebHomeBaseUrl + "/activity/new-circles");
            else Toasty.info(mActivity, getString(R.string.NO_TALK_STATE)).show();
            Log.i(TAG, "updateView: " + mUserPermissionBean.disTalk);
        }
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        if (t != null) {
            if (!loadMore) {
                circleDynamicList.clear();
            }
            circleDynamicList.addAll((Collection<? extends CircleDynamic>) t);
        }
        dynamicAdapter.notifyDataSetChanged();


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CircleDynamic circleDynamic = circleDynamicList.get(position - 1);
        circleDynamic.click += 1;
        if (circleDynamic.click >= 3) {
            mPresenter.addLoveCircle(circleDynamic.circleId, 2);
            circleDynamic.click = 0;
        }
        if (StringUtils.isEmpty(circleDynamic.coterieId) || StringUtils.isEmpty(circleDynamic.coterieName)) {
            String circleUrl = CommomUtils.getCircleUrl(circleDynamic.circleRoute, circleDynamic.moduleEnum, circleDynamic.resourceId);
            WebContainerActivity.startActivity(mActivity, circleUrl);
        } else {
            String url = CommomUtils.getDymanicUrl(circleDynamic.circleRoute, circleDynamic.moduleEnum, circleDynamic.coterieId, circleDynamic.resourceId);
            WebContainerActivity.startActivity(mActivity, url);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registerMsgUnreadInfoObserver(false);
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        requestMsgUnRead();
    }

    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerMsgUnreadInfoObserver(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeRecentContact(messageObserver, register);
        service.observeMsgStatus(statusObserver, register);
        service.observeRecentContactDeleted(deleteObserver, register);
    }

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            requestMsgUnRead();
        }
    };

    Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            requestMsgUnRead();
        }
    };

    Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
        @Override
        public void onEvent(RecentContact recentContact) {
            requestMsgUnRead();
        }
    };

    @Override
    public void refreshPage() {
        mPresenter.getCircleDynamicList(false);
    }

    private void requestMsgUnRead() {
        if (null == mUnread)
            return;

        if (Session.getUserIsLogin()) {
            int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
            if (unreadNum == 0) {
                mUnread.setVisibility(View.GONE);
            } else {
                mUnread.setVisibility(View.VISIBLE);
            }

        } else {
            mUnread.setVisibility(View.GONE);
        }
    }
}
