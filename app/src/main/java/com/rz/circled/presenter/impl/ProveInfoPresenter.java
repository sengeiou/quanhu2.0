package com.rz.circled.presenter.impl;

import android.content.Context;

import com.rz.circled.R;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.api.ApiProve;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.CallManager;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.ProveStatusBean;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Gsm on 2017/9/16.
 */
public class ProveInfoPresenter extends GeneralPresenter {

    public static final int FLAG_PROVE_INFO_SUCCESS = -600;//达人认证资料成功
    public static final int FLAG_PROVE_STATUS_ERROR = -701;//获得达人认证状态失败
    public static final int FLAG_PROVE_STATUS_SUCCESS = -700;//获得达人认证状态成功


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
     * 获得达人认证状态
     */
    public void getProveStatus() {
        Call<ResponseData<ProveStatusBean>> call = mApiService.getProveStatus(Session.getUserId());
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<ProveStatusBean>>() {
            @Override
            public void onResponse(Call<ResponseData<ProveStatusBean>> call, Response<ResponseData<ProveStatusBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<ProveStatusBean> responseData = response.body();
                    if (responseData.isSuccessful()) {
                        mView.updateViewWithFlag(responseData.getData(), FLAG_PROVE_STATUS_SUCCESS);
                    } else mView.updateViewWithFlag(responseData.getData(), FLAG_PROVE_STATUS_ERROR);
                } else mView.updateViewWithFlag(null, FLAG_PROVE_STATUS_ERROR);
            }

            @Override
            public void onFailure(Call<ResponseData<ProveStatusBean>> call, Throwable t) {
                super.onFailure(call, t);
                mView.updateViewWithFlag(null, FLAG_PROVE_STATUS_ERROR);
            }
        });
    }

    /**
     * 提交达人认证申请
     *
     * @param isOneSelf true 个人申请 ,false 企业申请
     * @param proveInfo
     */
    public void submitProveInfo(boolean isOneSelf, ProveStatusBean proveInfo) {
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
                        Toasty.info(mContext, mContext.getString(R.string.prove_info_success)).show();
                        mView.updateViewWithFlag(null, FLAG_PROVE_INFO_SUCCESS);
                    } else {
                        Toasty.info(mContext, mContext.getString(R.string.prove_info_fail)).show();
                    }
                } else {
                    Toasty.info(mContext, mContext.getString(R.string.prove_info_fail)).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                Toasty.info(mContext, mContext.getString(R.string.prove_info_fail)).show();
            }
        });
    }

    /**
     * 提交达人认证申请
     *
     * @param isOneSelf true 个人申请 ,false 企业申请
     * @param proveInfo
     */
    public void changeProveInfo(boolean isOneSelf, ProveStatusBean proveInfo) {
        Call<ResponseData> call;
        if (isOneSelf) call = mApiService.proveInfoPersonChange(proveInfo.getAuthType(),
                Session.getUserId(), proveInfo.getRealName(), proveInfo.getContactCall(),
                proveInfo.getIdCard(), proveInfo.getLocation(), proveInfo.getTradeField(),
                proveInfo.getOwnerAppId(), proveInfo.getResourceDesc());
        else call = mApiService.proveInfoAgencyChange(proveInfo.getAuthType(),
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
                        Toasty.info(mContext, mContext.getString(R.string.prove_info_success)).show();
                        mView.updateViewWithFlag(null, FLAG_PROVE_INFO_SUCCESS);
                    } else {
                        Toasty.info(mContext, mContext.getString(R.string.prove_info_fail)).show();
                    }
                } else {
                    Toasty.info(mContext, mContext.getString(R.string.prove_info_fail)).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                Toasty.info(mContext, mContext.getString(R.string.prove_info_fail)).show();
            }
        });
    }

}
