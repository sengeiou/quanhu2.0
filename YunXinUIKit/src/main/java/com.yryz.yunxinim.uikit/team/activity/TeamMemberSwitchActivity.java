package com.yryz.yunxinim.uikit.team.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.team.model.TeamMember;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.R;
import com.yryz.yunxinim.uikit.cache.SimpleCallback;
import com.yryz.yunxinim.uikit.cache.TeamDataCache;
import com.yryz.yunxinim.uikit.common.activity.UI;
import com.yryz.yunxinim.uikit.common.adapter.TAdapterDelegate;
import com.yryz.yunxinim.uikit.common.adapter.TViewHolder;
import com.yryz.yunxinim.uikit.common.ui.widget.ClearableEditTextWithIcon;
import com.yryz.yunxinim.uikit.model.ToolBarOptions;
import com.yryz.yunxinim.uikit.team.adapter.TeamMemberAdapter;
import com.yryz.yunxinim.uikit.team.adapter.TeamMemberAdapter.TeamMemberItem;
import com.yryz.yunxinim.uikit.team.adapter.TeamMemberSwitchAdapter;
import com.yryz.yunxinim.uikit.team.helper.TeamHelper;
import com.yryz.yunxinim.uikit.team.ui.TeamInfoListView;
import com.yryz.yunxinim.uikit.team.viewholder.TeamMemberSwitchHolder;
import com.yryz.yunxinim.uikit.uinfo.UserInfoHelper;
import com.yryz.yunxinim.uikit.uinfo.UserInfoObservable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 群成员选择列表
 * Created by hzxuwen on 2015/3/17.
 */
public class TeamMemberSwitchActivity extends UI implements TAdapterDelegate {

    // constant
    private static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_DATA = "EXTRA_DATA";

    private ClearableEditTextWithIcon searchEditText;

    // data source
    private String teamId;
    private List<TeamMember> members;
    private TeamMemberSwitchAdapter adapter;
    private List<String> memberAccounts;
    private List<TeamMemberItem> dataSource;
    private List<TeamMemberItem> dataCacheSource;

    private UserInfoObservable.UserInfoObserver userInfoObserver;

    public static void startActivityForResult(Activity context, String tid, int resCode) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, tid);
        intent.setClass(context, TeamMemberSwitchActivity.class);
        context.startActivityForResult(intent, resCode);
    }

    /**
     * *********************************lifeCycle*******************************************
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_team_member_listview_layout);

        ToolBarOptions options = new ToolBarOptions();
        options.titleId = R.string.team_member_switch;
        setToolBar(R.id.toolbar, options);

        parseIntentData();
        initAdapter();
        findViews();
        registerUserInfoChangedObserver(true);
        requestData();
    }

    @Override
    protected void onDestroy() {
        registerUserInfoChangedObserver(false);

        super.onDestroy();
    }

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_ID);
    }

    private void findViews() {
        TeamInfoListView teamInfoListView = (TeamInfoListView) findViewById(R.id.team_member_lv);
        teamInfoListView.setSelector(R.color.transparent);
        teamInfoListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        teamInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("account", dataSource.get(position).getAccount());
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        teamInfoListView.setAdapter(adapter);

        searchEditText = (ClearableEditTextWithIcon) findViewById(R.id.team_search_edittext);
        searchEditText.setDeleteImage(R.drawable.nim_grey_delete_icon);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    queryTeamById();
                    return true;
                }
                return false;
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(searchEditText.getText().toString().trim())) {
                    updateTeamMemberDataSource();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        int inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE;
        searchEditText.setInputType(inputType);
    }

    private void initAdapter() {
        memberAccounts = new ArrayList<>();
        members = new ArrayList<>();
        dataSource = new ArrayList<>();
        dataCacheSource = new ArrayList<>();
        adapter = new TeamMemberSwitchAdapter(this, dataSource, this);
    }

    private void updateTeamMember(final List<TeamMember> members) {
        if (members != null && members.isEmpty()) {
            return;
        }

        addTeamMembers(members, true);
    }

    private void addTeamMembers(final List<TeamMember> m, boolean clear) {
        if (m == null || m.isEmpty()) {
            return;
        }

        if (clear) {
            this.members.clear();
            this.memberAccounts.clear();
        }

        // add
        if (this.members.isEmpty()) {
            this.members.addAll(m);
        } else {
            for (TeamMember tm : m) {
                if (!this.memberAccounts.contains(tm.getAccount())) {
                    this.members.add(tm);
                }
            }
        }

        // sort
        Collections.sort(this.members, TeamHelper.teamMemberComparator);

        // accounts, manager, creator
        this.memberAccounts.clear();
        for (TeamMember tm : members) {
            if (tm.getAccount().equals(NimUIKit.getAccount())) {
                continue;
            }
            this.memberAccounts.add(tm.getAccount());
        }

        updateTeamMemberDataSource();
    }

    private void updateTeamMemberDataSource() {
        if (members.size() <= 0) {
            return;
        }

        dataSource.clear();
        dataCacheSource.clear();

        // member item
        for (String account : memberAccounts) {
            dataSource.add(new TeamMemberItem(TeamMemberAdapter.TeamMemberItemTag
                    .NORMAL, teamId, account, null));
            dataCacheSource.add(new TeamMemberItem(TeamMemberAdapter.TeamMemberItemTag
                    .NORMAL, teamId, account, null));
        }
        // refresh
        adapter.notifyDataSetChanged();
    }

    private void queryTeamById() {
        String query = searchEditText.getText().toString().trim();
        if (TextUtils.isEmpty(query)) {
            requestData();
            Toast.makeText(getApplicationContext(), R.string.not_allow_empty, Toast.LENGTH_SHORT).show();
        } else {
            dataSource.clear();
            for (TeamMemberItem teamMemberItem : dataCacheSource) {
                if (TeamDataCache.getInstance().getTeamMemberDisplayName(teamMemberItem.getTid(), teamMemberItem.getAccount()).contains(query)) {
                    dataSource.add(teamMemberItem);
                }
            }
            // refresh
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * ******************************加载数据*******************************
     */
    private void requestData() {
        TeamDataCache.getInstance().fetchTeamMemberList(teamId, new SimpleCallback<List<TeamMember>>() {
            @Override
            public void onResult(boolean success, List<TeamMember> members) {
                if (success && members != null && !members.isEmpty()) {
                    updateTeamMember(members);
                }
            }
        });
    }

    /**
     * ************************ TAdapterDelegate **************************
     */

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        return TeamMemberSwitchHolder.class;
    }

    @Override
    public boolean enabled(int position) {
        return true;
    }

    private void registerUserInfoChangedObserver(boolean register) {
        if (register) {
            if (userInfoObserver == null) {
                userInfoObserver = new UserInfoObservable.UserInfoObserver() {
                    @Override
                    public void onUserInfoChanged(List<String> accounts) {
                        adapter.notifyDataSetChanged();
                    }
                };
            }
            UserInfoHelper.registerObserver(userInfoObserver);
        } else {
            UserInfoHelper.unregisterObserver(userInfoObserver);
        }
    }
}
