package com.rz.circled.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.adapter.AdpPicManager;
import com.rz.circled.ui.view.ComSelPopWindow;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentKey;
import com.rz.common.permission.AfterPermissionGranted;
import com.rz.common.permission.AppSettingsDialog;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CacheUtils;
import com.rz.common.utils.DensityUtils;
import com.rz.common.utils.ImageUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.bean.ImageFloder;
import com.rz.httpapi.bean.PictureModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 图片管理器
 */
public class PictureManagerActivity extends BaseActivity implements OnItemClickListener {

    /**
     * 右侧发布按钮
     */
//    @BindView(R.id.id_pic_mag_btn)
//    TextView mTxtPublish;

    /**
     * 底部父控件
     */
    @BindView(R.id.id_mag_rela)
    RelativeLayout mLayout;

    /**
     * 图片的名称
     */
    @BindView(R.id.id_look_all_pic)
    TextView mTxtPicName;

    /**
     * 图片张数
     */
    @BindView(R.id.id_all_pic_num)
    TextView mTxtPicNum;

    /**
     * 图片显示器
     */
    @BindView(R.id.id_pic_gridview)
    GridView mGridView;

    private AdpPicManager mPicManager;

    private PictureModel mAddCameraPic;

    /**
     * 头像图片路径
     */
//    private File mPhotoFile;
    private String mPhotoFilePath;

    /**
     * 第一张图片的封图
     */
    private String mFirstPic;

    /**
     * 还可以选择几张图片
     */
    private int index;

    /**
     * 记录已经选择的图片张数
     */
    private int mChooseNum = 0;

    /**
     * 记录已经选择的图片张数
     */
    private boolean isSingle = false;

    /**
     * popupwindow弹出框，显示文件夹
     */
    private ComSelPopWindow mPopWindow;

    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

    /**
     * 展示文件夹下的图片
     */
    private List<PictureModel> mShowPictures = new ArrayList<PictureModel>();

    private List<PictureModel> mSelectPictures = new ArrayList<>();

