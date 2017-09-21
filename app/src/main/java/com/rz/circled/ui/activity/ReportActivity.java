package com.rz.circled.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;

import com.rz.circled.R;
import com.rz.circled.ui.view.FileWebView;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.activity.BaseActivity;


/**
 * Created by rzw2 on 2017/5/27.
 */

public class ReportActivity extends BaseActivity implements FileWebView.Listener {

    private FileWebView mWebView;
    private String url;

    public static void startAty(Context context, String url) {
        Intent intent = new Intent(context, ReportActivity.class);
        intent.putExtra(IntentKey.EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected boolean needSwipeBack() {
        return false;
    }

    @Override
    protected boolean needStatusBarTint() {
        return false;
    }

    @Override
    public boolean hasDataInPage() {
        return false;
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_report, null);
    }

    @Override
    public void initView() {
        url = getIntent().getStringExtra(IntentKey.EXTRA_URL);

        mWebView = (FileWebView) findViewById(R.id.webview_report);
        mWebView.setListener(this, this);
        mWebView.setGeolocationEnabled(false);
        mWebView.setMixedContentAllowed(true);
        mWebView.setCookiesEnabled(true);
        mWebView.setThirdPartyCookiesEnabled(true);
        mWebView.addHttpHeader("X-Requested-With", "");
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setVerticalScrollbarOverlay(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setHorizontalScrollbarOverlay(false);
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
        mWebView.loadUrl(url);
    }

    @Override
    public void initData() {

    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) {
            return;
        }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
    }

    @Override
    public void onPageFinished(String url) {
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
    }

    @Override
    public void onExternalPageRequest(String url) {
    }

    @Override
    public void refreshPage() {
        mWebView.loadUrl(url);
    }
}

