package com.yryz.yunxinim.uikit.team.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rz.yryz.api.Http;
import com.rz.yryz.api.model.ResponseData;
import com.yryz.yunxinim.uikit.ImService;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.R;
import com.yryz.yunxinim.uikit.cache.SimpleCallback;
import com.yryz.yunxinim.uikit.cache.TeamDataCache;
import com.yryz.yunxinim.uikit.common.activity.UI;
import com.yryz.yunxinim.uikit.common.ui.dialog.DialogMaker;
import com.yryz.yunxinim.uikit.common.util.sys.NetworkUtil;
import com.yryz.yunxinim.uikit.model.ToolBarOptions;
import com.yryz.yunxinim.uikit.team.helper.AnnouncementHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.yryz.yunxinim.uikit.team.model.Announcement;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 创建群公告界面
 * Created by hzxuwen on 2015/3/18.
 */
public class AdvancedTeamCreateAnnounceActivity extends UI {

    // constant
    private final static String EXTRA_TID = "EXTRA_TID";

    // data
    private String teamId;
    private String announce;

    // view
    private EditText teamAnnounceTitle;
    private EditText teamAnnounceContent;
    private TextView toolbarView;
    private TextView teamAnnounceTitleLast;
    private TextView teamAnnounceContentLast;

    public static void startActivityForResult(Activity activity, String teamId, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, AdvancedTeamCreateAnnounceActivity.class);
        intent.putExtra(EXTRA_TID, teamId);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_advanced_team_create_announce);

        ToolBarOptions options = new ToolBarOptions();
        options.titleString = getString(R.string.team_announcement1);
        setToolBar(R.id.toolbar, options);

        parseIntentData();
        findViews();
        initActionbar();
        requestAnnounceData(false);
    }

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_TID);
    }

    private void findViews() {
        teamAnnounceTitle = (EditText) findViewById(R.id.team_announce_title);
        teamAnnounceContent = (EditText) findViewById(R.id.team_announce_content);
        teamAnnounceTitleLast = (TextView) findViewById(R.id.tv_title_last);
        teamAnnounceContentLast = (TextView) findViewById(R.id.tv_desc_last);
        teamAnnounceTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        teamAnnounceContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(300)});
        teamAnnounceTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                teamAnnounceTitleLast.setText(String.valueOf(teamAnnounceTitle.getText().toString().trim().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        teamAnnounceContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                teamAnnounceContentLast.setText(String.valueOf(teamAnnounceContent.getText().toString().trim().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initActionbar() {
        toolbarView = findView(R.id.action_bar_right_clickable_textview);
        toolbarView.setText(R.string.save);
        toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAnnounceData(true);
            }
        });
    }

    private void requestAnnounceData(final boolean announceEdit) {
        if (!NetworkUtil.isNetAvailable(this)) {
            Toast.makeText(this, R.string.network_is_not_available, Toast.LENGTH_LONG).show();
            return;
        }

        if (announceEdit) {
            if (TextUtils.isEmpty(teamAnnounceTitle.getText().toString())) {
                Toast.makeText(AdvancedTeamCreateAnnounceActivity.this, R.string.team_announce_notice, Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(teamAnnounceContent.getText().toString())) {
                Toast.makeText(AdvancedTeamCreateAnnounceActivity.this, R.string.team_announce_notice_content, Toast.LENGTH_SHORT).show();
                return;
            }
            toolbarView.setEnabled(false);
        }

        // 请求群信息
        Team t = TeamDataCache.getInstance().getTeamById(teamId);
        if (t != null) {
            updateTeamData(t);
            if (announceEdit)
                updateAnnounce();
            else
                showAnnounce();
        } else {
            TeamDataCache.getInstance().fetchTeamById(teamId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result) {
                    if (success && result != null) {
                        updateTeamData(result);
                        if (announceEdit)
                            updateAnnounce();
                        else
                            showAnnounce();
                    } else {
                        toolbarView.setEnabled(true);
                    }
                }
            });
        }
    }

    /**
     * 获得最新公告内容
     *
     * @param team 群
     */
    private void updateTeamData(Team team) {
        if (team == null) {
            Toast.makeText(this, getString(R.string.team_not_exist), Toast.LENGTH_SHORT).show();
            showKeyboard(false);
            finish();
        } else {
            announce = team.getAnnouncement();
        }
    }

    /**
     * 创建公告更新到服务器
     */
//    private void updateAnnounce() {
//        String announcement = AnnouncementHelper.makeAnnounceJson("", teamAnnounceTitle.getText().toString(),
//                teamAnnounceContent.getText().toString());
//        Http.getNewService(ImService.class).updateTeam(NimUIKit.getAccount(), teamId, null, null, announcement, null).enqueue(new Callback<ResponseData>() {
//            @Override
//            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
//                DialogMaker.dismissProgressDialog();
//                if (response.isSuccessful() && response.body().isSuccessful()) {
//                    setResult(Activity.RESULT_OK);
//                    showKeyboard(false);
//                    finish();
//                    Toast.makeText(AdvancedTeamCreateAnnounceActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(AdvancedTeamCreateAnnounceActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData> call, Throwable t) {
//                DialogMaker.dismissProgressDialog();
//                toolbarView.setEnabled(true);
////                Toast.makeText(AdvancedTeamCreateAnnounceActivity.this, String.format(getString(R.string.update_failed), code), Toast.LENGTH_SHORT).show();
//                Toast.makeText(AdvancedTeamCreateAnnounceActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    /**
     * 创建公告更新到服务器
     */
    private void updateAnnounce() {
        String announcement = AnnouncementHelper.makeAnnounceJson("", teamAnnounceTitle.getText().toString(),
                teamAnnounceContent.getText().toString());
        NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.Announcement, announcement).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                DialogMaker.dismissProgressDialog();
                setResult(Activity.RESULT_OK);
                showKeyboard(false);
                finish();
                Toast.makeText(AdvancedTeamCreateAnnounceActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                toolbarView.setEnabled(true);
//                Toast.makeText(AdvancedTeamCreateAnnounceActivity.this, String.format(getString(R.string.update_failed), code), Toast.LENGTH_SHORT).show();
                Toast.makeText(AdvancedTeamCreateAnnounceActivity.this, R.string.default_save_fail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
                toolbarView.setEnabled(true);
            }
        });
    }

    private void showAnnounce() {
        if (TextUtils.isEmpty(announce)) {
            return;
        }

        List<Announcement> list = AnnouncementHelper.getAnnouncements(teamId, announce, 1);
        if (list == null || list.isEmpty()) {
            return;
        }

        teamAnnounceTitle.setText(list.get(0).getTitle());
        teamAnnounceContent.setText(list.get(0).getContent());
    }

    @Override
    public void onBackPressed() {
        showKeyboard(false);
        super.onBackPressed();
    }
}
