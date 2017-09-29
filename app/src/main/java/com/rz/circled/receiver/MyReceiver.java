package com.rz.circled.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rz.circled.constants.JPushTypeConstants;
import com.rz.circled.event.EventConstant;
import com.rz.circled.helper.NewsJumpHelper;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.httpapi.bean.MyPushInfo;
import com.rz.httpapi.bean.NewsBean;
import com.rz.httpapi.bean.UserPushInfo;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by rzw2 on 2017/8/14.
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
        } else {
            if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
                try {
                    Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                    String str = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                    Gson gson = new Gson();
                    NewsBean mNews = gson.fromJson(str, NewsBean.class);
                    MyPushInfo mInfo = gson.fromJson(str, MyPushInfo.class);
                    if (mNews != null && !TextUtils.isEmpty(mNews.getMessageId())) {
                        if (TextUtils.equals(mNews.getMsgEnumType(), "20040001")){
                            EventBus.getDefault().post(new BaseEvent(EventConstant.USER_AVATAR_REFUSE));
                            Log.e(TAG, "onReceive: avatar" );
                        }
                        EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_COME_UNREAD, mNews.getType() + "-" + mNews.getLabel()));
                    } else if (mInfo != null && !TextUtils.isEmpty(mInfo.getMsgId())) {
                        switch (mInfo.getColType()) {
                            case JPushTypeConstants.COLTYPE_MY_DYNAMIC:
                                MyPushInfo<UserPushInfo> myInfo = gson.fromJson(str, new TypeToken<MyPushInfo<UserPushInfo>>() {
                                }.getType());
                                if (myInfo == null || myInfo.getData() == null) return;
                                switch (mInfo.getMsgType()) {
                                    case JPushTypeConstants.APPLY_FOR_ADD_FRIEND:
                                        int num = TextUtils.isEmpty(Session.getUserFocusNum()) ? 0 : Integer.parseInt(Session.getUserFocusNum());
                                        Session.setUserFocusNum((num + 1) + "");
                                        EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_UNREAD_CHANGE));
                                        break;
                                }
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

                //打开自定义的Activity
                String str = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                Gson gson = new Gson();
                NewsBean mNews = gson.fromJson(str, NewsBean.class);
                MyPushInfo mInfo = gson.fromJson(str, MyPushInfo.class);
                if (mNews != null && !TextUtils.isEmpty(mNews.getMessageId())) {
                    NewsJumpHelper.startAcceptActivity(context, mNews);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_COME_UNREAD, mNews.getType() + "-" + mNews.getLabel()));
                } else if (mInfo != null && !TextUtils.isEmpty(mInfo.getMsgId())) {

                }

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        }
    }
}