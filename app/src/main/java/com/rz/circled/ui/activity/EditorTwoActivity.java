package com.rz.circled.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.google.gson.Gson;
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
import com.rz.circled.widget.PopupView;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.oss.OssManager;
import com.rz.common.oss.UploadPicManager;
import com.rz.common.permission.AfterPermissionGranted;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.ui.view.CommonDialog;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.DensityUtils;
import com.rz.common.utils.FileUtils;
import com.rz.common.utils.ImageUtils;
import com.rz.common.utils.Protect;
import com.rz.common.utils.Record;
import com.rz.common.utils.SystemUtils;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.api.CallManager;
import com.rz.sgt.jsbridge.JsEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
public class EditorTwoActivity extends BaseActivity implements View.OnClickListener, PopupView.OnItemPopupClick {

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

    private EditorDataSourceTwoModel dataSource;

    public static final long maxVideoSize = 500 * 1024 * 1024;

    private int contentEditCount = 0;
    private int contentImageCount = 0;
    private boolean isFirstInput = true;

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
//    private AnimationDrawable animationDrawable;

    public String ossDir;

    public static final String RULE_TXT = "text";
    public static final String RULE_IMAGE = "image";
    public static final String RULE_VIDEO = "video";
    public static final String RULE_AUDIO = "audio";

