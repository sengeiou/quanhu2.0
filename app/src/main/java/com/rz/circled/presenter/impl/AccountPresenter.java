package com.rz.circled.presenter.impl;

import android.content.Context;

import com.rz.circled.R;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.circled.ui.activity.AccountSafeAty;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Type;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.NetUtils;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.CallManager;
import com.rz.httpapi.api.HandleRetCode;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.LoginWayModel;
import com.rz.httpapi.bean.UserInfoModel;
import com.rz.httpapi.constans.ReturnCode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


/**
 * 作者：Administrator on 2016/8/22 0022 16:50
 * 功能：账号与安全设置
 * 说明：
 */
public class AccountPresenter extends GeneralPresenter {

    private static final String OPEN_PAY = "OPEN_PAY";
    //处理缓存
    private EntityCache<LoginWayModel> mLoginWay;

    private IViewController mView;

    private ApiService mUserService;

    private Context mContext;

    private AccountSafeAty mAccountAty;

    public void setmAccountAty(AccountSafeAty mAccountAty) {
        this.mAccountAty = mAccountAty;
    }

    @Override
    public void attachView(IViewController view) {
        this.mView = view;
        mContext = getContext(mView);
        mUserService = Http.getApiService(ApiService.class);
        mLoginWay = new EntityCache<LoginWayModel>(mContext, LoginWayModel.class);
    }

    @Override
    public Object getCacheData() {
        return mLoginWay.getListEntityAddTag(LoginWayModel.class, "LoginWayModel");
    }

    @Override
    public void detachView() {

    }

    /**
     * 获取登录方式
     */
    public void getLoginWay() {
        if (!NetUtils.isNetworkConnected(getContext(mView))) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));

            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.is_loading));
        Call<ResponseData<List<LoginWayModel>>> call = mUserService.getLoginMethod(1008, Session.getUserId());
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<LoginWayModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<LoginWayModel>>> call, Response<ResponseData<List<LoginWayModel>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<List<LoginWayModel>> res = response.body();
                    //FIXME 服务端修改字段
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<LoginWayModel> logins = res.getData();
                        if (null != logins && !logins.isEmpty()) {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            mView.updateView(logins);
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_EMPTY, "");
                        }
                        try {
                            mLoginWay.putListEntityAddTag(logins, "LoginWayModel");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    } else {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, "");
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
            }

            @Override
            public void onFailure(Call<ResponseData<List<LoginWayModel>>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
            }
        });
    }

    /**
     * 获取账户信息-是否设置支付密码和是否开启免密支付
     */
    public void isSettingPw() {
        if (!NetUtils.isNetworkConnected(getContext(mView))) {
            if (null != mAccountAty) {
                mAccountAty.handPwNews(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            } else {
                mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            }
            return;
        }
//        Call<ResponseData<UserInfoModel>> call = mUserService.searchUserNews(1054, Session.getUserId());
        Call<ResponseData<UserInfoModel>> call = mUserService.searchUserNews(1054, Session.getUserId());
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<UserInfoModel>>() {
            @Override
            public void onResponse(Call<ResponseData<UserInfoModel>> call, Response<ResponseData<UserInfoModel>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<UserInfoModel> res = response.body();
                    //FIXME 服务端修改字段
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        UserInfoModel users = res.getData();
                        if (null != users) {
                            if (null != mAccountAty) {
                                mAccountAty.handPwNews(CommonCode.General.DATA_SUCCESS, "");
                                mAccountAty.handPw(users);
                            } else {
                                mView.updateViewWithFlag(users, 0);
                            }
                        } else {
                            if (null != mAccountAty) {
                                mAccountAty.handPwNews(CommonCode.General.DATA_EMPTY, "");
                            } else {
                                mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.get_info_fail));
                            }
                        }
                        return;
                    } else {
                        HandleRetCode.handler(getContext(mView), res);
                    }
                }
                if (null != mAccountAty) {
                    mAccountAty.handPwNews(CommonCode.General.UN_NETWORK, "");
                } else {
                    mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.get_info_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<UserInfoModel>> call, Throwable t) {
                super.onFailure(call, t);
                if (null != mAccountAty) {
                    mAccountAty.handPwNews(CommonCode.General.UN_NETWORK, "");
                } else {
                    mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.get_info_fail));
                }
            }
        });
    }

    /**
     * 设置或者修改支付密码
     */
    public void setPayPw(final int type, String payPassword, String oldPayPassword) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.check_loading));
//        Call<ResponseData> call = mUserService.setOrModifyPayPw(
//                Session.getUserId(),
//                payPassword,
//                oldPayPassword);
        Call<ResponseData> call = mUserService.setOrModifyPayPw(
                Session.getUserId(),
                payPassword,
                oldPayPassword);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        //修改密码成功
                        if (type == Type.HAD_SET_PW) {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                        } else if (type == Type.HAD_NO_SET_PW) {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                        } else if (type == Type.RESET_PAY_PW) {
                            //找回忘记支付密码
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                        }
                        mView.updateView(type);
                        return;
                    } else {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, "");
                    }
                }
                if (type == Type.HAD_SET_PW) {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.modify_fail));
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.setting_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                //修改密码失败
                if (type == Type.HAD_SET_PW) {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.modify_fail));
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.setting_fail));
                }
            }
        });
    }

    /**
     * 忘记支付密码--重置密码
     */
    public void forgetPayPw(final int type, String payPassword, String name, String idCard) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.check_loading));


//        Call<ResponseData> call = mUserService.forgetPayPw(
//                1048,
//                Session.getUserId(),
//                payPassword,
//                custNname,
//                idCard);
        Call<ResponseData> call = mUserService.forgetPayPw(
                1048,
                Session.getUserId(),
                payPassword,
                name,
                idCard);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        //重置支付密码成功
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.change_pw_success));
                        mView.updateView(type);
                        return;
                    }
                }
                //重置支付密码失败
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.reset_fail));
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                //重置支付密码失败
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.reset_fail));
            }
        });
    }

    /**
     * 开启或者关闭免密支付 0不设置 1设置
     */
    public void openOrClosePay(final int type, String password) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
        }
//        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.data_loading));
//        Call<ResponseData> call = mUserService.closeOrOpenPay(
//                1064,
//                Session.getUserId(),
//                type, password);
        Call<ResponseData> call = mUserService.closeOrOpenPay(
                1064,
                Session.getUserId(),
                type, password);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        if (type == Type.OPEN_EASY_PAY) {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.easy_pay_open));
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.easy_pay_close));
                        }
                        mView.updateViewWithLoadMore(type, false);
                        return;
                    } else {
                        if (HandleRetCode.handler(mContext, res)) {
                            mView.updateViewWithLoadMore(-1, false);
//                            mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.action_fail));
                            return;
                        }
                        mView.updateViewWithLoadMore(-1, false);
                    }
                } else {
                    mView.updateViewWithLoadMore(-1, false);
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.action_fail));
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                //开启免密支付
                mView.updateViewWithLoadMore(-1, false);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.action_fail));
            }
        });
    }

    /**
     * 设置安全保护信息
     */
    public void setSecurityProblem(String name, String idCard) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.data_loading));
//        Call<ResponseData> call = mUserService.setSecurityProblem(
        Call<ResponseData> call = mUserService.setSecurityProblem(
                1074,
                Session.getUserId(),
                name, idCard);
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
                    }
                }
                // 设置安全保护信息
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.setting_fail));
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                // 设置安全保护信息
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.setting_fail));
            }
        });
    }


}
