package com.rz.circled.ui.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.js.model.EditorAuthorityPriceBean;
import com.rz.circled.js.model.EditorAuthorityRootBean;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.activity.BaseActivity;

import java.util.HashMap;

import butterknife.BindView;

public class EditorTwoAuthorityActivity extends BaseActivity {

    @BindView(R.id.ll_authority_root)
    LinearLayout llRoot;
    private EditorAuthorityRootBean authorityRootBean;

    private final int typeShare = 1;
    private final int typeRead = 2;
    private final int typePopularize = 3;
    private GridView myGridView;
    private HashMap<Integer, CheckBox> boxMaps = new HashMap<>();

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_editor_two_authority, null);
    }

    @Override
    public void initView() {
        setTitleText(R.string.authority_setting);
        setTitleLeftText(R.string.cancel);
        setTitleRightText(R.string.complete);
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boxMaps.containsKey(typeShare)) {
                    CheckBox checkBox = boxMaps.get(typeShare);
                    authorityRootBean.setAllowShareFlag(checkBox.isChecked() ? 1 : 0);
                }
                if (boxMaps.containsKey(typePopularize)) {
                    CheckBox checkBox = boxMaps.get(typePopularize);
                    authorityRootBean.setAllowGeneralizeFlag(checkBox.isChecked() ? 1 : 0);
                }
                Intent intent = new Intent();
                intent.putExtra(IntentKey.EXTRA_SERIALIZABLE, authorityRootBean);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }


    @Override
    public void initData() {
        authorityRootBean = (EditorAuthorityRootBean) getIntent().getSerializableExtra(IntentKey.EXTRA_SERIALIZABLE);
        if (authorityRootBean == null) finish();
        else {
            if (authorityRootBean.getAllowShareFlag() != -1) {//允许分享标识（0；不允许；1：允许）
                addItemView(typeShare);
            }
            if (authorityRootBean.getContentPriceData() != null && authorityRootBean.getContentPriceData().getData() != null && authorityRootBean.getContentPriceData().getData().size() > 0) {//付费可读
                addItemView(typeRead);
            }
            if (authorityRootBean.getAllowGeneralizeFlag() != -1) {//允许推广标识（0；不允许；1：允许）
                addItemView(typePopularize);
            }
        }

    }


    private void addItemView(int type) {
        ViewGroup rootView = (ViewGroup) getLayoutInflater().inflate(R.layout.item_authority, null);
        TextView tvName = (TextView) rootView.findViewById(R.id.tv_authority);
        CheckBox checkBox = (CheckBox) rootView.findViewById(R.id.cb_authority);
        switch (type) {
            case typeShare:
                tvName.setText(R.string.authority_share);
                boxMaps.put(typeShare, checkBox);
                break;
            case typeRead:
                tvName.setText(R.string.authority_read);
                initPriceView(rootView, authorityRootBean.getContentPriceData());
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            myGridView.setVisibility(View.VISIBLE);
                            if (authorityRootBean.getContentPriceData().getData().contains(authorityRootBean.getContentPrice())) {
                                authorityRootBean.setContentPrice(authorityRootBean.getContentPriceData().getData().get(0).intValue());
                            }
                        } else myGridView.setVisibility(View.GONE);
                    }
                });
                if (authorityRootBean.getContentPriceData().getData().contains(authorityRootBean.getContentPrice()))
                    checkBox.setChecked(true);
                boxMaps.put(typeRead, checkBox);
                break;
            case typePopularize:
                tvName.setText(R.string.authority_popularize);
                boxMaps.put(typePopularize, checkBox);
                break;
        }
        llRoot.addView(rootView);
    }


    private void initPriceView(ViewGroup rootView, final EditorAuthorityPriceBean priceRootBean) {
        myGridView = (GridView) rootView.findViewById(R.id.gv_authority);
        myGridView.setVisibility(View.VISIBLE);

        CommonAdapter priceAdapter = new CommonAdapter(mContext, R.layout.layout_item_authority_tv) {

            @Override
            public void convert(ViewHolder helper, Object item, final int position) {
                int price = (int) item;
                TextView tvPrice = helper.getView(R.id.tv_authority_price_item);
                tvPrice.setText(price + priceRootBean.getUnit());

                if (authorityRootBean.getContentPrice() != price)
                    tvPrice.setSelected(false);
                else tvPrice.setSelected(true);

                tvPrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!v.isSelected()) {
                            authorityRootBean.setContentPrice(authorityRootBean.getContentPriceData().getData().get(position).intValue());
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        };
        myGridView.setAdapter(priceAdapter);
        if (priceRootBean != null) {
            priceAdapter.setData(priceRootBean.getData());
            if (!priceRootBean.getData().contains(authorityRootBean.getContentPrice())) {
                myGridView.setVisibility(View.GONE);
            }
        }
    }


}
