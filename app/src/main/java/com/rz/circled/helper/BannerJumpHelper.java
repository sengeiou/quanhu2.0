package com.rz.circled.helper;

import android.app.Activity;
import android.content.Intent;

import com.rz.circled.R;
import com.rz.circled.event.EventConstant;
import com.rz.circled.ui.activity.CommonH5Activity;
import com.rz.circled.ui.activity.MainActivity;
import com.rz.circled.ui.activity.UserInfoActivity;
import com.rz.circled.ui.activity.VideoH5Aty;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.common.event.BaseEvent;
import com.rz.common.utils.StringUtils;
import com.yryz.yunxinim.uikit.common.util.string.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * Created by Administrator on 2017/11/14 0014.
 */

public class BannerJumpHelper {

    public static String tab_html = "1";
    public static String tab_native = "2";
    public static String user_info_acy = "2001";
    public static String reward_aty = "2002";

    public static void bannerJumpHanderHelper(Activity mActivity,String url,boolean jssdk){
        if(url.startsWith("quanhu:")){
            Intent intent = new Intent();
            Map parametersMap = StringUtils.getParameters(url);
            String jumpUrl = (String) parametersMap.get("url");
            String type = (String) parametersMap.get("type");
            String category = (String) parametersMap.get("category");
            if(tab_html.equals(type)){
                WebContainerActivity.startActivity(mActivity, jumpUrl, true);
            }else if(tab_native.equals(type)){
                if(user_info_acy.equals(category)){    //个人中心
                    String custId = (String) parametersMap.get("custId");
                    if(!StringUtil.isEmpty(custId)){
                        UserInfoActivity.newFrindInfo(mActivity,custId);
                    }
                }else if(reward_aty.equals(category)){  //悬赏

                    mActivity.finish();
                    intent.setClass(mActivity,MainActivity.class);
                    mActivity.startActivity(intent);
                    //发送event到
                    EventBus.getDefault().post(new BaseEvent(EventConstant.SET_REWARD_TAB));
                }
            }
        }else{
            if(jssdk){
                WebContainerActivity.startActivity(mActivity, url, true);
            }else{
                CommonH5Activity.startCommonH5(mActivity, "", url);
            }
        }
    }

    public static void bannerJumpActivityHelper(Activity mActivity, String url){
        Intent intent = new Intent();
        Map parametersMap = StringUtils.getParameters(url);
        String jumpUrl = (String) parametersMap.get("url");
        String type = (String) parametersMap.get("type");
        String category = (String) parametersMap.get("category");

        if(tab_html.equals(type)){
            if(!StringUtil.isEmpty(jumpUrl)){
                if (jumpUrl.contains("opus")) {
                    if (jumpUrl.contains("opus-h")) {
                        VideoH5Aty.startCommonH5(mActivity, jumpUrl, mActivity.getString(R.string.app_name));
                    } else {
                        WebContainerActivity.startActivity(mActivity, jumpUrl, true);
                    }
                } else {
                    WebContainerActivity.startActivity(mActivity, jumpUrl, true);
                }
            }
        }else if(tab_native.equals(type)){
            if(user_info_acy.equals(category)){    //个人中心
                String custId = (String) parametersMap.get("custId");
                if(!StringUtil.isEmpty(custId)){
                    UserInfoActivity.newFrindInfo(mActivity,custId);
                }
            }else if(reward_aty.equals(category)){  //悬赏
                intent.setClass(mActivity,MainActivity.class);
                mActivity.startActivity(intent);
                //发送event到
                EventBus.getDefault().post(new BaseEvent(EventConstant.SET_REWARD_TAB));
            }
        }else{
            if (url.contains("opus")) {
                if (url.contains("opus-h")) {

                    VideoH5Aty.startCommonH5(mActivity, url, mActivity.getString(R.string.app_name),1020);
                } else {
                    WebContainerActivity.startActivity(mActivity, url, true);
                }
            } else {
                WebContainerActivity.startActivity(mActivity, url, true);
            }
        }
    }

}
