package com.rz.circled.adapter;

import android.content.Context;
import android.view.View;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.httpapi.bean.ImageFloder;

/**
 * 图片文件夹展示
 */
public class PicDirPopupAdapter extends CommonAdapter<ImageFloder> {

    public PicDirPopupAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, ImageFloder item, int position) {
        if (item==null)return;
        helper.setText(R.id.tv_pic_dir_name,item.getmPicName());
        helper.setText(R.id.tv_pic_dir_count,item.getmPicCount() + mContext.getString(R.string.zhang));
        if (item.getmPicSelect() == position) {
            helper.getViewById(R.id.iv_pic_dir_choose_img).setVisibility(View.VISIBLE);
        } else {
            helper.getViewById(R.id.iv_pic_dir_choose_img).setVisibility(View.GONE);
        }
    }

}
