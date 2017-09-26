package com.rz.circled.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;

import com.rz.circled.R;
import com.rz.common.utils.DensityUtils;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import static net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator.MODE_MATCH_EDGE;
import static net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator.MODE_WRAP_CONTENT;

/**
 * Created by rzw2 on 2017/9/1.
 */

public class PrivateGroupNavigatorAdapter extends CommonNavigatorAdapter {
    private Context mContext;
    private List<String> mDataList;
    private ViewPager viewPager;

    public PrivateGroupNavigatorAdapter(Context context, ViewPager viewPager, List<String> mDataList) {
        this.mContext = context;
        this.viewPager = viewPager;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int i) {
        SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
        simplePagerTitleView.setText(mDataList.get(i));
        simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.px46));
        simplePagerTitleView.setNormalColor(mContext.getResources().getColor(R.color.font_gray_xl));
        simplePagerTitleView.setSelectedColor(mContext.getResources().getColor(R.color.color_0185FF));
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(i);
            }
        });
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(MODE_WRAP_CONTENT);
        indicator.setRoundRadius(8.0f);
        indicator.setColors(mContext.getResources().getColor(R.color.color_0185FF));
        return indicator;
    }

    public void setData(List<String> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }
}