    //    public static final long minMill = -3 * 1000 * 60 * 60 * 24L;
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


    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_editor_two, null);
    }

    @Override
    public void initView() {
        setTitleLeftText(R.string.cancel);
        setTitleText("");
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
//        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.audio_anim);
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
        parseIntent();
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.tv_editor_two_page_change, R.id.tv_editor_two_page_add, R.id.rl_editor_two_sort, R.id.rl_editor_two_location, R.id.rl_editor_two_time, R.id.iv_editor_two_choose_pic
            , R.id.iv_editor_two_choose_audio, R.id.iv_editor_two_choose_video, R.id.iv_editor_two_video_delete, R.id.iv_editor_two_audio_delete
            , R.id.iv_editor_two_video_icon, R.id.iv_editor_two_video_preview, R.id.iv_editor_two_audio_play, R.id.tv_editor_two_authority})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_editor_two_page_change:
            case R.id.tv_editor_two_page_add:
                Intent pageIntent = new Intent(this, PictureSelectedActivity.class);
                pageIntent.putExtra("index", 1);
                pageIntent.putExtra("isNeed", false);
                startActivityForResult(pageIntent, PIC_PAGE_CHANGE_REQUEST);
                break;
            case R.id.rl_editor_two_sort:
                EditorConfigTwoModel sortModel = (EditorConfigTwoModel) rlSort.getTag();
                if (sortModel.isEditable()) {
                    long currentId = -1;
                    if (dataSource != null)
                        currentId = dataSource.getClassifyItemId();
                    if (categoryBean != null && categoryBean.getData() != null && categoryBean.getData().size() > 0) {
                        Intent sortIntent = new Intent(this, EditorTwoSortActivity.class);
                        sortIntent.putExtra(IntentKey.EXTRA_ID, currentId);
                        sortIntent.putExtra(IntentKey.EXTRA_SERIALIZABLE, categoryBean.getData());
                        startActivityForResult(sortIntent, SORT_REQUEST);
                    }
                }
                break;
            case R.id.rl_editor_two_location:
                Intent locationIntent = new Intent(this, PersonAreaAty.class);
                locationIntent.putExtra(IntentKey.EXTRA_TYPE, TYPE_EDITOR);
                startActivityForResult(locationIntent, REQUEST_CODE);
                break;
            case R.id.rl_editor_two_time:
                showDateDialog();
                break;
            case R.id.iv_editor_two_choose_pic:
                EditorConfigTwoModel imgModel = (EditorConfigTwoModel) view.getTag();
                if (contentImageCount < imgModel.getUpperLimit()) {
                    Intent intent = new Intent(this, PictureSelectedActivity.class);
                    intent.putExtra("index", (imgModel.getUpperLimit() - contentImageCount));
                    intent.putExtra("isNeed", false);
                    startActivityForResult(intent, PIC_PUBLISH_REQUEST);
                } else {
                    Toasty.error(mContext, String.format(getString(R.string.max_size_choose_pic_hint), imgModel.getUpperLimit())).show();
                }
                break;
            case R.id.iv_editor_two_choose_audio:
                Intent audioIntent = new Intent(this, VoicePubActivity.class);
                audioIntent.putExtra(IntentKey.EXTRA_BOOLEAN, false);
                startActivityForResult(audioIntent, AUDIO_PUBLISH_REQUEST);
                break;
            case R.id.iv_editor_two_choose_video:
                mPopupView.showAtLocPop(llRoot, videoItems);
                break;
            case R.id.id_iv_delete:
                ViewGroup parent = (ViewGroup) view.getParent();
                llContentText.removeView(parent);
                mergeEditText();
                initImageCount();
                break;
            case R.id.iv_editor_two_video_delete:
                rlVideo.setVisibility(View.INVISIBLE);
                if (rlAudio.getVisibility() != View.VISIBLE && gvPic.getVisibility() != View.VISIBLE) {
                    llMediaRoot.setVisibility(View.GONE);
                }
                resetVideo();
                initContentVideoView();
                break;
            case R.id.iv_editor_two_audio_delete:
                rlAudio.setVisibility(View.INVISIBLE);
                if (rlVideo.getVisibility() != View.VISIBLE && gvPic.getVisibility() != View.VISIBLE) {
                    llMediaRoot.setVisibility(View.GONE);
                }
                resetAudio();
                initContentAudioView();
                break;
            case R.id.iv_editor_two_video_preview:
            case R.id.iv_editor_two_video_icon:
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
            case R.id.iv_editor_two_audio_play:
                if (mRecord != null) {
                    if (!TextUtils.isEmpty(mAudioFilePath)) {
                        mRecord.player(mAudioFilePath);
                    } else if (!TextUtils.isEmpty(mAudioUrl)) {
                        mRecord.player(mAudioUrl);
                    }
                }
                break;
            case R.id.tv_editor_two_authority:
                //跳转到选择权限页面
                EditorConfigTwoModel authModel = (EditorConfigTwoModel) view.getTag();
                Intent authorityIntent = new Intent(mContext, EditorTwoAuthorityActivity.class);
                authorityIntent.putExtra(IntentKey.EXTRA_SERIALIZABLE, authModel.getData());
                startActivityForResult(authorityIntent, AUTHORITY_REQUEST);
                break;
        }
    }

    @Override
    public void OnItemClick(int position, String tag) {
        switch (position) {
            case 0:
                toVideoRecorder(VIDEO_PUBLISH_REQUEST);
                break;
            case 1:
                Intent video = new Intent(this, VideoChooseActivity.class);
                startActivityForResult(video, VIDEO_PUBLISH_REQUEST);
                break;
            default:
                break;
        }
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
        if (resultCode == RESULT_CANCELED) {
//            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }

        if (requestCode == PIC_PUBLISH_REQUEST) {
            if (null == data) {
                return;
            }
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
            } else if (resultCode == PUBLISH_RESULT_CAMERA) {
                String path = data.getStringExtra("picture");
                if (isPicText) {
                    initContentImageView(path);
                } else {
                    picList.add(path);
                    initPic();
                }
            }
        } else if (requestCode == VIDEO_PUBLISH_REQUEST && resultCode == RESULT_OK) {
            if (null == data) {
                return;
            }
            String filePath = null;
            try {
                filePath = data.getStringExtra(IntentKey.EXTRA_PATH);
                checkVideoFile(filePath);
            } catch (Exception e) {
                e.printStackTrace();
                Toasty.info(mContext, getString(R.string.error_to_found_video)).show();
                return;
            }
            resetVideo();
            mVideoFilePath = filePath;
            initContentVideoView();
        } else if (requestCode == SYSTEM_SHOOT_VIDEO && requestCode == RESULT_OK) {
            mVideoFilePath = this.filePath;
            resetVideo();
            initContentVideoView();
        } else if (requestCode == RecorderContant.RECORDE_SHOW && resultCode == RESULT_OK) {
            RecordResult result = new RecordResult(data);
            //得到视频地址，和缩略图地址的数组，返回十张缩略图
            String[] thum = null;
            String filePath = null;
            filePath = result.getPath();
            thum = result.getThumbnail();
            result.getDuration();
            Log.d("videoTest", "视频路径:" + filePath + "图片路径:" + thum[0]);
            try {
                checkVideoFile(filePath);
                Log.d("videoTest", "视频路径:" + filePath + "图片路径:" + thum[0]);
            } catch (Exception e) {
                e.printStackTrace();
                Toasty.info(mContext, getString(R.string.error_to_found_video)).show();
                return;
            }
            resetVideo();
            mVideoFilePath = filePath;
            mVideoImage = thum[0];
            initContentVideoView();
        } else if (requestCode == AUDIO_PUBLISH_REQUEST) {
            if (null == data) {
                return;
            }
            resetAudio();
            mAudioFilePath = data.getStringExtra(IntentKey.EXTRA_PATH);
            initContentAudioView();
        } else if (requestCode == PIC_PAGE_CHANGE_REQUEST) {
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
        } else if (requestCode == REQUEST_CODE && data != null) {
            String stringArea = data.getStringExtra(IntentKey.EXTRA_POSITION);
            tvLocation.setText(stringArea.trim());
        } else if (requestCode == SORT_REQUEST) {
            if (data != null) {
                long currentId = data.getLongExtra(IntentKey.EXTRA_ID, -1);
                if (currentId > 0) {
                    if (dataSource == null)
                        dataSource = new EditorDataSourceTwoModel();
                    ArrayList<EditorCategoryTwoModel> sortList = categoryBean.getData();
                    for (EditorCategoryTwoModel categoryTwoModel : sortList) {
                        if (categoryTwoModel.getId() == currentId) {
                            dataSource.setClassifyItemId(categoryTwoModel.getId());
                            dataSource.setClassifyItemName(categoryTwoModel.getCategoryName());
                        }
                    }
                }
                if (dataSource != null)
                    tvSort.setText(dataSource.getClassifyItemName());
            }
        } else if (requestCode == AUTHORITY_REQUEST) {//权限
            if (data != null) {
                EditorAuthorityRootBean authorityRootBean = (EditorAuthorityRootBean) data.getSerializableExtra(IntentKey.EXTRA_SERIALIZABLE);
                dataSource.setAllowGeneralizeFlag(authorityRootBean.getAllowGeneralizeFlag() == 1 ? 1 : 0);
                dataSource.setAllowShareFlag(authorityRootBean.getAllowShareFlag() == 1 ? 1 : 0);
                dataSource.setContentPrice(authorityRootBean.getContentPrice());
                dataSource.setCoterieId(authorityRootBean.getCoterieId());
            }
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

        //音频
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
            ivSortArrow.setVisibility(sortModel.isEditable() ? View.VISIBLE : View.INVISIBLE);
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
            initContentEditText();
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
            mPopupView.setOnItemPopupClick(this);
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
//        int displayWidth = ScreenUtil.getDisplayWidth();
        int padding = (int) getResources().getDimension(R.dimen.px44);
//        int width = (displayWidth - padding * 3) / 2;
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, -1);
//        rlAudio.setLayoutParams(layoutParams);
//        layoutParams.setMargins(0, 0, padding, 0);
//        rlVideo.setLayoutParams(layoutParams);
    }

    private void processAuthority(EditorConfigTwoModel authModel) {
        if (authModel.getEnabled()) {
            tvAuthority.setVisibility(View.VISIBLE);
            tvAuthority.setTag(authModel);
        } else {
            tvAuthority.setVisibility(View.GONE);
        }
    }

    /**
     * 处理内容相关数据
     */
    private void processContentData() {
        ArrayList<Map<String, Object>> textList = dataSource.getContentSource();
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
//                .setMinMillseconds(System.currentTimeMillis() + minMill)
//                .setMaxMillseconds(System.currentTimeMillis() + maxMill)
//                .setMinMillseconds(minMill)
//                .setMaxMillseconds(System.currentTimeMillis() + maxMill)
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
                Toasty.normal(this, pageModel.getErrorPrompt()).show();
                return;
            }
        }
        if (llTitle.getVisibility() == View.VISIBLE) {//标题
            EditorConfigTwoModel titleModel = (EditorConfigTwoModel) llTitle.getTag();
            String titleStr = etTitle.getText().toString().trim();
            if (titleModel.getRequired() && TextUtils.isEmpty(titleStr)) {
                Toasty.normal(this, titleModel.getErrorPrompt()).show();
                return;
            }
            if (!TextUtils.isEmpty(titleStr) && (titleStr.length() < titleModel.getLowerLimit() || titleStr.length() > titleModel.getUpperLimit())) {
                Toasty.normal(this, titleModel.getErrorPrompt()).show();
                return;
            }
            dataSource.setTitle(titleStr);
        }
        if (rlSort.getVisibility() == View.VISIBLE) {//分类
            EditorConfigTwoModel sortModel = (EditorConfigTwoModel) rlSort.getTag();
            if (sortModel.getRequired() && TextUtils.isEmpty(dataSource.getClassifyItemName())) {
                Toasty.normal(this, sortModel.getErrorPrompt()).show();
                return;
            }
        }
        if (rlLocation.getVisibility() == View.VISIBLE) {//地点
            EditorConfigTwoModel locationModel = (EditorConfigTwoModel) rlLocation.getTag();
            String locationStr = tvLocation.getText().toString().trim();
            if (locationModel.getRequired() && TextUtils.isEmpty(locationStr)) {
                Toasty.normal(this, locationModel.getErrorPrompt()).show();
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
                Toasty.normal(this, timeModel.getErrorPrompt()).show();
                return;
            }
            dataSource.setDate(timeStr);
        }
        if (llLabel.getVisibility() == View.VISIBLE) {//标签
            EditorConfigTwoModel labelModel = (EditorConfigTwoModel) llLabel.getTag();
            String labelStr = etLabel.getText().toString().trim();
            if (labelModel.getRequired() && TextUtils.isEmpty(labelStr)) {
                Toasty.normal(this, labelModel.getErrorPrompt()).show();
                return;
            }
            if (!TextUtils.isEmpty(labelStr) && (labelStr.length() < labelModel.getLowerLimit() || labelStr.length() > labelModel.getUpperLimit())) {
                Toasty.normal(this, labelModel.getErrorPrompt()).show();
                return;
            }
            dataSource.setLabel(labelStr);
        }
        if (llIntroduction.getVisibility() == View.VISIBLE) {//简介
            EditorConfigTwoModel introductionModel = (EditorConfigTwoModel) llIntroduction.getTag();
            String introductionStr = etIntroduction.getText().toString().trim();
            if (introductionModel.getRequired() && TextUtils.isEmpty(introductionStr)) {
                Toasty.normal(this, introductionModel.getErrorPrompt()).show();
                return;
            }
            if (!TextUtils.isEmpty(introductionStr) && (introductionStr.length() < introductionModel.getLowerLimit() || introductionStr.length() > introductionModel.getUpperLimit())) {
                Toasty.normal(this, introductionModel.getErrorPrompt()).show();
                return;
            }
            dataSource.setDescription(introductionStr);
        }
        if (llContentText.getVisibility() == View.VISIBLE) {//详情
            EditorConfigTwoModel contentModel = (EditorConfigTwoModel) llContentText.getTag();
            if (contentModel != null) {
                if (contentModel.getRequired() && contentEditCount <= 0) {
                    Toasty.normal(this, contentModel.getErrorPrompt()).show();
                    return;
                }
                if (contentEditCount != 0 && (contentEditCount < contentModel.getLowerLimit() || contentEditCount > contentModel.getUpperLimit())) {
                    Toasty.normal(this, contentModel.getErrorPrompt()).show();
                    return;
                }
            }
        }
        if (ivChoosePic.getVisibility() == View.VISIBLE) {//图片
            EditorConfigTwoModel picModel = (EditorConfigTwoModel) ivChoosePic.getTag();
            if (isPicText && (picModel.getRequired() && (contentImageCount < picModel.getLowerLimit() || contentImageCount > picModel.getUpperLimit()))) {
                Toasty.normal(this, picModel.getErrorPrompt()).show();
                return;
            }
            if (!isPicText && (picModel.getRequired() && (picList.size() < picModel.getLowerLimit() || picList.size() > picModel.getUpperLimit()))) {
                Toasty.normal(this, picModel.getErrorPrompt()).show();
                return;
            }

        }
        if (ivChooseVideo.getVisibility() == View.VISIBLE) {//视频
            EditorConfigTwoModel videoModel = (EditorConfigTwoModel) ivChooseVideo.getTag();
            if (videoModel.getRequired() && rlVideo.getVisibility() != View.VISIBLE) {
                Toasty.normal(this, videoModel.getErrorPrompt()).show();
                return;
            }
        }
        if (ivChooseAudio.getVisibility() == View.VISIBLE) {//音频
            EditorConfigTwoModel audioModel = (EditorConfigTwoModel) ivChooseAudio.getTag();
            if (audioModel.getRequired() && rlAudio.getVisibility() != View.VISIBLE) {
                Toasty.normal(this, audioModel.getErrorPrompt()).show();
                return;
            }
        }
        if (cbAnonymity.getVisibility() == View.VISIBLE) {//匿名
//            EditorConfigTwoModel anonymityModel = (EditorConfigTwoModel) cbAnonymity.getTag();
            dataSource.setFunctionType(cbAnonymity.isChecked() ? 1 : 0);
        }
        if (cbVote.getVisibility() == View.VISIBLE) {//投票
//            EditorConfigTwoModel voteModel = (EditorConfigTwoModel) cbVote.getTag();
            dataSource.setFunctionType(cbVote.isChecked() ? 2 : 0);
        }
        if (contentEditCount <= 0 && contentImageCount <= 0 && picList.size() <= 0 && rlVideo.getVisibility() != View.VISIBLE && rlAudio.getVisibility() != View.VISIBLE) {
            Toasty.normal(this, getString(R.string.editor_content_null)).show();
            return;
        }
        jsResult = new ArrayList<>();
