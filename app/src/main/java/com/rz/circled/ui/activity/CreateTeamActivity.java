package com.rz.circled.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.nos.NosService;
import com.rz.circled.R;
import com.rz.circled.widget.PopupView;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.permission.AppSettingsDialog;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CacheUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.common.widget.toasty.Toasty;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.yryz.yunxinim.DemoCache;
import com.yryz.yunxinim.team.TeamCreateHelper;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.common.ui.dialog.DialogMaker;
import com.yryz.yunxinim.uikit.common.util.log.LogUtil;
import com.yryz.yunxinim.uikit.session.actions.PickImageAction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rzw2 on 2017/4/6.
 */

public class CreateTeamActivity extends BaseActivity implements PopupView.OnItemPopupClick {
    Long invokeId = 0l;
    @BindView(R.id.iv_create_team_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_create_team_icon)
    TextView tvIcon;
    @BindView(R.id.tv_create_team_name_num)
    TextView tvNameNum;
    @BindView(R.id.et_create_team_name)
    EditText etTeamName;
    @BindView(R.id.et_create_team_info)
    EditText etTeamInfo;
    @BindView(R.id.tv_create_team_info_num)
    TextView tvTeamInfoNum;
    @BindView(R.id.ll_create_team_root)
    LinearLayout llRoot;

    private PopupView popupView;
    private String mPhotoFileName;
    private AbortableFuture<String> uploadFuture;
    private ArrayList<String> selectedFriend;
    public static final String[] POPUP_ITEMS = {"拍摄", "从相册上传"};

    public static void createTeam(Activity activity, String circleKey, String circleName, long invokeId) {
        Intent intent = new Intent(activity, CreateTeamActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.EXTRA_CLUB_ID, circleKey);
        bundle.putString(IntentKey.EXTRA_NAME, circleName);
        bundle.putLong(IntentKey.EXTRA_INVOKEID, invokeId);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (invokeId == 0l) {
            invokeId = getIntent().getLongExtra(IntentKey.EXTRA_INVOKEID, 0l);
            return;
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (invokeId != 0l) {
            JsEvent.callJsEvent(getIntent().getLongExtra(IntentKey.EXTRA_INVOKEID, 0l), null, BaseParamsObject.RESULT_CODE_CANCEL);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (invokeId != 0l) {
            JsEvent.callJsEvent(getIntent().getLongExtra(IntentKey.EXTRA_INVOKEID, 0l), null, BaseParamsObject.RESULT_CODE_CANCEL);
        }
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_create_team, null);
    }

    @Override
    public void initView() {
        setTitleLeftText(getString(R.string.cancel));
        setTitle(getString(R.string.create_private_circle));
        setTitleRightTextColor(R.color.color_main);
        setTitleRightText(getString(R.string.next));
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNext();
            }
        });
        etTeamName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();
                String info = String.format(getResources().getString(R.string.create_team_add_name_num), length);
                tvNameNum.setText(Html.fromHtml(info));
            }
        });
        etTeamInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();
                String info = String.format(getResources().getString(R.string.create_team_add_info_num), length);
                tvTeamInfoNum.setText(Html.fromHtml(info));
            }
        });
    }

    @Override
    public void initData() {
        popupView = new PopupView(this);
        popupView.setOnItemPopupClick(this);
    }


    @OnClick(R.id.iv_create_team_icon)
    public void onClick() {
        popupView.showAtLocPop(llRoot, POPUP_ITEMS);
    }

    private void setIconImageView() {
        tvIcon.setText(R.string.create_team_change_icon);
        Glide.with(this).load(mPhotoFileName).placeholder(R.mipmap.ic_default_pic).error(R.mipmap.ic_default_pic).into(ivIcon);
    }

    /**
     * 跳转去拉去好友
     */
    private void toNext() {
        if (TextUtils.isEmpty(mPhotoFileName)) {
            Toasty.info(this, getResources().getString(R.string.create_team_add_icon_error)).show();
            return;
        }
        if (TextUtils.isEmpty(etTeamName.getText().toString().trim())) {
            Toasty.info(this, getResources().getString(R.string.create_team_add_name_error)).show();
            return;
        }
        ArrayList<String> list = new ArrayList<>();
        list.add(NimUIKit.getAccount());
//        ContactsSelectAty.startActivityForResult(this, new ArrayList<String>(), getIntent().getStringExtra(IntentKey.KEY_CLUB_ID), IntentCode.TeamCreate.REQUEST_CODE_ADVANCED);
        ContactsSelectAty.startActivityForResult(this, list, getIntent().getStringExtra(IntentKey.EXTRA_CLUB_ID), IntentCode.TeamCreate.REQUEST_CODE_ADVANCED);
    }

    /**
     * 更新头像
     */
    private void updateTeamIcon(final String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        File file = new File(path);
        if (file == null) {
            return;
        }
        DialogMaker.showProgressDialog(this, null, null, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        LogUtil.i(TAG, "start upload icon, local file path=" + file.getAbsolutePath());
//        new Handler().postDelayed(outimeTask, ICON_TIME_OUT);
        uploadFuture = NIMClient.getService(NosService.class).upload(file, PickImageAction.MIME_JPEG);
        uploadFuture.setCallback(new RequestCallbackWrapper<String>() {
            @Override
            public void onResult(int code, final String url, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && !TextUtils.isEmpty(url)) {
                    //头像上传成功去创建圈子
                    toCreateTeam(url);
                } else {
                    onCreateDone();
                }
            }
        });
    }


    private void toCreateTeam(String imageUrl) {
        String intro = etTeamInfo.getText().toString().trim() + "";
        TeamCreateHelper.createAdvancedTeamActivity(this, selectedFriend, etTeamName.getText().toString().trim(), invokeId, getIntent().getStringExtra(IntentKey.EXTRA_CLUB_ID), intro, imageUrl);
//        //防止接口访问时间过长
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                finish();
//            }
//        }, 3000);
    }

    private void onCreateDone() {
        uploadFuture = null;
        DialogMaker.dismissProgressDialog();
        Toast.makeText(DemoCache.getContext(), R.string.create_team_add_error, Toast.LENGTH_SHORT).show();
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
                case Constants.CHOOSETRUE:// 选择好照片后
                    if (!TextUtils.isEmpty(mPhotoFileName)) {
                        setIconImageView();
                    }
                    break;
                case IntentCode.TeamCreate.REQUEST_CODE_NORMAL:
                    final ArrayList<String> selected = data.getStringArrayListExtra(ContactsSelectAty.RESULT_DATA);
                    if (selected != null && !selected.isEmpty()) {
                        TeamCreateHelper.createNormalTeam(this, selected, false, null, Session.getUserName() + getString(R.string.s_discussion_group));
                    } else {
                        Toast.makeText(this, R.string.choose_one_person, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case IntentCode.TeamCreate.REQUEST_CODE_ADVANCED:
                    selectedFriend = data.getStringArrayListExtra(ContactsSelectAty.RESULT_DATA);
                    updateTeamIcon(mPhotoFileName);
                    break;
                default:
                    break;
            }
        } else {
//            finish();
        }
    }

    //******                   选择图片部分代码                  ******//

    @Override
    public void OnItemClick(int position, String tag) {
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
            new AppSettingsDialog.Builder(this, getString(R.string.camera_sd_permissions_run))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
                    .setRequestCode(RC_VIDEO_AND_EXTENER)
                    .build()
                    .show();
        }
    }


    @Override
    public void refreshPage() {

    }
}
