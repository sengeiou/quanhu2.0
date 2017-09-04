package com.rz.common.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * 带进度条的webview
 */
public class ProgressWebView extends WebView {

    private ProgressBar progressbar;
    private OnReceivedTitleListener mTitleListener;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setBackgroundColor(Color.parseColor("#29d1e2"));
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                5, 0, 0));
        addView(progressbar);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        setWebViewClient(new WebViewClient());
        setWebChromeClient(new WebChromeClient());
    }

    public class WebViewClient extends android.webkit.WebViewClient {

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
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.i("newProgress", "----->" + newProgress);
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE) {
                    progressbar.setVisibility(VISIBLE);
                }
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mTitleListener != null)
                mTitleListener.onReceivedTitle(title);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
    }

    public void setOnReceivedTitleListener(OnReceivedTitleListener mTitleListener) {
        this.mTitleListener = mTitleListener;
    }

    public interface OnReceivedTitleListener {
        void onReceivedTitle(String title);
    }
}
