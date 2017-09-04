package com.rz.circled.ui.view.circle_favite;


import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import com.rz.circled.R;
import com.rz.circled.application.QHApplication;
import com.rz.circled.modle.FavortShowItem;
import com.rz.circled.widget.FavortListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wsf
 * @Description:
 * @date 16/3/10 16:54
 */
public class FavortShowListAdapter {

    private FavortListView mListView;
    private List<FavortShowItem> datas = new ArrayList<FavortShowItem>() ;

    public List<FavortShowItem> getDatas() {
        return datas;
    }

    public void setDatas(List<FavortShowItem> datas) {
        this.datas = datas;
    }

    @NonNull
    public void bindListView(FavortListView listview){
        if(listview == null){
            throw new IllegalArgumentException("FavortListView is null ....");
        }
        mListView = listview;
    }


    public int getCount() {
        if(datas != null && datas.size() > 0){
            return datas.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        if(datas != null && datas.size() > position){
            return datas.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public void notifyDataSetChanged(){
        if(mListView == null){
            throw new NullPointerException("listview is null, please bindListView first...");
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if(datas != null && datas.size() > 0){
            //添加点赞图标
            builder.append(setImageSpan());
            //builder.append("  ");
            FavortShowItem item = null;
            for (int i=0; i<datas.size(); i++){
                item = datas.get(i);
                if(item != null){
                    builder.append(setClickableSpan(item.custNname, i));
                    if(i != datas.size()-1){
                        builder.append(", ");
                    }
                }
            }
        }
        mListView.setText(builder);
        mListView.setMovementMethod(new CircleMovementMethod(R.color.color_F5F5F5));
    }

    @NonNull
    private SpannableString setClickableSpan(String textStr, int position) {
    	 SpannableString subjectSpanText;
    	if(textStr.equals("")){
    		  subjectSpanText = new SpannableString("");
    	} else {
    		  subjectSpanText = new SpannableString(textStr);
    	}
       
        subjectSpanText.setSpan(new NameClickable(mListView.getSpanClickListener(), position), 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    private SpannableString setImageSpan(){
        String text = "  ";
        SpannableString imgSpanText = new SpannableString(text);
        imgSpanText.setSpan(new ImageSpan(QHApplication.getContext(), R.drawable.r_hom_love, DynamicDrawableSpan.ALIGN_BASELINE),
                0 , 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return imgSpanText;
    }
}
