package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.rz.circled.R;
import com.rz.circled.modle.ShareModel;
import com.rz.circled.ui.view.WorkImShareDialog;
import com.rz.common.ui.activity.BaseActivity;
import com.yryz.yunxinim.uikit.cache.TeamDataCache;
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
import com.yryz.yunxinim.uikit.model.CircleTeamModel;
import com.yryz.yunxinim.uikit.model.ToolBarOptions;

import java.util.List;

/**
 * Created by rzw2 on 2017/1/12.
 */

public class ShareTeamListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String EXTRA_DATA_ITEM_TYPES = "EXTRA_DATA_ITEM_TYPES";
    private static final String EXTRA_DATA_ITEM_DATA = "EXTRA_DATA_ITEM_DATA";

    private ContactDataAdapter adapter;

    private ListView lvContacts;

    private ClearableEditTextWithIcon searchEditText;

    private LinearLayout mNoDataLayout;

    private int itemType;

    private ShareModel model;

    int start = 0;

    private List<CircleTeamModel> circleData;

    public static final void start(Context context, int teamItemTypes) {
        Intent intent = new Intent();
        intent.setClass(context, ShareTeamListActivity.class);
        intent.putExtra(EXTRA_DATA_ITEM_TYPES, teamItemTypes);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static final void start(Context context, int teamItemTypes, ShareModel shareModel) {
        Intent intent = new Intent();
        intent.setClass(context, ShareTeamListActivity.class);
        intent.putExtra(EXTRA_DATA_ITEM_TYPES, teamItemTypes);
        intent.putExtra(EXTRA_DATA_ITEM_DATA, shareModel);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.group_list_activity, null);
    }

    @Override
    public void initView() {
        model = (ShareModel) getIntent().getSerializableExtra(EXTRA_DATA_ITEM_DATA);
        itemType = getIntent().getIntExtra(EXTRA_DATA_ITEM_TYPES, ItemTypes.TEAMS.ADVANCED_TEAM);

        ToolBarOptions options = new ToolBarOptions();
        options.titleId = itemType == ItemTypes.TEAMS.ADVANCED_TEAM ? com.yryz.yunxinim.R.string.advanced_team : com.yryz.yunxinim.R.string.normal_team;

        findViewById(R.id.app_bar_layout).setVisibility(View.GONE);
        mNoDataLayout = (LinearLayout) findViewById(R.id.line);

        searchEditText = (ClearableEditTextWithIcon) findViewById(com.yryz.yunxinim.R.id.team_search_edittext);
        searchEditText.setHint(R.string.keyword_search);
        if (itemType == ItemTypes.TEAMS.ADVANCED_TEAM) {
            setTitleText(getString(R.string.select_group));
            searchEditText.setHint(R.string.team_search);
        } else if (itemType == ItemTypes.TEAMS.NORMAL_TEAM) {
            setTitleText(getString(R.string.select_taolun));
            searchEditText.setHint(R.string.taolun_search);
            ((TextView) findViewById(com.yryz.yunxinim.R.id.group_tip)).setText(getString(R.string.select_taolun));
        }
        searchEditText.setDeleteImage(com.yryz.yunxinim.R.drawable.nim_grey_delete_icon);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    String query = searchEditText.getText().toString().trim();
                    if (TextUtils.isEmpty(query)) {
                        adapter.load(true);
                        Toast.makeText(getApplicationContext(), com.yryz.yunxinim.R.string.not_allow_empty, Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.query(query);
                    }
                    return true;
                }
                return false;
            }
        });

        int inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE;
        searchEditText.setInputType(inputType);

        lvContacts = (ListView) findViewById(com.yryz.yunxinim.R.id.group_list);

        findViewById(com.yryz.yunxinim.R.id.divider).setVisibility(View.VISIBLE);

        findViewById(com.yryz.yunxinim.R.id.group_tip).setVisibility(View.VISIBLE);

        ShareTeamListActivity.GroupStrategy groupStrategy = new ShareTeamListActivity.GroupStrategy();
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
                if (empty && !TextUtils.isEmpty(queryText) && all) {
                    if (mNoDataLayout.getVisibility() == View.GONE)
                        mNoDataLayout.setVisibility(View.VISIBLE);
                } else {
                    if (mNoDataLayout.getVisibility() == View.VISIBLE)
                        mNoDataLayout.setVisibility(View.GONE);
                }
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
                Toast.makeText(getApplicationContext(), com.yryz.yunxinim.R.string.no_team, Toast.LENGTH_SHORT).show();
            } else if (itemType == ItemTypes.TEAMS.NORMAL_TEAM) {
                Toast.makeText(getApplicationContext(), com.yryz.yunxinim.R.string.no_normal_team, Toast.LENGTH_SHORT).show();
            }
        }

        adapter.load(true);

        registerTeamUpdateObserver(true);
    }

    @Override
    public void initData() {

    }

    //    @Override
