package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.event.EventConstant;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.FriendPresenter1;
import com.rz.circled.presenter.impl.ProveInfoPresenter;
import com.rz.circled.presenter.impl.V3CirclePresenter;
import com.rz.circled.ui.fragment.MyActivityFragment;
import com.rz.circled.ui.fragment.MyArticleFragment;
import com.rz.circled.ui.fragment.MyCircleFragment;
import com.rz.circled.ui.fragment.MyRewardFragment;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.PagerSlidingTabStripHome;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.httpapi.bean.FriendInformationBean;
import com.rz.httpapi.bean.ProveStatusBean;
import com.yryz.yunxinim.session.SessionHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.famous_role_layout)
    LinearLayout famousLayout;

    @BindView(R.id.add_friend_layout)
    LinearLayout addFriendLayout;

    @BindView(R.id.add_friend_btn)
    Button addFriendBtn;


    private InfoAdapter infoAdapter;
    private List<Fragment> fragmentList;

    ImageView editImg;

    View header;
    View newTitilbar;
    private int headHight;
    private IPresenter presenter;
    private IPresenter friendPresenter;
    FriendInformationBean model;

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
        context.startActivity(intent);

    }
    @Override
    public void initView() {
        initHead();

        userId = getIntent().getExtras().getString(IntentKey.KEY_ID);

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
    }

    @Override
    public void initData() {

        //个人中心
        if(userId.equals(Session.getUserId())){
            Glide.with(this).load(Session.getUserPicUrl()).transform(new GlideCircleImage(this)).
                    placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(avatarImg);
            nameTxt.setText(Session.getUserName());
            levelTxt.setText("Lv." + Session.getUserLevel());
            if(TextUtils.isEmpty(Session.getUser_signatrue())){
                signTxt.setText(getString(R.string.mine_sign_default));
            }else{
                signTxt.setText(Session.getUser_signatrue());
            }

            addFriendBtn.setText("聊天");
            //普通用户
            if(Session.getCustRole().equals("0")){
                userRole.setText("去认证");
                userRole.setBackgroundResource(R.drawable.shape_white_bg);
                userRole.setPadding(20,0,20,0);
                userRole.getBackground().setAlpha(77);
            }else{
                //达人用户，另外调达人类型接口
                ((V3CirclePresenter) presenter).getFamousStatus(Session.getUserId());
            }
        }else{   //他人中心
            //判断他人与自己的关系（是否添加好友）
            editImg.setVisibility(View.GONE);
            ((FriendPresenter1) friendPresenter).getFriendInfoDetail(userId);

        }
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new V3CirclePresenter();
        presenter.attachView(this);

        friendPresenter = new FriendPresenter1();
        friendPresenter.attachView(this);

    }

    public void initHead(){

        newTitilbar = View.inflate(this, R.layout.titlebar_user_info, null);
        newTitilbar.setBackgroundResource(R.mipmap.topbar_blue_top);
        newTitilbar.getBackground().mutate().setAlpha(255);
        TextView tv = (TextView) newTitilbar.findViewById(R.id.titlebar_main_tv);
        ImageView ib = (ImageView) newTitilbar.findViewById(R.id.titlebar_main_left_btn);
        editImg = (ImageView) newTitilbar.findViewById(R.id.scores_img);
        RelativeLayout editLayout = (RelativeLayout) newTitilbar.findViewById(R.id.edit_layout);

        editLayout.setVisibility(View.VISIBLE);
        ib.setVisibility(View.VISIBLE);
        ib.setImageResource(R.mipmap.arrow_left);

        tv.setText("个人中心");
        mTitleContent.addView(newTitilbar);

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                skipActivity(UserInfoActivity.this,PersonInfoAty.class);

            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if(baseEvent.type == CommonCode.EventType.TYPE_USER_UPDATE){
            setData(null);
        }

        if(baseEvent.info.equals(FriendPresenter1.FRIEND_EVENT)){
            addFriendLayout.setVisibility(View.GONE);
        }

        if(baseEvent.type == EventConstant.USER_AVATAR_REFUSE){
            //更新用户详情
            ((FriendPresenter1) friendPresenter).getFriendInfoDetail(Session.getUserId());
        }

    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    public void refreshPage() {

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
                return MyRewardFragment.newInstance("1");      //悬赏
            if (position == 2)
                return MyCircleFragment.newInstance(MyCircleFragment.TYPE_ALL);     //私圈
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


    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if(t instanceof ProveStatusBean) {
            ProveStatusBean data = (ProveStatusBean) t;
            if(userId.equals(Session.getUserId())){
                if(Session.getCustRole().equals("0")){
                    userRole.setText("去认证");
                    userRole.setPadding(20,0,20,0);
                    userRole.setBackgroundResource(R.drawable.shape_white_bg);
                    userRole.getBackground().setAlpha(77);
                }else if(data.getAuthStatus() == 1){
                    userRole.setText(data.getTradeField());
                }
            }else  if(t instanceof FriendInformationBean){
                if(t != null){
                    FriendInformationBean bean = (FriendInformationBean) t;

                    Glide.with(this).load(bean.getCustImg()).transform(new GlideCircleImage(this)).
                            placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(avatarImg);
                }
            }else{
                if(data.getAuthStatus() == 1){
                    famousLayout.setVisibility(View.VISIBLE);
                    userRole.setText(data.getTradeField());
                }else {
                    famousLayout.setVisibility(View.GONE);
                }
            }

        }else if(t instanceof FriendInformationBean){
            model = (FriendInformationBean) t;
            setData(model);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setData(FriendInformationBean model){

        if(model == null){
            if(userId.equals(Session.getUserId())){
                Glide.with(this).load(Session.getUserPicUrl()).transform(new GlideCircleImage(this)).
                        placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(avatarImg);
                nameTxt.setText(Session.getUserName());
                levelTxt.setText("Lv." + Session.getUserLevel());
                if(TextUtils.isEmpty(Session.getUser_signatrue())){
                    signTxt.setText(getString(R.string.mine_sign_default));
                }else {
                    signTxt.setText(Session.getUser_signatrue());
                }

                addFriendLayout.setVisibility(View.GONE);
                //普通用户
                if(Session.getCustRole().equals("0")){
                    userRole.setText("去认证");
                    userRole.setBackgroundResource(R.drawable.shape_white_bg);
                    userRole.setPadding(20,0,20,0);
                    userRole.getBackground().setAlpha(77);
                }else{
                    //达人用户，另外调达人类型接口
                    ((V3CirclePresenter) presenter).getFamousStatus(Session.getUserId());
                }
            }
        }else {
            Glide.with(this).load(model.getCustImg()).transform(new GlideCircleImage(this)).
                    placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(avatarImg);
            nameTxt.setText(model.getCustNname());
            levelTxt.setText("Lv." + model.getCustLevel());
            if(TextUtils.isEmpty(Session.getUser_signatrue())){
                signTxt.setText(getString(R.string.mine_sign_default));
            }else{
                signTxt.setText(Session.getUser_signatrue());
            }
            if(model.getRelation() == 0){
                //陌生人
                addFriendBtn.setText("加好友");
            }else{  //好友
                addFriendBtn.setText("聊天");
            }
//            userRole.setText(model.getCustRole());

        }
    }

    @OnClick(R.id.add_friend_btn)
    public void addFriendClick(){
        if(model != null && model.getRelation() == 0){
            ((FriendPresenter1) friendPresenter).requireFriend(userId,"",1,CommonCode.requireFriend.require_type_agree);
        }else{
            SessionHelper.startP2PSession(this, model.getCustId());
        }
    }

}
