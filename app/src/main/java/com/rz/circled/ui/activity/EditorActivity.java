package com.rz.circled.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.rz.circled.R;
import com.rz.circled.js.EditorHandler;
import com.rz.circled.recorder.RecordResult;
import com.rz.circled.recorder.RecorderContant;
import com.rz.circled.widget.CustomProgressbar;
import com.rz.circled.widget.PopupView;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.oss.OssManager;
import com.rz.common.oss.UploadPicManager;
import com.rz.common.permission.AfterPermissionGranted;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.FileUtils;
import com.rz.common.utils.ImageUtils;
import com.rz.common.utils.Protect;
import com.rz.common.utils.Record;
import com.rz.common.utils.SystemUtils;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.bean.OpusTag;
import com.rz.sgt.jsbridge.JsEvent;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：Administrator on 2016/8/17 0017 17:29
 * 功能：发布图文
 * 说明：
 */
public class EditorActivity extends BaseActivity implements View.OnClickListener, PopupView.OnItemPopupClick {

    //    流式布局
//    @BindView(R.id.id_public_label_flow)
//    ScrollFlowLayout mFlowLayout;
    @BindView(R.id.id_vg_container)
    LinearLayout idVgContainer;

    //    @BindView(R.id.id_layout_tags)
//    LinearLayout idLayoutTags;
    @BindView(R.id.id_tv_describe_count)
    TextView idTvDescribeCount;

    @BindView(R.id.id_tv_img_count)
    TextView idTvImgCount;
    @BindView(R.id.id_iv_video_icon)
    ImageView idIvVideoIcon;
    @BindView(R.id.iv_video_preview)
    ImageView ivVideoPreview;
    @BindView(R.id.iv_video_delete)
    ImageView ivVideoDelete;
    @BindView(R.id.id_video_layout)
    FrameLayout idVideoLayout;
    @BindView(R.id.iv_audio_delete)
    ImageView ivAudioDelete;
    @BindView(R.id.id_audio_layout)
    RelativeLayout idAudioLayout;
    @BindView(R.id.id_media_layout)
    LinearLayout idMediaLayout;
    @BindView(R.id.id_public_choose_video_btn)
    ImageView idPublicChooseVideoBtn;
    @BindView(R.id.id_public_choose_audio_btn)
    ImageView idPublicChooseAudioBtn;
    @BindView(R.id.id_public_choose_pic_btn)
    View idPublicChoosePicBtn;
    @BindView(R.id.id_public_root_ll)
    LinearLayout idPublicRootLl;
    @BindView(R.id.id_audio_play_time_txt)
    TextView idAudioPlayTimeTxt;
    @BindView(R.id.id_audio_play_img)
    ImageView idAudioPlayImg;
    @BindView(R.id.id_audio_play_txt)
    TextView idAudioPlayTxt;
    @BindView(R.id.id_audio_text)
    TextView idAudioText;
    @BindView(R.id.id_video_text)
    TextView idVideoText;

    List<OpusTag> opusTags = new ArrayList<>();
    @BindView(R.id.id_et_max)
    TextView idEtMax;
    @BindView(R.id.id_img_max)
    TextView idImgMax;
    @BindView(R.id.layout_audio_count)
    LinearLayout layoutAudioCount;
    @BindView(R.id.layout_video_count)
    LinearLayout layoutVideoCount;
    @BindView(R.id.id_audio_publish_progress)
    CustomProgressbar idAudioPublishProgress;
    @BindView(R.id.id_audio_listen_ll)
    RelativeLayout idAudioListenLl;
    @BindView(R.id.layout_image_count)
    LinearLayout layoutImageCount;
    private String temp_imagPath;
    private int padding = 0;

    private UploadPicManager uploadPicManager;

    private int selectPos = -1;

    private int type;

    //视频
    private String[] videos = {"拍摄视频", "从手机相册中选取"};

    //音频
    private String[] audios = {"录制音频", "从手机中选取"};

    private PopupView mPopupView;

    protected static final int RC_VIDEO_AND_EXTENER = 127;

    private List<ArticleItem> articleItems = new ArrayList<>();


    private String mVideoUrl;
    private String mVideoFilePath;
    private String mVideoFileName;
    private String mVideoImage;
    private Bitmap mBitmap;
    private long videoDuration;
    private long videoSize;

    public final int SYSTEM_SHOOT_VIDEO = 1221;

    private String mAudioUrl;
    private String mAudioFilePath;
    private String mAudioFileName;
    private long audioDuration;
    private long audioSize;

