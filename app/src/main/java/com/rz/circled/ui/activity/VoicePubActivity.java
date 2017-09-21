package com.rz.circled.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.czt.mp3recorder.MP3Recorder;
import com.rz.circled.R;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.oss.OssManager;
import com.rz.common.permission.AfterPermissionGranted;
import com.rz.common.permission.AppSettingsDialog;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.DialogUtils;
import com.rz.common.utils.FileUtils;
import com.rz.common.utils.NetUtils;
import com.rz.common.utils.Record;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.common.widget.toasty.Toasty;
import com.rz.sgt.jsbridge.JsEvent;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rzw2 on 2016/8/9.
 */
public class VoicePubActivity extends BaseActivity {

    @BindView(R.id.iv_voice_record)
    ImageView ivVoiceRecord;
    @BindView(R.id.iv_voice_record_gif)
    ImageView ivVoiceRecordGif;
    @BindView(R.id.tv_voice_status)
    TextView tvVoiceStatus;
    @BindView(R.id.tv_voice_time)
    TextView tvVoiceTime;
    @BindView(R.id.tv_voice_re_answer)
    TextView tvVoiceReAnswer;
    @BindView(R.id.tv_voice_change_text)
    TextView tvVoiceChangeText;

    private Handler mHandler;

    // 录音类
    private MP3Recorder record;
    private Record palyer;
    // 产生的录音文件地址
    private String audioPath;
    // 录音起始时间
    private int mRecordTime = 0;

    PowerManager.WakeLock m_wklk;
    AnimationDrawable anim;

    public boolean nativePubish = true;
    private boolean isAanswer;

    private final int RECORD_START = 10;//点击开始录制
    private final int RECORD_ING = 11;//正在录制 -> 点击暂停录制
    private final int PLAY_START = 12;//点击开始播放
    private final int PLAY_ING = 13;//正在播放 -> 点击暂停播放

    private int voiceStatus = RECORD_START;

