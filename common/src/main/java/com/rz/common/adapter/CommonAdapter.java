package com.rz.common.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gsm on 2017/8/4.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    private List<T> mData;
    protected Context mContext;
    private int mLayoutId;
    public SparseBooleanArray checkMap;

    public CommonAdapter(Context context, int layoutId) {
        mContext = context;
        mLayoutId = layoutId;
    }

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mData = mDatas;
        this.mLayoutId = itemLayoutId;
        checkMap = new SparseBooleanArray();
    }

    /**
     * 初始設置数据
     *
     * @param list
     */
    public void setData(List<T> list) {
        if (mData != null) mData.clear();
        else mData = new ArrayList<>();
        addData(list);
    }

    /**
     * 后续添加数据
     *
     * @param list
     */
    public void addData(List<T> list) {
        if (mData != null && list != null)
            mData.addAll(list);
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public T getItem(int position) {
        return getCount() > 0 ? mData.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(i, convertView, parent);
        convert(viewHolder, (T) getItem(i), i);
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder helper, T item, int position);


    protected ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mLayoutId, position);
    }
    /**
     * 用来记录checkbox的状态
     */
    private boolean checkedDefaultValue = false;
    /**
     * 改变某个item的选中状态 <功能详细描述> 多选
     */
    public void setCheckAtPosition(int pos) {
        checkMap.put(pos, !checkMap.get(pos, checkedDefaultValue));
        notifyDataSetChanged();
    }

    /**
     * 改变某个item的选中状态 <功能详细描述> 单选
     */
    public void setCheckAtPosFalse(int pos, boolean flag) {
        for (int i = 0; i < getCount(); i++) {
            checkMap.put(i, false);
        }
        if (flag) {
            checkMap.put(pos, !checkMap.get(pos, checkedDefaultValue));
        }
        notifyDataSetChanged();
    }
}
