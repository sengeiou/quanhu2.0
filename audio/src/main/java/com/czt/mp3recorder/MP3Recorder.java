package com.czt.mp3recorder;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.czt.mp3recorder.util.LameUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MP3Recorder {
    //=======================AudioRecord Default Settings=======================
    private static final int DEFAULT_AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    /**
     * 以下三项为默认配置参数。Google Android文档明确表明只有以下3个参数是可以在所有设备上保证支持的。
     */
    private static final int DEFAULT_SAMPLING_RATE = 44100;//模拟器仅支持从麦克风输入8kHz采样率
    private static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    /**
     * 下面是对此的封装
     * private static final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
     */
    private static final PCMFormat DEFAULT_AUDIO_FORMAT = PCMFormat.PCM_16BIT;

    //======================Lame Default Settings=====================
    private static final int DEFAULT_LAME_MP3_QUALITY = 7;
    /**
     * 与DEFAULT_CHANNEL_CONFIG相关，因为是mono单声，所以是1
     */
    private static final int DEFAULT_LAME_IN_CHANNEL = 1;
    /**
     * Encoded bit rate. MP3 file will be encoded with bit rate 32kbps
     */
    private static final int DEFAULT_LAME_MP3_BIT_RATE = 32;

    //==================================================================

    /**
     * 自定义 每160帧作为一个周期，通知一下需要进行编码
     */
    private static final int FRAME_COUNT = 160;
    private AudioRecord mAudioRecord = null;
    private int mBufferSize;
    private short[] mPCMBuffer;
    private DataEncodeThread mEncodeThread;
    private boolean mIsRecording = false;
    private File mRecordFile;
    private File recordFileDir;
    private Context context;
    private final String SUFFIX = ".mp3";
    private CustomThread currentThread;

    /**
     * Default constructor. Setup recorder with default sampling rate 1 channel,
     * 16 bits pcm
     */
    public MP3Recorder(Context context) {
        recordFileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "daci" + File.separator + "myrecord");
        if (!recordFileDir.exists()) {
            recordFileDir.mkdirs();
            Log.v("录音", "创建录音文件！" + recordFileDir.exists());
        }
        this.context = context;
        mRecordTimeHandler = new RecordTimeHandler(context);
    }

    private String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH：mm：ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String time = formatter.format(curDate);
        System.out.println("当前时间");
        return time;
    }


    public class CustomThread extends Thread {
        public boolean delete = false;
        public boolean hasEff = false;
    }

    private void mainCancel() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                recordCanel("请检查是否打开应用权限");
            }
        });
    }

    /**
     * Start recording. Create an encoding thread. Start record from this
     * thread.
     *
     * @throws IOException initAudioRecorder throws
     */
    public void start() throws IOException {
        if (mIsRecording) {
            return;
        }
//        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS
        Log.d("yeying", "start ");
        String mMinute1 = getTime();
        mRecordFile = new File(recordFileDir, mMinute1 + SUFFIX);
        mRecordTime = 0;
        mIsRecording = true; // 提早，防止init或startRecording被多次调用
        Log.d("yeying", "start 1");
        initAudioRecorder();
        mAudioRecord.startRecording();
        Log.d("yeying", "start 2");
        currentThread = new CustomThread() {

            @Override
            public void run() {
                //设置线程权限
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                boolean begin = true;
                Log.d("yeying", "start 3");
                while (mIsRecording) {
                    int readSize = mAudioRecord.read(mPCMBuffer, 0, mBufferSize);
                    Log.d("yeying", "start 4");
                    if (readSize > 0) {
                        mEncodeThread.addTask(mPCMBuffer, readSize);
                        boolean result = calculateRealVolume(mPCMBuffer, readSize);
                        if (!hasEff) {
                            hasEff = !result;
                        }
                        Log.d("yeying", "volume1 " + mVolume);
                        //华为(部分)权限，部分手机读取音频数据时读取到长度但数据为0，还有部分手机打开了权限，但前面数据是0，过一会才非0
//                        if (!begin && result) {
//                            mainCancel();
//                            break;
//                        }
                        begin = false;
                    } else {
                        //小米权限,部分手机读取音频数据时读取不到长度
                        mainCancel();
                        break;
                    }
                }
                // release and finalize audioRecord
                mAudioRecord.stop();
                mAudioRecord.release();
                mAudioRecord = null;
                // stop the encoding thread and try to wait
                // until the thread finishes its job
                Log.d("yeying", "start 5");
                mEncodeThread.sendStopMessage(delete);
            }

            /**
             * 此计算方法来自samsung开发范例
             *
             * @param buffer buffer
             * @param readSize readSize
             */
            private boolean calculateRealVolume(short[] buffer, int readSize) {
                double sum = 0;
                boolean empty = true;
                for (int i = 0; i < readSize; i++) {
                    // 这里没有做运算的优化，为了更加清晰的展示代码
//                    Log.d("yeying", " buffer[i] " + buffer[i]);
                    if (empty) {
                        empty = buffer[i] == 0;
                    }
                    sum += buffer[i] * buffer[i];
                }
                if (readSize > 0) {
                    double amplitude = sum / readSize;
                    mVolume = (int) Math.sqrt(amplitude);
                }
                return empty;
            }
        };
        currentThread.start();
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
    }

    private int mVolume;

    /**
     * 获取真实的音量。 [算法来自三星]
     *
     * @return 真实音量
     */
    public int getRealVolume() {
        return mVolume;
    }

    /**
     * 获取相对音量。 超过最大值时取最大值。
     *
     * @return 音量
     */
    public int getVolume() {
        if (mVolume >= MAX_VOLUME) {
            return MAX_VOLUME;
        }
        return mVolume;
    }

    private static final int MAX_VOLUME = 2000;

    /**
     * 根据资料假定的最大值。 实测时有时超过此值。
     *
     * @return 最大音量值。
     */
    public int getMaxVolume() {
        return MAX_VOLUME;
    }

    public void stop() {
        mIsRecording = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (mFinishedListerer != null) {
            if (currentThread != null) {
                if (currentThread.hasEff) {
                    mFinishedListerer.onFinishedRecord(mRecordFile.getPath(), mRecordTime);
                } else {
                    mFinishedListerer.onCancleRecord("生成音频文件失败，请检查录音权限是否开启");
                }
            }

        }
    }

    public void recordCanel(String info) {
        if (currentThread != null) {
            currentThread.delete = true;
        }
        mIsRecording = false;
//        if (mRecordFile != null && mRecordFile.exists()) {
//            mRecordFile.delete();
//        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (mFinishedListerer != null) {
            mFinishedListerer.onCancleRecord(info);
        }
    }

    public boolean isRecording() {
        return mIsRecording;
    }

    /**
     * Initialize audio recorder
     */
    private void initAudioRecorder() throws IOException {
        mBufferSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLING_RATE,
                DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT.getAudioFormat());

        int bytesPerFrame = DEFAULT_AUDIO_FORMAT.getBytesPerFrame();
        /* Get number of samples. Calculate the buffer size
         * (round up to the factor of given frame size)
		 * 使能被整除，方便下面的周期性通知
		 * */
        int frameSize = mBufferSize / bytesPerFrame;
        if (frameSize % FRAME_COUNT != 0) {
            frameSize += (FRAME_COUNT - frameSize % FRAME_COUNT);
            mBufferSize = frameSize * bytesPerFrame;
        }

		/* Setup audio recorder */
        mAudioRecord = new AudioRecord(DEFAULT_AUDIO_SOURCE,
                DEFAULT_SAMPLING_RATE, DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT.getAudioFormat(),
                mBufferSize);

        mPCMBuffer = new short[mBufferSize];
        /*
         * Initialize lame buffer
		 * mp3 sampling rate is the same as the recorded pcm sampling rate 
		 * The bit rate is 32kbps
		 * 
		 */
        LameUtil.init(DEFAULT_SAMPLING_RATE, DEFAULT_LAME_IN_CHANNEL, DEFAULT_SAMPLING_RATE, DEFAULT_LAME_MP3_BIT_RATE, DEFAULT_LAME_MP3_QUALITY);
        // Create and run thread used to encode data
        // The thread will
        mEncodeThread = new DataEncodeThread(mRecordFile, mBufferSize);
        mEncodeThread.start();
        mAudioRecord.setRecordPositionUpdateListener(mEncodeThread, mEncodeThread.getHandler());
        mAudioRecord.setPositionNotificationPeriod(FRAME_COUNT);
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
        public void onCancleRecord(String info);

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

    private Handler mRecordTimeHandler; // 用于更新录音时间
    private Handler mRecordPlayTimeHandler; // 用于更新录音播放时间
    private OnSpeechingListener mSpeechingListener;// 录音时间监听
    private OnFinishedRecordListener mFinishedListerer;
    public static final int MAX_INTERVAL_TIME = 180;
    private Timer timer;
    // 录音播放计时器
    private Timer playtimer;

    class RecordTimeHandler extends Handler {
        public RecordTimeHandler(Context context) {
        }

        @Override
        public void handleMessage(Message msg) {
            if (timer != null)
                if (msg.what == HANDLE_FLAG) {
                    if (msg.arg1 > MAX_INTERVAL_TIME * 10) {
                        stop();
                    } else {
                        if (mSpeechingListener != null) {
                            mSpeechingListener.nowRecordTime(msg.arg1);
                        }
                    }
                }
        }
    }

    private static final int HANDLE_FLAG = 33333721;// 用作massage的标示
    private int mRecordTime;// 录音起始时间
}