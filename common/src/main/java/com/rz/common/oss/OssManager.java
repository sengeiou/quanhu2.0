package com.rz.common.oss;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.AbortMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.ListObjectsRequest;
import com.alibaba.sdk.android.oss.model.ListObjectsResult;
import com.alibaba.sdk.android.oss.model.ListPartsRequest;
import com.alibaba.sdk.android.oss.model.ListPartsResult;
import com.alibaba.sdk.android.oss.model.PartETag;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.oss.model.UploadPartRequest;
import com.alibaba.sdk.android.oss.model.UploadPartResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;


public class OssManager {

    public static OSS oss;

    public static String VIDEO = "video/";
    public static String AUDIO = "audio/";
    //    public static String PIC = "picture/";
    public static String PIC = "pic/";
    public static String H5 = "h5/";
    public static String HEADIMAGE = "headImage/";

    public HashMap<String, List<PartETag>> datas = new HashMap<>();
    public boolean download = false;
    public boolean excuting = false;

    public static final String endpoint = "oss-cn-hangzhou.aliyuncs.com";
//    public static final String accessKeyId = "Oul8T3WUa6qjuLLm";
//    public static final String secretAccessKey = "wzZjbJwDpzxN9smCMUZIIHt3HpEeVs";
//    public static final String bucketName = "yryz";

    public static final String accessKeyId = "LTAIwQU3aCce2JVt";
    public static final String secretAccessKey = "N8qQ8nVlTlUIVmpMH8773VAuHoAkjI";
    //    public static String bucketName = "yryz-circle";
    public static String bucketName = "yryz-resources";

//    public static final String bucketNameCircle = "yrcircle";
//    public static final String bucketNameShine = "yrshine";
//    public static final String bucketNameProfile = "yrprofile";
//    public static final String bucketNameAds = "yrads";

    public static final String bucketNameCircle = "yryz-circle";
    public static final String bucketNameShine = "yryz-circle";
    public static final String bucketNameProfile = "yryz-circle";
    public static final String bucketNameAds = "yryz-circle";

    public static final String objectNameCircle = "opus/";
    public static final String objectNameShine = "shine/";
    public static final String objectNameProfile = "headImage/";
    public static final String objectNameAds = "ads/";

    public static String CDN = "https://cdn.yryz.com/";
//    public static String CDN = BuildConfig.cdn;

    public interface OssCallBack {
        void onSuccess(String url, String uploadId);

        void onFailure(String uploadId);

        void onProgress(String uploadId, float progress);
    }

