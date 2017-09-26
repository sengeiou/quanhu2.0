package com.rz.circled.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.GlideRoundImage;
import com.rz.circled.widget.PopupView;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentKey;
import com.rz.common.oss.OssManager;
import com.rz.common.oss.UploadPicManager;
import com.rz.common.permission.AppSettingsDialog;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.AudioUtils;
import com.rz.common.utils.CacheUtils;
import com.rz.common.utils.Protect;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.rz.common.utils.SystemUtils.trackUser;

/**
 * Created by xiayumo on 16/8/15.
 * 个人详情
 */
public class PersonInfoAty extends BaseActivity implements View.OnClickListener, PopupView.OnItemPopupClick, UploadPicManager.OnUploadCallback {

    @BindView(R.id.id_person_head_img)
    ImageView idPersonHeadImg;
    @BindView(R.id.id_root)
    LinearLayout root;
    @BindView(R.id.id_nick_name)
    TextView nickName;
    @BindView(R.id.id_sex)
    TextView sex;
    @BindView(R.id.id_area)
    TextView area;
    @BindView(R.id.id_signatrue)
    TextView singatrue;
    @BindView(R.id.id_desc)
    TextView desc;

    UploadPicManager upManager;
    AudioUtils audioUtils;
    PopupView popupView;

    public static final String TYPE = "type";//区分个人签名和个人简介
    public static final String[] POPUP_ITEMS = {"拍摄", "从相册上传"};

