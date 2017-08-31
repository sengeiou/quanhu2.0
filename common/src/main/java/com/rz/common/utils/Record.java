package com.rz.common.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.rz.common.application.BaseApplication;
import com.rz.common.constant.CommonCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Record {
    private static final int HANDLE_FLAG = 33333721;// 用作massage的标示
    private Context context;
    /**
     * 存放录音文件的路径
     */
    private File myRecAudioDir;// 存放录音的文件夹
    private File myRecAudioFile;// 存放片段的录音
    private File bestFile;// 综合的录音文件
    /**
     * 音频文件的格式
     */
    private final String SUFFIX = ".mp4";
    private MediaRecorder mMediaRecorder01;
    private MediaPlayer mPlayer;
    private int mRecordTime;// 录音起始时间
    private int mRecordPlayTime;
    // private ObtainDecibelThread mThread;
    private Timer timer;
    // 录音播放计时器
    private Timer playtimer;
    private Handler mRecordTimeHandler; // 用于更新录音时间
    private Handler mRecordPlayTimeHandler; // 用于更新录音播放时间
    private OnSpeechingListener mSpeechingListener;// 录音时间监听
    private OnFinishedRecordListener mFinishedListerer;
    private OnPlayListener listener;
    private boolean isPause;// 是否处于暂停状态
    private boolean inThePause;
    private boolean isStopRecord;// 是否停止录音
    private boolean mIsPlaying;// 是否正在播放
    private boolean inPlayPause;// 是否处于播放暂停
    /**
     * 记录需要合成的几段amr语音文件
     **/
    private ArrayList<String> list;
    private String length1;// 计算文件大小（单位：KB)

    public Record(Context context) {
        this.context = context;
        myRecAudioDir = new File(
                Environment.getExternalStorageDirectory() + File.separator + "daci" + File.separator + "myrecord");
        if (!myRecAudioDir.exists()) {
            myRecAudioDir.mkdirs();
            Log.v("录音", "创建录音文件！" + myRecAudioDir.exists());
        }
        list = new ArrayList<String>();
        mRecordTimeHandler = new RecordTimeHandler(context);
        mRecordPlayTimeHandler = new RecordPlayTimeHandler(context);
    }

    /**
     * 开始录音
     */
    public boolean start() {
        mRecordTime = 0;
        list.clear();
        return recordStart();
    }


    /**
     * 停止录音
     */
    public boolean stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        // 这里写暂停处理的 文件！加上list里面 语音合成起来
        if (isPause) {
            // 在暂停状态按下结束键,处理list就可以了
            if (inThePause) {
                getInputCollection(list, false);
            }
            // 在正在录音时，处理list里面的和正在录音的语音
            else {
                list.add(myRecAudioFile.getPath());
                recodeStop();
                getInputCollection(list, true);
            }
            // 还原标志位
            isPause = false;
            inThePause = false;
            if (mFinishedListerer != null) {
                mFinishedListerer.onFinishedRecord(bestFile.getPath(), mRecordTime);
            }
        }
        // 若录音没有经过任何暂停
        else {
            if (myRecAudioFile != null && mMediaRecorder01 != null) {
                try {
                    // 停止录音
                    mMediaRecorder01.stop();
                    mMediaRecorder01.release();
                    mMediaRecorder01 = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                if (mFinishedListerer != null) {
                    mFinishedListerer.onFinishedRecord(myRecAudioFile.getPath(), mRecordTime);
                }
            }
        }
        return true;
    }

    /**
     * 暂停录音
     */
    public void onPause() {
        isPause = true;
        // 已经暂停过了，再次点击按钮 开始录音，录音状态在录音中
        if (inThePause) {
            recordStart();
            inThePause = false;
        }
        // 正在录音，点击暂停,现在录音状态为暂停
        else {
            // 当前正在录音的文件名，全程
            recodeStop();
            list.add(myRecAudioFile.getPath());
            inThePause = true;
        }
    }

    /**
     * 播放录音
     */
    public void player(String str) {
        if (str != null) {
            // 打开播放程序
            startPlay(str);
            // openFile(bestFile);
        } else {
            Toast.makeText(context, "你选的是一个空文件", Toast.LENGTH_LONG).show();
            Log.d("没有选择文件", "没有选择文件");
        }
    }

    public boolean prepared = false;
    public boolean targetPlaying = false;


    public void startOrPreparedPlay(String mAudioPath) {
        targetPlaying = true;
        if (prepared) {
            if (mPlayer != null) {
                mPlayer.start();
                if (listener != null) {
                    listener.starPlay();
                }
                mRecordPlayTime = 0;
                playtimer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        mRecordPlayTime++;
                        Message msg = mRecordPlayTimeHandler.obtainMessage();
                        msg.what = HANDLE_FLAG;
                        msg.arg1 = mRecordPlayTime;
                        mRecordPlayTimeHandler.sendMessage(msg);
                    }
                };
                playtimer.schedule(timerTask, 100, 100);
                mIsPlaying = true;
                mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {

                        return false;
                    }
                });
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopPlay();
                    }
                });
                return;
            }
        } else {
            startPlay(mAudioPath);
        }
    }

    /**
     * 开始播放
     */
    public void startPlay(String mAudioPath) {
        startPlay(mAudioPath, null);
    }

    public void resume(String mAudioPath) {
        try {
            if (prepared && mPlayer != null) {
                mPlayer.start();
                if (listener != null) {
                    listener.starPlay();
                }
                mRecordPlayTime = 0;
                playtimer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        mRecordPlayTime++;
                        Message msg = mRecordPlayTimeHandler.obtainMessage();
                        msg.what = HANDLE_FLAG;
                        msg.arg1 = mRecordPlayTime;
                        mRecordPlayTimeHandler.sendMessage(msg);
                    }
                };
                playtimer.schedule(timerTask, 100, 100);
                mIsPlaying = true;
                mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {

                        return false;
                    }
                });
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopPlay();
                    }
                });
            } else {
                startPlay(mAudioPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startAudio() {
        if (!targetPlaying) {
            return;
        }
        try {
            mPlayer.start();
            if (listener != null) {
                listener.starPlay();
            }
            mRecordPlayTime = 0;
            playtimer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    mRecordPlayTime++;
                    Message msg = mRecordPlayTimeHandler.obtainMessage();
                    msg.what = HANDLE_FLAG;
                    msg.arg1 = mRecordPlayTime;
                    mRecordPlayTimeHandler.sendMessage(msg);
                }
            };
            playtimer.schedule(timerTask, 100, 100);
            mIsPlaying = true;
            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {

                    return false;
                }
            });
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlay();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void asyncStartPlay(String audioPath) {
        targetPlaying = true;
        if (!TextUtils.isEmpty(audioPath)) {
            try {
                if (!prepared) {
                    mPlayer = new MediaPlayer();
                    mPlayer.setDataSource(audioPath);
                    mPlayer.prepareAsync();
                    mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            prepared = true;
                            startAudio();
                        }
                    });
                } else {
                    startAudio();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(BaseApplication.getContext(), "音频文件未找到~", Toast.LENGTH_SHORT).show();
        }
    }


    public void startPlay(String audioPath, TextView timeView) {
        if (!mIsPlaying) {// 这个播放没有经过任何暂停
            if (!TextUtils.isEmpty(audioPath)) {
                mPlayer = new MediaPlayer();
                try {
                    mPlayer.setDataSource(audioPath);
                    mPlayer.prepare();
                    mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
//                            mp.start();
                            prepared = true;
                        }
                    });
                    if (timeView != null) {
                        int len = (mPlayer.getDuration() + 500) / 1000;
                        timeView.setText(len + "s");
                    }
                    mPlayer.start();
                    Log.d("yeying", "audio start play path is " + audioPath);
                    if (listener != null) {
                        listener.starPlay();
                    }
                    mRecordPlayTime = 0;
                    playtimer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            mRecordPlayTime++;
                            Message msg = mRecordPlayTimeHandler.obtainMessage();
                            msg.what = HANDLE_FLAG;
                            msg.arg1 = mRecordPlayTime;
                            mRecordPlayTimeHandler.sendMessage(msg);
                        }
                    };
                    playtimer.schedule(timerTask, 100, 100);
                    mIsPlaying = true;
                    mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {

                            return false;
                        }
                    });
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopPlay();
                        }
                    });
