package com.rz.circled.presenter.impl;

import android.content.Context;

import com.rz.circled.http.ApiYylService;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.constant.CommonCode;
import com.rz.common.ui.inter.IViewController;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.CouponsBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Gsm on 2017/9/18.
 */
public class CouponsPresenter extends GeneralPresenter {

    public final int FLAG_COUPONS_NORMAL_LIST = 100;//获得卡券未过期列表成功
    public final int FLAG_COUPONS_EXPIRED_LIST = 101;//获得卡券已过期列表成功

    private Context mContext;
    private IViewController mView;
    private ApiYylService mApiService;

    @Override
    public Object getCacheData() {
        return null;
    }

    @Override
    public void attachView(IViewController view) {
        mView = view;
        mContext = getContext(mView);
        mApiService = Http.getApiService(ApiYylService.class);
    }

    @Override
    public void detachView() {

    }

    /**
     * 获得卡券列表
     *
     * @param isOverdue 是否过期 0没有过期 1过期
     * @param status    类型1投票券 2自定义奖品
     */
    public void getCouponsList(final int isOverdue, int status) {
//        Call<ResponseData<List<CouponsBean>>> call = mApiService.getCouponsList(isOverdue, Session.getUserId(), status);
        Call<ResponseData<List<CouponsBean>>> call = mApiService.getCouponsList(isOverdue, "eldrsm2k", status);
        call.enqueue(new BaseCallback<ResponseData<List<CouponsBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<CouponsBean>>> call, Response<ResponseData<List<CouponsBean>>> response) {
                super.onResponse(call, response);
                mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                if (response.isSuccessful()) {
                    ResponseData<List<CouponsBean>> responseData = response.body();
                    if (responseData.isSuccessful()) {
                        mView.updateViewWithFlag(responseData.getData(), isOverdue == 0 ? FLAG_COUPONS_NORMAL_LIST : FLAG_COUPONS_EXPIRED_LIST);
                    } else {
                        mView.updateViewWithFlag(null, 0);
                    }
                } else mView.updateViewWithFlag(null, 0);
            }

            @Override
            public void onFailure(Call<ResponseData<List<CouponsBean>>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                mView.updateViewWithFlag(null, 0);
            }
        });
    }
}
