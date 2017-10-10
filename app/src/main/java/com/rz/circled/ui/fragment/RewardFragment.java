package com.rz.circled.ui.fragment;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.circled.js.RequestJsBroadcastHandler;
import com.rz.common.constant.CommonCode;
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
public class RewardFragment extends BaseFragment {

    @BindView(R.id.webView)
    AdvancedWebView mWebView;

    private WebViewProxy mWebViewProxy;


    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_reward, null);
    }

    @Override
    public void initView() {

        mWebViewProxy = new WebViewProxy(mWebView);
        mWebViewProxy.registerAll(RegisterList.getAllRegisterHandler(getActivity()));

//        mWebView.setVisibility(View.INVISIBLE);
        mWebViewProxy.removeRepetLoadUrl(BuildConfig.WebHomeBaseUrl + "/activity/reward");
//        mWebViewProxy.removeRepetLoadUrl("file:///android_asset/test.html");
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
        }
    }

    @Override
    public void refreshPage() {

    }
}