//        if (!NetUtils.isNetworkConnected(this)) {
//            onLoadingStatus(CommonCode.General.UN_NETWORK, getString(R.string.status_un_network));
//            return;
//        }
//        MaterialDialog materialDialog = showProgressDialog("正在处理中");
//        materialDialog.setCanceledOnTouchOutside(false);
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
//            Glide.with(this).load(imgPath).into(ivPage);
            if (imgPath.startsWith("http"))
                ivPage.setImageURI(Uri.parse(imgPath));
            else
                ivPage.setImageURI(Uri.fromFile(new File(imgPath)));
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
    }

    private void initContentEditText() {
        final EditText et = (EditText) getLayoutInflater().inflate(R.layout.layout_et_article_item, llContentText, false);
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
        et.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        llContentText.addView(et);
        if (isFirstInput) {
            et.setHint(R.string.add_text_pic_here);
            isFirstInput = false;
        }
        et.requestFocus();
        if (model.getUpperLimit() == 0) {
            et.setEnabled(false);
        }
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(model.getUpperLimit())});
    }

    private EditText initContentEditText(int position) {
        final EditText et = (EditText) getLayoutInflater().inflate(R.layout.layout_et_article_item, llContentText, false);
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
        final SimpleDraweeView iv = (SimpleDraweeView) view.findViewById(R.id.id_iv);
        ImageView iv_delete = (ImageView) view.findViewById(R.id.id_iv_delete);
        iv_delete.setOnClickListener(this);

        int focusPos = getCurrentFocusPosition();
        View focusView = getCurrentFocus();
        if (focusPos != -1 && focusView instanceof EditText) {
            splitEditText((EditText) getCurrentFocus(), focusPos);
            llContentText.addView(view, focusPos + 1);
        } else {
            llContentText.addView(view);
        }

        iv.setTag(R.id.id_iv, imgPath);
        if (Protect.checkLoadImageStatus(this)) {
            // 需要使用 ControllerBuilder 方式请求图片
            final PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            if (imgPath.startsWith("http"))
                controller.setUri(Uri.parse(imgPath));
            else
                controller.setUri(Uri.fromFile(new File(imgPath)));
            controller.setOldController(iv.getController());

// 需要设置 ControllerListener，获取图片大小后，传递给 PhotoDraweeView 更新图片长宽
            controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    iv.getLayoutParams().height = imageInfo.getHeight() * (DensityUtils.getScreenW(EditorTwoActivity.this)) / imageInfo.getWidth();
                    iv.requestLayout();
                    iv.setController(controller.build());
                }
            });
        }
        initImageCount();
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
    }

    private void initContentVideoView() {
        if (!TextUtils.isEmpty(mVideoFilePath)) {
            llMediaRoot.setVisibility(View.VISIBLE);
            rlVideo.setVisibility(View.VISIBLE);
            tvVideoNum.setText(1 + "");
            if (!TextUtils.isEmpty(mVideoImage)) {
                if (Protect.checkLoadImageStatus(this)) {
//                    Glide.with(this).load(mVideoImage).into(ivVideoPreview);
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
//                    Glide.with(this).load(mVideoImage).into(ivVideoPreview);
                }
            }
        } else {
            tvVideoNum.setText(0 + "");
            rlVideo.setVisibility(View.GONE);
        }
    }

    private void initImageCount() {
        contentImageCount = 0;
        for (int i = 0; i < llContentText.getChildCount(); i++) {
            if (llContentText.getChildAt(i) instanceof FrameLayout) {
                contentImageCount++;
            }
        }
        tvContentPicNum.setText(contentImageCount + "");
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
        return -1;
    }

    /**
     * 合并editText
     */
    private void mergeEditText() {
        EditText editText = null;
        isPicChange = true;
        processContentEditViewInputFilter(true);
        for (int i = 0; i < llContentText.getChildCount(); i++) {
            View view = llContentText.getChildAt(i);
            if (view instanceof EditText) {
                if (editText != null) {
                    EditText temp = (EditText) view;
                    editText.setText(editText.getText().toString() + temp.getText().toString());
                    llContentText.removeView(temp);
//                    contentEditCount = contentEditCount - temp.getText().length();
//                    updateTextCount();
                    editText.requestFocus();
                    editText.setSelection(editText.getText().length());
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
                    String text = articleItem.content.replaceAll("\\n", "\\\\n").replaceAll("\\r", "").replaceAll("\\t", " ");
                    map.put(RULE_TXT, text);
                    jsResult.add(map);
                    if (TextUtils.isEmpty(dataSource.getContent())) dataSource.setContent(text);
                    else dataSource.setContent(dataSource.getContent() + text);
                } else {//其他情况走后台接口 content:\n -> 空格 contentSource \n -> <br>
                    String contentText = articleItem.content.replaceAll("\\n", "").replaceAll("\\r", "").replaceAll("\\t", " ");
                    if (TextUtils.isEmpty(dataSource.getContent())) dataSource.setContent(contentText);
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
//        Glide.with(this).load(R.drawable.editor_audio_play_gif).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivVideoAudioPlay);
//        Glide.with(this).load(R.drawable.editor_audio_play_gif).diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .transform(new GlideCircleImage(this)).crossFade().into(ivVideoAudioPlay);
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
                    Log.d("yeying", "this is pic upload result result is " + result);
                    if (result) {
                        Log.d("yeying", "this is pic upload result resultList is " + resultList.toString());
                        for (int i = 0; i < resultList.size(); i++) {
                            UploadPicManager.UploadInfo uploadInfo = resultList.get(i);
                            articleItems.get((Integer) uploadInfo.tag).content = uploadInfo.fileSavePath;
                        }
                        if (jsResult == null) {
                            jsResult = new ArrayList<>();
                        }
                        Log.d("yeying", "this is pic upload result 1 ");
                        addTextImageJsContent();
                        Log.d("yeying", "this is pic upload result 2 ");
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
                        Log.d("yeying", "this is video pic upload result result is " + result);
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
                            Toasty.error(EditorTwoActivity.this, getString(R.string.editor_two_video_image_fail), Toast.LENGTH_SHORT, true).show();
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
        Log.d("yeying", "uploadVideo ");
        if (!TextUtils.isEmpty(mVideoFilePath)) {
            OssManager.uploadFile(mVideoFilePath, OssManager.VIDEO + (TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir) + mVideoFileName, TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                    Log.d("yeying", "this is mVideoFilePath upload result result is " + putObjectResult.getServerCallbackReturnBody());
                    if (!TextUtils.isEmpty(putObjectResult.getRequestId()) && !TextUtils.isEmpty(putObjectResult.getETag())) {
//                        String OSS_HOST = "http://" + OssManager.bucketName + "." + OssManager.endpoint + "/";
                        String OSS_HOST = OssManager.CDN;
                        if (jsResult == null) {
                            jsResult = new ArrayList<>();
                        }
                        mVideoUrl = OSS_HOST + OssManager.VIDEO + (TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir) + mVideoFileName;
                        uploadVideoPic();
                    } else {
                        onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        Toasty.error(EditorTwoActivity.this, getString(R.string.video_upload_failed), Toast.LENGTH_SHORT, true).show();
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
            OssManager.uploadFile(mAudioFilePath, OssManager.AUDIO + (TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir) + mAudioFileName, TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                    Log.d("yeying", "this is mAudioFilePath upload result result is " + putObjectResult.getServerCallbackReturnBody());
                    if (!TextUtils.isEmpty(putObjectResult.getRequestId()) && !TextUtils.isEmpty(putObjectResult.getETag())) {
                        String OSS_HOST = OssManager.CDN;
                        if (jsResult == null) {
                            jsResult = new ArrayList<>();
                        }
                        mAudioUrl = OSS_HOST + OssManager.AUDIO + (TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir) + mAudioFileName;
//                        jsResult.add(getAudioHtml(mAudioUrl));
                        AudioEntity audioEntity = new AudioEntity();
                        audioEntity.url = mAudioUrl;
                        audioEntity.time = audioDuration;
                        audioEntity.size = audioSize;
                        addAudioJsContent(audioEntity);
                        callJs();
                    } else {
                        onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        Toasty.error(EditorTwoActivity.this, getString(R.string.editor_two_audio_fail), Toast.LENGTH_SHORT, true).show();
                    }

                }

                @Override
                public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                    onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                    Toasty.error(EditorTwoActivity.this, getString(R.string.editor_two_audio_fail), Toast.LENGTH_SHORT, true).show();
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

    private String filePath;

    @AfterPermissionGranted(RC_VIDEO_AND_EXTENER)
    private void toVideoRecorder(int requestCode) {
        if (SystemUtils.isX86()) {
            return;
        }
        String[] perms = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            try {
                Intent intent = new Intent();
                intent.setAction("android.media.action.VIDEO_CAPTURE");
                intent.addCategory("android.intent.category.DEFAULT");
                File file = createMediaFile();
                filePath = file.getAbsolutePath();
                if (file.exists()) {
                    file.delete();
                }
                Uri uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, SYSTEM_SHOOT_VIDEO);
            } catch (IOException e) {
                Toasty.error(mContext, getString(R.string.get_audio_fail_two));
                e.printStackTrace();
            }
        } else {
            EasyPermissions.requestPermissions(this, "悠然一指要使用摄像头,读取手机状态,使用sd卡和录音权限", RC_VIDEO_AND_EXTENER, perms);
        }
    }

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
            // Create an image file name
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
        EventBus.getDefault().unregister(this);
    }


    /**
     * 通知js回调
     * 1.投票走jsSDK -> publish方法,且直接关闭页面
     * 2.发布或匿名发布走 -> 调用发布接口,给js回调,且关闭页面
     */
    private void callJs() {
        dataSource.setContentSource(jsResult);
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
                } else {
                    publishFail();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                publishFail();
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
        if (headerMap.containsKey("userId")) {
            String userId = headerMap.get("userId");
            int id = (int) Double.parseDouble(userId);
            headerMap.put("userId", id + "");
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), httpRequestModel.getBody());
        Call<JsonObject> call = null;
        if (httpRequestModel.getMethod().equalsIgnoreCase("post")) {
            call = WebHttp.getWebService(QHApplication.getContext(), headerMap).reQuestNetPost(httpRequestModel.getUrl(), requestBody, headerMap);
        } else if (httpRequestModel.getMethod().equalsIgnoreCase("get")) {
            Map bodyMap = getMap(httpRequestModel.getBody());
            if (bodyMap.isEmpty())
                call = WebHttp.getWebService(QHApplication.getContext(), headerMap).reQuestNetGet(httpRequestModel.getUrl(), headerMap);
            else
                call = WebHttp.getWebService(QHApplication.getContext(), headerMap).reQuestNetGetMap(httpRequestModel.getUrl(), bodyMap, headerMap);
        } else if (httpRequestModel.getMethod().equalsIgnoreCase("put")) {
            call = WebHttp.getWebService(QHApplication.getContext(), headerMap).reQuestNetPut(httpRequestModel.getUrl(), requestBody, headerMap);
        } else if (httpRequestModel.getMethod().equalsIgnoreCase("delete")) {
            call = WebHttp.getWebService(QHApplication.getContext(), headerMap).reQuestNetDelete(httpRequestModel.getUrl(), requestBody, headerMap);
        } else if (httpRequestModel.getMethod().equalsIgnoreCase("patch")) {
            call = WebHttp.getWebService(QHApplication.getContext(), headerMap).reQuestNetPatch(httpRequestModel.getUrl(), requestBody, headerMap);
        }


        CallManager.add(call);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                    String jsonStr = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if (jsonObject.has("code")) {
                            String code = jsonObject.getString("code");
                            if (code.equals("200")) {
                                Toasty.info(EditorTwoActivity.this, getString(R.string.editor_two_publish_success)).show();
                                HashMap<String, EditorDataSourceTwoModel> map = new HashMap<String, EditorDataSourceTwoModel>();
                                map.put("dataSource", dataSource);
                                JsEvent.callJsEvent(map, true);
                                finish();
                            } else publishFail();
                        } else publishFail();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        publishFail();
                    }
                } else {
                    publishFail();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                publishFail();
            }
        });
    }

    /**
     * 发布失败
     */
    private void publishFail() {
        onLoadingStatus(CommonCode.General.DATA_SUCCESS);
        Toasty.info(EditorTwoActivity.this, getString(R.string.editor_two_publish_fail)).show();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (isFinishing()) return;
//        if (baseEvent.key.equals("publishResultCode")) {
//            dismissProgress();
//            int code = (int) baseEvent.event;
//            if (code == 200) {
//                Toasty.info(this, "发布成功").show();
//                finish();
//            } else if (code == RequestParamsObject.RESULT_CODE_CANCEL) {
//                Toasty.info(this, "正在发布中....").show();
//            } else {
//                Toasty.error(this, TextUtils.isEmpty(baseEvent.infoId) ? "发布失败" : baseEvent.infoId).show();
//            }
//        }
//        if (baseEvent.key.equals("publishBefore")) {
//            int code = (int) baseEvent.event;
//            if (code == RequestParamsObject.RESULT_CODE_FAILED) {
//                SVProgressHUD.showErrorWithStatus(aty, baseEvent.infoId);
//            } else if (code == RequestParamsObject.RESULT_CODE_SUCRESS) {
//                //发布成功
//                SVProgressHUD.showInfoWithStatus(aty, baseEvent.infoId);
//                Object event = baseEvent.event;
//                JsEvent.callJsEvent(event, true);
//                finish();
//            }
//        }
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }
}