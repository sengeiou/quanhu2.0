package com.rz.circled.presenter.impl;

import android.content.Context;

import com.rz.circled.R;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.NetUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiProve;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.CallManager;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.ProveInfoBean;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Gsm on 2017/9/16.
 */
public class ProveInfoPresenter extends GeneralPresenter {

    public static final int FLAG_PROVE_INFO_SUCCESS = -600;//达人认证资料成功


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

    public void submitProveInfo(boolean isOneSelf, ProveInfoBean proveInfo) {
        if (!NetUtils.isNetworkConnected(getContext(mView))) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING);
        Call<ResponseData> call;
        if (isOneSelf) call = mApiService.proveInfoPerson(proveInfo.getAuthType(),
                Session.getUserId(), proveInfo.getRealName(), proveInfo.getContactCall(),
                proveInfo.getIdCard(), proveInfo.getLocation(), proveInfo.getTradeField(),
                proveInfo.getOwnerAppId(), proveInfo.getResourceDesc());
        else call = mApiService.proveInfoAgency(proveInfo.getAuthType(),
                Session.getUserId(), proveInfo.getOrganizationName(), proveInfo.getRealName(),
                proveInfo.getContactCall(), proveInfo.getLocation(), proveInfo.getTradeField(),
                proveInfo.getOwnerAppId(), proveInfo.getResourceDesc(), proveInfo.getOrganizationPaper());
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                if (response.isSuccessful()) {
                    if (response.body().isSuccessful()) {
                        SVProgressHUD.showInfoWithStatus(mContext, mContext.getString(R.string.prove_info_success));
                        mView.updateViewWithFlag(null, FLAG_PROVE_INFO_SUCCESS);
                    } else {
                        SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.prove_info_success));
                    }
                } else {
                    SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.prove_info_success));
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.prove_info_success));
            }
        });
    }

}
