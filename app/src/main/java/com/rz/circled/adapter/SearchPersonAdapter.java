package com.rz.circled.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;

/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchPersonAdapter extends CommonAdapter {

    private boolean isTalentPage = false;
    private String keyWord;

    public SearchPersonAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    public void setTalentPage(boolean isTalentPage) {
        this.isTalentPage = isTalentPage;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public void convert(ViewHolder helper, Object item, int position) {
        TextView tvName = (TextView) helper.getViewById(R.id.tv_search_person_name);
        TextView tvInfo = (TextView) helper.getViewById(R.id.tv_search_person_info);
        TextView tvLv = (TextView) helper.getViewById(R.id.tv_search_person_lv);


        ImageView ivIcon = (ImageView) helper.getViewById(R.id.iv_search_person);
        ImageView ivTalent = (ImageView) helper.getViewById(R.id.iv_search_person_talent);

        //name  或者 info 包含keyWord 则显示为蓝色字体


        if (isTalentPage) {//达人列表
            int basePadding = (int) mContext.getResources().getDimension(R.dimen.px20);
            tvName.setPadding(0, basePadding, 0, basePadding);
            tvInfo.setPadding(0, basePadding, 0, basePadding);
            tvLv.setVisibility(View.VISIBLE);
        }

    }
}