//                    mPlayer.prepareAsync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(BaseApplication.getContext(), "音频文件未找到~", Toast.LENGTH_SHORT).show();
            }
        } else {// 这个播放暂停过
            // 已经暂停过了，再次点击按钮 开始播放，播放状态在播放中
            if (inPlayPause) {
                inPlayPause = false;
                mPlayer.start();
                if (listener != null) {
                    listener.starPlay();
                }
                playtimer = new Timer();
                TimerTask timerTask = new TimerTask() {

                    @Override
                    public void run() {
                        mRecordPlayTime++;
                        Message msg = mRecordPlayTimeHandler.obtainMessage();
                        msg.what = HANDLE_FLAG;
                        msg.arg1 = mRecordPlayTime;
                        mRecordPlayTimeHandler.sendMessage(msg);
                    }

                };
                playtimer.schedule(timerTask, 100, 100);
            } else {// 正在播放，点击暂停,现在播放状态为暂停
                inPlayPause = true;
                mPlayer.pause();
                if (listener != null) {
                    listener.pausePlay();
                }
                if (playtimer != null) {
                    playtimer.cancel();
                    playtimer = null;
                }
            }

        } // end playing
    }

    public void pausePlay() {
        if (mPlayer != null) {
//            mPlayer.seekTo(0);
            mPlayer.pause();
            targetPlaying = false;
            mIsPlaying = false;
            if (listener != null) {
                listener.stopPlay();
            }
            if (playtimer != null) {
                playtimer.cancel();
                playtimer = null;
            }
        }
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        if (mPlayer != null) {
            targetPlaying = false;
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            mIsPlaying = false;
            prepared = false;
            if (listener != null) {
                listener.stopPlay();
            }
            if (playtimer != null) {
                playtimer.cancel();
                playtimer = null;
            }
        }
    }

    public void stopPlay(boolean forceStop) {
        this.forceStop = forceStop;
        if (mPlayer != null) {
            targetPlaying = false;
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            mIsPlaying = false;
            prepared = false;
            if (listener != null) {
                listener.stopPlay();
            }
            if (playtimer != null) {
                playtimer.cancel();
                playtimer = null;
            }
        }
    }

    private boolean forceStop = false;

    public boolean isPlayIng() {
        return mIsPlaying;
    }

    private boolean recordStart() {
        if (bestFile != null)
            bestFile = null;
        String mMinute1 = getTime();
        // 创建音频文件
        myRecAudioFile = new File(myRecAudioDir, mMinute1 + SUFFIX);
        mMediaRecorder01 = new MediaRecorder();
        mMediaRecorder01.reset();
        // 设置录音为麦克风
        mMediaRecorder01.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder01.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mMediaRecorder01.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        // 录音文件保存这里
        mMediaRecorder01.setOutputFile(myRecAudioFile.getAbsolutePath());
        try {
            mMediaRecorder01.prepare();
            mMediaRecorder01.start();
        } catch (IllegalStateException e) {
            Toast.makeText(BaseApplication.getContext(), "请检查录音权限是否开启~", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            Toast.makeText(BaseApplication.getContext(), "请检查录音权限是否开启~", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            Toast.makeText(BaseApplication.getContext(), "请检查录音权限是否开启~", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }


        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mRecordTime++;
                Message msg = mRecordTimeHandler.obtainMessage();
                msg.what = HANDLE_FLAG;
                msg.arg1 = mRecordTime;
                mRecordTimeHandler.sendMessage(msg);
            }
        };
        timer.schedule(timerTask, 100, 100);
        return true;
    }

    // 打开录音播放程序
    private void openFile(File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMIMEType(f);
        intent.setDataAndType(Uri.fromFile(f), type);
        context.startActivity(intent);
    }

    private String getMIMEType(File f) {

        String end = f.getName().substring(f.getName().lastIndexOf(".") + 1, f.getName().length()).toLowerCase();
        String type = "";
        if (end.equals("mp3") || end.equals("aac") || end.equals("amr") || end.equals("mpeg") || end.equals("mp4")) {
            type = "audio";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg")) {
            type = "image";
        } else {
            type = "*";
        }
        type += "/";
        return type;
    }

    /**
     * @param isAddLastRecord 是否需要添加list之外的最新录音，一起合并
     * @return 将合并的流用字符保存
     */
    public void getInputCollection(List list, boolean isAddLastRecord) {
        if (list.size() > 0) {
            String mMinute1 = getTime();
            // Toast.makeText(EX07.this,
            // "当前时间是:"+mMinute1,Toast.LENGTH_LONG).show();

            // 创建音频文件,合并的文件放这里
            bestFile = new File(myRecAudioDir, mMinute1 + SUFFIX);
            FileOutputStream fileOutputStream = null;

            if (!bestFile.exists()) {
                try {
                    bestFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                fileOutputStream = new FileOutputStream(bestFile);

            } catch (IOException e) {
                e.printStackTrace();
            }
            // list里面为暂停录音 所产生的 几段录音文件的名字，中间几段文件的减去前面的6个字节头文件

            for (int i = 0; i < list.size(); i++) {
                File file = new File((String) list.get(i));
                Log.d("list的长度", list.size() + "");
                if (file.exists())
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        byte[] myByte = new byte[fileInputStream.available()];
                        // 文件长度
                        int length = myByte.length;
                        System.out.println("开始长度：" + length);
                        // 头文件
                        if (i == 0) {
                            while (fileInputStream.read(myByte) != -1) {
                                fileOutputStream.write(myByte, 0, length);
                            }
                        }

                        // 之后的文件，去掉头文件就可以了
                        else {
                            while (fileInputStream.read(myByte) != -1) {

                                fileOutputStream.write(myByte, 6, length - 6);
                            }
                        }

                        fileOutputStream.flush();
                        fileInputStream.close();
                        System.out.println("合成文件长度：" + bestFile.length());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }
            // 结束后关闭流
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 合成一个文件后，删除之前暂停录音所保存的零碎合成文件
            deleteListRecord(isAddLastRecord);
        }

    }

    /**
     * 删除录音片段
     *
     * @param isAddLastRecord
     */
    private void deleteListRecord(boolean isAddLastRecord) {
        for (int i = 0; i < list.size(); i++) {
            File file = new File((String) list.get(i));
            if (file.exists()) {
                file.delete();
            }
        }
        // 正在暂停后，继续录音的这一段音频文件
        if (isAddLastRecord && myRecAudioFile != null) {
            myRecAudioFile.delete();
        }
    }

    private void recodeStop() {
        if (mMediaRecorder01 != null && !isStopRecord) {
            // 停止录音
            mMediaRecorder01.stop();
            mMediaRecorder01.release();
            mMediaRecorder01 = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void recordCancel() {
        recodeStop();
        stopPlay();
        if (myRecAudioFile != null)
            if (myRecAudioFile.exists())
                myRecAudioFile.delete();
        if (bestFile != null)
            if (bestFile.exists())
                bestFile.delete();
        if (mFinishedListerer != null) {
            mFinishedListerer.onCancleRecord();
        }
        mIsPlaying = false;
        // 还原标志位
        isPause = false;
        inThePause = false;
        getInputCollection(list, true);
        list.clear();
        if (bestFile != null)
            if (bestFile.exists())
                bestFile.delete();
    }

    private String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH：mm：ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String time = formatter.format(curDate);
        System.out.println("当前时间");
        return time;
    }

    /*******************************
     * inner class
     ****************************************/

    private class ObtainDecibelThread extends Thread {
        private volatile boolean running = true;

        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mRecordTime++;
                Message msg = mRecordTimeHandler.obtainMessage();
                msg.what = HANDLE_FLAG;
                msg.arg1 = mRecordTime;
                // 没有录音了.
                if (context == null && !running) {
                    exit();
                }
                mRecordTimeHandler.sendMessage(msg);
            }
        }
    }

    class RecordTimeHandler extends Handler {
        public RecordTimeHandler(Context context) {
        }

        @Override
        public void handleMessage(Message msg) {
            if (timer != null)
                if (msg.what == HANDLE_FLAG) {
                    if (msg.arg1 > CommonCode.Constant.MAX_INTERVAL_TIME * 10) {
                        stop();
                    } else {
                        if (mSpeechingListener != null) {
                            mSpeechingListener.nowRecordTime(msg.arg1);
                        }
                    }
                }
        }
    }

    class RecordPlayTimeHandler extends Handler {
        public RecordPlayTimeHandler(Context context) {
        }

        @Override
        public void handleMessage(Message msg) {
            if (playtimer != null)
                if (msg.what == HANDLE_FLAG) {
                    if (listener != null) {
                        listener.progressPlay(msg.arg1);
                    }
                }
        }
    }

    // 结束后需要释放资源
    public void onDestroy() {
        if (mMediaRecorder01 != null && !isStopRecord) {
            // 停止录音
            mMediaRecorder01.stop();
            mMediaRecorder01.release();
            mMediaRecorder01 = null;
        }

        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            mIsPlaying = false;
            if (listener != null) {
                listener.stopPlay();
            }
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 播放声音结束时调用
     *
     * @param l
     */
    public void setOnPlayListener(OnPlayListener l) {
        this.listener = l;
    }

    public interface OnPlayListener {
        /**
         * 播放声音结束时调用
         */
        void stopPlay();

        /**
         * 播放声音暂停时调用
         */
        void pausePlay();

        /**
         * 播放声音开始时调用
         */
        void starPlay();

        /**
         * 播放进度
         */
        void progressPlay(int playTime);

        void onError(int code);
    }

    /**
     * 结束录音的监听器
     *
     * @param listener
     */
    public void setOnFinishedRecordListener(OnFinishedRecordListener listener) {
        mFinishedListerer = listener;
    }

    public interface OnFinishedRecordListener {
        /**
         * 用户手动取消
         */
        public void onCancleRecord();

        /**
         * 录音完成
         */
        public void onFinishedRecord(String audioPath, int recordTime);
    }

    /**
     * 正在录音的监听器
     *
     * @param listener
     */
    public void setOnSpeechingListener(OnSpeechingListener listener) {
        mSpeechingListener = listener;
    }

    public interface OnSpeechingListener {
        /**
         * 当前录音时间
         */
        void nowRecordTime(int recordTime);
    }
}
