package com.rz.circled.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.rz.circled.R;
import com.rz.circled.adapter.PicAdapter;
import com.rz.circled.application.QHApplication;
import com.rz.circled.js.http.WebHttp;
import com.rz.circled.js.model.EditorAuthorityRootBean;
import com.rz.circled.js.model.EditorCategoryRootTwoModel;
import com.rz.circled.js.model.EditorCategoryTwoModel;
import com.rz.circled.js.model.EditorConfigTwoModel;
import com.rz.circled.js.model.EditorDataSourceTwoModel;
import com.rz.circled.js.model.EditorRootTwoBean;
import com.rz.circled.js.model.HttpRequestModel;
import com.rz.circled.recorder.RecordResult;
import com.rz.circled.recorder.RecorderContant;
import com.rz.circled.widget.ExpandGridView;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.PopupView;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.oss.OssManager;
import com.rz.common.oss.UploadPicManager;
import com.rz.common.permission.AfterPermissionGranted;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.ui.view.CommonDialog;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.FileUtils;
import com.rz.common.utils.ImageUtils;
import com.rz.common.utils.NetUtils;
import com.rz.common.utils.NetWorkSpeedUtils;
import com.rz.common.utils.Protect;
import com.rz.common.utils.Record;
import com.rz.common.utils.StatusBarUtils;
import com.rz.common.utils.SystemUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.api.CallManager;
import com.rz.sgt.jsbridge.JsEvent;
import com.yryz.yunxinim.uikit.common.util.sys.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 2017/5/27
 * 发布图文
 */
public class EditorTwoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.sv_editor_two_root)
    ScrollView scrollViewRoot;
    @BindView(R.id.ll_editor_two_root)
    LinearLayout llRoot;
    @BindView(R.id.ll_editor_two_sort_root)
    LinearLayout llSortRoot;
    //封面
    @BindView(R.id.iv_editor_two_page)
    SimpleDraweeView ivPage;
    @BindView(R.id.tv_editor_two_page_change)
    TextView tvPageChange;
    @BindView(R.id.tv_editor_two_page_add)
    TextView tvPageAdd;
    @BindView(R.id.ll_editor_two_page)
    LinearLayout llPage;
    //标题
    @BindView(R.id.et_editor_two_title)
    EditText etTitle;
    @BindView(R.id.tv_editor_two_title_num)
    TextView tvTitleNum;
    @BindView(R.id.tv_editor_two_title_count)
    TextView tvTitleCount;
    @BindView(R.id.ll_editor_two_title)
    LinearLayout llTitle;
    //分类
    @BindView(R.id.tv_editor_two_sort_name)
    TextView tvSortName;
    @BindView(R.id.tv_editor_two_sort)
    TextView tvSort;
    @BindView(R.id.rl_editor_two_sort)
    RelativeLayout rlSort;
    @BindView(R.id.iv_editor_two_sort_arrow)
    ImageView ivSortArrow;
    //地点
    @BindView(R.id.tv_editor_two_location_name)
    TextView tvLocationName;
    @BindView(R.id.tv_editor_two_location)
    TextView tvLocation;
    @BindView(R.id.rl_editor_two_location)
    RelativeLayout rlLocation;
    //时间
    @BindView(R.id.tv_editor_two_time_name)
    TextView tvTimeName;
    @BindView(R.id.tv_editor_two_time)
    TextView tvTime;
    @BindView(R.id.rl_editor_two_time)
    RelativeLayout rlTime;
    //标签
    @BindView(R.id.et_editor_two_label)
    EditText etLabel;
    @BindView(R.id.tv_editor_two_label_num)
    TextView tvLabelNum;
    @BindView(R.id.tv_editor_two_label_count)
    TextView tvLabelCount;
    @BindView(R.id.ll_editor_two_label)
    LinearLayout llLabel;
    //简介
    @BindView(R.id.tv_editor_two_introduction_num)
    TextView tvIntroductionNum;
    @BindView(R.id.tv_editor_two_introduction_count)
    TextView tvIntroductionCount;
    @BindView(R.id.et_editor_two_introduction)
    EditText etIntroduction;
    @BindView(R.id.ll_editor_two_introduction)
    LinearLayout llIntroduction;
    //详情
    @BindView(R.id.tv_editor_two_content_num)
    TextView tvContentTextNum;
    @BindView(R.id.tv_editor_two_content_count)
    TextView tvContentTextCount;
    @BindView(R.id.tv_editor_two_pic_num)
    TextView tvContentPicNum;
    @BindView(R.id.tv_editor_two_pic_count)
    TextView tvContentPicCount;
    @BindView(R.id.ll_editor_two_content_text)
    LinearLayout llContentText;
    @BindView(R.id.ll_editor_two_content_num)
    LinearLayout llContentNum;
    @BindView(R.id.ll_editor_two_pic_num)
    LinearLayout llContentPicNum;
    @BindView(R.id.ll_editor_two_content)
    LinearLayout llContent;
    @BindView(R.id.ll_editor_two_bottom)
    LinearLayout llBottom;
    //图片,视频,音频
    @BindView(R.id.iv_editor_two_choose_pic)
    ImageView ivChoosePic;
    @BindView(R.id.iv_editor_two_choose_audio)
    ImageView ivChooseAudio;
    @BindView(R.id.iv_editor_two_choose_video)
    ImageView ivChooseVideo;
    @BindView(R.id.ll_editor_two_media_root)
    LinearLayout llMediaRoot;
    @BindView(R.id.ll_editor_two_media)
    LinearLayout llMedia;
    @BindView(R.id.tv_editor_two_video_num)
    TextView tvVideoNum;
    @BindView(R.id.tv_editor_two_video_count)
    TextView tvVideoCount;
    @BindView(R.id.tv_editor_two_audio_num)
    TextView tvAudioNum;
    @BindView(R.id.tv_editor_two_audio_count)
    TextView tvAudioCount;
    @BindView(R.id.ll_editor_two_video_num)
    LinearLayout llVideoNum;
    @BindView(R.id.ll_editor_two_audio_num)
    LinearLayout llAudioNum;
    @BindView(R.id.gv_editor_two_pic)
    ExpandGridView gvPic;
    //视频
    @BindView(R.id.rl_editor_two_video)
    RelativeLayout rlVideo;
    @BindView(R.id.iv_editor_two_video_icon)
    ImageView ivVideoIcon;
    @BindView(R.id.iv_editor_two_video_preview)
    ImageView ivVideoPreview;
    @BindView(R.id.iv_editor_two_video_delete)
    ImageView ivVideoDelete;
    //音频
    @BindView(R.id.rl_editor_two_audio)
    RelativeLayout rlAudio;
    @BindView(R.id.iv_editor_two_audio_play)
    ImageView ivVideoAudioPlay;
    @BindView(R.id.tv_editor_two_audio_play_time)
    TextView tvAudioPlayTime;
    @BindView(R.id.iv_editor_two_audio_delete)
    ImageView ivVideoAudioDelete;
    @BindView(R.id.tv_editor_two_media)
    TextView tvMedia;
    //匿名,投票
    @BindView(R.id.cb_editor_two_anonymity)
    CheckBox cbAnonymity;
    @BindView(R.id.cb_editor_two_vote)
    CheckBox cbVote;
    //权限
    @BindView(R.id.tv_editor_two_authority)
    TextView tvAuthority;
    @BindView(R.id.tv_editor_title_left)
    TextView tvEditorTitleLeft;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.tv_editor_title_right)
    TextView tvEditorTitleRight;
    //    @BindView(R.id.et_editor_two_title)
//    EditText etEditorTwoTitle;
//    @BindView(R.id.tv_editor_two_sort_name)
//    TextView tvEditorTwoSortName;
//    @BindView(R.id.tv_editor_two_location_name)
//    TextView tvEditorTwoLocationName;
    @BindView(R.id.iv_editor_two_location_arrow)
    ImageView ivEditorTwoLocationArrow;
    //    @BindView(R.id.tv_editor_two_time_name)
//    TextView tvEditorTwoTimeName;
    @BindView(R.id.iv_editor_two_time_arrow)
    ImageView ivEditorTwoTimeArrow;
    //    @BindView(R.id.et_editor_two_label)