    /**
     * 存储的所有搜索到的图片
     */
    private List<PictureModel> mSaveScanAllPics = new ArrayList<PictureModel>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 表示没有扫描到图片
                case 0:
                    initHomData(0);
                    break;
                // 表示有扫描到图片
                case 1:
                    initHomData(1);
                    break;
            }
        }
    };
    //是否需要照片建材，默认是需要
    private boolean isNeedCut = true;

    /**
     * 点击相册选择回调
     */
    public static final int DEFAULT_REQUEST = 11;
    /**
     * 照相机回调
     */
    public static final int PUBLISH_RESULT_CAMERA = 12;


    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_manager_pic, null, false);
    }

    @Override
    public void initData() {
    }

    /**
     * 加载并显示所有的图片
     */
    private void initHomData(int flag) {
        mSaveScanAllPics.clear();
        //    initCameraPic();

        if (flag == 1) {
            for (ImageFloder floder : mImageFloders) {
                mShowPictures.addAll(choosePic(floder));
                mSaveScanAllPics.addAll(choosePic(floder));
            }
        }
        Collections.sort(mShowPictures, new FileComparator());
        initCameraPic();
        mPicManager.setmAddCameraPic(mAddCameraPic);

        mPicManager.notifyDataSetChanged();

        ImageFloder floder = new ImageFloder();
        floder.setmFirstPicPath(mFirstPic);
        floder.setmPicName("所有图片");
        floder.setmPicCount(mSaveScanAllPics.size());
        mImageFloders.add(0, floder);

        mTxtPicNum.setText(mSaveScanAllPics.size()
                + getString(R.string.zhang_num));
        mTxtPicName.setText("所有图片");
    }

    /**
     * 根据文件夹查找里面的图片
     *
     * @param floder
     * @return
     */
    private List<PictureModel> choosePic(ImageFloder floder) {
        File mImgDir = new File(floder.getmPicDir());
        List<PictureModel> mPicPaths = new ArrayList<PictureModel>();
        String[] picName = mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png")
                        || filename.endsWith(".jpeg")
                        || filename.endsWith(".JPG")
                        || filename.endsWith(".PNG")
                        || filename.endsWith(".JPEG")
                        || filename.endsWith(".gif")
                        || filename.endsWith(".GIF")
                        || filename.endsWith(".BMP")
                        || filename.endsWith(".bmp"))
                    return true;
                return false;
            }
        });
        if (null != picName && picName.length > 0) {
            for (String aPicName : picName) {
                PictureModel pic = new PictureModel();
                String filePath = mImgDir.getAbsolutePath() + "/" + aPicName;
                pic.setmPicPath(filePath);
                pic.setLastModifyTime(new File(filePath).lastModified());
                // pic.setmBitmap(ImageUtils.GetZoom(filePath));
                if (mSaveScanAllPics.isEmpty()) {
                    pic.setSelect(false);
                } else {
                    for (PictureModel model : mSaveScanAllPics) {
                        if (model.getmPicPath().equals(filePath)) {
                            if (model.isSelect()) {
                                pic.setSelect(true);
                            }
                        }
                    }
                }
                mPicPaths.add(pic);
            }
        }
        return mPicPaths;
    }


    @Override
    public void initView() {
        isNeedCut = getIntent().getBooleanExtra("isNeed", true);
//        setTitleText(getString(R.string.attach_picture));
//        setRightPublishText(R.string.publish);
        index = getIntent().getIntExtra("index", 30);

        if (getIntent().hasExtra("isSingle"))
            isSingle = getIntent().getExtras().getBoolean("isSingle");

        if (isSingle) {
            clearTitleRight();
        }

//        mTxtPublish.setText(getString(R.string.channel_finish));
//        mTxtPublish.setEnabled(false);

        mPopWindow = new ComSelPopWindow(aty);


        int space = (DensityUtils.getScreenW(aty) - DensityUtils.dip2px(aty, 5)
                * 2 - DensityUtils.dip2px(aty, 110) * 3) / 3;
        //    initCameraPic();
        mPicManager = new AdpPicManager(aty, mShowPictures);
        mPicManager.setmAddCameraPic(mAddCameraPic);

        mGridView.setAdapter(mPicManager);
        mGridView.setVerticalSpacing(space);
        mGridView.setHorizontalSpacing(space);
        mGridView.setNumColumns(3);

        mGridView.setOnItemClickListener(this);

        doImages();
    }

    /**
     * 添加照相机图片
     */
    private void initCameraPic() {
        if (null != mAddCameraPic) {
            mAddCameraPic = null;
        }
        mAddCameraPic = new PictureModel();
        mAddCameraPic.setmBitmap(ImageUtils.drawableToBitmap(getResources()
                .getDrawable(R.mipmap.r_pic_carmer)));

        if (!mShowPictures.contains(mAddCameraPic)) {
            mShowPictures.add(0, mAddCameraPic);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(IntentKey.EXTRA_PATH, mPhotoFilePath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mPhotoFilePath = savedInstanceState.getString(IntentKey.EXTRA_PATH);
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE_TAKE_PICTURE: // 已经选择了照片，这个是通过手机相机拍照返回的请求码
                    if (isNeedCut) {
                        if (!TextUtils.isEmpty(mPhotoFilePath))
                            doPhoto(Uri.fromFile(new File(mPhotoFilePath)));
                    } else {
                        if (!TextUtils.isEmpty(mPhotoFilePath))
                            gotoPublish(new File(mPhotoFilePath));
                    }

                    //TOAST
                    break;
                // 选择好照片后
                case Constants.CHOOSETRUE:// select_photo_already
                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
            }
        }
    }

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
        intent.putExtra("outputX", 240);
        intent.putExtra("outputY", 240);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Constants.CHOOSETRUE);
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
            File f = new File(CacheUtils.getCacheDirectory(aty, true, "pic")
                    + imgName);
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
                Intent data = new Intent();
                data.putExtra("picture", f.toString());
                setResult(PictureManagerActivity.PUBLISH_RESULT_CAMERA, data);
                finish();
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

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void doCamera() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            String SDState = Environment.getExternalStorageState();
            if (SDState.equals(Environment.MEDIA_MOUNTED)) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 创建拍摄照片保存的文件夹及图片文件名
                String imgName = StringUtils.getPhotoFileName();
                File mPhotoFile = new File(CacheUtils.getCacheDirectory(aty, true, "pic") + imgName);
                if (mPhotoFile.exists()) {
                    mPhotoFile.delete();
                }
                try {
                    mPhotoFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mPhotoFilePath = mPhotoFile.getAbsolutePath();
                // 下面这句指定调用相机拍照后的照片存储的路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(intent,
                        Constants.REQUEST_CODE_TAKE_PICTURE);// 使用手机进行拍照的请求码是：1
            } else {
                //	ViewInject.toast(getString(R.string.personal_no_sd_card));
//                showToast(getString(R.string.personal_no_sd_card));
                Toasty.info(mContext, mContext.getString(R.string.personal_no_sd_card)).show();
            }
        } else {
            EasyPermissions.requestPermissions(this, "圈乎要调用摄像头", RC_CAMERA_PERM, Manifest.permission.CAMERA);
        }

    }


    private void gotoPublish(File mPhotoFile) {
        List<String> pics = new ArrayList<String>();
        for (PictureModel model : mSaveScanAllPics) {
            if (model.isSelect()) {
                pics.add(model.getmPicPath());
            }
        }

        Intent data = new Intent();
        data.putExtra("picture", mPhotoFile.toString());
//        setResult(PublishAty.PUBLISH_RESULT_CAMERA, data);

        finish();

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
            new AppSettingsDialog.Builder(this, "圈乎要使用摄像头,和sd卡权限，否则app可能无法正常运行")
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
                    .setRequestCode(RC_SETTINGS_SCREEN)
                    .build()
                    .show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        PictureModel model = mPicManager.getItem(position);
        if (model == mAddCameraPic) {
            // 执行拍照前，应该先判断SD卡是否存在
            doCamera();
        } else {
            if (!isSingle) {
                for (PictureModel mp : mSaveScanAllPics) {
                    if (model.getmPicPath().equals(mp.getmPicPath())) {
//                        if (model.isSelect()) {
//                            mChooseNum--;
//                            mp.setSelect(false);
//                            mSelectPictures.remove(mPicManager.getItem(position));
//                            mPicManager.setPicSelect(position, false);
//                            if (mChooseNum == 0) {
//                                mTxtPublish.setText(getString(R.string.channel_finish));
//                                mTxtPublish.setEnabled(false);
//                            } else {
//                                mTxtPublish.setText(getString(R.string.channel_finish)
//                                        + "(" + mChooseNum + "/" + index
//                                        + ")");
//                                mTxtPublish.setEnabled(true);
//                            }
//                        } else {
//                            mChooseNum++;
//                            if (mChooseNum > index) {
//                                mChooseNum = index;
//                                mTxtPublish
//                                        .setText(getString(R.string.channel_finish)
//                                                + "(" + index + "/" + index + ")");
//                                mTxtPublish.setEnabled(true);
//                                showToast("最多只能上传" + index + "张");
//                            } else {
//                                mp.setSelect(true);
//                                mPicManager.setPicSelect(position, true);
//                                mSelectPictures.add(mPicManager.getItem(position));
//                                mTxtPublish
//                                        .setText(getString(R.string.channel_finish)
//                                                + "(" + mChooseNum + "/" + index
//                                                + ")");
//                                mTxtPublish.setEnabled(true);
//                            }
//                        }
                    }
                }
            } else {
                doPhoto(Uri.fromFile(new File(model.getmPicPath())));
            }
        }
    }

    /**
     * 是否还可以添加图片
     *
     * @return true 不可以 false 可以
     */
