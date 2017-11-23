package com.rz.circled.ui.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.circled.application.QHApplication;
import com.rz.circled.js.RequestJsBroadcastHandler;
import com.rz.circled.js.model.HeaderModel;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.IntentUtil;
import com.rz.common.utils.SystemUtils;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;

/**
 * Created by Gsm on 2017/8/29.
 */
public class RewardFragment extends BaseFragment implements AdvancedWebView.Listener {

    @BindView(R.id.webView)
    AdvancedWebView mWebView;

    private WebViewProxy mWebViewProxy;

    private boolean hadHeader = false;

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

        mWebViewProxy = new WebViewProxy(mWebView);
        mWebViewProxy.registerAll(RegisterList.getAllRegisterHandler(getActivity()));

        onLoadingStatus(CommonCode.General.DATA_LOADING);
        mWebViewProxy.removeRepetLoadUrl(BuildConfig.WebHomeBaseUrl + "/activity/reward");
//        mWebViewProxy.removeRepetLoadUrl("file:///android_asset/test.html");

        mWebView.setListener(getActivity(), this);
        setRefreshListener(this);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 90 && hadHeader) {
                    onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                }
            }
        });
    }

    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebViewProxy.autoCancelUiHandler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent == null) return;
        switch (baseEvent.type) {
            case CommonCode.EventType.FINISH_LOADING:
                if (mWebView != null)
                    mWebView.setVisibility(View.VISIBLE);
                onLoadingStatus(CommonCode.General.DATA_SUCCESS);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBaseEvent(BaseEvent baseEvent) {
        if (baseEvent == null) return;
        switch (baseEvent.type) {
            case CommonCode.EventType.TYPE_REWARD_REFRESH:
                //刷新
                RequestJsBroadcastHandler refreshHandler = new RequestJsBroadcastHandler(mActivity);
                refreshHandler.setNativeEvent(baseEvent.key);
                refreshHandler.setRequestData(baseEvent.data);
                if (mWebViewProxy != null)
                    mWebViewProxy.requestJsFun(refreshHandler);
                break;
            case CommonCode.EventType.TYPE_HTTP_HEADER:
                hadHeader = true;
                break;

            case CommonCode.EventType.TYPE_BACKLOGIN_REFRESH:
                mWebView.reload();
                break;

        }
    }

    @Override
    public void refreshPage() {
        onLoadingStatus(CommonCode.General.DATA_LOADING);
        mWebView.reload();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        onLoadingStatus(CommonCode.General.ERROR_DATA);
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition,
                                    String userAgent) {

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

    @Override
    protected void onVisible() {
        super.onVisible();
        RequestJsBroadcastHandler requestJsBroadcastHandler = new RequestJsBroadcastHandler(getActivity());
        requestJsBroadcastHandler.setNativeEvent("nativeActivated");
        requestJsBroadcastHandler.setRequestData(getLoginWebResultData());
        mWebViewProxy.requestJsFun(requestJsBroadcastHandler);
    }

    private HeaderModel getLoginWebResultData() {
        HeaderModel headerModel = new HeaderModel();
        headerModel.sign = "sign";
        headerModel.token = Session.getSessionKey();
        headerModel.devType = "2";
        try {
            headerModel.devName = URLEncoder.encode(Build.MODEL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headerModel.appVersion = BuildConfig.VERSION_NAME;
        headerModel.devId = SystemUtils.getIMEI(QHApplication.getContext());
        headerModel.ip = SystemUtils.getIp(QHApplication.getContext());
        headerModel.net = IntentUtil.getNetType(QHApplication.getContext());
        headerModel.custId = Session.getUserId();
        headerModel.phone = Session.getUserPhone();
        headerModel.cityCode = Session.getCityCode();
        return headerModel;
    }
}
