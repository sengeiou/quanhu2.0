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
import com.rz.httpapi.api.HandleRetCode;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.constans.ReturnCode;

import retrofit2.Call;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * 作者：Administrator on 2016/8/20 0020 19:33
 * 功能：版本更新或者退出APP
 * 说明：
 */
public class UpdateOrExitPresenter extends GeneralPresenter {

    private IViewController mView;

    private ApiService mUserService;

    private Context mContext;

    @Override
    public void attachView(IViewController view) {
        this.mView = view;
        mContext = getContext(mView);
        mUserService = Http.getApiService(ApiService.class);
    }

    @Override
    public Object getCacheData() {
        return null;
    }

    @Override
    public void loadData() {
        super.loadData();
    }


    @Override
    public void detachView() {

    }

    public void messageFree(int pushStatus) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        mUserService.setMessFree(Session.getUserId(),pushStatus)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ResponseData>() {
                        @Override
                        public void call(ResponseData res) {


                        }
                    });

    }

    /**
     * 退出APP
     */
    public void ExitApp() {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.exit_loading));
        Call<ResponseData> call = mUserService.exitAPP(Session.getUserId());
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.exit_success));
                        mView.updateView("");
                        return;
                    } else {
                        if (HandleRetCode.handler(mContext, res)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, "");
                                }
                            }, 2000);
                            return;
                        }
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.exit_fail));
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                //退出失败
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.exit_fail));
            }
        });
    }
}