//    private boolean isMorePic() {
//        mChooseNum = 0;
//        for (PictureModel mp : mSaveScanAllPics) {
//            if (mp.isSelect()) {
//                mChooseNum++;
//            }
//        }
//        if (mChooseNum == 0) {
//            mTxtPublish.setText(getString(R.string.channel_finish));
//            mTxtPublish.setEnabled(false);
//        } else {
//            mTxtPublish.setText(getString(R.string.channel_finish) + "("
//                    + mChooseNum + "/" + index + ")");
//            mTxtPublish.setEnabled(true);
//        }
//        if (mChooseNum >= index) {
//            return true;
//        }
//        return false;
//    }

    @AfterPermissionGranted(RC_EXTENER)
    public void doImages() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            getImages();
        } else {
            EasyPermissions.requestPermissions(this, "圈呼要使用sd卡", RC_EXTENER, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            //	ViewInject.toast(getString(R.string.personal_no_sd_card));
            Toasty.info(mContext, mContext.getString(R.string.personal_no_sd_card)).show();
            return;
        }
        // 扫描图片文件夹

        mDirPaths.clear();
        mImageFloders.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = getContentResolver();
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                if (null != mCursor && mCursor.getCount() > 0) {
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor.getString(mCursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));
                        // 拿到第一张图片的路径
                        if (StringUtils.isEmpty(mFirstPic)) {
                            mFirstPic = path;
                        }

                        // 获取该图片的父路径名
                        File parentFile = new File(path).getParentFile();
                        if (parentFile == null)
                            continue;
                        String dirPath = parentFile.getAbsolutePath();
                        ImageFloder imageFloder = null;
                        // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                        if (mDirPaths.contains(dirPath)) {
                            continue;
                        } else {
                            mDirPaths.add(dirPath);
                            // 初始化imageFloder
                            imageFloder = new ImageFloder();
                            imageFloder.setmPicDir(dirPath);
                            imageFloder.setmFirstPicPath(path);
                        }

                        String[] s = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                if (filename.endsWith(".jpg")
                                        || filename.endsWith(".png")
                                        || filename.endsWith(".jpeg")
                                        || filename.endsWith(".JPG")
                                        || filename.endsWith(".PNG")
                                        || filename.endsWith(".JPEG"))
                                    return true;
                                return false;
                            }
                        });

                        if (s == null) {
                            continue;
                        }
                        int picSize = s.length;

                        imageFloder.setmPicCount(picSize);
                        mImageFloders.add(imageFloder);
                    }
                    mCursor.close();
                    // 通知Handler扫描图片完成
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

