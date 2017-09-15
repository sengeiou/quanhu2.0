package com.rz.circled.presenter.impl;

import android.content.Context;
import android.os.Handler;

import com.rz.circled.R;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Type;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.NetUtils;
import com.rz.httpapi.api.ApiRewardService;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.CallManager;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.RegisterBean;
import com.rz.httpapi.bean.RewardBean;
import com.rz.httpapi.bean.RewardGiftModel;
import com.rz.httpapi.bean.UserInfoBean;
import com.rz.httpapi.constans.ReturnCode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者：Administrator on 2016/8/13 0013 10:42
 * 功能：处理注册数据
 * 说明：
 */
public class RewardGiftPresenter extends GeneralPresenter {

    private IViewController mView;

    private Context mContext;

    private ApiRewardService mApiService;

    @Override
    public Object getCacheData() {
        return null;
    }

    @Override
    public void attachView(IViewController view) {
        this.mView = view;
        mContext = getContext(mView);
        mApiService = Http.getApiService(ApiRewardService.class);
    }

    @Override
    public void loadData() {
        super.loadData();
    }

    @Override
    public void detachView() {
    }

    public void rewardGiftList(int start, int limit) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.WEB_ERROR, mContext.getString(R.string.no_net_work));
            return;
        }
        Call<ResponseData<List<RewardGiftModel>>> call = mApiService.rewardGiftList(start, limit);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<RewardGiftModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<RewardGiftModel>>> call, Response<ResponseData<List<RewardGiftModel>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body().isSuccessful()) {
                    List<RewardGiftModel> mGifts = response.body().getData();
                    if (mGifts != null && !mGifts.isEmpty()) {
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        mView.updateView(mGifts);
                    } else {
                        mView.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<RewardGiftModel>>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
            }
        });
    }

    public void rewardDo(String custId, String giftId, int giftNum, String resourceId, String toCustId, String type) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.WEB_ERROR, mContext.getString(R.string.no_net_work));
            return;
        }
        Call<ResponseData<RewardBean>> call = mApiService.rewardDo(custId, giftId, giftNum, resourceId, toCustId, type);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<RewardBean>>() {
            @Override
            public void onResponse(Call<ResponseData<RewardBean>> call, Response<ResponseData<RewardBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body().isSuccessful()) {
                    RewardBean data = response.body().getData();
                    if (data != null) {
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        mView.updateView(data);
                    } else {
                        mView.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<RewardBean>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
            }
        });
    }
}
