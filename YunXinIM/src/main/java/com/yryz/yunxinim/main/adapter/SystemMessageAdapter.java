package com.yryz.yunxinim.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.yryz.yunxinim.main.viewholder.SystemMessageViewHolder;
import com.yryz.yunxinim.uikit.common.adapter.TAdapter;
import com.yryz.yunxinim.uikit.common.adapter.TAdapterDelegate;

import java.util.List;

public class SystemMessageAdapter extends TAdapter {

    private SystemMessageViewHolder.SystemMessageListener systemMessageListener;

    public SystemMessageAdapter(Context context, List<?> items, TAdapterDelegate delegate,
                                SystemMessageViewHolder.SystemMessageListener listener) {
        super(context, items, delegate);
        this.systemMessageListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (systemMessageListener != null) {
            ((SystemMessageViewHolder) view.getTag()).setListener(systemMessageListener);
        }

        return view;
    }
}
