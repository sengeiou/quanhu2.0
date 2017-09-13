package com.rz.circled.adapter;

import android.content.Context;

import com.rz.circled.widget.ViewHolder;
import com.rz.httpapi.bean.CircleDynamic;

import java.util.List;

/**
 * Created by Administrator on 2017/9/13 0013.
 */

public class CollectionAdapter extends DynamicAdapter {


    public CollectionAdapter(Context context, List mDatas) {
        super(context, mDatas);
    }

    @Override
    public void convert(ViewHolder helper, CircleDynamic item) {

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
