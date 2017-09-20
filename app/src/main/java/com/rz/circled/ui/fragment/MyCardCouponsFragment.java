package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.adapter.MyAwardAdapter;
import com.rz.circled.adapter.MyCouponsAdapter;
import com.rz.circled.presenter.impl.CouponsPresenter;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.ui.view.BaseLoadView;
import com.rz.common.utils.NetUtils;
import com.rz.common.widget.MyListView;
import com.rz.httpapi.bean.CouponsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Gsm on 2017/9/18.
 */
public class MyCardCouponsFragment extends BaseFragment {

    private final int TYPE_COUPONS = 0;
    private final int TYPE_AWARD = 1;

    @BindView(R.id.lv_card_coupons_normal)
    MyListView lvNormal;
    @BindView(R.id.tv_card_coupons_hint)
    TextView tvHint;
    @BindView(R.id.ll_card_coupons_hint)
    LinearLayout llHint;
    @BindView(R.id.lv_card_coupons_expired)
    MyListView lvExpired;

    private boolean isCoupons = true;//当前是否为卡券
    private MyCouponsAdapter couponsNormalAdapter;
    private MyAwardAdapter awardNormalAdapter;
    private CouponsPresenter couponsPresenter;
    private MyCouponsAdapter couponsExpiredAdapter;
    private MyAwardAdapter awardExpireAdapter;

    public static MyCardCouponsFragment newInstance(int type) {
        MyCardCouponsFragment frg = new MyCardCouponsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentKey.EXTRA_TYPE, type);
        frg.setArguments(bundle);
        return frg;
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean hasDataInPage() {
        if (isCoupons)
            return couponsNormalAdapter != null && couponsNormalAdapter.getCount() > 0 || couponsExpiredAdapter != null && couponsExpiredAdapter.getCount() != 0;
        else
            return awardNormalAdapter != null && awardNormalAdapter.getCount() > 0 || awardExpireAdapter != null && awardExpireAdapter.getCount() != 0;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_card_coupons, null);
    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            int type = bundle.getInt(IntentKey.EXTRA_TYPE);
            isCoupons = type == TYPE_COUPONS;
        }
        if (isCoupons) {
            couponsNormalAdapter = new MyCouponsAdapter(mActivity, R.layout.item_coupons);
            couponsNormalAdapter.setCanUse(true);
            lvNormal.setAdapter(couponsNormalAdapter);
            couponsExpiredAdapter = new MyCouponsAdapter(mActivity, R.layout.item_coupons);
            couponsExpiredAdapter.setCanUse(false);
            lvExpired.setAdapter(couponsExpiredAdapter);
        } else {
            awardNormalAdapter = new MyAwardAdapter(mActivity, R.layout.item_award);
            awardNormalAdapter.setCanUse(true);
            lvNormal.setAdapter(awardNormalAdapter);
            awardNormalAdapter.setParentView(lvNormal);
            awardExpireAdapter = new MyAwardAdapter(mActivity, R.layout.item_award);
            awardExpireAdapter.setCanUse(false);
            lvExpired.setAdapter(awardExpireAdapter);
            awardExpireAdapter.setParentView(lvExpired);
        }
        setRefreshListener(new BaseLoadView.RefreshListener() {
            @Override
            public void refreshPage() {
                onLoadingStatus(CommonCode.General.DATA_LOADING);
                couponsPresenter.getCouponsList(0, isCoupons ? 1 : 2);
                couponsPresenter.getCouponsList(1, isCoupons ? 1 : 2);
            }
        });

    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        couponsPresenter = new CouponsPresenter();
        couponsPresenter.attachView(this);
    }

    @Override
    public void initData() {
        if (!NetUtils.isNetworkConnected(mActivity)) {
            onLoadingStatus(CommonCode.General.UN_NETWORK, getString(R.string.no_net_work));
            return;
        }
        onLoadingStatus(CommonCode.General.DATA_LOADING);
        couponsPresenter.getCouponsList(0, isCoupons ? 1 : 2);
        couponsPresenter.getCouponsList(1, isCoupons ? 1 : 2);
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
//        List<CouponsBean> test = (List<CouponsBean>) t;
//        Log.e("okhttp", test != null ? test.size() + "" : "size = null");
        if (flag == couponsPresenter.FLAG_COUPONS_NORMAL_LIST) {//没有过期
            List<CouponsBean> couponsList = (List<CouponsBean>) t;
            if (couponsList != null && couponsList.size() > 0 && couponsList.get(0) != null) {//加get(0)!=null,是因为在接口返回时出现了data[null]的情况.
                ArrayList<CouponsBean> normalList = new ArrayList<>();
                ArrayList<CouponsBean> expireList = new ArrayList<>();
                for (CouponsBean couponsBean : couponsList) {
                    if (couponsBean.getStatus() == 1) {
                        normalList.add(couponsBean);
                    } else {
                        expireList.add(couponsBean);
                    }
                }
                if (isCoupons) {
                    couponsNormalAdapter.setData(normalList);
                    couponsExpiredAdapter.addData(expireList);
                } else {
                    awardNormalAdapter.setData(normalList);
                    awardExpireAdapter.addData(expireList);
                }
            }
        }
        if (flag == couponsPresenter.FLAG_COUPONS_EXPIRED_LIST) {//已过期
            List<CouponsBean> couponsList = (List<CouponsBean>) t;
            if (couponsList != null && couponsList.size() > 0 && couponsList.get(0) != null) {//加get(0)!=null,是因为在接口返回时出现了data[null]的情况.
                if (isCoupons)
                    couponsExpiredAdapter.addData(couponsList);
                else awardExpireAdapter.addData(couponsList);
            }
        }
        if (isCoupons) {
            llHint.setVisibility(couponsExpiredAdapter.getCount() == 0 ? View.GONE : View.VISIBLE);
        } else {
            llHint.setVisibility(awardExpireAdapter.getCount() == 0 ? View.GONE : View.VISIBLE);
        }
        if (!hasDataInPage()) onLoadingStatus(CommonCode.General.DATA_EMPTY);
    }
}
