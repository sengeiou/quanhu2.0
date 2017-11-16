package com.rz.common.oss;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.litesuits.common.utils.FileUtil;
import com.rz.common.utils.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadPicManager {


    public static String OSS_HOST;

    public ConcurrentHashMap<String, OSSAsyncTask> concurrentHashMap = new ConcurrentHashMap<>();

    private OnUploadCallback mOnUploadCallback;

    private List<UploadInfo> resultList;

    private Context mContext;
    List<UploadInfo> uploadInfos;

    public static class UploadInfo {
        public Object tag;
        public String fileSavePath;

        @Override
        public String toString() {
            return "UploadInfo{" +
                    "tag=" + tag +
                    ", fileSavePath='" + fileSavePath + '\'' +
                    '}';
        }
    }

    public interface OnUploadCallback {
        void onResult(boolean result, List<UploadInfo> resultList);
    }

    public List<String> syncUploads(Context context, List<String> uploadStrs, String objectDir) throws ClientException, ServiceException, IOException {
        this.mContext = context;
        OSS_HOST = "http://" + OssManager.bucketName + "." + OssManager.endpoint + "/";
        concurrentHashMap.clear();
        resultList.clear();
        String uuid = null;
        String objectKey = null;
        List<String> uploadResultList = new ArrayList<>();
        for (int i = 0; i < uploadStrs.size(); i++) {
            uuid = UUID.randomUUID().toString();
            if (uploadStrs.get(i).lastIndexOf(".") != -1) {
                objectKey = OssManager.PIC + objectDir + uuid + uploadStrs.get(i).substring(uploadStrs.get(i).lastIndexOf("."));
            } else {
                objectKey = OssManager.PIC + objectDir + uuid;
            }

            if (!isNetUrl(uploadStrs.get(i))) {
                //当为本地地址时才送oss服务器上传
                String result = OssManager.syncUploadFile(uploadStrs.get(i), objectKey, objectDir);
                FileUtil.deleteFile(new File(uploadStrs.get(i)));
                if (!TextUtils.isEmpty(result)) {
//                    uploadResultList.add(OSS_HOST + objectKey);
                    uploadResultList.add(OssManager.CDN + objectKey);
                } else {
                    return null;
                }
            } else {
                //网络地址直接作为结果保存
                uploadResultList.add(uploadStrs.get(i));
            }
        }
        return uploadResultList;
    }

    public static boolean isNetUrl(String s) {
        try {
            return Patterns.WEB_URL.matcher(s).matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void uploads(Context context, List<UploadInfo> uploadInfos, String objectDir) {
        this.mContext = context;
        this.uploadInfos = uploadInfos;

        OSS_HOST = "http://" + OssManager.bucketName + "." + OssManager.endpoint + "/";

        concurrentHashMap.clear();
        resultList.clear();
        String uuid = null;
        String objectKey = null;
        UploadInfo resultUpload = null;
        for (int i = 0; i < uploadInfos.size(); i++) {
            resultUpload = new UploadInfo();
            resultUpload.tag = uploadInfos.get(i).tag;
            uuid = UUID.randomUUID().toString();
            if (uploadInfos.get(i).fileSavePath.lastIndexOf(".") != -1) {
                objectKey = OssManager.PIC + objectDir + uuid + uploadInfos.get(i).fileSavePath.substring(uploadInfos.get(i).fileSavePath.lastIndexOf("."));
            } else {
                objectKey = OssManager.PIC + objectDir + uuid;
            }
            if (!isNetUrl(uploadInfos.get(i).fileSavePath)) {
                //当为本地地址时才送oss服务器上传
                OSSAsyncTask task = OssManager.uploadFile(uploadInfos.get(i).fileSavePath, objectKey, objectDir, new CustomCallBack(uuid));
                concurrentHashMap.put(uuid, task);
//                resultUpload.fileSavePath = OSS_HOST + objectKey;
                resultUpload.fileSavePath = OssManager.CDN + objectKey;
            } else {
                //网络地址直接作为结果保存
                resultUpload.fileSavePath = uploadInfos.get(i).fileSavePath;
            }
            resultList.add(resultUpload);
        }
        checkResult();
        Log.d("test", "this is uploadManager " + resultList.toString());
    }

    public void compressAndUploads(Context context, List<UploadInfo> uploadInfos, String objectDir) {
        compressImage(context, uploadInfos, objectDir);
    }

    public List<String> compressAndUploadsSync(Context context, List<String> uploadInfos, String objectDir) throws ClientException, ServiceException, IOException {
        List<String> list = new ArrayList<>();
        if (uploadInfos != null) {
            for (int i = 0; i < uploadInfos.size(); i++) {
                Bitmap result = ImageUtils.getImage(uploadInfos.get(i));
                File file = ImageUtils.compressBmpToFile(context, result, OssManager.objectNameProfile.equals(objectDir) ? 10 : 200);
                if (file != null && file.exists()) {
                    String str = file.getAbsolutePath();
                    list.add(str);
                } else {
                    return null;
                }
            }
            return syncUploads(context, list, objectDir);
        }
        return null;
    }

    public List<ImageInfo> compressAndUploadsSyncImageInfo(Context context, List<String> uploadInfos, String objectDir) throws ClientException, ServiceException, IOException {
        List<String> list = new ArrayList<>();
        List<ImageInfo> imageInfos = new ArrayList<>();
        if (uploadInfos != null) {
            for (int i = 0; i < uploadInfos.size(); i++) {
                ImageInfo imageInfo = new ImageInfo();
                Bitmap result = ImageUtils.getImage(uploadInfos.get(i));
                imageInfo.height = result.getHeight();
                imageInfo.width = result.getWidth();
                imageInfos.add(imageInfo);
                File file = ImageUtils.compressBmpToFile(context, result, OssManager.objectNameProfile.equals(objectDir) ? 10 : 200);
                if (file != null && file.exists()) {
                    String str = file.getAbsolutePath();
                    list.add(str);
                } else {
                    return null;
                }
            }
            List<String> urls = syncUploads(context, list, objectDir);
            if (urls != null) {
                for (int i = 0; i < urls.size(); i++) {
                    imageInfos.get(i).url = urls.get(i);
                }
            }
            return imageInfos;
        }
        return null;
    }

    public class ImageInfo {
        public String url;
        public int width;
        public int height;

        @Override
        public String toString() {
            return "ImageInfo{" +
                    "url='" + url + '\'' +
                    ", width=" + width +
                    ", height=" + height +
                    '}';
        }
    }

    private void compressImage(final Context context, List<UploadInfo> resultList, final String objectDir) {
        new AsyncTask<List<UploadInfo>, Void, List<UploadInfo>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<UploadInfo> doInBackground(List<UploadInfo>... params) {
                List<UploadInfo> list = null;
                try {
                    list = params[0];
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            if (!TextUtils.isEmpty(list.get(i).fileSavePath) && !isNetUrl(list.get(i).fileSavePath)) {
                                Bitmap result = ImageUtils.getImage(list.get(i).fileSavePath);
                                File file = ImageUtils.compressBmpToFile(context, result, OssManager.objectNameProfile.equals(objectDir) ? 10 : 200);
                                if (file != null && file.exists()) {
                                    list.get(i).fileSavePath = file.getAbsolutePath();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("zxw", "Exception: " + Thread.currentThread().getName());
                    e.printStackTrace();
                    return null;
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<UploadInfo> s) {
                super.onPostExecute(s);
                Log.e("zxw", "onPostExecute: " + Thread.currentThread().getName());
                if (s != null) {
                    uploads(context, s, objectDir);
                } else {
                    if (mOnUploadCallback != null) {
                        mOnUploadCallback.onResult(false, null);
                    }
                }
            }
        }.execute(resultList);
    }

    public UploadPicManager(OnUploadCallback onUploadCallback) {
        mOnUploadCallback = onUploadCallback;
        resultList = new ArrayList<UploadInfo>();
    }

    public UploadPicManager() {
        resultList = new ArrayList<UploadInfo>();
    }

    public static ExecutorService executorService = Executors.newFixedThreadPool(9);

    public class CustomCallBack implements OSSCompletedCallback<PutObjectRequest, PutObjectResult> {

        private String uuid;


        public CustomCallBack(String uuid) {
            this.uuid = uuid;
        }


        @Override
        public void onSuccess(final PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
            Log.d("test", "uploadSuccess uuid is  -- " + uuid);
            Log.d("test", "uploadSuccess getUploadFilePath " + putObjectRequest.getUploadFilePath());
            concurrentHashMap.remove(uuid);
            checkResult();
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        FileUtil.deleteFile(new File(putObjectRequest.getUploadFilePath()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.executeOnExecutor(executorService);
        }

        @Override
        public void onFailure(final PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
            Log.d("test", "uploadFailure uuid is  -- " + uuid);
            if (e != null) {
                Log.d("test", "uploadFailure  -- " + e.getMessage());
            }
            if (e1 != null) {
                Log.d("test", "uploadFailure  -- " + e1.getMessage());
            }
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        FileUtil.deleteFile(new File(putObjectRequest.getUploadFilePath()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.executeOnExecutor(executorService);
            Log.d("test", "uploadFailure  -- notifyError");
            notifyError();
            cancelAll();
        }

    }

    private void checkResult() {
        if (concurrentHashMap.size() == 0) {
            if (mOnUploadCallback != null) {
                mOnUploadCallback.onResult(true, resultList);
            }
        }
    }

    private void cancelAll() {
        java.util.Iterator it = concurrentHashMap.entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry<String, OSSAsyncTask> entry = (java.util.Map.Entry) it.next();
            entry.getValue().cancel();
            it.remove();
        }
    }

    private void notifyError() {
        if (mOnUploadCallback != null) {
            mOnUploadCallback.onResult(false, null);
        }
    }

}
