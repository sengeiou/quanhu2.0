package com.rz.circled.adapter;

import android.content.Context;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class MyRewardAdapter extends CommonNavigatorAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int i) {
        return null;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        return null;
    }
}
