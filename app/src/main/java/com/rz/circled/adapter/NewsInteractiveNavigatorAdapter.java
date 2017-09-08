package com.rz.circled.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.httpapi.bean.NewsInteractiveTabBean;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.List;

import static net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator.MODE_WRAP_CONTENT;

/**
 * Created by rzw2 on 2017/9/1.
 */

public class NewsInteractiveNavigatorAdapter extends CommonNavigatorAdapter {
    private Context mContext;
    private List<NewsInteractiveTabBean> mDataList;
    private ViewPager viewPager;

    public NewsInteractiveNavigatorAdapter(Context context, ViewPager viewPager, List<NewsInteractiveTabBean> mDataList) {
        this.mContext = context;
        this.viewPager = viewPager;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public IPagerTitleView getTitleView(final Context context, final int i) {
        CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);

        // load custom layout
        final View customLayout = LayoutInflater.from(context).inflate(R.layout.item_news_interactive_tab, null);
        final ImageView titleImg = (ImageView) customLayout.findViewById(R.id.image);
        final TextView titleText = (TextView) customLayout.findViewById(R.id.tv_desc);
        titleText.setText(mDataList.get(i).getName());
        titleImg.setVisibility(mDataList.get(i).getUnReadNum() != 0 ? View.VISIBLE : View.GONE);
        commonPagerTitleView.setContentView(customLayout);

        commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

            @Override
            public void onSelected(int index, int totalCount) {
                titleText.setTextColor(context.getResources().getColor(R.color.font_color_blue));
            }

            @Override
            public void onDeselected(int index, int totalCount) {
                titleText.setTextColor(context.getResources().getColor(R.color.font_gray_xl));
            }

            @Override
            public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
            }

            @Override
            public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
            }
        });

        commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(i);
            }
        });

        return commonPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(MODE_WRAP_CONTENT);
        indicator.setColors(mContext.getResources().getColor(R.color.color_0185FF));
        return indicator;
    }

    public void setData(List<NewsInteractiveTabBean> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }
}
