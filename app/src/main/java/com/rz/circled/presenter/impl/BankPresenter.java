package com.rz.circled.presenter.impl;

import android.content.Context;
import android.os.Handler;

import com.rz.circled.R;
import com.rz.circled.http.HandleRetCode;
import com.rz.circled.presenter.AbsPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.NetUtils;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.CallManager;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.BankCardModel;
import com.rz.httpapi.bean.CashModel;
import com.rz.httpapi.constans.ReturnCode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by xiayumo on 16/8/29.
 * 银行卡信息
 */
public class BankPresenter extends AbsPresenter {
    private IViewController mView;
    private ApiService mApiService;

    @Override
    public void attachView(IViewController view) {
        super.attachView(view);
        this.mView = view;
        mApiService = Http.getApiService(ApiService.class);
    }

    @Override
    public void loadData() {
        super.loadData();
    }

    @Override
    public void detachView() {
    }

    /**
     * 设置默认银行卡
     */
    public void setDefaultBanckCard(String cust2BankId) {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, activity.getString(R.string.status_un_network));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, activity.getString(R.string.is_setting));
//        Call<ResponseData> call = mUserService.setDefaultCard(1047, Session.getUserId(), cust2BankId);
        Call<ResponseData> call = mApiService.setDefaultCard(1047, Session.getUserId(), cust2BankId);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {

                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                        mView.updateView("2");
                        return;
                    } else {
                        if (HandleRetCode.handler(activity, res)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.setting_fail));
                                }
                            }, 2000);
                            return;
                        }
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.setting_fail));
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.setting_fail));
            }
        });
    }

    /**
     * 我绑定的银行卡列表
     *
     * @param custId 用户id
     */
    public void getBanckCardList(String custId) {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, activity.getString(R.string.status_un_network));
            return;
        }
//        Call<ResponseData<List<BankCardModel>>> call = mUserService.getBankCardList(1046, custId);
        Call<ResponseData<List<BankCardModel>>> call = mApiService.getBankCardList(1046, custId);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<BankCardModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<BankCardModel>>> call, Response<ResponseData<List<BankCardModel>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<List<BankCardModel>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<BankCardModel> dataList = res.getData();
                        if (dataList != null && !dataList.isEmpty()) {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            mView.updateView(dataList);
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_EMPTY, "");
                        }
                        return;
                    } else {
                        if (HandleRetCode.handler(activity, res)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.load_fail));
                                }
                            }, 2000);
                            return;
                        }
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.load_fail));
            }

            @Override
            public void onFailure(Call<ResponseData<List<BankCardModel>>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.load_fail));
            }
        });
    }

    /**
     * 解绑银行卡
     */
    public void unBandBanck(String cust2BankId, String pw) {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, activity.getString(R.string.status_un_network));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, activity.getString(R.string.check_ing));
        Call<ResponseData> call = mApiService.unBindCard(1053, pw, Session.getUserId(), cust2BankId);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                        //解绑成功
                        mView.updateView("1");
                        return;
                    } else {
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, "error");
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, "");
//                        return;
                        if (HandleRetCode.handler(activity, res)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.unbind_card_fail));
                                }
                            }, 2000);
                            return;
                        }
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.unbind_card_fail));
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.unbind_card_fail));
            }
        });
    }

    /**
     * 添加银行卡
     */
    public void addBankCard(String custId, String name, String cardNumber, String password) {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, activity.getString(R.string.status_un_network));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, activity.getString(R.string.submit_news_loading));
        Call<ResponseData> call = mApiService.bandCard(1045, custId, cardNumber, name, password);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                        //绑定成功
                        mView.updateView(res.getRet());
                        return;
                    } else {
                        if (HandleRetCode.handler(activity, res)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.bind_fail));
                                }
                            }, 2000);
                            return;
                        }
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.bind_fail));
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.bind_fail));
            }
        });
    }

    /**
     * 获取提现手续费
     */
    public void getCharge() {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, activity.getString(R.string.status_un_network));
            return;
        }
        Call<ResponseData<List<CashModel>>> call = mApiService.getServiceCharge(1059);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<CashModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<CashModel>>> call, Response<ResponseData<List<CashModel>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<List<CashModel>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<CashModel> cashs = res.getData();
                        if (null != cashs && !cashs.isEmpty()) {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            for (CashModel model : cashs) {
                                if (model.getType() == 0) {
                                    //提现类型
                                    mView.updateView(model);
                                }
                            }
                            return;
                        }
                    } else {
                        if (HandleRetCode.handler(activity, res)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.load_fail));
                                }
                            }, 2000);
                            return;
                        }
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.load_fail));
            }

            @Override
            public void onFailure(Call<ResponseData<List<CashModel>>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.load_fail));
            }
        });
    }

    /**
     * 提现
     */
    public void getCash(String password, String cost, String cust2BankId) {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, activity.getString(R.string.status_un_network));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, activity.getString(R.string.submit_news_loading));
        Call<ResponseData> call = mApiService.getCash(1055, password, Session.getUserId(), cost, cust2BankId);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                        mView.updateView("1");
                        return;
                    } else {
                        if (HandleRetCode.handler(activity, res)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.withdraw_fail));
                                    mView.updateViewWithFlag("", 0);
                                }
                            }, 2000);
                            return;
                        }
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.withdraw_fail));
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.withdraw_fail));
            }
        });
    }
}
