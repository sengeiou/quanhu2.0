package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.rz.circled.R;
import com.rz.circled.adapter.SearchCircleAdapter;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.bean.CircleEntrModle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AllCircleSearchActivity extends BaseActivity {

    @BindView(R.id.et_search_keyword)
    EditText etKeyword;
    @BindView(R.id.iv_search_clear_keyword)
    ImageView ivClearKeyword;

    @BindView(R.id.gv_search_circle)
    GridView gvCircle;
    private SearchCircleAdapter circleAdapter;
    private List<CircleEntrModle> circleBeanList = new ArrayList<>();

    public static String searchWord = "";
    private int type = 0;

    public static final void stratActivity(Context context, int type, List dataList) {
        Intent intent = new Intent(context, AllCircleSearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.ALL_CIRCLE_TYPE, (Serializable) dataList);
        bundle.putInt(IntentKey.EXTRA_TYPE, type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_seach_circle, null);
    }

    @Override
    public void initView() {

        Bundle bundle = getIntent().getExtras();
        circleBeanList = (List<CircleEntrModle>) bundle.getSerializable(IntentKey.ALL_CIRCLE_TYPE);
        type = bundle.getInt(IntentKey.EXTRA_TYPE,0);

        circleAdapter = new SearchCircleAdapter(this, R.layout.circle_adapter_item);
        circleAdapter.setData(circleBeanList);
        circleAdapter = new SearchCircleAdapter(this, R.layout.circle_adapter_item);
//        circleAdapter.setData(circleBeanList);
        gvCircle.setAdapter(circleAdapter);

        etKeyword.setHint("搜圈子");

        gvCircle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CircleEntrModle circleEntrModle = circleBeanList.get(position);
                if (getString(R.string.FIND_MORE).equals(circleEntrModle.appId)) {
                    Intent intent = new Intent(AllCircleSearchActivity.this, AllCirclesAty.class);
                    AllCircleSearchActivity.this.startActivity(intent);
                } else {
                    circleEntrModle.click += 1;
                    WebContainerActivity.startActivity(AllCircleSearchActivity.this, circleBeanList.get(position).circleUrl);
                }
            }
        });


        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !TextUtils.isEmpty(s.toString())) {
                    ivClearKeyword.setVisibility(View.VISIBLE);
                } else {
                    ivClearKeyword.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    protected View getTitleView() {
        return getLayoutInflater().inflate(R.layout.layout_search_title, null);
    }

    @OnClick({R.id.iv_search_title_back, R.id.iv_search_clear_keyword, R.id.tv_search_to_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search_title_back:
                finish();
                break;
            case R.id.iv_search_clear_keyword:
                etKeyword.setText("");
                break;
            case R.id.tv_search_to_search:
                toSearch();
                break;
        }
    }

    /**
     * 去搜索
     */
    private void toSearch() {
        searchWord = etKeyword.getText().toString();
        String keyWord = etKeyword.getText().toString().trim();
        if (TextUtils.isEmpty(keyWord)) {
            Toasty.info(mContext,mContext.getString(R.string.search_attention_title)).show();
            return;
        }
        //本地匹配关键词搜索

        List<CircleEntrModle> dataList = new ArrayList<>();

        for(int i=0;i<circleBeanList.size();i++){
            if(circleBeanList.get(i).getCircleName().contains(keyWord)){
                dataList.add(circleBeanList.get(i));
            }
        }
        circleAdapter.setData(dataList);
        circleAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshPage() {

    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean needSwipeBack() {
        return false;
    }

}
