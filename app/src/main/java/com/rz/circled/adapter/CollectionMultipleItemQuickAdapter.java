package com.rz.circled.adapter;

import com.chad.library.adapter.base.*;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rz.circled.modle.MultipleItem;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class CollectionMultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem> {

    public CollectionMultipleItemQuickAdapter(List data) {
        super(data);
//        addItemType(MultipleItem.HOME_TYPE, R.layout.text_view);
//        addItemType(MultipleItem.ANSWER_TYPE, R.layout.image_view);
//        addItemType(MultipleItem.FIND_TYPE, R.layout.image_view);
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, MultipleItem multipleItem) {

    }
}
