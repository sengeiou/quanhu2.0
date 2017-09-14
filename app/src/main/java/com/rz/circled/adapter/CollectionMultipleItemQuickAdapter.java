package com.rz.circled.adapter;

import com.chad.library.adapter.base.*;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rz.circled.R;
import com.rz.circled.modle.MultipleItem;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class CollectionMultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem> {

    public CollectionMultipleItemQuickAdapter(List data) {
        super(data);
        addItemType(MultipleItem.HOME_TYPE, R.layout.item_dynamic_home);
        addItemType(MultipleItem.ANSWER_TYPE, R.layout.activity_item);
        addItemType(MultipleItem.FIND_TYPE, R.layout.item_answer);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MultipleItem multipleItem) {

        switch (baseViewHolder.getItemViewType()) {
            case MultipleItem.HOME_TYPE:
                break;
            case MultipleItem.ANSWER_TYPE:
                break;

            case MultipleItem.FIND_TYPE:
                break;
        }


    }
}
