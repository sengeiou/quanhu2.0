package com.yryz.yunxinim.main.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.RequestCallback;
import com.yryz.yunxinim.DemoCache;
import com.yryz.yunxinim.R;
import com.yryz.yunxinim.session.SessionHelper;
import com.yryz.yunxinim.team.TeamCreateHelper;
import com.yryz.yunxinim.team.activity.AdvancedTeamJoinActivity;
import com.yryz.yunxinim.team.activity.AdvancedTeamSearchActivity;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.UserPreferences;
import com.yryz.yunxinim.uikit.cache.NimUserInfoCache;
import com.yryz.yunxinim.uikit.cache.TeamDataCache;
import com.yryz.yunxinim.uikit.common.activity.UI;
import com.yryz.yunxinim.uikit.common.ui.widget.ClearableEditTextWithIcon;
import com.yryz.yunxinim.uikit.contact.core.item.AbsContactItem;
import com.yryz.yunxinim.uikit.contact.core.item.ContactItem;
import com.yryz.yunxinim.uikit.contact.core.item.ItemTypes;
import com.yryz.yunxinim.uikit.contact.core.model.ContactDataAdapter;
import com.yryz.yunxinim.uikit.contact.core.model.ContactGroupStrategy;
import com.yryz.yunxinim.uikit.contact.core.provider.ContactDataProvider;
import com.yryz.yunxinim.uikit.contact.core.query.IContactDataProvider;
import com.yryz.yunxinim.uikit.contact.core.viewholder.ContactHolder;
import com.yryz.yunxinim.uikit.contact.core.viewholder.LabelHolder;
import com.yryz.yunxinim.uikit.contact_selector.activity.ContactSelectActivity;
import com.yryz.yunxinim.uikit.model.ToolBarOptions;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.yryz.yunxinim.uikit.team.helper.TeamHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 群列表(通讯录)
 * <p/>
 * Created by huangjun on 2015/4/21.
 */
public class TeamListActivity extends UI implements AdapterView.OnItemClickListener {

    private static final String EXTRA_DATA_ITEM_TYPES = "EXTRA_DATA_ITEM_TYPES";
    private static final int REQUEST_CODE_NORMAL = 1;
    private static final int REQUEST_CODE_ADVANCED = 2;

    private ContactDataAdapter adapter;

    private ListView lvContacts;

    private ClearableEditTextWithIcon searchEditText;

    private LinearLayout layoutGroupCreate;

    private TextView tvGroupCreate;

    private int itemType;

