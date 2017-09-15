package com.rz.circled.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.rz.circled.R;
import com.rz.circled.http.HandleRetCode;
import com.rz.circled.modle.SnsLoginModel;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Type;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.NetUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.CallManager;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.UserInfoBean;
import com.rz.httpapi.constans.ReturnCode;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

import static com.umeng.socialize.bean.SHARE_MEDIA.QQ;


/**
 * 处理登录逻辑操作
 */
public class SnsAuthPresenter extends GeneralPresenter {

    private ApiService mUserService;
    private IViewController mView;

    private Context mContext;
    //是否是绑定 0是代表绑定 -1是代表第三方登录 1代表解绑
    private int actionBind = -1;
    //第三方的accesstoken
    private String accessToken;

    private String openId;

    private UMShareAPI mShareAPI;

    //记录解绑时的第三方的openid
    private String mSaveOpenId;


    private boolean isLogin = false;

    public void setActionBind(int actionBind) {
        this.actionBind = actionBind;
    }

    @Override
    public void attachView(IViewController view) {
        this.mView = view;
        mContext = getContext(mView);
        mShareAPI = UMShareAPI.get(mContext);
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

    /**
     * 登录请求
     */
    public void loginRequest(String phone, String pw) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.is_loading));

        Call<ResponseData<UserInfoBean>> call = mUserService.login(1003, phone, pw);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<UserInfoBean>>() {
            @Override
            public void onResponse(Call<ResponseData<UserInfoBean>> call, Response<ResponseData<UserInfoBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    final ResponseData<UserInfoBean> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        UserInfoBean user = res.getData();
                        if (null != user) {
                            //登录成功
                            Session.setLoginWay(Type.LOGIN_PHONE);
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.login_success));
                            mView.updateView(user);
                         /*   Set<String> sset = new HashSet<String>();
                            sset.add("testTag");
                            JPushInterface.setAliasAndTags(mContext.getApplicationContext(), Session.getUserId(), sset, new TagAliasCallback() {
                                @Override
                                public void gotResult(int arg0, String arg1, Set<String> arg2) {
                                    // TODO Auto-generated method stub
                                    Log.i("setAlias", "success");
                                }
                            });*/
                            return;
                        }
                    } else {
//                        if (HandleRetCode.handler(mContext, res)) {
                            //登录失败
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.login_fail));
                                    Toasty.info(mContext,res.getMsg()).show();
                                }
                            }, 2000);
                            return;
//                        }

                    }
                }
                //登录失败
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.login_fail));
            }

            @Override
            public void onFailure(Call<ResponseData<UserInfoBean>> call, Throwable t) {
                super.onFailure(call, t);
                //登录失败
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.login_fail));
            }
        });
    }

    /**
     * 解绑、绑定第三方账号
     *
     * @param type        1，微信 2，微博 3，qq
     * @param openId
     * @param action      0，绑定 1，解绑
     * @param accessToken
     */
    public void bindThirdAccout(final int type, final String openId, final int action, String accessToken) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.data_loading));
        Call<ResponseData> call = mUserService.bindThirdAccount(
                1012,
                Session.getUserId(),
                type,
                openId,
                action,
                accessToken);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        //绑定或者解绑
                        if (action == Type.ACTION_BIND) {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.bind_success));
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.unbind_card_success));
                        }
                        mView.updateViewWithLoadMore(type, true);
                        return;
                    } else {
                        if (HandleRetCode.handler(mContext, res)) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (action == Type.ACTION_BIND) {
                                        if (type == Type.LOGIN_WX) {
                                            delWXAuth(openId);
                                        } else if (type == Type.LOGIN_SINA) {
                                            delWBAuth(openId);
                                        } else if (type == Type.LOGIN_QQ) {
                                            delQQAuth(openId);
                                        }
                                    }
                                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, "");
                                }
                            }, 2000);
                            return;
                        }
                    }
                }
                //绑定或者解绑
                if (action == Type.ACTION_BIND) {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.bind_fail));
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.unbind_card_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                //绑定或者解绑
                if (action == Type.ACTION_BIND) {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.bind_fail));
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.unbind_card_fail));
                }
            }
        });
    }

     /**
     * 第三方登录请求
     */
    public void otherLogin(String openId, String accessToken, final int type) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            public void run() {
                mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.login_loading));
            }
        });
        Call<ResponseData<UserInfoBean>> call = mUserService.otherLogin(
                1004,
                openId,
                accessToken,
                type,
               "s"
//                BuildConfig.serverChannel
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
                            //登录成功
                            Session.setLoginWay(type);
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.login_success));
                            mView.updateView(user);
                            return;
                        }
                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        //登录失败
                        delAuthOther(type, res.getMsg());
                        return;
                    }
                }
                //登录失败
                delAuthOther(type, mContext.getString(R.string.login_fail));
            }

            @Override
            public void onFailure(Call<ResponseData<UserInfoBean>> call, Throwable t) {
                super.onFailure(call, t);
                //登录失败
                delAuthOther(type, mContext.getString(R.string.login_fail));
            }
        });
    }

    //删除第三方授权
    public void delAuthOther(int type, String error) {
        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, error);
