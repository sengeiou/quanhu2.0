package com.rz.circled.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.rz.circled.R;
import com.rz.circled.js.model.EditorCategoryTwoModel;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.activity.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class EditorTwoSortActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.lv_editor_two_sort)
    ListView lvSort;
    private ArrayList<EditorCategoryTwoModel> sortList;
    private CommonAdapter sortAdapter;

    private long currentId = -1;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_editor_two_sort, null, false);
    }


    @Override
    public void initView() {
        setTitleRightText(R.string.comple);
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.putExtra(IntentKey.EXTRA_ID, currentId);
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });
        setTitleRightTextColor(R.color.color_main);
        lvSort.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        sortList = (ArrayList<EditorCategoryTwoModel>) getIntent().getSerializableExtra(IntentKey.EXTRA_SERIALIZABLE);
        currentId = getIntent().getLongExtra(IntentKey.EXTRA_ID, -1);
        sortAdapter = new CommonAdapter(this, R.layout.item_editor_two_sort) {
            @Override
            public void convert(ViewHolder helper, Object item, int position) {
                EditorCategoryTwoModel categoryModel = (EditorCategoryTwoModel) item;
                CheckBox cbSort = (CheckBox) helper.getViewById(R.id.cb_editor_two_anonymity);
                cbSort.setText(categoryModel.getCategoryName());
                if (categoryModel.getId() == currentId)
                    cbSort.setChecked(true);
                else
                    cbSort.setChecked(false);
            }
        };
        lvSort.setAdapter(sortAdapter);
        sortAdapter.setData(sortList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentId = sortList.get(position).getId();
        sortAdapter.notifyDataSetChanged();
    }
}