    private Record mRecord;
    private AnimationDrawable animationDrawable;

    public static final String RULE_TXT = "text";
    public static final String RULE_IMAGE = "image";
    public static final String RULE_VIDEO = "video";
    public static final String RULE_AUDIO = "audio";

    private List<Map<String, Object>> jsResult = new ArrayList<>();

    private int txtMaxNum;
    private int imgMaxNum;

    private boolean enableAudio;
    private boolean enableImage;
    private boolean enableVideo;

    public String ossDir;

    @Override
    public void refreshPage() {

    }


    public static class VideoEntity implements Serializable {
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

    public static class AudioEntity implements Serializable {
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

//    private JsResult jsResult;
//
//    public class JsResult {
//        private String videoUrl;
//        private String audioUrl;
//        private String imageText;
//
//        @Override
//        public String toString() {
//            return "JsResult{" +
//                    "videoUrl='" + videoUrl + '\'' +
//                    ", audioUrl='" + audioUrl + '\'' +
//                    ", imageText='" + imageText + '\'' +
//                    '}';
//        }
//    }

    public void parseIntent() {
        List<Map<String, Object>> editorThings = (List<Map<String, Object>>) getIntent().getSerializableExtra("map");
        txtMaxNum = getIntent().getIntExtra(IntentKey.EXTRA_TEXT_MAX, 10000);
        imgMaxNum = getIntent().getIntExtra(IntentKey.EXTRA_IMAGE_MAX, 20);
        enableImage = getIntent().getBooleanExtra(IntentKey.EXTRA_ENABLE_IMAGE, true);
        enableAudio = getIntent().getBooleanExtra(IntentKey.EXTRA_ENABLE_AUDIO, true);
        enableVideo = getIntent().getBooleanExtra(IntentKey.EXTRA_ENABLE_VIDEO, true);

        if (editorThings == null) {
            return;
        }

        if (!enableAudio && !enableVideo) {
            idMediaLayout.setVisibility(View.GONE);
        } else {
            idMediaLayout.setVisibility(View.VISIBLE);
        }

        idPublicChoosePicBtn.setVisibility(enableImage ? View.VISIBLE : View.GONE);
        idPublicChooseAudioBtn.setVisibility(enableAudio ? View.VISIBLE : View.GONE);
        idPublicChooseVideoBtn.setVisibility(enableVideo ? View.VISIBLE : View.GONE);
        layoutImageCount.setVisibility(enableImage ? View.VISIBLE : View.GONE);
        layoutAudioCount.setVisibility(enableAudio ? View.VISIBLE : View.GONE);
        layoutVideoCount.setVisibility(enableVideo ? View.VISIBLE : View.GONE);

        jsResult.clear();
        Map serializableMap = null;
        for (int i = 0; i < editorThings.size(); i++) {
            serializableMap = editorThings.get(i);
            if (serializableMap != null && serializableMap != null && serializableMap.size() > 0) {
                Map<String, Object> map = serializableMap;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    if (RULE_TXT.equalsIgnoreCase(entry.getKey())) {
                        if (getLastEt() != null && getLastEt() instanceof EditText) {
                            EditText et = (EditText) getLastEt();
                            String s = (String) entry.getValue();
                            if (!TextUtils.isEmpty(s)) {
                                et.setText(s.replaceAll("\\\\n", "\\n"));
                                et.setSelection(s.length());
                            }
                        }
                    } else if (RULE_IMAGE.equalsIgnoreCase(entry.getKey())) {
                        initImageView((String) entry.getValue());
                    } else if (RULE_VIDEO.equalsIgnoreCase(entry.getKey())) {
                        VideoEntity videoEntity = (VideoEntity) entry.getValue();
                        resetVideo();
                        mVideoUrl = videoEntity.url;
                        mVideoImage = videoEntity.thumbnailImage;
                        videoDuration = videoEntity.time;
                        videoSize = videoEntity.size;
                        initVideoView();
                    } else if (RULE_AUDIO.equalsIgnoreCase(entry.getKey())) {
                        AudioEntity audioEntity = (AudioEntity) entry.getValue();
                        resetVideo();
                        mAudioUrl = audioEntity.url;
                        audioDuration = audioEntity.time;
                        audioSize = audioEntity.size;
                        initAudioView();
                    }

                }
            }
        }
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

