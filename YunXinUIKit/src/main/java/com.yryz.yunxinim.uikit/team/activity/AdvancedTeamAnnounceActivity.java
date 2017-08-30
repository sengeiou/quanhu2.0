package com.yryz.yunxinim.uikit.team.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamUpdateModeEnum;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.R;
import com.yryz.yunxinim.uikit.cache.SimpleCallback;
import com.yryz.yunxinim.uikit.cache.TeamDataCache;
import com.yryz.yunxinim.uikit.common.activity.UI;
import com.yryz.yunxinim.uikit.common.adapter.TAdapter;
import com.yryz.yunxinim.uikit.common.adapter.TAdapterDelegate;
import com.yryz.yunxinim.uikit.common.adapter.TViewHolder;
import com.yryz.yunxinim.uikit.common.ui.listview.ListViewUtil;
import com.yryz.yunxinim.uikit.model.ToolBarOptions;
import com.yryz.yunxinim.uikit.team.helper.AnnouncementHelper;
import com.yryz.yunxinim.uikit.team.model.Announcement;
import com.yryz.yunxinim.uikit.team.viewholder.TeamAnnounceHolder;
import com.netease.nimlib.sdk.team.constant.TeamMemberType;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.ArrayList;
import java.util.List;

/**
 * 群公告列表
 * Created by hzxuwen on 2015/3/18.
 */
public class AdvancedTeamAnnounceActivity extends UI implements TAdapterDelegate {
    // constant
    private final static String EXTRA_TID = "EXTRA_TID";
    private final static String EXTRA_AID = "EXTRA_AID";
    private final static int RES_ANNOUNCE_CREATE_CODE = 0x10;
    public final static String RESULT_ANNOUNCE_DATA = "RESULT_ANNOUNCE_DATA";

    // context
    private Handler uiHandler;

    // data
    private String teamId;
    private String announceId;
    private String announce;

    // view
    private View announceTip;
    private ListView announceListView;
    private TAdapter mAdapter;
    private List<Announcement> items;

    private boolean isMember = false;

    public static void start(Activity activity, String teamId) {
        start(activity, teamId, null);
    }

    public static void start(Activity activity, String teamId, String announceId) {
        Intent intent = new Intent();
        intent.setClass(activity, AdvancedTeamAnnounceActivity.class);
        intent.putExtra(EXTRA_TID, teamId);
        if (announceId != null) {
            intent.putExtra(EXTRA_AID, announceId);
        }
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_advanced_team_announce);

        ToolBarOptions options = new ToolBarOptions();
        options.titleString = getString(R.string.team_announcement1);
        setToolBar(R.id.toolbar, options);

        uiHandler = new Handler(getMainLooper());

        parseIntentData();
        findViews();
        initActionbar();
        initAdapter();
        requestTeamData();
        requestMemberData();
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
        return TeamAnnounceHolder.class;
    }

    @Override
    public boolean enabled(int position) {
        return false;
    }

    /**
     * ******************************初始化*******************************
     */

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_TID);
        announceId = getIntent().getStringExtra(EXTRA_AID);
    }

    private void findViews() {
        announceListView = (ListView) findViewById(R.id.team_announce_listview);
        announceTip = findViewById(R.id.team_announce_tips_layout);
    }

    private void initActionbar() {
        TextView toolbarView = findView(R.id.action_bar_right_clickable_textview);
        toolbarView.setText((null != items && items.size() != 0) ? R.string.edit : R.string.create);
        toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedTeamCreateAnnounceActivity.startActivityForResult(AdvancedTeamAnnounceActivity.this, teamId, RES_ANNOUNCE_CREATE_CODE);
            }
        });
    }

    private void initAdapter() {
        items = new ArrayList<>();
        mAdapter = new TAdapter(this, items, this);
        announceListView.setAdapter(mAdapter);
        announceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        announceListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void requestTeamData() {
        // 请求群信息
        Team t = TeamDataCache.getInstance().getTeamById(teamId);
        if (t != null) {
            updateAnnounceInfo(t);
        } else {
            TeamDataCache.getInstance().fetchTeamById(teamId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result) {
                    if (success && result != null) {
                        updateAnnounceInfo(result);
                    }
                }
            });
        }
    }

    private void requestMemberData() {
        TeamMember teamMember = TeamDataCache.getInstance().getTeamMember(teamId, NimUIKit.getAccount());
        if (teamMember != null) {
            updateTeamMember(teamMember);
        } else {
            // 请求群成员
            TeamDataCache.getInstance().fetchTeamMember(teamId, NimUIKit.getAccount(), new SimpleCallback<TeamMember>() {
                @Override
                public void onResult(boolean success, TeamMember member) {
                    if (success && member != null) {
                        updateTeamMember(member);
                    }
                }
            });
        }
    }

    /**
     * 更新公告信息
     *
     * @param team 群
     */
    private void updateAnnounceInfo(Team team) {
        if (team == null) {
            Toast.makeText(this, getString(R.string.team_not_exist), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            announce = team.getAnnouncement();
            setAnnounceItem();
        }
    }

    /**
     * 判断是否是普通成员
     *
     * @param teamMember 群成员
     */
    private void updateTeamMember(TeamMember teamMember) {
        // 请求群信息
        Team team = TeamDataCache.getInstance().getTeamById(teamId);
        if (team.getTeamUpdateMode() == TeamUpdateModeEnum.Manager) {
            if (teamMember.getType() == TeamMemberType.Manager || teamMember.getType() == TeamMemberType.Owner) {
                findView(R.id.action_bar_right_clickable_textview).setVisibility(View.VISIBLE);
            } else if (teamMember.getType() == TeamMemberType.Normal) {
                isMember = true;
                findView(R.id.action_bar_right_clickable_textview).setVisibility(View.GONE);
            }
        } else {
            if (teamMember.getType() == TeamMemberType.Normal) {
                isMember = true;
            }
            findView(R.id.action_bar_right_clickable_textview).setVisibility(View.VISIBLE);
        }


    }

    /**
     * 设置公告
     */
    private void setAnnounceItem() {
        if (TextUtils.isEmpty(announce)) {
            announceTip.setVisibility(View.VISIBLE);
            return;
        } else {
            announceTip.setVisibility(View.GONE);
        }

        List<Announcement> list = AnnouncementHelper.getAnnouncements(teamId, announce, isMember ? 1 : Integer.MAX_VALUE);
        if (list == null || list.isEmpty()) {
            return;
        }

        items.clear();
        items.addAll(list);

        mAdapter.notifyDataSetChanged();

        jumpToIndex(list);

        initActionbar();
    }

    /**
     * 跳转到选中的公告
     *
     * @param list 群公告列表
     */
    private void jumpToIndex(List<Announcement> list) {
        if (TextUtils.isEmpty(announceId)) {
            return;
        }

        int jumpIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(announceId)) {
                jumpIndex = i;
                break;
            }
        }

        if (jumpIndex >= 0) {
            final int position = jumpIndex;
            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ListViewUtil.scrollToPosition(announceListView, position, 0);
                }
            }, 200);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RES_ANNOUNCE_CREATE_CODE:
                    announceId = null;
                    items.clear();
                    requestTeamData();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(RESULT_ANNOUNCE_DATA, announce);
        setResult(Activity.RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}
