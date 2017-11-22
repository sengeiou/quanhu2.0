package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.rz.circled.R;
import com.rz.circled.adapter.MyFragmentStatePagerAdapter;
import com.rz.circled.constants.CommonConstants;
import com.rz.circled.event.EventConstant;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.FriendPresenter1;
import com.rz.circled.presenter.impl.V3CirclePresenter;
import com.rz.circled.ui.fragment.MyActivityFragment;
import com.rz.circled.ui.fragment.MyArticleFragment;
import com.rz.circled.ui.fragment.MyCircleFragment;
import com.rz.circled.ui.fragment.UserRewardFragment;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.PagerSlidingTabStripHome;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.Protect;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.bean.FriendInformationBean;
import com.rz.httpapi.bean.ProveStatusBean;
import com.rz.httpapi.bean.RequestFriendStatusBean;
import com.yryz.yunxinim.session.SessionHelper;
import com.yryz.yunxinim.uikit.common.util.string.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.tab_pager_search)
    PagerSlidingTabStripHome tabPagerSearch;

    @BindView(R.id.vp_view)
    ViewPager viewPager;

    @BindView(R.id.scrollableLayout)
    ScrollableLayout scrollableLayout;

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

    private static final Integer[] TITLES = new Integer[]{R.string.article_activity, R.string.tab_reward, R.string.tab_private_circle, R.string.news_interactive_tab_activity};
    private List<String> mTitles = new ArrayList<>();
    private MyFragmentStatePagerAdapter infoAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();

    ImageView editImg;

    View newTitilbar;
    private IPresenter presenter;
    private IPresenter friendPresenter;
    FriendInformationBean model;

    private String userId = "";

    public static void newFrindInfo(Context context, String id) {
        newFrindInfo(context, id, -1);
    }

    public static void newFrindInfo(Context context, String id, int flag) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        if (flag != -1) intent.setFlags(flag);
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.KEY_ID, id);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_user_info, null);
    }

    @Override
    public void initView() {

        userId = getIntent().getExtras().getString(IntentKey.KEY_ID);
        if (TextUtils.isEmpty(userId)) {
            userId = Session.getUserId();
        }

        initHead();
        initFragment();
        initIndicator();
    }

    @Override
    public void initData() {

        signTxt.setTextColor(Color.argb(168, 255, 255, 255)); //背景透明度
        //个人中心
        if (userId.equals(Session.getUserId())) {
            if (Protect.checkLoadImageStatus(mContext)) {
                Glide.with(this).load(Session.getUserPicUrl()).transform(new GlideCircleImage(this)).
                        placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(avatarImg);
            }

            nameTxt.setText(Session.getUserName());
            levelTxt.setText("Lv." + Session.getUserLevel());
            if (TextUtils.isEmpty(Session.getUser_desc())) {
                signTxt.setText(getString(R.string.mine_sign_default));
            } else {
                signTxt.setText(Session.getUser_desc());
            }

            addFriendLayout.setVisibility(View.GONE);

            //达人用户，另外调达人类型接口
            ((V3CirclePresenter) presenter).getFamousStatus(Session.getUserId());
        } else {   //他人中心
            //判断他人与自己的关系（是否添加好友）
            editImg.setVisibility(View.GONE);
            ((FriendPresenter1) friendPresenter).getFriendInfoDetail(userId);
            ((V3CirclePresenter) presenter).getFamousStatus(userId);

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

    public void initHead() {
//        avatarLayout.getBackground().setAlpha(66);
        newTitilbar = View.inflate(this, R.layout.titlebar_user_info, null);
//        newTitilbar.setBackgroundResource(R.mipmap.topbar_blue_top);
        newTitilbar.getBackground().mutate().setAlpha(255);
        TextView tv = (TextView) newTitilbar.findViewById(R.id.titlebar_main_tv);
        ImageView ib = (ImageView) newTitilbar.findViewById(R.id.titlebar_main_left_btn);
        editImg = (ImageView) newTitilbar.findViewById(R.id.scores_img);
        RelativeLayout editLayout = (RelativeLayout) newTitilbar.findViewById(R.id.edit_layout);

        editLayout.setVisibility(View.VISIBLE);
        ib.setVisibility(View.VISIBLE);
        ib.setImageResource(R.mipmap.arrow_left);

        tv.setText("个人主页");
        mTitleContent.addView(newTitilbar);

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showActivity(UserInfoActivity.this, PersonInfoAty.class);

            }
        });

    }

    private void initFragment() {
        BaseFragment fragment = MyArticleFragment.newInstance(userId);
        BaseFragment fragment1 = UserRewardFragment.newInstance(userId);
        BaseFragment fragment2 = MyCircleFragment.newInstance(userId);
        BaseFragment fragment3 = MyActivityFragment.newInstance(userId);

        fragmentList.add(fragment);
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);

        scrollableLayout.getHelper().setCurrentScrollableContainer((ScrollableHelper.ScrollableContainer) fragment);
    }

    private void initIndicator() {
        for (int resId : TITLES) mTitles.add(getString(resId));
        tabPagerSearch.setCustomLayoutParams(4);
        tabPagerSearch.setLineFitFont(true);
        viewPager.setAdapter(infoAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager(), fragmentList, mTitles));
        viewPager.setOffscreenPageLimit(4);
        tabPagerSearch.setViewPager(viewPager);
        tabPagerSearch.notifyDataSetChanged();
        tabPagerSearch.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ScrollableHelper.ScrollableContainer container = null;
                switch (position) {
                    case 0:
                        container = (MyArticleFragment) fragmentList.get(position);
                        break;
                    case 1:
                        container = (UserRewardFragment) fragmentList.get(position);
                        break;
                    case 2:
                        container = (MyCircleFragment) fragmentList.get(position);
                        break;
                    case 3:
                        container = (MyActivityFragment) fragmentList.get(position);
                        break;
                }
                scrollableLayout.getHelper().setCurrentScrollableContainer(container);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        switch(baseEvent.type){
            case CommonCode.EventType.TYPE_USER_UPDATE:
                setData(null);
                break;
            case FriendPresenter1.FRIEND_EVENT:
                Toasty.info(mContext, mContext.getString(R.string.add_friend_success)).show();
                ((FriendPresenter1) friendPresenter).getFriendRequire(model.getCustId());
                break;
            case EventConstant.USER_AVATAR_REFUSE:
                //更新用户详情
                ((FriendPresenter1) friendPresenter).getFriendInfoDetail(Session.getUserId());
                break;
            case CommonCode.EventType.TYPE_BACKLOGIN_REFRESH:
                if(model != null && !StringUtil.isEmpty(model.getCustId())){
                    ((FriendPresenter1) friendPresenter).getFriendInfoDetail(model.getCustId());
                }
                break;
        }
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    public void refreshPage() {

    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (t instanceof ProveStatusBean) {
            ProveStatusBean data = (ProveStatusBean) t;

            if (data.getAuthStatus() == 1) {
                famousLayout.setVisibility(View.VISIBLE);
                userRole.setText(data.getTradeField());
            }else{
                famousLayout.setVisibility(View.GONE);
            }

        } else if (t instanceof FriendInformationBean) {
            model = (FriendInformationBean) t;

            //陌生人状态需要查询用户是否已经发送申请好友请求
            if (model.getRelation() == 0) {
                ((FriendPresenter1) friendPresenter).getFriendRequire(model.getCustId());
            }

            setData(model);
        } else if (t instanceof RequestFriendStatusBean) {
            if (t != null) {
                RequestFriendStatusBean requestBean = (RequestFriendStatusBean) t;
                if (requestBean.getStatus() == 0) {
                    if (requestBean.getIsRequire() == 1) {
                        addFriendBtn.setText("等待对方同意");
                        addFriendBtn.setClickable(false);
                        addFriendBtn.setBackgroundResource(R.drawable.shape_bg_gray_on);
                    } else {
                        addFriendBtn.setText("加好友");
                    }
                } else if (requestBean.getStatus() == 1) {
                    addFriendBtn.setText("聊天");
                } else if (requestBean.getStatus() == 2) {
                    addFriendBtn.setText("加好友");
                } else if (requestBean.getStatus() == 3) {
                    addFriendBtn.setText("加好友");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setData(FriendInformationBean model) {

        if (model == null) {
            if (userId.equals(Session.getUserId())) {

                if (Protect.checkLoadImageStatus(mContext)) {
                    Glide.with(this).load(Session.getUserPicUrl()).transform(new GlideCircleImage(this)).
                            placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(avatarImg);
                }

                nameTxt.setText(Session.getUserName());
                levelTxt.setText("Lv." + Session.getUserLevel());
                if (TextUtils.isEmpty(Session.getUser_desc())) {
                    signTxt.setText(getString(R.string.mine_sign_default));
                } else {
                    signTxt.setText(Session.getUser_desc());
                }

                addFriendLayout.setVisibility(View.GONE);
            }
        } else {
            if (Protect.checkLoadImageStatus(mContext)) {
                Glide.with(this).load(model.getCustImg()).transform(new GlideCircleImage(this)).
                        placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(avatarImg);
            }

            nameTxt.setText(model.getCustNname());
            levelTxt.setText("Lv." + model.getCustLevel());
            if (TextUtils.isEmpty(model.getCustDesc())) {
                signTxt.setText(getString(R.string.mine_sign_default));
            } else {
                signTxt.setText(model.getCustDesc());
            }

            addFriendLayout.setClickable(true);
            if (model.getRelation() == 0) {
                addFriendLayout.setVisibility(View.VISIBLE);
                //陌生人
                addFriendBtn.setText("加好友");
                EventBus.getDefault().post(new BaseEvent(CommonCode.EventType.TYPE_ADD_LAYOUT));

            } else {
                addFriendLayout.setVisibility(View.VISIBLE);
                //好友
                addFriendBtn.setText("聊天");
                EventBus.getDefault().post(new BaseEvent(CommonCode.EventType.TYPE_ADD_LAYOUT));

            }
        }
    }

    @OnClick(R.id.add_friend_btn)
    public void addFriendClick() {

        if(isLogin()){
            if (model != null && model.getRelation() == 0) {
                ((FriendPresenter1) friendPresenter).requireFriend(userId, "", 1, CommonCode.requireFriend.require_type_add);
            } else {
                if (checkLogin() && model != null)
                    SessionHelper.startP2PSession(this, model.getCustId());
            }
        }
    }

    @OnClick(R.id.user_avatar)
    void avatarClick(){
        List<String> imageList = new ArrayList<String>();
        if(imageList.size()>0){
            imageList.clear();
        }
        if (userId.equals(Session.getUserId())) {
            imageList.add(Session.getUserPicUrl());
        }else{
            if (model.getCustImg() != null && !TextUtils.isEmpty(model.getCustImg())) {
                imageList.add(model.getCustImg());
            } else {
                imageList.add("http://i1.piimg.com/4851/9f9fd766410b0d94.png");
            }
        }

        ImagePagerActivity.imageSize = new ImageSize(avatarImg
                .getMeasuredWidth(), avatarImg
                .getMeasuredHeight());
        ImagePagerActivity.isLocation = false;
        ImagePagerActivity.startImagePagerActivity(aty,
                imageList, 0);

    }

    private boolean checkLogin() {
        if (Session.getUserIsLogin() && NIMClient.getStatus() == StatusCode.LOGINED) {
            return true;
        } else {
            Toasty.info(mContext, getString(R.string.im_link_error_hint)).show();
            return false;
        }
    }


}