    @OnClick({R.id.id_iv_video_icon, R.id.iv_video_preview, R.id.iv_video_delete, R.id.iv_audio_delete, R.id.id_audio_layout, R.id.id_public_choose_video_btn, R.id.id_public_choose_audio_btn, R.id.id_public_choose_pic_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_iv_video_icon:
            case R.id.iv_video_preview:
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
            case R.id.iv_video_delete:
                idVideoLayout.setVisibility(View.INVISIBLE);
                resetVideo();
                initVideoView();
                break;
            case R.id.iv_audio_delete:
                idAudioLayout.setVisibility(View.INVISIBLE);
                resetAudio();
                initAudioView();
                break;
            case R.id.id_audio_layout:
                if (mRecord != null) {
                    if (!TextUtils.isEmpty(mAudioFilePath)) {
                        mRecord.player(mAudioFilePath);
                    } else if (!TextUtils.isEmpty(mAudioUrl)) {
                        mRecord.player(mAudioUrl);
                    }
                }
                break;
            case R.id.id_public_choose_video_btn:
                type = Type.TYPE_VIDEO;
                mPopupView.showAtLocPop(idPublicRootLl, videos);
                break;
//            case R.id.id_public_choose_audio_btn:
//                Intent i = new Intent(this, VoicePubActivity1.class);
//                i.putExtra(IntentKey.KEY_BOOLEAN, false);
//                startActivityForResult(i, AUDIO_PUBLISH_REQUEST);
//                break;
            case R.id.id_public_choose_pic_btn:
                if (imageCount < imgMaxNum) {
                    PictureSelectedActivity.startActivityForResult(EditorActivity.this, PIC_PUBLISH_REQUEST, imgMaxNum - imageCount, false);
                } else {
                    Toasty.error(mContext, String.format(getString(R.string.max_size_choose_pic_hint), imgMaxNum)).show();
                }
                break;
            case R.id.id_iv_delete:
                ViewGroup vg = (ViewGroup) view.getParent();
                idVgContainer.removeView(vg);
                mergeEt();
                initImageCount();
                break;
            default:
                break;
        }
    }

    @Override
    public void OnItemClick(int position, String tag) {
        switch (position) {
            case 0:
//                if (type == Type.TYPE_AUDIO) {
//                    //音频
//                    jump(VoicePubActivity1.class);
//                } else if (type == Type.TYPE_VIDEO) {
                //视频
//                }
                toVideoRecorder(VIDEO_PUBLISH_REQUEST);
                break;
            case 1:
//                if (type == Type.TYPE_AUDIO) {
//                    //音频
//                    Intent audio = new Intent(frg, AudioChooseAty.class);
//                    startActivityForResult(audio, SYSYEM_REQUEST_AUDIO);
//                } else if (type == Type.TYPE_VIDEO) {
                //视频

                Intent video = new Intent(this, VideoChooseActivity.class);
                startActivityForResult(video, VIDEO_PUBLISH_REQUEST);
//                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        ossDir = getIntent().getStringExtra(IntentKey.EXTRA_URL);
    }

    public class ArticleItem {
        int type;
        String content;
    }

    private boolean isfirstIn = true;

    private static final int TYPE_TEXT = 1;
    private static final int TYPE_IMAGE = 2;

    /**
     * 公共请求code
     */
    public static final int PIC_PUBLISH_REQUEST = 10;

    /**
     * 点击相册选择回调
     */
    public static final int PUBLISH_RESULT = 11;

    /**
     * 照相机回调
     */
    public static final int PUBLISH_RESULT_CAMERA = 12;

    public static final int VIDEO_PUBLISH_REQUEST = 15;

    public static final int AUDIO_PUBLISH_REQUEST = 17;

    Dialog dialog;

    public void addTextImageJsContent() {
        for (int i = 0; i < articleItems.size(); i++) {
            ArticleItem articleItem = articleItems.get(i);
            if (articleItem.type == TYPE_IMAGE) {
                Map map = new HashMap();
                map.put(RULE_IMAGE, articleItem.content);
                jsResult.add(map);
            } else {
                Map map = new HashMap();
                map.put(RULE_TXT, articleItem.content.replaceAll("\\n", "\\\\n").replaceAll("\\r", "").replaceAll("\\t", " "));
                jsResult.add(map);
            }
        }
    }


//    public String getHTMLcontent() {
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < articleItems.size(); i++) {
//            ArticleItem articleItem = articleItems.get(i);
//            if (articleItem.type == TYPE_IMAGE) {
//                sb.append(getImageHtml(articleItem.content));
//            } else {
//                sb.append(getTextHtml(articleItem.content));
//            }
//        }
//        return sb.toString();
//    }

//    private String getContent() {
//        for (int i = 0; i < articleItems.size(); i++) {
//            ArticleItem articleItem = articleItems.get(i);
//            if (articleItem.type == TYPE_TEXT) {
//                if (!TextUtils.isEmpty(articleItem.content) && articleItem.content.length() > 50) {
//                    return articleItem.content.substring(0, 50);
//                }
//                return articleItem.content;
//            }
//        }
//        return "";
//    }


//    public String getThumbPic() {
//        for (int i = 0; i < articleItems.size(); i++) {
//            ArticleItem articleItem = articleItems.get(i);
//            if (articleItem.type == TYPE_IMAGE) {
//                return articleItem.content;
//            }
//        }
//        return "";
//    }


    public void addVideoJsContent(VideoEntity src) {
        Map map = new HashMap();
        map.put(RULE_VIDEO, src);
        jsResult.add(map);
    }

    public void addAudioJsContent(AudioEntity src) {
        Map map = new HashMap();
        map.put(RULE_AUDIO, src);
        jsResult.add(map);
    }

    protected boolean checkContentLength() {
        if (textCount > txtMaxNum) {
            Toasty.error(mContext, String.format(getString(R.string.max_size_text_hint), txtMaxNum)).show();
            return false;
        }
        return true;
    }


    public boolean checkData() {
        if (checkContentData()) {
            if (checkContentLength()) {
                jsResult = new ArrayList<>();
                uploadHtml();
            } else {

            }
        } else {
            Toasty.error(mContext, getString(R.string.cannot_add_content)).show();
        }
        return false;
    }


    public boolean checkContentData() {
//        for (int i = 0; i < idVgContainer.getChildCount(); i++) {
//            View view = idVgContainer.getChildAt(i);
//            if (view instanceof EditText) {
//                EditText et = (EditText) view;
//                if (!TextUtils.isEmpty(et.getText())) {
//                    return true;
//                }
//            } else {
//                return true;
//            }
//        }
//        return false;
        return true;
    }


    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_editor, null, false);
    }

