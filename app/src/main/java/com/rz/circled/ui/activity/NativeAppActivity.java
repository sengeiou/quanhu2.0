package com.rz.circled.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.circled.event.EventConstant;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2017/11/10 0010.
 */

public class NativeAppActivity extends BaseActivity {

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(null, null);
    }

    @Override
    public void initView() {

        Log.e("gggggg","------");

        Uri uri = getIntent().getData();
        if (uri != null)
        {
            List<String> pathSegments = uri.getPathSegments();
            String uriQuery = uri.getQuery();
            Intent intent;
            if (pathSegments != null && pathSegments.size() > 0) {
                this.getIntent().getScheme();//获得Scheme名称
                this.getIntent().getDataString();//获得Uri全部路径

                //// TODO: 2017/11/10 0010
                //
                // 解析SCHEME
//                if (someif) {
//                    dosomething();

                        //类型 tab=1 时 webview启动
//                    CommonH5Activity.startCommonH5();

                    //类型 tab=2 是跳转原生
                String tab = "";
                String categary = "";

                if(tab.equals(1)){
//                    CommonH5Activity.startCommonH5();
                }else{
                    Intent toIntent = new Intent();
                    if(categary.equals("2001")){    //个人中心
                        toIntent.setClass(this,UserInfoActivity.class);
                        startActivity(toIntent);
                    }else if(categary.equals("2002")){  //悬赏
                        toIntent.setClass(this,MainActivity.class);
                        startActivity(toIntent);
                        //发送event到
                        EventBus.getDefault().post(new BaseEvent(EventConstant.SET_REWARD_TAB));
                    }
                }
//                }
//                else {
//                    // 若解析不到SCHEME，则关闭NativeAppActivity；
//                    finish();
//                }
            } else {
                finish();
            }
        } else {
            finish();
        }

    }

    @Override
    public void initData() {

    }

    @Override
    public void refreshPage() {

    }
}
