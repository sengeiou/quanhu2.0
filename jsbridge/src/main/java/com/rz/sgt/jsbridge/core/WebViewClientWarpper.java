package com.rz.sgt.jsbridge.core;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public abstract class WebViewClientWarpper extends WebViewClient {
    public static final String JS_BRIGE_FILE = "yryz.js";
    public WebViewProxy mWebViewProxy;
    public boolean needClearHistory;

    public WebViewProxy getWebViewProxy() {
        return mWebViewProxy;
    }

    public void setWebViewProxy(WebViewProxy mWebViewProxy) {
        this.mWebViewProxy = mWebViewProxy;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
//        BridgeUtil.webViewLoadLocalJs(view, JS_BRIGE_FILE);
//        view.loadUrl("javascript:" + "document.cookie='userid=testUserId'");
//        mWebViewProxy.monitor(url);
        Log.d("app", "url " + url);

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//        Log.d("jsbridge", "shouldInterceptRequest " + url);
//        if ("yryz.js".equalsIgnoreCase(url)) {
//            Log.d("jsbridge", "yryz.js InterceptRequest" + url);
//            return new WebResourceResponse("text/html", "utf-8", BridgeUtil.assetFile2InputStream(view.getContext(), url));
//        } else {
//            return super.shouldInterceptRequest(view, url);
//        }
        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        Log.d("yeying", "onLoadResource " + url);
        super.onLoadResource(view, url);

//        android.webkit.CookieManager.getInstance().setAcceptCookie(true);
//        android.webkit.CookieManager.getInstance().setCookie(url, "userid=testuserid");
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
//        if (needClearHistory) {
//            needClearHistory = false;
//            view.clearHistory();//清除历史记录
//        }
    }

}