    public void initView() {
        idVgContainer.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                ViewGroup viewGroup = (ViewGroup) parent;
                if (viewGroup.getChildCount() > 1 && viewGroup.getChildAt(0).getLayoutParams().height == ViewGroup.LayoutParams.MATCH_PARENT) {
                    viewGroup.getChildAt(0).getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    viewGroup.getChildAt(0).requestLayout();
                }
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                ViewGroup viewGroup = (ViewGroup) parent;
                if (viewGroup.getChildCount() > 0) {
                    if (viewGroup.getChildAt(0).getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                        viewGroup.getChildAt(0).getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                        viewGroup.getChildAt(0).requestLayout();
                    }
                }
            }
        });

        txtMaxNum = getIntent().getIntExtra(IntentKey.EXTRA_TEXT_MAX, 10000);
        initEditText();
        parseIntent();
        // 布局进出动画

        idEtMax.setText("/" + txtMaxNum);
        idImgMax.setText("/" + imgMaxNum);
        setTitleLeftText(R.string.cancel);
        mPopupView = new PopupView(this);
        mPopupView.setOnItemPopupClick(this);

        setTitleText(getString(R.string.add_content));
        setTitleRightText(R.string.confirm);
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CountDownTimer.isFastClick()) {
                    checkData();
                }
            }
        });

        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.audio_anim);
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

    private void showAudioImgAnim() {
        idAudioPlayTxt.setVisibility(View.INVISIBLE);
        idAudioPlayImg.setImageDrawable(animationDrawable);
        animationDrawable.start();
    }

    private void stopAudioImgAnim() {
        animationDrawable.stop();
        idAudioPlayTxt.setVisibility(View.VISIBLE);
        idAudioPlayImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_audio_play3));
    }


    public void uploadHtml() {
        articleItems.clear();
        for (int i = 0; i < idVgContainer.getChildCount(); i++) {
            View view = idVgContainer.getChildAt(i);
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


//        SVProgressHUD.showWithStatus(aty, getString(R.string.is_loading));
//        MaterialDialog materialDialog = showProgressDialog(getString(R.string.editor_two_publish_ing));
//        materialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                finish();
//            }
//        });

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
//                        SVProgressHUDDismiss();
//                        dismissProgress();
//                        SVProgressHUD.showErrorWithStatus(EditorActivity.this, "图片上传失败");
                        Toasty.error(EditorActivity.this, getString(R.string.editor_two_image_fail), Toast.LENGTH_SHORT, true).show();
                    }
                }
            });

            uploadPicManager.compressAndUploads(this, uploadInfos, TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir);
        } else {
            addTextImageJsContent();
            uploadVideo();
        }
    }

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
//                            dismissProgress();
//                        SVProgressHUD.showErrorWithStatus(EditorActivity.this, "图片上传失败");
                            Toasty.error(EditorActivity.this, getString(R.string.editor_two_video_image_fail), Toast.LENGTH_SHORT, true).show();
