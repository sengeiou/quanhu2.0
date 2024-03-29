package com.rz.circled.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.ui.activity.EditorTwoActivity;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;

/**
 * 图片管理
 */
public class PicAdapter extends CommonAdapter<String> {

    public PicAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, final String model, int position) {

        ImageView imageView = (ImageView) helper.getViewById(R.id.iv_item_editor_two_pic);
        Glide.with(mContext).load(model).into(imageView);

        View viewDelete = helper.getViewById(R.id.tv_item_editor_two_pic_delete);

        viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String item = (String) v.getTag();
                if (mContext instanceof EditorTwoActivity) {
                    ((EditorTwoActivity) mContext).deletePic(item);
                }

            }
        });
    }


}
