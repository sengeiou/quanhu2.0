package com.rz.circled.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.widget.ImageView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.StringFormatUtil;
import com.rz.httpapi.bean.CircleEntrModle;

/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchCircleAdapter extends SearchCommonAdapter {

    private int[] resourceColors = {R.drawable.resource_blue, R.drawable.resource_red, R.drawable.resource_yellow,
            R.drawable.resource_green, R.drawable.resource_pink, R.drawable.resource_purple, R.drawable.resource_cyan};
    private String keyWord = "";
    StringFormatUtil stringFormatUtil;

    public SearchCircleAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }


    @Override
    public void convert(ViewHolder helper, Object item, int position) {
        CircleEntrModle model = (CircleEntrModle) item;
        TextView tvName = (TextView) helper.getViewById(R.id.id_friends_name);
        ImageView mImgHead = (ImageView) helper.getViewById(R.id.id_friends_img);

        int num = position % 7;
        mImgHead.setImageResource(resourceColors[num]);
        tvName.setText(model.circleName);

//        tvName.setSelected(model.isSeleced());
//        tvName.setText("");


//        stringFormatUtil = new StringFormatUtil(mContext, model.circleName, keyWord, R.color.colorAccent).fillColor();
//        if(stringFormatUtil != null && stringFormatUtil.getResult() != null){
//            tvName.setText(stringFormatUtil.getResult());
//        }else{
//            tvName.setText(model.circleName);
//        }

    }
}