    private boolean canUpload = false;//是否可以上传
    private String uploadUrl;//上传url

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_voice_pub, null);
    }

    @Override
    public void initView() {

        isAanswer = getIntent().getBooleanExtra(IntentKey.EXTRA_BOOLEAN, false);//是否是录制回答的音频

        nativePubish = getIntent().getBooleanExtra(IntentKey.EXTRA_BOOLEAN, true);

        uploadUrl = getIntent().getStringExtra(IntentKey.EXTRA_URL);

        if (uploadedUrl != null)
            mOssManager = new OssManager();

        if (isAanswer) {
            tvVoiceChangeText.setVisibility(View.VISIBLE);
            setTitleText(R.string.reply);
        } else {
            setTitleText(R.string.record_voice);
        }

        tvVoiceTime.setText(getString(R.string.max) + CommonCode.Constant.MAX_INTERVAL_TIME + "s");

        setTitleLeftText(R.string.cancel);
        setTitleRightText(R.string.complete);
        setTitleRightTextColor(R.color.font_gray_m);
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canUpload) return;
                if (TextUtils.isEmpty(uploadUrl)) {
                    Intent i = new Intent();
                    i.putExtra(IntentKey.EXTRA_PATH, audioPath);
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    initAudioDuration();
                    oss();
                }
            }
        });

        record = new MP3Recorder(this);
        palyer = new Record(this);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.arg1 != 0) {
                    DecimalFormat df = new DecimalFormat();
                    String style = "0";// 定义要显示的数字的格式
                    df.applyPattern(style);// 将格式应用于格式化器
//                    tvVoiceTime.setText(df.format((msg.arg1 / 10) / 60) + ":" + df.format((msg.arg1 / 10) % 60));
                    tvVoiceTime.setText(df.format(msg.arg1 / 10) + "s/" + CommonCode.Constant.MAX_INTERVAL_TIME + "s");
                }
            }
        };

        record.setOnSpeechingListener(new MP3Recorder.OnSpeechingListener() {
            @Override
            public void nowRecordTime(int recordTime) {
                Log.v(TAG, "recordTime==" + recordTime);
                mRecordTime = recordTime;
                Message msg = mHandler.obtainMessage();
                msg.what = (int) (recordTime * (100f / CommonCode.Constant.MAX_INTERVAL_TIME));
                msg.arg1 = recordTime;
                msg.sendToTarget();
            }
        });
        record.setOnFinishedRecordListener(new MP3Recorder.OnFinishedRecordListener() {

            @Override
            public void onFinishedRecord(String Path, int recordTime) {

                mRecordTime = recordTime;
                audioPath = Path;

                processFinishRecord();

                Log.v(TAG, "onFinish" + recordTime + "audioPath");

            }

            @Override
            public void onCancleRecord(String info) {
                //取消录制 ->
                processCancelRecord();

                mHandler.sendEmptyMessage(0);
                mRecordTime = 0;
                audioPath = null;
                Log.v(TAG, "onCanel");
                if (!TextUtils.isEmpty(info)) {
                    Toasty.info(mContext, info).show();
                }
            }
        });
        palyer.setOnPlayListener(new Record.OnPlayListener() {

            @Override
            public void stopPlay() {
                //停止播放
                processFinishRecord();
            }

            @Override
            public void starPlay() {
                //播放语音
                processPlayVoice();
            }

            @Override
            public void pausePlay() {
                //暂停播放
                processFinishRecord();
            }

            @Override
            public void progressPlay(int playTime) {
                Log.v(TAG, "playTime==" + playTime);
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    /**
     * 开始录音/重新录音 -> 暂停录音
     */
    private void processStartRecord() {
        //开始录音 -> 可暂停
        tvVoiceStatus.setText(R.string.click_finish_record);//点击结束录音
        ivVoiceRecordGif.setVisibility(View.VISIBLE);
        tvVoiceReAnswer.setVisibility(View.GONE);
        ivVoiceRecord.setVisibility(View.GONE);
        Glide.with(mContext).load(R.drawable.icon_record_voice).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivVoiceRecordGif);
        tvVoiceReAnswer.setVisibility(View.GONE);
        canUpload = false;
        voiceStatus = RECORD_ING;
        setTitleRightTextColor(R.color.font_gray_m);
    }

    /**
     * 完成录制/停止播放 ->可播放录音
     */
    private void processFinishRecord() {
        //完成录音 -> 可播放录音
        ivVoiceRecord.setImageResource(R.mipmap.icon_record_play);//点击播放图片
        ivVoiceRecord.setVisibility(View.VISIBLE);
        ivVoiceRecordGif.setVisibility(View.GONE);
        tvVoiceStatus.setText(R.string.click_play_voice);//点击播放
        tvVoiceReAnswer.setVisibility(View.VISIBLE);//显示重新按钮
        if (isAanswer)
            tvVoiceReAnswer.setText(R.string.re_answer);
        else tvVoiceReAnswer.setText(R.string.re_record);
        tvVoiceTime.setText(mRecordTime / 10 + "s");//显示录制时长
        canUpload = true;
        voiceStatus = PLAY_START;
        setTitleRightTextColor(R.color.font_color_blue);
    }

    /**
     * 取消录制/重新录制 -> 可重新录制
     */
    private void processCancelRecord() {
        ivVoiceRecord.setImageResource(R.mipmap.icon_record);//点击播放图片
        ivVoiceRecord.setVisibility(View.VISIBLE);
        ivVoiceRecordGif.setVisibility(View.GONE);
        tvVoiceStatus.setText(R.string.click_start_record);//点击开始录音
        tvVoiceTime.setText(getString(R.string.max) + CommonCode.Constant.MAX_INTERVAL_TIME + "s");//最多xxs
        tvVoiceReAnswer.setVisibility(View.GONE);
        canUpload = false;
        voiceStatus = RECORD_START;
        setTitleRightTextColor(R.color.font_gray_m);
    }

    /**
     * 播放录音 -> 暂停
     */
    private void processPlayVoice() {
        //播放录音
        Glide.with(mContext).load(R.drawable.icon_play_voice).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivVoiceRecordGif);
        ivVoiceRecordGif.setVisibility(View.VISIBLE);
        ivVoiceRecord.setVisibility(View.GONE);
        tvVoiceStatus.setText(R.string.click_stop);//点击暂停
        tvVoiceTime.setText(mRecordTime / 10 + "s");//显示录制时长
        tvVoiceReAnswer.setVisibility(View.GONE);
        canUpload = true;
        voiceStatus = PLAY_ING;
        setTitleRightTextColor(R.color.font_color_blue);
    }

    @Override
    public void initData() {
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        m_wklk = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "cn");
        m_wklk.acquire(); // 设置保持唤醒
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ivVoiceRecord.getVisibility() == View.VISIBLE) {
                finish();
            } else {
                record.recordCanel("");
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_wklk.release(); // 解除保持唤醒
        if (record != null) {
            record.recordCanel("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_wklk.acquire(); // 设置保持唤醒
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_wklk.release();// 解除保持唤醒
        if (palyer.stop()) {
            palyer.stopPlay();
        }
    }

    @AfterPermissionGranted(RC_AUDIO_AND_EXTENER)
    public void doRecord() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) && EasyPermissions.hasPermissions(this, Manifest.permission.RECORD_AUDIO) && EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (!record.isRecording()) {
                try {
                    record.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "IOException " + e.getMessage());
                    record.recordCanel("请检查录音权限");
                    return;
                }
                processStartRecord();
            }
        } else {
            EasyPermissions.requestPermissions(this, "悠然一指要使用录音功能，读写存储卡权限", RC_AUDIO_AND_EXTENER, perms);
        }

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
            new AppSettingsDialog.Builder(this, "悠然一指要使用录音功能，读写存储卡权限，否则app可能无法正常运行")
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
                    .setRequestCode(RC_SETTINGS_SCREEN)
                    .build()
                    .show();
        }
    }

    @OnClick({R.id.iv_voice_record, R.id.iv_voice_record_gif, R.id.tv_voice_status, R.id.tv_voice_re_answer, R.id.tv_voice_change_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_voice_record:
            case R.id.iv_voice_record_gif:
            case R.id.tv_voice_status:
                processClickStatus();
                break;
            case R.id.tv_voice_re_answer:
                //重新回答
                processCancelRecord();
                break;
            case R.id.tv_voice_change_text:
                //切换文字回答
                break;
        }
    }

    /**
     * 处理点击状态
     */
    private void processClickStatus() {
        switch (voiceStatus) {
            case RECORD_START:
                doRecord();
                break;
            case RECORD_ING:
                if (mRecordTime < 30) {
                    SVProgressHUD.showInfoWithStatus(this, getString(R.string.record_cancel_error_hint));
                    record.recordCanel("");
                } else {
                    record.stop();
                }
                break;
            case PLAY_START:
                palyer.player(audioPath);
                break;
            case PLAY_ING:
                palyer.stopPlay();
//                record.recordCanel("");

                break;
        }
    }

    //****************************************js 与 oss 部分********************************************//

    private String fileName;

    protected String uploadedUrl;

    private String currentUploadId;

    private long audioDuration;
    private long audioSize;

    private OssManager mOssManager;

    Dialog dialog;

    public String ossDir;

    public void oss() {
        if (!TextUtils.isEmpty(audioPath)) {
            String[] result = audioPath.split("/");
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
        onLoadingStatus(CommonCode.General.DATA_LOADING);
        Log.d("test", "uploadVideoFile uoloadId is " + currentUploadId);
        if (mOssManager == null)
            mOssManager = new OssManager();
        mOssManager.asyncMultipartUpload(audioPath, fileName, OssManager.AUDIO, new OssManager.OssCallBack() {
            @Override
            public void onSuccess(String url, String uploadId) {
                Log.d("multipartUpload", "OssCallBack onSuccess url " + url + "\nuploadId " + uploadId);
                onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                currentUploadId = "";
                uploadedUrl = url;
                callResult(true);
            }

            @Override
            public void onFailure(String uploadId) {
                if (!TextUtils.isEmpty(uploadId)) {
                    currentUploadId = uploadId;
                }
                onLoadingStatus(CommonCode.General.LOAD_ERROR);
                showRetryPublishVideoDialog();
                Log.d("multipartUpload", "OssCallBack onFailure uploadId " + uploadId);
            }

            @Override
            public void onProgress(String uploadId, float progress) {
                Log.d("multipartUpload", "OssCallBack onProgress  " + progress + "");
                if (!TextUtils.isEmpty(uploadId)) {
                    currentUploadId = uploadId;
                }
            }
        }, currentUploadId, TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir);
        onLoadingStatus(CommonCode.General.DATA_LOADING);
    }

    private void showRetryPublishVideoDialog() {
        if (isFinishing()) {
            return;
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        View view = getLayoutInflater().inflate(R.layout.dialog_one_button, null, false);
        TextView tv = (TextView) view.findViewById(R.id.id_tv_message);
        if (NetUtils.isNetworkConnected(this)) {
            tv.setText("音频上传失败");
        } else {
            tv.setText(getString(R.string.status_un_network));
        }
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

    private void initAudioDuration() {
        try {
            audioDuration = Long.parseLong(codeMediaInfo(audioPath));
            audioSize = new Double(FileUtils.getFileSize(audioPath, FileUtils.SIZETYPE_KB)).longValue();
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

    @Override
    public void refreshPage() {

    }
}
