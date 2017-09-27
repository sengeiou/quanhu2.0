package com.rz.circled.ui.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.rz.circled.R;
import com.rz.circled.application.QHApplication;

/**
 * Created by Administrator on 2017/5/18 0018.
 */

public class LoadResActivity extends Activity {
    private ImageView mImgBg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
        setContentView(R.layout.activity_splash);
        mImgBg = (ImageView) findViewById(R.id.id_start_page_bg_img);
        mImgBg.setImageResource(R.drawable.page_bg_qq_new);
        Log.e("loadRes", "onCreate");
        new LoadDexTask().execute();
    }

    class LoadDexTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Log.e("loadRes", "doInBackground start");
                MultiDex.install(getApplication());
                ((QHApplication) getApplication()).installFinish(getApplication());
                Log.e("loadRes", "doInBackground");
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.e("loadRes", "onPostExecute");
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onBackPressed() {
        Log.e("loadRes", "onBackPressed");
    }
}