//    public void initData() {
//        if (!TextUtils.isEmpty(model.getAppId())) {
//            Http.getNewService(ImService.class)
//                    .getTeamInCircle(model.getAppId(), NimUIKit.getAccount(), start, Constants.PAGESIZE_MAX)
//                    .enqueue(new BaseCallback<ResponseData<List<CircleTeamModel>>>() {
//                                 @Override
//                                 public void onResponse(Call<ResponseData<List<CircleTeamModel>>> call, Response<ResponseData<List<CircleTeamModel>>> response) {
//                                     super.onResponse(call, response);
//                                     if (response.isSuccessful() && response.body().isSuccessful()) {
//                                         List<CircleTeamModel> datas = response.body().getData();
//                                         start += 1;
//                                         ArrayList<String> filterTeams = new ArrayList<>();
//                                         if (datas.size() > 0) {
//                                             for (CircleTeamModel item : datas) {
//                                                 filterTeams.add(item.getTid());
//                                             }
//                                         }
//                                         adapter.setFilter(new ContactTeamIdFilter(filterTeams));
//                                         adapter.load(true);
//                                     }
//                                 }
//
//                                 @Override
//                                 public void onFailure(Call<ResponseData<List<CircleTeamModel>>> call, Throwable t) {
//                                     super.onFailure(call, t);
//                                 }
//                             }
//
//                    );
//        } else {
//            String tids = null;
//            List<Team> allAdvancedTeams = TeamDataCache.getInstance().getAllAdvancedTeams();
//            for (int i = 0, allAdvancedTeamsSize = allAdvancedTeams.size(); i < allAdvancedTeamsSize; i++) {
//                if (i > 0)
//                    tids += ",";
//                Team item = allAdvancedTeams.get(i);
//                tids += item.getId();
//            }
//            Http.getNewService(ImService.class).getAllTeam(tids).enqueue(new BaseCallback<ResponseData<List<CircleTeamModel>>>() {
//                @Override
//                public void onResponse(Call<ResponseData<List<CircleTeamModel>>> call, Response<ResponseData<List<CircleTeamModel>>> response) {
//                    super.onResponse(call, response);
//                    if (response.isSuccessful() && response.body().isSuccessful() && response.body().getData().size() > 0) {
//                        List<CircleTeamModel> data = response.body().getData();
//                        ArrayList<String> filterTeams = new ArrayList<>();
//                        for (CircleTeamModel item : data) {
//                            if (!TextUtils.isEmpty(item.getAppId())) {
//                                filterTeams.add(item.getTid());
//                            }
//                        }
//                        adapter.setFilter(new ContactIdFilter(filterTeams));
//                    }
//                    adapter.load(true);
//                }
//
//                @Override
//                public void onFailure(Call<ResponseData<List<CircleTeamModel>>> call, Throwable t) {
//                    super.onFailure(call, t);
//                    adapter.load(true);
//                }
//            });
//        }
//    }

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
                Team team = TeamDataCache.getInstance().getTeamById(((ContactItem) item).getContact().getContactId());
                model.setUserAvater(team.getIcon());
                if (itemType == ItemTypes.TEAMS.ADVANCED_TEAM) {
                    model.userAvaterRes = R.drawable.ic_default_avatar_team;
                } else if (itemType == ItemTypes.TEAMS.NORMAL_TEAM) {
                    model.userAvaterRes = R.drawable.ic_default_avatar_team_normal;
                }
                model.setUserName(team.getName());
                model.setUserId(team.getId());
                model.setTypeEnum(SessionTypeEnum.Team);
                new WorkImShareDialog(aty, model).show();
                break;
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

    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
