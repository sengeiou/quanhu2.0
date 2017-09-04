package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.rz.circled.R;
import com.rz.common.ui.activity.BaseActivity;
import com.yryz.yunxinim.uikit.common.util.string.StringTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class ApplyForCreatePrivateGroupActivity extends BaseActivity {
    @BindView(R.id.etv_desc)
    EditText etvDesc;
    @BindView(R.id.tv_desc_num)
    TextView tvDescNum;
    @BindView(R.id.btn_update_pic)
    LinearLayout btnUpdatePic;
    @BindView(R.id.img_group)
    ImageView imgGroup;
    @BindView(R.id.etv_name)
    EditText etvName;
    @BindView(R.id.etv_group_desc)
    EditText etvGroupDesc;
    @BindView(R.id.tv_desc_group_num)
    TextView tvDescGroupNum;
    @BindView(R.id.tv_group)
    EditText tvGroup;
    @BindView(R.id.btn_group)
    LinearLayout btnGroup;
    @BindView(R.id.cbx_protocol)
    CheckBox cbxProtocol;
    @BindView(R.id.btn_protocol)
    TextView btnProtocol;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_apply_for_private_group, null, false);
    }

    @Override
    public void initView() {
        etvDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvDescNum.setText(String.valueOf(s.length()));
            }
        });
        etvGroupDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvDescGroupNum.setText(String.valueOf(s.length()));
            }
        });
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_update_pic, R.id.btn_group, R.id.btn_protocol})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_pic:

                break;
            case R.id.btn_group:

                break;
            case R.id.btn_protocol:

                break;
        }
    }
}
