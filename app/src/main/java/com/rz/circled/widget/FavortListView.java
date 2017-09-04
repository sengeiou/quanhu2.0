package com.rz.circled.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.rz.circled.ui.view.circle_favite.FavortListAdapter;
import com.rz.circled.ui.view.circle_favite.FavortShowListAdapter;
import com.rz.circled.ui.view.circle_favite.ISpanClick;


/**
 * @author wsf
 * @Description:
 * @date 16/3/10 16:54
 */
public class FavortListView extends TextView{
    private ISpanClick mSpanClickListener;

    public void setSpanClickListener(ISpanClick listener){
        mSpanClickListener = listener;
    }
    public ISpanClick getSpanClickListener(){
        return  mSpanClickListener;
    }

    public FavortListView(Context context) {
        super(context);
    }

    public FavortListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FavortListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAdapter(FavortListAdapter adapter){
        adapter.bindListView(this);
    }
    
    public void setAdapter2(FavortShowListAdapter adapter){
        adapter.bindListView(this);
    }

}