//    EditText etEditorTwoLabel;
//    @BindView(R.id.tv_editor_two_introduction_num)
//    TextView tvEditorTwoIntroductionNum;
//    @BindView(R.id.tv_editor_two_content_num)
//    TextView tvEditorTwoContentNum;
//    @BindView(R.id.rl_editor_two_video)
//    RelativeLayout rlEditorTwoVideo;
//    @BindView(R.id.rl_editor_two_audio)
//    RelativeLayout rlEditorTwoAudio;
//    @BindView(R.id.iv_editor_two_choose_pic)
//    ImageView ivEditorTwoChoosePic;
//    @BindView(R.id.cb_editor_two_anonymity)
//    CheckBox cbEditorTwoAnonymity;
//    @BindView(R.id.tv_editor_two_authority)
//    TextView tvEditorTwoAuthority;
    //视频
    private String[] videoItems = {"拍摄视频", "从手机相册中选取"};
    private static final int TYPE_TEXT = 1;
    private static final int TYPE_IMAGE = 2;
    public final int SYSTEM_SHOOT_VIDEO = 1221;
    /**
     * 公共请求code
     */
    private static final int PIC_PUBLISH_REQUEST = 10;
    private static final int REQUEST_CODE = 2;
    public static final String TYPE_EDITOR = "typeEditor";
    private static final int PIC_PAGE_CHANGE_REQUEST = 20;
    /**
     * 点击相册选择回调
     */
    private static final int PUBLISH_RESULT = 11;
    /**
     * 照相机回调
     */
    private static final int PUBLISH_RESULT_CAMERA = 12;
    private static final int VIDEO_PUBLISH_REQUEST = 15;
    private static final int AUDIO_PUBLISH_REQUEST = 17;
    /**
     * 分类回调
     */
    private static final int SORT_REQUEST = 30;
    private final int AUTHORITY_REQUEST = 40;
    public static final long maxVideoSize = 500 * 1024 * 1024;
    private int contentEditCount = 0;
    private int contentImageCount = 0;
    private boolean isFirstInput = true;
    private EditorDataSourceTwoModel dataSource;
    private String mVideoUrl;
    private String mVideoFilePath;
    private String mVideoFileName;
    private String mVideoImage;
    private Bitmap mBitmap;
    private long videoDuration;
    private long videoSize;
    private String mAudioUrl;
    private String mAudioFilePath;
    private String mAudioFileName;
    private long audioDuration;
    private long audioSize;
    private PopupView mPopupView;
    private Record mRecord;
    public String ossDir;
    public static final String RULE_TXT = "text";
    public static final String RULE_IMAGE = "image";
    public static final String RULE_VIDEO = "video";
    public static final String RULE_AUDIO = "audio";
    public static final long minMill = 0L;
    public static final long maxMill = 60 * 1000 * 60 * 60 * 24L;
    private ArrayList<Map<String, Object>> jsResult = new ArrayList<>();
    private List<ArticleItem> articleItems = new ArrayList<>();
    private UploadPicManager uploadPicManager;
    private EditorRootTwoBean rootBean;
    private TimePickerDialog mDialogYearMonthDay;
    private EditorCategoryRootTwoModel categoryBean;
    /**
     * 是否为图文混排
     */
    private boolean isPicText = true;
    private ArrayList<String> picList = new ArrayList<>();
    private CommonAdapter picAdapter;
    private HttpRequestModel httpRequestModel;
    private CommonDialog commonDialog;
    private String videoFilePath;

    @Override
    protected boolean needStatusBarTint() {
        return false;
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_editor_two, null);
    }

    @Override
    public void setTitleRightText(int StringId) {
        tvEditorTitleRight.setText(getString(StringId));
    }

    @Override
    public void setTitleRightListener(View.OnClickListener rightListener) {
        tvEditorTitleRight.setOnClickListener(rightListener);
    }

    @Override
    public void setTitleLeftListener(View.OnClickListener leftListener) {
        tvEditorTitleLeft.setOnClickListener(leftListener);
    }

    @Override
    public void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        StatusBarUtils.setDarkStatusIcon(this, true);
        setTitleRightText(R.string.publish);
        setTitleRightTextColor(R.color.color_main);
        setTitleRightListener(new OnTitleRightClickListener());
        setTitleLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commonDialog == null)
                    commonDialog = new CommonDialog(mContext);
                commonDialog.showDialog(R.string.editor_two_cancel, new CommonDialog.OnCommonDialogConfirmListener() {
                    @Override
                    public void onConfirmListener() {
                        finish();
                    }
                });
            }
        });
        mRecord = new Record(this);
        mRecord.setOnPlayListener(new Record.OnPlayListener() {
            @Override
            public void stopPlay() {
                stopAudioImgAnim();
            }

            @Override
            public void pausePlay() {
                stopAudioImgAnim();
            }

            @Override
            public void starPlay() {
                showAudioImgAnim();
            }

            @Override
            public void progressPlay(int playTime) {
            }

            @Override
            public void onError(int code) {
                Toasty.info(mContext, getString(R.string.play_audio_fail)).show();
            }
        });
    }

    @Override
    public void initData() {
        parseIntent();
    }

    @OnClick({R.id.tv_editor_two_page_change, R.id.tv_editor_two_page_add, R.id.rl_editor_two_sort, R.id.rl_editor_two_location, R.id
            .rl_editor_two_time, R.id.iv_editor_two_choose_pic
            , R.id.iv_editor_two_choose_audio, R.id.iv_editor_two_choose_video, R.id.iv_editor_two_video_delete, R.id
            .iv_editor_two_audio_delete
            , R.id.iv_editor_two_video_icon, R.id.iv_editor_two_video_preview, R.id.iv_editor_two_audio_play, R.id.tv_editor_two_authority})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_editor_two_page_change:
            case R.id.tv_editor_two_page_add://选择封面
                PictureSelectedActivity.startActivityForResult(EditorTwoActivity.this, PIC_PAGE_CHANGE_REQUEST, 1, false);
                break;
            case R.id.rl_editor_two_sort://分类
                EditorConfigTwoModel sortModel = (EditorConfigTwoModel) rlSort.getTag();
                if (sortModel.isEditable() == null || sortModel.isEditable()) {
                    long currentId = -1;
                    if (dataSource != null && dataSource.getClassifyItemId() != null) {
                        currentId = dataSource.getClassifyItemId();
                    }
                    if (categoryBean != null && categoryBean.getData() != null && categoryBean.getData().size() > 0) {
                        Intent sortIntent = new Intent(this, EditorTwoSortActivity.class);
                        sortIntent.putExtra(IntentKey.EXTRA_ID, currentId);
                        sortIntent.putExtra(IntentKey.EXTRA_SERIALIZABLE, categoryBean.getData());
                        startActivityForResult(sortIntent, SORT_REQUEST);
                    }
                }
                break;
            case R.id.rl_editor_two_location://地区
                Intent locationIntent = new Intent(this, PersonAreaAty.class);
                locationIntent.putExtra(IntentKey.EXTRA_BOOLEAN, false);
                startActivityForResult(locationIntent, REQUEST_CODE);
                break;
            case R.id.rl_editor_two_time://选择日期
                showDateDialog();
                break;
            case R.id.iv_editor_two_choose_pic://选择图片
                EditorConfigTwoModel imgModel = (EditorConfigTwoModel) view.getTag();
                if (contentImageCount < imgModel.getUpperLimit())
                    PictureSelectedActivity.startActivityForResult(EditorTwoActivity.this, PIC_PUBLISH_REQUEST, imgModel.getUpperLimit()
                            - contentImageCount, false);
                else
                    Toasty.error(mContext, String.format(getString(R.string.max_size_choose_pic_hint), imgModel.getUpperLimit())).show();
                break;
            case R.id.iv_editor_two_choose_audio://选择音频
                Intent audioIntent = new Intent(this, VoicePubActivity.class);
                audioIntent.putExtra(IntentKey.EXTRA_BOOLEAN, false);
                startActivityForResult(audioIntent, AUDIO_PUBLISH_REQUEST);
                break;
            case R.id.iv_editor_two_choose_video://选择视频
                mPopupView.showAtLocPop(llRoot, videoItems);
                break;
            case R.id.id_iv_delete://删除图片(图文混排)
                ViewGroup parent = (ViewGroup) view.getParent();
                llContentText.removeView(parent);
                mergeEditText();
                initImageCount();
                break;
            case R.id.iv_editor_two_video_delete://删除视频
                rlVideo.setVisibility(View.INVISIBLE);
                if (rlAudio.getVisibility() != View.VISIBLE && gvPic.getVisibility() != View.VISIBLE) {
                    llMediaRoot.setVisibility(View.GONE);
                }
                resetVideo();
                initContentVideoView();
                break;
            case R.id.iv_editor_two_audio_delete://删除音频
                rlAudio.setVisibility(View.INVISIBLE);
                if (rlVideo.getVisibility() != View.VISIBLE && gvPic.getVisibility() != View.VISIBLE) {
                    llMediaRoot.setVisibility(View.GONE);
                }
                resetAudio();
                initContentAudioView();
                break;
            case R.id.iv_editor_two_video_preview:
            case R.id.iv_editor_two_video_icon://视频播放
                if (!TextUtils.isEmpty(mVideoFilePath)) {
                    Intent video = new Intent(this, MediaActivity.class);
                    video.putExtra(IntentKey.EXTRA_PATH, mVideoFilePath);
                    startActivity(video);
                } else if (!TextUtils.isEmpty(mVideoUrl)) {
                    Intent video = new Intent(this, MediaActivity.class);
                    video.putExtra(IntentKey.EXTRA_PATH, mVideoUrl);
                    startActivity(video);
                }
                break;
            case R.id.iv_editor_two_audio_play://播放音频
                if (mRecord != null) {
                    if (!TextUtils.isEmpty(mAudioFilePath)) {
                        mRecord.player(mAudioFilePath);
                    } else if (!TextUtils.isEmpty(mAudioUrl)) {
                        mRecord.player(mAudioUrl);
                    }
                }
                break;
            case R.id.tv_editor_two_authority://选择权限
                //跳转到选择权限页面
                EditorAuthorityRootBean authModel = (EditorAuthorityRootBean) view.getTag();
                Intent authorityIntent = new Intent(mContext, EditorTwoAuthorityActivity.class);
                authorityIntent.putExtra(IntentKey.EXTRA_SERIALIZABLE, authModel);
                startActivityForResult(authorityIntent, AUTHORITY_REQUEST);
                break;
        }
    }

    @Override
    public void refreshPage() {

    }

    private class OnTitleRightClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!CountDownTimer.isFastClick()) {
                checkData();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) return;
        if (requestCode == PIC_PUBLISH_REQUEST && data != null) {
            if (resultCode == CommonCode.REQUEST.PUBLISH_RESULT) {
                ArrayList<String> mList = data.getExtras().getStringArrayList("picList");
                if (null != mList && !mList.isEmpty()) {
                    if (isPicText) {
                        for (String filePath : mList) {
                            initContentImageView(filePath);
                        }
                    } else {
                        picList.addAll(mList);
                        initPic();
                    }
                }
            } else if (resultCode == CommonCode.REQUEST.PUBLISH_RESULT_CAMERA) {
                String path = data.getStringExtra("picture");
                if (isPicText) {
                    initContentImageView(path);
                } else {
                    picList.add(path);
                    initPic();
                }
            }
            return;
        }
        if (requestCode == VIDEO_PUBLISH_REQUEST && resultCode == RESULT_OK && data != null) {
            String filePath;
            try {
                filePath = data.getStringExtra(IntentKey.EXTRA_PATH);
                checkVideoFile(filePath);
                resetVideo();
                mVideoFilePath = filePath;
                initContentVideoView();
            } catch (Exception e) {
                e.printStackTrace();
                Toasty.info(mContext, getString(R.string.error_to_found_video)).show();
            }
            return;
        }
        if (requestCode == SYSTEM_SHOOT_VIDEO && resultCode == RESULT_OK) {
            resetVideo();
            mVideoFilePath = this.videoFilePath;
            initContentVideoView();
            return;
        }
        if (requestCode == RecorderContant.RECORDE_SHOW && resultCode == RESULT_OK) {
            RecordResult result = new RecordResult(data);
            //得到视频地址，和缩略图地址的数组，返回十张缩略图
            String filePath = result.getPath();
            String[] thumbs = result.getThumbnail();
            result.getDuration();
            try {
                checkVideoFile(filePath);
                resetVideo();
                mVideoFilePath = filePath;
                mVideoImage = thumbs[0];
                initContentVideoView();
            } catch (Exception e) {
                e.printStackTrace();
                Toasty.info(mContext, getString(R.string.error_to_found_video)).show();
            }
            return;
        }
        if (requestCode == AUDIO_PUBLISH_REQUEST && data != null) {
            resetAudio();
            mAudioFilePath = data.getStringExtra(IntentKey.EXTRA_PATH);
            initContentAudioView();
            return;
        }
        if (requestCode == PIC_PAGE_CHANGE_REQUEST) {
            if (resultCode == CommonCode.REQUEST.PUBLISH_RESULT) {//相册
                ArrayList<String> picList = data.getExtras().getStringArrayList("picList");
                if (null != picList && !picList.isEmpty()) {
                    String filePath = picList.get(0);
                    initPageImageView(filePath);
                }
            } else if (resultCode == PUBLISH_RESULT_CAMERA) {//相机
                String picPath = data.getStringExtra("picture");
                initPageImageView(picPath);
            }
            return;
        }
        if (requestCode == REQUEST_CODE && data != null) {
            String stringArea = data.getStringExtra(IntentKey.EXTRA_POSITION);
            tvLocation.setText(stringArea.trim());
            return;
        }
        if (requestCode == SORT_REQUEST && data != null) {
            long currentId = data.getLongExtra(IntentKey.EXTRA_ID, -1);
            if (currentId > 0) {
                if (dataSource == null) dataSource = new EditorDataSourceTwoModel();
                ArrayList<EditorCategoryTwoModel> sortList = categoryBean.getData();
                for (EditorCategoryTwoModel categoryTwoModel : sortList) {
                    if (categoryTwoModel.getId() == currentId) {
                        dataSource.setClassifyItemId(categoryTwoModel.getId());
                        dataSource.setClassifyItemName(categoryTwoModel.getCategoryName());
                    }
                }
            }
            if (dataSource != null) tvSort.setText(dataSource.getClassifyItemName());
            return;
        }
        if (requestCode == AUTHORITY_REQUEST && data != null) {//权限
            EditorAuthorityRootBean authorityRootBean = (EditorAuthorityRootBean) data.getSerializableExtra(IntentKey.EXTRA_SERIALIZABLE);
            dataSource.setAllowGeneralizeFlag(authorityRootBean.getAllowGeneralizeFlag() == 1 ? 1 : 0);
            dataSource.setAllowShareFlag(authorityRootBean.getAllowShareFlag() == 1 ? 1 : 0);
            dataSource.setContentPrice(authorityRootBean.getContentPrice());
            dataSource.setCoterieId(authorityRootBean.getCoterieId());
            tvAuthority.setTag(authorityRootBean);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 解析intent
     */
    private void parseIntent() {
        rootBean = (EditorRootTwoBean) getIntent().getSerializableExtra(IntentKey.EXTRA_SERIALIZABLE);
        if (rootBean == null)
            return;
        dataSource = rootBean.getDataSource();
        categoryBean = rootBean.getCategory();
        httpRequestModel = rootBean.getRequest();

        HashMap<String, EditorConfigTwoModel> configMap = rootBean.getConfig();

        //图文混排
        EditorConfigTwoModel picTextModel = null;
        if (configMap.containsKey("picTxt")) {
            picTextModel = configMap.get("picTxt");
            isPicText = picTextModel.getEnabled();
        }

        //封面图
        EditorConfigTwoModel pageModel = null;
        if (configMap.containsKey("coverPlan")) {
            pageModel = configMap.get("coverPlan");
        }
        processPage(pageModel);

        //标题
        EditorConfigTwoModel titleModel = null;
        if (configMap.containsKey("title")) {
            titleModel = configMap.get("title");
        }
        processTitle(titleModel);

        //分类
        EditorConfigTwoModel sortModel = null;
        if (configMap.containsKey("classify")) {
            sortModel = configMap.get("classify");
        }
        processSort(sortModel);

        //地点
        EditorConfigTwoModel areaModel = null;
        if (configMap.containsKey("site")) {
            areaModel = configMap.get("site");
        }
        processArea(areaModel);

        //时间
        EditorConfigTwoModel timeModel = null;
        if (configMap.containsKey("date")) {
            timeModel = configMap.get("date");
        }
        processTime(timeModel);

        if (rlLocation.getVisibility() != View.VISIBLE && rlTime.getVisibility() != View.VISIBLE && rlSort.getVisibility() != View.VISIBLE)
            llSortRoot.setVisibility(View.GONE);

        //标签
        EditorConfigTwoModel labelModel = null;
        if (configMap.containsKey("label")) {
            labelModel = configMap.get("label");
        }
        processLabel(labelModel);

        //简介
        EditorConfigTwoModel introductionModel = null;
        if (configMap.containsKey("description")) {
            introductionModel = configMap.get("description");
        }
        processIntroduction(introductionModel);

        //详情
        EditorConfigTwoModel contentModel = null;
        if (configMap.containsKey("text")) {
            contentModel = configMap.get("text");
        }
        processContent(contentModel);

        //图片
        EditorConfigTwoModel imgModel = null;
        if (configMap.containsKey("img")) {
            imgModel = configMap.get("img");
        }
        processImg(imgModel);

        //匿名
        EditorConfigTwoModel anonymityModel = null;
        if (configMap.containsKey("anonymity")) {
            anonymityModel = configMap.get("anonymity");
        }
        processAnonymity(anonymityModel);

        //投票
        EditorConfigTwoModel voteModel = null;
        if (configMap.containsKey("vote")) {
            voteModel = configMap.get("vote");
        }
        processVote(voteModel);

        //视频
        EditorConfigTwoModel videoModel = null;
        if (configMap.containsKey("video")) {
            videoModel = configMap.get("video");
        }
        processVideo(videoModel);

        //音频
        EditorConfigTwoModel audioModel = null;
        if (configMap.containsKey("audio")) {
            audioModel = configMap.get("audio");
        }
        processAudio(audioModel);
        processMedia();
        if (dataSource != null && dataSource.getContentSource() != null)
            processContentData();

        //权限
        EditorConfigTwoModel authModel = null;
        if (configMap.containsKey("auth")) {
            authModel = configMap.get("auth");
        }
        processAuthority(authModel);
    }

    /**
     * 处理封面图片
     */
    private void processPage(EditorConfigTwoModel pageModel) {
        if (pageModel != null && pageModel.getEnabled()) {
            llPage.setVisibility(View.VISIBLE);
            if (dataSource != null && !TextUtils.isEmpty(dataSource.getCoverPlanUrl())) {
//                Glide.with(this).load(dataSource.getCoverPlanUrl()).into(ivPage);
                ivPage.setTag(R.id.iv_editor_two_page, dataSource.getCoverPlanUrl());
                ivPage.setVisibility(View.VISIBLE);
                tvPageChange.setVisibility(View.VISIBLE);
                tvPageAdd.setVisibility(View.GONE);
            } else {
                ivPage.setVisibility(View.GONE);
                tvPageChange.setVisibility(View.GONE);
                tvPageAdd.setVisibility(View.VISIBLE);
            }
            llPage.setTag(pageModel);
        } else {
            llPage.setVisibility(View.GONE);
        }
    }

    /**
     * 处理标题
     */
    private void processTitle(EditorConfigTwoModel titleModel) {
        //title不为空且存在
        if (titleModel != null && titleModel.getEnabled()) {
            llTitle.setVisibility(View.VISIBLE);
            etTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(titleModel.getUpperLimit())});
            tvTitleCount.setText("/" + titleModel.getUpperLimit());
            etTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    tvTitleNum.setText(s.toString().length() + "");
                }
            });
            etTitle.setHint(titleModel.getInputPrompt());
            if (dataSource != null && !TextUtils.isEmpty(dataSource.getTitle())) {
                etTitle.setText(dataSource.getTitle());
                etTitle.setSelection(dataSource.getTitle().length());
            }
            etTitle.setHint(titleModel.getInputPrompt());
            llTitle.setTag(titleModel);
        } else {
            llTitle.setVisibility(View.GONE);
        }
    }

    /**
     * 处理分类
     */
    private void processSort(EditorConfigTwoModel sortModel) {
        if (sortModel != null && sortModel.getEnabled()) {
            rlSort.setVisibility(View.VISIBLE);
            if (dataSource != null && !TextUtils.isEmpty(dataSource.getClassifyItemName())) {
                tvSort.setText(dataSource.getClassifyItemName());
            } else if (categoryBean != null && !TextUtils.isEmpty(categoryBean.getDefaultText())) {
                if (dataSource == null)
                    dataSource = new EditorDataSourceTwoModel();
                dataSource.setClassifyItemId(categoryBean.getDefaultId());
                dataSource.setClassifyItemName(categoryBean.getDefaultText());
                tvSort.setText(dataSource.getClassifyItemName());
            }
            ivSortArrow.setVisibility((sortModel.isEditable() == null || sortModel.isEditable()) ? View.VISIBLE : View.INVISIBLE);
            rlSort.setTag(sortModel);
        } else {
            rlSort.setVisibility(View.GONE);
        }
    }

    /**
     * 处理地点
     */
    private void processArea(EditorConfigTwoModel areaModel) {
        if (areaModel != null && areaModel.getEnabled()) {
            rlLocation.setVisibility(View.VISIBLE);
            if (dataSource != null && !TextUtils.isEmpty(dataSource.getProvince())) {
                tvLocation.setText(dataSource.getProvince() + " " + dataSource.getCity());
            } else if (areaModel.getRequired()) {
                tvLocation.setText(Session.getUser_area());
            }
            rlLocation.setTag(areaModel);
        } else {
            rlLocation.setVisibility(View.GONE);
        }
    }

    /**
     * 处理时间
     */
    private void processTime(EditorConfigTwoModel timeModel) {
        if (timeModel != null && timeModel.getEnabled()) {
            rlTime.setVisibility(View.VISIBLE);
            if (dataSource != null && !TextUtils.isEmpty(dataSource.getDate())) {
                tvTime.setText(dataSource.getDate());
            } else if (timeModel.getRequired()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM--dd");
                String format = dateFormat.format(new Date());
                tvTime.setText(format);
            }
            rlTime.setTag(timeModel);
        } else {
            rlTime.setVisibility(View.GONE);
        }


    }

    /**
     * 处理标签
     */
    private void processLabel(EditorConfigTwoModel labelModel) {
        if (labelModel != null && labelModel.getEnabled()) {
            llLabel.setVisibility(View.VISIBLE);
            etLabel.setFilters(new InputFilter[]{new InputFilter.LengthFilter(labelModel.getUpperLimit())});
            tvLabelCount.setText("/" + labelModel.getUpperLimit());
            etLabel.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    tvLabelNum.setText(s.toString().length() + "");
                }
            });
            if (dataSource != null && !TextUtils.isEmpty(dataSource.getLabel())) {
                etLabel.setText(dataSource.getLabel());
                etLabel.setSelection(dataSource.getLabel().length());
            }
            etLabel.setHint(labelModel.getInputPrompt());
            llLabel.setTag(labelModel);
        } else {
            llLabel.setVisibility(View.GONE);
        }
    }

    /**
     * 处理简介
     */
    private void processIntroduction(EditorConfigTwoModel introductionModel) {
        if (introductionModel != null && introductionModel.getEnabled()) {
            llIntroduction.setVisibility(View.VISIBLE);
            tvIntroductionCount.setText("/" + introductionModel.getUpperLimit());
            etIntroduction.setFilters(new InputFilter[]{new InputFilter.LengthFilter(introductionModel.getUpperLimit())});
            etIntroduction.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    tvIntroductionNum.setText(s.toString().length() + "");
                }
            });

            etIntroduction.setHint(introductionModel.getInputPrompt());
            if (dataSource != null && !TextUtils.isEmpty(dataSource.getDescription())) {
                etIntroduction.setText(dataSource.getDescription());
                etIntroduction.setSelection(dataSource.getDescription().length());
            }
            etIntroduction.setHint(introductionModel.getInputPrompt());
            llIntroduction.setTag(introductionModel);
        } else {
            llIntroduction.setVisibility(View.GONE);
        }
    }

    /**
     * 处理内容
     */
    private void processContent(EditorConfigTwoModel contentModel) {
        if (contentModel != null && contentModel.getEnabled()) {
            llContent.setVisibility(View.VISIBLE);
            llContentNum.setVisibility(View.VISIBLE);
            llContentText.setTag(contentModel);
            tvContentTextCount.setText("/" + contentModel.getUpperLimit());
            initContentEditText(contentModel);
        }
    }

    /**
     * 处理图片
     */
    private void processImg(EditorConfigTwoModel imgModel) {
        if (imgModel != null && imgModel.getEnabled()) {
            ivChoosePic.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.VISIBLE);
            llContent.setVisibility(View.VISIBLE);
            if (isPicText)
                llContentPicNum.setVisibility(View.VISIBLE);
            tvContentPicCount.setText("/" + imgModel.getUpperLimit());
            ivChoosePic.setTag(imgModel);
        }
    }

    /**
     * 处理匿名
     */
    private void processAnonymity(EditorConfigTwoModel anonymityModel) {
        if (anonymityModel != null && anonymityModel.getEnabled()) {
            cbAnonymity.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.VISIBLE);
            if (dataSource != null) {
                cbAnonymity.setChecked(dataSource.getFunctionType() == 1 ? true : false);
            }
            if (anonymityModel.getRequired()) {
                cbAnonymity.setChecked(true);
                cbAnonymity.setClickable(false);
            }
            cbVote.setTag(anonymityModel);
        } else {
            cbVote.setVisibility(View.GONE);
        }
    }

    /**
     * 处理投票
     */
    private void processVote(EditorConfigTwoModel voteModel) {
        if (voteModel != null && voteModel.getEnabled()) {
            cbVote.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.VISIBLE);
            if (dataSource != null) {
                cbVote.setChecked(dataSource.getFunctionType() == 2 ? true : false);
            }
            if (voteModel.getRequired()) {
                cbVote.setChecked(true);
                cbVote.setClickable(false);
            }
            if (cbVote.isChecked()) {
                setTitleRightText(R.string.next);
            } else {
                setTitleRightText(R.string.publish);
            }
            cbVote.setTag(voteModel);
        } else {
            cbVote.setVisibility(View.GONE);
        }
    }

    /**
     * 处理音频
     */
    private void processAudio(EditorConfigTwoModel audioModel) {
        if (audioModel != null && audioModel.getEnabled()) {
            ivChooseAudio.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.VISIBLE);
            llAudioNum.setVisibility(View.VISIBLE);
            tvAudioCount.setText("/" + audioModel.getUpperLimit());
            ivChooseAudio.setTag(audioModel);
        } else {
            ivChooseAudio.setVisibility(View.GONE);
        }
    }

    /**
     * 处理视频
     */
    private void processVideo(EditorConfigTwoModel videoModel) {
        if (videoModel != null && videoModel.getEnabled()) {
            ivChooseVideo.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.VISIBLE);
            llVideoNum.setVisibility(View.VISIBLE);
            tvVideoCount.setText("/" + videoModel.getUpperLimit());
            ivChooseVideo.setTag(videoModel);
            mPopupView = new PopupView(this);
            mPopupView.setOnItemPopupClick(new PopupView.OnItemPopupClick() {
                @Override
                public void OnItemClick(int position, String tag) {
                    if (position == 0) toVideoRecorder();
                    else
                        startActivityForResult(new Intent(mContext, VideoChooseActivity.class), VIDEO_PUBLISH_REQUEST);
                }
            });
        } else {
            ivChooseVideo.setVisibility(View.GONE);
        }
    }

    /**
     * 处理媒体文件部分控件
     */
    private void processMedia() {
        if (ivChooseAudio.getVisibility() == View.VISIBLE && ivChooseVideo.getVisibility() == View.VISIBLE) {
            tvMedia.setVisibility(View.VISIBLE);
            tvMedia.setText(getString(R.string.editor_two_show_video_audio_text));
        } else if (ivChooseAudio.getVisibility() == View.VISIBLE) {
            tvMedia.setVisibility(View.VISIBLE);
            tvMedia.setText(getString(R.string.editor_two_show_audio_text));
        } else if (ivChooseVideo.getVisibility() == View.VISIBLE) {
            tvMedia.setVisibility(View.VISIBLE);
            tvMedia.setText(getString(R.string.editor_two_show_video_text));
        }
    }

    private void processAuthority(EditorConfigTwoModel authModel) {
        if (authModel != null && authModel.getEnabled()) {
            tvAuthority.setVisibility(View.VISIBLE);
            tvAuthority.setTag(authModel.getData());
            dataSource.setAllowGeneralizeFlag(authModel.getData().getAllowGeneralizeFlag() == 1 ? 1 : 0);
            dataSource.setAllowShareFlag(authModel.getData().getAllowShareFlag() == 1 ? 1 : 0);
            dataSource.setContentPrice(authModel.getData().getContentPrice());
            dataSource.setCoterieId(authModel.getData().getCoterieId());
        } else {
            tvAuthority.setVisibility(View.GONE);
        }
    }

    private void showDateDialog() {
        mDialogYearMonthDay = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH_DAY)
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String format = dateFormat.format(new Date(millseconds));
                        tvTime.setText(format);
                    }
                })
                .setCyclic(false)
                .build();
        mDialogYearMonthDay.show(getSupportFragmentManager(), "year_month_day");
    }

    /**
     * 检查数据是否合格
     */
    private void checkData() {
        if (dataSource == null)
            dataSource = new EditorDataSourceTwoModel();
        dataSource.setImgUrl("");
        if (llPage.getVisibility() == View.VISIBLE) {//封面图片
            EditorConfigTwoModel pageModel = (EditorConfigTwoModel) llPage.getTag();
            if (pageModel.getRequired() && ivPage.getTag(R.id.iv_editor_two_page) == null) {
                Toasty.info(this, pageModel.getErrorPrompt()).show();
                return;
            }
        }
        if (llTitle.getVisibility() == View.VISIBLE) {//标题
            EditorConfigTwoModel titleModel = (EditorConfigTwoModel) llTitle.getTag();
            String titleStr = etTitle.getText().toString().trim();
            if (titleModel.getRequired() && TextUtils.isEmpty(titleStr)) {
                Toasty.info(this, titleModel.getErrorPrompt()).show();
                return;
            }
            if (!TextUtils.isEmpty(titleStr) && (titleStr.length() < titleModel.getLowerLimit() || titleStr.length() > titleModel
                    .getUpperLimit())) {
                Toasty.info(this, titleModel.getErrorPrompt()).show();
                return;
            }
            dataSource.setTitle(titleStr);
        }
        if (rlSort.getVisibility() == View.VISIBLE) {//分类
            EditorConfigTwoModel sortModel = (EditorConfigTwoModel) rlSort.getTag();
            if (sortModel.getRequired() && TextUtils.isEmpty(dataSource.getClassifyItemName())) {
                Toasty.info(this, sortModel.getErrorPrompt()).show();
                return;
            }
        }
        if (rlLocation.getVisibility() == View.VISIBLE) {//地点
            EditorConfigTwoModel locationModel = (EditorConfigTwoModel) rlLocation.getTag();
            String locationStr = tvLocation.getText().toString().trim();
            if (locationModel.getRequired() && TextUtils.isEmpty(locationStr)) {
                Toasty.info(this, locationModel.getErrorPrompt()).show();
                return;
            }
            if (locationStr.contains(" ")) {
                String[] locationSplit = locationStr.split(" ");
                if (locationSplit.length == 2) {
                    dataSource.setProvince(locationSplit[0]);
                    dataSource.setCity(locationSplit[1]);
                }
            } else {
                dataSource.setProvince(locationStr);
            }
        }
        if (rlTime.getVisibility() == View.VISIBLE) {//日期
            EditorConfigTwoModel timeModel = (EditorConfigTwoModel) rlTime.getTag();
            String timeStr = tvTime.getText().toString().trim();
            if (timeModel.getRequired() && TextUtils.isEmpty(timeStr)) {
                Toasty.info(this, timeModel.getErrorPrompt()).show();
                return;
            }
            dataSource.setDate(timeStr);
        }
        if (llLabel.getVisibility() == View.VISIBLE) {//标签
            EditorConfigTwoModel labelModel = (EditorConfigTwoModel) llLabel.getTag();
            String labelStr = etLabel.getText().toString().trim();
            if (labelModel.getRequired() && TextUtils.isEmpty(labelStr)) {
                Toasty.info(this, labelModel.getErrorPrompt()).show();
                return;
            }
            if (!TextUtils.isEmpty(labelStr) && (labelStr.length() < labelModel.getLowerLimit() || labelStr.length() > labelModel
                    .getUpperLimit())) {
                Toasty.info(this, labelModel.getErrorPrompt()).show();
                return;
            }
            dataSource.setLabel(labelStr);
        }
        if (llIntroduction.getVisibility() == View.VISIBLE) {//简介
            EditorConfigTwoModel introductionModel = (EditorConfigTwoModel) llIntroduction.getTag();
            String introductionStr = etIntroduction.getText().toString().trim();
            if (introductionModel.getRequired() && TextUtils.isEmpty(introductionStr)) {
                Toasty.info(this, introductionModel.getErrorPrompt()).show();
                return;
            }
            if (!TextUtils.isEmpty(introductionStr) && (introductionStr.length() < introductionModel.getLowerLimit() || introductionStr
                    .length() > introductionModel.getUpperLimit())) {
                Toasty.info(this, introductionModel.getErrorPrompt()).show();
                return;
            }
            dataSource.setDescription(introductionStr);
        }
        if (llContentText.getVisibility() == View.VISIBLE) {//详情
            EditorConfigTwoModel contentModel = (EditorConfigTwoModel) llContentText.getTag();
            if (contentModel != null) {
                if (contentModel.getRequired() && contentEditCount <= 0) {
                    Toasty.info(this, contentModel.getErrorPrompt()).show();
                    return;
                }
                if (contentEditCount != 0 && (contentEditCount < contentModel.getLowerLimit() || contentEditCount > contentModel
                        .getUpperLimit())) {
                    Toasty.info(this, contentModel.getErrorPrompt()).show();
                    return;
                }
            }
        }
        if (ivChoosePic.getVisibility() == View.VISIBLE) {//图片
            EditorConfigTwoModel picModel = (EditorConfigTwoModel) ivChoosePic.getTag();
            if (isPicText && (picModel.getRequired() && (contentImageCount < picModel.getLowerLimit() || contentImageCount > picModel
                    .getUpperLimit()))) {
                Toasty.info(this, picModel.getErrorPrompt()).show();
                return;
            }
            if (!isPicText && (picModel.getRequired() && (picList.size() < picModel.getLowerLimit() || picList.size() > picModel
                    .getUpperLimit()))) {
                Toasty.info(this, picModel.getErrorPrompt()).show();
                return;
            }

        }
        if (ivChooseVideo.getVisibility() == View.VISIBLE) {//视频
            EditorConfigTwoModel videoModel = (EditorConfigTwoModel) ivChooseVideo.getTag();
            if (videoModel.getRequired() && rlVideo.getVisibility() != View.VISIBLE) {
                Toasty.info(this, videoModel.getErrorPrompt()).show();
                return;
            }
        }
        if (ivChooseAudio.getVisibility() == View.VISIBLE) {//音频
            EditorConfigTwoModel audioModel = (EditorConfigTwoModel) ivChooseAudio.getTag();
            if (audioModel.getRequired() && rlAudio.getVisibility() != View.VISIBLE) {
                Toasty.info(this, audioModel.getErrorPrompt()).show();
                return;
            }
        }
        if (cbAnonymity.getVisibility() == View.VISIBLE) {//匿名
            dataSource.setFunctionType(cbAnonymity.isChecked() ? 1 : 0);
        }
        if (cbVote.getVisibility() == View.VISIBLE) {//投票
            dataSource.setFunctionType(cbVote.isChecked() ? 2 : 0);
        }
        if (contentEditCount <= 0 && contentImageCount <= 0 && picList.size() <= 0 && rlVideo.getVisibility() != View.VISIBLE && rlAudio
                .getVisibility() != View.VISIBLE) {
            Toasty.info(this, getString(R.string.editor_content_null)).show();
            return;
        }
        jsResult = new ArrayList<>();
        if (!NetUtils.isNetworkConnected(mContext)) {
            Toasty.error(mContext, mContext.getString(R.string.no_net_work)).show();
            return;
        }
        if (TextUtils.isEmpty(mVideoFilePath))
            publicUpload();
        else checkWifi();
    }

    private void publicUpload() {
        onLoadingStatus(CommonCode.General.DATA_LOADING, getString(R.string.editor_two_publish_ing));
        if (llPage.getVisibility() == View.VISIBLE)
            uploadPage();
        else
            uploadHtml();
    }

    /**
     * 上传封面图
     */
    private void uploadPage() {
        List<UploadPicManager.UploadInfo> uploadInfoList = new ArrayList<>();
        UploadPicManager.UploadInfo uploadInfo = new UploadPicManager.UploadInfo();
        uploadInfo.fileSavePath = (String) ivPage.getTag(R.id.iv_editor_two_page);
        uploadInfoList.add(uploadInfo);
        if (!TextUtils.isEmpty(uploadInfo.fileSavePath)) {
            UploadPicManager uploadPicManager = new UploadPicManager(new UploadPicManager.OnUploadCallback() {
                @Override
                public void onResult(boolean result, List<UploadPicManager.UploadInfo> resultList) {
                    if (result) {
                        for (int i = 0; i < resultList.size(); i++) {
                            UploadPicManager.UploadInfo uploadInfo = resultList.get(i);
                            dataSource.setCoverPlanUrl(uploadInfo.fileSavePath);
                        }
                        uploadHtml();
                    } else {
                        onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        Toasty.error(EditorTwoActivity.this, getString(R.string.editor_two_page_fail), Toast.LENGTH_SHORT, true).show();
                    }
                }
            });
            uploadPicManager.compressAndUploads(this, uploadInfoList, TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir);
        } else {
            uploadHtml();
        }

    }

    private void initPageImageView(String imgPath) {
        if (TextUtils.isEmpty(imgPath) || (!new File(imgPath).exists() && !imgPath.contains("http"))) {
            Toasty.info(mContext, getString(R.string.not_found_pic)).show();
            return;
        }
        tvPageAdd.setVisibility(View.GONE);
        ivPage.setVisibility(View.VISIBLE);
        tvPageChange.setVisibility(View.VISIBLE);
        ivPage.setTag(R.id.iv_editor_two_page, imgPath);
        if (Protect.checkLoadImageStatus(this)) {
            Glide.with(this).load(imgPath).into(ivPage);
        }
    }

    /**
     * 非图文混排item删除图片
     *
     * @param item
     */
    public void deletePic(String item) {
        if (picList.contains(item))
            picList.remove(picList.indexOf(item));
        picAdapter.notifyDataSetChanged();
        contentImageCount = picList.size();
        if (contentImageCount <= 0) {
            gvPic.setVisibility(View.GONE);
            if (rlAudio.getVisibility() != View.VISIBLE && rlVideo.getVisibility() != View.VISIBLE)
                llMediaRoot.setVisibility(View.GONE);
        }
    }

    /**
     * 处理非图文混排时的图片
     */
    private void initPic() {
        if (picAdapter == null) {
            picAdapter = new PicAdapter(this, R.layout.item_editor_two_pic);
            picAdapter.setData(picList);
            gvPic.setAdapter(picAdapter);
            gvPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // 因为单张图片时，图片实际大小是自适应的，imageLoader缓存时是按测量尺寸缓存的
                    ImagePagerActivity.imageSize = new ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                    ImagePagerActivity.isLocation = true;
                    Intent intent = new Intent(mContext, ImagePagerActivity.class);
                    intent.putStringArrayListExtra("imgurls", picList);
                    intent.putExtra("position", position);
                    startActivityForResult(intent, PIC_PUBLISH_REQUEST);
                }
            });
        }
        picAdapter.notifyDataSetChanged();
        contentImageCount = picList.size();
        gvPic.setVisibility(View.VISIBLE);
        llMediaRoot.setVisibility(View.VISIBLE);
        changeChooseImageStatus();
        changeChooseVideoStatus();
        changeChooseAudioStatus();
    }

    private void initContentEditText(EditorConfigTwoModel contentModel) {
        final EditText et = (EditText) getLayoutInflater().inflate(R.layout.layout_et_article_item, llContentText, false);
        et.setTextColor(ContextCompat.getColor(mContext, R.color.font_gray_xl));
        et.setHintTextColor(ContextCompat.getColor(mContext, R.color.font_gray_a));
        et.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_50));
        EditorConfigTwoModel model = (EditorConfigTwoModel) llContentText.getTag();
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isPicChange) {
                    contentEditCount = count - before + contentEditCount;
                    updateTextCount();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isPicChange)
                    processContentEditViewInputFilter(false);
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        et.setMinHeight((int) getResources().getDimension(R.dimen.px400));
        et.setLineSpacing(0, 1.3f);
        llContentText.addView(et, layoutParams);
        if (isFirstInput) {
            et.setHint(TextUtils.isEmpty(contentModel.getInputPrompt()) ? getString(R.string.add_text_pic_here) : contentModel.getInputPrompt());
            isFirstInput = false;
        }
