package com.rz.circled.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import com.rz.common.ui.view.CommonDialog;
import com.rz.common.utils.FileUtils;
import com.rz.common.utils.ImageUtils;
import com.rz.common.utils.NetWorkSpeedUtils;
import com.rz.common.widget.toasty.Toasty;
import com.rz.sgt.jsbridge.JsEvent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.OnClick;

public class UploadVideoActivity extends BaseActivity {

    public final int SYSTEM_REQUEST_VIDEO = 1220;
    public final int SYSTEM_SHOOT_VIDEO = 1221;


    private String mVideoFilePath;
    private long videoDuration;
    private double videoSize;
    private int maxSize;


    private String fileName;
    private String uploadedUrl;
    private String currentUploadId;
    private String filePath;
    private OssManager mOssManager;
    private String ossDir;
    private String mVideoImage;
    private Bitmap mBitmap;
    private UploadPicManager uploadPicManager;
//    private MaterialDialog progressDialog;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_upload_video, null);
    }

    @Override
    public void initView() {
        setTitleText(R.string.upload_video);
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
    public void initData() {
        mOssManager = new OssManager();
        ossDir = getIntent().getStringExtra(IntentKey.EXTRA_URL);
    }

    @OnClick({R.id.tv_upload_video_local, R.id.tv_upload_video_shoot})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_upload_video_local) {//本地选择
            videoPick();
        } else if (i == R.id.tv_upload_video_shoot) {//拍摄视频
            takeVideo();
        }
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    private void videoPick() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(mContext, perms)) {
            Intent audio = new Intent(mContext, VideoChooseActivity.class);
            startActivityForResult(audio, SYSTEM_REQUEST_VIDEO);
        } else {
            EasyPermissions.requestPermissions(mContext, "圈乎要使用摄像头,读取手机状态,使用sd卡权限", RC_CAMERA_PERM, perms);
        }
    }

    @AfterPermissionGranted(RC_EXTENER)
    private void takeVideo() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(mContext, perms)) {
            try {
                Intent intent = new Intent();
                intent.setAction("android.media.action.VIDEO_CAPTURE");
                intent.addCategory("android.intent.category.DEFAULT");
                File file = createMediaFile();
                filePath = file.getAbsolutePath();
                if (file.exists()) {
                    file.delete();
                }
                Uri uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, SYSTEM_SHOOT_VIDEO);
            } catch (IOException e) {
//                Toasty.error(mContext, getString(R.string.get_audio_fail_two));
                e.printStackTrace();
            }
        } else {
            EasyPermissions.requestPermissions(mContext, "圈乎要使用摄像头,读取手机状态,使用sd卡权限", RC_EXTENER, perms);
        }
    }

    private File createMediaFile() throws IOException {
        if (FileUtils.checkSDCardAvaliable()) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES), "CameraDemo");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create directory");
                    return null;
                }
            }
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "VID_" + timeStamp;
            String suffix = ".mp4";
            File mediaFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
            return mediaFile;
        }
        return null;
    }

    /**
     * 检查视频文件
     *
     * @param videoPath
     * @return
     */
    private boolean checkVideoFile(String videoPath) {
        if (TextUtils.isEmpty(videoPath)) {
//            Toasty.error(mContext, getString(R.string.get_video_fail));
            return false;
        }

        File file = new File(videoPath);
        if (file.exists()) {
            if (maxSize < file.length()) {
//                Toasty.error(mContext, getString(R.string.file_too_big));
                return false;
            }
        } else {
//            Toasty.error(mContext, getString(R.string.get_video_fail));
            return false;
        }
        return true;
    }

    /**
     * 获得视频的大小和时长
     */
    private void initVideoDuration() {
        try {
            videoDuration = Long.parseLong(codeMediaInfo(mVideoFilePath));
            videoSize = new Double(FileUtils.getFileSize(mVideoFilePath, FileUtils.SIZETYPE_KB)).longValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String codeMediaInfo(String url) throws Exception {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        Log.d(TAG, "str:" + url);
        mmr.setDataSource(url);
        String mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
        Log.d(TAG, "mime:" + mime);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
        Log.d(TAG, "duration:" + duration);
        mmr.release();
        return duration;
    }

    /**
     * 处理oss
     */
    public void processOss() {
        uploadedUrl = "";
        currentUploadId = "";
        if (!TextUtils.isEmpty(mVideoFilePath)) {
            String[] result = mVideoFilePath.split("/");
            fileName = result[result.length - 1];
            String[] s = fileName.split("\\.");
            if (s != null && s.length > 0) {
                fileName = UUID.randomUUID().toString() + "." + s[s.length - 1];
            }
            uploadVideoFile();
        }
    }

    private void uploadVideoFile() {
        Log.d(TAG, "uploadVideoFile uoloadId is " + currentUploadId);
        onLoadingStatus(CommonCode.General.DATA_LOADING);
        mOssManager.asyncMultipartUpload(mVideoFilePath, fileName, OssManager.VIDEO, new OssManager.OssCallBack() {
            @Override
            public void onSuccess(String url, String uploadId) {
                Log.d("multipartUpload", "OssCallBack onSuccess url " + url);
                Log.d("multipartUpload", "uploadId " + uploadId);
                currentUploadId = "";
                uploadedUrl = url;
//                materialDialogUtils.dismissProgress();
                uploadVideoPic();
            }

            @Override
            public void onFailure(String uploadId) {
//                materialDialogUtils.dismissProgress();
                if (!TextUtils.isEmpty(uploadId)) {
                    currentUploadId = uploadId;
                }
//                showRetryPublishVideoDialog();
                Log.d(TAG, "OssCallBack onFailure  ");
                Log.d(TAG, "uploadId " + uploadId);
                onLoadingStatus(CommonCode.General.ERROR_DATA);
//                Toast.makeText(App.getContext(), getString(R.string.upload_video_fail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(String uploadId, float progress) {
                Log.d(TAG, "OssCallBack onProgress  " + progress + "");
                if (!TextUtils.isEmpty(uploadId)) {
                    currentUploadId = uploadId;
                }
//                if (progressDialog != null) {
//                    progressDialog.setProgress((int) progress);
//                }
            }
        }, currentUploadId, TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir);

//        progressDialog = materialDialogUtils.showDeterminateProgressDialog(R.string.is_upload_video);
//        progressDialog.setCancelable(false);
    }

    private void uploadVideoPic() {
        processVideoPic();
        if (!TextUtils.isEmpty(mVideoImage)) {
            if (!mVideoImage.contains("http")) {
//                materialDialogUtils.showProgressDialog(getString(R.string.is_processing)).setCancelable(false);
                List<UploadPicManager.UploadInfo> uploadInfos = new ArrayList<>();
                UploadPicManager.UploadInfo uploadInfo = new UploadPicManager.UploadInfo();
                uploadInfo.fileSavePath = mVideoImage;
                uploadInfos.add(uploadInfo);
                //                            callResult(true);
                uploadPicManager = new UploadPicManager(new UploadPicManager.OnUploadCallback() {
                    @Override
                    public void onResult(boolean result, List<UploadPicManager.UploadInfo> resultList) {
//                        materialDialogUtils.dismissProgress();
                        Log.d(TAG, "this is video pic upload result result is " + result);
                        if (result) {
                            onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                            mVideoImage = resultList.get(0).fileSavePath;
                            callResult(true);
                        } else {
                            onLoadingStatus(CommonCode.General.ERROR_DATA);
                            Toasty.error(mContext, getString(R.string.upload_video_fail), Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
                uploadPicManager.compressAndUploads(this, uploadInfos, OssManager.objectNameCircle);
            } else {

            }
        } else {
            //图片异常直接传空的图片
            onLoadingStatus(CommonCode.General.DATA_SUCCESS);
            mVideoImage = "";
            callResult(true);
        }
    }

    /**
     * 处理视频图片
     */
    private void processVideoPic() {
        if (!TextUtils.isEmpty(mVideoFilePath)) {
            if (TextUtils.isEmpty(mVideoImage)) {
                mBitmap = ImageUtils.getVideoThumbnail(mVideoFilePath);
            }
        }
        if (TextUtils.isEmpty(mVideoImage)) {
            if (mBitmap != null) {
                try {
                    File file = ImageUtils.saveFile(mBitmap, UUID.randomUUID().toString(), this);
                    mVideoImage = file.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void callResult(boolean result) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("url", uploadedUrl);
        map.put("thumbnailImage", mVideoImage);
        map.put("videoTime", videoDuration);
        map.put("size", videoSize);
        JsEvent.callJsEvent(map, result);
        finish();
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
            new AppSettingsDialog.Builder(this, "圈乎要使用摄像头,读取手机状态,使用sd卡权限，否则app可能无法正常运行")
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
                    .setRequestCode(requestCode)
                    .build()
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        String filePath = null;
        if (requestCode == SYSTEM_REQUEST_VIDEO && null != data) {
            try {
                filePath = data.getStringExtra(IntentKey.EXTRA_PATH);
                checkVideoFile(filePath);
            } catch (Exception e) {
                e.printStackTrace();
//                Toasty.error(mContext, getString(R.string.get_video_fail));
                return;
            }
            mVideoFilePath = filePath;
            initVideoDuration();
            getRxBytes();
        } else if (requestCode == SYSTEM_SHOOT_VIDEO) {
            mVideoFilePath = this.filePath;
            initVideoDuration();
            getRxBytes();
        }
    }

    @Override
    public void refreshPage() {

    }

    /**
     * 获得网速
     */
    private void getRxBytes() {
        final long lastTotalRxBytes = NetWorkSpeedUtils.getTotalRxBytes();
        final long lastTimeStamp = System.currentTimeMillis();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                long nowTotalRxBytes = NetWorkSpeedUtils.getTotalRxBytes();
                long nowTimeStamp = System.currentTimeMillis();
                long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换
                Message msg = speedHandler.obtainMessage();
                msg.what = 100;
                msg.obj = speed;
                speedHandler.sendMessage(msg);
            }
        }, 500);//隔2s

    }

    Handler speedHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == 100) {
                long speed = msg.what;
                checkSpeed(speed);
            }
        }
    };

    private void checkSpeed(long speed) {
        Log.d(TAG, "speed = " + speed);
        CommonDialog commonDialog = new CommonDialog(mContext);
        if (speed > 0) {
            long time = (long) (videoSize / speed);//需要多少秒
            if (time > 60 * 1) {//大于1分钟给出提示
                commonDialog.showDialog("视频过大,可能需要" + time / 60 + "分钟上传,确定发布吗?", new CommonDialog.OnCommonDialogConfirmListener() {
                    @Override
                    public void onConfirmListener() {
                        processOss();
                    }
                });
            } else {
                processOss();
            }
        } else if (videoSize > 5000) {
            commonDialog.showDialog("视频过大,可能需要较长时间上传,确定发布吗?", new CommonDialog.OnCommonDialogConfirmListener() {
                @Override
                public void onConfirmListener() {
                    processOss();
                }
            });
        } else {
            //发布
            processOss();
        }

    }
}
