package com.rz.circled.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.rz.circled.R;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/3/8 0008.
 */

public class SocializationShareAty extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    // constant
    private final static String EXTRA_INVOKE_ID = "EXTRA_INVOKE_ID";
    private final static String EXTRA_APPID = "EXTRA_APPID";
    private final static String EXTRA_TITLE = "EXTRA_TITLE";
    private final static String EXTRA_DESC = "EXTRA_DESC";
    private final static String EXTRA_URL = "EXTRA_URL";
    private final static String EXTRA_IMAGE = "EXTRA_IMAGE";
    private final static String EXTRA_PLAT = "EXTRA_PLAT";

    private ShareAction shareAction;

    private String title;
    private String desc;
    private String url;
    private String imageUrl;
    private String platform;
    private String invokeId;
    private String appId;

    private boolean finishTag = false;

    public static void start(Activity activity, String invokeId, String appId, String title, String desc, String image, String url, String plat) {
        Intent intent = new Intent();
        intent.setClass(activity, SocializationShareAty.class);
        intent.putExtra(EXTRA_INVOKE_ID, invokeId);
        intent.putExtra(EXTRA_APPID, appId);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DESC, desc);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_IMAGE, image);
        intent.putExtra(EXTRA_PLAT, plat);
        activity.startActivity(intent);
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return super.loadView(inflater);
    }

    @Override
    public void initView() {
        invokeId = getIntent().getStringExtra(EXTRA_INVOKE_ID);
        appId = getIntent().getStringExtra(EXTRA_APPID);
        title = getIntent().getStringExtra(EXTRA_TITLE);
        desc = getIntent().getStringExtra(EXTRA_DESC);
        url = getIntent().getStringExtra(EXTRA_URL);
        imageUrl = getIntent().getStringExtra(EXTRA_IMAGE);
        platform = getIntent().getStringExtra(EXTRA_PLAT);
    }

    @Override
    public void initData() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            UMWeb web = new UMWeb(url);
            web.setTitle(title);//标题
            web.setDescription(desc);//描述

            if (!TextUtils.isEmpty(imageUrl)) {
                UMImage image = new UMImage(aty, imageUrl);
                UMImage thumb = new UMImage(this, R.mipmap.icon_share);
                image.setThumb(thumb);
                web.setThumb(image);  //缩略图
            } else {
                UMImage image = new UMImage(aty, R.mipmap.icon_share);
                UMImage thumb = new UMImage(this, R.mipmap.icon_share);
                image.setThumb(thumb);
                web.setThumb(image);  //缩略图
            }

            shareAction = new ShareAction(aty)
                    .withMedia(web)
                    .setCallback(umShareListener);

            if (!TextUtils.isEmpty(platform)) {
                switch (platform) {
                    case "WeChat":
                        if (UMShareAPI.get(aty).isInstall(aty, SHARE_MEDIA.WEIXIN)) {
                            shareAction.setPlatform(SHARE_MEDIA.WEIXIN).share();
                        } else {
                            SVProgressHUD.showInfoWithStatus(aty, "沒有安装微信客户端");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 1000);

                        }
                        break;
                    case "WeChatLine":
                        if (UMShareAPI.get(aty).isInstall(aty, SHARE_MEDIA.WEIXIN)) {
                            shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
                        } else {
                            SVProgressHUD.showInfoWithStatus(aty, "沒有安装微信客户端");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 1000);
                        }
                        break;
                    case "QQ":
                        if (UMShareAPI.get(aty).isInstall(aty, SHARE_MEDIA.QQ)) {
                            shareAction.setPlatform(SHARE_MEDIA.QQ).share();
                        } else {
                            SVProgressHUD.showInfoWithStatus(aty, "沒有安装QQ客户端");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 1000);
                        }
                        break;
                    case "QQZone":
                        if (UMShareAPI.get(aty).isInstall(aty, SHARE_MEDIA.QQ)) {
                            shareAction.setPlatform(SHARE_MEDIA.QZONE).share();
                        } else {
                            SVProgressHUD.showInfoWithStatus(aty, "沒有安装QQ客户端");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 1000);
                        }
                        break;
                    case "Sina":
                        ImageRequest imageRequest = ImageRequestBuilder
                                .newBuilderWithSource(Uri.parse(imageUrl))
                                .setProgressiveRenderingEnabled(true)
                                .build();

                        ImagePipeline imagePipeline = Fresco.getImagePipeline();
                        DataSource<CloseableReference<CloseableImage>>
                                dataSource = imagePipeline.fetchDecodedImage(imageRequest, aty);

                        dataSource.subscribe(new BaseBitmapDataSubscriber() {
                                                 @Override
                                                 public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                                     // You can use the bitmap in only limited ways
                                                     // No need to do any cleanup.
                                                     UMImage image = new UMImage(aty, bitmap);
                                                     UMImage thumb = new UMImage(aty, R.mipmap.icon_share);
                                                     image.setThumb(thumb); //缩略图
                                                     image.setTitle(title + url);
                                                     image.setDescription(desc);
                                                     shareAction.withMedia(image);
                                                     shareAction.withText(title + url);
                                                     shareAction.setPlatform(SHARE_MEDIA.SINA).share();
                                                 }

                                                 @Override
                                                 public void onFailureImpl(DataSource dataSource) {
                                                     // No cleanup required here.
                                                     UMImage image = new UMImage(aty, R.mipmap.icon_share);
                                                     UMImage thumb = new UMImage(aty, R.mipmap.icon_share);
                                                     image.setThumb(thumb); //缩略图
                                                     image.setTitle(title + url);
                                                     image.setDescription(desc);
                                                     shareAction.withMedia(image);
                                                     shareAction.withText(title + url);
                                                     shareAction.setPlatform(SHARE_MEDIA.SINA).share();
                                                 }
                                             },
                                CallerThreadExecutor.getInstance());
                        break;
                    default:
                        break;
                }
            } else {

            }
        } else {
            EasyPermissions.requestPermissions(this, "圈乎调用sd卡", 12, perms);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(aty).onActivityResult(requestCode, resultCode, data);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("zxw", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(aty, "收藏成功~", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(aty, "分享成功~", Toast.LENGTH_SHORT).show();
            }
            if (platform.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
                zhugeTrack();
            }
            JsEvent.callJsEvent(null, true);
            finish();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Log.d("zxw", "onError: ");
            Toast.makeText(aty, "分享失败~", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
            JsEvent.callJsEvent(null, false);
            finish();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Log.d("zxw", "cancel:" + platform.toString());
            JsEvent.callJsEvent(null, BaseParamsObject.RESULT_CODE_CANCEL);
            finish();
        }
    };

    private void zhugeTrack() {
        //定义与事件相关的属性信息
        JSONObject eventObject = new JSONObject();
        //记录事件
        ZhugeSDK.getInstance().track(getApplicationContext(), "发到朋友圈",
                eventObject);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        initData();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (finishTag) {
            finishTag = false;
            finish();
        }
        finishTag = true;
    }

    @Override
    public void refreshPage() {

    }
}