//        et.requestFocus();
        if (model.getUpperLimit() == 0) {
            et.setEnabled(false);
        }
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(model.getUpperLimit())});
    }

    private EditText initContentEditText(int position) {
        final EditText et = (EditText) getLayoutInflater().inflate(R.layout.layout_et_article_item, llContentText, false);
        et.setTextColor(ContextCompat.getColor(mContext, R.color.font_gray_xl));
        et.setHintTextColor(ContextCompat.getColor(mContext, R.color.font_gray_a));
        et.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_50));
        EditorConfigTwoModel model = (EditorConfigTwoModel) llContentText.getTag();
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isPicChange) {
                    contentEditCount = count - before + contentEditCount;
                    updateTextCount();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isPicChange)
                    processContentEditViewInputFilter(false);
            }
        });
        et.setLineSpacing(0, 1.3f);
        llContentText.addView(et, position);
        if (isFirstInput) {
            et.setHint(R.string.add_text);
            isFirstInput = false;
        }
        if (model.getUpperLimit() == 0) {
            et.setEnabled(false);
        }
        return et;
    }

    /**
     * 处理EditText输入限制
     */
    private void processContentEditViewInputFilter(boolean isMax) {
        EditorConfigTwoModel model = (EditorConfigTwoModel) llContentText.getTag();
        for (int i = 0; i < llContentText.getChildCount(); i++) {
            View view = llContentText.getChildAt(i);
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                int max = model.getUpperLimit();
                if (!isMax)
                    max = model.getUpperLimit() - contentEditCount + editText.getText().toString().trim().length();
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
            }
        }
    }

    private void initContentImageView(final String imgPath) {
        Log.d("picture", "initImageView " + imgPath);
        Log.d("picture", "initImageView fileExist" + new File(imgPath).exists());
        if (TextUtils.isEmpty(imgPath) || (!new File(imgPath).exists() && !imgPath.contains("http"))) {
            Toasty.info(mContext, getString(R.string.not_found_pic)).show();
        }
        View view = getLayoutInflater().inflate(R.layout.layout_iv_article_item, llContentText, false);
        final ImageView iv = (ImageView) view.findViewById(R.id.id_iv);
        ImageView iv_delete = (ImageView) view.findViewById(R.id.id_iv_delete);
        iv_delete.setOnClickListener(this);

        int focusPos = getCurrentFocusPosition();
        View focusView = getCurrentFocus();
        if (focusPos != -1 && focusView instanceof EditText) {
            splitEditText((EditText) getCurrentFocus(), focusPos);
            llContentText.addView(view, focusPos + 1);
//            processImageEdit(focusPos + 1);
        } else {
            llContentText.addView(view);
//            initContentEditText(llContentText.getChildCount());
//            processImageEdit(llContentText.getChildCount() - 1);
        }

        iv.setTag(R.id.id_iv, imgPath);
        if (Protect.checkLoadImageStatus(this)) {
//            Glide.with(this).load(imgPath).into(new ViewTarget<ImageView, GlideDrawable>(iv) {
//                @Override
//                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                    iv.getLayoutParams().height = resource.getIntrinsicHeight() * (DensityUtils.getScreenW(EditorTwoActivity.this)) /
// resource.getIntrinsicWidth();
//                    iv.requestLayout();
//                    iv.setImageDrawable(resource.getCurrent());
//                }
//            });
            Glide.with(this).load(imgPath).placeholder(R.drawable.circle_default).error(R.drawable.circle_default).into(iv);
        }
        initImageCount();

    }

    private void processImageEdit(int position) {
        int childCount = llContentText.getChildCount();
        if (childCount > 4 && position < childCount) {//当有两张图片同时排列的时候处理
            if (llContentText.getChildAt(position - 1) instanceof EditText) {
                EditText childAt = (EditText) llContentText.getChildAt(position - 1);
                if (TextUtils.isEmpty(childAt.getText().toString()))
                    llContentText.removeView(childAt);
            }
        }
    }

    private void initContentAudioView() {
        if (!TextUtils.isEmpty(mAudioFilePath)) {
            llMediaRoot.setVisibility(View.VISIBLE);
            rlAudio.setVisibility(View.VISIBLE);
            tvAudioNum.setText(1 + "");
            String[] result = mAudioFilePath.split("/");
            mAudioFileName = result[result.length - 1];
            String[] s = mAudioFileName.split("\\.");
            if (s != null && s.length > 0) {
                mAudioFileName = UUID.randomUUID().toString() + "." + s[s.length - 1];
            }
            initDurationText();
        } else if (!TextUtils.isEmpty(mAudioUrl)) {
            llMediaRoot.setVisibility(View.VISIBLE);
            rlAudio.setVisibility(View.VISIBLE);
            tvAudioNum.setText(1 + "");
            if (audioDuration != 0) {
                tvAudioPlayTime.setText(audioDuration / 1000 + " \"");
            }
        } else {
            tvAudioNum.setText(0 + "");
            rlAudio.setVisibility(View.INVISIBLE);
        }
        changeChooseImageStatus();
        changeChooseVideoStatus();
        changeChooseAudioStatus();
    }

    private void initContentVideoView() {
        if (!TextUtils.isEmpty(mVideoFilePath)) {
            llMediaRoot.setVisibility(View.VISIBLE);
            rlVideo.setVisibility(View.VISIBLE);
            tvVideoNum.setText(1 + "");
            if (!TextUtils.isEmpty(mVideoImage)) {
                if (Protect.checkLoadImageStatus(this)) {
                    Glide.with(this).load(mVideoImage).into(ivVideoPreview);
                }
            } else {
                mBitmap = ImageUtils.getVideoThumbnail(mVideoFilePath);
                ivVideoPreview.setImageBitmap(mBitmap);
            }
            String[] result = mVideoFilePath.split("/");
            mVideoFileName = result[result.length - 1];
            String[] s = mVideoFileName.split("\\.");
            if (s != null && s.length > 0) {
                mVideoFileName = UUID.randomUUID().toString() + "." + s[s.length - 1];
            }
            initVideoDuration();
        } else if (!TextUtils.isEmpty(mVideoUrl)) {
            llMediaRoot.setVisibility(View.VISIBLE);
            rlVideo.setVisibility(View.VISIBLE);
            tvVideoNum.setText(1 + "");
            if (!TextUtils.isEmpty(mVideoImage)) {
                if (Protect.checkLoadImageStatus(this)) {
                    Glide.with(this).load(mVideoImage).into(ivVideoPreview);
                }
            }
        } else {
            tvVideoNum.setText(0 + "");
            rlVideo.setVisibility(View.GONE);
        }
        changeChooseImageStatus();
        changeChooseVideoStatus();
        changeChooseAudioStatus();

    }

    /**
     * 修改 音频可选状态
     */
    private void changeChooseAudioStatus() {
        if (isEmptyImageVideoAudio()) {
            ivChooseAudio.setImageResource(R.mipmap.icon_editor_audio);
            ivChooseAudio.setEnabled(true);
        } else {
            ivChooseAudio.setImageResource(R.mipmap.icon_editor_audio_gray);
            ivChooseAudio.setEnabled(false);
        }
    }

    /**
     * 修改 图片可选状态
     */
    private void changeChooseImageStatus() {
        if (!TextUtils.isEmpty(mVideoFilePath) || !TextUtils.isEmpty(mVideoUrl) || !TextUtils.isEmpty(mAudioFilePath) ||
                !TextUtils.isEmpty(mAudioUrl)) {
            ivChoosePic.setImageResource(R.mipmap.icon_editor_pic_gray);
            ivChoosePic.setEnabled(false);
        } else {
            if (ivChoosePic.getTag() == null) return;
            EditorConfigTwoModel imgModel = (EditorConfigTwoModel) ivChoosePic.getTag();
            if (contentImageCount >= imgModel.getUpperLimit() || picList.size() >= imgModel.getUpperLimit()) {
                ivChoosePic.setImageResource(R.mipmap.icon_editor_pic_gray);
                ivChoosePic.setEnabled(false);
            } else {
                ivChoosePic.setImageResource(R.mipmap.icon_editor_pic);
                ivChoosePic.setEnabled(true);
            }

        }

    }

    /**
     * 修改视频可选状态
     */
    private void changeChooseVideoStatus() {
        //用户选择图片 视频 音频
        if (isEmptyImageVideoAudio()) {
            ivChooseVideo.setImageResource(R.mipmap.icon_editor_video);
            ivChooseVideo.setEnabled(true);
        } else {
            ivChooseVideo.setImageResource(R.mipmap.icon_editor_video_gray);
            ivChooseVideo.setEnabled(false);
        }

    }

    /**
     * 判断 图片 视频 银屏 是否全为空
     *
     * @return
     */
    private boolean isEmptyImageVideoAudio() {
        if (contentImageCount == 0 && picList.size() == 0 && TextUtils.isEmpty(mVideoFilePath) && TextUtils.isEmpty(mVideoUrl) &&
                TextUtils.isEmpty(mAudioFilePath) && TextUtils.isEmpty(mAudioUrl)) {
            return true;
        }
        return false;
    }

    private void initImageCount() {
        contentImageCount = 0;
        int childCount = llContentText.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (llContentText.getChildAt(i) instanceof FrameLayout) {
                contentImageCount++;
            }
        }
        tvContentPicNum.setText(contentImageCount + "");
        changeChooseImageStatus();
        changeChooseVideoStatus();
        changeChooseAudioStatus();
        if (childCount > 0) {
            View childAt = llContentText.getChildAt(0);
            if (childAt instanceof EditText) {
                int minHeight = childCount == 1 ? R.dimen.px400 : R.dimen.px100;
                ((EditText) childAt).setMinHeight((int) getResources().getDimension(minHeight));
            }
        }
    }

    private void initDurationText() {
        initAudioDuration();
        tvAudioPlayTime.setText(audioDuration / 1000 + " \"");
    }

    private void initVideoDuration() {
        try {
            videoDuration = Long.parseLong(codeMediaInfo(mVideoFilePath));
            videoSize = new Double(FileUtils.getFileSize(mVideoFilePath, FileUtils.SIZETYPE_KB)).longValue();
            Log.d(TAG, "videoSize:" + videoSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAudioDuration() {
        try {
            audioDuration = Long.parseLong(codeMediaInfo(mAudioFilePath));
            audioSize = new Double(FileUtils.getFileSize(mAudioFilePath, FileUtils.SIZETYPE_KB)).longValue();
            Log.d(TAG, "audioSize:" + audioSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String codeMediaInfo(String url) throws Exception {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        Log.d(TAG, "str:" + url);
        mmr.setDataSource(url);
        String mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
        Log.d(TAG, "mime:" + mime);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
        Log.d(TAG, "duration:" + duration);


        mmr.release();
        return duration;
    }

    private boolean isPicChange = false;

    /**
     * 根据插入图片位置去分割editText内容
     *
     * @param editText
     * @param position
     */
    public void splitEditText(EditText editText, int position) {
        if (editText != null) {
            int start = editText.getSelectionStart();
            String contentStr = editText.getText().toString();
            int length = 0;
            if (!TextUtils.isEmpty(contentStr)) {
                length = contentStr.length();
            }
            isPicChange = true;
            if (length > start) {
                EditText newEditText = initContentEditText(position + 1);
                editText.setText(contentStr.substring(0, start));
                newEditText.setText(contentStr.substring(start, length));
                newEditText.requestFocus();
                newEditText.setSelection(0);
            } else {
                EditText otherEditText = initContentEditText(position + 1);
                otherEditText.requestFocus();
            }
            isPicChange = false;
            processContentEditViewInputFilter(false);
        }
    }

    public View getCurrentFocusEt() {
        return llContentText.getFocusedChild();
    }

    /**
     * 获得当前焦点所在position
     *
     * @return
     */
    public int getCurrentFocusPosition() {
        View focusView = getCurrentFocusEt();
        if (focusView != null) {
            for (int i = 0; i < llContentText.getChildCount(); i++) {
                if (focusView == llContentText.getChildAt(i)) {
                    return i;
                }
            }
        }
        return 0;
    }

    /**
     * 合并editText
     */
    private void mergeEditText() {
        EditText editText = null;
        isPicChange = true;
        processContentEditViewInputFilter(true);
        boolean selectionEnd = false;
        for (int i = 0; i < llContentText.getChildCount(); i++) {
            View view = llContentText.getChildAt(i);
            if (view instanceof EditText) {
                if (editText != null) {
                    EditText temp = (EditText) view;
                    String afterText = editText.getText().toString() + "";
                    editText.setText(editText.getText().toString() + temp.getText().toString());
                    llContentText.removeView(temp);
                    editText.requestFocus();
                    if (!selectionEnd) {
                        editText.setSelection(afterText.length());
                    } else {
                        editText.setSelection(editText.getText().toString().trim().length());
                        selectionEnd = true;
                    }

                    break;
                } else {
                    editText = (EditText) view;
                }
            } else {
                editText = null;
            }
        }
        isPicChange = false;
        processContentEditViewInputFilter(false);
    }

    /**
     * 检查视频文件
     *
     * @param videoPath
     * @return
     */
    private boolean checkVideoFile(String videoPath) {
        if (TextUtils.isEmpty(videoPath)) {
            Toasty.info(mContext, getString(R.string.not_found_video)).show();
            return false;
        }

        File file = new File(videoPath);
        if (file.exists()) {
            if (maxVideoSize < file.length()) {
                Toasty.info(mContext, getString(R.string.file_too_big)).show();
                return false;
            }
        } else {
            Toasty.info(mContext, getString(R.string.not_found_video)).show();
            return false;
        }
        return true;
    }

    private void updateTextCount() {
        tvContentTextNum.setText(contentEditCount + "");
    }

    private void resetAudio() {
        mAudioUrl = "";
        mAudioFilePath = "";
        mAudioFileName = "";
        audioDuration = 0;
    }

    private void resetVideo() {
        mVideoUrl = "";
        mVideoFilePath = "";
        mVideoImage = "";
        mVideoFileName = "";
        mBitmap = null;
    }

    private class ArticleItem {
        int type;
        String content;
    }

    private static class VideoEntity implements Serializable {
        public String url;
        public String thumbnailImage;
        public long time;
        public long size;

        @Override
        public String toString() {
            return "VideoEntity{" +
                    "url='" + url + '\'' +
                    ", thumbnailImage='" + thumbnailImage + '\'' +
                    '}';
        }
    }

    private static class AudioEntity implements Serializable {
        public String url;
        public long time;
        public long size;

        @Override
        public String toString() {
            return "AudioEntity{" +
                    "url='" + url + '\'' +
                    ", time=" + time +
                    '}';
        }

    }

    private void addTextImageJsContent() {
        for (int i = 0; i < articleItems.size(); i++) {
            ArticleItem articleItem = articleItems.get(i);
            if (articleItem.type == TYPE_IMAGE) {
                Map map = new HashMap();
                map.put(RULE_IMAGE, articleItem.content);
                jsResult.add(map);
                if (TextUtils.isEmpty(dataSource.getImgUrl()))
                    dataSource.setImgUrl(articleItem.content);
                else dataSource.setImgUrl(dataSource.getImgUrl() + "," + articleItem.content);
            } else {
                Map map = new HashMap();
                if (cbVote.getVisibility() == View.VISIBLE && cbVote.isChecked()) {//投票 走jsSdk
                    String text = articleItem.content.replaceAll("\\n", "<br>").replaceAll("\\r", "").replaceAll("\\t", " ");
                    map.put(RULE_TXT, text);
                    jsResult.add(map);
                    if (TextUtils.isEmpty(dataSource.getContent())) dataSource.setContent(text);
                    else dataSource.setContent(dataSource.getContent() + text);
                } else {//其他情况走后台接口 content:\n -> 空格 contentSource \n -> <br>
                    String contentText = articleItem.content.replaceAll("\\n", "").replaceAll("\\r", "").replaceAll("\\t", " ");
                    if (TextUtils.isEmpty(dataSource.getContent()))
                        dataSource.setContent(contentText);
                    else dataSource.setContent(dataSource.getContent() + contentText);
                    String contentSourceText = articleItem.content.replaceAll("\\n", "<br>").replaceAll("\\r", "").replaceAll("\\t", " ");
                    map.put(RULE_TXT, contentSourceText);
                    jsResult.add(map);
                }
            }
        }
    }

    private void addVideoJsContent(VideoEntity src) {
        Map map = new HashMap();
        map.put(RULE_VIDEO, src);
        jsResult.add(map);
        dataSource.setVideoUrl(src.url);
        dataSource.setVideoThumbnailUrl(src.thumbnailImage);
    }

    private void addAudioJsContent(AudioEntity src) {
        Map map = new HashMap();
        map.put(RULE_AUDIO, src);
        jsResult.add(map);
        dataSource.setAudioUrl(src.url);
    }

    private void showAudioImgAnim() {
        Glide.with(this).load(R.drawable.editor_audio_play_gif).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivVideoAudioPlay);
        Glide.with(this).load(R.drawable.editor_audio_play_gif).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .transform(new GlideCircleImage(this)).crossFade().into(ivVideoAudioPlay);
    }

    private void stopAudioImgAnim() {
        ivVideoAudioPlay.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.icon_editor_audio_play));
    }

    /**
     * 上传富文本 : 1.上传图片2.将图片文本记录3.上传视频4.上传视频图片5.上传音频
     */
    public void uploadHtml() {
        articleItems.clear();
        for (int i = 0; i < llContentText.getChildCount(); i++) {
            View view = llContentText.getChildAt(i);
            if (view instanceof EditText) {
                EditText et = (EditText) view;
                if (!TextUtils.isEmpty(et.getText())) {
                    ArticleItem articleItem = new ArticleItem();
                    articleItem.content = et.getText().toString();
                    articleItem.type = TYPE_TEXT;
                    articleItems.add(articleItem);
                }
            } else {
                View iv = view.findViewById(R.id.id_iv);
                ArticleItem articleItem = new ArticleItem();
                articleItem.content = (String) iv.getTag(R.id.id_iv);
                articleItem.type = TYPE_IMAGE;
                articleItems.add(articleItem);
            }
        }
        if (!isPicText) {
            for (String picPath : picList) {
                ArticleItem articleItem = new ArticleItem();
                articleItem.content = picPath;
                articleItem.type = TYPE_IMAGE;
                articleItems.add(articleItem);
            }
        }
        List<UploadPicManager.UploadInfo> uploadInfos = new ArrayList<>();
        for (int i = 0; i < articleItems.size(); i++) {
            ArticleItem articleItem = articleItems.get(i);
            if (articleItem.type == TYPE_IMAGE) {
                UploadPicManager.UploadInfo uploadInfo = new UploadPicManager.UploadInfo();
                uploadInfo.tag = i;
                uploadInfo.fileSavePath = articleItem.content;
                uploadInfos.add(uploadInfo);
            }
        }
        if (uploadInfos.size() > 0) {
            //oss上传图片
            uploadPicManager = new UploadPicManager(new UploadPicManager.OnUploadCallback() {
                @Override
                public void onResult(boolean result, List<UploadPicManager.UploadInfo> resultList) {
                    if (result) {
                        Log.d(TAG, "this is pic upload result resultList is " + resultList.toString());
                        for (int i = 0; i < resultList.size(); i++) {
                            UploadPicManager.UploadInfo uploadInfo = resultList.get(i);
                            articleItems.get((Integer) uploadInfo.tag).content = uploadInfo.fileSavePath;
                        }
                        if (jsResult == null) {
                            jsResult = new ArrayList<>();
                        }
                        addTextImageJsContent();
                        uploadVideo();
                    } else {
                        onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        Toasty.error(EditorTwoActivity.this, getString(R.string.editor_two_image_fail), Toast.LENGTH_SHORT, true).show();
                    }
                }
            });

            uploadPicManager.compressAndUploads(this, uploadInfos, TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir);
        } else {
            addTextImageJsContent();
            uploadVideo();
        }
    }

    /**
     * 上传视频图片
     */
    private void uploadVideoPic() {
        if (TextUtils.isEmpty(mVideoImage)) {
            if (mBitmap != null) {
                try {
                    File file = ImageUtils.saveFile(mBitmap, UUID.randomUUID().toString(), this);
                    mVideoImage = file.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!TextUtils.isEmpty(mVideoImage)) {
            if (!mVideoImage.contains("http")) {
                List<UploadPicManager.UploadInfo> uploadInfos = new ArrayList<>();
                UploadPicManager.UploadInfo uploadInfo = new UploadPicManager.UploadInfo();
                uploadInfo.fileSavePath = mVideoImage;
                uploadInfos.add(uploadInfo);
                uploadPicManager = new UploadPicManager(new UploadPicManager.OnUploadCallback() {
                    @Override
                    public void onResult(boolean result, List<UploadPicManager.UploadInfo> resultList) {
                        Log.d(TAG, "this is video pic upload result result is " + result);
                        if (result) {
                            mVideoImage = resultList.get(0).fileSavePath;
                            VideoEntity videoEntity = new VideoEntity();
                            videoEntity.url = mVideoUrl;
                            videoEntity.thumbnailImage = mVideoImage;
                            videoEntity.time = videoDuration;
                            videoEntity.size = videoSize;
                            addVideoJsContent(videoEntity);
                            uploadAudio();
                        } else {
                            onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                            Toasty.error(EditorTwoActivity.this, getString(R.string.editor_two_video_image_fail), Toast.LENGTH_SHORT,
                                    true).show();
                        }
                    }
                });
                uploadPicManager.compressAndUploads(this, uploadInfos, TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir);
            } else {
                VideoEntity videoEntity = new VideoEntity();
                videoEntity.url = mVideoUrl;
                videoEntity.thumbnailImage = mVideoImage;
                videoEntity.time = videoDuration;
                videoEntity.size = videoSize;
                addVideoJsContent(videoEntity);
                uploadAudio();
            }
        } else {
            //图片异常直接传空的图片
            VideoEntity videoEntity = new VideoEntity();
            videoEntity.url = mVideoUrl;
            addVideoJsContent(videoEntity);
            uploadAudio();
        }
    }

    /**
     * 上传视频
     */
    public void uploadVideo() {
        if (!TextUtils.isEmpty(mVideoFilePath)) {
            OssManager.uploadFile(mVideoFilePath, OssManager.VIDEO + (TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir) +
                    mVideoFileName, TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir, new
                    OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                        @Override
                        public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                            Log.d(TAG, "this is mVideoFilePath upload result result is " + putObjectResult.getServerCallbackReturnBody());
                            if (!TextUtils.isEmpty(putObjectResult.getRequestId()) && !TextUtils.isEmpty(putObjectResult.getETag())) {
                                if (jsResult == null) jsResult = new ArrayList<>();
                                mVideoUrl = OssManager.CDN + OssManager.VIDEO + (TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle :
                                        ossDir) + mVideoFileName;
                                uploadVideoPic();
                            } else {
                                onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                                Toasty.error(EditorTwoActivity.this, getString(R.string.video_upload_failed), Toast.LENGTH_SHORT, true)
                                        .show();
                            }

                        }

                        @Override
                        public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                            onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                            Toasty.error(EditorTwoActivity.this, getString(R.string.video_upload_failed), Toast.LENGTH_SHORT, true).show();
                        }
                    });
        } else {
            if (!TextUtils.isEmpty(mVideoUrl)) {
                VideoEntity videoEntity = new VideoEntity();
                videoEntity.url = mVideoUrl;
                if (!TextUtils.isEmpty(mVideoImage) && mVideoImage.contains("http")) {
                    videoEntity.thumbnailImage = mVideoImage;
                }
                if (videoDuration != 0) videoEntity.time = videoDuration;
                if (videoSize != 0) videoEntity.size = videoSize;
                addVideoJsContent(videoEntity);
            }
            uploadAudio();
        }
    }

    /**
     * 上传音频
     */
    public void uploadAudio() {
        if (!TextUtils.isEmpty(mAudioFilePath)) {
            OssManager.uploadFile(mAudioFilePath, OssManager.AUDIO + (TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir) +
                    mAudioFileName, TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir, new
                    OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                        @Override
                        public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                            Log.d(TAG, "this is mAudioFilePath upload result result is " + putObjectResult.getServerCallbackReturnBody());
                            if (!TextUtils.isEmpty(putObjectResult.getRequestId()) && !TextUtils.isEmpty(putObjectResult.getETag())) {
                                String OSS_HOST = OssManager.CDN;
                                if (jsResult == null) {
                                    jsResult = new ArrayList<>();
                                }
                                mAudioUrl = OSS_HOST + OssManager.AUDIO + (TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle :
                                        ossDir) +
                                        mAudioFileName;
                                AudioEntity audioEntity = new AudioEntity();
                                audioEntity.url = mAudioUrl;
                                audioEntity.time = audioDuration;
                                audioEntity.size = audioSize;
                                addAudioJsContent(audioEntity);
                                callJs();
                            } else {
                                onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                                Toasty.error(EditorTwoActivity.this, getString(R.string.editor_two_audio_fail), Toast.LENGTH_SHORT, true)
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                            onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                            Toasty.error(EditorTwoActivity.this, getString(R.string.editor_two_audio_fail), Toast.LENGTH_SHORT, true)
                                    .show();
                        }
                    });
        } else {
            if (!TextUtils.isEmpty(mAudioUrl)) {
                AudioEntity audioEntity = new AudioEntity();
                audioEntity.url = mAudioUrl;
                if (audioDuration != 0) audioEntity.time = audioDuration;
                if (audioSize != 0) audioEntity.size = audioSize;
                addAudioJsContent(audioEntity);
            }
            callJs();
        }
    }

    @AfterPermissionGranted(RC_VIDEO_AND_EXTENER)
    private void toVideoRecorder() {
        if (SystemUtils.isX86()) {
            return;
        }
        String[] perms = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            try {
                Intent intent = new Intent();
                intent.setAction("android.media.action.VIDEO_CAPTURE");
                intent.addCategory("android.intent.category.DEFAULT");
                File file = createMediaFile();
                videoFilePath = file.getAbsolutePath();
                if (file.exists()) {
                    file.delete();
                }
                Uri uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, SYSTEM_SHOOT_VIDEO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.sd_card_permissions_run), RC_VIDEO_AND_EXTENER, perms);
        }
    }

    /**
     * 生成音频文件
     *
     * @return
     * @throws IOException
     */
    private File createMediaFile() throws IOException {
        if (FileUtils.checkSDCardAvaliable()) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES), "CameraDemo");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create directory");
                    return null;
                }
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "VID_" + timeStamp;
            String suffix = ".mp4";
            File mediaFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
            return mediaFile;
        }
        return null;
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
    protected void onStop() {
        super.onStop();
        if (mRecord != null) {
            if (mRecord.isPlayIng()) {
                mRecord.pausePlay();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRecord != null && mRecord.isPlayIng()) {
            mRecord.stopPlay();
        }
    }

    /**
     * 通知js回调
     * 1.投票走jsSDK -> publish方法,且直接关闭页面
     * 2.发布或匿名发布走 -> 调用发布接口,给js回调,且关闭页面
     */
    private void callJs() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        dataSource.setContentSource(gson.toJson(jsResult));
        rootBean.setDataSource(dataSource);
        if (cbVote.getVisibility() == View.VISIBLE && cbVote.isChecked()) {//1.投票
            onLoadingStatus(CommonCode.General.DATA_SUCCESS);
            JsEvent.callJsEvent(rootBean, true);
            finish();
        } else {//2.发布
            try {
                if (httpRequestModel != null) {
                    processPublishData();
                    toPublish();
                } else publishFail("");
            } catch (JSONException e) {
                e.printStackTrace();
                publishFail("");
            }
        }

    }

    /**
     * 拼接发布数据
     */
    private void processPublishData() {
        Gson gson = new Gson();
        httpRequestModel.setBody(gson.toJson(dataSource));
    }

    /**
     * 调用发布
     *
     * @throws JSONException
     */
    private void toPublish() throws JSONException {
        HashMap<String, String> headerMap = httpRequestModel.getHeaders();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), httpRequestModel.getBody());
        Call<JsonObject> call = null;
        if (httpRequestModel.getMethod().equalsIgnoreCase("post")) {
            call = WebHttp.getWebService(QHApplication.getContext(), headerMap).reQuestNetPost(httpRequestModel.getUrl(), requestBody,
                    headerMap);
        } else if (httpRequestModel.getMethod().equalsIgnoreCase("get")) {
            Map bodyMap = getMap(httpRequestModel.getBody());
            if (bodyMap.isEmpty())
                call = WebHttp.getWebService(QHApplication.getContext(), headerMap).reQuestNetGet(httpRequestModel.getUrl(), headerMap);
            else
                call = WebHttp.getWebService(QHApplication.getContext(), headerMap).reQuestNetGetMap(httpRequestModel.getUrl(), bodyMap,
                        headerMap);
        } else if (httpRequestModel.getMethod().equalsIgnoreCase("put")) {
            call = WebHttp.getWebService(QHApplication.getContext(), headerMap).reQuestNetPut(httpRequestModel.getUrl(), requestBody,
                    headerMap);
        } else if (httpRequestModel.getMethod().equalsIgnoreCase("delete")) {
            call = WebHttp.getWebService(QHApplication.getContext(), headerMap).reQuestNetDelete(httpRequestModel.getUrl(), requestBody,
                    headerMap);
        } else if (httpRequestModel.getMethod().equalsIgnoreCase("patch")) {
            call = WebHttp.getWebService(QHApplication.getContext(), headerMap).reQuestNetPatch(httpRequestModel.getUrl(), requestBody,
                    headerMap);
        }


        CallManager.add(call);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                    String jsonStr = response.body().toString();
                    Gson gson = new Gson();
                    HashMap hashMap = gson.fromJson(jsonStr, HashMap.class);
                    if (hashMap.containsKey("code") && hashMap.get("code").equals("200")) {
                        Toasty.info(mContext, getString(R.string.editor_two_publish_success)).show();
                        Map data = (Map) hashMap.get("data");
                        if (data.containsKey("contentSource"))
                            data.remove("contentSource");
                        if (data.containsKey("content"))
                            data.remove("content");
                        if (data.containsKey("description"))
                            data.remove("description");
                        if (data.containsKey("title"))
                            data.remove("title");
                        hashMap.put("dataSource", data);
                        hashMap.remove("data");
                        JsEvent.callJsEvent(hashMap, true);
                        finish();
                    } else
                        publishFail(hashMap.containsKey("msg") ? (String) hashMap.get("msg") : "");
                } else {
                    publishFail("");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                publishFail("");
            }
        });
    }

    /**
     * 发布失败
     */
    private void publishFail(String msg) {
        onLoadingStatus(CommonCode.General.DATA_SUCCESS);
        Toasty.info(mContext, TextUtils.isEmpty(msg) ? getString(R.string.editor_two_publish_fail) : msg).show();
    }

    private Map getMap(String headerStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(headerStr);
        Iterator<String> keyIterator = jsonObject.keys();
        HashMap<String, String> headerMap = new HashMap<>();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            headerMap.put(key, jsonObject.getString(key));
        }
        return headerMap;
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    /**
     * 处理内容相关数据
     */
    private void processContentData() {
        ArrayList<Map<String, Object>> demoList = new ArrayList<>();
        String contentSource = dataSource.getContentSource();
        Gson gson = new Gson();
        ArrayList<Map<String, Object>> textList = gson.fromJson(contentSource, demoList.getClass());
        for (int i = 0; i < textList.size(); i++) {
            Map<String, Object> contentMap = textList.get(i);
            if (contentMap != null && contentMap.size() > 0) {
                for (Map.Entry<String, Object> contentEntry : contentMap.entrySet()) {
                    if (RULE_TXT.equalsIgnoreCase(contentEntry.getKey())) {
                        View lastView = llContentText.getChildAt(llContentText.getChildCount() - 1);
                        if (lastView != null && lastView instanceof EditText) {
                            EditText et = (EditText) lastView;
                            String s = (String) contentEntry.getValue();
                            if (!TextUtils.isEmpty(s)) {
                                et.setText(s.replaceAll("\\\\n", "\\n"));
                                et.setSelection(s.length());
                            }
                        }
                    } else if (RULE_IMAGE.equalsIgnoreCase(contentEntry.getKey())) {
                        if (isPicText) {
                            initContentImageView((String) contentEntry.getValue());
                        } else {
                            picList.add((String) contentEntry.getValue());
                        }
                    } else if (RULE_VIDEO.equalsIgnoreCase(contentEntry.getKey())) {
                        Map<String, Object> videoMap = (Map<String, Object>) contentEntry.getValue();
                        resetVideo();
                        if (videoMap.containsKey("url")) {
                            mVideoUrl = (String) videoMap.get("url");
                        }
                        if (videoMap.containsKey("thumbnailImage")) {
                            mVideoImage = (String) videoMap.get("thumbnailImage");
                        }
                        if (videoMap.containsKey("time")) {
                            Object time = videoMap.get("time");
                            if (time instanceof Double) {
                                videoDuration = ((Double) time).longValue();
                            } else {
                                videoDuration = (long) time;
                            }
                        }
                        if (videoMap.containsKey("size")) {
                            Object size = videoMap.get("size");
                            if (size instanceof Double) {
                                videoSize = ((Double) size).longValue();
                            } else {
                                videoSize = (long) size;
                            }
                        }
                        initContentVideoView();
                    } else if (RULE_AUDIO.equalsIgnoreCase(contentEntry.getKey())) {
                        Map<String, Object> audioMap = (Map<String, Object>) contentEntry.getValue();
                        resetAudio();
                        if (audioMap.containsKey("url")) {
                            mAudioUrl = (String) audioMap.get("url");
                        }
                        if (audioMap.containsKey("time")) {
                            Object time = audioMap.get("time");
                            if (time instanceof Double) {
                                audioDuration = ((Double) time).longValue();
                            } else {
                                audioDuration = (long) time;
                            }
                        }
                        if (audioMap.containsKey("size")) {
                            Object size = audioMap.get("size");
                            if (size instanceof Double) {
                                audioSize = ((Double) size).longValue();
                            } else {
                                audioSize = (long) size;
                            }
                        }
                        initContentAudioView();
                    }
                }
            }
        }
        if (!isPicText) {
            initPic();
        }
    }

    /**
     * 获得网速
     */
    private void getRxBytes() {
        if (videoSize > 500 * 1024) {
            Toasty.info(mContext, getString(R.string.upload_video_500)).show();
            return;
        }
        final long lastTotalRxBytes = NetWorkSpeedUtils.getTotalRxBytes();
        final long lastTimeStamp = System.currentTimeMillis();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                long nowTotalRxBytes = NetWorkSpeedUtils.getTotalRxBytes();
                long nowTimeStamp = System.currentTimeMillis();
                long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换
                Message msg = speedHandler.obtainMessage();
                msg.what = 100;
                msg.obj = speed;
                speedHandler.sendMessage(msg);
            }
        }, 500);//隔2s

    }

    Handler speedHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == 100) {
                long speed = msg.what;
                checkSpeed(speed);
            }
        }
    };

    private void checkSpeed(long speed) {
        Log.d(TAG, "speed = " + speed);
        CommonDialog commonDialog = new CommonDialog(mContext);
        if (speed > 0) {
            long time = videoSize / speed;//需要多少秒
            if (time > 60 * 1) {//大于1分钟给出提示
                commonDialog.showDialog("视频过大,可能需要" + time / 60 + "分钟上传,确定发布吗?", new CommonDialog.OnCommonDialogConfirmListener() {
                    @Override
                    public void onConfirmListener() {
                        publicUpload();
                    }
                });
            } else {
                publicUpload();
            }
        } else if (videoSize > 5000) {
            commonDialog.showDialog("视频过大,可能需要较长时间上传,确定发布吗?", new CommonDialog.OnCommonDialogConfirmListener() {
                @Override
                public void onConfirmListener() {
                    publicUpload();
                }
            });
        } else {
            //发布
            publicUpload();
        }

    }

    private void checkWifi() {
        if (NetworkUtil.isWifi(this)) {
            publicUpload();
        } else {
            CommonDialog commonDialog = new CommonDialog(mContext);
            commonDialog.showDialog(getString(R.string.wifi_upload), new CommonDialog.OnCommonDialogConfirmListener() {
                @Override
                public void onConfirmListener() {
                    publicUpload();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (SVProgressHUD.isShowing(this)) return;
        if (commonDialog == null)
            commonDialog = new CommonDialog(mContext);
        commonDialog.showDialog(R.string.editor_two_cancel, new CommonDialog.OnCommonDialogConfirmListener() {
            @Override
            public void onConfirmListener() {
                finish();
            }
        });
    }
}
