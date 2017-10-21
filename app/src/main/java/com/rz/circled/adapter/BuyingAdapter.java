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
import com.rz.common.utils.Currency;
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
        super(context, mDatas, R.layout.item_buy_home);
    }

    @Override
    public void convert(ViewHolder helper, final MyBuyingModel item) {


        bindCircleContent(helper, item);

        TextView mTvCircleName = helper.getView(R.id.tv_circle_name);//圈子来自

        TextView tv_other_left = (TextView) helper.getViewById(R.id.tv_other_left);


        TextView fromWhere = (TextView) helper.getViewById(R.id.ll_other_right_type1_tv2);

        tv_other_left.setText( Currency.returnDollar(Currency.RMB, item.getAmount() + "", 1));
        if (item.getResource().coterieId==null||item.getResource().coterieName==null){
            fromWhere.setText("来自圈子"+item.getResource().circleName);
        }else {
            fromWhere.setText("来自私圈"+item.getResource().coterieName);
        }
        fromWhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getResource().coterieId==null||item.getResource().coterieName==null){
                    WebContainerActivity.startActivity(mContext, BuildConfig.WebHomeBaseUrl+"/"+item.getResource().circleRoute+"/");
                }else {
                    WebContainerActivity.startActivity(mContext, BuildConfig.WebHomeBaseUrl+"/"+item.getResource().circleRoute+"/coterie/"+item.getResource().coterieId);
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