    public static final void start(Context context, int teamItemTypes) {
        Intent intent = new Intent();
        intent.setClass(context, TeamListActivity.class);
        intent.putExtra(EXTRA_DATA_ITEM_TYPES, teamItemTypes);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemType = getIntent().getIntExtra(EXTRA_DATA_ITEM_TYPES, ItemTypes.TEAMS.ADVANCED_TEAM);

        setContentView(R.layout.group_list_activity);

        ToolBarOptions options = new ToolBarOptions();
        options.titleId = itemType == ItemTypes.TEAMS.ADVANCED_TEAM ? R.string.advanced_team1 : R.string.normal_team;
        setToolBar(R.id.toolbar, options);

        searchEditText = (ClearableEditTextWithIcon) findViewById(R.id.team_search_edittext);
        searchEditText.setDeleteImage(R.drawable.nim_grey_delete_icon);
        String hint;
        hint = itemType == ItemTypes.TEAMS.ADVANCED_TEAM ? getResources().getString(R.string.team_search_mine_hint) : getResources().getString(R.string.team_normal_search_mine_hint);
        searchEditText.setHint(hint);

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

        int inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE;
        searchEditText.setInputType(inputType);

        tvGroupCreate = (TextView) findViewById(R.id.group_create_tv);
        tvGroupCreate.setText(itemType == ItemTypes.TEAMS.ADVANCED_TEAM ? "创建群" : "创建讨论组");
        findViewById(R.id.group_tip).setVisibility(View.VISIBLE);
        layoutGroupCreate = (LinearLayout) findViewById(R.id.group_create_layout);
        layoutGroupCreate.setVisibility(View.VISIBLE);
        layoutGroupCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.rz.yryz.FRIEND_SELECT_ACTION");
                intent.putStringArrayListExtra("EXTRA_DATA", new ArrayList<String>());
                startActivityForResult(intent, itemType == ItemTypes.TEAMS.ADVANCED_TEAM ? REQUEST_CODE_ADVANCED : REQUEST_CODE_NORMAL);
//                ContactSelectActivity.Option advancedOption = TeamHelper.getCreateContactSelectOption(null, 50);
//                NimUIKit.startContactSelect(TeamListActivity.this, advancedOption, itemType == ItemTypes.TEAMS.ADVANCED_TEAM ? REQUEST_CODE_ADVANCED : REQUEST_CODE_NORMAL);
            }
        });

        lvContacts = (ListView) findViewById(R.id.group_list);

        GroupStrategy groupStrategy = new GroupStrategy();
        IContactDataProvider dataProvider = new ContactDataProvider(itemType);

        adapter = new ContactDataAdapter(this, groupStrategy, dataProvider) {
            @Override
            protected List<AbsContactItem> onNonDataItems() {
                return null;
            }

            @Override
            protected void onPreReady() {
            }

            @Override
            protected void onPostLoad(boolean empty, String queryText, boolean all) {
            }
        };
        adapter.addViewHolder(ItemTypes.LABEL, LabelHolder.class);
        adapter.addViewHolder(ItemTypes.TEAM, ContactHolder.class);

        lvContacts.setAdapter(adapter);
        lvContacts.setOnItemClickListener(this);
        lvContacts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                showKeyboard(false);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

        // load data
        int count = NIMClient.getService(TeamService.class).queryTeamCountByTypeBlock(itemType == ItemTypes.TEAMS
                .ADVANCED_TEAM ? TeamTypeEnum.Advanced : TeamTypeEnum.Normal);
        if (count == 0) {
            if (itemType == ItemTypes.TEAMS.ADVANCED_TEAM) {
                Toast.makeText(TeamListActivity.this, R.string.no_team, Toast.LENGTH_SHORT).show();
            } else if (itemType == ItemTypes.TEAMS.NORMAL_TEAM) {
                Toast.makeText(TeamListActivity.this, R.string.no_normal_team, Toast.LENGTH_SHORT).show();
            }
        }

        adapter.load(true);

        registerTeamUpdateObserver(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != adapter)
            adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        registerTeamUpdateObserver(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AbsContactItem item = (AbsContactItem) adapter.getItem(position);
        switch (item.getItemType()) {
            case ItemTypes.TEAM:
                searchEditText.setText("");
                SessionHelper.startTeamSession(this, ((ContactItem) item).getContact().getContactId());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_NORMAL) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                if (selected != null && !selected.isEmpty()) {
                    TeamCreateHelper.createNormalTeam(TeamListActivity.this, selected, false, null, NimUserInfoCache.getInstance().getUserDisplayName(DemoCache.getAccount()) + "的讨论组");
                } else {
                    Toast.makeText(TeamListActivity.this, "请选择至少一个联系人！", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_CODE_ADVANCED) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                TeamCreateHelper.createAdvancedTeam(TeamListActivity.this, selected, NimUserInfoCache.getInstance().getUserDisplayName(DemoCache.getAccount()) + "的群");
            }
        }

    }

    private void registerTeamUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataChangedObserver);
        }
    }

    TeamDataCache.TeamDataChangedObserver teamDataChangedObserver = new TeamDataCache.TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            adapter.load(true);
        }

        @Override
        public void onRemoveTeam(Team team) {
            adapter.load(true);
        }
    };

    private void queryTeamById() {
        String query = searchEditText.getText().toString().trim();
        if (TextUtils.isEmpty(query)) {
            adapter.load(true);
            Toast.makeText(getApplicationContext(), R.string.not_allow_empty, Toast.LENGTH_SHORT).show();
        } else {
            adapter.query(query);
        }
    }

    private static class GroupStrategy extends ContactGroupStrategy {
        GroupStrategy() {
            add(ContactGroupStrategy.GROUP_NULL, 0, ""); // 默认分组
        }

        @Override
        public String belongs(AbsContactItem item) {
            switch (item.getItemType()) {
                case ItemTypes.TEAM:
                    return GROUP_NULL;
                default:
                    return null;
            }
        }
    }

}
