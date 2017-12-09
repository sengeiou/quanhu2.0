package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.rz.circled.R;
import com.rz.circled.adapter.ContactsAdp;
import com.rz.circled.event.EventConstant;
import com.rz.circled.presenter.impl.FriendPresenter1;
import com.rz.circled.widget.SideBar;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.bean.FriendInformationBean;
import com.rz.httpapi.bean.FriendRequireModel;
import com.yryz.yunxinim.main.activity.SystemMessageActivity;
import com.yryz.yunxinim.main.activity.TeamListActivity;
import com.yryz.yunxinim.session.SessionHelper;
import com.yryz.yunxinim.uikit.contact.core.item.ItemTypes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 好友列表
 */
public class ContactsAty extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    /**
     * 内容展示
     */
    @BindView(R.id.id_content_listv)
    ListView mListview;

    /**
     * 右侧字母导航栏
     */

    @BindView(R.id.id_sidrbar)
    SideBar mSidebar;

    /**
     * 字母提醒框
     */
    @BindView(R.id.id_letter_dialog)
    TextView mTxtDialog;
    @BindView(R.id.id_search_key)
    TextView tvSearch;
    /**
     * 空布局
     */
    @BindView(R.id.layout_none)
    View mLayoutNone;

    @BindView(R.id.id_search_key_rela2)
    View mLayoutSearch2;

    @BindView(R.id.id_search_key_rela1)
    View mLayoutSearch1;

    /**
     * 头布局
     */
    private View mheadView;

    /**
     * 关注我的消息数目提醒
     */
    private TextView mTxtFollowNum;
    /**
     * 群验证消息数目提醒
     */
    private TextView mTvVerifyNum;

    private List<FriendInformationBean> mSaveAllFriends = new ArrayList<>();

    private ContactsAdp mContactsAdp;

    private FriendPresenter1 presenter;
    private boolean imChat;


    public static void startActivity(Context context, boolean imChat) {
        Intent intent = new Intent(context, ContactsAty.class);
        intent.putExtra(IntentKey.EXTRA_BOOLEAN, imChat);
        context.startActivity(intent);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_contacts, null);
    }

    @Override
    public void initPresenter() {
        presenter = new FriendPresenter1();
        presenter.attachView(this);
    }

    @Override
    public void initView() {
        imChat = getIntent().getBooleanExtra(IntentKey.EXTRA_BOOLEAN, false);
        setTitleText(imChat ? getString(R.string.select_firend) : getString(R.string.mine_my_contacts));
        setTitleRightText(R.string.add_friends);
        setTitleRightListener(this);
        tvSearch.setText(getString(R.string.search_key1));
        mLayoutSearch1.setVisibility(View.GONE);
        mLayoutSearch2.setVisibility(View.VISIBLE);
        initHeadView();
        mLayoutSearch2.setOnClickListener(this);
        mListview.addHeaderView(mheadView);
        mListview.setDividerHeight(0);
        mListview.setAdapter(mContactsAdp = new ContactsAdp(aty, mSaveAllFriends, R.layout.adp_contacts));
        mListview.setOnItemClickListener(this);
        if (!Session.isNeedTeam()) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.px410), 0, 0);
            mLayoutNone.setLayoutParams(lp);
        } else {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.px900), 0, 0);
            mLayoutNone.setLayoutParams(lp);
        }
        setFocusNum(Session.getUserFocusNum());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(BaseEvent event) {
        if (FriendPresenter1.FRIEND_APPLY_EVENT == event.getType() || FriendPresenter1.CANCEL_FRIEND_EVENT == event.getType()) {
            Log.e("tag", "好友列表更新并缓存");
            presenter.getCacheFriends(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(IntentCode.Contacts.Contacts_RESULT_CODE);
        finish();
    }

    /**
     * 加载头布局
     */
    private void initHeadView() {
        mheadView = LayoutInflater.from(aty).inflate(R.layout.head_contacts, null);
        if (Session.isNeedTeam()) {
            mheadView.findViewById(R.id.line_need).setVisibility(View.VISIBLE);
        } else {
            mheadView.findViewById(R.id.line_need).setVisibility(View.GONE);
        }
        mheadView.findViewById(R.id.id_follow_me_rela).setOnClickListener(this);
        mheadView.findViewById(R.id.id_my_follow_txt).setOnClickListener(this);
        mheadView.findViewById(R.id.id_group_chat_rela).setOnClickListener(this);
        mheadView.findViewById(R.id.id_group_discuss).setOnClickListener(this);
        mheadView.findViewById(R.id.id_verify_message_rela).setOnClickListener(this);
        mheadView.findViewById(R.id.id_black_name_txt).setOnClickListener(this);

        mTxtFollowNum = (TextView) mheadView.findViewById(R.id.id_remind_num_follow_txt);
        mTvVerifyNum = (TextView) mheadView.findViewById(R.id.id_remind_num_verify_message_txt);

        // 设置右侧触摸监听
        mSidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mContactsAdp.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListview.setSelection(position);
                }
            }
        });
        mSidebar.setTextView(mTxtDialog);
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "reloadWhenDataChanged: contacts onresume");
        //缓存部分新的好友列表，以便进入新的好友列表没有卡顿
        presenter.requestRequireList(false);
        presenter.getCacheFriends(false);
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        mLayoutNone.setVisibility(View.GONE);

        if (null == t)
            return;

        showUnreadMsg();

        mSaveAllFriends.clear();
        mSaveAllFriends.addAll((List<FriendInformationBean>) t);
        mContactsAdp.notifyDataSetChanged();
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);

        if (null == t)
            return;

        List<FriendRequireModel> infos = (List<FriendRequireModel>) t;
        EntityCache<FriendRequireModel> friendRequireModelEntityCache = new EntityCache<>(aty, FriendRequireModel.class);
        friendRequireModelEntityCache.putListEntity(infos);
    }

    private void updateUnreadNum(int unreadCount) {
        if (unreadCount > 0) {
            mTvVerifyNum.setVisibility(View.VISIBLE);
            mTvVerifyNum.setText("" + unreadCount);
        } else {
            mTvVerifyNum.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        super.onLoadingStatus(loadingStatus, string);
        mLayoutNone.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //关键字搜索
            case R.id.id_search_key_rela2:
                Bundle bundle = new Bundle();
                bundle.putInt("type", Type.custType_3);
                showActivity(aty, ContactsSearchAty.class, bundle);
                break;
            //关注我的
            case R.id.id_follow_me_rela:
                Session.setUserFocusNum("");
                setFocusNum("");
                showActivity(aty, FollowMeActivity.class);
                EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_UNREAD_CHANGE));
                break;
            //我关注的
            case R.id.id_my_follow_txt:
                if (checkLogin()) {
                    return;
                }
