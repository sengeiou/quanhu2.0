package com.yryz.yunxinim.uikit.team.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.yryz.yunxinim.uikit.common.adapter.TAdapter;
import com.yryz.yunxinim.uikit.common.adapter.TAdapterDelegate;
import com.yryz.yunxinim.uikit.team.viewholder.TeamMemberHolder;
import com.yryz.yunxinim.uikit.team.viewholder.TeamMemberSwitchHolder;

import java.util.List;

public class TeamMemberSwitchAdapter extends TAdapter {

    private Context context;

    public TeamMemberSwitchAdapter(Context context, List<?> items, TAdapterDelegate delegate) {
        super(context, items, delegate);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        return view;
    }
}
