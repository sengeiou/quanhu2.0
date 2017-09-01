package com.rz.circled.presenter.impl;

import android.content.Context;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.rz.rz_rrz.R;
import com.rz.rz_rrz.app.App;
import com.rz.rz_rrz.constant.CodeStatus;
import com.rz.rz_rrz.constant.ReturnCode;
import com.rz.rz_rrz.http.BaseCallback;
import com.rz.rz_rrz.http.CallManager;
import com.rz.rz_rrz.http.Http;
import com.rz.rz_rrz.http.request.APIService;
import com.rz.rz_rrz.model.ResponseData;
import com.rz.rz_rrz.presenter.GeneralPresenter;
import com.rz.rz_rrz.utils.NetUtils;
import com.rz.rz_rrz.view.base.IViewController;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by xiayumo on 16/8/18.
 */
public class PersonInfoPresenter extends GeneralPresenter {

    private APIService mUserService;
    private IViewController mView;
    private Context mContext;

    @Override
    public void attachView(IViewController view) {
        this.mView = view;
        mUserService = Http.getUserService(mContext);
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
            mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_NET, mContext.getString(R.string.no_net_work));
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
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, "修改失败");
                        SVProgressHUD.showErrorWithStatus(mContext, res.getMsg());

                    }
                } else {
                    mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, R.string.modify_fail);
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, R.string.modify_fail);
            }
        });
    }

    public void report(String sourceId, String custId, int infoType, String content) {
        if (!NetUtils.isNetworkConnected(App.getContext())) {
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
                        mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_FULL, R.string.refunds_success);
                        mView.updateView(0);
                    } else {
                        mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, mContext.getString(R.string.refunds_fail) +":" + res.getMsg());
                    }
                } else {
                    mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, R.string.refunds_fail);
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, R.string.refunds_fail);
            }
        });
    }


}
