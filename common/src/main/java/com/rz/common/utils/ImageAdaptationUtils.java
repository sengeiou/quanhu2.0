package com.rz.common.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.rz.common.R;
import com.rz.common.application.BaseApplication;

/**
 * Created by rzw2 on 2017/7/18.
 */

public class ImageAdaptationUtils {

    private static final int TYPE_HOME_THUMB = 0;
    private static final int TYPE_HOME_CONTENT_MULTI = 1;
    private static final int TYPE_HOME_CONTENT_SINGLE = 2;
    private static final int TYPE_SHOW_DETAILS = 3;
    private static final int TYPE_SHOW_LIST_MULTI = 4;
    private static final int TYPE_SHOW_USER_HOME_MULTI = 5;
    public static final int TYPE_SHOW_LIST_SINGLE = 6;
    public static final int TYPE_SHOW_USER_HOME_SINGLE = 7;


    private static int max_show_list_h = getDimen(R.dimen.px504);
    private static int max_show_list_w = getDimen(R.dimen.px750);
    private static int mix_show_list_h = getDimen(R.dimen.px320);
    private static int mix_show_list_w = getDimen(R.dimen.px320);

    private static int max_show_user_home_h = getDimen(R.dimen.px504);
    private static int max_show_user_home_w = getDimen(R.dimen.px750);
    private static int mix_show_user_home_h = getDimen(R.dimen.px302);
    private static int mix_show_user_home_w = getDimen(R.dimen.px302);

    private static String getZoomByType(Context mContext, String url, int type) {
        return initUrlStitching(url) + getAccurateByType(type);
    }

    public static String getZoomByWH(Context mContext, String url, int limit) {
        return getZoomByWH(mContext, url, getWidthByUrl(url), getHeightByUrl(url), limit);
    }

    public static String getZoomByWH(Context mContext, String url, int limit_w, int limit_h) {
        return initUrl(url) + String.format(mContext.getResources().getString(R.string.image_adaptation_stitching), getDimen(limit_w), limit_h != 0 ? getDimen(limit_h) : 0);
    }

    public static String getZoomByWH(Context mContext, String url, int width, int height, int limit) {
        if (width == 0)
            return url;
        switch (limit) {
            case TYPE_SHOW_LIST_SINGLE:
                if ((height / width) <= 2 && (width / height) <= 2) {
                    if (width < mix_show_list_w)
                        width = mix_show_list_w;
                    if (width > max_show_list_w)
                        width = max_show_list_w;
                    if (height < mix_show_list_h)
                        height = mix_show_list_h;
                    if (height > max_show_list_h)
                        height = max_show_list_h;
                }
                return initUrl(url) + String.format(mContext.getResources().getString(R.string.image_adaptation_stitching), width, height);
            case TYPE_SHOW_USER_HOME_SINGLE:
                if ((height / width) <= 2) {
                    if (width < mix_show_user_home_w)
                        width = mix_show_user_home_w;
                    if (width > max_show_user_home_w)
                        width = max_show_user_home_w;
                    if (height < mix_show_user_home_h)
                        height = mix_show_user_home_h;
                    if (height > max_show_user_home_h)
                        height = max_show_user_home_h;
                }
                return initUrl(url) + String.format(mContext.getResources().getString(R.string.image_adaptation_stitching), width, height);
            default:
                return url;
        }
    }

    private static String initUrl(String url) {
        if (url.contains("?")) {
            url = url + "&";
        } else {
            url = url + "?";
        }
        return url;
    }

    private static String initUrlStitching(String url) {
        if (url.contains("?")) {
            url = url + "&x-oss-process=image/resize,m_lfit,limit_0,";
        } else {
            url = url + "?x-oss-process=image/resize,m_lfit,limit_0,";
        }
        return url;
    }

    private static String getAccurateByType(int type) {
        String Accurate = "";
        switch (type) {
            case TYPE_HOME_THUMB:
                Accurate = "w_288,h_260";
                break;
            case TYPE_HOME_CONTENT_MULTI:
                Accurate = "w_320,h_320";
                break;
            case TYPE_HOME_CONTENT_SINGLE:
                Accurate = "w_994,h_558";
                break;
            case TYPE_SHOW_DETAILS:
                Accurate = "w_994";
                break;
            case TYPE_SHOW_LIST_MULTI:
                Accurate = "w_320,h_320";
                break;
            case TYPE_SHOW_USER_HOME_MULTI:
                Accurate = "w_302,h_302";
                break;
        }
        return Accurate;
    }

    private static int getDimen(int size) {
        int dimension = (int) BaseApplication.getContext().getResources().getDimension(size);
        return dimension;
    }

    public static int getWidthByUrl(String url) {
        try {
            if (TextUtils.isEmpty(url)) {
                return 0;
            }
            Uri uri = Uri.parse(url);
            String width = uri.getQueryParameter("w");
            return TextUtils.isEmpty(width) ? 0 : Integer.parseInt(width);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    public static int getHeightByUrl(String url) {
        try {
            if (TextUtils.isEmpty(url)) {
                return 0;
            }
            Uri uri = Uri.parse(url);
            String height = uri.getQueryParameter("h");
            return TextUtils.isEmpty(height) ? 0 : Integer.parseInt(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
