package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.ui.fragment.PrivateGroupCreateByMyselfFragment;
import com.rz.circled.ui.fragment.SearchCircleFragment;
import com.rz.circled.ui.fragment.SearchContentFragment;
import com.rz.circled.ui.fragment.SearchPersonFragment;
import com.rz.circled.ui.fragment.SearchPrivateCircleFragment;
import com.rz.circled.ui.fragment.SearchRewardFragment;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.PagerSlidingTabStripHome;
import com.rz.circled.ui.fragment.MyActivityFragment;
import com.rz.circled.ui.fragment.MyArticleFragment;
import com.rz.circled.ui.fragment.MyCircleFragment;
import com.rz.circled.ui.fragment.MyRewardFragment;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class UserInfoActivity extends BaseActivity{

    @BindView(R.id.tab_pager_search)
    PagerSlidingTabStripHome tabPagerSearch;

    @BindView(R.id.vp_view)
    ViewPager viewPager;

//    @BindView(R.id.scrollableLayout)
//    ScrollableLayout scrollableLayout;

    @BindView(R.id.title_content)
    FrameLayout mTitleContent;

    @BindView(R.id.avatar_layout)
    RelativeLayout avatarLayout;

    @BindView(R.id.user_avatar)
    ImageView avatarImg;

    @BindView(R.id.user_name_txt)
    TextView nameTxt;

    @BindView(R.id.level_txt)
    TextView levelTxt;

    @BindView(R.id.sign_txt)
    TextView signTxt;

    @BindView(R.id.user_role)
    TextView userRole;

    private InfoAdapter infoAdapter;
    private List<Fragment> fragmentList;

    View header;
    View newTitilbar;
    private int headHight;

    private String userId = "";

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_user_info, null);
    }
    public static void newFrindInfo(Context context, String id) {

        Intent intent = new Intent(context, UserInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.KEY_ID, id);
        intent.putExtras(bundle);
//        if (!StringUtils.isEmpty(model.getCustNname())) {
//            tvNick.setText(model.getCustNname());
//        } else {
//            tvNick.setText("");
//        }
        context.startActivity(intent);


    }
    @Override
    public void initView() {
        initHead();

        avatarLayout.getBackground().setAlpha(77);

        tabPagerSearch.setCustomLayoutParams(4);
        tabPagerSearch.setLineFitFont(true);
        infoAdapter = new InfoAdapter(getSupportFragmentManager());
        viewPager.setAdapter(infoAdapter);
        viewPager.setOffscreenPageLimit(4);

//        tabPagerSearch.setTempPosition(type);

        tabPagerSearch.setViewPager(viewPager);
        tabPagerSearch.notifyDataSetChanged();
        tabPagerSearch.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                scrollableLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            scrollableLayout.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                    if (scrollY <= 0) {
//                        newTitilbar.getBackground().mutate().setAlpha(0);
//                    } else if (scrollY > 0 && scrollY <= headHight) {
//                        float scale = (float) scrollY / headHight;
//                        float alpha = (255 * scale);
//                        // 只是layout背景透明(仿知乎滑动效果)
//                        newTitilbar.getBackground().mutate().setAlpha((int) alpha);
//                    } else {
//                        newTitilbar.getBackground().mutate().setAlpha(255);
//                    }
//                }
//            });
//        }

    }
//    @Override
//    public View getTransTitleView() {
//        return View.inflate(this, R.layout.titlebar_mine, null);
//    }

    @Override
    public void initData() {

        if(userId.equals(Session.getUserId())){
            Glide.with(this).load(Session.getUserPicUrl()).transform(new GlideCircleImage(this)).
                    placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(avatarImg);
            nameTxt.setText(Session.getUserName());
            levelTxt.setText(Session.getUserLevel());
            signTxt.setText(Session.getUser_signatrue());

//            //普通用户
//            if(Session.getCustRole().equals("0")){
//
//            }else{
//                //达人用户，另外调达人类型接口
//
//
//
//            }
            userRole.setText("达人");

        }

    }

    public void initHead(){

        newTitilbar = View.inflate(this, R.layout.titlebar_mine, null);
        newTitilbar.setBackgroundResource(R.mipmap.topbar_blue_top);
        newTitilbar.getBackground().mutate().setAlpha(255);
        TextView tv = (TextView) newTitilbar.findViewById(R.id.titlebar_main_tv);
        ImageView ib = (ImageView) newTitilbar.findViewById(R.id.titlebar_main_left_btn);
        ib.setVisibility(View.VISIBLE);
        ib.setImageResource(R.mipmap.icon_arrow_left_gray);
        tv.setText("个人中心");
        mTitleContent.addView(newTitilbar);

    }


    @Override
    protected boolean needShowTitle() {
        return false;
    }


    private class InfoAdapter extends FragmentStatePagerAdapter {

        private final int[] itemName;

        public InfoAdapter(FragmentManager fm) {
            super(fm);
            itemName = new int[]{R.string.article_info, R.string.tab_reward, R.string.tab_private_circle, R.string.news_interactive_tab_activity};
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(itemName[position]);
        }



        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return MyArticleFragment.newInstance();     //文章
            if (position == 1)
                return MyRewardFragment.newInstance();      //悬赏
            if (position == 2)
                return PrivateGroupCreateByMyselfFragment.newInstance(PrivateGroupCreateByMyselfFragment.TYPE_ALL);     //私圈
            if (position == 3)
                return MyActivityFragment.newInstance();   //活动

            return MyArticleFragment.newInstance();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return itemName.length;
        }
    }
}
