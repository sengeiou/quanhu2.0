package com.rz.circled.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.jakewharton.rxbinding.view.RxView;
import com.rz.circled.R;
import com.rz.circled.adapter.DynamicAdapter;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.circled.ui.activity.NewsActivity;
import com.rz.circled.ui.activity.SearchActivity;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.AutoRollLayout;
import com.rz.circled.widget.CommomUtils;
import com.rz.common.constant.Constants;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.ACache;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.BannerAddSubjectModel;
import com.rz.httpapi.bean.CircleDynamic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

import static com.rz.circled.BuildConfig.WebHomeBaseUrl;

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
    SwipeRefreshLayout mRefresh;
    private List<BannerAddSubjectModel> bannerList = new ArrayList<>();
    private List<CircleDynamic> circleDynamicList = new ArrayList<>();
    DynamicAdapter dynamicAdapter;
    private CirclePresenter mPresenter;

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
        mPresenter.getCircleDynamicList("", false);
    }

    @Override
    public void initView() {
        initTitleBar();
        initDynamicLv();
    }

    private void initTitleBar() {
        View v = View.inflate(getActivity(), R.layout.titlebar_transparent, null);
        mLayoutContent.addView(v);
        RxView.clicks(v.findViewById(R.id.et_search_keyword)).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //跳搜索界面
                startActivity(new Intent(mActivity, SearchActivity.class));
            }
        });
        RxView.clicks(v.findViewById(R.id.iv_mess)).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //跳消息界面
                startActivity(new Intent(mActivity, NewsActivity.class));
            }
        });
        RxView.clicks(mHomePublish).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //跳消息界面
                WebContainerActivity.startActivity(mActivity, WebHomeBaseUrl + "/activity/new-circles");
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
        ACache mCache = ACache.get(mActivity);
        List<CircleDynamic> model = (List<CircleDynamic>) mCache.getAsObject(Constants.HOME_FRAGMENT_CACHE);
        List<BannerAddSubjectModel> bannerList= (List<BannerAddSubjectModel>) mACache.getAsObject(Constants.BANNER_CACHE);
        updateViewWithFlag(bannerList,2);
        updateViewWithLoadMore(model,false);
        mRefresh.setColorSchemeColors(Constants.COLOR_SCHEMES);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getBannerList("2");
                mPresenter.getCircleDynamicList("", false);
                mRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == 2&&t!=null) {
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
        CircleDynamic circleDynamic = circleDynamicList.get(position-1);
//        Http.getApiService(ApiService.class).addCollect(Session.getUserId(),"535243033497698304")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<ResponseData>() {
//                    @Override
//                    public void call(ResponseData responseData) {
//
//                    }
//                });
//        Http.getApiService(ApiService.class).addCollect(Session.getUserId(),"535319157498134528")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<ResponseData>() {
//                    @Override
//                    public void call(ResponseData responseData) {
//
//                    }
//                });
//        Http.getApiService(ApiService.class).addCollect(Session.getUserId(),"535319157498134528")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<ResponseData>() {
//                    @Override
//                    public void call(ResponseData responseData) {
//
//                    }
//                });
        circleDynamic.click += 1;
        if (circleDynamic.click >= 3) {
            mPresenter.addLoveCircle(circleDynamic.circleId, 2);
            circleDynamic.click=0;
        }
        if (StringUtils.isEmpty(circleDynamic.coterieId)||StringUtils.isEmpty(circleDynamic.coterieName)) {
            String circleUrl = CommomUtils.getCircleUrl(circleDynamic.circleRoute,circleDynamic.moduleEnum, circleDynamic.resourceId);
            WebContainerActivity.startActivity(mActivity, circleUrl);
        } else {
            String url = CommomUtils.getDymanicUrl(circleDynamic.circleRoute,circleDynamic.moduleEnum, circleDynamic.coterieId, circleDynamic.resourceId);
            WebContainerActivity.startActivity(mActivity, url);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
