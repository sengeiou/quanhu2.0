package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.sgt.jsbridge.core.AdvancedWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gsm on 2017/8/29.
 */
public class RewardFragment extends BaseFragment {

    @BindView(R.id.webView)
    AdvancedWebView webView;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_reward, null);
    }

    @Override
    public void initView() {
        webView.loadUrl(BuildConfig.WebHomeBaseUrl + "/activity/reward");
    }

    @Override
    public void initData() {

    }
}
