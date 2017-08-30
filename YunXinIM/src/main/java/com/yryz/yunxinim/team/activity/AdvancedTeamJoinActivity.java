package com.yryz.yunxinim.team.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.team.constant.VerifyTypeEnum;
import com.yryz.yunxinim.R;
import com.yryz.yunxinim.session.SessionHelper;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.cache.SimpleCallback;
import com.yryz.yunxinim.uikit.cache.TeamDataCache;
import com.yryz.yunxinim.uikit.common.activity.UI;
import com.yryz.yunxinim.uikit.common.ui.imageview.HeadImageView;
import com.yryz.yunxinim.uikit.common.util.sys.ScreenUtil;
import com.yryz.yunxinim.uikit.model.ToolBarOptions;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;

/**
 * 申请加入群组界面
 * Created by hzxuwen on 2015/3/20.
 */
public class AdvancedTeamJoinActivity extends UI implements View.OnClickListener {

    private static final String EXTRA_ID = "EXTRA_ID";

    private String teamId;
    private Team team;

    private TextView teamNameText;
    private TextView memberCountText;
    private TextView teamTypeText;
    private Button applyJoinButton;
    private HeadImageView teamIcon;

    public static void start(Context context, String teamId) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, teamId);
        intent.setClass(context, AdvancedTeamJoinActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nim_advanced_team_join_activity);

        ToolBarOptions options = new ToolBarOptions();
        options.titleId = R.string.team_join;
        setToolBar(R.id.toolbar, options);

        findViews();
        parseIntentData();
        requestTeamInfo();
    }

    private void findViews() {
        teamNameText = (TextView) findViewById(R.id.team_name);
        memberCountText = (TextView) findViewById(R.id.member_count);
        applyJoinButton = (Button) findViewById(R.id.apply_join);
        teamTypeText = (TextView) findViewById(R.id.team_type);
        teamIcon = (HeadImageView) findViewById(R.id.team_head_image);
        applyJoinButton.setOnClickListener(this);
    }

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_ID);
    }

    private void requestTeamInfo() {
        Team t = TeamDataCache.getInstance().getTeamById(teamId);
        if (t != null) {
            updateTeamInfo(t);
        } else {
            TeamDataCache.getInstance().fetchTeamById(teamId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    }
                }
            });
        }
    }

    /**
     * 更新群信息
     *
     * @param t 群
     */
    private void updateTeamInfo(final Team t) {
        if (t == null) {
            Toast.makeText(AdvancedTeamJoinActivity.this, R.string.team_not_exist, Toast.LENGTH_LONG).show();
            finish();
        } else {
            team = t;
            teamNameText.setText(team.getName());
            memberCountText.setText(team.getMemberCount() + "人");
            if (team.getType() == TeamTypeEnum.Advanced) {
                teamTypeText.setText(R.string.advanced_team);
            } else {
                teamTypeText.setText(R.string.normal_team);
            }
            teamTypeText.setText("群号：" + team.getId());
            if (team.getVerifyType() == VerifyTypeEnum.Private) {
                applyJoinButton.setText("该群无法申请加入");
                applyJoinButton.setEnabled(false);
            }
            if (team.isMyTeam()) {
                applyJoinButton.setText("进入群");
            }
            teamIcon.loadTeamIconByTeam(t);
        }
    }

    @Override
    public void onClick(View v) {
        if (team != null) {

            if (team.isMyTeam()) {
                SessionHelper.startTeamSession(this, team.getId()); // 进入创建的群
                return;
            }

            switch (team.getVerifyType()) {
                case Free:
                    applyJoinTeam("我是" + NimUIKit.getUserInfoProvider().getUserInfo(NimUIKit.getAccount()).getName());
                    break;
                case Apply:
                    final Dialog dialog;
                    View view = getLayoutInflater().inflate(R.layout.dialog_team_verification, null, false);
                    TextView id_tv_confirm = (TextView) view.findViewById(R.id.id_tv_confirm);
                    final EditText editText = (EditText) view.findViewById(R.id.id_edit);
                    HeadImageView imageView = (HeadImageView) view.findViewById(R.id.contacts_item_head);
                    if (team != null) {
                        editText.setHint("我是" + NimUIKit.getUserInfoProvider().getUserInfo(NimUIKit.getAccount()).getName());
                        imageView.loadBuddyAvatar(NimUIKit.getAccount());
                    }
                    dialog = new Dialog(this, R.style.dialog_default_style);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ScreenUtil.getDisplayWidth() - 80,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    dialog.setContentView(view, params);
                    dialog.setCanceledOnTouchOutside(true);
                    id_tv_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    TextView id_tv_cancel = (TextView) view.findViewById(R.id.id_tv_cancel);
                    id_tv_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String verificationText;
                            if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                                verificationText = editText.getHint().toString().trim();
                            } else {
                                verificationText = editText.getText().toString().trim();
                            }
                            if (team != null) {
                                applyJoinTeam(verificationText);
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    break;
                case Private:
                    break;
                default:
                    break;
            }
        }
    }

    private void applyJoinTeam(String verificationText) {
        NIMClient.getService(TeamService.class).applyJoinTeam(team.getId(), verificationText).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                applyJoinButton.setEnabled(false);
                String toast = getString(R.string.team_join_success, team.getName());
                Toast.makeText(AdvancedTeamJoinActivity.this, toast, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code) {
                if (code == 808) {
                    applyJoinButton.setEnabled(false);
                    Toast.makeText(AdvancedTeamJoinActivity.this, R.string.team_apply_to_join_send_success,
                            Toast.LENGTH_SHORT).show();
                } else if (code == 809) {
                    applyJoinButton.setEnabled(false);
                    Toast.makeText(AdvancedTeamJoinActivity.this, R.string.has_exist_in_team,
                            Toast.LENGTH_SHORT).show();
                } else if (code == 802) {
                    Toast.makeText(AdvancedTeamJoinActivity.this, "该群已设置不允许任何人加入",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdvancedTeamJoinActivity.this, "failed, error code =" + code,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }
}
