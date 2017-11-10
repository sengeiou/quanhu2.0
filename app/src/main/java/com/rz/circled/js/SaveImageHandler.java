package com.rz.circled.js;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.rz.circled.R;
import com.rz.common.widget.toasty.Toasty;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Gsm on 2017/8/11.
 */
public class SaveImageHandler extends ServerHandler {

    private static final String SAVE_PIC_PATH = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";//保存到SD卡
    private static final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/quanhu/Image";//保存的确切位置

    public SaveImageHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "saveImage";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Gson gson = new Gson();
        String dataJson = gson.toJson(paramObj.getData());
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(dataJson);
            String name = jsonObject.getString("name");
            String data = jsonObject.getString("data");
            if (data.startsWith("http://") || data.startsWith("https://")) {
                Glide.with(mActivity).load(data).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        try {
                            saveFile(bitmap, System.currentTimeMillis() + ".jpg", "");
                            Toasty.info(mActivity, mActivity.getString(R.string.save_success)).show();
                            JsEvent.callJsEvent(null, true);
                        } catch (IOException e) {
                            Toasty.info(mActivity, mActivity.getString(R.string.save_fail)).show();
                            e.printStackTrace();
                            Log.e("zxw", e.getMessage(), e);
                            JsEvent.callJsEvent(null, false);
                        }
                    }
                });
            } else {
                if (data.split(",").length == 2) {
                    data = data.split(",")[1];
                }
                byte[] bytes = Base64.decode(data, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                saveFile(bitmap, System.currentTimeMillis() + ".jpg", "");
                Toasty.info(mActivity, mActivity.getString(R.string.save_success)).show();
                JsEvent.callJsEvent(null, true);
            }
        } catch (JSONException | IOException e) {
            Toasty.info(mActivity, mActivity.getString(R.string.save_fail)).show();
            e.printStackTrace();
            Log.e("zxw", e.getMessage(), e);
            JsEvent.callJsEvent(null, false);
        }
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        return new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParams, BaseParamsObject baseParamsObject) {

            }
        };
    }

    @Override
    public boolean isUi() {
        return false;
    }

    private void saveFile(Bitmap bm, String fileName, String path) throws IOException {
        String subForder = SAVE_REAL_PATH + path;
        File foder = new File(subForder);
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(subForder, fileName);
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    Log.e("zxw", e.getMessage(), e);
                }
            }
        }
        notifyPicture(myCaptureFile, fileName);
    }

    private void notifyPicture(File file, String fileName) {
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        mActivity.sendBroadcast(intent);
    }

}
