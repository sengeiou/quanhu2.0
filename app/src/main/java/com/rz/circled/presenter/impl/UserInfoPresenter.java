package com.rz.circled.presenter.impl;

import android.content.Context;
import android.os.Handler;

import com.rz.circled.R;
import com.rz.circled.event.EventConstant;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.NetUtils;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.CallManager;
import com.rz.httpapi.api.HandleRetCode;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.LoginTypeBean;
import com.rz.httpapi.bean.RegisterBean;
import com.rz.httpapi.bean.RegisterModel;
import com.rz.httpapi.bean.UserInfoBean;
import com.rz.httpapi.constans.ReturnCode;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 作者：Administrator on 2016/8/13 0013 10:42
 * 功能：处理注册数据
 * 说明：
 */
public class UserInfoPresenter extends GeneralPresenter {

    private IViewController mView;

    private Context mContext;

    private ApiService mUserService;

    @Override
    public Object getCacheData() {
        return null;
    }

    @Override
    public void attachView(IViewController view) {
        this.mView = view;
        mContext = getContext(mView);
        mUserService = Http.getApiService(ApiService.class);
    }

    @Override
    public void loadData() {
        super.loadData();
    }

    @Override
    public void detachView() {
    }

    /**
     * 获得验证码
     *
     * @param phone
     */
    public void getVeriCode(String phone, final String function) {

        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.is_loading));
        Call<ResponseData<RegisterBean>> call = null;
        call = mUserService.sendVeriCode(1001, phone, Type.VERIFY_CODE, function);

        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<RegisterBean>>() {
            @Override
            public void onResponse(Call<ResponseData<RegisterBean>> call, Response<ResponseData<RegisterBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        RegisterBean model = (RegisterBean) res.getData();
                        if (model != null) {
                            if (function == null || function.length() == 0 || Type.FUNCTION_CODE_2.equals(function)) {
                                mView.updateView(model);
                                mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                            } else {
                                mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                            }
                        }
                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, res.getMsg());
                        mView.updateViewWithFlag( "",200);
                        Toasty.info(mContext, res.getMsg()).show();
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.send_code_failed));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<RegisterBean>> call, Throwable t) {
                super.onFailure(call, t);
                //发送验证码失败
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.send_code_failed));
            }
        });
    }


    /**
     * 注册用户
     */
    public void registerUser(String phone, String password, String veriCode,String location, String cityCode) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.WEB_ERROR, mContext.getString(R.string.no_net_work));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.register_loading));
        Call<ResponseData<UserInfoBean>> call = mUserService.register(
                1002,
                phone,
                password,
                veriCode, "1",
                "",
                cityCode,
                location


        );
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<UserInfoBean>>() {
            @Override
            public void onResponse(Call<ResponseData<UserInfoBean>> call, Response<ResponseData<UserInfoBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<UserInfoBean> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {

                        UserInfoBean user = res.getData();
                        if (null != user) {
                            //注册成功
                            Session.setLoginWay(Type.LOGIN_PHONE);
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.regist_success));
                            mView.updateView(user);
                            return;
                        }
                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        //注册失败
                        mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, res.getMsg());
                        return;
                    }
                }
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.regist_failed));
            }

            @Override
            public void onFailure(Call<ResponseData<UserInfoBean>> call, Throwable t) {
                super.onFailure(call, t);
                //注册失败
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.regist_failed));
            }
        });
    }

    /**
     * 忘记密码--重置密码
     */
    public void changePw(String phone, String password, String veriCode) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.WEB_ERROR, mContext.getString(R.string.no_net_work));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.check_loading));
        Call<ResponseData> call = mUserService.changePw(
                1010,
                phone,
                password,
                veriCode);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        //重置密码成功
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.change_pw_success));
                        mView.updateView("1");
                        return;
                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        //重置密码失败
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, res.getMsg());
                        return;
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.change_pw_fail));
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                //重置密码失败
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.change_pw_fail));
            }
        });
    }

    /**
     * 修改登录密码
     */
    public void modifyPw(String password, String newPassword) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.WEB_ERROR, mContext.getString(R.string.no_net_work));
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.check_loading));
        Call<ResponseData> call = mUserService.modifyPw(
                Session.getUserId(),
                password,
                newPassword);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        //修改密码成功
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.modify_success));
                        mView.updateView("");
                        return;
                    } else {
//                        if (HandleRetCode.handler(mContext, res)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, "");
                                }
                            }, 2000);
                            return;
//                        }
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.modify_fail));
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                //修改密码失败
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.modify_fail));
            }
        });
    }

    /**
     * 绑定手机号
     */
    public void bindPhone(final String phone, String password, String veriCode) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.WEB_ERROR, mContext.getString(R.string.no_net_work));
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.check_loading));
        Call<ResponseData> call = mUserService.bindPhone(
                1012,
                Session.getUserId(),
                phone,
                password,
                veriCode);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    final ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        //绑定手机号成功
                        Session.setUserLoginPw(true);
                        Session.setUserPhone(phone);
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.bind_success));
                        mView.updateView("1");
                        return;
                    } else {
//                        if (HandleRetCode.handler(mContext, res)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, res.getMsg());
                                }
                            }, 2000);
                            return;
