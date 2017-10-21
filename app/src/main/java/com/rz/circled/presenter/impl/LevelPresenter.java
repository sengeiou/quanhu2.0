package com.rz.circled.presenter.impl;

import android.content.Context;

import com.rz.circled.R;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.NetUtils;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.MyLevelAcountBean;
import com.rz.httpapi.bean.MyLevelBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Gsm on 2017/9/19.
 */
public class LevelPresenter extends GeneralPresenter {

    public final int FLAG_LEVEL_ACOUNT = 100;//成长总值
    public final int FLAG_LEVEL_LIST = 101;//成长值列表数据


    @Override
    public Object getCacheData() {
        return null;
    }

    private Context mContext;
    private IViewController mView;
    private ApiService mApiService;

    @Override
    public void attachView(IViewController view) {
        mView = view;
        mContext = getContext(mView);
        mApiService = Http.getApiService(ApiService.class);
    }

    @Override
    public void detachView() {

    }

    /**
     * 获得用户积分/成长总值
     */
    public void getLevelAcount() {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        Call<ResponseData<MyLevelAcountBean>> call = mApiService.getLevelAcount(Session.getUserId());
        call.enqueue(new BaseCallback<ResponseData<MyLevelAcountBean>>() {
            @Override
            public void onResponse(Call<ResponseData<MyLevelAcountBean>> call, Response<ResponseData<MyLevelAcountBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<MyLevelAcountBean> responseData = response.body();
                    if (responseData.isSuccessful()) {
                        mView.updateViewWithFlag(responseData.getData(), FLAG_LEVEL_ACOUNT);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseData<MyLevelAcountBean>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    /**
     * 获得用户积分/成长总值
     */
    public void getLevelList(int limit, int start, final boolean loadMore) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        Call<ResponseData<List<MyLevelBean>>> call = mApiService.getLevelList(Session.getUserId(), limit, start);
        call.enqueue(new BaseCallback<ResponseData<List<MyLevelBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<MyLevelBean>>> call, Response<ResponseData<List<MyLevelBean>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<List<MyLevelBean>> responseData = response.body();
                    if (responseData.isSuccessful()) {
                        mView.updateViewWithLoadMore(responseData.getData(), loadMore);
                    }
                }
                mView.updateViewWithFlag(null, FLAG_LEVEL_LIST);
            }

            @Override
            public void onFailure(Call<ResponseData<List<MyLevelBean>>> call, Throwable t) {
                super.onFailure(call, t);
                mView.updateViewWithFlag(null, FLAG_LEVEL_LIST);
            }
        });
    }
}
