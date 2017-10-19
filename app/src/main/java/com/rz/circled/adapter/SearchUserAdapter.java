package com.rz.circled.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.utils.StringFormatUtil;
import com.rz.httpapi.bean.StarListBean;

/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchUserAdapter extends SearchCommonAdapter {

    private boolean isTalentPage = false;
    private String keyWord = "";
    StringFormatUtil stringFormatUtil;

    public SearchUserAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    public void setKeyWord(String keyWord){
        this.keyWord = keyWord;
    }

    public void setTalentPage(boolean isTalentPage) {
        this.isTalentPage = isTalentPage;
    }

    @Override
    public void convert(ViewHolder helper, Object item, int position) {
        StarListBean.CustInfoBean starListBean= (StarListBean.CustInfoBean) item;
        TextView tvName = (TextView) helper.getViewById(R.id.tv_search_person_name);
        TextView tvInfo = (TextView) helper.getViewById(R.id.tv_search_person_info);
        TextView tvLv = (TextView) helper.getViewById(R.id.tv_search_person_lv);
        ImageView ivIcon = (ImageView) helper.getViewById(R.id.iv_search_person);
        ImageView ivTalent = (ImageView) helper.getViewById(R.id.iv_search_person_talent);

        stringFormatUtil = new StringFormatUtil(mContext, starListBean.getCustNname(), keyWord, R.color.colorAccent).fillColor();

                //name  或者 info 包含keyWord 则显示为蓝色字体
        tvName.setText(stringFormatUtil.getResult());
//        tvName.append(getSpan(""));//替换文本
        Glide.with(mContext).load(starListBean.getCustImg()).transform(new GlideCircleImage(mContext)).
                placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(ivIcon);


        if(!TextUtils.isEmpty(starListBean.getCustDesc())){
            tvInfo.setText(starListBean.getCustDesc());
        }

        if(starListBean.getCustRole() == 0){
            ivTalent.setVisibility(View.GONE);
        }else{
            ivTalent.setVisibility(View.VISIBLE);
        }

//        tvInfo.append(getSpan(""));//替换文本

        if (isTalentPage) {//达人列表
            int basePadding = (int) mContext.getResources().getDimension(R.dimen.px20);
            tvName.setPadding(0, basePadding, 0, basePadding);
            tvInfo.setPadding(0, basePadding, 0, basePadding);
            tvLv.setVisibility(View.GONE);
//            tvLv.setText("Lv"+starListBean.getCustLevel());
        }

    }
}
