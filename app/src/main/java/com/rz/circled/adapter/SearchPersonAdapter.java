package com.rz.circled.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.common.adapter.ViewHolder;
import com.rz.httpapi.bean.StarListBean;

/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchPersonAdapter extends SearchCommonAdapter {

    private boolean isTalentPage = false;

    public SearchPersonAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    public void setTalentPage(boolean isTalentPage) {
        this.isTalentPage = isTalentPage;
    }

    @Override
    public void convert(ViewHolder helper, Object item, int position) {
        StarListBean starListBean= (StarListBean) item;
        TextView tvName = (TextView) helper.getViewById(R.id.tv_search_person_name);
        TextView tvInfo = (TextView) helper.getViewById(R.id.tv_search_person_info);
        TextView tvLv = (TextView) helper.getViewById(R.id.tv_search_person_lv);
        ImageView ivIcon = (ImageView) helper.getViewById(R.id.iv_search_person);
        ImageView ivTalent = (ImageView) helper.getViewById(R.id.iv_search_person_talent);

        //name  或者 info 包含keyWord 则显示为蓝色字体
        tvName.setText(starListBean.custInfo.getCustNname());
        tvName.append(getSpan(""));//替换文本
        Glide.with(mContext).load(starListBean.custInfo.getCustImg()).placeholder(R.drawable.iv_famous_defaule).transform(new GlideCircleImage(mContext)).into(ivIcon);
        tvInfo.setText(starListBean.starInfo.getTradeField());
        tvInfo.append(getSpan(""));//替换文本

        if (isTalentPage) {//达人列表
            int basePadding = (int) mContext.getResources().getDimension(R.dimen.px20);
            tvName.setPadding(0, basePadding, 0, basePadding);
            tvInfo.setPadding(0, basePadding, 0, basePadding);
            tvLv.setVisibility(View.VISIBLE);
            tvLv.setText("Lv"+starListBean.custInfo.getCustLevel());
        }

    }
}
