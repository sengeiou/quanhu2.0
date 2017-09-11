package com.rz.circled.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.httpapi.bean.NewsInteractiveTabBean;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;

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
        final BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

        SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
        simplePagerTitleView.setText(mDataList.get(i).getName());
        simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.px50));
        simplePagerTitleView.getPaint().setFakeBoldText(true);
        simplePagerTitleView.setNormalColor(context.getResources().getColor(R.color.font_gray_xl));
        simplePagerTitleView.setSelectedColor(context.getResources().getColor(R.color.font_color_blue));
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(i);
                badgePagerTitleView.setBadgeView(null);
            }
        });
        if (mDataList.get(i).getUnReadNum() == 0) {
            badgePagerTitleView.setBadgeView(null);
//            View badgeImageView = LayoutInflater.from(context).inflate(R.layout.item_news_interactive_tab_img, null);
//            badgePagerTitleView.setBadgeView(badgeImageView);
        } else {
            View badgeImageView = LayoutInflater.from(context).inflate(R.layout.item_news_interactive_tab_img, null);
            badgePagerTitleView.setBadgeView(badgeImageView);
        }
        badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);
        badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, 0));
        badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));
        // don't cancel badge when tab selected
        badgePagerTitleView.setAutoCancelBadge(false);
        return badgePagerTitleView;
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