//                            SVProgressHUDDismiss();
//                            SVProgressHUD.showErrorWithStatus(EditorActivity.this, "视频图片上传失败");
                        }
                    }
                });
                uploadPicManager.compressAndUploads(this, uploadInfos, TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir);
            } else {
                VideoEntity videoEntity = new VideoEntity();
                videoEntity.url = mVideoUrl;
                videoEntity.thumbnailImage = mVideoImage;
                if (videoDuration != 0) videoEntity.time = videoDuration;
                if (videoSize != 0) videoEntity.size = videoSize;
                addVideoJsContent(videoEntity);
                uploadAudio();
            }
        } else {
            //图片异常直接传空的图片
            VideoEntity videoEntity = new VideoEntity();
            videoEntity.url = mVideoUrl;
            if (videoDuration != 0) videoEntity.time = videoDuration;
            if (videoSize != 0) videoEntity.size = videoSize;
            addVideoJsContent(videoEntity);
            uploadAudio();
        }
    }

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
//                        jsResult.add(getVideoHtml(mVideoUrl));
//                        VideoEntity videoEntity = new VideoEntity();
//                        videoEntity.url = mVideoUrl;
//                        addVideoJsContent(videoEntity);
//                        uploadAudio();
                        uploadVideoPic();
                    } else {
//                        SVProgressHUDDismiss();
//                        SVProgressHUD.showErrorWithStatus(EditorActivity.this, "视频上传失败");

//                        dismissProgress();
                        Toasty.error(EditorActivity.this, getString(R.string.editor_two_audio_fail), Toast.LENGTH_SHORT, true).show();
                    }

                }

                @Override
                public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
