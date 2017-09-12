package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.rz.circled.R;
import com.rz.circled.js.RequestBackHandler;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.ui.view.BaseLoadView;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.rz.sgt.jsbridge.RegisterList;
import com.rz.sgt.jsbridge.core.AdvancedWebView;
import com.rz.sgt.jsbridge.core.AndroidBug5497Workaround;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class WebContainerActivity extends BaseActivity implements BaseLoadView.RefreshListener {

    private AdvancedWebView mWebView;
    private WebViewProxy mWebViewProxy;

    private boolean hadError = false;
    private WebChromeClient webChromeClient;

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, WebContainerActivity.class);
        intent.putExtra(IntentKey.EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_web_container, null);
    }


    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    protected boolean needSwipeBack() {
        return hadError;
    }

    @Override
    public void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        AndroidBug5497Workaround.assistActivity(this);

        if (!EventBus.getDefault().isRegistered(mContext))
            EventBus.getDefault().register(mContext);

        mWebView = (AdvancedWebView) findViewById(R.id.webview_container);
        mWebView.setListener(this, new MListener());

        mWebViewProxy = new WebViewProxy(mWebView);
        mWebViewProxy.registerAll(RegisterList.getAllRegisterHandler(this));

//        mWebView.setVisibility(View.INVISIBLE);

        setRefreshListener(this);

        String loadUrl = getIntent().getStringExtra(IntentKey.EXTRA_URL);
//        mWebViewProxy.removeRepetLoadUrl(loadUrl);

//        onLoadingStatus(CommonCode.General.DATA_LOADING);

        mWebViewProxy.removeRepetLoadUrl("file:///android_asset/test.html");
    }

    @Override
    public void initData() {
        webChromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
//                if (newProgress >= 98 && !hadError) {
//                    mWebView.setVisibility(View.VISIBLE);
//                    onLoadingStatus(CodeStatus.General.DATA_SUCCESS);
//                }
            }
        };
        mWebView.setWebChromeClient(webChromeClient);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mWebView.resumeTimers();
        mWebViewProxy.autoCancelUiHandler();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mWebView.pauseTimers();
    }

    public String getOssDir() {
        String url = mWebView.getUrl();
        if (!TextUtils.isEmpty(url)) {
            String[] strs = url.split("/");
            if (strs.length >= 4) {
                return strs[3] + "/";
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }

    @Override
    public void refreshPage() {
        hadError = false;
        mWebView.reload();
        onLoadingStatus(CommonCode.General.DATA_LOADING);
    }


    private class MListener implements AdvancedWebView.Listener {

        @Override
        public void onPageStarted(String url, Bitmap favicon) {

        }

        @Override
        public void onPageFinished(String url) {

        }

        @Override
        public void onPageError(int errorCode, String description, String failingUrl) {
            hadError = true;
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent == null) return;
        switch (baseEvent.type) {
            case CommonCode.EventType.FINISH_LOADING:
//                if (mWebView != null)
//                    mWebView.setVisibility(View.VISIBLE);
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

    @Override
    public void onBackPressed() {
        if (hadError)
            super.onBackPressed();
        else {
            RequestBackHandler backHandler = new RequestBackHandler(this);
            backHandler.setRequestData(null);
            mWebViewProxy.requestJsFun(backHandler);
        }
    }
}
