package com.rz.circled.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.permission.AfterPermissionGranted;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.ImageUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.toasty.Toasty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Administrator on 2016/12/5 0005 17:29
 * 功能：分享测试-分享到随手晒
 * 说明：
 */
public class TestAty extends BaseActivity {

    private boolean isFinish = false;

    //0纯文 1图片 2视频 3网页
    private int mType = 0;

    //存储图片的路径
    ArrayList<String> mList = new ArrayList<>();

    //存储视频第一帧图片
    private String mSaveFirstVideoPic;

    //存储视频地址
    private String mSaveVideoPath;

    private String invokeId, title, url, imageUrl;

    @Override
    protected boolean needSwipeBack() {
        return false;
    }

    @Override
    protected boolean needStatusBarTint() {
        return false;
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_test, null);
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (null == intent)
            return;
        parseIntent(intent);
        String action = intent.getAction();
        String type = intent.getType();
        if (TextUtils.equals(action, Intent.ACTION_SEND) && null != type) {
            Log.d("zxw", "type==" + type + "**url==" + url + "**title==" + title);
            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(url)) {
                mType = 3;
//                goToPublish(mType);
                return;
            }
            if (TextUtils.equals("text/plain", type)) {
                Toasty.info(mContext, getString(R.string.buzhifu)).show();
                finish();
            } else if (type.startsWith("image/")) {
                mType = 1;
                doCamera();
            } else if (type.startsWith("video/")) {
                mType = 2;
                doCamera();
            } else {
                Log.d("huang", "44444444444444");
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFinish) {
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.ShareAty.share_request_code || requestCode == IntentCode.Login.LOGIN_REQUEST_CODE) {
            if (resultCode == IntentCode.ShareAty.share_result_code || resultCode == IntentCode.Login.LOGIN_RESULT_CODE || resultCode == IntentCode.Contacts.Contacts_RESULT_CODE) {
                finish();
            }
        }
    }

    /**
     * 处理登录返回
     */
    @Subscribe
    public void onChangeList(BaseEvent event) {
//        if (TextUtils.equals(LOGIN_IN_SUCCESS, event.info)) {
//            goToPublish(mType);
//        }
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void doCamera() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (mType == 1 || mType == 3) {
                handleSendSingleImage();
            } else if (mType == 2) {
                handleSendVideo();
            } else {
                finish();
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.sd_card_permissions), RC_CAMERA_PERM, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case RC_CAMERA_PERM:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mType == 1) {
                        handleSendSingleImage();
                    } else if (mType == 2) {
                        handleSendVideo();
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
    }

    /**
     * 返回获取的url和tittle信息
     *
     * @param intent
     * @return
     */
    private boolean parseIntent(Intent intent) {
        String text = intent.getExtras().getString(Intent.EXTRA_TEXT);
        String subject = intent.getExtras().getString(Intent.EXTRA_SUBJECT);
        Log.d("huang", text + "{{}{}{}{}{}{}{}{}{}{}{}{}{}" + subject);
        invokeId = intent.getExtras().getString(Intent.EXTRA_UID);
        imageUrl = intent.getExtras().getString(Intent.EXTRA_SHORTCUT_ICON);
        if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(subject)) {
            url = getHttpUrlByText(text);
            title = subject;
        } else if (!TextUtils.isEmpty(text) && TextUtils.isEmpty(subject)) {
            url = getHttpUrlByText(text);
            title = text.replace(url, "");
            if (TextUtils.isEmpty(title)) {
                title = getString(R.string.qh_zuop);
            }
        } else {
            url = "";
            title = "";
            return false;
        }
        return true;
    }

    private String getHttpUrlByText(String text) {
        Spannable.Factory mSpannableFactory = Spannable.Factory.getInstance();
        Spannable spannable = mSpannableFactory.newSpannable(text);
        if (Linkify.addLinks(spannable, Linkify.WEB_URLS)) {
            int end = text.length();
            URLSpan urls[] = spannable.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();
            if (null != urls && urls.length > 0) {
                return urls[0].getURL();
            }
        }
        return "";
    }

    /**
     * 处理接收到的单个图片
     */
    private void handleSendSingleImage() {
        mList.clear();
        Uri imageUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        String mPicPath = ImageUtils.getRealPathFromUri(this, imageUri);
        Log.d("huang", mPicPath + "-------------");
        if (!TextUtils.isEmpty(mPicPath)) {
            mList.add(mPicPath);
//            goToPublish(mType);
        } else {
            Toasty.info(mContext, getString(R.string.get_path_fail)).show();
            finish();
        }
    }

    /**
     * 处理接收到的视频
     */
    private void handleSendVideo() {
        Uri videoUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        mSaveVideoPath = ImageUtils.getRealPathFromUri(this, videoUri);
        if (!StringUtils.isEmpty(mSaveVideoPath)) {
            //获取视频第一帧图片
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            File file = new File(mSaveVideoPath);
            if (file.exists()) {
                mmr.setDataSource(file.getAbsolutePath());
                // 播放时长单位为毫秒
                String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                if (StringUtils.isEmpty(duration)) {
                    Toasty.info(mContext, getString(R.string.video_error_un_share)).show();
                    finish();
                    return;
                }
                if (Double.parseDouble(duration) > 15000) {
                    Toasty.info(mContext, getString(R.string.video_max_length)).show();
                    finish();
                    return;
                }
                Bitmap bitmap = mmr.getFrameAtTime();
                if (null != bitmap) {
                    File firstPicPath1 = ImageUtils.compressBmpToFile(aty, bitmap, 10);
                    mSaveFirstVideoPic = firstPicPath1.toString();
                }
            } else {
                // TODO: 2017/9/14 之后切换默认图片
                Bitmap defaultPic = ImageUtils.drawableToBitmap(ContextCompat.getDrawable(mContext, R.mipmap.icon_data_empty));
                if (null != defaultPic) {
                    File firstPicPath2 = ImageUtils.compressBmpToFile(aty, defaultPic, 10);
                    mSaveFirstVideoPic = firstPicPath2.toString();
                }
            }
//            goToPublish(mType);
        } else {
            Toasty.info(mContext, getString(R.string.video_path_error)).show();
            finish();
        }
    }

