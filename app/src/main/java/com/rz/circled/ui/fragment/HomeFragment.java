package com.rz.circled.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rz.circled.R;
import com.rz.circled.adapter.DynamicAdapter;
import com.rz.circled.presenter.impl.V3CirclePresenter;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.AutoRollLayout;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.BannerAddSubjectModel;
import com.rz.httpapi.bean.CircleDynamic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Gsm on 2017/8/29.
 */
public class HomeFragment extends BaseFragment {
    AutoRollLayout mAuto_viewpager;
    @BindView(R.id.id_homefrg_listview)
    ListView mHomeLv;
    private List<BannerAddSubjectModel> bannerList = new ArrayList<>();
    private List<CircleDynamic> circleDynamicList = new ArrayList<>();
    DynamicAdapter dynamicAdapter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void initPresenter() {
        V3CirclePresenter presenter = new V3CirclePresenter();
        presenter.attachView(this);
        presenter.getBannerList(5);
        presenter.getCircleDynamicList(false);
    }

    @Override
    public void initView() {
        initDynamicLv();
    }

    private void initDynamicLv() {
        View v = View.inflate(mActivity, R.layout.banner, null);
        mAuto_viewpager = (AutoRollLayout) v.findViewById(R.id.auto_viewpager);
        mHomeLv.addHeaderView(v);
        dynamicAdapter = new DynamicAdapter(mActivity, circleDynamicList);//泛型要改
        mHomeLv.setAdapter(dynamicAdapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == 5) {
            bannerList.clear();
            bannerList.addAll((List<BannerAddSubjectModel>) t);
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

    @OnClick(R.id.tv_home_web)
    public void onClick() {
        startActivity(new Intent(getActivity(), WebContainerActivity.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}