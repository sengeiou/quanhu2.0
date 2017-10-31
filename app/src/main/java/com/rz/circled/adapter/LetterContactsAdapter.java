package com.rz.circled.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.constant.Type;
import com.rz.common.utils.Protect;
import com.rz.httpapi.bean.FriendInformationBean;

public class LetterContactsAdapter extends CommonAdapter<FriendInformationBean> {

    public LetterContactsAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, FriendInformationBean model, int position) {
        TextView alpha = (TextView) helper.getViewById(R.id.id_alpha_txt);
        ImageView mImgHead = (ImageView) helper.getViewById(R.id.id_friends_img);
        TextView mTxtName = (TextView) helper.getViewById(R.id.id_friends_name);
        TextView mTxtBrief = (TextView) helper.getViewById(R.id.id_friends_brief);
        TextView mTxtState = (TextView) helper.getViewById(R.id.id_friends_state);

        if (null != model) {
            if (Protect.checkLoadImageStatus(mContext)) {
                Glide.with(mContext).load(model.getCustImg()).placeholder(R.drawable.ic_default_head).into(mImgHead);
            }
            /**
             *  昵称
             */
            if (!TextUtils.isEmpty(model.getCustNname())) {
                mTxtName.setText(model.getCustNname());
            }
//            /**
//             * 备注覆盖昵称
//             */
            if (!TextUtils.isEmpty(model.getNameNotes())) {
                mTxtName.setText(model.getNameNotes());
            }

            if (!TextUtils.isEmpty(model.getCustDesc())) {
                mTxtBrief.setText(model.getCustDesc());
                mTxtBrief.setVisibility(View.VISIBLE);
            } else {
                mTxtBrief.setVisibility(View.GONE);
                mTxtBrief.setText("");
            }

            if (position < 10)
                Log.e("zxw", "custNname=" + model.getCustNname() + "  relation=" + model.getRelation());
            /**
             *  根据state显示关注状态
             */
            switch (model.getRelation()) {
                case Type.relation_stranger:
                    mTxtState.setText(R.string.invite_friend);
                    mTxtState.setTextColor(mContext.getResources().getColor(R.color.font_color_blue));
                    mTxtState.setBackgroundResource(R.drawable.stoke_mobile_contacts_stats);
                    break;
                case Type.relation_friend:
                    mTxtState.setText(R.string.friends);
                    mTxtState.setTextColor(mContext.getResources().getColor(R.color.color_666666));
                    mTxtState.setBackgroundResource(android.R.color.transparent);
                    break;
                default:
                    break;

            }

            // 根据position获取分类的首字母的Char ascii值
            if (getCount() > 0) {
                int section = getSectionForPosition(position);
                // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
                if (position == getPositionForSection(section)) {
                    alpha.setVisibility(View.VISIBLE);
                    alpha.setText(model.getFirstLetter());
                } else {
                    alpha.setVisibility(View.GONE);
                }
            } else {
                alpha.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return getItem(position).getFirstLetter().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = getItem(i).getFirstLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}
