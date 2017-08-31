package com.yryz.yunxinim.team;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.yryz.yunxinim.DemoCache;
import com.yryz.yunxinim.R;
import com.yryz.yunxinim.main.activity.MainActivity;
import com.yryz.yunxinim.session.SessionHelper;
import com.yryz.yunxinim.uikit.ImService;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.cache.NimUserInfoCache;
import com.yryz.yunxinim.uikit.common.ui.dialog.DialogMaker;
import com.yryz.yunxinim.uikit.model.TeamCreateModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hzxuwen on 2015/9/25.
 */
public class TeamCreateHelper {
    private static final String TAG = TeamCreateHelper.class.getSimpleName();
    private static final int DEFAULT_TEAM_CAPACITY = 200;

    public static void createNormalTeam(final Context context, List<String> memberAccounts, final boolean isNeedBack, final RequestCallback<Void> callback, String teamName) {

        DialogMaker.showProgressDialog(context, context.getString(com.yryz.yunxinim.R.string.empty), true);
        // 创建群
        HashMap<TeamFieldEnum, Serializable> fields = new HashMap<TeamFieldEnum, Serializable>();
        fields.put(TeamFieldEnum.Name, teamName);
        NIMClient.getService(TeamService.class).createTeam(fields, TeamTypeEnum.Normal, "",
                memberAccounts).setCallback(
                new RequestCallback<Team>() {
                    @Override
                    public void onSuccess(Team team) {
                        DialogMaker.dismissProgressDialog();
                        Toast.makeText(DemoCache.getContext(), com.yryz.yunxinim.R.string.create_team_success,
                                Toast.LENGTH_SHORT).show();
                        if (isNeedBack) {
                            SessionHelper.startTeamSession(context, team.getId(), MainActivity.class, null); // 进入创建的群
                        } else {
                            SessionHelper.startTeamSession(context, team.getId());
                        }
                        if (callback != null) {
                            callback.onSuccess(null);
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                        if (code == 801) {
                            String tip = context.getString(com.yryz.yunxinim.R.string.over_team_member_capacity, DEFAULT_TEAM_CAPACITY);
                            Toast.makeText(DemoCache.getContext(), tip,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DemoCache.getContext(), com.yryz.yunxinim.R.string.create_team_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        Log.e(TAG, "create team error: " + code);
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                }
        );
    }

    /**
     * 创建讨论组
     */
    public static void createNormalTeam(final Context context, List<String> memberAccounts, final boolean isNeedBack, final RequestCallback<Void> callback) {

        String teamName = NimUserInfoCache.getInstance().getUserDisplayName(NimUIKit.getAccount()) + "讨论组";

        DialogMaker.showProgressDialog(context, context.getString(com.yryz.yunxinim.R.string.empty), true);
        // 创建群
        HashMap<TeamFieldEnum, Serializable> fields = new HashMap<TeamFieldEnum, Serializable>();
        fields.put(TeamFieldEnum.Name, teamName);
        NIMClient.getService(TeamService.class).createTeam(fields, TeamTypeEnum.Normal, "",
                memberAccounts).setCallback(
                new RequestCallback<Team>() {
                    @Override
                    public void onSuccess(Team team) {
                        DialogMaker.dismissProgressDialog();
                        Toast.makeText(DemoCache.getContext(), com.yryz.yunxinim.R.string.create_team_success,
                                Toast.LENGTH_SHORT).show();
                        if (isNeedBack) {
                            SessionHelper.startTeamSession(context, team.getId(), MainActivity.class, null); // 进入创建的群
                        } else {
                            SessionHelper.startTeamSession(context, team.getId());
                        }
                        if (callback != null) {
                            callback.onSuccess(null);
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                        if (code == 801) {
                            String tip = context.getString(com.yryz.yunxinim.R.string.over_team_member_capacity, DEFAULT_TEAM_CAPACITY);
                            Toast.makeText(DemoCache.getContext(), tip,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DemoCache.getContext(), com.yryz.yunxinim.R.string.create_team_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        Log.e(TAG, "create team error: " + code);
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                }
        );
    }

    /**
     * 创建高级群
     */
    public static void createAdvancedTeam(final Context context, List<String> memberAccounts) {
        createAdvancedTeam(context, memberAccounts, "高级群");
    }

    public static void createAdvancedTeam(final Context context, List<String> memberAccounts, String teamName) {
        createAdvancedTeam(context, memberAccounts, teamName, 0l, "");
    }

    public static void createAdvancedTeamActivity(final Activity context, List<String> memberAccounts, String teamName, final Long invokeId, String circleKey
            , String intro, String imgUrl) {
//        DialogMaker.showProgressDialog(context, context.getString(com.yryz.yunxinim.R.string.empty), true);
        String[] array = new String[memberAccounts.size()];
        for (int i = 0; i < memberAccounts.size(); i++) {
            array[i] = memberAccounts.get(i);
        }
        // 创建群
        Http.getApiService(ImService.class).createTeam(teamName, NimUIKit.getAccount(), new Gson().toJson(memberAccounts), "", intro, "验证消息", imgUrl, circleKey).enqueue(new Callback<ResponseData<TeamCreateModel>>() {
            @Override
            public void onResponse(Call<ResponseData<TeamCreateModel>> call, Response<ResponseData<TeamCreateModel>> response) {
                if (null == response.body() || null == response.body().getData()) {
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    DialogMaker.dismissProgressDialog();
                    return;
                }
                Log.i(TAG, "create team success, team id =" + response.body().getData().getTid() + ", now begin to update property...");
                if (invokeId != 0l) {
                    JsEvent.callJsEvent(invokeId, response.body().getData().getTid(), BaseParamsObject.RESULT_CODE_SUCRESS);
                }
                onCreateSuccess(context, response.body().getData());
                context.finish();

            }

            @Override
            public void onFailure(Call<ResponseData<TeamCreateModel>> call, Throwable t) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(DemoCache.getContext(), R.string.create_team_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void createAdvancedTeam(final Context context, List<String> memberAccounts, String teamName, final Long invokeId, String circleKey) {
//        DialogMaker.showProgressDialog(context, context.getString(com.yryz.yunxinim.R.string.empty), true);
        String[] array = new String[memberAccounts.size()];
        for (int i = 0; i < memberAccounts.size(); i++) {
            array[i] = memberAccounts.get(i);
        }
        // 创建群
        Http.getApiService(ImService.class).createTeam(teamName, NimUIKit.getAccount(), new Gson().toJson(memberAccounts), "", "", "验证消息", "", circleKey).enqueue(new Callback<ResponseData<TeamCreateModel>>() {
            @Override
            public void onResponse(Call<ResponseData<TeamCreateModel>> call, Response<ResponseData<TeamCreateModel>> response) {
                if (null == response.body() || null == response.body().getData()) {
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "create team success, team id =" + response.body().getData().getTid() + ", now begin to update property...");
                if (invokeId != 0l) {
                    JsEvent.callJsEvent(invokeId, response.body().getData().getTid(), BaseParamsObject.RESULT_CODE_SUCRESS);
                }
                onCreateSuccess(context, response.body().getData());

            }

            @Override
            public void onFailure(Call<ResponseData<TeamCreateModel>> call, Throwable t) {
                if (invokeId != 0l) {
                    JsEvent.callJsEvent(invokeId, null, BaseParamsObject.RESULT_CODE_FAILED);
                }
                DialogMaker.dismissProgressDialog();
            }
        });


//        DialogMaker.showProgressDialog(context, context.getString(com.yryz.yunxinim.R.string.empty), true);
//        // 创建群
//        TeamTypeEnum type = TeamTypeEnum.Advanced;
//        HashMap<TeamFieldEnum, Serializable> fields = new HashMap<>();
//        fields.put(TeamFieldEnum.Name, teamName);
//        NIMClient.getService(TeamService.class).createTeam(fields, type, "",
//                memberAccounts).setCallback(
//                new RequestCallback<Team>() {
//                    @Override
//                    public void onSuccess(Team t) {
//                        Log.i(TAG, "create team success, team id =" + t.getId() + ", now begin to update property...");
//
//                        if (invokeId != 0l) {
//                            JsEvent.callJsEvent(invokeId, t.getId(), BaseParamsObject.RESULT_CODE_SUCRESS);
//                        }
//
//                        onCreateSuccess(context, t);
//                    }
//
//                    @Override
//                    public void onFailed(int code) {
//                        DialogMaker.dismissProgressDialog();
//
//                        if (invokeId != 0l) {
//                            JsEvent.callJsEvent(invokeId, null, BaseParamsObject.RESULT_CODE_FAILED);
//                        }
//
//                        String tip;
//                        if (code == 801) {
//                            tip = context.getString(com.yryz.yunxinim.R.string.over_team_member_capacity,
//                                    DEFAULT_TEAM_CAPACITY);
//                        } else if (code == 806) {
//                            tip = context.getString(com.yryz.yunxinim.R.string.over_team_capacity);
//                        } else {
//                            tip = context.getString(com.yryz.yunxinim.R.string.create_team_failed) + ", code=" +
//                                    code;
//                        }
//                        Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
//                        Log.e(TAG, "create team error: " + code);
//                    }
//
//                    @Override
//                    public void onException(Throwable exception) {
//                        DialogMaker.dismissProgressDialog();
//                    }
//                }
//        );
    }

//    public static void createAdvancedTeam(final Context context, List<String> memberAccounts, String teamName) {
//
//        DialogMaker.showProgressDialog(context, context.getString(com.yryz.yunxinim.R.string.empty), true);
//        // 创建群
//        Http.getNewService(ImService.class).createTeam(teamName, NimUIKit.getAccount(), "", "", "", "0", "", "").enqueue(new BaseCallback<ResponseData<TeamCreateModel>>() {
//            @Override
//            public void onResponse(Call<ResponseData<TeamCreateModel>> call, Response<ResponseData<TeamCreateModel>> response) {
//                super.onResponse(call, response);
//                Log.i(TAG, "create team success, team id =" + response.body().getData().getTid() + ", now begin to update property...");
//                onCreateSuccess(context, response.body().getData());
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<TeamCreateModel>> call, Throwable t) {
//                super.onFailure(call, t);
//            }
//        });
//    }

    /**
     * 群创建成功回调
     */
    private static void onCreateSuccess(final Context context, final TeamCreateModel team) {
        if (team == null) {
            Log.e(TAG, "onCreateSuccess exception: team is null");
            return;
        }
        Log.i(TAG, "create and update team success");
        DialogMaker.dismissProgressDialog();
        Toast.makeText(DemoCache.getContext(), com.yryz.yunxinim.R.string.create_team_success, Toast.LENGTH_SHORT).show();
        // 演示：向群里插入一条Tip消息，使得该群能立即出现在最近联系人列表（会话列表）中，满足部分开发者需求
        Map<String, Object> content = new HashMap<>(1);
        content.put("content", "成功创建高级群");
        IMMessage msg = MessageBuilder.createTipMessage(team.getTid(), SessionTypeEnum.Team);
        msg.setRemoteExtension(content);
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableUnreadCount = false;
        msg.setConfig(config);
        msg.setStatus(MsgStatusEnum.success);
        NIMClient.getService(MsgService.class).saveMessageToLocal(msg, true);

        // 发送后，稍作延时后跳转
        new Handler(context.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                SessionHelper.startTeamSession(context, team.getTid()); // 进入创建的群
            }
        }, 50);
    }

    /**
     * 群创建成功回调
     */
    private static void onCreateSuccess(final Context context, final Team team) {
        if (team == null) {
            Log.e(TAG, "onCreateSuccess exception: team is null");
            return;
        }
        Log.i(TAG, "create and update team success");

        DialogMaker.dismissProgressDialog();
        Toast.makeText(DemoCache.getContext(), com.yryz.yunxinim.R.string.create_team_success, Toast.LENGTH_SHORT).show();

        // 演示：向群里插入一条Tip消息，使得该群能立即出现在最近联系人列表（会话列表）中，满足部分开发者需求
        Map<String, Object> content = new HashMap<>(1);
        content.put("content", "成功创建高级群");
        IMMessage msg = MessageBuilder.createTipMessage(team.getId(), SessionTypeEnum.Team);
        msg.setRemoteExtension(content);
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableUnreadCount = false;
        msg.setConfig(config);
        msg.setStatus(MsgStatusEnum.success);
        NIMClient.getService(MsgService.class).saveMessageToLocal(msg, true);

        // 发送后，稍作延时后跳转
        new Handler(context.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                SessionHelper.startTeamSession(context, team.getId()); // 进入创建的群
            }
        }, 50);
    }
}
