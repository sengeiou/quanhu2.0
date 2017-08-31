package com.rz.circled.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.constant.IntentKey;
import com.rz.common.oss.OssManager;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.DialogUtils;
import com.rz.common.utils.FileUtils;
import com.rz.sgt.jsbridge.JsEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Administrator on 2017/3/8 0008.
 */

public class UploadAudioActivity extends BaseActivity {

    public static final int SYSYEM_REQUEST_AUDIO = 1320;

    private String mFilePath;
    private String fileName;


    protected String uploadedUrl;

    private String currentUploadId;

    private long audioDuration;
    private long audioSize;

    private OssManager mOssManager;

    Dialog dialog;
//    MaterialDialog progressDialog;

    public String ossDir;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return super.loadView(inflater);
    }


    @Override
    public void initView() {
        mOssManager = new OssManager();
        ossDir = getIntent().getStringExtra(IntentKey.EXTRA_URL);
    }

    public void oss() {
        if (!TextUtils.isEmpty(mFilePath)) {
            String[] result = mFilePath.split("/");
            fileName = result[result.length - 1];
            String[] s = fileName.split("\\.");
            if (s != null && s.length > 0) {
                fileName = UUID.randomUUID().toString() + "." + s[s.length - 1];
            }
            uploadAudioFile();
        }
    }

    public void callResult(boolean result) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("url", uploadedUrl);
        map.put("audioTime", audioDuration);
        map.put("size", audioSize);
        JsEvent.callJsEvent(map, result);
        finish();
    }

    private void uploadAudioFile() {
        Log.d("test", "uploadVideoFile uoloadId is " + currentUploadId);
        mOssManager.asyncMultipartUpload(mFilePath, fileName, OssManager.AUDIO, new OssManager.OssCallBack() {
            @Override
            public void onSuccess(String url, String uploadId) {
                Log.d("multipartUpload", "OssCallBack onSuccess url " + url);
                Log.d("multipartUpload", "uploadId " + uploadId);
//                mAudioPublishProgress.setProgress(100);
                dismissProgress();
                currentUploadId = "";
                uploadedUrl = url;
                callResult(true);
//                Toast.makeText(App.getContext(), "音频上传成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String uploadId) {
                if (!TextUtils.isEmpty(uploadId)) {
                    currentUploadId = uploadId;
                }
                dismissProgress();
                showRetryPublishVideoDialog();
                Log.d("multipartUpload", "OssCallBack onFailure  ");
                Log.d("multipartUpload", "uploadId " + uploadId);
//                Toast.makeText(App.getContext(), getString(R.string.upload_video_fail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(String uploadId, float progress) {
                Log.d("multipartUpload", "OssCallBack onProgress  " + progress + "");
                if (!TextUtils.isEmpty(uploadId)) {
                    currentUploadId = uploadId;
                }
//                if (progressDialog != null) {
//                    progressDialog.setProgress((int) progress);
//                }
//                mAudioPublishProgress.setProgress((int) progress);
            }
        }, currentUploadId, TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir);
//        showProgressDialog("音频上传中");
//        progressDialog.setCancelable(false);
    }

    public void dismissProgress() {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
    }

//    public MaterialDialog showProgressDialog(String title, String msg) {
//        progressDialog = new MaterialDialog.Builder(this)
//                .content(title)
//                .contentGravity(GravityEnum.CENTER)
//                .content(msg)
//                .progress(true, 0)
//                .progressIndeterminateStyle(true)
//                .show();
//        return progressDialog;
//    }

//    public MaterialDialog showProgressDialog(String title) {
//        progressDialog = new MaterialDialog.Builder(this)
//                .content(title)
//                .contentGravity(GravityEnum.CENTER)
//                .progress(true, 0)
//                .progressIndeterminateStyle(true)
//                .show();
//        return progressDialog;
//    }


//    public MaterialDialog showDeterminateProgressDialog(String title) {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
//        progressDialog = new MaterialDialog.Builder(this)
//                .content(title)
//                .contentGravity(GravityEnum.CENTER)
//                .progress(false, 100, false)
//                .progressIndeterminateStyle(true)
//                .show();
//        return progressDialog;
//    }

//    public void showProgressDialog(String title, boolean cancelAble) {
//        progressDialog = new MaterialDialog.Builder(this)
//                .content(title)
//                .contentGravity(GravityEnum.CENTER)
//                .progress(true, 0)
//                .cancelable(cancelAble)
//                .progressIndeterminateStyle(true)
//                .show();
//    }

    private void showRetryPublishVideoDialog() {
        if (isFinishing()) {
            return;
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        View view = getLayoutInflater().inflate(R.layout.dialog_one_button, null, false);
        TextView tv = (TextView) view.findViewById(R.id.id_tv_message);
//        if (NetUtils.isNetworkConnected(this)) {
//            tv.setText("音频上传失败");
//        } else {
//            tv.setText(getString(R.string.status_un_network));
//        }
        view.findViewById(R.id.id_tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                uploadAudioFile();
            }
        });

        dialog = DialogUtils.selfDialog(this, view, false);
        dialog.show();
    }

    public void initData() {
        Intent i = new Intent(this, VoicePubActivity.class);
        i.putExtra(IntentKey.EXTRA_BOOLEAN, false);
        startActivityForResult(i, SYSYEM_REQUEST_AUDIO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SYSYEM_REQUEST_AUDIO) {
            if (data != null) {
                mFilePath = data.getStringExtra(IntentKey.EXTRA_PATH);
                if (!TextUtils.isEmpty(mFilePath)) {
                    initAudioDuration();
                    oss();
                }
            } else {
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initAudioDuration() {
        try {
            audioDuration = Long.parseLong(codeMediaInfo(mFilePath));
            audioSize = new Double(FileUtils.getFileSize(mFilePath, FileUtils.SIZETYPE_KB)).longValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String codeMediaInfo(String url) throws Exception {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        Log.d("UploadAudioAty", "str:" + url);
        mmr.setDataSource(url);
        String mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
        Log.d("UploadAudioAty", "mime:" + mime);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
        Log.d("UploadAudioAty", "duration:" + duration);
        mmr.release();
        return duration;
    }

}
