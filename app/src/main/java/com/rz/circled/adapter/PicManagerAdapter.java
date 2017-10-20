package com.rz.circled.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.Protect;
import com.rz.httpapi.bean.PictureModel;

import java.io.File;

/**
 * 图片管理
 */
public class PicManagerAdapter extends CommonAdapter<PictureModel> {

    private PictureModel mAddCameraPic;

    public void setmAddCameraPic(PictureModel mAddCameraPic) {
        this.mAddCameraPic = mAddCameraPic;
    }

    public PicManagerAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, PictureModel model, int position) {
        CheckBox mCheckBox = (CheckBox) holder.getViewById(R.id.cb_pic_sel);
        if (model == mAddCameraPic) {
            mCheckBox.setVisibility(View.GONE);
            ((SimpleDraweeView) holder.getViewById(R.id.iv_pic_sel)).setImageBitmap(model.getmBitmap());
        } else {
            mCheckBox.setVisibility(View.VISIBLE);
            if (model.getmPicPath() != null && Protect.checkLoadImageStatus(mContext)) {
                SimpleDraweeView draweeView = (SimpleDraweeView) holder.getViewById(R.id.iv_pic_sel);
                draweeView.setImageURI(Uri.fromFile(new File(model.getmPicPath())));
            }
            mCheckBox.setChecked(model.isSelect());
        }
    }

    public void updateSingleItem(String path, GridView mGridView) {
        if (mGridView != null) {
            int start = mGridView.getFirstVisiblePosition();
            for (int i = start, j = mGridView.getLastVisiblePosition(); i <= j; i++) {
                if (TextUtils.equals(path, ((PictureModel) mGridView.getItemAtPosition(i)).getmPicPath())) {
                    View view = mGridView.getChildAt(i - start);
                    getView(i, view, mGridView);
                    break;
                }
            }
        }
    }

}
