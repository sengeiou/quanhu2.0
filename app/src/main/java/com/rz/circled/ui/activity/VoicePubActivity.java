package com.rz.circled.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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

import com.czt.mp3recorder.MP3Recorder;
import com.rz.circled.R;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.permission.AfterPermissionGranted;
import com.rz.common.permission.AppSettingsDialog;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.Record;
import com.rz.common.widget.RoundProgressBar;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.common.widget.toasty.Toasty;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rzw2 on 2016/8/9.
 */
public class VoicePubActivity extends BaseActivity {

    @BindView(R.id.tv_tip)
    TextView tvTip;
    // 控制录音的progress
    @BindView(R.id.progress_record)
    RoundProgressBar progressRecord;
    @BindView(R.id.img_record)
    ImageView imgRecord;
    @BindView(R.id.img_record1)
    ImageView imgPause;
    @BindView(R.id.img_record2)
    ImageView imgPlaying;
    @BindView(R.id.img_record3)
    ImageView imgPlay;
    @BindView(R.id.btn1)
    TextView btn1;
    @BindView(R.id.btn2)
    TextView btn2;
    @BindView(R.id.tv_time)
    TextView tvTime;

    // 控制录音进度条handler
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

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_voice_pub, null);
    }

    @Override
    public void initView() {
        nativePubish = getIntent().getBooleanExtra(IntentKey.EXTRA_BOOLEAN, true);
//        setBackTextWithNull("取消");
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressRecord.setProgress(msg.what);
                if (msg.arg1 != 0) {
                    DecimalFormat df = new DecimalFormat();
                    String style = "00";// 定义要显示的数字的格式
                    df.applyPattern(style);// 将格式应用于格式化器
                    tvTime.setText(df.format((msg.arg1 / 10) / 60) + ":" + df.format((msg.arg1 / 10) % 60));
                } else {
                    tvTime.setText("");
                }
            }
        };

        record = new MP3Recorder(this);

        anim = (AnimationDrawable) imgPlaying.getDrawable();
        palyer = new Record(this);
        record.setOnSpeechingListener(new MP3Recorder.OnSpeechingListener() {
            @Override
            public void nowRecordTime(int recordTime) {
                Log.v("zxw", "recordTime==" + recordTime);
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
                tvTip.setText("点击试听");
                progressRecord.setProgress(1000);
                imgRecord.setVisibility(View.GONE);
                imgPause.setVisibility(View.GONE);
                imgPlaying.setVisibility(View.GONE);
                imgPlay.setVisibility(View.VISIBLE);

                mRecordTime = recordTime;
                Log.v("zxw", "onFinish" + recordTime + "audioPath");
                audioPath = Path;

                btn1.setEnabled(true);
                btn2.setEnabled(true);
                btn1.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_bg_base_sel));
                btn2.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_bg_base_sel));
            }

            @Override
            public void onCancleRecord(String info) {
                tvTip.setText("点击开始录音，最长可以录制120”");
                imgRecord.setVisibility(View.VISIBLE);
                imgPause.setVisibility(View.GONE);
                imgPlaying.setVisibility(View.GONE);
                imgPlay.setVisibility(View.GONE);

                mHandler.sendEmptyMessage(0);
                mRecordTime = 0;
                audioPath = null;
                Log.v("zxw", "onCanel");
                btn1.setEnabled(false);
                btn2.setEnabled(false);

                btn1.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_round_gray));
                btn2.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_round_gray));
                if (!TextUtils.isEmpty(info)) {
                    Toasty.info(mContext, info).show();
                }
            }
        });
        palyer.setOnPlayListener(new Record.OnPlayListener() {

            @Override
            public void stopPlay() {
                tvTip.setText("点击试听");
                progressRecord.setProgress(1000);
                imgRecord.setVisibility(View.GONE);
                imgPause.setVisibility(View.GONE);
                imgPlaying.setVisibility(View.GONE);
                imgPlay.setVisibility(View.VISIBLE);
                anim.stop();

                btn1.setEnabled(true);
                btn2.setEnabled(true);
                btn1.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_bg_base_sel));
                btn2.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_bg_base_sel));
            }

            @Override
            public void starPlay() {
                tvTip.setText("语音播放中，点击停止播放");
                progressRecord.setProgress(1000);
                imgRecord.setVisibility(View.GONE);
                imgPause.setVisibility(View.GONE);
                imgPlaying.setVisibility(View.VISIBLE);
                imgPlay.setVisibility(View.GONE);
                anim.start();

                btn1.setEnabled(true);
                btn2.setEnabled(true);
                btn1.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_bg_base_sel));
                btn2.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_bg_base_sel));
            }

            @Override
            public void pausePlay() {
                tvTip.setText("点击试听");
                progressRecord.setProgress(1000);
                imgRecord.setVisibility(View.GONE);
                imgPause.setVisibility(View.GONE);
                imgPlaying.setVisibility(View.GONE);
                imgPlay.setVisibility(View.VISIBLE);
                anim.stop();

                btn1.setEnabled(true);
                btn2.setEnabled(true);
                btn1.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_bg_base_sel));
                btn2.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_bg_base_sel));
            }

            @Override
            public void progressPlay(int playTime) {
                Log.v("zxw", "playTime==" + playTime);
            }

            @Override
            public void onError(int code) {

            }
        });
        btn1.setEnabled(false);
        btn2.setEnabled(false);
    }

    private void initMp3() {

    }

    @Override
    public void initData() {
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        m_wklk = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "cn");
        m_wklk.acquire(); // 设置保持唤醒
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.img_record, R.id.img_record1, R.id.img_record2, R.id.img_record3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                palyer.stopPlay();
                record.recordCanel("");
                break;
            case R.id.btn2:
//                if (nativePubish) {
//                    Intent intent = new Intent(this, PublishOpusAty.class);
//                    intent.putExtra(IntentKey.KEY_TYPE, Type.TYPE_AUDIO);
//                    intent.putExtra(IntentKey.KEY_PATH, audioPath);
//                    startActivity(intent);
//                    finish();
//                } else {
                Intent i = new Intent();
                i.putExtra(IntentKey.EXTRA_PATH, audioPath);
                setResult(RESULT_OK, i);
                finish();
//                }
                break;
            case R.id.img_record:
                doRecord();
                break;
            case R.id.img_record1:
                if (mRecordTime < 30) {
                    SVProgressHUD.showInfoWithStatus(this, "录音长度需大于3秒，请重新录制");
                    record.recordCanel("");
                } else {
                    record.stop();
                }
                break;
            case R.id.img_record2:
                palyer.player(audioPath);
                break;
            case R.id.img_record3:
                palyer.player(audioPath);
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (imgRecord.getVisibility() == View.VISIBLE) {
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
                    Log.d("yeying", "IOException " + e.getMessage());
                    record.recordCanel("请检查录音权限");
                    return;
                }
                tvTip.setText("录音中，点击停止录音");
                imgRecord.setVisibility(View.GONE);
                imgPause.setVisibility(View.VISIBLE);
                imgPlaying.setVisibility(View.GONE);
                imgPlay.setVisibility(View.GONE);
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
}
