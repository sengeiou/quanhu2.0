package com.rz.circled.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kyleduo.switchbutton.SwitchButton;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rz.circled.R;
import com.rz.circled.dialog.ApplyForGroupSuccessDialog;
import com.rz.circled.presenter.impl.PrivateGroupPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.H5Address;
import com.rz.common.event.BaseEvent;
import com.rz.common.oss.OssManager;
import com.rz.common.oss.UploadPicManager;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.Protect;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_BELONG_ID;
import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_CREATE_REFRESH;
import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_JOIN_WAY;
import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_TAB_REFRESH;
import static com.rz.circled.ui.activity.PictureSelectedActivity.PUBLISH_RESULT_CAMERA;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class ApplyForCreatePrivateGroupActivity extends BaseActivity implements UploadPicManager.OnUploadCallback {
    private static int COVER_ADD_REQUEST = 2;

    @BindView(R.id.tv_group)
    TextView tvGroup;
    @BindView(R.id.btn_group)
    LinearLayout btnGroup;
    @BindView(R.id.etv_desc)
    EditText etvDesc;
    @BindView(R.id.tv_desc_num)
    TextView tvDescNum;
    @BindView(R.id.btn_update_pic)
    LinearLayout btnUpdatePic;
    @BindView(R.id.img_group)
    RoundedImageView imgGroup;
    @BindView(R.id.etv_name)
    EditText etvName;
    @BindView(R.id.tv_name_num)
    TextView tvNameNum;
    @BindView(R.id.etv_group_desc)
    EditText etvGroupDesc;
    @BindView(R.id.tv_desc_group_num)
    TextView tvDescGroupNum;
    @BindView(R.id.tv_way)
    TextView tvWay;
    @BindView(R.id.cbx)
    SwitchButton cbx;
    @BindView(R.id.line_cbx)
    LinearLayout lineCbx;
    @BindView(R.id.cbx_protocol)
    CheckBox cbxProtocol;
    @BindView(R.id.btn_protocol)
    TextView btnProtocol;

    //私圈相关
    private PrivateGroupPresenter mPresenter;
    private String coverPath;
    private String circleId;
    private int price;
    private UploadPicManager upManager;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_apply_for_private_group, null, false);
    }

    @Override
    public void initView() {
        setTitleText(R.string.private_group_apply_for);
        setTitleRightText(R.string.submit);
        setTitleRightTextColor(R.color.font_gray_s);
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(circleId)) {
                    Toast.makeText(mContext, "请选择圈子", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etvDesc.getText().toString().trim())) {
                    Toast.makeText(mContext, "请填写身份简介", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(coverPath)) {
                    Toast.makeText(mContext, "请上传私圈封面图", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etvName.getText().toString().trim())) {
                    Toast.makeText(mContext, "请填写私圈名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etvGroupDesc.getText().toString().trim())) {
                    Toast.makeText(mContext, "请填写私圈简介", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!cbxProtocol.isChecked()) {
                    Toast.makeText(mContext, "请阅读并同意私圈创建协议", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateCoverPic();
            }
        });
        etvName.addTextChangedListener(new MyTextWatcher(tvNameNum));
        etvDesc.addTextChangedListener(new MyTextWatcher(tvDescNum));
        etvGroupDesc.addTextChangedListener(new MyTextWatcher(tvDescGroupNum));
        cbxProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                validateSubmit();
            }
        });
    }

    @Override
    public void initData() {
        upManager = new UploadPicManager(this);
    }

    @Override
    public void initPresenter() {
        mPresenter = new PrivateGroupPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (t instanceof Integer) {
            Integer data = (Integer) t;
            if (data == CommonCode.General.DATA_SUCCESS) {
                EventBus.getDefault().post(new BaseEvent(PRIVATE_GROUP_CREATE_REFRESH));
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ApplyForGroupSuccessDialog.newInstance().show(ft, "");
            }
        }
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        super.onLoadingStatus(loadingStatus, string);
        getTitleView().findViewById(R.id.tv_base_title_right).setEnabled(true);
    }

    @OnClick({R.id.btn_update_pic, R.id.btn_group, R.id.btn_protocol, R.id.btn_way, R.id.img_group})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_group:
            case R.id.btn_update_pic:
                PictureSelectedActivity.startActivityForResult(this, COVER_ADD_REQUEST, 1, false);
                break;
            case R.id.btn_group:
                ApplyForPrivateGroupBelongActivity.startPrivateGroupBelong(mContext, TextUtils.isEmpty(circleId) ? "" : circleId);
                break;
            case R.id.btn_way:
                PrivateGroupJoinWayChangeActivity.startJoinWay(mContext, price);
                break;
            case R.id.btn_protocol:
                CommonH5Activity.startCommonH5(mContext, "", H5Address.PRIVATE_GROUP_CREATE_AGREEMENT);
                break;
        }
    }

    @Subscribe
    public void eventBus(BaseEvent event) {
        switch (event.getType()) {
            case PRIVATE_GROUP_JOIN_WAY:
                if ((int) event.getData() == 0) {
                    price = 0;
                    tvWay.setText(R.string.private_group_free);
                    lineCbx.setVisibility(View.VISIBLE);
                } else {
                    price = (int) event.getData();
                    tvWay.setText(String.format(mContext.getString(R.string.private_group_youran_price), price));
                    lineCbx.setVisibility(View.GONE);
                }
                break;
            case PRIVATE_GROUP_BELONG_ID:
                String[] array = ((String) event.getData()).split("\\|");
                if (array.length == 2) {
                    circleId = array[0];
                    tvGroup.setText(array[1]);
                    validateSubmit();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COVER_ADD_REQUEST) {
            if (null != data) {
                if (resultCode == CommonCode.REQUEST.PUBLISH_RESULT) {//相册
                    ArrayList<String> picList = data.getExtras().getStringArrayList("picList");
                    if (null != picList && !picList.isEmpty())
                        coverPath = picList.get(0);
                } else if (resultCode == CommonCode.REQUEST.PUBLISH_RESULT_CAMERA) //相机
                    coverPath = data.getStringExtra("picture");
                if (!TextUtils.isEmpty(coverPath)) {
                    if (Protect.checkLoadImageStatus(mContext))
                        Glide.with(mContext).load(coverPath).into(imgGroup);
                    imgGroup.setVisibility(View.VISIBLE);
                    btnUpdatePic.setVisibility(View.GONE);
                    validateSubmit();
                }
            }
        }
    }

    private void createPrivateGroupSubmit(String url, String groupName, String groupDesc, String ownDesc) {
        mPresenter.privateGroupCreate(circleId, url, groupDesc, price == 0 ? (cbx.isChecked() ? 1 : 0) : 0, price * 100, groupName, Session.getUserId(), ownDesc, Session.getUserName());
    }

    private void updateCoverPic() {
        onLoadingStatus(CommonCode.General.DATA_LOADING, getString(R.string.private_group_create_submit));
        getTitleView().findViewById(R.id.tv_base_title_right).setEnabled(false);
        /**
         * oss上传图片，得到回调之后，保存图片地址到本地
         */
        UploadPicManager.UploadInfo info = new UploadPicManager.UploadInfo();
        info.fileSavePath = coverPath;
        List<UploadPicManager.UploadInfo> dataList = new ArrayList<>();
        dataList.add(info);
        upManager.compressAndUploads(this, dataList, OssManager.objectNameProfile);
    }

    @Override
    public void onResult(boolean result, List<UploadPicManager.UploadInfo> resultList) {
        if (result) {
            String groupName = etvName.getText().toString().trim();
            String groupDesc = etvGroupDesc.getText().toString().trim();
            String ownDesc = etvDesc.getText().toString().trim();
            createPrivateGroupSubmit(resultList.get(0).fileSavePath, groupName, groupDesc, ownDesc);
        } else {
            getTitleView().findViewById(R.id.tv_base_title_right).setEnabled(true);
            onLoadingStatus(CommonCode.General.ERROR_DATA);
        }
    }

    @Override
    public void refreshPage() {

    }

    private void validateSubmit() {
        if (TextUtils.isEmpty(circleId)
                || TextUtils.isEmpty(etvDesc.getText().toString().trim())
                || TextUtils.isEmpty(coverPath)
                || TextUtils.isEmpty(etvName.getText().toString().trim())
                || TextUtils.isEmpty(etvGroupDesc.getText().toString().trim())
                || !cbxProtocol.isChecked()) {
            setTitleRightTextColor(R.color.font_gray_s);
        } else {
            setTitleRightTextColor(R.color.font_color_blue);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private TextView tv;

        public MyTextWatcher(TextView tv) {
            this.tv = tv;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            tv.setText(String.valueOf(s.length()));
            validateSubmit();
        }
    }
}
