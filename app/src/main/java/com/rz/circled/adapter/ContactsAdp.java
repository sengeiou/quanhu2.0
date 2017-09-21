package com.rz.circled.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.Protect;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.FriendInformationBean;

import java.util.List;

public class ContactsAdp extends CommonAdapter<FriendInformationBean> {
    private boolean isHideChat;

    public ContactsAdp(Context context, List<FriendInformationBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    public void setHideChat(boolean isHideChat) {
        this.isHideChat = isHideChat;
    }

    @Override
    public void convert(ViewHolder helper, FriendInformationBean model, int position) {
        TextView tvName = (TextView) helper.getViewById(R.id.id_friends_name);
        View line = helper.getViewById(R.id.line);
        TextView tvAlpha = (TextView) helper.getViewById(R.id.id_alpha_txt);
        ImageView mImgHead = (ImageView) helper.getViewById(R.id.id_friends_img);

        if (null != model) {

            if (Protect.checkLoadImageStatus(mContext)) {
                Glide.with(mContext).load(model.getCustImg())
                        .transform(new GlideCircleImage(mContext))
                        .placeholder(R.drawable.ic_default_head).into(mImgHead);
            }

            if (!StringUtils.isEmpty(model.getCustNname())) {
                tvName.setText(model.getCustNname());
            }
//            /**
//             * 备注覆盖昵称
//             */
            if (!StringUtils.isEmpty(model.getNameNotes())) {
                tvName.setText(model.getNameNotes());
            }

            // 根据position获取分类的首字母的Char ascii值
            if (getCount() > 0) {
                int section = getSectionForPosition(position);
                // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
                if (position == getPositionForSection(section) && !isHideChat) {
                    tvAlpha.setVisibility(View.VISIBLE);
                    tvAlpha.setText(model.getFirstLetter());
                    line.setVisibility(View.GONE);
                } else {
                    tvAlpha.setVisibility(View.GONE);
                    line.setVisibility(View.VISIBLE);
                }
            } else {
                tvAlpha.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return (getItem(position)).getFirstLetter().charAt(0);
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
            String sortStr = (getItem(i)).getFirstLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}
