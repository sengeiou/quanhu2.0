package com.rz.circled.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
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
import com.rz.httpapi.bean.CircleDynamic;
import com.rz.httpapi.bean.MyBuyingModel;

import java.util.List;


/**
 * Created by Administrator on 2017/3/30/030.
 */

public class BuyingAdapter extends BuyingContentAdapter {


    public BuyingAdapter(Context context, List mDatas) {
        super(context, mDatas, R.layout.item_dynamic_home);
    }

    @Override
    public void convert(ViewHolder helper, final MyBuyingModel item) {


        bindCircleContent(helper, item);
//        ImageView mCivHead = helper.getView(R.id.civ_head);//用户头像
//        CircleImageView mCivSuperV = helper.getView(R.id.civ_superV);//头像加V
//        TextView mTvName = helper.getView(R.id.tv_name);//用户名字
//        TextView mTvTime = helper.getView(R.id.tv_time);//时间
        TextView mTvCircleName = helper.getView(R.id.tv_circle_name);//圈子来自

//        ImageView iv_other_left = (ImageView) helper.getViewById(R.id.iv_other_left);
        TextView tv_other_left = (TextView) helper.getViewById(R.id.tv_other_left);

//        ImageView ll_other_left_type1_iv1 = (ImageView) helper.getViewById(R.id.ll_other_left_type1_iv1);
//        TextView ll_other_left_type1_tv2 = (TextView) helper.getViewById(R.id.ll_other_left_type1_tv2);
//        ImageView ll_other_mid_type1_iv1 = (ImageView) helper.getViewById(R.id.ll_other_mid_type1_iv1);
//        TextView ll_other_mid_type1_tv2 = (TextView) helper.getViewById(R.id.ll_other_mid_type1_tv2);
//        ImageView ll_other_right_type1_iv1 = (ImageView) helper.getViewById(R.id.ll_other_right_type1_iv1);
        TextView fromWhere = (TextView) helper.getViewById(R.id.ll_other_right_type1_tv2);
//        ViewGroup rl_other_left_content = helper.getView(R.id.rl_other_left_content);//rl 左边的other信息 content

//        if (Protect.checkLoadImageStatus(mContext) && item.getResource().getCust()!= null) {
//            Glide.with(mContext).load(item.getResource().getCust().getCustImg()).transform(new GlideCircleImage(mContext)).
//                    placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).into(mCivHead);
//        }

//        mCivSuperV.setVisibility(item.talentType==1?View.VISIBLE:View.GONE);

//        if (item.getResource().getCust() != null) {
//            mTvName.setText(item.getResource().getCustNname()== null ? "" : item.getResource().getCust().getCustNname());
//        }
        tv_other_left.setText(item.getAmount()+"悠然币");
        if (item.getResource().getCoterieId()==null||item.getResource().getCoterieName()==null){
            fromWhere.setText("来自圈子"+item.getResource().getCircleName());
        }else {
            fromWhere.setText("来自私圈"+item.getResource().getCoterieName());
        }
//        mTvTime.setText(StringUtils.stampToDate(item.getResource().getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        fromWhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getResource().getCoterieId()==null||item.getResource().getCoterieName()==null){
                    WebContainerActivity.startActivity(mContext, BuildConfig.WebHomeBaseUrl+"/"+item.getResource().getCircleRoute()+"/");
                }else {
                    WebContainerActivity.startActivity(mContext, BuildConfig.WebHomeBaseUrl+"/"+item.getResource().getCircleRoute()+"/coterie/"+item.getResource().getCoterieId());
                }
            }
        });
//        mCivHead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isLogin()) {
//                    if (item.getResource().getCust() != null) {
//                        UserInfoActivity.newFrindInfo(mContext, item.getResource().getCust().getCustId());
//                    }
//                }
//            }
//        });
//        mTvName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isLogin()) {
//                    if (item.getResource().getCust() != null) {
//                        UserInfoActivity.newFrindInfo(mContext, item.getCustId());
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
