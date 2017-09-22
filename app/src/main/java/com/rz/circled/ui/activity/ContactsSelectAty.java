package com.rz.circled.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rz.circled.R;
import com.rz.circled.adapter.ContactsSelectAdp;
import com.rz.circled.presenter.impl.FriendPresenter1;
import com.rz.circled.presenter.impl.OpusGeneralPresenter;
import com.rz.circled.widget.CommomUtils;
import com.rz.circled.widget.SideBar;
import com.rz.circled.widget.pinyin.CharacterParser;
import com.rz.circled.widget.pinyin.SearchPingyinEngine;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.FriendInformationBean;
import com.yryz.yunxinim.uikit.common.ui.widget.ClearableEditTextWithIcon;
import com.yryz.yunxinim.uikit.common.util.sys.SoftKeyboardUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 好友选择列表
 */
public class ContactsSelectAty extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String EXTRA_DATA = "EXTRA_DATA"; // 请求数据：Option
    public static final String EXTRA_APPID = "EXTRA_APPID"; // 请求数据：APPID
    public static final String RESULT_DATA = "RESULT_DATA"; // 返回结果

    /**
     * 内容展示
     */
    @BindView(R.id.id_content_listv)
    ListView mListview;

    /**
     * 搜索输入
     */
    @BindView(R.id.team_search_edittext)
    ClearableEditTextWithIcon searchEditText;

    /**
     * 搜索布局1
     */
    @BindView(R.id.id_search_key_rela1)
    View searchLayout1;

    /**
     * 搜索布局2
     */
    @BindView(R.id.id_search_key_rela2)
    View searchLayout2;
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

    /**
     * 空布局
     */
    @BindView(R.id.layout_none)
    View mLayoutNone;

    /**
     * 选择提交按钮
     */
    @BindView(R.id.btnSelect)
    Button mBtnSubmit;

    /**
     * 已选择人数
     */
    @BindView(R.id.tvSelectNum)
    TextView mTvSelectNum;

    /**
     * 选择布局
     */
    @BindView(R.id.contacts_select_submit_layout)
    View mLayoutSelect;

    ContactsSelectAdp mContactsAdp;
    OpusGeneralPresenter mPresenter;
    CharacterParser finder = CharacterParser.getInstance();

    private List<FriendInformationBean> mSaveAllFriends = new ArrayList<>();
    private List<FriendInformationBean> mSelectedFriends = new ArrayList<>();
    private ArrayList<String> mDisableFriends = new ArrayList<>();
    private ArrayList<String> mSelectFriends = new ArrayList<>();
    private FriendPresenter1 presenter;

    public static void startActivityForResult(Context context, ArrayList<String> mDisableFriends, int requestCode) {
        startActivityForResult(context, mDisableFriends, "", requestCode);
    }

    public static void startActivityForResult(Context context, ArrayList<String> mDisableFriends, String appId, int requestCode) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_DATA, mDisableFriends);
        if (!TextUtils.isEmpty(appId))
            intent.putExtra(EXTRA_APPID, appId);
        intent.setClass(context, ContactsSelectAty.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_contacts, null);
    }

    @Override
    public void initPresenter() {
        presenter = new FriendPresenter1();
        mPresenter = new OpusGeneralPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void initView() {
        setTitleText(R.string.invite_friend);
        mDisableFriends = getIntent().getStringArrayListExtra("EXTRA_DATA");
        mLayoutSelect.setVisibility(View.VISIBLE);
        searchLayout2.setVisibility(View.GONE);
        searchLayout1.setVisibility(View.VISIBLE);
        mSidebar.setVisibility(View.GONE);
        mContactsAdp = new ContactsSelectAdp(aty, R.layout.adp_contacts_select);
        mListview.setAdapter(mContactsAdp);
        mListview.setOnItemClickListener(this);
        mBtnSubmit.setOnClickListener(this);

        searchEditText.setDeleteImage(com.yryz.yunxinim.R.drawable.nim_grey_delete_icon);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    /**
                     *  这里还应该注意两次敲击时间间隔
                     *  短与多少毫秒就不去搜索
                     */
                    if (CountDownTimer.isFastClick(1000))
                        return true;

                    if (TextUtils.isEmpty(searchEditText.getText().toString())) {
                        mContactsAdp.setData(mSaveAllFriends);
                        mContactsAdp.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), com.yryz.yunxinim.R.string.not_allow_empty, Toast.LENGTH_SHORT).show();
                    } else {
                        matchSearchResult(searchEditText.getText().toString().trim() + "");
                    }
                    return true;
                }
                return false;
            }
        });

        int inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE;
        searchEditText.setInputType(inputType);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(BaseEvent event) {
        if (FriendPresenter1.FRIEND_EVENT.equals(event.info)) {
            Log.e("tag", "好友列表更新并缓存");
            presenter.getCacheFriends(false);
        }
    }

    private void matchSearchResult(String str) {
        mSelectedFriends.clear();

        if (!StringUtils.isEmpty(str)) {
            mSelectedFriends.addAll(SearchPingyinEngine.searchGroup(finder.hasLetter(str) ? str.toUpperCase() : str, mSaveAllFriends));
        }

        if (mSelectedFriends.size() == 0) {
            CommomUtils.showNoDataTip(aty);
            SoftKeyboardUtil.showKeyboard(aty, false);
            return;
        }

        mContactsAdp.setData(mSelectedFriends);
        mContactsAdp.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void initData() {
        if (getIntent().hasExtra(EXTRA_APPID)) {
            mPresenter.getCircleMember(getIntent().getExtras().getString(EXTRA_APPID));
        } else {
            /**
             * 拉取最新的好友列表并同步到最新，并且只有好友列表才有初次loading
             */
            Log.e(TAG, "reloadWhenDataChanged: contactselect");
            presenter.getCacheFriends(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);

        mLayoutNone.setVisibility(View.GONE);
        mSaveAllFriends.clear();
        mSaveAllFriends.addAll((List<FriendInformationBean>) t);

        if (null != mDisableFriends && mDisableFriends.size() > 0)
            for (String custId : mDisableFriends) {
                for (FriendInformationBean item : mSaveAllFriends) {
                    if (TextUtils.equals(item.getCustId(), custId)) {
                        item.setDisable(true);
                        break;
                    }
                }
            }

        mContactsAdp.notifyDataSetChanged();
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        super.onLoadingStatus(loadingStatus, string);
        mLayoutNone.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //关注我的
//            case R.id.id_follow_me_rela:
//                Session.setUserFocusNum("");
//                Intent focus = new Intent(MineFrg.MINEFRGFOCUS);
//                sendBroadcast(focus);
//                showActivity(aty, FollowMeAty.class);
//                break;
//            //我关注的
//            case R.id.id_my_follow_txt:
//                showActivity(aty, MyFollowAty.class);
//                break;
//            //群聊
//            case R.id.id_group_chat_rela:
//                TeamListActivity.start(getApplicationContext(), ItemTypes.TEAMS.ADVANCED_TEAM);
//                break;
//            //讨论组
//            case R.id.id_group_discuss:
//                TeamListActivity.start(getApplicationContext(), ItemTypes.TEAMS.NORMAL_TEAM);
//                break;
//            //验证消息
//            case R.id.id_verify_message_rela:
//                SystemMessageActivity.start(getApplicationContext());
//                break;
//            //黑名单
//            case R.id.id_black_name_txt:
//                showActivity(aty, BlackListAty.class);
//                break;
//            //添加好友
//            case R.id.titlebar_right_text:
//                showActivity(aty, AddContactsAty.class);
//                break;
            case R.id.btnSelect:
                Intent intent = new Intent();
                intent.putStringArrayListExtra(RESULT_DATA, mSelectFriends);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i < 0) {
            return;
        }
        FriendInformationBean item = (FriendInformationBean) mContactsAdp.getItem(i);
        if (item.isDisable())
            return;
        item.setSelect(!item.isSelect());

        if (mSelectFriends.contains(item.getCustId()))
            mSelectFriends.remove(item.getCustId());
        else
            mSelectFriends.add(item.getCustId());

        mTvSelectNum.setText(String.valueOf(mSelectFriends.size() + ""));

        mContactsAdp.notifyDataSetChanged();
    }

    @Override
    public void refreshPage() {

    }
}
