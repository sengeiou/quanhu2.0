package com.rz.common.cache;

import android.os.Environment;

import com.rz.common.application.BaseApplication;

public class CachePath {

    /**
     * 外部路径
     */
    public static final String wai_path = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/Android/data/"
            + BaseApplication.getContext().getPackageName();

    /**
     * 内部路径
     */
    public static final String nei_path = "/data/data/"
            + BaseApplication.getContext().getPackageName();
//
    /**
     * 晒一晒用户手动保存的图片路径
     */
    public static final String save_pic_path = wai_path + "/myPics";

    /**
     * 数据库路径
     */
    public static final String databases_path = "/data/data/"
            + BaseApplication.getContext().getPackageName() + "/databases";

    public static final String media = wai_path + "/media";

    /**
     * SharedPreferences路径
     */
    public static final String share_path = "/data/data/"
            + BaseApplication.getContext().getPackageName() + "/shared_prefs";
}