    public static final int REQUEST_CODE = 1;
    public static final int RESULT_CODE1 = 102;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    /**
                     * 保存图片
                     */
//                    ((PersonInfoPresenter) presenter).savePersonInfo(Session.getUserId(), "headImg", (String) msg.obj);
                    break;
                case 2:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);

        Session.setUserPicUrl(t.toString());
        if (Protect.checkLoadImageStatus(aty)) {
            Glide.with(aty).load(Session.getUserPicUrl()).transform(new GlideRoundImage(aty)).
                    placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(idPersonHeadImg);

        }
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_my_info, null);
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
//        presenter = new PersonInfoPresenter();
    }

    @Override
    public void initView() {

        nickName.setText(Session.getUserName());
        sex.setText(Session.getUser_sex());
        area.setText(Session.getUser_area());
        singatrue.setText(Session.getUser_signatrue());
        desc.setText(Session.getUser_desc());
        setTitleText(getString(R.string.mine_person_info));
        if (Protect.checkLoadImageStatus(aty)) {
            Glide.with(this).load(Session.getUserPicUrl()).transform(new GlideCircleImage(this)).
                    placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(idPersonHeadImg);
        }
    }


    @Override
    public void initData() {

        audioUtils = new AudioUtils(this);
        popupView = new PopupView(this);
        popupView.setOnItemPopupClick(this);
        upManager = new UploadPicManager(this);
    }

    @OnClick({R.id.id_layout_person_photo, R.id.id_layout_person_nick, R.id.id_layout_person_scan,
            R.id.id_layout_person_sex, R.id.id_layout_person_area, R.id.id_layout_person_sign,
            R.id.id_layout_person_brief})
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        Bundle data = new Bundle();
        switch (v.getId()) {
            case R.id.id_layout_person_photo:
                trackUser("我的", "个人资料二级界面", "头像");
                intent.setClass(this, PictureManagerActivity.class);
                intent.putExtra("isSingle", true);
                startActivityForResult(intent, REQUEST_CODE);
//                popupView.showAtLocPop(root, POPUP_ITEMS);
                break;
            case R.id.id_layout_person_nick:
                trackUser("我的", "个人资料二级界面", "昵称");
                intent.setClass(this, PersionNickActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.id_layout_person_scan:
//                showActivity(this, PersonScanAty.class);
                break;
            case R.id.id_layout_person_sex:
                trackUser("我的", "个人资料二级界面", "性别");
                intent.setClass(this, PersonSexActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.id_layout_person_area:
                trackUser("我的", "个人资料二级界面", "地区");
                intent.setClass(this, PersonAreaAty.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.id_layout_person_sign:
                trackUser("我的", "个人资料二级界面", "个人签名");
                data.putString("content", singatrue.getText().toString());
                data.putString(TYPE, getString(R.string.mine_person_sign));
                intent.putExtras(data);
                intent.setClass(this, PersonBriefAty.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.id_layout_person_brief:
                trackUser("我的", "个人资料二级界面", "个人简介");
                data.putString("content", desc.getText().toString());
                data.putString(TYPE, getString(R.string.mine_person_brief));
                intent.putExtras(data);
                intent.setClass(this, PersonBriefAty.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            default:
                break;
        }
    }

//    private void setHeadImage(Uri uri) {
//
//        /**
//         * oss上传图片，得到回调之后，保存图片地址到本地
//         */
//
//        UploadPicManager.UploadInfo info = new UploadPicManager.UploadInfo();
//
//        info.fileSavePath = AudioUtils.getRealFilePath(this, uri);
//
//        List<UploadPicManager.UploadInfo> dataList = new ArrayList<>();
//        dataList.add(info);
//
//        upManager.uploads(this, dataList);
//    }

    @Override
    public void onResult(boolean result, List<UploadPicManager.UploadInfo> resultList) {

        if (result) {
            Log.e("tag", "图片oss地址=" + resultList.get(0).fileSavePath);
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = resultList.get(0).fileSavePath;
            mHandler.sendMessage(msg);
        }

    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == AudioUtils.SELECT_A_PICTURE) {
//            if (resultCode == RESULT_OK && null != data) {
//                setHeadImage(Uri.fromFile(new File(AudioUtils.IMGPATH,
//                        AudioUtils.TMP_IMAGE_FILE_NAME)));
//
//                Log.e("tag", "SELECT_A_PICTURE");
//
//            } else if (resultCode == RESULT_CANCELED) {
//                SVProgressHUD.showErrorWithStatus(this, "取消上传");
//            }
//        } else if (requestCode == AudioUtils.SELECET_A_PICTURE_AFTER_KIKAT) {
//            if (resultCode == RESULT_OK && null != data) {
//                String mAlbumPicturePath = audioUtils.getPath(getApplicationContext(), data.getData());
//                audioUtils.cropImageUriAfterKikat(Uri.fromFile(new File(mAlbumPicturePath)));
//            } else if (resultCode == RESULT_CANCELED) {
//                SVProgressHUD.showErrorWithStatus(this, "取消上传");
//            }
//        } else if (requestCode == AudioUtils.SET_ALBUM_PICTURE_KITKAT) {
//            if (resultCode == RESULT_OK && null != data) {
//                setHeadImage(Uri.fromFile(new File(AudioUtils.IMGPATH, AudioUtils.TMP_IMAGE_FILE_NAME)));
//                Log.e("tag", "SET_ALBUM_PICTURE_KITKAT");
//            } else if (resultCode == RESULT_CANCELED) {
//                SVProgressHUD.showErrorWithStatus(this, "取消上传");
//            }
//
//        } else if (requestCode == AudioUtils.TAKE_A_PICTURE) {
//            if (resultCode == RESULT_OK) {
//                audioUtils.cameraCropImageUri(Uri.fromFile(new File(AudioUtils.IMGPATH, AudioUtils.IMAGE_FILE_NAME)));
//            } else {
//                SVProgressHUD.showErrorWithStatus(this, "取消上传");
//            }
//        } else if (requestCode == AudioUtils.SET_PICTURE) {
//            if (resultCode == RESULT_OK && null != data) {
//                setHeadImage(Uri.fromFile(new File(AudioUtils.IMGPATH, AudioUtils.IMAGE_FILE_NAME)));
//                Log.e("tag", "SET_PICTURE");
//            } else if (resultCode == RESULT_CANCELED) {
//                SVProgressHUD.showErrorWithStatus(this, "取消上传");
//            } else {
//                SVProgressHUD.showErrorWithStatus(this, "取消上传");
//            }
//        } else if (requestCode == REQUEST_CODE) {
//            if (resultCode == RESULT_CODE1) {
//                nickName.setText(Session.getUserName());
//                sex.setText(Session.getUser_sex());
//                area.setText(Session.getUser_area());
//                singatrue.setText(Session.getUser_signatrue());
//                desc.setText(Session.getUser_desc());
//            } else if (resultCode == PublishAty.PUBLISH_RESULT_CAMERA) {
//                String path = data.getStringExtra("picture");
//                setHeadImage(Uri.fromFile(new File(path)));
//            }
//        }
//    }

    /**
     * 相片裁剪
     */
    private void doPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 640);
        intent.putExtra("outputY", 640);
//        intent.putExtra("return-data", true);
        intent.putExtra("return-data", false);
        initHeadPicPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPhotoFileName)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, Constants.CHOOSETRUE);
    }

    private void setHeadImage(String filePath) {
        /**
         * oss上传图片，得到回调之后，保存图片地址到本地
         */

        UploadPicManager.UploadInfo info = new UploadPicManager.UploadInfo();

        info.fileSavePath = filePath;

        List<UploadPicManager.UploadInfo> dataList = new ArrayList<>();
        dataList.add(info);

        upManager.compressAndUploads(this, dataList, OssManager.objectNameProfile);
    }

    private void initHeadPicPath() {
        String imgName = StringUtils.getPhotoFileName();
        File f = new File(CacheUtils.getCacheDirectory(aty, true, "pic") + imgName);
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (f != null) {
            mPhotoFileName = f.getAbsolutePath();
        }
    }

    /**
     * 设置头像
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bitmap photo = null;
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            photo = bundle.getParcelable("data");
            String imgName = StringUtils.getPhotoFileName();
            File f = new File(CacheUtils.getCacheDirectory(aty, true, "pic") + imgName);
            if (f.exists()) {
                f.delete();
            }
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream b = null;
            try {
                b = new FileOutputStream(f);// 将数据写入文件夹下中
                photo.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                setHeadImage(f.toString());
//                requestUploadHeadPic(f, photo);
                // uploadUserAvatar(Bitmap2Bytes(photo));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (b != null) {
                        b.flush();
                        b.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(IntentKey.EXTRA_PATH, mPhotoFileName);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mPhotoFileName = savedInstanceState.getString(IntentKey.EXTRA_PATH, "");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE_TAKE_PICTURE: // 已经选择了照片，这个是通过手机相机拍照返回的请求码
                    File mPhotoFile = null;
                    if (!TextUtils.isEmpty(mPhotoFileName)) {
                        mPhotoFile = new File(mPhotoFileName);
                    }
                    if (mPhotoFile != null) {
                        doPhoto(Uri.fromFile(mPhotoFile));
                    }
                    break;
                case Constants.REQUEST_CODE_TAKE_ALBUM: // 相册请求码
                    doPhoto(data.getData());
                    break;
                // 选择好照片后
                case Constants.CHOOSETRUE:// select_photo_already
//                    if (data != null) {
//                        setPicToView(data);
//                    }
                    if (!TextUtils.isEmpty(mPhotoFileName)) {
                        setHeadImage(mPhotoFileName);
                    }
                    break;
                default:
                    break;
            }
        } else if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_CODE1) {
                nickName.setText(Session.getUserName());
                sex.setText(Session.getUser_sex());
                area.setText(Session.getUser_area());
                singatrue.setText(Session.getUser_signatrue());
                desc.setText(Session.getUser_desc());
                zhugeIdentify(Session.getUserId(), Session.getUser_sex(), Session.getUser_area());
            }
//            } else if (resultCode == PublishAty.PUBLISH_RESULT_CAMERA) {
//                String path = data.getStringExtra("picture");
//                setHeadImage(path);
//            }
        }
    }

    private String mPhotoFileName;

    @Override
    public void OnItemClick(int position, String str) {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            switch (position) {
                case 0:
                    // 执行拍照前，应该先判断SD卡是否存在
                    String SDState = Environment.getExternalStorageState();
                    if (SDState.equals(Environment.MEDIA_MOUNTED)) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 创建拍摄照片保存的文件夹及图片文件名
                        String imgName = StringUtils.getPhotoFileName();
                        File mPhotoFile = new File(CacheUtils.getCacheDirectory(aty, true, "pic") + imgName);
                        if (mPhotoFile != null) {
                            mPhotoFileName = mPhotoFile.getAbsolutePath();
                        }
                        if (mPhotoFile.exists()) {
                            mPhotoFile.delete();
                        }
                        try {
                            mPhotoFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // 下面这句指定调用相机拍照后的照片存储的路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                        startActivityForResult(intent, Constants.REQUEST_CODE_TAKE_PICTURE);// 使用手机进行拍照的请求码是：1
                    } else {
                        SVProgressHUD.showInfoWithStatus(aty, getString(R.string.personal_no_sd_card));
                    }
//                audioUtils.callImageCapture();
                    break;
                case 1:
//                audioUtils.callImageGellery();
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    /**
                     * 下面这句话，与其它方式写是一样的效果，如果：
                     * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                     * intent.setType(""image/*");设置数据类型
                     * 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                     */
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    startActivityForResult(intent, Constants.REQUEST_CODE_TAKE_ALBUM);// 从相册中取图片的请求码是：2
                    break;
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.carme_sd_permission), RC_VIDEO_AND_EXTENER, perms);
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
            new AppSettingsDialog.Builder(this, "圈呼要使用摄像头,使用sd卡权限，否则app可能无法正常运行")
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
                    .setRequestCode(RC_VIDEO_AND_EXTENER)
                    .build()
                    .show();
        }
    }

    private void zhugeIdentify(String uId, String sex, String location) {
        //定义用户识别码

        //定义用户属性
        JSONObject personObject = new JSONObject();
        try {
            personObject.put("用户id", uId);
            personObject.put("性别", sex);
            personObject.put("地域", location);
        } catch (Exception e) {

        }
        //标识用户
        ZhugeSDK.getInstance().identify(getApplicationContext(), uId,
                personObject);
    }

    @Override
    public void refreshPage() {

    }
}