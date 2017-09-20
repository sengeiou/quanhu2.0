package com.rz.circled.ui.activity;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.rz.circled.R;
import com.rz.circled.adapter.ContactsAdp;
import com.rz.circled.modle.ShareModel;
import com.rz.circled.presenter.impl.FriendPresenter1;
import com.rz.circled.ui.view.WorkImShareDialog;
import com.rz.circled.widget.CommomUtils;
import com.rz.circled.widget.SideBar;
import com.rz.circled.widget.pinyin.CharacterParser;
import com.rz.circled.widget.pinyin.SearchPingyinEngine;
import com.rz.common.constant.IntentCode;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.FriendInformationBean;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.common.ui.widget.ClearableEditTextWithIcon;
import com.yryz.yunxinim.uikit.uinfo.UserInfoHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by rzw2 on 2017/1/12.
 */

public class ShareFriendsListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String EXTRA_DATA_ITEM_DATA = "EXTRA_DATA_ITEM_DATA";

    /**
     * 内容展示
     */
    @BindView(R.id.contact_list_view)
    ListView mListview;

    /**
     * 搜索输入
     */
    @BindView(R.id.team_search_edittext)
    ClearableEditTextWithIcon searchEditText;

    /**
     * 右侧字母导航栏
     */

    @BindView(R.id.id_nim_sidrbar)
    SideBar mSidebar;
    /**
     * 字母提醒框
     */
    @BindView(R.id.id_nim_letter_dialog)
    TextView mTxtDialog;
    /**
     * 空布局
     */
    @BindView(R.id.layout_none)
    View mLayoutNone;

    CharacterParser finder = CharacterParser.getInstance();

    ContactsAdp mContactsAdp;

    public static final int INVITE_REQUEST_CODE = 117;
    /**
     * 当前类型所有数据集合以及搜索的过滤集合
     */
    private List<FriendInformationBean> searchFriends = new ArrayList<>();

    private List<FriendInformationBean> mSaveAllFriends = new ArrayList<>();
    private FriendPresenter1 presenter;

    public static final void start(Context context, ShareModel shareModel) {
        Intent intent = new Intent();
        intent.setClass(context, ShareFriendsListActivity.class);
        intent.putExtra(EXTRA_DATA_ITEM_DATA, shareModel);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_nim_contacts, null);
    }

    @Override
    public void initPresenter() {
        presenter = new FriendPresenter1();
    }

    @Override
    public void initView() {
        ShareModel model = (ShareModel) getIntent().getSerializableExtra(EXTRA_DATA_ITEM_DATA);
//        if (INVITE_REQUEST_CODE == model.fromPage) {
//            setTitleText("邀请好友转发", null);
//        } else {
//            setTitleText("分享到悠然聊天", null);
//        }
        setTitleText(getString(R.string.select_firend));
        mContactsAdp = new ContactsAdp(aty, mSaveAllFriends, R.layout.adp_contacts);
        mListview.setAdapter(mContactsAdp);
        mListview.setOnItemClickListener(this);

        searchEditText.setDeleteImage(com.yryz.yunxinim.R.drawable.nim_grey_delete_icon);
        searchEditText.setHint(R.string.input_nick);

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
        //增加侧边搜索框
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
    public void onBackPressed() {
        super.onBackPressed();
        setResult(IntentCode.Contacts.Contacts_RESULT_CODE);
        finish();
    }

    @Override
    public void initData() {
        /**
         * 拉取最新的好友列表并同步到最新，并且只有好友列表才有初次loading
         */
        Log.e(TAG, "reloadWhenDataChanged: share");
        ((FriendPresenter1) presenter).getCacheFriends(false);
    }

    private void matchSearchResult(String str) {
        if (!StringUtils.isEmpty(str)) {
            searchFriends.addAll(SearchPingyinEngine.searchGroup(finder.hasLetter(str) ? str.toUpperCase() : str, mSaveAllFriends));
        }

        if (searchFriends.size() == 0) {
            CommomUtils.showNoDataTip(aty);
            return;
        }

        mContactsAdp.setData(searchFriends);
        mContactsAdp.notifyDataSetChanged();
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        mLayoutNone.setVisibility(View.GONE);

        mSaveAllFriends.clear();
        mSaveAllFriends.addAll((List<FriendInformationBean>) t);
        mContactsAdp.notifyDataSetChanged();
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        super.onLoadingStatus(loadingStatus, string);
        mLayoutNone.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i < 0) {
            return;
        }

        FriendInformationBean item = (FriendInformationBean) mContactsAdp.getItem(i);
        if (item == null) {
            return;
        }

        if (NimUIKit.getContactEventListener() != null) {
            ShareModel model = (ShareModel) getIntent().getSerializableExtra(EXTRA_DATA_ITEM_DATA);
            // 判断是否需要ImageLoader加载
            final UserInfoProvider.UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(item.getCustId());
            if (userInfo != null) {
                if (userInfo.getAvatar() != null)
                    model.setUserAvater(userInfo.getAvatar());
                model.setUserName(UserInfoHelper.getUserTitleName(item.getCustId(), SessionTypeEnum.P2P));
                if (userInfo.getAccount() != null)
                    model.setUserId(userInfo.getAccount());
            }

            model.setTypeEnum(SessionTypeEnum.P2P);
            new WorkImShareDialog(ShareFriendsListActivity.this, model).show();
        }
    }
}
