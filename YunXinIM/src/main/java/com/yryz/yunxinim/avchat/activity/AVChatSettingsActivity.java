package com.yryz.yunxinim.avchat.activity;

import android.os.Bundle;

import com.yryz.yunxinim.R;
import com.yryz.yunxinim.uikit.common.activity.UI;
import com.yryz.yunxinim.uikit.model.ToolBarOptions;

/**
 * Created by liuqijun on 7/19/16.
 * 注意:全局配置,不区分用户
 */
public class AVChatSettingsActivity extends UI {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.avchat_settings_layout);

        ToolBarOptions options = new ToolBarOptions();
        options.titleId = R.string.nrtc_settings;
        setToolBar(R.id.toolbar, options);


    }

}
