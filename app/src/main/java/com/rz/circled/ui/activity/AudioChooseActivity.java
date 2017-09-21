package com.rz.circled.ui.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.widget.SmoothCheckBox;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.cache.BitmapMemoryCache;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.permission.AfterPermissionGranted;
import com.rz.common.permission.AppSettingsDialog;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.MediaFile;
import com.rz.common.utils.Record;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.bean.LocalMediaInfo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;


public class AudioChooseActivity extends BaseActivity implements AdapterView.OnItemClickListener, SmoothCheckBox.OnCheckedChangeListener {

    @BindView(R.id.id_comm_listview)
    ListView idCommListview;

    private CommonAdapter commonAdapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    /**
     * 存储的所有
     */
    public List<LocalMediaInfo> localMediaInfos = new ArrayList<LocalMediaInfo>();

    private BitmapMemoryCache bitmapMemoryCache;

    private int selectPos = -1;

    private EntityCache<LocalMediaInfo> localMediaInfoEntityCache;


    private Record record;

    @Override
    protected View loadView(LayoutInflater inflater) {
        localMediaInfoEntityCache = new EntityCache<LocalMediaInfo>(this, LocalMediaInfo.class);
        return inflater.inflate(R.layout.comm_list_view, null, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initView() {
        setTitleRightText(R.string.define);
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPos == -1) {
                    Toasty.info(mContext, getString(R.string.choose_one_audio_publish)).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(IntentKey.EXTRA_PATH, localMediaInfos.get(selectPos).filePath);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        idCommListview.setOnItemClickListener(this);

        final int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        final int size = 1024 * 1024 * memClass / 8;

        bitmapMemoryCache = new BitmapMemoryCache(size);

        commonAdapter = new CommonAdapter<LocalMediaInfo>(this, R.layout.item_local_choose_audio) {

            @Override
            public void convert(ViewHolder helper, LocalMediaInfo item, int position) {
                SmoothCheckBox smoothCheckBox = (SmoothCheckBox) helper.getViewById(R.id.id_checkbox);
                smoothCheckBox.setTag(helper.getPosition());
                smoothCheckBox.setOnCheckedChangeListener(AudioChooseActivity.this);
                boolean checked = helper.getPosition() == selectPos;
                if (checked != smoothCheckBox.isChecked()) {
                    smoothCheckBox.setChecked(checked, true, false);
                }
                Log.d("yeying", "checked " + checked);
                Log.d("yeying", "checked helper.getPosition()" + helper.getPosition() + " selectPos" + selectPos);
                ImageView iv = (ImageView) helper.getViewById(R.id.id_img);
                iv.setTag(item.filePath);
//                if (!TextUtils.isEmpty(item.thumbnails)) {
//                    helper.setImageByUrl(R.id.id_img, item.thumbnails, R.drawable.ic_height_bg);
//                } else {
//                bitmapMemoryCache.loadBitmap(item.filePath, iv);
//                }
                TextView tv_name = (TextView) helper.getViewById(R.id.id_name_txt);
                tv_name.setText(item.title);

                TextView tv_time = (TextView) helper.getViewById(R.id.id_time_txt);
                tv_time.setText(getString(R.string.time_long) + item.tvDuration);

                TextView tv_size = (TextView) helper.getViewById(R.id.id_size_txt);
                tv_size.setText(getString(R.string.size) + item.tvSize);

                TextView tv_format = (TextView) helper.getViewById(R.id.id_format_txt);
                tv_format.setText(item.tvMime);
            }
        };

    }


    @Override
    public void initData() {
        List<LocalMediaInfo> localMediaInfos = localMediaInfoEntityCache.getListEntityAddTag(LocalMediaInfo.class, Type.TYPE_AUDIO + "");
        if (localMediaInfos != null && localMediaInfos.size() > 0) {
            refreshView(localMediaInfos);
        }
        checkPermissions();
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 表示没有扫描到
                case 0:
                    onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                    break;
                // 表示有扫描到
                case 1:
                    List<LocalMediaInfo> localMediaInfos = (List<LocalMediaInfo>) msg.obj;
                    if (localMediaInfos != null && localMediaInfos.size() > 0) {
                        onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        if (localMediaInfoEntityCache != null) {
                            localMediaInfoEntityCache.putListEntityAddTag((List<LocalMediaInfo>) msg.obj, Type.TYPE_AUDIO + "");
                        }
                        refreshView((List<LocalMediaInfo>) msg.obj);
                    } else {
                        onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                    }
                    break;
                default:
                    break;
            }
        }
    };


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
            new AppSettingsDialog.Builder(this, getString(R.string.sd_card_permissions_run))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        }
                    })
                    .setRequestCode(RC_SETTINGS_SCREEN)
                    .build()
                    .show();
        }
    }

    @AfterPermissionGranted(RC_EXTENER)
    private void checkPermissions() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            onLoadingStatus(CommonCode.General.DATA_LOADING);
            getFiles();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.sd_card_permissions), RC_EXTENER, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public boolean hasDataInPage() {
        return localMediaInfos != null && localMediaInfos.size() != 0;
    }

    private void refreshView(List<LocalMediaInfo> lists) {
        if (localMediaInfos != null && lists.size() > 0) {
            if (selectPos != -1) {
                LocalMediaInfo localMediaInfo = localMediaInfos.get(selectPos);
                selectPos = lists.indexOf(localMediaInfo);
            }
        }
        localMediaInfos.clear();
        localMediaInfos.addAll(lists);
        Log.d(TAG, "localMediaInfos:size" + localMediaInfos.size());
        idCommListview.setAdapter(commonAdapter);
        commonAdapter.setData(localMediaInfos);
        String path = localMediaInfos.get(0).filePath;
//        LameActivity.encodeFile(path, path.substring(path.lastIndexOf(".") + 1) + "testIn.mp3");
        Log.d("yeying", "test encode " + path.substring(path.lastIndexOf(".") + 1) + "testIn.mp3");
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getFiles() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toasty.info(mContext, getString(R.string.personal_no_sd_card)).show();
            return;
        }
        // 扫描图片文件夹

        mDirPaths.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<LocalMediaInfo> temp = new ArrayList<LocalMediaInfo>();

                // MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
