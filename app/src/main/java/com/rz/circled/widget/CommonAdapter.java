package com.rz.circled.widget;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.rz.circled.widget.callback.IShowListOperateListener;

import java.util.List;

/**
 * 作者：Administrator on 2016/8/9 0009 10:26
 * 功能：
 * 说明：
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;

    protected Context mContext;

    protected List<T> mDatas;

    protected final int mItemLayoutId;

    public SparseBooleanArray checkMap;

    public IShowListOperateListener mShowListOperateListener;

    public void setOnIShowListOperateListener(IShowListOperateListener listener) {
        this.mShowListOperateListener = listener;
    }

    /**
     * 用来记录checkbox的状态
     */
    private boolean checkedDefaultValue = false;

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
        checkMap = new SparseBooleanArray();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        if (mDatas == null || mDatas.size() == 0)
            return null;
        if (i <= mDatas.size()) {
            return mDatas.get(i);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(i, convertView, parent);
        Object item = getItem(i);
        convert(viewHolder, (T) getItem(i));
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder helper, T item);

    protected ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
    }

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
