package com.rz.circled.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.circled.ui.activity.UserInfoActivity;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.CircleImageView;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.utils.Protect;
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

public class ArticleAdapter extends CircleContentAdapter {


    public ArticleAdapter(Context context, List mDatas) {
        super(context, mDatas, R.layout.item_dynamic_home);
    }

    @Override
    public void convert(ViewHolder helper, final CircleDynamic item) {
        bindCircleContent(helper, item);
//        ImageView mCivHead = helper.getView(R.id.civ_head);//用户头像
//        CircleImageView mCivSuperV = helper.getView(R.id.civ_superV);//头像加V
//        TextView mTvName = helper.getView(R.id.tv_name);//用户名字
//        TextView mTvTime = helper.getView(R.id.tv_time);//时间
        TextView mTvCircleName = helper.getView(R.id.tv_circle_name);//圈子来自

        LinearLayout  hewadLayout = helper.getView(R.id.user_head_layout);
        hewadLayout.setVisibility(View.GONE);

//        ImageView iv_other_left = (ImageView) helper.getViewById(R.id.iv_other_left);
        TextView tv_other_left = (TextView) helper.getViewById(R.id.tv_other_left);

//        ImageView ll_other_left_type1_iv1 = (ImageView) helper.getViewById(R.id.ll_other_left_type1_iv1);
//        TextView ll_other_left_type1_tv2 = (TextView) helper.getViewById(R.id.ll_other_left_type1_tv2);
//        ImageView ll_other_mid_type1_iv1 = (ImageView) helper.getViewById(R.id.ll_other_mid_type1_iv1);
//        TextView ll_other_mid_type1_tv2 = (TextView) helper.getViewById(R.id.ll_other_mid_type1_tv2);
//        ImageView ll_other_right_type1_iv1 = (ImageView) helper.getViewById(R.id.ll_other_right_type1_iv1);
        TextView fromWhere = (TextView) helper.getViewById(R.id.ll_other_right_type1_tv2);
//        ViewGroup rl_other_left_content = helper.getView(R.id.rl_other_left_content);//rl 左边的other信息 content

//        if (Protect.checkLoadImageStatus(mContext) && item.cust != null) {
//            Glide.with(mContext).load(item.cust.custImg).transform(new GlideCircleImage(mContext)).
//                    placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).into(mCivHead);
//        }

//        mCivSuperV.setVisibility(item.talentType==1?View.VISIBLE:View.GONE);

//        if (item.cust != null) {
//            mTvName.setText(item.cust.custNname == null ? "" : item.cust.custNname);
//        }
//        tv_other_left.setText(item.readNum+" 阅读");
        if (StringUtil.isEmpty(item.coterieId)||StringUtil.isEmpty(item.coterieName)){
            fromWhere.setText("来自圈子 "+item.circleName);
        }else {
            fromWhere.setText("来自私圈 "+item.coterieName);
        }
//        mTvTime.setText(StringUtils.stampToDate(item.createTime, "yyyy-MM-dd HH:mm:ss"));
        tv_other_left.setText(StringUtils.stampToDate(item.createTime, "yyyy-MM-dd"));


        //获取当前时间
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);

        long lt = new Long(item.createTime);
        Date date = new Date(lt);
        String res = sdf.format(date);
        String time = TimeUtil.getTime(res, dateNowStr);
        String timeArray[] = time.replace("-", "").split(",");
        String time1 = timeArray[0];
        String time2 = timeArray[1];
        String time3 = timeArray[2];

        if (Integer.parseInt(time1) > 0) {
            if (Integer.valueOf(time1) >= 2) {
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                tv_other_left.setText(sdf2.format(item.createTime));
            } else {
                tv_other_left.setText(time1 + "天" + time2 + "小时" + time3 + "分钟前");
            }
        } else {
            if (Integer.parseInt(time2) > 0) {
                tv_other_left.setText(time2 + "小时" + time3 + "分钟前");
            } else if (Integer.parseInt(time3) > 0) {
                tv_other_left.setText(time3 + "分钟前");
            } else {
                tv_other_left.setText("刚刚");
            }
        }


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
//        mCivHead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isLogin()) {
//                    if (item.cust != null) {
//                        UserInfoActivity.newFrindInfo(mContext, item.cust.custId);
//                    }
//                }
//            }
//        });
//        mTvName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isLogin()) {
//                    if (item.cust != null) {
//                        UserInfoActivity.newFrindInfo(mContext, item.cust.custId);
//                    }
//                }
//            }
//        });

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