//                String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA,
//                        MediaStore.Video.Thumbnails.VIDEO_ID};

                // MediaStore.Video.Media.DATA：视频文件路径；
                // MediaStore.Video.Media.DISPLAY_NAME : 视频文件名，如 testVideo.mp4
                // MediaStore.Video.Media.TITLE: 视频标题 : testVideo
                String[] mediaColumns = {MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DISPLAY_NAME};

                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = getContentResolver();
                Cursor mCursor = mContentResolver.query(uri, mediaColumns, null, null, null);
                if (null != mCursor && mCursor.getCount() > 0) {
                    //首先根据数据库查询所有已存在的视频文件
                    while (mCursor.moveToNext()) {
                        try {
                            String path = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                            File file = new File(path);

                            if (file == null || !file.exists()) {
                                continue;
                            }

                            if (!MediaFile.isAudioMp3OrAmr(path)) {
                                continue;
                            }

                            LocalMediaInfo localMediaInfo = new LocalMediaInfo();
                            localMediaInfo.filePath = path;
                            if (temp.contains(localMediaInfo)) {
                                continue;
                            }

                            try {
                                codeMediaInfo(localMediaInfo);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d(TAG, "localMediaInfos:size database" + e.getMessage());
                                continue;
                            }

                            Log.d(TAG, "localMediaInfos:size database" + 1);

                            if (!MediaFile.isAudioMp3OrAmrFromMimeType(localMediaInfo.mimeType)) {
                                continue;
                            }
                            Log.d(TAG, "localMediaInfos:size database" + 2);
                            if (localMediaInfo.duration == 0 || TextUtils.isEmpty(localMediaInfo.mimeType)) {
                                continue;
                            }
                            if (localMediaInfo.duration < 3000) {
                                continue;
                            }
                            Log.d(TAG, "localMediaInfos:size database" + 3);
                            localMediaInfo.size = file.length();

                            localMediaInfo.title = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

                            localMediaInfo.fileName = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));

                            temp.add(localMediaInfo);

                            Log.d(TAG, "localMediaInfos:size database" + localMediaInfo.toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                    //根据已经查到的视频文件搜索同目录，去重。
                    for (int i = 0; i < temp.size(); i++) {
                        String path = temp.get(i).filePath;
                        // 获取父路径名
                        File parentFile = new File(path).getParentFile();
                        if (parentFile == null)
                            continue;
                        String dirPath = parentFile.getAbsolutePath();
                        // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                        if (mDirPaths.contains(dirPath)) {
                            continue;
                        } else {
                            mDirPaths.add(dirPath);
                        }
                        String[] s = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                return MediaFile.isAudioMp3OrAmr(filename);
                            }
                        });
                        if (s == null) {
                            continue;
                        } else {
                            for (int j = 0; j < s.length; j++) {
                                File file = new File(s[j]);
                                if (file == null || !file.exists()) {
                                    continue;
                                }
                                LocalMediaInfo localMediaInfo = new LocalMediaInfo();
                                localMediaInfo.filePath = s[j];
                                if (temp.contains(localMediaInfo)) {
                                    continue;
                                }
                                try {
                                    codeMediaInfo(localMediaInfo);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    continue;
                                }

                                if (localMediaInfo.duration == 0 || TextUtils.isEmpty(localMediaInfo.mimeType)) {
                                    continue;
                                }

                                if (localMediaInfo.duration < 3000) {
                                    continue;
                                }

                                if (!MediaFile.isAudioMp3OrAmrFromMimeType(localMediaInfo.mimeType)) {
                                    continue;
                                }

                                localMediaInfo.fileName = MediaFile.getFileName(s[j]);
                                localMediaInfo.title = localMediaInfo.fileName;
                                localMediaInfo.size = file.length();
                                temp.add(localMediaInfo);
                                Log.d(TAG, "localMediaInfos:size parentFile" + localMediaInfo.toString());
                            }

                        }
                    }
                    for (int i = 0; i < temp.size(); i++) {
                        LocalMediaInfo single = temp.get(i);
                        single.tvDuration = StringUtils.formatDuration(single.duration);
                        single.tvSize = StringUtils.getPrintSize(single.size);
                        if (TextUtils.isEmpty(single.mimeType)) {
                            single.tvMime = "";
                        } else {
                            String[] s = single.mimeType.split("/");
                            if (s.length == 2) {
                                single.tvMime = getString(R.string.format) + s[1];
                            }
                        }
                    }
                    mCursor.close();
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = temp;
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }


    private void codeMediaInfo(LocalMediaInfo localMediaInfo) throws Exception {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        Log.d(TAG, "str:" + localMediaInfo.filePath);
        mmr.setDataSource(localMediaInfo.filePath);
        String mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
        Log.d(TAG, "mime:" + mime);
        localMediaInfo.mimeType = mime;
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
        Log.d(TAG, "duration:" + duration);
        localMediaInfo.duration = Long.valueOf(duration);
        mmr.release();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MediaActivity.class);
        intent.putExtra(IntentKey.EXTRA_PATH, localMediaInfos.get(position).filePath);
        intent.putExtra(IntentKey.EXTRA_TYPE, Type.TYPE_AUDIO);
        intent.putExtra(IntentKey.EXTRA_SERIALIZABLE, localMediaInfos.get(position));
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
        if (isChecked) {
            selectPos = (int) checkBox.getTag();
        } else {
            selectPos = -1;
        }
        commonAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshPage() {

    }
}