//                    SVProgressHUDDismiss();
//                    SVProgressHUD.showErrorWithStatus(EditorActivity.this, "视频上传失败");
//                    dismissProgress();
                    Toasty.error(EditorActivity.this, getString(R.string.editor_two_audio_fail), Toast.LENGTH_SHORT, true).show();
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

    public void uploadAudio() {
        if (!TextUtils.isEmpty(mAudioFilePath)) {
            OssManager.uploadFile(mAudioFilePath, OssManager.AUDIO + (TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir) + mAudioFileName, TextUtils.isEmpty(ossDir) ? OssManager.objectNameCircle : ossDir, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                    Log.d("yeying", "this is mAudioFilePath upload result result is " + putObjectResult.getServerCallbackReturnBody());
                    if (!TextUtils.isEmpty(putObjectResult.getRequestId()) && !TextUtils.isEmpty(putObjectResult.getETag())) {
//                        String OSS_HOST = "http://" + OssManager.bucketName + "." + OssManager.endpoint + "/";
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
                        callJs(true);
                    } else {
//                        SVProgressHUDDismiss();
//                        SVProgressHUD.showErrorWithStatus(EditorActivity.this, "音频上传失败");
//                        dismissProgress();
                        Toasty.error(EditorActivity.this, getString(R.string.editor_two_audio_fail), Toast.LENGTH_SHORT, true).show();
                    }

                }

                @Override
                public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
//                    SVProgressHUDDismiss();
//                    SVProgressHUD.showErrorWithStatus(EditorActivity.this, "音频上传失败");
//                    dismissProgress();
                    Toasty.error(EditorActivity.this, getString(R.string.editor_two_audio_fail), Toast.LENGTH_SHORT, true).show();
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
            callJs(true);
        }
    }

    public void callJs(boolean result) {
        Log.d("yeying", "jsresult " + jsResult.toString());
//        dismissProgress();
        EditorHandler.EditorEntity editorEntity = new EditorHandler.EditorEntity();
        editorEntity.content = (ArrayList<Map<String, Object>>) jsResult;
//        EditorHandler.EditorEntity.Info.ImageInfo imageInfo = new EditorHandler.EditorEntity.Info.ImageInfo();
//        imageInfo.enable = enableImage;
//        imageInfo.imgCurrentNum = imageCount;
//        imageInfo.imgMaxNum = imgMaxNum;
//        EditorHandler.EditorEntity.Info.VideoInfo videoInfo = new EditorHandler.EditorEntity.Info.VideoInfo();
//        videoInfo.enable = enableVideo;
//        EditorHandler.EditorEntity.Info.AudioInfo audioInfo = new EditorHandler.EditorEntity.Info.AudioInfo();
//        audioInfo.enable = enableAudio;
//        EditorHandler.EditorEntity.Info.TextInfo textInfo = new EditorHandler.EditorEntity.Info.TextInfo();
//        textInfo.currentTxtNum = textCount;
//        textInfo.txtMaxNum = txtMaxNum;
//        editorEntity.info = new EditorHandler.EditorEntity.Info();
//        editorEntity.info.audio = audioInfo;
//        editorEntity.info.video = videoInfo;
//        editorEntity.info.image = imageInfo;
//        editorEntity.info.text = textInfo;
        editorEntity.audioEnable = enableAudio;
        editorEntity.videoEnable = enableVideo;
        editorEntity.imageEnable = enableImage;
        editorEntity.currentTxtNum = textCount;
        editorEntity.imgCurrentNum = imageCount;
        editorEntity.imgMaxNum = imgMaxNum;
        editorEntity.txtMaxNum = txtMaxNum;
        JsEvent.callJsEvent(editorEntity, result);
        finish();
    }

    @Override
    public void initData() {

    }

    public View getLastEt() {
        return idVgContainer.getChildAt(idVgContainer.getChildCount() - 1);
    }


    public View getCurrentFocusEt() {
        return idVgContainer.getFocusedChild();
    }

    public int getCurrentFocusPos() {
        View focusView = getCurrentFocusEt();
        if (focusView != null) {
            for (int i = 0; i < idVgContainer.getChildCount(); i++) {
                if (focusView == idVgContainer.getChildAt(i)) {
                    return i;
                }
            }
        }
        return -1;
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
            if (resultCode == PUBLISH_RESULT) {
                ArrayList<String> mList = data.getExtras().getStringArrayList("picList");
                if (null != mList && !mList.isEmpty()) {
                    for (String filePath : mList) {
                        initImageView(filePath);
                    }
                }
            } else if (resultCode == CommonCode.REQUEST.PUBLISH_RESULT_CAMERA) {
                String path = data.getStringExtra("picture");
                initImageView(path);
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
                showToastById(R.string.error_to_found_video);
                return;
            }
            resetVideo();
            mVideoFilePath = filePath;
            initVideoView();
        } else if (requestCode == SYSTEM_SHOOT_VIDEO && requestCode == RESULT_OK) {
            mVideoFilePath = this.filePath;
            resetVideo();
            initVideoView();
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
                showToastById(R.string.error_to_found_video);
                return;
            }
            resetVideo();
            mVideoFilePath = filePath;
            mVideoImage = thum[0];
            initVideoView();
        } else if (requestCode == AUDIO_PUBLISH_REQUEST) {
            if (null == data) {
                return;
            }
            resetAudio();
            mAudioFilePath = data.getStringExtra(IntentKey.EXTRA_PATH);
            initAudioView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void initImageView(final String imgPath) {
        Log.d("picture", "initImageView " + imgPath);
        Log.d("picture", "initImageView fileExist" + new File(imgPath).exists());
        if (TextUtils.isEmpty(imgPath) || (!new File(imgPath).exists() && !imgPath.contains("http"))) {
            Toasty.info(mContext, getString(R.string.not_found_pic)).show();
        }
        View view = getLayoutInflater().inflate(R.layout.layout_iv_article_item, idVgContainer, false);
        final ImageView iv = (ImageView) view.findViewById(R.id.id_iv);
        ImageView iv_delete = (ImageView) view.findViewById(R.id.id_iv_delete);
        iv_delete.setOnClickListener(this);

        int focusPos = getCurrentFocusPos();
        View focusView = getCurrentFocus();
        if (focusPos != -1 && focusView instanceof EditText) {
            spliteEt((EditText) getCurrentFocusEt(), focusPos);
            idVgContainer.addView(view, focusPos + 1);
        } else {
            idVgContainer.addView(view);
        }

        iv.setTag(R.id.id_iv, imgPath);
        if (Protect.checkLoadImageStatus(this)) {
//            Glide.with(this).load(imgPath).into(new ViewTarget<ImageView, GlideDrawable>(iv) {
//                @Override
//                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                    iv.getLayoutParams().height = resource.getIntrinsicHeight() * (DensityUtils.getScreenW(EditorActivity.this) - padding * 2) / resource.getIntrinsicWidth();
////                  iv.setPadding(padding, padding / 2, padding, padding / 2);
//                    iv.requestLayout();
//                    iv.setImageDrawable(resource.getCurrent());
////                    iv.setTag(imgPath);
//                }
//            });
        }
        initImageCount();
//        initEditText();
    }

    private void initImageCount() {
        imageCount = 0;
        for (int i = 0; i < idVgContainer.getChildCount(); i++) {
            if (idVgContainer.getChildAt(i) instanceof FrameLayout) {
                imageCount++;
            }
        }
        idTvImgCount.setText(imageCount + "");
//        if (imageCount == 0) {
//            idPublicChooseAudioBtn.setEnabled(true);
//            idPublicChoosePicBtn.setEnabled(true);
//            idPublicChooseVideoBtn.setEnabled(true);
//        } else {
//            idPublicChooseAudioBtn.setEnabled(false);
//            idPublicChoosePicBtn.setEnabled(true);
//            idPublicChooseVideoBtn.setEnabled(false);
//        }
    }

    private int textCount = 0;
    private int imageCount = 0;

    private void initEditText(String s) {
        EditText et = (EditText) getLayoutInflater().inflate(R.layout.layout_et_article_item, idVgContainer, false);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textCount = count - before + textCount;
                updateTextCount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        idVgContainer.addView(et);
        if (isfirstIn) {
            if (enableImage) {
                et.setHint(R.string.add_text_pic_here);
            } else {
                et.setHint(R.string.add_text);
            }
            isfirstIn = false;
        }
        et.setText(s);
        et.requestFocus();
        if (txtMaxNum == 0) {
            et.setEnabled(false);
        }
    }

    private void initEditText() {
        EditText et = (EditText) getLayoutInflater().inflate(R.layout.layout_et_article_item, idVgContainer, false);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textCount = count - before + textCount;
                updateTextCount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        idVgContainer.addView(et);
        if (isfirstIn) {
            if (enableImage) {
                et.setHint(R.string.add_text_pic_here);
            } else {
                et.setHint(R.string.add_text);
            }
            isfirstIn = false;
        }
        et.requestFocus();
        if (txtMaxNum == 0) {
            et.setEnabled(false);
        }
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(txtMaxNum - textCount)});
    }

    private EditText initEditText(int pos) {
        EditText et = (EditText) getLayoutInflater().inflate(R.layout.layout_et_article_item, idVgContainer, false);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textCount = count - before + textCount;
                updateTextCount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        idVgContainer.addView(et, pos);
        if (isfirstIn) {
            if (enableImage) {
                et.setHint(R.string.add_text_pic_here);
            } else {
                et.setHint(R.string.add_text);
            }
            isfirstIn = false;
        }
        if (txtMaxNum == 0) {
            et.setEnabled(false);
        }
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(txtMaxNum - textCount)});
        return et;
    }

    private void updateTextCount() {
        idTvDescribeCount.setText(textCount + "");
    }


    public void spliteEt(EditText et, int pos) {
        if (et != null) {
            int start = et.getSelectionStart();
            String text = et.getText().toString();
            int length = 0;
            if (!TextUtils.isEmpty(text)) {
                length = text.length();
            }
            if (length > start) {
                EditText newEt = initEditText(pos + 1);
                et.setText(text.substring(0, start));
                newEt.setText(text.substring(start, length));
//                et.requestFocus();
//                et.setSelection(start);
                newEt.requestFocus();
                newEt.setSelection(0);
            } else {
                EditText et1 = initEditText(pos + 1);
                et1.requestFocus();
            }
        }
    }

    /**
     * 合并两个et
     */
    private void mergeEt() {
        EditText et = null;
        for (int i = 0; i < idVgContainer.getChildCount(); i++) {
            View view = idVgContainer.getChildAt(i);
            if (view instanceof EditText) {
                if (et != null) {
                    EditText temp = (EditText) view;
                    et.setText(et.getText().toString() + temp.getText().toString());
                    idVgContainer.removeView(temp);
                    textCount = textCount - temp.getText().length();
                    updateTextCount();
                    et.requestFocus();
                    et.setSelection(et.getText().length());
                    break;
                } else {
                    et = (EditText) view;
                }
            } else {
                et = null;
            }
        }
    }

    public void showToastById(int resouce) {
        Toast.makeText(this, getString(resouce), Toast.LENGTH_SHORT)
                .show();
    }


    private void initAudioView() {
        if (!TextUtils.isEmpty(mAudioFilePath)) {
            idAudioLayout.setVisibility(View.VISIBLE);
            idAudioText.setText(1 + "");
            String[] result = mAudioFilePath.split("/");
            mAudioFileName = result[result.length - 1];
            String[] s = mAudioFileName.split("\\.");
            if (s != null && s.length > 0) {
                mAudioFileName = UUID.randomUUID().toString() + "." + s[s.length - 1];
            }
            initDurationText();
        } else if (!TextUtils.isEmpty(mAudioUrl)) {
            idAudioLayout.setVisibility(View.VISIBLE);
            idAudioText.setText(1 + "");
            if (audioDuration != 0) {
                idAudioPlayTimeTxt.setText(audioDuration / 1000 + " \"");
            }
        } else {
            idAudioText.setText(0 + "");
            idAudioLayout.setVisibility(View.INVISIBLE);
        }
//        updateAudioBtnStatus();
    }


    private void initDurationText() {
        initAudioDuration();
        idAudioPlayTimeTxt.setText(audioDuration / 1000 + " \"");
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

    private void initAudioDuration() {
//        MediaPlayer mp = MediaPlayer.create(this, Uri.parse(mFilePath));
//        audioDuration = mp.getDuration();
        try {
            audioDuration = Long.parseLong(codeMediaInfo(mAudioFilePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initVideoDuration() {
        try {
            videoDuration = Long.parseLong(codeMediaInfo(mVideoFilePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 视频
     */

    private void initVideoView() {
        if (!TextUtils.isEmpty(mVideoFilePath)) {
            idVideoLayout.setVisibility(View.VISIBLE);
            idVideoText.setText(1 + "");
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
            idVideoLayout.setVisibility(View.VISIBLE);
            idVideoText.setText(1 + "");
            if (!TextUtils.isEmpty(mVideoImage)) {
                if (Protect.checkLoadImageStatus(this)) {
//                    Glide.with(this).load(mVideoImage).into(ivVideoPreview);
                }
            }
        } else {
            idVideoText.setText(0 + "");
            idVideoLayout.setVisibility(View.INVISIBLE);
        }
//        updateVideoBtnStatus();
    }

//    public void updateVideoBtnStatus() {
//        if (idVideoLayout.getVisibility() == View.INVISIBLE) {
//            idPublicChooseAudioBtn.setEnabled(true);
//            idPublicChoosePicBtn.setEnabled(true);
//            idPublicChooseVideoBtn.setEnabled(true);
//        } else {
//            idPublicChooseAudioBtn.setEnabled(false);
//            idPublicChoosePicBtn.setEnabled(false);
//            idPublicChooseVideoBtn.setEnabled(true);
//        }
//    }
//
//    public void updateAudioBtnStatus() {
//        if (idAudioLayout.getVisibility() == View.INVISIBLE) {
//            idPublicChooseAudioBtn.setEnabled(true);
//            idPublicChoosePicBtn.setEnabled(true);
//            idPublicChooseVideoBtn.setEnabled(true);
//        } else {
//            idPublicChooseAudioBtn.setEnabled(true);
//            idPublicChoosePicBtn.setEnabled(false);
//            idPublicChooseVideoBtn.setEnabled(false);
//        }
//    }

    public static final long maxSize = 500 * 1024 * 1024;

    private boolean checkVideoFile(String videoPath) {
        if (TextUtils.isEmpty(videoPath)) {
            showToastById(R.string.not_found_video);
            return false;
        }

        File file = new File(videoPath);
        if (file.exists()) {
            if (maxSize < file.length()) {
                showToastById(R.string.file_too_big);
                return false;
            }
        } else {
            showToastById(R.string.not_found_video);
            return false;
        }
        return true;
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
//                Toasty.error(mContext, getString(R.string.get_audio_fail_two));
                e.printStackTrace();
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.camera_sd_permissions_run), RC_VIDEO_AND_EXTENER, perms);
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
    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            if (requestCode == RC_VIDEO_AND_EXTENER) {
//                new AppSettingsDialog.Builder(this, "圈乎要使用摄像头,读取手机状态,使用sd卡和录音权限，否则app可能无法正常运行")
//                        .setPositiveButton(getString(R.string.setting))
//                        .setNegativeButton(getString(R.string.cancel), null /* click listener */)
//                        .setRequestCode(requestCode)
//                        .build()
//                        .show();
//            } else {
//
//            }
//        }
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

    public void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getApplicationWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRecord != null && mRecord.isPlayIng()) {
            mRecord.stopPlay();
        }
    }

    @Override
    public void finish() {
        hideInputMethod();
        super.finish();
    }

    /** 视频*/

}
