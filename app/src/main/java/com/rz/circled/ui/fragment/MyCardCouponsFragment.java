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
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.widget.MyListView;

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

    public static MyCardCouponsFragment newInstance(int type) {
        MyCardCouponsFragment frg = new MyCardCouponsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentKey.EXTRA_TYPE, type);
        frg.setArguments(bundle);
        return frg;
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
            lvNormal.setAdapter(couponsNormalAdapter);
        } else {
            awardNormalAdapter = new MyAwardAdapter(mActivity, R.layout.item_award);
            lvNormal.setAdapter(awardNormalAdapter);
        }

    }

    @Override
    public void initData() {

    }
}
