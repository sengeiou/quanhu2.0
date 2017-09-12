package com.rz.circled.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kyleduo.switchbutton.SwitchButton;
import com.rz.circled.R;
import com.rz.circled.dialog.ApplyForGroupSuccessDialog;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.oss.OssManager;
import com.rz.common.oss.UploadPicManager;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.Protect;
import com.rz.httpapi.api.ApiPGService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_BELONG_ID;
import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_JOIN_WAY;
import static com.rz.circled.event.EventConstant.USER_CREATE_PRIVATE_GROUP_NUM;
import static com.rz.circled.event.EventConstant.USER_JOIN_PRIVATE_GROUP_NUM;
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
    ImageView imgGroup;
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
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        setTitleRightText(R.string.submit);
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(coverPath)) {
                    Toast.makeText(mContext, "封面图为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(circleId)) {
                    Toast.makeText(mContext, "请选择圈子", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!cbxProtocol.isChecked()) {
                    Toast.makeText(mContext, "请同意创建协议", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateCoverPic();
            }
        });
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
        upManager = new UploadPicManager(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.btn_update_pic, R.id.btn_group, R.id.btn_protocol, R.id.btn_way})
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
                startActivity(new Intent(mContext, PrivateGroupJoinWayChangeActivity.class));
                break;
            case R.id.btn_protocol:

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
                } else if (resultCode == PUBLISH_RESULT_CAMERA) //相机
                    coverPath = data.getStringExtra("picture");
                if (!TextUtils.isEmpty(coverPath)) {
                    if (Protect.checkLoadImageStatus(mContext))
                        Glide.with(mContext).load(coverPath).into(imgGroup);
                    imgGroup.setVisibility(View.VISIBLE);
                    btnUpdatePic.setVisibility(View.GONE);
                }
            }
        }
    }

    private void createPrivateGroupSubmit(String url, String groupName, String groupDesc, String ownDesc) {
        Http.getApiService(ApiPGService.class).privateGroupCreate(
                circleId,
                url,
                groupDesc,
                price == 0 ? (cbx.isChecked() ? 1 : 0) : 0,
                price,
                groupName,
                Session.getUserId(),
                ownDesc,
                Session.getUserName()).enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body().isSuccessful()) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ApplyForGroupSuccessDialog.newInstance().show(ft, "");
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void updateCoverPic() {
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
        String groupName = etvName.getText().toString().trim();
        String groupDesc = etvGroupDesc.getText().toString().trim();
        String ownDesc = etvDesc.getText().toString().trim();
        createPrivateGroupSubmit(resultList.get(0).fileSavePath, groupName, groupDesc, ownDesc);
    }

}
