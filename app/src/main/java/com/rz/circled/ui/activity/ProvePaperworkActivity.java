package com.rz.circled.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.rz.circled.R;
import com.rz.circled.presenter.impl.ProveInfoPresenter;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.oss.OssManager;
import com.rz.common.oss.UploadPicManager;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.DensityUtils;
import com.rz.common.utils.NetUtils;
import com.rz.common.utils.Protect;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.bean.ProveStatusBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ProvePaperworkActivity extends BaseActivity {

    @BindView(R.id.tv_prove_paperwork_choose_pic)
    TextView tvChoosePic;
    @BindView(R.id.iv_prove_paperwork)
    ImageView ivProvePaperwork;
    private ProveStatusBean proveInfo;

    private final int REQUEST_PIC = 10;
    private ProveInfoPresenter proveInfoPresenter;
    private boolean isChange = false;
    private String imgPath;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_prove_paperwork, null);
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean hasDataInPage() {
        return true;
    }

    @Override
    public void initView() {
        proveInfo = (ProveStatusBean) getIntent().getSerializableExtra(IntentKey.EXTRA_SERIALIZABLE);
        isChange = getIntent().getBooleanExtra(IntentKey.EXTRA_BOOLEAN, false);
        setTitleRightText(R.string.submit);
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(imgPath)) {
                    Toasty.info(mContext, getString(R.string.choose_prove_img_hint)).show();
                    return;
                }
                requestOss();
            }
        });
        setTitleRightTextColor(R.color.font_color_blue);
        setTitleText(getString(R.string.upload_paperwork));
        if (!TextUtils.isEmpty(proveInfo.getOrganizationPaper()) && proveInfo.getOrganizationPaper().startsWith("http")) {
            processImageView(proveInfo.getOrganizationPaper());
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        proveInfoPresenter = new ProveInfoPresenter();
        proveInfoPresenter.attachView(this);
    }


    private void requestOss() {
        if (!NetUtils.isNetworkConnected(mContext)) {
            onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        onLoadingStatus(CommonCode.General.DATA_LOADING);
        if (imgPath.startsWith("http")) {
            toSubmit();
            return;
        }
        List<UploadPicManager.UploadInfo> uploadInfoList = new ArrayList<>();
        UploadPicManager.UploadInfo uploadInfo = new UploadPicManager.UploadInfo();
        uploadInfo.fileSavePath = imgPath;
        uploadInfoList.add(uploadInfo);
        UploadPicManager uploadPicManager = new UploadPicManager(new UploadPicManager.OnUploadCallback() {
            @Override
            public void onResult(boolean result, List<UploadPicManager.UploadInfo> resultList) {
                if (result && resultList != null && resultList.size() > 0) {
                    proveInfo.setOrganizationPaper(resultList.get(0).fileSavePath);
                    toSubmit();
                } else {
                    onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                    SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.prove_info_fail));
                }
            }
        });
        uploadPicManager.compressAndUploads(this, uploadInfoList, OssManager.objectNameCircle);
    }

    private void toSubmit() {
        if (isChange) proveInfoPresenter.changeProveInfo(false, proveInfo);
        else proveInfoPresenter.submitProveInfo(false, proveInfo);
    }


    private void processImageView(String imgPath) {
        if (Protect.checkLoadImageStatus(this)) {
            ivProvePaperwork.setVisibility(View.VISIBLE);
            tvChoosePic.setVisibility(View.GONE);
            Glide.with(this).load(imgPath).into(new ViewTarget<ImageView, GlideDrawable>(ivProvePaperwork) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    ivProvePaperwork.getLayoutParams().height = resource.getIntrinsicHeight() * (DensityUtils.getScreenW(mContext)) / resource.getIntrinsicWidth();
                    ivProvePaperwork.requestLayout();
                    ivProvePaperwork.setImageDrawable(resource.getCurrent());
                }
            });
            this.imgPath = imgPath;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_PIC || data == null) return;
        if (resultCode == CommonCode.REQUEST.PUBLISH_RESULT_CAMERA) {
            String picture = data.getStringExtra("picture");
            processImageView(picture);
        } else if (resultCode == CommonCode.REQUEST.PUBLISH_RESULT) {
            ArrayList<String> picList = data.getStringArrayListExtra("picList");
            if (picList != null && picList.size() > 0) {
                processImageView(picList.get(0));
            }

        }
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == proveInfoPresenter.FLAG_PROVE_INFO_SUCCESS) {
            setResult(Activity.RESULT_OK, null);
            finish();
        }

    }

    @OnClick({R.id.tv_prove_paperwork_choose_pic, R.id.iv_prove_paperwork})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_prove_paperwork_choose_pic:
            case R.id.iv_prove_paperwork:
                //调用相机相机或相册咯
                PictureSelectedActivity.startActivityForResult(this, REQUEST_PIC, 1, false);
                break;
        }
    }

    @Override
    public void refreshPage() {

    }
}
