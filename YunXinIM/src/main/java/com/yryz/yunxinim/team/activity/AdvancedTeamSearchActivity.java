package com.yryz.yunxinim.team.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.yryz.yunxinim.R;
import com.yryz.yunxinim.uikit.ImService;
import com.yryz.yunxinim.uikit.common.activity.UI;
import com.yryz.yunxinim.uikit.common.ui.widget.ClearableEditTextWithIcon;
import com.yryz.yunxinim.uikit.model.CircleTeamModel;
import com.yryz.yunxinim.uikit.model.ToolBarOptions;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 搜索加入群组界面
 * Created by hzxuwen on 2015/3/20.
 */
public class AdvancedTeamSearchActivity extends UI {

    private ClearableEditTextWithIcon searchEditText;

    public static final void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AdvancedTeamSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_advanced_team_search_activity);
        setTitle(R.string.search_join_team);

        ToolBarOptions options = new ToolBarOptions();
        options.titleId = R.string.search_join_team;
        setToolBar(R.id.toolbar, options);

        findViews();
        initActionbar();
    }

    private void findViews() {
        searchEditText = (ClearableEditTextWithIcon) findViewById(R.id.team_search_edittext);
        searchEditText.setDeleteImage(R.drawable.nim_grey_delete_icon);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    if (TextUtils.isEmpty(searchEditText.getText().toString())) {
                        Toast.makeText(AdvancedTeamSearchActivity.this, R.string.not_allow_empty, Toast.LENGTH_SHORT).show();
                    } else {
                        queryTeamById();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void initActionbar() {
        TextView toolbarView = findView(R.id.action_bar_right_clickable_textview);
        toolbarView.setText(R.string.search);
        toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(searchEditText.getText().toString())) {
                    Toast.makeText(AdvancedTeamSearchActivity.this, R.string.not_allow_empty, Toast.LENGTH_SHORT).show();
                } else {
                    queryTeamById();
                }
            }
        });
    }

    private void queryTeamById() {
        NIMClient.getService(TeamService.class).searchTeam(searchEditText.getText().toString()).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                updateTeamInfo(team);
            }

            @Override
            public void onFailed(int code) {
                if (code == 803) {
                    Toast.makeText(AdvancedTeamSearchActivity.this, R.string.team_number_not_exist, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AdvancedTeamSearchActivity.this, "search team failed: " + code, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(AdvancedTeamSearchActivity.this, "search team exception：" + exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 搜索群组成功的回调
     *
     * @param team 群
     */
    private void updateTeamInfo(final Team team) {
        if (team.getId().equals(searchEditText.getText().toString())) {
            Http.getApiService(ImService.class).getAllTeam(team.getId()).enqueue(new Callback<ResponseData<List<CircleTeamModel>>>() {
                @Override
                public void onResponse(Call<ResponseData<List<CircleTeamModel>>> call, Response<ResponseData<List<CircleTeamModel>>> response) {
                    if (response.isSuccessful() && response.body().isSuccessful() && response.body().getData().size() > 0 && !TextUtils.isEmpty(response.body().getData().get(0).getAppId())) {
                        Toast.makeText(AdvancedTeamSearchActivity.this, "没有搜索结果", Toast.LENGTH_LONG).show();
                    } else {
                        AdvancedTeamJoinActivity.start(AdvancedTeamSearchActivity.this, team.getId());
                    }
                }

                @Override
                public void onFailure(Call<ResponseData<List<CircleTeamModel>>> call, Throwable t) {
                    AdvancedTeamJoinActivity.start(AdvancedTeamSearchActivity.this, team.getId());
                }
            });
        }
    }
}