//                showActivity(aty, MyFollowAty.class);
                break;
            //群聊
            case R.id.id_group_chat_rela:
                if (checkLogin()) {
                    return;
                }
                TeamListActivity.start(getApplicationContext(), ItemTypes.TEAMS.ADVANCED_TEAM);
                break;
            //讨论组
            case R.id.id_group_discuss:
                if (checkLogin()) {
                    return;
                }
                TeamListActivity.start(getApplicationContext(), ItemTypes.TEAMS.NORMAL_TEAM);
                break;
            //验证消息
            case R.id.id_verify_message_rela:
                if (checkLogin()) {
                    return;
                }
                SystemMessageActivity.start(getApplicationContext(), true);
                break;
            //黑名单
            case R.id.id_black_name_txt:
                if (checkLogin()) {
                    return;
                }
//                showActivity(aty, BlackListAty.class);
                break;
            //添加好友
            case R.id.tv_base_title_right:
                showActivity(aty, AddContactsActivity.class);
                break;
            default:
                break;
        }
    }

    private boolean checkLogin() {
        if (!Session.getUserIsLogin() || NIMClient.getStatus() != StatusCode.LOGINED) {
            Toasty.info(mContext, getString(R.string.im_link_error_hint)).show();
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i - 1 < 0) {
            return;
        }
        FriendInformationBean item = mContactsAdp.getItem(i - 1);

        /**
         * 进入好友详情
         */
        if (i > 0) {
            if (item != null) {
                if (!imChat)
                    UserInfoActivity.newFrindInfo(aty, item.getCustId());
                else if (!checkLogin()) {
                    SessionHelper.startP2PSession(this, item.getCustId());
                }
            }
        }
    }

    public void setFocusNum(String focusNum) {
        if (!StringUtils.isEmpty(focusNum)) {
            mTxtFollowNum.setVisibility(View.VISIBLE);
            long num = Long.parseLong(focusNum);
            mTxtFollowNum.setText(num > 99 ? "99+" : (num + ""));
        } else {
            mTxtFollowNum.setVisibility(View.GONE);
        }
    }

    private void showUnreadMsg() {
        List<SystemMessageType> types = new ArrayList<>();
        types.add(SystemMessageType.ApplyJoinTeam);
        types.add(SystemMessageType.DeclineTeamInvite);
        types.add(SystemMessageType.RejectTeamApply);
        types.add(SystemMessageType.TeamInvite);
        types.add(SystemMessageType.undefined);
        int unreadCount = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountByType(types);
        updateUnreadNum(unreadCount);
    }

    @Override
    public void refreshPage() {

    }
}
