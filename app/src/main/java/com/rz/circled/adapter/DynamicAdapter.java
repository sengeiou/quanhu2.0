package com.rz.circled.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.widget.CircleImageView;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.utils.Protect;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.CircleDynamic;

import java.util.List;


/**
 * Created by Administrator on 2017/3/30/030.
 */

public class DynamicAdapter extends CircleContentAdapter {


    public DynamicAdapter(Context context, List mDatas) {
        super(context, mDatas, R.layout.item_dynamic_home);
    }


    @Override
    public void convert(ViewHolder helper, final CircleDynamic item) {
        bindCircleContent(helper, item);

        ImageView mCivHead = helper.getView(R.id.civ_head);//用户头像
        CircleImageView mCivSuperV = helper.getView(R.id.civ_superV);//头像加V
        TextView mTvName = helper.getView(R.id.tv_name);//用户名字
        TextView mTvTime = helper.getView(R.id.tv_time);//时间
        TextView mTvCircleName = helper.getView(R.id.tv_circle_name);//圈子来自

//        ImageView iv_other_left = (ImageView) helper.getViewById(R.id.iv_other_left);
//        TextView tv_other_left = (TextView) helper.getViewById(R.id.tv_other_left);

//        ImageView ll_other_left_type1_iv1 = (ImageView) helper.getViewById(R.id.ll_other_left_type1_iv1);
//        TextView ll_other_left_type1_tv2 = (TextView) helper.getViewById(R.id.ll_other_left_type1_tv2);
//        ImageView ll_other_mid_type1_iv1 = (ImageView) helper.getViewById(R.id.ll_other_mid_type1_iv1);
//        TextView ll_other_mid_type1_tv2 = (TextView) helper.getViewById(R.id.ll_other_mid_type1_tv2);
//        ImageView ll_other_right_type1_iv1 = (ImageView) helper.getViewById(R.id.ll_other_right_type1_iv1);
//        TextView ll_other_right_type1_tv2 = (TextView) helper.getViewById(R.id.ll_other_right_type1_tv2);
//        ViewGroup rl_other_left_content = helper.getView(R.id.rl_other_left_content);//rl 左边的other信息 content

        if (Protect.checkLoadImageStatus(mContext) && item.user != null) {
            Glide.with(mContext).load(item.user.custImg).transform(new GlideCircleImage(mContext)).
                    placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).into(mCivHead);
        }
        if (item.user != null) {
            mTvName.setText(Protect.setName(item.user.custNname, item.user.custNameNote));
        }

        mTvTime.setText(StringUtils.formatDisplayTime(item.createTime, "yyyy-MM-dd HH:mm:ss"));
        mTvCircleName.setText(mContext.getString(R.string.from)  + item.circleName);
        mTvCircleName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin()) {
//                    WebContainerAty.startAty(mContext, item.circleUrl);
                }
            }
        });
        mCivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin()) {
                    if (item.user != null) {
//                        V3FriendInfoActivity.newFrindInfo(mContext, item.user.custId);
                    }
                }
            }
        });
        mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin()) {
                    if (item.user != null) {
//                        V3FriendInfoActivity.newFrindInfo(mContext, item.user.custId);
                    }
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