    public static void initUpload(final Context context) {

        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
//        if (BuildConfig.isFlag == 2) {
//            OssManager.bucketName = "yryz-resources-mo";
//            OssManager.AUDIO = "audio-mo/";
//            OssManager.VIDEO = "video-mo/";
//            OssManager.H5 = "h5-mo/";
//            OssManager.HEADIMAGE = "headimg-mo/";
//            OssManager.PIC = "pic-mo/";
//        }
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, secretAccessKey);
//        OSSCredentialProvider credentialProvider = new OSSCustomSignerCredentialProvider() {
//            @Override
//            public String signContent(String content) {
//                // 您需要在这里依照OSS规定的签名算法，实现加签一串字符内容，并把得到的签名传拼接上AccessKeyId后返回
//                // 一般实现是，将字符内容post到您的业务服务器，然后返回签名
//                // 如果因为某种原因加签失败，描述error信息后，返回nil
//
//                // 以下是用本地算法进行的演示
//                String result = null;
//                try {
//                    result = "OSS " + accessKeyId + ":" + Base64.decode(StringUtils.HmacSHA1Encrypt(secretAccessKey, content), Base64.DEFAULT);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return result;
//            }
//        };


        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(20 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(20 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        oss = new OSSClient(context, endpoint, credentialProvider, conf);
    }

    private static void asyncCancelUpLoad(final String uploadId, String fileName, String targetDir, String objectDir) throws ClientException, ServiceException {
        Log.d("multipartUpload", "this is asyncCancelUpLoad uploadId" + uploadId + " fileName" + fileName + " targetDir" + targetDir);
        AbortMultipartUploadRequest abort = new AbortMultipartUploadRequest(OssManager.bucketName, targetDir + objectDir + fileName, uploadId);
        oss.asyncAbortMultipartUpload(abort, new OSSCompletedCallback<AbortMultipartUploadRequest, AbortMultipartUploadResult>() {
            @Override
            public void onSuccess(AbortMultipartUploadRequest abortMultipartUploadRequest, AbortMultipartUploadResult abortMultipartUploadResult) {
                Log.d("multipartUpload", "AbortUpload  onSuccess:  is" + uploadId);
            }

            @Override
            public void onFailure(AbortMultipartUploadRequest abortMultipartUploadRequest, ClientException clientExcepion, ServiceException serviceException) {
                Log.d("multipartUpload", "AbortUpload  onFailure:  is" + uploadId);
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    Log.d("multipartUpload", "AbortUpload  onFailure:  clientExcepion" + clientExcepion.getMessage());
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("multipartUpload", serviceException.getErrorCode());
                    Log.e("multipartUpload", serviceException.getRequestId());
                    Log.e("multipartUpload", serviceException.getHostId());
                    Log.e("multipartUpload", serviceException.getRawMessage());
                    Log.d("multipartUpload", "AbortUpload  onFailure:  serviceException" + serviceException.getMessage());
                }
            }
        });
//        ossBefore.abortMultipartUpload(abort); // 若无异常抛出说明删除成功
    }

    private static void syncCancelUpLoad(final String uploadId, String fileName, String targetDir) {
        Log.d("multipartUpload", "syncCancelUpLoad  uploadId:  is" + uploadId);
        AbortMultipartUploadRequest abort = new AbortMultipartUploadRequest(bucketName, targetDir + fileName, uploadId);
        try {
            oss.abortMultipartUpload(abort);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("multipartUpload", "syncCancelUpLoad  exception is" + e.getMessage());

        }
    }

    public void AbortUpload(final String uploadId, final String fileName, String targetDir, String objectDir) {
        try {
            Log.d("multipartUpload", "AbortUpload  uploadId:  is" + uploadId);
            asyncCancelUpLoad(uploadId, fileName, targetDir, objectDir);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("multipartUpload", "AbortUpload  error! Exception: " + e.getMessage());
        }


    }

    public static ListPartsResult ListPartsResult(String uploadId, String fileName, String targetDir, String objectDir) throws ClientException, ServiceException {
        ListPartsRequest listParts = new ListPartsRequest(OssManager.bucketName, targetDir + objectDir + fileName, uploadId);

        ListPartsResult result = oss.listParts(listParts);
        Log.d("listParts", "getNextPartNumberMarker: " + result.getNextPartNumberMarker());
        Log.d("listParts", "getBucketName: " + result.getBucketName());
        Log.d("listParts", "getPartNumberMarker: " + result.getPartNumberMarker());
        Log.d("listParts", "getMaxParts: " + result.getMaxParts());

//        for (int i = 0; i < result.getParts().size(); i++) {
//            Log.d("listParts", "partNum: " + result.getParts().get(i).getPartNumber());
//            Log.d("listParts", "partEtag: " + result.getParts().get(i).getETag());
//            Log.d("listParts", "lastModified: " + result.getParts().get(i).getLastModified());
//            Log.d("listParts", "partSize: " + result.getParts().get(i).getSize());
//        }
        return result;
    }