//    private void goToPublish(int type) {
//        if (isLogin()) {
//            if (type == 1) {
//                //图片
//                Intent i = new Intent(this, PublishAty2.class);
//                i.putExtra("picList", mList);
//                startActivityForResult(i, IntentCode.ShareAty.share_request_code);
//                isFinish = true;
//            } else if (type == 2) {
//                //视频
//                Intent target = new Intent(this, PublishAty2.class);
//                target.putExtra(IntentKey.KEY_PATH, mSaveVideoPath);
//                target.putExtra(IntentKey.KEY_IMAGE, mSaveFirstVideoPic);
//                startActivityForResult(target, IntentCode.ShareAty.share_request_code);
//                isFinish = true;
//            } else if (type == 3) {
//                //网页分享
//                Intent intentTxt = new Intent(aty, PublishAty2.class);
//                intentTxt.putExtra("isWenzi", true);
//                intentTxt.putExtra(IntentKey.EXTRA_TITLE, title);
//                intentTxt.putExtra(IntentKey.EXTRA_URL, url);
//                if (!TextUtils.isEmpty(invokeId))
//                    intentTxt.putExtra(IntentKey.EXTRA_ID, invokeId);
//                if (!TextUtils.isEmpty(imageUrl))
//                    intentTxt.putExtra(IntentKey.EXTRA_IMAGE, imageUrl);
//                startActivityForResult(intentTxt, IntentCode.ShareAty.share_request_code);
//                isFinish = true;
//            }
//        }
//    }

    //    /**
//     * 处理接收到的多张图片
//     *
//     * @param intent
//     */
//    private void handleSendMultipleImage(Intent intent) {
//        mList.clear();
//        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
//        if (null != imageUris && !imageUris.isEmpty()) {
//            for (Uri uri : imageUris) {
//                String mPicPath = getRealFilePath(uri);
//                String path = mPicPath.toLowerCase().trim();
//                if (!path.endsWith("3gp") && !path.endsWith("mp4") && !path.endsWith("avi") && !path.endsWith("wmv") && !path.endsWith("mkv") && !path.endsWith("flv") && !path.endsWith("rmvb")) {
//                mList.add(mPicPath);
//                }
//                if (!StringUtils.isEmpty(mPicPath)) {
//                    mList.add(mPicPath);
//                }
//            }
//            if (Session.getUserIsLogin()) {
//                Intent i = new Intent(this, PublishAty2.class);
//                i.putExtra("picList", mList);
//                startActivityForResult(i, IntentCode.ShareAty.share_request_code);
//                isFinish = true;
//            } else {
//                Intent login = new Intent(aty, LoginAty.class);
//                startActivityForResult(login, IntentCode.Login.LOGIN_REQUEST_CODE);
//            }
//        } else {
//            ToastUtil.showToast("获取失败");
//            finish();
//        }
//    }

    /**
     * 用户是否登录
     */
    public boolean isLogin() {
        if (Session.getUserIsLogin()) {
            return true;
        } else {
            Intent login = new Intent(aty, LoginActivity.class);
            startActivityForResult(login, IntentCode.Login.LOGIN_REQUEST_CODE);
//            showLoginDialog();
            return false;
        }
    }
}
