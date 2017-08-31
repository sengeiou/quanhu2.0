package com.rz.circled.js;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.rz.circled.R;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;
import com.yryz.yunxinim.uikit.ImService;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.common.ui.imageview.HeadImageView;
import com.yryz.yunxinim.uikit.common.util.sys.ScreenUtil;
import com.yryz.yunxinim.uikit.common.util.sys.SoftKeyboardUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rzw2 on 2017/4/6.
 */

public class JoinTeamHandler extends ServerHandler {

    private Long invokeId;

    public JoinTeamHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "joinTeam";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Log.d("zxw", "handle: params" + params);
        invokeId = paramObj.getInvokeId();

        JSONObject object = JSON.parseObject(params);

        if (object.containsKey("data") && object.getJSONObject("data").containsKey("teamId")) {
            final String teamId = object.getJSONObject("data").getString("teamId");
            int verifyType = object.getJSONObject("data").getInteger("verifyType");
            switch (verifyType) {
                case 0:
                    applyJoinTeam(teamId, mActivity.getString(R.string.i_am) + NimUIKit.getUserInfoProvider().getUserInfo(NimUIKit.getAccount()).getName());
                    break;
                case 1:
                    if (NIMClient.getStatus() == StatusCode.LOGINED) {
                        final Dialog dialog;
                        View view = mActivity.getLayoutInflater().inflate(com.yryz.yunxinim.R.layout.dialog_team_verification, null, false);
                        TextView id_tv_confirm = (TextView) view.findViewById(com.yryz.yunxinim.R.id.id_tv_confirm);
                        final EditText editText = (EditText) view.findViewById(com.yryz.yunxinim.R.id.id_edit);
                        HeadImageView imageView = (HeadImageView) view.findViewById(com.yryz.yunxinim.R.id.contacts_item_head);
                        editText.setHint(mActivity.getString(R.string.i_am) + NimUIKit.getUserInfoProvider().getUserInfo(NimUIKit.getAccount()).getName());
                        imageView.loadBuddyAvatar(NimUIKit.getAccount());
                        dialog = new Dialog(mActivity, com.yryz.yunxinim.R.style.dialog_default_style);
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                                ScreenUtil.getDisplayWidth() - 80,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        dialog.setContentView(view, params1);
                        dialog.setCanceledOnTouchOutside(true);
                        id_tv_confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        TextView id_tv_cancel = (TextView) view.findViewById(com.yryz.yunxinim.R.id.id_tv_cancel);
                        id_tv_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String verificationText;
                                if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                                    verificationText = editText.getHint().toString().trim();
                                } else {
                                    verificationText = editText.getText().toString().trim();
                                }
                                applyJoinTeam(teamId, verificationText);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else {
                        JsEvent.callJsEvent(invokeId, null, BaseParamsObject.RESULT_CODE_FAILED);
                    }
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        } else
            JsEvent.callJsEvent(invokeId, null, BaseParamsObject.RESULT_CODE_FAILED);
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParms, BaseParamsObject baseParamsObject) {
                if (null == businessParms) return;

                int status = (int) businessParms;
                Map<String, Object> map = new HashMap<>();
                map.put("status", status);
                baseParamsObject.data = map;
            }
        };
        return callback;
    }

    @Override
    public boolean isUi() {
        return false;
    }

    private void applyJoinTeam(String teamId, String verificationText) {
        NIMClient.getService(TeamService.class).applyJoinTeam(teamId, verificationText).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                JsEvent.callJsEvent(invokeId, 1, BaseParamsObject.RESULT_CODE_SUCRESS);
                String toast = mActivity.getString(com.yryz.yunxinim.R.string.team_join_success, team.getName());
                Toast.makeText(mActivity, toast, Toast.LENGTH_SHORT).show();

                SoftKeyboardUtil.showKeyboard(mActivity, false);

                Http.getApiService(ImService.class).joinTeam(NimUIKit.getAccount(), NimUIKit.getAccount(), team.getId()).enqueue(new BaseCallback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        super.onResponse(call, response);
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        super.onFailure(call, t);
                    }
                });
            }

            @Override
            public void onFailed(int code) {
                if (code == 808) {
//                    Toast.makeText(webContainerAty, com.yryz.yunxinim.R.string.team_apply_to_join_send_success,
//                            Toast.LENGTH_SHORT).show();

                    SoftKeyboardUtil.showKeyboard(mActivity, false);

                    JsEvent.callJsEvent(invokeId, 2, BaseParamsObject.RESULT_CODE_SUCRESS);
                    return;
                } else if (code == 809) {
                    Toast.makeText(mActivity, com.yryz.yunxinim.R.string.has_exist_in_team,
                            Toast.LENGTH_SHORT).show();
                } else if (code == 802) {
                    Toast.makeText(mActivity, R.string.unallowed_join_hint,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, "failed, error code =" + code,
                            Toast.LENGTH_SHORT).show();
                }
                JsEvent.callJsEvent(invokeId, 0, BaseParamsObject.RESULT_CODE_FAILED);
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }
}
