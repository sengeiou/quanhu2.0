package com.rz.circled.ui.activity;

import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.ContactsSearchAdapter;
import com.rz.circled.db.dao.FriendInformationDao;
import com.rz.circled.presenter.impl.FriendPresenter1;
import com.rz.circled.widget.CommomUtils;
import com.rz.circled.widget.pinyin.CharacterParser;
import com.rz.circled.widget.pinyin.SearchPingyinEngine;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.FriendInformationBean;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiayumo on 16/8/18.
 */
public class ContactsSearchAty extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, SwipyRefreshLayout.OnRefreshListener {

    @BindView(R.id.id_search_result_list)
    ListView mSearchList;
    @BindView(R.id.id_search_edit)
    EditText mSearchEdit;
    @BindView(R.id.id_clear)
    ImageView clearImg;
    @BindView(R.id.id_swipyRefreshLayout)
    SwipyRefreshLayout mSwipyRefreshLayout;

    private ContactsSearchAdapter mAdapter;
    private CharacterParser finder = CharacterParser.getInstance();
    private FriendInformationDao friendInformationDao;
    /**
     * 当前类型所有数据集合以及搜索的过滤集合
     */
    private List<FriendInformationBean> searchFriends = new ArrayList<>();

    private List<FriendInformationBean> localFriends = new ArrayList<>();

    private FriendPresenter1 presenter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.layout_contacts_search, null);
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (getIntent().getExtras().getInt("type", Type.custType_3) == Type.custType_5) {
            searchFriends.clear();
            searchFriends.addAll((List<FriendInformationBean>) t);

            if (searchFriends.size() == 0) {
                CommomUtils.showNoDataTip(aty);
            }

            mAdapter.notifyDataSetChanged();
            mSwipyRefreshLayout.setRefreshing(false);
        } else {
            /**c
             * 手机通讯录查询结果
             */
            localFriends.clear();
            localFriends.addAll((List<FriendInformationBean>) t);
        }
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new FriendPresenter1();
        presenter.attachView(this);
    }

    @Override
    public void initView() {
        int type = getIntent().getExtras().getInt("type", Type.custType_3);
        String title = getString(R.string.contacts_search_keywords);
        mSwipyRefreshLayout.setEnabled(false);
        switch (type) {
            case Type.custType_1:
                title = getString(R.string.myfocus);
                mSearchEdit.setHint(getString(R.string.search_key1));
                break;
            case Type.custType_3:
                title = getString(R.string.friend);
                break;
            case Type.custType_2:
                title = getString(R.string.new_friend);
                mSearchEdit.setHint(getString(R.string.search_key1));
                break;
            case Type.custType_0:
                title = getString(R.string.phone_contacts);
                mSearchEdit.setHint(getString(R.string.search_key1));
                break;
            case Type.custType_5:
                title = getString(R.string.qh_personal);
                mSearchEdit.setHint(getString(R.string.search_key1));
                mSwipyRefreshLayout.setEnabled(true);
                mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
                mSwipyRefreshLayout.setOnRefreshListener(this);
                break;
        }
        setTitleText(title);

        mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    /**
                     *  这里还应该注意两次敲击时间间隔
                     *  短与多少毫秒就不去搜索
                     */
                    if (CountDownTimer.isFastClick(1000))
                        return true;
                    String s = mSearchEdit.getText().toString().trim();
                    mAdapter.setKeyWord(finder.hasLetter(s) ? s.toUpperCase() : s);
                    if (getIntent().getExtras().getInt("type", Type.custType_3) == Type.custType_5) {
                        if (!TextUtils.isEmpty(s + "")) {
                            presenter.searchFriend(s + "", false);
                        } else {
                            searchFriends.clear();
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        matchSearchResult(s + "");
                    }

                    if (StringUtils.isEmpty(s)) {
                        clearImg.setVisibility(View.INVISIBLE);
                    } else {
                        clearImg.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                return false;
            }
        });

        int inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE;
        mSearchEdit.setInputType(inputType);

        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(mSearchEdit.getText().toString().trim())) {
                    searchFriends.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initData() {
        friendInformationDao = new FriendInformationDao(mContext);
        localFriends.clear();
        searchFriends.clear();
        int type = getIntent().getExtras().getInt("type", Type.custType_3);
        /**
         * 如果是好友或我关注的则直接查询数据库，如果不是则匹配联系人
         */
        if (type == Type.custType_0) {
            presenter.queryAndFilterContacts();
        } else if (type != Type.custType_5) {
            localFriends.addAll(friendInformationDao.queryForAll());
        }
        mSearchList.setAdapter(mAdapter = new ContactsSearchAdapter(mContext, R.layout.layout_contacts_search_item));
        mSearchList.setOnItemClickListener(this);
        mAdapter.setData(searchFriends);
    }

    private void matchSearchResult(String str) {

        searchFriends.clear();

        if (!StringUtils.isEmpty(str)) {
            searchFriends.addAll(SearchPingyinEngine.searchGroup(finder.hasLetter(str) ? str.toUpperCase() : str, localFriends));
        }

        if (searchFriends.size() == 0) {
            CommomUtils.showNoDataTip(aty);
        }
        mAdapter.setData(searchFriends);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.id_clear})
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_clear) {
            mSearchEdit.setText("");
            searchFriends.clear();
            mAdapter.setData(searchFriends);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int type = getIntent().getExtras().getInt("type", Type.custType_3);
        /**
         * 进入好友详情
         */
        FriendInformationBean item = mAdapter.getItem(i);
        if (item != null) {
            if (item.getRelation() == Type.relation_friend && type != Type.custType_0) {
                UserInfoActivity.newFrindInfo(aty, item.getCustId());
            } else {
                ((FriendPresenter1) presenter).requestSaveFriendByFriend(item);
            }
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        String keyWord = mSearchEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(keyWord))
            presenter.searchFriend(keyWord, direction != SwipyRefreshLayoutDirection.TOP);
        else
            mSwipyRefreshLayout.setRefreshing(false);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(final BaseEvent event) {
        if (FriendPresenter1.FRIEND_INVITE_EVENT == event.getType()) {
            finish();
        } else if (FriendPresenter1.FRIEND_CONTACTS_EVENT == event.getType()) {
//            FriendInfoAty.newFrindInfo(aty, ((FriendRelationModel) event.data).getCustId());
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void refreshPage() {

    }
}
