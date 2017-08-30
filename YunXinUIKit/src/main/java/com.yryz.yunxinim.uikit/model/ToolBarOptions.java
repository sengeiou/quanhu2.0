package com.yryz.yunxinim.uikit.model;

import com.yryz.yunxinim.uikit.R;

/**
 * Created by hzxuwen on 2016/6/16.
 */
public class ToolBarOptions {
    /**
     * toolbar的title资源id
     */
    public int titleId = 0;
    /**
     * toolbar的title
     */
    public String titleString;
    /**
     * toolbar的logo资源id
     */
    public int logoId = R.drawable.nim_actionbar_nest_dark_logo;
    /**
     * toolbar的返回按钮资源id，默认开启的资源nim_actionbar_dark_back_icon
     */
    public int navigateId = R.drawable.v3_dark_left_arrow;
    /**
     * toolbar的返回按钮，默认开启
     */
    public boolean isNeedNavigate = true;

    public int backgroundId = R.drawable.nim_toolbar_bg;


}
