package com.rz.circled.presenter.impl;

import android.content.Context;

import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.ui.inter.IViewController;
import com.rz.httpapi.api.ApiProve;
import com.rz.httpapi.api.Http;

/**
 * Created by Gsm on 2017/9/18.
 */
public class CouponsPersenter extends GeneralPresenter {

    private Context mContext;
    private IViewController mView;
    private ApiProve mApiService;

    @Override
    public Object getCacheData() {
        return null;
    }

    @Override
    public void attachView(IViewController view) {
        mView = view;
        mContext = getContext(mView);
        mApiService = Http.getApiService(ApiProve.class);
    }

    @Override
    public void detachView() {

    }

    /**
     * 获得卡券列表
     *
     * @param status
     */
    public void getCouponsList(int status) {

    }
}
