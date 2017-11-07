package com.rz.circled.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rz.circled.R;
import com.rz.circled.modle.ShareModel;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentKey;
import com.rz.common.utils.StatusBarUtils;
import com.rz.common.utils.StringUtils;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.VideoEnabledWebChromeClient;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 作者：Administrator on 2016/8/31 0031 19:10
 * 功能：显示H5的界面
 * 说明：
 */
public class VideoH5Aty extends Activity {

    public static final String JS_BRIDGE_SEND = "window.postMessage";

    private VideoEnabledWebChromeClient webChromeClient;

    /**
     * 进度条
     */
    @BindView(R.id.id_webview)
    WebView webView;

    /**
     * 错误url
     */
    @BindView(R.id.id_error_ulr_txt)
    TextView mTxtErrorUrl;

    /**
     * 哪个界面
     */
    private int page = 0;

    /**
     * 路径地址
     */
    private String url;
    /**
     * 标题名
     */
    private String titleName;

    private String titleDescription;

    private TextView titleText, titleBack;

    private TextView mTvTitleBarRight;

    /**
     * 启动h5界面
     */
    public static void startCommonH5(Activity activity, String url) {
        Intent intent = new Intent(activity, VideoH5Aty.class);
        intent.putExtra(IntentKey.KEY_URL, url);
        activity.startActivity(intent);
    }

    /**
     * 启动h5界面
     */
    public static void startCommonH5(Activity activity, String url, String titleName) {
        Intent intent = new Intent(activity, VideoH5Aty.class);
        intent.putExtra(IntentKey.KEY_URL, url);
        intent.putExtra(IntentKey.KEY_DESC, titleName);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_video_h5);
        ButterKnife.bind(this);
        initView();
    }

    protected void setTitleText(String text) {
        titleText.setText(text);
    }

    public void initView() {
        StatusBarUtils.setDarkStatusIcon(this, true);
        titleText = (TextView) findViewById(R.id.titlebar_title_text);
        titleBack = (TextView) findViewById(R.id.titlebar_back_text);
        mTvTitleBarRight = (TextView) findViewById(R.id.titlebar_right_text);
        page = getIntent().getIntExtra("page", Constants.DEFAULTVALUE);
        url = getIntent().getStringExtra(IntentKey.KEY_URL);
        if (getIntent().getStringExtra(IntentKey.KEY_DESC) != null) {
            setTitleText(getIntent().getStringExtra(IntentKey.KEY_DESC));
        }
        titleName = getString(R.string.share_activity_title);
        titleDescription = getString(R.string.share_activity_desc);
        mTvTitleBarRight.setText("分享");
        mTvTitleBarRight.setTextColor(getResources().getColor(R.color.font_gray_l));
        mTvTitleBarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injectGetContent();
            }
        });

        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setDomStorageEnabled(true);
        WebSettings settings = webView.getSettings();
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
        webView.setVerticalScrollBarEnabled(false);
        webView.setVerticalScrollbarOverlay(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setHorizontalScrollbarOverlay(false);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
//        webView.addJavascriptInterface(this, ServerProxy.JAVA_BRIDGE);

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse(url)));
            }
        });
        webView.setWebViewClient(new WebViewClient());


        initVideoConfig();

//        webView.setWebChromeClient(new MyWebChromeClient());
//        webView.setWebViewClient(new WebViewClientWarpper() {
//        });
//        webView.setWebViewClient(new ReWebViewClient());

        if (!StringUtils.isEmpty(url) && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("www."))) {
            if (url.startsWith("www."))
                url = "http://" + url;
            webView.loadUrl(url);
        } else {
            mTxtErrorUrl.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            if (StringUtils.isEmpty(url)) {
                mTxtErrorUrl.setText(R.string.jump_url_null);
            } else {
                mTxtErrorUrl.setText(url);
            }
        }


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

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
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
        webView.loadUrl(script);
    }

    private void initVideoConfig() {
        View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout); // Your own view, read class comments
        //noinspection all
        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available
                // constructors...
        {

            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress) {
                Log.d("yeying", "onProgressChanged " + progress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Bundle extras = getIntent().getExtras();
                Log.d("videoH5", "webTitle = " + title);
                if (extras != null && TextUtils.isEmpty(extras.getString(IntentKey.EXTRA_TITLE)) && !TextUtils.isEmpty(title)) {
//                    String loadUrl = mWebView.getUrl();
                    if (!TextUtils.isEmpty(title) && !title.startsWith("http://") && !title.startsWith("https://") && !title.startsWith("www.")) {
                        setTitleText(title);
                        titleName = !TextUtils.isEmpty(title) ? title : getString(R.string.share_activity_title);
                    }
                }
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen) {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (Build.VERSION.SDK_INT >= 14) {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (Build.VERSION.SDK_INT >= 14) {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

            }
        });
        webView.setWebChromeClient(webChromeClient);
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
    }


    @Override
    public void onBackPressed() {
        // Notify the VideoEnabledWebChromeClient, and handle it ourselves if it doesn't handle it
        if (!webChromeClient.onBackPressed()) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                // Standard back button implementation (for example this could close the app)
                super.onBackPressed();
            }
        }
    }

    //    /**
//     * js端调用native统一方法入口
//     */
    @JavascriptInterface
    public void invoke(String params) {
        Log.d("JsBridge", "js invoke -- " + params);
        ParamsObject paramsObject = ParamsObject.Parse(params);
        if (paramsObject != null) {
            paramsObject.data = getString(R.string.unsupport_service);
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
//        Toast.makeText(mWebView.getContext(), result, Toast.LENGTH_LONG).show();
        return result;
    }

    public void callbackInvoke(String params) {
        String script = toCallbackJsParams(params);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(script, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                }
            });
        } else {
            webView.loadUrl("javascript:" + script);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    /**
     * 获取WebView的数据:标题,描述,分享链接等
     */
    final class InJavaScriptLocalObj {
        @android.webkit.JavascriptInterface
        public void getMetaContent(String string) {
            Log.e("zxw", "getShareTitle: " + string);
            titleDescription = TextUtils.isEmpty(string.trim()) ? getString(R.string.share_activity_desc) : string;
            ShareNewsAty.startShareNews(VideoH5Aty.this, new ShareModel(titleName, titleDescription, url));
        }
    }
}
