package com.rz.sgt.jsbridge.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

/**
 * webview代理，解决部分交互事宜
 */

public class WebViewProxy {

    public static final String JS_BRIDGE_SEND = "window.postMessage";
    public static final String JS_BRIDGE_ON = "window.yryz._on";

    private static final String PRES_NAME = "system_share";
    private static final String USER_ID = "user_id";
    private AdvancedWebView mWebView;
    private WebViewClientWarpper webViewClientWarpper;
    private String mCurrentPage;

    private ServerProxy mServerProxy;

    private String userId;

    private boolean needLoading = false;

    public boolean isNeedLoading() {
        return needLoading;
    }

    public void setNeedLoading(boolean needLoading) {
        this.needLoading = needLoading;
    }

    public WebViewProxy(AdvancedWebView webView) {
        this.mWebView = webView;
        this.mServerProxy = new ServerProxy(this);


        mWebView.setGeolocationEnabled(true);
        mWebView.setMixedContentAllowed(true);
        mWebView.setCookiesEnabled(true);
        mWebView.setThirdPartyCookiesEnabled(true);
//        mWebView.addHttpHeader("X-Requested-With", "");

//        mWebView.getSettings().setUseWideViewPort(true);
//        mWebView.getSettings().setSupportZoom(true);
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mWebView.setVerticalScrollBarEnabled(false);
//        mWebView.setVerticalScrollbarOverlay(false);
//        mWebView.setHorizontalScrollBarEnabled(false);
//        mWebView.setHorizontalScrollbarOverlay(false);
//        mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        inject(webView);

    }


    public void removeRepetLoadUrl(String url) {
        userId = mWebView.getContext().getSharedPreferences(PRES_NAME, Context.MODE_PRIVATE).getString(USER_ID, "");
//        if (!TextUtils.isEmpty(url) && !url.contains("http://")) {
//            url = " http://" + url;
//        }
        if (!TextUtils.isEmpty(url)) {
            Log.d("webView", "this is webContainer load url is " + url);
//            webViewClientWarpper.needClearHistory = true;
            mCurrentPage = url;
            mServerProxy.reload();
            setCookie(mWebView.getContext(), "userid=" + userId, url);
            mWebView.loadUrl(url);
        }
    }

    public void setCookie(Context context, String cookie, String url) {

        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, "device=android");
        cookieManager.setCookie(url, cookie);

        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }

        Log.d("app", "cookieManager " + cookieManager.getCookie(url));
    }

    public void clerWebviewCache() {
        if (mWebView != null) {
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.clearFormData();
        }
    }


    public void monitor(String urlString) {
        if (!isInnerPageJump(mCurrentPage, urlString)) {
            mCurrentPage = urlString;
//          mClientProxy.onNewPageLoaded();
        }
    }

    public Callback getCallBackByInvokeId(long invokeId) {
        return mServerProxy.getCallbackByInvokeId(invokeId);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "JavascriptInterface"})
    private void inject(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(mServerProxy, ServerProxy.JAVA_BRIDGE);

    }

    public void register(ServerHandler serverHandler) {
        mServerProxy.register(serverHandler);
    }

    public void registerAll(Map<String, ServerHandler> handlers) {
        mServerProxy.registerAll(handlers);
    }

    public void setWebViewClientWarpper(WebViewClientWarpper webViewClientWarpper) {
        this.webViewClientWarpper = webViewClientWarpper;
        mWebView.setWebViewClient(webViewClientWarpper);
    }

    private boolean isInnerPageJump(String oldUrl, String newUrl) {
        if (oldUrl == null || newUrl == null) {
            return false;
        }
        try {
            URL urlA = new URL(oldUrl);
            URL urlB = new URL(newUrl);
            return urlA.sameFile(urlB);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void destroy() {
        mServerProxy.destroy();
        mWebView.getSettings().setJavaScriptEnabled(false);
        clerWebviewCache();
    }

    public void subject(ParamsObject paramsObject) {
        subjectJs(new Gson().toJson(paramsObject));
    }

    public void callbackInvoke(String params) {
        String script = toCallbackJsParams(params);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript(script, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                }
            });
        } else {
            mWebView.loadUrl("javascript:" + script);
        }
    }

    public void subjectJs(String params) {
        String script = toSubjectJsParams(params);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript(script, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                }
            });
        } else {
            mWebView.loadUrl("javascript:" + script);
        }
    }

    public String toCallbackJsParams(String params) {
        String result = String.format(Locale.getDefault(), "%s(\'%s\',window.location.origin)", JS_BRIDGE_SEND, params);
        Log.d("JsBridge", "js callback " + result);
//        Toast.makeText(mWebView.getContext(), result, Toast.LENGTH_LONG).show();
        return result;
    }

    public String toSubjectJsParams(String params) {
        String result = String.format(Locale.getDefault(), "%s(\'%s\')", JS_BRIDGE_ON, params);
        Log.d("JsBridge", "toSubjectJsParams " + result);
        return result;
    }

    public Callback removeCallbackFromInvokeId(long invokeId) {
        return mServerProxy.removeCallback(invokeId);
    }


    public void changeUIStatus(long invokeId) {
        mServerProxy.changeUIStatus(invokeId);
    }


    public void autoCancelUiHandler() {
        Callback callback = mServerProxy.getCurrentHandingUiHandlerCallback();
        if (callback != null) {
            changeUIStatus(callback.invokeId);
            removeCallbackFromInvokeId(callback.invokeId);
            BaseParamsObject baseParamsObject = callback.initBaseParamsObject();
            baseParamsObject.resultCode = BaseParamsObject.RESULT_CODE_CANCEL;
            ParamsObject paramsObject = new ParamsObject(baseParamsObject);
            callbackInvoke(new Gson().toJson(paramsObject));
        }
    }

    public Callback getCurrentHandingUiHandlerCallback() {
        Callback callback = mServerProxy.getCurrentHandingUiHandlerCallback();
        return callback;
    }

    //----------------主动调用js------------//

    /**
     * 请求js的方法
     *
     * @param jsServerHandler
     */
    public void requestJsFun(RequestJsServerHandler jsServerHandler) {
        boolean hasAdd = mServerProxy.addRequestCallback(jsServerHandler);
        if (hasAdd) {//可以请求
            String jsScript = toRequestJsScript(jsServerHandler.getRequestJsData());
            requestJs(jsScript);
        }
    }

    public String toRequestJsScript(String params) {
        String result = String.format(Locale.getDefault(), "%s(\'%s\',window.location.origin)", JS_BRIDGE_SEND, params);
        Log.d("JsBridge", "js request " + result);
        return result;
    }

    private void requestJs(String jsScript) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript(jsScript, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                }
            });
        } else {
            mWebView.loadUrl("javascript:" + jsScript);
        }
    }
}
