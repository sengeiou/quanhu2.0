package com.rz.circled.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.circled.application.QHApplication;
import com.rz.circled.event.EventConstant;
import com.rz.circled.js.RequestBackHandler;
import com.rz.circled.js.RequestJsBroadcastHandler;
import com.rz.circled.js.model.HeaderModel;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.ui.view.BaseLoadView;
import com.rz.common.utils.IntentUtil;
import com.rz.common.utils.StringUtils;
import com.rz.common.utils.SystemUtils;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.rz.sgt.jsbridge.RegisterList;
import com.rz.sgt.jsbridge.core.AdvancedWebView;
import com.rz.sgt.jsbridge.core.AndroidBug5497Workaround;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;
import com.yryz.yunxinim.uikit.common.util.string.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class WebContainerActivity extends BaseActivity implements BaseLoadView.RefreshListener {

    private AdvancedWebView mWebView;
    private WebViewProxy mWebViewProxy;

    private boolean processBack = false;
    private WebChromeClient webChromeClient;
    private boolean processLoading = false;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    public static void startActivity(Context context, String url) {
        startActivity(context, url, -1);
    }

    public static void startActivity(Context context, String url, int flag) {
        Intent intent = new Intent(context, WebContainerActivity.class);
        intent.putExtra(IntentKey.EXTRA_URL, url);
        if (flag != -1) intent.setFlags(flag);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String url, boolean processLoading) {
        Intent intent = new Intent(context, WebContainerActivity.class);
        intent.putExtra(IntentKey.EXTRA_URL, url);
        intent.putExtra(IntentKey.EXTRA_BOOLEAN, processLoading);
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
    protected boolean hasDataInPage() {
        return false;
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    protected boolean needSwipeBack() {
        return false;
    }

    @Override
    public void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mWebView = (AdvancedWebView) findViewById(R.id.webview_container);

        AndroidBug5497Workaround.assistActivity(this, mWebView);

        mWebView.setListener(this, new MListener());

        mWebViewProxy = new WebViewProxy(mWebView);
        mWebViewProxy.registerAll(RegisterList.getAllRegisterHandler(this));

//        mWebView.setVisibility(View.INVISIBLE);

        setRefreshListener(this);
        processLoading = getIntent().getBooleanExtra(IntentKey.EXTRA_BOOLEAN, false);
        String loadUrl = getIntent().getStringExtra(IntentKey.EXTRA_URL);
        mWebViewProxy.removeRepetLoadUrl(loadUrl);
        if (processLoading)
            processBack = false;

        onLoadingStatus(CommonCode.General.DATA_LOADING);

//        mWebViewProxy.removeRepetLoadUrl("file:///android_asset/test.html");
    }

    @Override
    public void initData() {
        webChromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 98 && processLoading) {
                    mWebView.setVisibility(View.VISIBLE);
                    onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                }
            }
        };
        mWebView.setWebChromeClient(webChromeClient);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebViewProxy.autoCancelUiHandler();
        mWebView.onResume();

        RequestJsBroadcastHandler requestJsBroadcastHandler = new RequestJsBroadcastHandler(this);
        requestJsBroadcastHandler.setNativeEvent("nativeActivated");
        requestJsBroadcastHandler.setRequestData(getLoginWebResultData());
        mWebViewProxy.requestJsFun(requestJsBroadcastHandler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
//        mWebView.destroy();
    }

    @Override
    public void refreshPage() {
        processBack = false;
        mWebView.reload();
        onLoadingStatus(CommonCode.General.DATA_LOADING);
    }

    private String telUri = "";

    private class MListener implements AdvancedWebView.Listener {

        @Override
        public void onPageStarted(String url, Bitmap favicon) {

        }

        @Override
        public void onPageFinished(String url) {

        }

        @Override
        public void onPageError(int errorCode, String description, String failingUrl) {
            processBack = false;
            onLoadingStatus(CommonCode.General.WEB_ERROR);
        }

        @Override
        public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String
                contentDisposition, String userAgent) {

        }

        @Override
        public void onExternalPageRequest(String url) {

        }

        @Override
        public void callPhoneNumber(String uri) {
            telUri = uri;
            // 检查是否获得了权限（Android6.0运行时权限）
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // 没有获得授权，申请授权
                if (ActivityCompat.shouldShowRequestPermissionRationale(WebContainerActivity.this, Manifest.permission.CALL_PHONE)) {
                    // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                    // 弹窗需要解释为何需要该权限，再次请求授权
                    Toast.makeText(mContext, R.string.call_phone_authorization, Toast.LENGTH_LONG).show();
                } else {
                    // 不需要解释为何需要该权限，直接请求授权
                    ActivityCompat.requestPermissions(WebContainerActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }
            } else {
                // 已经获得授权，可以打电话
                callPhone();
            }
        }

        @Override
        public void doUpdateVisitedHistory() {

        }
    }

    private void callPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse(telUri));
        startActivity(intent);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent == null) return;
        switch (baseEvent.type) {
            case CommonCode.EventType.FINISH_LOADING:
//                if (mWebView != null)
//                    mWebView.setVisibility(View.VISIBLE);
                processBack = true;
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
        if (!processBack)
            super.onBackPressed();
        else {
            RequestBackHandler backHandler = new RequestBackHandler(this);
            backHandler.setRequestData(null);
            mWebViewProxy.requestJsFun(backHandler);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 授权成功，继续打电话
                callPhone();
            } else {
                // 授权失败！
                Toast.makeText(this, R.string.authorization_fail, Toast.LENGTH_LONG).show();
            }
        }
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