//        if (type == Type.LOGIN_QQ) {
//            delQQAuth();
//        } else if (type == Type.LOGIN_WX) {
//            delWXAuth();
//        } else if (type == Type.LOGIN_SINA) {
//            delWBAuth();
//        }
    }


    private boolean isAuthAfterDel = false;

    private UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//            ToastUtil.showToast("授权成功");
            for (Map.Entry<String, String> entry : map.entrySet()) {
            }
            if (map.containsKey("refresh_token")) {
                Session.setSinaRefreshAccessToken(map.get("refresh_token"));
            }

            accessToken = map.get("access_token");
            mShareAPI.getPlatformInfo(((Activity) mContext), share_media, getInfoListener);
//            String openId = map.get("openid");
//            String accessToken = map.get("access_token");
//            int type = -1;
//            switch (share_media) {
//                case QQ: {
//                    type = Type.LOGIN_QQ;
//                    break;
//                }
//                case WEIXIN: {
//                    type = Type.LOGIN_WX;
//                    break;
//                }
//                case SINA: {
//                    type = Type.LOGIN_SINA;
//                    break;
//                }
//            }
//            if (actionBind == 1) {
//                bindThirdAccout(type, openId, Type.ACTION_BIND, accessToken);
//            } else {
//                otherLogin(openId, accessToken, type);
//            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//            SVProgressHUD.showInfoWithStatus(mContext, "授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
//            SVProgressHUD.showInfoWithStatus(mContext, "取消授权");
        }
    };

    private UMAuthListener delAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//            ToastUtil.showToast("删除授权成功");
            if (isAuthAfterDel) {
                checkAuth(share_media);
            }
            toBindThirdAcout(share_media);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//            SVProgressHUD.showInfoWithStatus(mContext, "删除授权失败");
            if (!TextUtils.isEmpty(throwable.getMessage()) && throwable.getMessage().contains("Argument error!")) {
                toBindThirdAcout(share_media);
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
//            SVProgressHUD.showInfoWithStatus(mContext, "删除授权取消");
        }

    };

    private void toBindThirdAcout(SHARE_MEDIA share_media) {
        int type = -1;
        switch (share_media) {
            case QQ: {
                type = Type.LOGIN_QQ;
                break;
            }
            case WEIXIN: {
                type = Type.LOGIN_WX;
                break;
            }
            case SINA: {
                type = Type.LOGIN_SINA;
                break;
            }
        }
        if (actionBind == 1) {
            bindThirdAccout(type, mSaveOpenId, actionBind, "");
        }
    }

    private UMAuthListener getInfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//            ToastUtil.showToast("读取用户信息成功");
            if (map != null) {
                SnsLoginModel model = null;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                }
                int type = -1;
                switch (share_media) {
                    case QQ: {
                        type = Type.LOGIN_QQ;
//                        openId = map.get("client_id");
                        openId = map.get("openid");
//                        model = getQQUserInfo(map);
                        break;
                    }
                    case WEIXIN: {
                        type = Type.LOGIN_WX;
                        openId = map.get("openid");
//                        model = getWXUserInfo(map);
                        break;
                    }
                    case SINA: {
                        type = Type.LOGIN_SINA;
                        openId = map.get("uid");
                        accessToken = map.get("access_token");
//                        model = getWBUserInfo(map);
                        break;
                    }
                }
//                if (type == Type.LOGIN_SINA) {
//                    if (null != model) {
//                        openId = model.openId;
//                    }
//                }
                if (isLogin) {
                    Session.setOpenId(openId);
                }
                isLogin = false;
                Session.setSinaAccessToken(accessToken);
                if (actionBind == 0) {
                    bindThirdAccout(type, openId, Type.ACTION_BIND, accessToken);
                } else if (actionBind == -1) {
                    if (!StringUtils.isEmpty(openId) && !StringUtils.isEmpty(accessToken)) {
                        otherLogin(openId, accessToken, type);
                    }
                }

//                switch (share_media) {
//                    case QQ: {
//                        model = getQQUserInfo(map);
//                        break;
//                    }
//                    case WEIXIN: {
//                        model = getWXUserInfo(map);
//                        break;
//                    }
//                    case SINA: {
//                        model = getWBUserInfo(map);
//                        break;
//                    }
//                }
//                if (model != null) {
//                    Log.d("WX", model.toString());
//                    if (TextUtils.isEmpty(model.userName)
//                            || TextUtils.isEmpty(model.openId)) {
//                        //如果在网站后台删除了授权
//                        //导致未获取到用户数据时需要重新授权
//                        delAuth(share_media, true);
//                    } else {
//                        //TODO 向后台提交数据
//                    }
//                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            if (!SVProgressHUD.isShowing(mContext)) {
//                SVProgressHUD.showInfoWithStatus(mContext, "读取用户信息失败");
            }
            //获取用户信息错误时需要删除授权并重新授权
            delAuth(share_media, true);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