    public void asyncMultipartUpload(final String filePath, final String fileName, final String targetDir, final OssCallBack ossCallBack, final String resumeUploadId, final String objectDir) {

        new AsyncTask<Void, Float, String>() {

            private String uploadId;

            @Override
            protected String doInBackground(Void... params) {

                String result = null;
                int currentIndex = 1;
                List<PartETag> partETags = null; // 保存分片上传的结果
                InputStream input = null;
                try {
                    if (!TextUtils.isEmpty(resumeUploadId)) {
                        uploadId = resumeUploadId;//防止下面抛异常 覆盖了uploadId为null
                        //断点续传 优先计算上传的截止片段
                        ListPartsResult listPartsResult = ListPartsResult(resumeUploadId, fileName, targetDir, objectDir);
                        currentIndex = listPartsResult.getNextPartNumberMarker();
                    }

                    if (TextUtils.isEmpty(uploadId)) {
                        InitiateMultipartUploadRequest init = new InitiateMultipartUploadRequest(OssManager.bucketName, targetDir + objectDir + fileName);
                        InitiateMultipartUploadResult initResult = oss.initMultipartUpload(init);
                        uploadId = initResult.getUploadId();
                    }
                    Log.d("multipartUpload", "multipart upload 开始 uploadId is" + uploadId);
                    Log.d("multipartUpload", "multipart upload 开始 currentIndex is" + currentIndex);
                    // 上传分片编号，从1开始

                    File uploadFile = new File(filePath); // 需要分片上传的文件

                    if (!uploadFile.exists()) {
                        return null;
                    }
                    //分片大小需要大于100kb,数量少于10000,业务规定最大500MB,所以最多只有1000个断片;
                    long fileLength = uploadFile.length();

                    long partSize = 500 * 1024;

//                    long partSize = fileLength / 99;//分片的大小
//
//                    Log.d("multipartUpload", "multipart upload fileLength is" + fileLength);
//
//                    if (100 * 1024 > partSize) {//分片小于100kb
//
//                        Log.d("multipartUpload", "分片小于100kb" );
//                        partSize = 128 * 1024;
//                    }

                    Log.d("multipartUpload", "multipart upload partSize is" + partSize);

                    //获取分片个数
                    int partsNum = (int) (fileLength % partSize == 0 ? (fileLength / partSize) : (fileLength / partSize + 1));

                    Log.d("multipartUpload", "multipart upload partsNum is" + partsNum);

                    input = new FileInputStream(uploadFile);

                    long uploadedLength = (currentIndex - 1) * partSize;//计算出已上传的数量从1开始

                    if (uploadedLength != 0) {
                        com.litesuits.common.io.IOUtils.skip(input, uploadedLength);
                    }

                    if (datas.get(uploadId) == null || datas.get(uploadId).size() == 0) {
                        partETags = new ArrayList<PartETag>();
                    } else {
                        partETags = datas.get(uploadId);//本地获取已存储上传成功的断片
                    }

                    byte[] partData = null;

                    download = true;

                    while (uploadedLength < fileLength && download) {

                        int partLength = (int) Math.min(partSize, fileLength - uploadedLength);
                        partData = IOUtils.readStreamAsBytesArray(input, partLength); // 按照分片大小读取文件的一段内容
                        Log.d("multipartUpload", "multipart upload begin currentIndex is " + currentIndex);
                        UploadPartRequest uploadPart = new UploadPartRequest(OssManager.bucketName, targetDir + objectDir + fileName, uploadId, currentIndex);
                        uploadPart.setPartContent(partData); // 设置分片内容
                        UploadPartResult uploadPartResult = oss.uploadPart(uploadPart);
                        PartETag partETag = new PartETag(currentIndex, uploadPartResult.getETag());
                        if (partETags.size() >= currentIndex) {
                            partETags.set(currentIndex - 1, partETag);
                        } else {
                            partETags.add(partETag); // 保存分片上传成功后的结果
                        }
                        Log.d("multipartUpload", "multipart upload PartETag partNum is" + partETag.getPartNumber());

                        publishProgress(currentIndex * 100f / partsNum);

                        uploadedLength += partLength;
                        currentIndex++;
//                        if (currentIndex == 15) {
//                            download = false;
//                            asyncCancelUpLoad(uploadId, fileName, targetDir);
//                        }
                    }

                    if (download) {
                        CompleteMultipartUploadRequest complete = new CompleteMultipartUploadRequest(OssManager.bucketName, targetDir + objectDir + fileName, uploadId, partETags);
                        CompleteMultipartUploadResult completeResult = oss.completeMultipartUpload(complete);
                        complete.setCallbackParam(new HashMap<String, String>() {
                            {
                                //FIXME 添加服务端回调和参数传入
                                put("callbackUrl", "<server address>");
                                put("callbackBody", "<test>");
                            }
                        });

                        result = completeResult.getLocation();
                        Log.d("multipartUpload", "multipart upload success! Location: " + completeResult.getLocation());
                    }

                } catch (Exception e) {
                    result = null;
                    e.printStackTrace();
                    Log.d("multipartUpload", "multipart upload error! Exception: " + e.getMessage());
                } finally {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (partETags != null && partETags.size() != 0) {
                    datas.put(uploadId, partETags);
                }

                return result;
            }

            @Override
            protected void onProgressUpdate(Float... values) {
                super.onProgressUpdate(values);
                ossCallBack.onProgress(uploadId, values[0]);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                excuting = false;
                if (!TextUtils.isEmpty(result)) {
                    ossCallBack.onSuccess(result, uploadId);
                } else {
                    ossCallBack.onFailure(uploadId);
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                excuting = true;
            }
        }.executeOnExecutor(Executors.newCachedThreadPool());
    }

    private static void upLoad(String filePath, String fileName, String targetDir, OssCallBack ossCallBack) throws ClientException, ServiceException, IOException {

        String uploadId = null;
        InitiateMultipartUploadRequest init = new InitiateMultipartUploadRequest(bucketName, targetDir + fileName);
        InitiateMultipartUploadResult initResult = oss.initMultipartUpload(init);
        uploadId = initResult.getUploadId();

        int currentIndex = 1; // 上传分片编号，从1开始

        File uploadFile = new File(filePath); // 需要分片上传的文件

        if (!uploadFile.exists()) {
            ossCallBack.onFailure(uploadId);
            return;
        }
        //分片大小需要大于100kb,数量少于10000,业务规定最大500MB;
        long fileLength = uploadFile.length();

        long partSize = fileLength / 99;//分片的大小

        if (100 * 1024 > partSize) {//分片小于100kb
            partSize = 128 * 1024;
        }

        //获取分片个数
        int partsNum = (int) (fileLength % partSize == 0 ? (fileLength / partSize) : (fileLength / partSize + 1));

        InputStream input = new FileInputStream(uploadFile);

        long uploadedLength = 0;
        List<PartETag> partETags = new ArrayList<PartETag>(); // 保存分片上传的结果
        while (uploadedLength < fileLength) {

            int partLength = (int) Math.min(partSize, fileLength - uploadedLength);
            byte[] partData = IOUtils.readStreamAsBytesArray(input, partLength); // 按照分片大小读取文件的一段内容

            UploadPartRequest uploadPart = new UploadPartRequest(bucketName, targetDir + fileName, uploadId, currentIndex);
            uploadPart.setPartContent(partData); // 设置分片内容
            UploadPartResult uploadPartResult = oss.uploadPart(uploadPart);
            partETags.add(new PartETag(currentIndex, uploadPartResult.getETag())); // 保存分片上传成功后的结果

            ossCallBack.onProgress(uploadId, currentIndex / partsNum * 100);//抛出分片完成百分比

            uploadedLength += partLength;
            currentIndex++;
        }

        CompleteMultipartUploadRequest complete = new CompleteMultipartUploadRequest(bucketName, targetDir + fileName, uploadId, partETags);
        CompleteMultipartUploadResult completeResult = oss.completeMultipartUpload(complete);
        complete.setCallbackParam(new HashMap<String, String>() {
            {
                //FIXME 添加服务端回调和参数传入
                put("callbackUrl", "<server address>");
                put("callbackBody", "<test>");
            }
        });

        ossCallBack.onSuccess(completeResult.getLocation(), uploadId);

        Log.d("multipartUpload", "multipart upload success! Location: " + completeResult.getLocation());

    }

//    delimiter	用于对Object名字进行分组的字符。所有名字包含指定的前缀且第一次出现delimiter字符之间的object作为一组元素: CommonPrefixes。
//    marker	设定结果从marker之后按字母排序的第一个开始返回。
//    maxkeys	限定此次返回object的最大数，如果不设定，默认为100，maxkeys取值不能大于1000。
//    prefix	限定返回的object key必须以prefix作为前缀。注意使用prefix查询时，返回的key中仍会包含prefix。

    public static void getAllBucketFile() {
        ListObjectsRequest listObjects = new ListObjectsRequest(bucketName);
        // 设定前缀
        listObjects.setPrefix("file");
        // 设置成功、失败回调，发送异步罗列请求
        OSSAsyncTask task = oss.asyncListObjects(listObjects, new OSSCompletedCallback<ListObjectsRequest, ListObjectsResult>() {
            @Override
            public void onSuccess(ListObjectsRequest request, ListObjectsResult result) {
                Log.d("AyncListObjects", "Success!");
                for (int i = 0; i < result.getObjectSummaries().size(); i++) {
                    Log.d("AyncListObjects", "object: " + result.getObjectSummaries().get(i).getKey() + " "
                            + result.getObjectSummaries().get(i).getETag() + " "
                            + result.getObjectSummaries().get(i).getLastModified());
                }
            }

            @Override
            public void onFailure(ListObjectsRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
        task.waitUntilFinished();
    }


    /**
     * 检查oss是否存在这个文件
     *
     * @return
     */
    public static boolean checkFileExist() {
        boolean result = false;
        try {
            if (oss.doesObjectExist("<bucketName>", "<objectKey>")) {
                Log.d("doesObjectExist", "object exist.");
                result = true;
            } else {
                Log.d("doesObjectExist", "object does not exist.");
            }
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("RequestId", e.getRequestId());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }
        return result;
    }


    public static void deleteFile(String filename, String targetDir) {
        // 创建删除请求
        DeleteObjectRequest delete = new DeleteObjectRequest(bucketName, targetDir + filename);
        // 异步删除
        OSSAsyncTask deleteTask = oss.asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
            @Override
            public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
                Log.d("asyncCopyAndDelObject", "success!");
            }

            @Override
            public void onFailure(DeleteObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }

        });
    }

    public static OSSAsyncTask uploadFile(String fileSavePath, String objectKey, String objectDir, OSSCompletedCallback<PutObjectRequest, PutObjectResult> ossCompletedCallback) {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(OssManager.bucketName, objectKey, fileSavePath);

        OSSAsyncTask task = oss.asyncPutObject(put, ossCompletedCallback);
        return task;

        // task.cancel(); // 可以取消任务
        // task.waitUntilFinished(); // 可以等待任务完成
    }

    public static String syncUploadFile(String fileSavePath, String objectKey, String objectDir) throws ClientException, ServiceException {
        PutObjectRequest put = new PutObjectRequest(OssManager.bucketName, objectKey, fileSavePath);
        PutObjectResult putObjectResult = oss.putObject(put);
        Log.d("uoload", "putObjectResult " + putObjectResult.getRequestId());
        Log.d("uoload", "putObjectResult " + putObjectResult.getETag());
        Log.d("uoload", "putObjectResult " + putObjectResult.getServerCallbackReturnBody());
        if (!TextUtils.isEmpty(putObjectResult.getRequestId()) && !TextUtils.isEmpty(putObjectResult.getETag())) {
//            String OSS_HOST = "http://" + OssManager.bucketName + "." + OssManager.endpoint + "/";
            String OSS_HOST = CDN;
            return OSS_HOST + objectKey;
        } else {
            return "";
        }
    }
}
