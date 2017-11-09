package com.rz.circled.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.CircleEntrModle;

/**
 * Created by Administrator on 2017/11/8/008.
 */

public class CircleAdapter extends CommonAdapter<CircleEntrModle> {
    private int[] resourceColors={R.drawable.resource_blue,R.drawable.resource_red,R.drawable.resource_yellow,R.drawable.resource_green,R.drawable.resource_pink,R.drawable.resource_purple,R.drawable.resource_cyan};
    public CircleAdapter(Context context, int itemLayoutId) {
        super(context,itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, CircleEntrModle model, int position) {
        TextView tvName = (TextView) helper.getViewById(R.id.id_friends_name);
        ImageView mImgHead = (ImageView) helper.getViewById(R.id.id_friends_img);
        if (null != model) {
//                Glide.with(mContext).load(model.getCircleIcon())
//                        .transform(new GlideCircleImage(mContext))
//                        .placeholder(R.drawable.circle_default).into(mImgHead);
                int num=position%7;
                mImgHead.setImageResource(resourceColors[num]);


            if (!StringUtils.isEmpty(model.getCircleName())) {
                tvName.setText(model.getCircleName());
            }

            // 根据position获取分类的首字母的Char ascii值
//            if (getCount() > 0) {
//                int section = getSectionForPosition(position);
//                // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
//                if (position == getPositionForSection(section) && !isHideChat) {
//                    tvAlpha.setVisibility(View.VISIBLE);
//                    tvAlpha.setText(model.getFirstLetter());
//                    line.setVisibility(View.GONE);
//                } else {
//                    tvAlpha.setVisibility(View.GONE);
//                    line.setVisibility(View.VISIBLE);
//                }
//            } else {
//                tvAlpha.setVisibility(View.GONE);
//                line.setVisibility(View.GONE);
//            }
        }
    }
}