//    @OnClick(R.id.id_pic_mag_btn)
//    public void complete() {
//        mTxtPublish.setEnabled(false);
////        List<String> pics = new ArrayList<String>();
////        for (PictureModel model : mSaveScanAllPics) {
////            if (model.isSelect()) {
////                pics.add(model.getmPicPath());
////            }
////        }
//        List<String> pics = new ArrayList<String>();
//        for (PictureModel model : mSelectPictures) {
//            if (model.isSelect()) {
//                pics.add(model.getmPicPath());
//            }
//        }
//        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putStringArrayList("picList", (ArrayList<String>) pics);
//        intent.putExtras(bundle);
//        setResult(PublishAty.PUBLISH_RESULT, intent);
//        finish();
//    }

    @OnClick(R.id.id_look_all_pic)
    public void looAllPic() {
        backgroundAlpha(0.7f);
        mPopWindow.showPop(mLayout, mImageFloders);
        mPopWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        mPopWindow.setOnSelectFloderListener(new ComSelPopWindow.onSelectFloderListener() {
            @Override
            public void onSelect(int position, ImageFloder imageFloder) {
                for (ImageFloder floder : mImageFloders) {
                    if (floder == imageFloder) {
                        floder.setmPicSelect(position);
                    } else {
                        floder.setmPicSelect(-1);
                    }
                }

                mShowPictures.clear();


                if (position == 0) {
                    mShowPictures.addAll(mSaveScanAllPics);
                } else {
                    mShowPictures.addAll(choosePic(imageFloder));
                }

                Collections.sort(mShowPictures, new FileComparator());
                initCameraPic();
                mPicManager.setmAddCameraPic(mAddCameraPic);
                mPicManager.notifyDataSetChanged();

                mTxtPicNum.setText(imageFloder.getmPicCount()
                        + getString(R.string.zhang_num));
                mTxtPicName.setText(imageFloder.getmPicName());
                mPopWindow.dismiss();
            }
        });
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = bgAlpha; // 0.0-1.0
//        getWindow().setAttributes(lp);
    }

    @Override
    public void refreshPage() {

    }


    private class FileComparator implements Comparator<PictureModel> {

        @Override
        public int compare(PictureModel lhs, PictureModel rhs) {
//            return ((Long) lhs.getLastModifyTime()).compareTo((Long) rhs.getLastModifyTime());
            return ((Long) rhs.getLastModifyTime()).compareTo((Long) lhs.getLastModifyTime());
        }
    }
}
