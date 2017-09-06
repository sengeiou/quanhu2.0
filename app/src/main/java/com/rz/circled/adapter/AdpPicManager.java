package com.rz.circled.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.common.utils.Protect;
import com.rz.httpapi.bean.PictureModel;

import java.util.List;

/**
 * 图片管理
 */
public class AdpPicManager extends BaseAdapter<PictureModel> {

    private PictureModel mAddCameraPic;

    public void setmAddCameraPic(PictureModel mAddCameraPic) {
        this.mAddCameraPic = mAddCameraPic;
    }

    public AdpPicManager(Context context, List<PictureModel> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (null == view) {
            holder = new ViewHolder();
            view = inflate(R.layout.adp_pic_mag, null);
            holder.mImgBg = (ImageView) view.findViewById(R.id.id_manager_pic);
            holder.mCheckBox = (CheckBox) view
                    .findViewById(R.id.id_manager_check);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        PictureModel model = getItem(position);
        if (model == mAddCameraPic) {
            holder.mCheckBox.setVisibility(View.GONE);
            holder.mImgBg.setImageBitmap(model.getmBitmap());
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
            if (model.getmPicPath() != null && Protect.checkLoadImageStatus(context)) {
                Glide.with(context).load(model.getmPicPath()).into(holder.mImgBg);
            }
//                ImageLoader.getInstance(3, Type.LIFO).loadImage(
//                        model.getmPicPath(), holder.mImgBg);
            holder.mCheckBox.setChecked(model.isSelect());
        }
        return view;
    }

    /**
     * 某张图片被选中或者取消
     *
     * @param position 选中图片的地址
     *                 剩余可选择的图片
     */
    public void setPicSelect(int position, boolean flag) {
        getItem(position).setSelect(flag);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private ImageView mImgBg;

        private CheckBox mCheckBox;
    }
}