//            SVProgressHUD.showInfoWithStatus(mContext, "读取用户信息取消");
        }
    };

//    /**
//     * 获取QQ用户信息
//     *
//     * @param map
//     * @return
//     */
//    private SnsLoginModel getQQUserInfo(Map<String, String> map) {
//        SnsLoginModel model = new SnsLoginModel();
//        model.platform = "qq";
//        model.openId = map.get("openid");
//        model.userName = map.get("screen_name");
//        model.userAvatar = map.get("profile_image_url");
//        model.accessToken = map.get("access_token");
//        return model;
//    }
//
//    /**
//     * 获取微信用户信息
//     *
//     * @param map
//     * @return
//     */
//    private SnsLoginModel getWXUserInfo(Map<String, String> map) {
//        SnsLoginModel model = new SnsLoginModel();
//        model.platform = "weixin";
//        model.accessToken = map.get("access_token");
//        model.openId = map.get("openid");
//        model.userName = map.get("nickname");
//        model.userAvatar = map.get("headimgurl");
//        return model;
//    }
//
//    /**
//     * 获取微博用户信息
//     *
//     * @param map
//     * @return
//     */
//    private SnsLoginModel getWBUserInfo(Map<String, String> map) {
//        SnsLoginModel model = null;
//        if (map.containsKey("result")) {
//            String value = map.get("result");
//            try {
//                JSONObject jsonObj = new JSONObject(value);
//                model = new SnsLoginModel();
//                model.platform = "weibo";
//                model.openId = jsonObj.optString("idstr");
//                model.userName = jsonObj.optString("custNname");
//                model.userAvatar = jsonObj.optString("avatar_hd");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return model;
//    }
//
    /**
     * QQ授权登录
     */
    public void qqAuth(boolean isLogin) {
        this.isLogin = isLogin;
//        SVProgressHUD.showWithStatus(mContext, mContext.getString(R.string.data_loading));
        SHARE_MEDIA platform = QQ;
        checkAuth(platform);
    }

    /**
     * 微信授权登录
     */
    public void wxAuth(boolean isLogin) {
        this.isLogin = isLogin;
//        SVProgressHUD.showWithStatus(mContext, mContext.getString(R.string.data_loading));
        SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
        boolean install = mShareAPI.isInstall((Activity) mContext, platform);
        if (install) {
            checkAuth(platform);
        } else {
            SVProgressHUD.showInfoWithStatus(mContext, mContext.getString(R.string.no_install_wx));
        }
    }

    /**
     * 微博授权登录
     */
    public void wbAuth(boolean isLogin) {
        this.isLogin = isLogin;
//        SVProgressHUD.showWithStatus(mContext, mContext.getString(R.string.data_loading));
        SHARE_MEDIA platform = SHARE_MEDIA.SINA;
        checkAuth(platform);
    }

    private void checkAuth(SHARE_MEDIA platform) {
//        SVProgressHUD.dismiss(mContext);
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        switch (platform) {
            case WEIXIN:
                mShareAPI.doOauthVerify((Activity) mContext, platform, authListener);
                break;
            case SINA:
                //未授权则先去授权
                if (!mShareAPI.isAuthorize((Activity) mContext, platform)) {
                    mShareAPI.doOauthVerify((Activity) mContext, platform, authListener);
                } else {
                    //已授权则获取用户数据
                    mShareAPI.getPlatformInfo((Activity) mContext, platform, getInfoListener);
                }
                break;
            case QQ:
                mShareAPI.doOauthVerify((Activity) mContext, platform, authListener);
                break;
        }

    }

    /**
     * 删除QQ授权
     */
    public void delQQAuth(String mQQOpenId) {
        this.mSaveOpenId = mQQOpenId;
        SHARE_MEDIA platform = QQ;
        delAuth(platform, false);
    }


    /**
     * 删除微信授权
     */
    public void delWXAuth(String mWXOpenId) {
        SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
        boolean install = mShareAPI.isInstall((Activity) mContext, platform);
        if (install) {
            this.mSaveOpenId = mWXOpenId;
            delAuth(platform, false);
        } else {
            SVProgressHUD.showInfoWithStatus(mContext, mContext.getString(R.string.no_install_wx));
        }
    }

    /**
     * 删除新浪微博授权
     */
    public void delWBAuth(String mSinaOpenId) {
        this.mSaveOpenId = mSinaOpenId;
        SHARE_MEDIA platform = SHARE_MEDIA.SINA;
//        delSinaToken();
        delAuth(platform, false);

    }

    private void delAuth(SHARE_MEDIA platform, boolean isReAuth) {
        isAuthAfterDel = isReAuth;
        mShareAPI.deleteOauth((Activity) mContext, platform, delAuthListener);
    }

}
