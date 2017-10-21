package com.rz.circled.ui.fragment;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.circled.application.QHApplication;
import com.rz.circled.js.RequestJsBroadcastHandler;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.H5Address;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.rz.sgt.jsbridge.RegisterList;
import com.rz.sgt.jsbridge.core.AdvancedWebView;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by Gsm on 2017/8/29.
 */
public class RewardFragment extends BaseFragment implements AdvancedWebView.Listener {

    @BindView(R.id.ll_reward_root)
    LinearLayout llRoot;

    private WebViewProxy mWebViewProxy;
    private AdvancedWebView mWebView;

    private boolean finishloading = false;

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_reward, null);
    }

    @Override
    public void initView() {
        mWebView = QHApplication.getInstance().getWebView(false);
        ViewParent viewParent = mWebView.getParent();
        if (viewParent == null) {
            addWebView();
        } else if (viewParent instanceof ViewGroup) {
            ((ViewGroup) viewParent).removeAllViews();
            addWebView();
        }
        setRefreshListener(this);
    }

    //添加webview
    private void addWebView() {
        ViewGroup.LayoutParams layoutParams = mWebView.getLayoutParams();
        if (layoutParams == null)
            layoutParams = new ViewGroup.LayoutParams(-1, -1);
        else {
            layoutParams.width = -1;
            layoutParams.height = -1;
        }
        mWebView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
        llRoot.addView(mWebView, layoutParams);
        Object tag = mWebView.getTag();
        if (tag != null && tag instanceof WebViewProxy)
            mWebViewProxy = (WebViewProxy) tag;
        mWebViewProxy.registerAll(RegisterList.getAllRegisterHandler(getActivity()));
        mWebView.setListener(mActivity, this);
        toReload();
    }


    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        mWebView.resumeTimers();
        if (mWebViewProxy != null)
            mWebViewProxy.autoCancelUiHandler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent == null) return;
        switch (baseEvent.type) {
            case CommonCode.EventType.FINISH_LOADING:
                if (mWebView != null)
                    mWebView.setVisibility(View.VISIBLE);
                onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                finishloading = true;
                break;
            case CommonCode.EventType.TYPE_REWARD_REFRESH:
                //刷新
                RequestJsBroadcastHandler refreshHandler = new RequestJsBroadcastHandler(mActivity);
                refreshHandler.setNativeEvent(baseEvent.key);
                refreshHandler.setRequestData(baseEvent.data);
                if (mWebViewProxy != null)
                    mWebViewProxy.requestJsFun(refreshHandler);
                break;
            case CommonCode.EventType.TYPE_LOGIN_WEB:
                //登录
                RequestJsBroadcastHandler loginHandler = new RequestJsBroadcastHandler(mActivity);
                loginHandler.setNativeEvent(baseEvent.key);
                loginHandler.setRequestData(baseEvent.data);
                if (mWebViewProxy != null)
                    mWebViewProxy.requestJsFun(loginHandler);
                toReload();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(JsEvent jsEvent) {
        Log.d("webView", "onevent");
        if (jsEvent != null) {
            Log.d("jsbridge", "onevent " + jsEvent.invokeId);
            if (jsEvent.invokeId == 0) {

            } else if (jsEvent.invokeId == -1) {
                Callback callback = mWebViewProxy.getCurrentHandingUiHandlerCallback();
                if (callback != null) {
                    mWebViewProxy.changeUIStatus(callback.invokeId);
                    mWebViewProxy.removeCallbackFromInvokeId(callback.invokeId);
                    BaseParamsObject baseParamsObject = callback.initBaseParamsObject();
                    baseParamsObject.resultCode = jsEvent.resultCode;
                    callback.invoke(jsEvent.object, baseParamsObject);
                    ParamsObject paramsObject = new ParamsObject(baseParamsObject);
                    mWebViewProxy.callbackInvoke(new Gson().toJson(paramsObject));
                }
            } else {
                Callback callback = mWebViewProxy.getCallBackByInvokeId(jsEvent.invokeId);
                if (callback != null) {
                    mWebViewProxy.changeUIStatus(jsEvent.invokeId);
                    mWebViewProxy.removeCallbackFromInvokeId(jsEvent.invokeId);
                    BaseParamsObject baseParamsObject = callback.initBaseParamsObject();
                    baseParamsObject.resultCode = jsEvent.resultCode;
                    callback.invoke(jsEvent.object, baseParamsObject);
                    ParamsObject paramsObject = new ParamsObject(baseParamsObject);
                    mWebViewProxy.callbackInvoke(new Gson().toJson(paramsObject));
                }
            }
        }
    }

    private void toReload() {
        if (mWebViewProxy != null) {
            mWebViewProxy.removeRepetLoadUrl(BuildConfig.WebHomeBaseUrl + H5Address.REWARD_LIST_URL);
            onLoadingStatus(CommonCode.General.DATA_LOADING);
            finishloading = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (finishloading) return;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onLoadingStatus(CommonCode.General.WEB_ERROR);
                        }
                    });
                }
            }, 20 * 1000);
        }
    }

    @Override
    public void refreshPage() {
        toReload();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        onLoadingStatus(CommonCode.General.WEB_ERROR);
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    @Override
    public void callPhoneNumber(String uri) {

    }

    @Override
    public void doUpdateVisitedHistory() {

    }

}
