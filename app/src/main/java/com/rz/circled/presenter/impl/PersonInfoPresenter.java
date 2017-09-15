package com.rz.circled.presenter.impl;

import android.content.Context;
import android.os.Handler;

import com.rz.circled.R;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.application.BaseApplication;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.NetUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.CallManager;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.CircleDynamic;
import com.rz.httpapi.bean.SearchDataBean;
import com.rz.httpapi.constans.ReturnCode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by xiayumo on 16/8/18.
 */
public class PersonInfoPresenter extends GeneralPresenter {

    private ApiService mUserService;
    private IViewController mView;
    private Context mContext;


    //每页分页标记
    private int start = 0;

    //记录每页分页标记
    private int record_start = 0;

    //是否没有数据
    private boolean isNoData;

    @Override
    public void attachView(IViewController view) {
        this.mView = view;
        mUserService = Http.getApiService(ApiService.class);
        mContext = getContext(mView);
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

    /**
     * 保存等信息
     */
    public void savePersonInfo(String id, String paramasType, final String paramas) {

        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.status_un_network));
            return;
        }

        Call<ResponseData> call = null;

        switch (paramasType) {
            case "headImg":
                call = mUserService.editSaveHeadImg(1050, id, paramas);
                break;
            case "nickName":
                call = mUserService.editSaveNickName(1050, id, paramas);
                break;
            case "sex":
                call = mUserService.editSaveSex(1050, id, paramas);
                break;
            case "signature":
                call = mUserService.editSaveSignature(1050, id, paramas);
                break;
            case "location":
                call = mUserService.editSaveAress(1050, id, paramas);
                break;
            case "desc":
                call = mUserService.editSaveDesc(1050, id, paramas);
                break;
            default:
                break;

        }
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {

                        mView.updateView(paramas);

                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        SVProgressHUD.showErrorWithStatus(mContext, res.getMsg());

                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.modify_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.modify_fail));
            }
        });
    }

    public void report(String sourceId, String custId, int infoType, String content) {
        if (!NetUtils.isNetworkConnected(BaseApplication.getContext())) {
            return;
        }
        Call<ResponseData> call = mUserService.report(custId, infoType, sourceId, content);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.refunds_success));
                        mView.updateView(0);
                    } else {
                        mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.refunds_fail) + ":" + res.getMsg());
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.refunds_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.refunds_fail));
            }
        });
    }

    /**
     * 获取我的文章
     */

    public void getArticle(final boolean loadMore, String custId, String resourceType){

        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.WEB_ERROR, mContext.getString(R.string.no_net_work));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING);
        if (!loadMore) {
            start = 0;
        } else {
            if (isNoData) {
                start = record_start;
            } else {
                start += Constants.PAGESIZE;
            }
            record_start = start;
        }

        Call<ResponseData<CircleDynamic>> call = mUserService.getMyResource(
                custId,
                5,
                resourceType,
                0);


        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<CircleDynamic>>() {
            @Override
            public void onResponse(Call<ResponseData<CircleDynamic>> call, Response<ResponseData<CircleDynamic>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {

                    ResponseData<CircleDynamic> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<CircleDynamic> modelList = (List<CircleDynamic>) res.getData();

                        if (null != modelList && !modelList.isEmpty()) {
                            isNoData = false;
                            mView.updateViewWithLoadMore(modelList, loadMore);
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        } else {
                            mView.updateViewWithLoadMore(modelList, loadMore);
                            mView.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                            isNoData = true;
                        }
                        return;
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                            }
                        }, 2000);
                        isNoData = true;
                        return;
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                isNoData = true;

            }

            @Override
            public void onFailure(Call<ResponseData<CircleDynamic>> call, Throwable t) {
                super.onFailure(call, t);
                //发送验证码失败
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }
        });


    }

}
