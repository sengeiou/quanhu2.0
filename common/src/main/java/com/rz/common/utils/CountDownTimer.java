package com.rz.common.utils;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by JS01 on 2016/6/14.
 */
public class CountDownTimer {

    private long countMillis;
    private long periodMillis;
    private int times = 0;
    private TimerTask task;
    private Timer timer;

    public CountDownTimer(long periodMillis, final long countMillis) {
        if (periodMillis > countMillis) {
            periodMillis = countMillis;
        }
        this.periodMillis = periodMillis;
        this.countMillis = countMillis;
        timer = new Timer();
        times = (int) (countMillis / periodMillis);
    }

    public void start(final CountDownListener listener) {
        task = new TimerTask() {
            @Override
            public void run() {
                times--;
                if (times < 0) {
                    CountDownTimer.this.cancel();
                    if (listener != null) {
                        listener.onFinish();
                    }
                } else {
                    if (listener != null) {
                        listener.onTick(times * periodMillis);
                    }
                }
            }
        };
        timer.schedule(task, periodMillis, periodMillis);
        if (listener != null) {
            listener.onStart(countMillis);
        }
    }

    public void cancel() {
        task.cancel();
        timer.cancel();
    }

    public interface CountDownListener {

        void onStart(long countTimeMillis);

        void onTick(long leftTimeMillis);

        void onFinish();
    }

    private static long lastClickTime;

    /**
     * 判断是否是快速点击
     *
     * @return
     */
    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastClick(long timeMillis) {
        long time = System.currentTimeMillis();
        Log.d("yeying", "time " + time);
        Log.d("yeying", "lastClickTime " + lastClickTime);
        if (time - lastClickTime < timeMillis) {
            return true;
        }
        Log.d("yeying", "time " + time);
        lastClickTime = time;
        return false;
    }
}