//                        }
                    }
                }
                //绑定手机号失败
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.bind_fail));
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                //绑定手机号失败
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.bind_fail));
            }
        });
    }

    /**
     * 验证安全信息
     */
    public void checkCode(String phone, String code, String name, String problem) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, "验证中...");
//        Call<ResponseData<RegisterModel>> call = mUserService.checkProblem(
        Call<ResponseData<RegisterModel>> call = mUserService.checkProblem(
                1076,
                Session.getUserId(),
                phone,
                code,
                name,
                problem);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<RegisterModel>>() {
            @Override
            public void onResponse(Call<ResponseData<RegisterModel>> call, Response<ResponseData<RegisterModel>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        //验证短信验证码成功
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                        mView.updateView("1");
                        return;
                    } else {
                        if (HandleRetCode.handler(mContext, res)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, "");
                                }
                            }, 2000);
                        }
                        return;
                    }
                }
                //验证短信验证码失败
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, "验证失败");
            }

            @Override
            public void onFailure(Call<ResponseData<RegisterModel>> call, Throwable t) {
                super.onFailure(call, t);
                //验证短信验证码失败
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, "验证失败");
            }
        });
    }




    /**
     * 验证手机号是否绑定
     *
     */
    public void verfityBoundPhone(String custId) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, "验证中...");
//        Call<ResponseData<RegisterModel>> call = mUserService.checkProblem(
        Call<ResponseData<List<LoginTypeBean>>> call = mUserService.loginMethod(1020,
                custId);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<LoginTypeBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<LoginTypeBean>>> call, Response<ResponseData<List<LoginTypeBean>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<LoginTypeBean> model = (List<LoginTypeBean>) res.getData();
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        mView.updateViewWithFlag(model,200);
                        return;
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                            }
                        }, 2000);
                        return;
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
            }

            @Override
            public void onFailure(Call<ResponseData<List<LoginTypeBean>>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
            }
        });
    }

//    /**
//     * 绑定手机号
//     *
//     * @param phones
//     */
//    public void checkPhone(String phones) {
//        Call<ResponseData<TreeMap<String, String>>> call = Http.getTestService(mContext).findPhone(1021, Session.getUserId(), phones);
//        CallManager.add(call);
//        Response<ResponseData<TreeMap<String, String>>> response = null;
//
//        try {
//            response = call.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (response.isSuccessful()) {
//            ResponseData<TreeMap<String, String>> res = response.body();
//            if (res.getRet() == ReturnCode.SUCCESS) {
//                TreeMap<String, String> model = res.getData();
//                if (null != model) {
//                    mView.updateView(model);
//                } else {
//                    mView.updateView("");
//                }
//                return;
//            } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
//                //发送失败
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, res.getMsg());
//                return;
//            }
//        }
//        mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, mContext.getString(R.string.send_code_failed));
//    }


    /**
     * 绑定手机号
     */
    public void boundPhone(final String phone, String password, String veriCode,String custId) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.WEB_ERROR, mContext.getString(R.string.no_net_work));
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.check_loading));
        Call<ResponseData> call = mUserService.bindPhone(
                1012,
                custId,
                phone,
                password,
                veriCode);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    final ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        //绑定手机号成功
                        Session.setUserLoginPw(true);
                        Session.setUserPhone(phone);
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.bind_success));
                        mView.updateView("1");
                        return;
                    } else {
//                        if (HandleRetCode.handler(mContext, res)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, res.getMsg());
                            }
                        }, 2000);
                        return;
//                        }
                    }
                }
                //绑定手机号失败
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.bind_fail));
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                //绑定手机号失败
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.bind_fail));
            }
        });
    }


    /**
     * 获得验证码
     *
     * @param phone
     */
    public void getVeriCode2(String phone, final String function) {

        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.is_loading));
        Call<ResponseData<RegisterBean>> call = null;
        call = mUserService.sendVeriCode(1001, phone, Type.VERIFY_CODE, function);

        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<RegisterBean>>() {
            @Override
            public void onResponse(Call<ResponseData<RegisterBean>> call, Response<ResponseData<RegisterBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        RegisterBean model = (RegisterBean) res.getData();
                        if (model != null) {
                            if (function == null || function.length() == 0 || Type.FUNCTION_CODE_5.equals(function)) {
                                mView.updateView(model);
                                mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            } else {
                                mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            }
                        }
                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, res.getMsg());
                        EventBus.getDefault().post(new BaseEvent(EventConstant.BOUND_PHONE_FAIL, res.getMsg()));
                        Toasty.info(mContext, res.getMsg()).show();
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.send_code_failed));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<RegisterBean>> call, Throwable t) {
                super.onFailure(call, t);
                //发送验证码失败
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.send_code_failed));
            }
        });
    }



}
