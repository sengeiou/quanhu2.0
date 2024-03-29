package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.rz.circled.R;
import com.rz.circled.modle.ShareModel;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.widget.ProgressWebView;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.ServerProxy;

import java.util.Locale;

import butterknife.BindView;

public class CommonH5Activity extends BaseActivity {

    private final String JS_BRIDGE_SEND = "window.postMessage";

    @BindView(R.id.webview_common_h5)
    ProgressWebView mWebView;

    /**
     * 哪个界面
     */
    private int page = 0;

    private boolean needShare = false;


    /**
     * 标题名
     */
    private String titleName;

    private String titleDescription;
    private String loadUrl;

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean hasDataInPage() {
        return false;
    }

    public static void startCommonH5(Context context, String title, String url) {
        startCommonH5(context, title, url, -1);
    }

    public static void startCommonH5(Context context, String title, String url, int flag) {
        Intent intent = new Intent(context, CommonH5Activity.class);
        intent.putExtra(IntentKey.EXTRA_TITLE, title);
        intent.putExtra(IntentKey.EXTRA_URL, url);
        if (flag != -1) intent.setFlags(flag);
        context.startActivity(intent);
    }

    public static void startCommonH5(Context context, String title, String url, boolean needClose, boolean needShare) {
        Intent intent = new Intent(context, CommonH5Activity.class);
        intent.putExtra(IntentKey.EXTRA_TITLE, title);
        intent.putExtra(IntentKey.EXTRA_URL, url);
        intent.putExtra(IntentKey.EXTRA_BOOLEAN, needClose);
        intent.putExtra(IntentKey.EXTRA_TYPE, needShare);
        context.startActivity(intent);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_common_h5, null);
    }

    @Override
    public void initView() {
        mWebView.getSettings().setDomStorageEnabled(true);
        WebSettings settings = mWebView.getSettings();
        settings.setAllowFileAccess(true);//设置启用或禁止访问文件数据
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true); //设置是否支持JavaScript
        settings.setBlockNetworkImage(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setVerticalScrollbarOverlay(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setHorizontalScrollbarOverlay(false);
        mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        mWebView.setOnReceivedTitleListener(new ProgressWebView.OnReceivedTitleListener() {
            @Override
            public void onReceivedTitle(String title) {
                Bundle extras = getIntent().getExtras();
                Log.d(TAG, "webTitle = " + title);
                if (extras != null && TextUtils.isEmpty(extras.getString(IntentKey.EXTRA_TITLE)) && !TextUtils.isEmpty(title)) {
                    if (!TextUtils.isEmpty(title) && !title.startsWith("http://") && !title.startsWith("https://") && !title.startsWith("www.")) {
                        setTitleText(title);
                        titleName = !TextUtils.isEmpty(title) ? title : getString(R.string.share_activity_title);
                    }
                }
            }
        });

        page = getIntent().getIntExtra("page", Constants.DEFAULTVALUE);
        titleName = getString(R.string.share_activity_title);
        titleDescription = getString(R.string.share_activity_desc);

        mWebView.setWebViewClient(new WebViewClient());

        setTitleLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void initData() {
        parseIntent();
    }

    private void parseIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString(IntentKey.EXTRA_TITLE, "");
            setTitleText(name);
            if (!extras.getBoolean(IntentKey.EXTRA_BOOLEAN)) {
                clearTitleRight();
            }
            needShare = extras.getBoolean(IntentKey.EXTRA_TYPE);
            if (needShare) {
                setTitleRightText("分享");
                setTitleRightListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        injectGetContent();
                    }
                });
                mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
            } else {
                mWebView.addJavascriptInterface(this, ServerProxy.JAVA_BRIDGE);
            }
            loadUrl = extras.getString(IntentKey.EXTRA_URL);
            if (!TextUtils.isEmpty(loadUrl) && (loadUrl.startsWith("http://") || loadUrl.startsWith("https://") || loadUrl.startsWith("www.")))
                mWebView.loadUrl(loadUrl);
            else onLoadingStatus(CommonCode.General.WEB_ERROR);
        } else {
            onLoadingStatus(CommonCode.General.WEB_ERROR);
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            if (!mWebView.canGoBack()) {
                showClose = false;
                if (!needShare)
                    clearTitleRight();
            }
        } else
            super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * js端调用native统一方法入口
     */
    @JavascriptInterface
    public void invoke(String params) {
        Log.d("JsBridge", "js invoke -- " + params);
        ParamsObject paramsObject = ParamsObject.Parse(params);
        if (paramsObject != null) {
            paramsObject.data = getString(R.string.un_support_function);
            paramsObject.errMsg = paramsObject.getInvokeName() + ":error";
            final ParamsObject temp = paramsObject;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callbackInvoke(new Gson().toJson(temp));
                }
            });
        }


    }

    public String toCallbackJsParams(String params) {
        String result = String.format(Locale.getDefault(), "%s(\'%s\',window.location.origin)", JS_BRIDGE_SEND, params);
        Log.d("JsBridge", "js callback " + result);
        return result;
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

    private boolean showClose = false;

    @Override
    public void refreshPage() {
        onLoadingStatus(CommonCode.General.DATA_LOADING);
        mWebView.reload();
    }

    public class WebViewClient extends android.webkit.WebViewClient {

        @Override
        public void onLoadResource(WebView view, String url) {
            Log.d(TAG, "url = " + url);
            super.onLoadResource(view, url);
            if (view.canGoBack() && !showClose && !needShare) {
                showClose = true;
                setTitleRightImageView(R.drawable.gray_close, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http:") || url.startsWith("https:")) {
                return false;
            }
            try {
                // Otherwise allow the OS to handle things like tel, mailto, etc.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            onLoadingStatus(CommonCode.General.WEB_ERROR);
        }
    }

    private void injectGetContent() {
        String script = "javascript:window.local_obj.getMetaContent(function (){" +
                "var children = document.head.children;var sons=[];" +
                "for (var i=0; i<children.length; i++) {" +
                "var son = children[i];sons.push(son.outerHTML);" +
                "   if (son.tagName.toLowerCase()=='meta' && son.name == 'description') {" +
                "       return son.content;" +
                "       }" +
                "   }" +
                " return \"\"}());";
        Log.e("zxw", "injectGetContent: " + script);
        mWebView.loadUrl(script);
    }

    /**
     * 获取WebView的数据:标题,描述,分享链接等
     */
    final class InJavaScriptLocalObj {
        @android.webkit.JavascriptInterface
        public void getMetaContent(String string) {
            Log.e("zxw", "getShareTitle: " + string);
            titleDescription = TextUtils.isEmpty(string.trim()) ? getString(R.string.share_activity_desc) : string;
            ShareNewsAty.startShareNews(CommonH5Activity.this, new ShareModel(titleName, titleDescription, loadUrl), page);
        }
    }
}
