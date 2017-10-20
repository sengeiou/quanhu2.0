package com.rz.circled.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.utils.StringUtils;
import com.rz.common.utils.TimeUtil;
import com.rz.httpapi.bean.CircleDynamic;
import com.yryz.yunxinim.uikit.common.util.string.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2017/3/30/030.
 */

public class SearchContentAdapter extends CircleContentAdapter {


    public SearchContentAdapter(Context context, List mDatas) {
        super(context, mDatas, R.layout.item_dynamic_home);
    }

    @Override
    public void convert(ViewHolder helper, final CircleDynamic item) {
        bindCircleContent(helper, item);

        LinearLayout  hewadLayout = helper.getView(R.id.user_head_layout);
        hewadLayout.setVisibility(View.GONE);

        TextView tv_other_left = (TextView) helper.getViewById(R.id.tv_other_left);

        TextView fromWhere = (TextView) helper.getViewById(R.id.ll_other_right_type1_tv2);

        if (StringUtil.isEmpty(item.coterieId)||StringUtil.isEmpty(item.coterieName)){
            fromWhere.setText("来自圈子 "+item.circleName);
        }else {
            fromWhere.setText("来自私圈 "+item.coterieName);
        }
        tv_other_left.setText(item.readNum+ "  阅读");

        fromWhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(item.coterieId)||StringUtil.isEmpty(item.coterieName)){
                    WebContainerActivity.startActivity(mContext, BuildConfig.WebHomeBaseUrl+"/"+item.circleRoute+"/");
                }else {
                    WebContainerActivity.startActivity(mContext, BuildConfig.WebHomeBaseUrl+"/"+item.circleRoute+"/coterie/"+item.coterieId);
                }
            }
        });

    }

    public boolean isLogin() {
        if (Session.getUserIsLogin()) {
            return true;
        } else {
//            Intent login = new Intent(mContext, LoginAty.class);
//            ((Activity) mContext).startActivityForResult(login, IntentCode.Login.LOGIN_REQUEST_CODE);
            return false;
        }
    }

}
