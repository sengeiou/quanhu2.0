package com.rz.circled.presenter.impl;

import android.content.Context;
import android.os.Handler;

import com.rz.circled.R;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.NetUtils;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.CallManager;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.constans.ReturnCode;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchPresenter extends GeneralPresenter {

    private ApiService mUserService;
    private IViewController mView;
    private Context mContext;

    @Override
    public Object getCacheData() {
        return null;
    }

    @Override
    public void attachView(IViewController view) {
        this.mView = view;
        mUserService = Http.getApiService(ApiService.class);
        mContext = getContext(mView);
    }

    @Override
    public void detachView() {

    }

    /**
     * 搜索内容
     */

    public void searchContent(String keyWord){
//        if (!NetUtils.isNetworkConnected(mContext)) {
//            mView.onLoadingStatus(CommonCode.General.WEB_ERROR, mContext.getString(R.string.no_net_work));
//        }
//        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.check_loading));
//        Call<ResponseData> call = mUserService.modifyPw(
//                Session.getUserId(),
//                password,
//                newPassword);
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData>() {
//            @Override
//            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        //修改密码成功
//                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.modify_success));
//                        mView.updateView("");
//                        return;
//                    } else {
////                        if (HandleRetCode.handler(mContext, res)) {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, "");
//                            }
//                        }, 2000);
//                        return;
////                        }
//                    }
//                }
//                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.modify_fail));
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData> call, Throwable t) {
//                super.onFailure(call, t);
//                //修改密码失败
//                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.modify_fail));
//            }
//        });
    }

    /**
     *  搜索用户
     */
    public void searchPerson(String keyWord) {

    }

    /**
     * 搜索私圈
     */
    public void searchPrivateCircle(String keyWord){


    }

    /**
     * 搜索圈子
     */
    public void searchCircle(String keyWord){


    }

    /**
     *  搜索悬赏
     */
    public void searchReward(String keyWord){


    }


}
