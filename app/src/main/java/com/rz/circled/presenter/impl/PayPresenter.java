package com.rz.circled.presenter.impl;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.rz.circled.R;
import com.rz.circled.pay.PayResult;
import com.rz.circled.presenter.AbsPresenter;
import com.rz.circled.ui.activity.SetPayPassAty;
import com.rz.circled.widget.password.GridPasswordView;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.Currency;
import com.rz.common.utils.DialogUtils;
import com.rz.common.utils.NetUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.CallManager;
import com.rz.httpapi.api.HandleRetCode;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.AccountBean;
import com.rz.httpapi.bean.BillDetailModel;
import com.rz.httpapi.bean.PayOrderInfoBean;
import com.rz.httpapi.bean.PaySignModel;
import com.rz.httpapi.bean.RewardDetailBean;
import com.rz.httpapi.bean.ScoreBean;
import com.rz.httpapi.bean.UserInfoModel;
import com.rz.httpapi.constans.ReturnCode;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JS01 on 2016/7/28.
 * 处理支付相关的数据逻辑,YIN
 */
public class PayPresenter extends AbsPresenter {

    public static final int SDK_ALIAY = 1;

    //阿里订单支付成功
    public static final String ALIPAY_STATE_SUCCESS = "9000";
    //正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
    public static final String ALIPAY_STATE_CONFIRMING = "8000";
    //用户中途取消
    public static final String ALIPAY_STATE_CANCEL = "6001";
    //网络连接出错
    public static final String ALIPAY_STATE_NET_ERROR = "6002";

    private int start = 0;

    //是否加载出错
    private boolean isDataError;

    private int record_start = 0;

    //调用支付
    private IViewController mView;

    private ApiService mUserService;

    private boolean flag;
    //记录验证支付密码的免密
    private String mCheckPayPw;

    public PayPresenter(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void attachView(IViewController view) {
        super.attachView(view);
        this.mView = view;
//        if (flag) {
//            //支付临时地址
//            mAddSignPayService = Http.getAddSignService(activity, "1052");
//        }
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
     * 创建订单
     */
    public void pay(String custId, final String payWay, String orderAmount, String currency) {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, activity.getString(R.string.status_un_network));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING);
        Session.setNowAct("1052");
        Call<ResponseData<PaySignModel>> call = mUserService.payProvingSign(1052, custId, payWay, "2", orderAmount.replace(".0", ""), currency);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<PaySignModel>>() {
            @Override
            public void onResponse(Call<ResponseData<PaySignModel>> call, Response<ResponseData<PaySignModel>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<PaySignModel> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        PaySignModel model = res.getData();
                        if (null != model) {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                            PaySignModel.Sign sign = model.ext;
                            if (null != sign) {
                                String orderInfo = sign.orderStr;
                                /**
                                 * 支付宝微信的验证信息分别传不同
                                 */
                                if (TextUtils.equals(payWay, Type.TYPE_ALI_PAY)) {
                                    if (StringUtils.isEmpty(orderInfo)) {
                                        SVProgressHUD.showErrorWithStatus(activity, activity.getString(R.string.backstage_configuration_error));
                                    } else {
                                        aliPay(orderInfo);
                                    }
                                } else if (TextUtils.equals(payWay, Type.TYPE_WX_PAY)) {
                                    wxPay(sign);
                                }
                            } else {
                                SVProgressHUD.showErrorWithStatus(activity, activity.getString(R.string.backstage_configuration_error));
                            }
                            return;
                        }
                    } else {
                        HandleRetCode.handler(activity, res);
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                        return;
                    }
                }
//                mView.onLoadingStatus(CodeStatus.PayCode.PAY_ABNORMAL);
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }

            @Override
            public void onFailure(Call<ResponseData<PaySignModel>> call, Throwable t) {
                super.onFailure(call, t);
//                mView.onLoadingStatus(CodeStatus.PayCode.PAY_ABNORMAL);
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, "");
            }
        });
    }

    /**
     * 微信支付
     */
    public void wxPay(PaySignModel.Sign sign) {
        //TODO 微信支付
//        if (activity != null) {
//            IWXAPI iwxapi = WXAPIFactory.createWXAPI(activity, Constants.WeiXin.APP_ID);
//
//            iwxapi = WXAPIFactory.createWXAPI(activity, null);
//            iwxapi.registerApp(Constants.WeiXin.APP_ID);
//
//            PayReq req = new PayReq();
//            req.appId = sign.appid;
//            req.partnerId = sign.partnerid;
//            req.prepayId = sign.prepayid;
//            req.nonceStr = sign.noncestr;
//            req.timeStamp = sign.timestamp;
//            req.packageValue = "Sign=WXPay";
//            req.sign = sign.sign;
//            iwxapi.sendReq(req);
//        }
    }

    /**
     * 支付宝
     *
     * @param order 支付宝订单数据
     */
    public void aliPay(final String order) {
        if (activity != null && !activity.isFinishing()) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
//                    PayTask alipay = new PayTask(activity);
//                    String result = alipay.pay(order, false);
//                    subscriber.onNext(result);
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "onCompleted");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError");
                        }

                        @Override
                        public void onNext(String s) {
                            Log.d(TAG, "onNext");
                            AliPayHandler handler = new AliPayHandler(PayPresenter.this);
                            Message msg = new Message();
                            msg.what = SDK_ALIAY;
                            msg.obj = s;
                            handler.sendMessage(msg);
                        }
                    });
        }
    }

    /**
     * 查询余额
     */
    public void getUserAccount(String custId, String loading) {
        if (!NetUtils.isNetworkConnected(activity)) {
            Toasty.info(activity, activity.getString(R.string.status_un_network)).show();
//            mView.onLoadingStatus(CommonCode.General.ERROR_NET, activity.getString(R.string.no_net_work));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, loading);
//        Call<ResponseData<AccountModel>> call = mUserService.getUserAccount(1050, custId);
        Call<ResponseData<AccountBean>> call = mUserService.getUserAccount(1050, custId);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<AccountBean>>() {
            @Override
            public void onResponse(Call<ResponseData<AccountBean>> call, Response<ResponseData<AccountBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<AccountBean> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        AccountBean model = res.getData();
                        if (null != model) {
                            if (Type.USER_MONEY_NORMAL == model.getAccountState()) {
                                Session.setUserMoneyState(true);
                            } else {
                                Session.setUserMoneyState(false);
                            }
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                            mView.updateView(model);
                            return;
                        }
                    } else {
                        if (HandleRetCode.handler(activity, res)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
                                }
                            }, 2000);
                            return;
                        }
                    }
                }
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }

            @Override
            public void onFailure(Call<ResponseData<AccountBean>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }
        });
    }

    /**
     * 获取账户信息-是否设置支付密码和是否开启免密支付
     *
     * @param flag 是否弹出加载框
     */
    public void isSettingPw(final boolean flag) {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        if (flag) {
            //验证中请稍后...
            mView.onLoadingStatus(CommonCode.General.DATA_LOADING, activity.getString(R.string.check_ing));
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
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                            mView.updateView(users);
                            return;
                        }
                    } else {
                        if (HandleRetCode.handler(activity, res)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                                    return;
                                }
                            }, 2000);
                        }
                    }
                }
                if (flag) {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.check_fail));
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<UserInfoModel>> call, Throwable t) {
                super.onFailure(call, t);
                if (flag) {
                    mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, activity.getString(R.string.check_fail));
                } else {
                    mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
                }
            }
        });
    }

    /**
     * 获取消费或者收益明细
     *
     * @param loadmore true 加载更多 false 刷新
     */
    public void requestGetBillList(final boolean loadmore, int type) {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        if (isDataError) {
            start = record_start;
        } else {
            if (!loadmore) {
                start = 0;
            } else {
                start += Constants.PAGESIZE;
            }
            record_start = start;
        }
        Call<ResponseData<List<BillDetailModel>>> call = mUserService
                .getBillList(Session.getUserId(), "", type, start, Constants.PAGESIZE);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<BillDetailModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<BillDetailModel>>> call, Response<ResponseData<List<BillDetailModel>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<List<BillDetailModel>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<BillDetailModel> dataList = res.getData();
                        if (null != dataList && !dataList.isEmpty()) {
                            isDataError = false;
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            mView.updateViewWithLoadMore(dataList, loadmore);
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            isDataError = true;
                        }
                        return;
                    } else {
                        if (HandleRetCode.handler(activity, res)) {
                            mView.onLoadingStatus(CommonCode.General.ERROR_DATA, "");
                            isDataError = true;
                            return;
                        }
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.load_fail));
                isDataError = true;
            }

            @Override
            public void onFailure(Call<ResponseData<List<BillDetailModel>>> call, Throwable t) {
                super.onFailure(call, t);
                Log.i(TAG, "onFailure: "+t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.load_fail));
                isDataError = true;
            }
        });
    }

    /**
     * 获取积分明细
     *
     * @param loadmore true 加载更多 false 刷新
     */
    public void requestGetScoreList(final boolean loadmore) {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        if (isDataError) {
            start = record_start;
        } else {
            if (!loadmore) {
                start = 0;
            } else {
                start += Constants.PAGESIZE;
            }
            record_start = start;
        }
        Call<ResponseData<List<ScoreBean>>> call = mUserService
                .getScoreList(Session.getUserId(), start, Constants.PAGESIZE);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<ScoreBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<ScoreBean>>> call, Response<ResponseData<List<ScoreBean>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<List<ScoreBean>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<ScoreBean> dataList = res.getData();
                        if (null != dataList && !dataList.isEmpty()) {
                            isDataError = false;
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            mView.updateViewWithLoadMore(dataList, loadmore);
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            isDataError = true;
                        }
                        return;
                    } else {
                        if (HandleRetCode.handler(activity, res)) {
                            mView.onLoadingStatus(CommonCode.General.ERROR_DATA, "");
                            isDataError = true;
                            return;
                        }
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.load_fail));
                isDataError = true;
            }

            @Override
            public void onFailure(Call<ResponseData<List<ScoreBean>>> call, Throwable t) {
                super.onFailure(call, t);
                Log.i(TAG, "onFailure: "+t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.load_fail));
                isDataError = true;
            }
        });
    }

    /**
     * 获取打赏明细
     *
     * @param loadmore true 加载更多 false 刷新
     */
    public void requestGetRewardList(final boolean loadmore) {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        if (isDataError) {
            start = record_start;
        } else {
            if (!loadmore) {
                start = 0;
            } else {
                start += Constants.PAGESIZE;
            }
            record_start = start;
        }
        Call<ResponseData<List<RewardDetailBean>>> call = mUserService
                .getMineRewardList(Session.getUserId(), Constants.PAGESIZE, "");
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<RewardDetailBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<RewardDetailBean>>> call, Response<ResponseData<List<RewardDetailBean>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<List<RewardDetailBean>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<RewardDetailBean> dataList = res.getData();
                        if (null != dataList && !dataList.isEmpty()) {
                            isDataError = false;
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                            mView.updateViewWithLoadMore(dataList, loadmore);
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                            isDataError = true;
                        }
                        return;
                    } else {
                        if (HandleRetCode.handler(activity, res)) {
                            mView.onLoadingStatus(CommonCode.General.ERROR_DATA, "");
                            isDataError = true;
                            return;
                        }
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.load_fail));
                isDataError = true;
            }

            @Override
            public void onFailure(Call<ResponseData<List<RewardDetailBean>>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.load_fail));
                isDataError = true;
            }
        });
    }



    /**
     * 收益消费到账户
     *
     * @param password 支付密码
     * @param cost     兑换金额
     */
    public void pointsToAccount(String password, String cost) {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, activity.getString(R.string.no_net_work));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, activity.getString(R.string.check_ing));
        Call<ResponseData> call = mUserService
                .pointsToAccount(1056, password, Session.getUserId(), cost);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    final ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        //收益兑换成功
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                        mView.updateView(res.getRet());
                        return;
                    } else {
//                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, "error");
//                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, "");
//                        return;
                        if (HandleRetCode.handler(activity, res)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.updateView(res.getRet());
                                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.action_fail));
                                }
                            }, 2000);
                            return;
                        }
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.action_fail));
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.action_fail));
            }
        });
    }

    public void payOrder(String orderId) {
        payOrder(orderId, "");
    }

    public void payOrder(String orderId, String pwd) {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        Call<ResponseData<HashMap<String, String>>> call = mUserService.payOrder(Session.getUserId(), orderId, TextUtils.isEmpty(pwd) ? "" : HexUtil.encodeHexStr(MD5Util.md5(pwd)));
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<HashMap<String, String>>>() {
            @Override
            public void onResponse(Call<ResponseData<HashMap<String, String>>> call, Response<ResponseData<HashMap<String, String>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.isSuccessful()) {
                        if (res.getData() != null) {
                            HashMap<String, String> data = response.body().getData();
                            if (data.containsKey("code") && TextUtils.equals(data.get("code"), "" + 1000)) {
                                mView.updateView(1000);
                            } else {
                                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                            }
                        } else {
                            mView.onLoadingStatus(CommonCode.General.PAY_SUCCESS);
                            mView.updateView(res.getRet());
                        }
                    } else {
                        HandleRetCode.handler(activity, res);
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<HashMap<String, String>>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
            }
        });
    }

    public void payOrderDetails(String orderId) {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        Call<ResponseData<PayOrderInfoBean>> call = mUserService.payOrderDetails(orderId);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<PayOrderInfoBean>>() {
            @Override
            public void onResponse(Call<ResponseData<PayOrderInfoBean>> call, Response<ResponseData<PayOrderInfoBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.isSuccessful()) {
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        mView.updateView(response.body().getData());
                    } else {
                        HandleRetCode.handler(activity, res);
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<PayOrderInfoBean>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, "");
            }
        });
    }

    public void payRewardDetails(String orderId) {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        Call<ResponseData<PayOrderInfoBean>> call = mUserService.payRewardDetails(orderId);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<PayOrderInfoBean>>() {
            @Override
            public void onResponse(Call<ResponseData<PayOrderInfoBean>> call, Response<ResponseData<PayOrderInfoBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.isSuccessful()) {
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        mView.updateView(response.body().getData());
                    } else {
                        HandleRetCode.handler(activity, res);
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<PayOrderInfoBean>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, "");
            }
        });
    }

    /**
     * 检测是否开启免密支付 ,单位为分
     *
     * @param mPayMoney  支付金额
     * @param mUserMoney 账户余额
     * @param desc       描述
     * @param flag       0做免密支付 显示元 1做免密支付 不显示元 2不做免密支付 显示元 3不做免密支付 不显示元
     */
    public void checkIsOpenEasyPay(double mPayMoney, double mUserMoney, String desc, int flag) {
        if (Session.getUserIsLogin()) {
            if (Session.getUserMoneyState()) {
                if (mPayMoney <= mUserMoney) {
                    if (Session.getUserSetpaypw()) {
                        //设置了支付密码
                        if (flag == 2 || flag == 3) {
                            showPayDialog(mPayMoney, desc, flag);
                        } else {
                            if (Session.getIsOpenGesture()) {
                                //开启免密支付
                                if (mPayMoney <= Constants.EasyPayMoney) {
                                    //--去支付
                                    mView.updateView("");
                                } else {
                                    //支付金额大于免密支付额
                                    showPayDialog(mPayMoney, desc, flag);
                                }
                            } else {
                                showPayDialog(mPayMoney, desc, flag);
                            }
                        }
                    } else {
                        //未设置，去设置
                        View mSetPayPw = LayoutInflater.from(activity).inflate(R.layout.dialog_to_set_pay_passw, null);
                        final Dialog mSetDialog = DialogUtils.selfDialog(activity, mSetPayPw, true);
                        mSetPayPw.findViewById(R.id.id_set_pay_pw_txt).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mSetDialog.dismiss();
                                Intent intent = new Intent(activity, SetPayPassAty.class);
                                intent.putExtra(IntentKey.KEY_TYPE, Type.HAD_NO_SET_PW);
                                activity.startActivity(intent);
                            }
                        });
                        mSetDialog.show();
                    }
                } else {
                    SVProgressHUD.showInfoWithStatus(activity, activity.getString(R.string.money_less));
                }
            } else {
                SVProgressHUD.showInfoWithStatus(activity, activity.getString(R.string.user_money_freeze));
            }
        } else {
            SVProgressHUD.showInfoWithStatus(activity, activity.getString(R.string.please_go_login));
        }
    }

    /**
     * 显示支付弹出框
     */
    public void showPayDialog(final double mPayMoney, String desc, int flag) {
        showPayDialog(mPayMoney, desc, "", flag);
    }

    public void showPayDialog(final double mPayMoney, String desc, final String orderId, final int flag) {
        View payViwe = LayoutInflater.from(activity).inflate(R.layout.dialog_pay, null);
        if (Session.getUserSafetyproblem()) {
            payViwe.findViewById(R.id.id_is_set_user_txt).setVisibility(View.GONE);
        } else {
            payViwe.findViewById(R.id.id_is_set_user_txt).setVisibility(View.VISIBLE);
        }
        final GridPasswordView payPass = (GridPasswordView) payViwe.findViewById(R.id.id_pay_pw_pass);

        final Dialog mPayDialog = DialogUtils.selfDialog(activity, payViwe, false);
        mPayDialog.setCancelable(true);
        mPayDialog.setCanceledOnTouchOutside(true);
        mPayDialog.show();
        mPayDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (payPass.getPassWord().length() != payPass.getmPasswordLength()) {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.pay_cancel));
                }
            }
        });
        payViwe.findViewById(R.id.id_pay_close_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPayDialog.dismiss();
            }
        });
        //描述
        if (!TextUtils.isEmpty(desc))
            ((TextView) payViwe.findViewById(R.id.id_pay_des_txt)).setText(desc);
        if (flag == 2 || flag == 0) {
            payViwe.findViewById(R.id.id_dialog_pay_yuan_txt).setVisibility(View.VISIBLE);
        } else {
            payViwe.findViewById(R.id.id_dialog_pay_yuan_txt).setVisibility(View.GONE);
        }
        ((TextView) payViwe.findViewById(R.id.id_pay_money_txt)).setText(Currency.returnDollar(Currency.RMB, mPayMoney + "", 0).replace("元", ""));
        //支付密码
        payPass.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
            }

            @Override
            public void onInputFinish(String psw) {
                hideInputMethod();
                mPayDialog.dismiss();
                //去支付
                if (flag==3){
                    mView.updateView(psw);
                }else{
                payOrder(orderId, psw);
                }
            }
        });
    }


    /**
     * 检测用户是否设置了支付密码
     */
    public void checkIsSetPw() {
        if (Session.getUserSetpaypw()) {
            checkPayPw();
        } else {
            //未设置，去设置
            View mSetPayPw = LayoutInflater.from(activity).inflate(R.layout.dialog_to_set_pay_passw, null);
            final Dialog mSetDialog = DialogUtils.selfDialog(activity, mSetPayPw, true);
            mSetDialog.setCancelable(true);
            mSetDialog.setCanceledOnTouchOutside(true);
            mSetPayPw.findViewById(R.id.id_set_pay_pw_txt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSetDialog.dismiss();
                    Intent intent = new Intent(activity, SetPayPassAty.class);
                    intent.putExtra(IntentKey.KEY_TYPE, Type.HAD_NO_SET_PW);
                    activity.startActivity(intent);
                }
            });
            mSetDialog.show();
        }
    }

    /**
     * 弹出验证支付密码弹出框
     */
    public void checkPayPw() {
        View payViwe = LayoutInflater.from(activity).inflate(R.layout.window_check_pwd, null);
        final Dialog mPayDialog = DialogUtils.selfDialog(activity, payViwe, false);
        mPayDialog.setCancelable(true);
        mPayDialog.setCanceledOnTouchOutside(true);
        mPayDialog.show();
        payViwe.findViewById(R.id.id_tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPayDialog.dismiss();
                mView.updateView("");
                mCheckPayPw = "";
            }
        });
        payViwe.findViewById(R.id.id_tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isEmpty(mCheckPayPw)) {
//                    SVProgressHUD.showInfoWithStatus(activity, "请输入支付密码");
                    Toasty.error(activity, "请输入支付密码").show();
                } else {
                    mPayDialog.dismiss();
                    mView.updateView(mCheckPayPw);
                    mCheckPayPw = "";
                }
            }
        });
        GridPasswordView payPass = (GridPasswordView) payViwe.findViewById(R.id.id_check_pay_pw);
        //支付密码
        payPass.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
            }

            @Override
            public void onInputFinish(String psw) {
                mCheckPayPw = psw;
                hideInputMethod();
            }
        });
    }

    public void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getApplicationWindowToken(), 0);
    }

    /**
     * 弹出重置支付弹出框
     */
    public void showResetDialog() {
        View mResetView = LayoutInflater.from(activity).inflate(R.layout.dialog_zhuanfa, null);
        final Dialog mResetDialog = DialogUtils.selfDialog(activity, mResetView, false);
        mResetDialog.setCancelable(true);
        mResetDialog.setCanceledOnTouchOutside(true);
        ((TextView) mResetView.findViewById(R.id.id_tv_message)).setText(R.string.again_pay_miss);
        TextView mTxtForgetPw = (TextView) mResetView.findViewById(R.id.id_tv_confirm);
        mTxtForgetPw.setText(R.string.forget_password);
        TextView mSureTxt = (TextView) mResetView.findViewById(R.id.id_tv_cancel);
        mSureTxt.setText(R.string.define);
        mSureTxt.setTextColor(activity.getResources().getColor(R.color.txt_pop_detail));
        //忘记密码
        mTxtForgetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mResetDialog.dismiss();
//                Intent intent = new Intent(activity, ResetPayPwAty.class);
//                activity.startActivity(intent);
            }
        });
        //确定
        mSureTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mResetDialog.dismiss();
                isSettingPw(true);
            }
        });
        mResetDialog.show();
    }

    public void pay(String orderId, double mPayMoney) {
        pay(orderId, mPayMoney, mPayMoney, 0);
    }

    public void pay(String orderId, double mPayMoney, double mUserMoney, int flag) {
        pay(orderId, mPayMoney, mUserMoney, "", flag);
    }

    public void pay(final String orderId, final double mPayMoney, final double mUserMoney, final String desc, final int flag) {
        if (!NetUtils.isNetworkConnected(activity)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        //验证中请稍后...
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, activity.getString(R.string.check_ing));
        Call<ResponseData<UserInfoModel>> call = mUserService.searchUserNews(1054, Session.getUserId());
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<UserInfoModel>>() {
            @Override
            public void onResponse(Call<ResponseData<UserInfoModel>> call, Response<ResponseData<UserInfoModel>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<UserInfoModel> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        UserInfoModel user = res.getData();
                        if (null != user) {
                            if (Type.HAD_SET_PW == user.getIsPayPassword()) {
                                Session.setUserSetpaypw(true);
                            } else {
                                Session.setUserSetpaypw(false);
                            }
                            if (Type.OPEN_EASY_PAY == user.getSmallNopass()) {
                                Session.setIsOpenGesture(true);
                            } else {
                                Session.setIsOpenGesture(false);
                            }

                            if (!Session.getUserIsLogin()) {
                                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.please_go_login));
                                return;
                            }
                            if (!Session.getUserMoneyState()) {
                                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.user_money_freeze));
                                return;
                            }
                            if (mPayMoney > mUserMoney) {
                                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.money_less));
                                return;
                            }
                            if (Session.getUserSetpaypw()) {
                                //设置了支付密码
                                if (flag == 2 || flag == 3) {
                                    showPayDialog(mPayMoney, desc, orderId, flag);
                                } else {
                                    if (Session.getIsOpenGesture()) {
                                        //开启免密支付
                                        if (mPayMoney <= Constants.EasyPayMoney) {
                                            //去支付
                                            payOrder(orderId);
                                        } else {
                                            //支付金额大于免密支付额
                                            showPayDialog(mPayMoney, desc, orderId, flag);
                                        }
                                    } else {
                                        showPayDialog(mPayMoney, desc, orderId, flag);
                                    }
                                }
                            } else {
                                final boolean[] toSetPayPwd = {false};
                                //未设置，去设置
                                View mSetPayPw = LayoutInflater.from(activity).inflate(R.layout.dialog_to_set_pay_passw, null);
                                final Dialog mSetDialog = DialogUtils.selfDialog(activity, mSetPayPw, true);
                                mSetPayPw.findViewById(R.id.id_set_pay_pw_txt).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(activity, SetPayPassAty.class);
                                        intent.putExtra(IntentKey.KEY_TYPE, Type.HAD_NO_SET_PW);
                                        activity.startActivity(intent);
                                        toSetPayPwd[0] = true;
                                        mSetDialog.dismiss();
                                        activity.finish();
                                    }
                                });
                                mSetDialog.show();
                                mSetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        if (!toSetPayPwd[0])
                                            mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                                    }
                                });
                            }
                        }
                    } else {
                        if (HandleRetCode.handler(activity, res)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                                    return;
                                }
                            }, 2000);
                        }
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.check_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<UserInfoModel>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, activity.getString(R.string.check_fail));
            }
        });
    }

    /**
     * 支付宝支付回调
     */
    private class AliPayHandler extends Handler {

        private WeakReference<PayPresenter> weakref;

        public AliPayHandler(PayPresenter presenter) {
            this.weakref = new WeakReference<>(presenter);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_ALIAY: {
                    String str = (String) msg.obj;
                    PayResult payResult = new PayResult(str);
                    String result = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, ALIPAY_STATE_SUCCESS)) {
                        //TODO 支付成功
                        mView.onLoadingStatus(CommonCode.PayCode.PAY_SUCCESS);
                    } else {
                        if (TextUtils.equals(resultStatus, ALIPAY_STATE_CONFIRMING)) {
                            //TODO 支付结果确认中
                            mView.onLoadingStatus(CommonCode.PayCode.PAY_CONFIRM);
                        } else if (TextUtils.equals(resultStatus, ALIPAY_STATE_CANCEL)) {
                            //TODO 用户支付取消
                            mView.onLoadingStatus(CommonCode.PayCode.PAY_CANDEL);
                        } else {
                            //TODO 支付失败
                            mView.onLoadingStatus(CommonCode.PayCode.PAY_FAIL);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }
}
