package com.rz.circled.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.Protect;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.FriendInformationBean;

public class ContactsSelectAdp extends CommonAdapter {


    public ContactsSelectAdp(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, Object item, int position) {
        FriendInformationBean model = (FriendInformationBean) getItem(position);
        RelativeLayout view = helper.getView(R.id.rl_contacts_select_item);
        TextView tvName = (TextView) helper.getViewById(R.id.id_friends_name);
        ImageView mImgSelect = (ImageView) helper.getViewById(R.id.imgSelect);
        TextView tvAlpha = (TextView) helper.getViewById(R.id.id_alpha_txt);
        View divider = helper.getViewById(R.id.view_divider);
        ImageView mImgHead = (ImageView) helper.getViewById(R.id.id_friends_img);


        if (null != model) {

            if (Protect.checkLoadImageStatus(mContext)) {
                Glide.with(mContext).load(model.getCustImg()).placeholder(R.drawable.ic_default_head).into(mImgHead);
            }

            if (!StringUtils.isEmpty(model.getCustNname())) {
                tvName.setText(model.getCustNname());
            }
            /**
             * 备注覆盖昵称
             */
            if (!StringUtils.isEmpty(model.getNameNotes())) {
                tvName.setText(model.getNameNotes());
            }

            if (model.isDisable()) {
                mImgSelect.setImageResource(com.yryz.yunxinim.uikit.R.drawable.nim_contact_checkbox_checked_grey);
                view.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            } else if (model.isSelect()) {
                mImgSelect.setImageResource(com.yryz.yunxinim.uikit.R.drawable.ic_pay_checked);
                view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.comm_item_selector));
            } else {
                mImgSelect.setImageResource(com.yryz.yunxinim.uikit.R.drawable.ic_pay_unchecked);
                view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.comm_item_selector));
            }

            // 根据position获取分类的首字母的Char ascii值
            if (getCount() > 0) {
                if (position == 0) {
                    divider.setVisibility(View.VISIBLE);
                } else {
                    divider.setVisibility(View.GONE);
                }
                int section = getSectionForPosition(position);
                // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
                if (position == getPositionForSection(section)) {
                    tvAlpha.setVisibility(View.VISIBLE);
                    tvAlpha.setText(model.getFirstLetter());
                } else {
                    tvAlpha.setVisibility(View.GONE);
                }
            } else {
                tvAlpha.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return ((FriendInformationBean) getItem(position)).getFirstLetter().charAt(0);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = ((FriendInformationBean) getItem(i)).getFirstLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}
