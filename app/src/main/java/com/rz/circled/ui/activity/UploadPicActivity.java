package com.rz.circled.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.rz.circled.R;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.oss.OssManager;
import com.rz.common.oss.UploadPicManager;
import com.rz.common.permission.AfterPermissionGranted;
import com.rz.common.permission.AppSettingsDialog;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CacheUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.toasty.Toasty;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class UploadPicActivity extends BaseActivity {

    public static final String EXTRA_INDEX = "extraIndex";
    public static final String EXTRA_IS_NEED = "extraIsNeed";
    public static final String EXTRA_IS_SINGLE = "extraIsSingle";

    private String mPhotoFileName;
    public int maxPicNum;
    private boolean isCrop;
    private String ossDir;

    List<String> localPaths = new ArrayList<>();
    List<String> netPaths = new ArrayList<>();
    private UploadPicManager uploadPicManager;

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean hasDataInPage() {
        return true;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_upload_pic, null);
    }

    @Override
    public void initView() {
        isCrop = getIntent().getBooleanExtra(IntentKey.EXTRA_BOOLEAN, false);
        ossDir = getIntent().getStringExtra(IntentKey.EXTRA_URL);
        maxPicNum = getIntent().getIntExtra(IntentKey.EXTRA_NUM, 30);
        setTitleText(getString(R.string.upload_pic));
    }

    @Override
    public void initData() {
        uploadPicManager = new UploadPicManager(onUploadCallback);
    }

    @OnClick({R.id.tv_upload_pic_local, R.id.tv_upload_pic_shoot})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_upload_pic_local) {//本地选择图片
            PictureSelectedActivity.startActivityForResult(UploadPicActivity.this, CommonCode.REQUEST.PUBLISH_REQUEST, maxPicNum, false);
        } else if (i == R.id.tv_upload_pic_shoot) {//拍摄图片
            doCamera();
        }
    }

    public void callResult(boolean result) {
        JsEvent.callJsEvent(netPaths, result ? BaseParamsObject.RESULT_CODE_SUCRESS : BaseParamsObject.RESULT_CODE_FAILED);
        finish();
    }

    private UploadPicManager.OnUploadCallback onUploadCallback = new UploadPicManager.OnUploadCallback() {
        @Override
        public void onResult(boolean result, List<UploadPicManager.UploadInfo> resultList) {
            Log.d(TAG, "this is pic upload result result is " + result);
//            materialDialogUtils.dismissProgress();
            if (result) {
                Log.d(TAG, "this is pic upload result resultList is " + resultList.toString());
                netPaths.clear();
                for (int i = 0; i < resultList.size(); i++) {
                    UploadPicManager.UploadInfo uploadInfo = resultList.get(i);
                    netPaths.add(uploadInfo.fileSavePath);
                }
            } else {
//                SVProgressHUD.showErrorWithStatus(UploadPicAty.this, "上传图片失败");
                Toasty.error(mContext, getString(R.string.upload_pic_fail), Toast.LENGTH_SHORT, true).show();
            }
            onLoadingStatus(CommonCode.General.DATA_SUCCESS);
            callResult(result);
        }
    };

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void doCamera() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            String SDState = Environment.getExternalStorageState();
            if (SDState.equals(Environment.MEDIA_MOUNTED)) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 创建拍摄照片保存的文件夹及图片文件名
                String imgName = StringUtils.getPhotoFileName();
                File mPhotoFile = new File(CacheUtils.getCacheDirectory(mContext, true, "pic") + imgName);
                if (mPhotoFile != null) mPhotoFileName = mPhotoFile.getAbsolutePath();
                if (mPhotoFile.exists()) mPhotoFile.delete();
                try {
                    mPhotoFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 下面这句指定调用相机拍照后的照片存储的路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(intent, CommonCode.REQUEST.TAKE_PICTURE);// 使用手机进行拍照的请求码是：1
            } else {
                Toasty.info(mContext, getString(R.string.upload_pic_fail), Toast.LENGTH_SHORT, true).show();
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.camera_sd_permissions_run), RC_CAMERA_PERM, perms);
        }
    }

    private void requestOss() {
        netPaths.clear();
        List<UploadPicManager.UploadInfo> uploadInfos = new ArrayList<>();
        for (int i = 0; i < localPaths.size(); i++) {
            UploadPicManager.UploadInfo uploadInfo = new UploadPicManager.UploadInfo();
            uploadInfo.tag = i;
            uploadInfo.fileSavePath = localPaths.get(i);
            uploadInfos.add(uploadInfo);
        }
//        SVProgressHUD.showWithStatus(aty, getString(R.string.is_loading));
//        materialDialogUtils.showProgressDialog(R.string.is_upload_pic);
        onLoadingStatus(CommonCode.General.DATA_LOADING);
        if (uploadInfos.size() > 0) {
            //oss上传图片
            uploadPicManager.compressAndUploads(this, uploadInfos, TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir);
        }
    }

    private void doPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 640);
        intent.putExtra("outputY", 640);
//        intent.putExtra("return-data", true);
        intent.putExtra("return-data", false);
        initHeadPicPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPhotoFileName)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, CommonCode.REQUEST.CHOOSE_TRUE);
    }

    private void initHeadPicPath() {
        String imgName = StringUtils.getPhotoFileName();
        File f = new File(CacheUtils.getCacheDirectory(mContext, true, "pic") + imgName);
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (f != null) {
            mPhotoFileName = f.getAbsolutePath();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
//            Toast.makeText(getApplication(), R.string.cancel, Toast.LENGTH_LONG).show();
            return;
        }

        if (requestCode == CommonCode.REQUEST.PUBLISH_REQUEST) {
            if (null == data) return;

            if (resultCode == CommonCode.REQUEST.PUBLISH_RESULT_CAMERA) {
                String path = data.getStringExtra("picture");
                localPaths.add(path);
                if (isCrop) doPhoto(Uri.fromFile(new File(path)));
                else requestOss();
            } else {
                ArrayList<String> mList = data.getExtras().getStringArrayList("picList");
                localPaths.clear();
                if (null != mList && !mList.isEmpty()) {
                    for (String filePath : mList) {
                        localPaths.add(filePath);
                    }
                    if (isCrop) doPhoto(Uri.fromFile(new File(localPaths.get(0))));
                    else requestOss();
                }
            }
        }
        if (requestCode == CommonCode.REQUEST.TAKE_PICTURE) {
            if (isCrop) {
                File mPhotoFile = null;
                if (!TextUtils.isEmpty(mPhotoFileName)) mPhotoFile = new File(mPhotoFileName);
                if (mPhotoFile != null) doPhoto(Uri.fromFile(mPhotoFile));
            } else {
                localPaths.clear();
                localPaths.add(mPhotoFileName);
                requestOss();
            }
        }
        if (requestCode == CommonCode.REQUEST.CHOOSE_TRUE) {
            localPaths.clear();
            localPaths.add(mPhotoFileName);
            requestOss();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, getString(R.string.camera_sd_permissions_run))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
                    .setRequestCode(RC_CAMERA_PERM)
                    .build()
                    .show();
        }
    }

    @Override
    public void refreshPage() {

    }
}
