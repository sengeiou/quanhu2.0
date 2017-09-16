package com.rz.circled.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.httpapi.bean.ProveInfoBean;

import butterknife.BindView;
import butterknife.OnClick;

public class ProvePaperworkActivity extends BaseActivity {

    @BindView(R.id.tv_prove_paperwork_choose_pic)
    TextView tvChoosePic;
    @BindView(R.id.iv_prove_paperwork)
    ImageView ivProvePaperwork;
    private ProveInfoBean proveInfo;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_prove_paperwork, null);
    }

    @Override
    public void initView() {
        proveInfo = (ProveInfoBean) getIntent().getSerializableExtra(IntentKey.EXTRA_SERIALIZABLE);
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.tv_prove_paperwork_choose_pic)
    public void onClick() {
        //调用相机相机或相册咯
    }
}
