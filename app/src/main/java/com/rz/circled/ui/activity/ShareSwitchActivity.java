package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.rz.circled.R;
import com.rz.circled.modle.ShareModel;
import com.rz.circled.ui.view.WorkImShareDialog;
import com.rz.circled.widget.CommomUtils;
import com.rz.common.cache.preference.Session;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.widget.MyListView;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.cache.TeamDataCache;
import com.yryz.yunxinim.uikit.common.adapter.TAdapterDelegate;
import com.yryz.yunxinim.uikit.common.adapter.TViewHolder;
import com.yryz.yunxinim.uikit.contact.core.item.ItemTypes;
import com.yryz.yunxinim.uikit.recent.viewholder.RecentContactAdapter;
import com.yryz.yunxinim.uikit.recent.viewholder.ShareRecentViewHolder;
import com.yryz.yunxinim.uikit.uinfo.UserInfoHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.rz.common.constant.Constants.SWITCH_SHARE_SUCCESS;

/**
 * Created by rzw2 on 2017/1/5.
 */

public class ShareSwitchActivity extends BaseActivity implements TAdapterDelegate {
    private static final String EXTRA_DATA_ITEM_DATA = "EXTRA_DATA_ITEM_DATA";
    @BindView(R.id.id_content_lv)
    MyListView mRecentLv;
    @BindView(R.id.layout_recent)
    LinearLayout mLayoutRecent;
    @BindView(R.id.id_tv_title)
    TextView tvShareTitle;
    @BindView(R.id.layout)
    View layoutTeam;

    private RecentContactAdapter adapter;
    // data
    private List<RecentContact> items;

    public static final void start(Context context, ShareModel shareModel) {
        Intent intent = new Intent();
        intent.setClass(context, ShareSwitchActivity.class);
        intent.putExtra(EXTRA_DATA_ITEM_DATA, shareModel);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_share_switch, null);
    }

    @Override
    public void initView() {

        setTitleText(R.string.share_youran_chat);

        if (Session.isNeedTeam()) {
            tvShareTitle.setText(R.string.share_object);
            layoutTeam.setVisibility(View.VISIBLE);
        } else {
            tvShareTitle.setText(R.string.share_to_friend);
            layoutTeam.setVisibility(View.GONE);
        }

        items = new ArrayList<>();
        adapter = new RecentContactAdapter(ShareSwitchActivity.this, items, this);
        mRecentLv.setAdapter(adapter);
        mRecentLv.setItemsCanFocus(true);
        mRecentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 0) {
                    return;
                }

                RecentContact item = adapter.getItem(position);
                if (item == null) {
                    return;
                }

                switch (item.getSessionType()) {
                    case P2P:
                        if (NimUIKit.getContactEventListener() != null) {
                            ShareModel model = (ShareModel) getIntent().getSerializableExtra(EXTRA_DATA_ITEM_DATA);
                            // 判断是否需要ImageLoader加载
                            UserInfoProvider.UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(item.getContactId());
                            if (userInfo != null) {
                                if (userInfo.getAvatar() != null)
                                    model.setUserAvater(userInfo.getAvatar());
                                model.setUserName(UserInfoHelper.getUserTitleName(item.getContactId(), SessionTypeEnum.P2P));
                                if (userInfo.getAccount() != null)
                                    model.setUserId(userInfo.getAccount());
                            }
                            model.setTypeEnum(SessionTypeEnum.P2P);
                            new WorkImShareDialog(aty, model).show();
                        }
                        break;
                    case Team:
                        ShareModel model = (ShareModel) getIntent().getSerializableExtra(EXTRA_DATA_ITEM_DATA);
                        Team team = TeamDataCache.getInstance().getTeamById(item.getContactId());
                        model.setUserAvater(team.getIcon());
                        if (team.getType() == TeamTypeEnum.Advanced) {
                            model.userAvaterRes = R.drawable.ic_default_avatar_team;
                        } else if (team.getType() == TeamTypeEnum.Normal) {
                            model.userAvaterRes = R.drawable.ic_default_avatar_team_normal;
                        }
                        model.setUserName(team.getName());
                        model.setUserId(team.getId());
                        model.setTypeEnum(SessionTypeEnum.Team);
                        new WorkImShareDialog(aty, model).show();
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                // 查询最近联系人列表数据
                NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                            mLayoutRecent.setVisibility(View.GONE);
                            return;
                        }
                        items.clear();
                        items.addAll(recents);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @OnClick({R.id.id_group_chat_rela1, R.id.id_group_discuss1, R.id.id_group_friend1})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.id_group_chat_rela1:
//                CommomUtils.trackUser("分享", "悠友圈分享", "群聊");
//                ShareTeamListActivity.start(getApplicationContext(), ItemTypes.TEAMS.ADVANCED_TEAM, (ShareModel) getIntent().getSerializableExtra(EXTRA_DATA_ITEM_DATA));
//                break;
//            case R.id.id_group_discuss1:
//                CommomUtils.trackUser("分享", "悠友圈分享", "讨论组");
//                ShareTeamListActivity.start(getApplicationContext(), ItemTypes.TEAMS.NORMAL_TEAM, (ShareModel) getIntent().getSerializableExtra(EXTRA_DATA_ITEM_DATA));
//                break;
//            case R.id.id_group_friend1:
//                CommomUtils.trackUser("分享", "悠友圈分享", "好友");
//                ShareFriendsListActivity.start(getApplicationContext(), (ShareModel) getIntent().getSerializableExtra(EXTRA_DATA_ITEM_DATA));
//                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(BaseEvent event) {
        if (TextUtils.equals(SWITCH_SHARE_SUCCESS, event.key)) {
            finish();
        }
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        return ShareRecentViewHolder.class;
    }

    @Override
    public boolean enabled(int position) {
        return true;
    }

    @Override
    public void refreshPage() {

    }
}
