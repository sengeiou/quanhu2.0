package com.rz.circled.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.common.application.BaseApplication;
import com.rz.common.cache.preference.EntityCache;
import com.rz.httpapi.bean.RewardGiftModel;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by rzw2 on 2016/10/9.
 */
public class CommomUtils {
    //诸葛io通用工具
    public static void trackUser(String text, String name, String value) {
        //定义与事件相关的属性信息
        JSONObject eventObject = new JSONObject();
        try {
            eventObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }


//记录事件
        ZhugeSDK.getInstance().track(BaseApplication.getContext(), text, eventObject);

    }

    public static void showNoDataTip(Activity mActivity) {
        TextView idRefreshStatusTxt = (TextView) mActivity.findViewById(R.id.id_refresh_status_txt);
        final RelativeLayout idRefreshStatusLl = (RelativeLayout) mActivity.findViewById(R.id.id_refresh_status_ll);

        idRefreshStatusTxt.setText("没有更多的搜索结果");
        idRefreshStatusLl.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                idRefreshStatusLl.setVisibility(View.GONE);
            }
        }, 3000);
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    public static RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }

    public static int getGiftIntFromPrice(Context context, long price, boolean bigPic) {
        if (price != 0) {
            if (price == 100) {
                return bigPic ? R.drawable.v3_gift_icecream_big : R.drawable.v3_gift_icecream_small;
            } else if (price == 200) {
                return bigPic ? R.drawable.v3_gift_flower_big : R.drawable.v3_gift_flower_small;
            } else if (price == 500) {
                return bigPic ? R.drawable.v3_gift_lip_big : R.drawable.v3_gift_lip_small;
            } else if (price == 1000) {
                return bigPic ? R.drawable.v3_gift_champagne_big : R.drawable.v3_gift_champagne_small;
            } else if (price == 10000) {
                return bigPic ? R.drawable.v3_gift_bag_big : R.drawable.v3_gift_bag_small;
            } else if (price == 30000) {
                return bigPic ? R.drawable.v3_gift_ring_big : R.drawable.v3_gift_ring_small;
            } else if (price == 50000) {
                return bigPic ? R.drawable.v3_gift_car_big : R.drawable.v3_gift_car_small;
            } else if (price == 100000) {
                return bigPic ? R.drawable.v3_gift_rocket_big : R.drawable.v3_gift_rocket_small;
            }
        }
        return 0;
    }

    public static int getGiftIntWithBgFromPrice(Context context, long price) {
        if (price != 0) {
            if (price == 100) {
                return R.drawable.v3_gift_icecream_bg;
            } else if (price == 200) {
                return R.drawable.v3_gift_flower_bg;
            } else if (price == 500) {
                return R.drawable.v3_gift_lip_bg;
            } else if (price == 1000) {
                return R.drawable.v3_gift_champagne_bg;
            } else if (price == 10000) {
                return R.drawable.v3_gift_bag_bg;
            } else if (price == 30000) {
                return R.drawable.v3_gift_ring_bg;
            } else if (price == 50000) {
                return R.drawable.v3_gift_car_bg;
            } else if (price == 100000) {
                return R.drawable.v3_gift_rocket_bg;
            }
        }
        return R.drawable.v3_gift_icecream_bg;
    }

    public static String getGiftUrlFromPrice(Context context, long price) {
        String result = "http://yryz-resources.oss-cn-hangzhou.aliyuncs.com/pic/reward/icecreambg.png";
        EntityCache<RewardGiftModel> rewardGiftModelEntityCache = new EntityCache<>(context, RewardGiftModel.class);
        List<RewardGiftModel> rewardGiftModelList = rewardGiftModelEntityCache.getListEntityAddTag(RewardGiftModel.class, "transfer");
        if (rewardGiftModelList != null && rewardGiftModelList.size() > 0) {
            for (int i = 0; i < rewardGiftModelList.size(); i++) {
                RewardGiftModel rewardGiftModel = rewardGiftModelList.get(i);
                if (TextUtils.equals(rewardGiftModel.getPrice(), price + "")) {
                    return rewardGiftModel.getIcon();
                }
            }
        }
        return result;
    }

    /***
     * @param context
     * @param price
     * @param bigPic  true大图,false小图
     * @return
     */
    public static Drawable getGiftFromPrice(Context context, long price, boolean bigPic) {
        if (price != 0) {
            if (price == 100) {
                return bigPic ? ContextCompat.getDrawable(context, R.drawable.v3_gift_icecream_big) :
                        ContextCompat.getDrawable(context, R.drawable.v3_gift_icecream_small);
            } else if (price == 200) {
                return bigPic ? ContextCompat.getDrawable(context, R.drawable.v3_gift_flower_big) :
                        ContextCompat.getDrawable(context, R.drawable.v3_gift_flower_small);
            } else if (price == 500) {
                return bigPic ? ContextCompat.getDrawable(context, R.drawable.v3_gift_lip_big) :
                        ContextCompat.getDrawable(context, R.drawable.v3_gift_lip_small);
            } else if (price == 1000) {
                return bigPic ? ContextCompat.getDrawable(context, R.drawable.v3_gift_champagne_big) :
                        ContextCompat.getDrawable(context, R.drawable.v3_gift_champagne_small);
            } else if (price == 10000) {
                return bigPic ? ContextCompat.getDrawable(context, R.drawable.v3_gift_bag_big) :
                        ContextCompat.getDrawable(context, R.drawable.v3_gift_bag_small);
            } else if (price == 3000) {
                return bigPic ? ContextCompat.getDrawable(context, R.drawable.v3_gift_ring_big) :
                        ContextCompat.getDrawable(context, R.drawable.v3_gift_ring_small);
            } else if (price == 50000) {
                return bigPic ? ContextCompat.getDrawable(context, R.drawable.v3_gift_car_big) :
                        ContextCompat.getDrawable(context, R.drawable.v3_gift_car_small);
            } else if (price == 100000) {
                return bigPic ? ContextCompat.getDrawable(context, R.drawable.v3_gift_rocket_big) :
                        ContextCompat.getDrawable(context, R.drawable.v3_gift_rocket_small);
            }
        }
        return bigPic ? ContextCompat.getDrawable(context, R.drawable.v3_gift_icecream_big) :
                ContextCompat.getDrawable(context, R.drawable.v3_gift_icecream_small);
    }


    public static String getGiftNameFromPrice(Context context, long price) {
//        if (price != 0) {
//            if (price == 100) {
//                return "冰淇淋";
//            } else if (price == 200) {
//                return "蓝色妖姬";
//            } else if (price == 500) {
//                return "么么哒";
//            } else if (price == 1000) {
//                return "香槟";
//            } else if (price == 10000) {
//                return "金福钱袋";
//            } else if (price == 30000) {
//                return "钻戒";
//            } else if (price == 50000) {
//                return "法拉利";
//            } else if (price == 100000) {
//                return "火箭";
//            }
//        }
//        return "冰淇淋";

        String result = "冰淇淋";
        EntityCache<RewardGiftModel> rewardGiftModelEntityCache = new EntityCache<>(context, RewardGiftModel.class);
        List<RewardGiftModel> rewardGiftModelList = rewardGiftModelEntityCache.getListEntityAddTag(RewardGiftModel.class, "transfer");
        if (rewardGiftModelList != null && rewardGiftModelList.size() > 0) {
            for (int i = 0; i < rewardGiftModelList.size(); i++) {
                RewardGiftModel rewardGiftModel = rewardGiftModelList.get(i);
                if (TextUtils.equals(rewardGiftModel.getPrice(), price + "")) {
                    return rewardGiftModel.getName();
                }
            }
        }
        return result;

    }

    /**
     * 普通的链接拼接地址
     *
     * @param circleUrl
     * @param moduleId
     * @param infoId
     * @return
     */
    public static String getCircleDetailUrl(String circleUrl, String moduleId, String infoId) {
        if (TextUtils.isEmpty(circleUrl)) {
            return "";
        } else {
            if (circleUrl.endsWith("/")) {
                circleUrl = circleUrl.substring(0, circleUrl.length() - 1);
            }
        }
        if (!TextUtils.isEmpty(infoId)) {
            infoId = infoId.split("\\.")[0];
        }
        String url = circleUrl + "/redirect/" + moduleId + "/" + infoId;
        return url;
    }

    /**
     * 首页和搜索的动态跳转
     *
     * @param
     * @param moduleId
     * @param
     * @return
     */
    public static String getDymanicUrl(String moduleId, String coterieId, String resourceId) {
        String url = BuildConfig.WebHomeBaseUrl + "/redirect/coterie/" + coterieId + "/" + moduleId + "/" + resourceId;
        return url;
    }
    public static String getCircleUrl(String moduleId, String resourceId) {
        String url = BuildConfig.WebHomeBaseUrl+"/redirect/"+moduleId+"/"+resourceId;
        return url;
    }

    /**
     * 转发详情页内嵌Webview网址
     *
     * @param circleUrl
     * @param moduleId
     * @param infoId
     * @return
     */
    public static String getForwardUrl(String circleUrl, String moduleId, String infoId) {
//        http://www-dev.yryz.com/yertx/redirect/{moduleId}/{infoId}/forward
        if (TextUtils.isEmpty(circleUrl)) {
            return "";
        } else {
            if (circleUrl.endsWith("/")) {
                circleUrl = circleUrl.substring(0, circleUrl.length() - 1);
            }
        }
        if (!TextUtils.isEmpty(infoId)) {
            infoId = infoId.split("\\.")[0];
        }
//        String url = circleUrl + "/redirect/" + moduleId + "/" + infoId + "/" + "forward";
        String url = circleUrl + "/redirect/" + moduleId + "/" + infoId + "?" + "type=forward";
        return url;
    }

    /**
     * 转发分享到第三方链接地址
     *
     * @param circleUrl
     * @param moduleId
     * @param infoId
     * @param custId
     * @param transferId
     * @return
     */
    public static String getForwardShareUrl(String circleUrl, String moduleId, String infoId, String custId, String transferId) {
        if (TextUtils.isEmpty(circleUrl)) {
            return "";
        } else {
            if (circleUrl.endsWith("/")) {
                circleUrl = circleUrl.substring(0, circleUrl.length() - 1);
            }
        }
        if (!TextUtils.isEmpty(infoId)) {
            infoId = infoId.split("\\.")[0];
        }
//        String url = circleUrl + "/redirect/" + moduleId + "/" + infoId + "/" + "transfer/" + custId + "/" + transferId;
        String url = circleUrl + "/redirect/" + moduleId + "/" + infoId + "?type=transfer&custId=" + custId + "&transferId=" + transferId;
        return url;
    }

    public static boolean isNetUrl(String s) {
        try {
            return Patterns.WEB_URL.matcher(s).matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getPrivateGroupHomeUrl(String circleRoute, String coterieId) {
        return "/" + circleRoute + "/coterie/" + coterieId;
    }

    public static String getPrivateGroupRescourceUrl(String circleRoute, String coterieId, String modelId, String resourceId) {
        return "/" + circleRoute + "/redirect/coterie/" + coterieId + "/" + modelId + "/" + resourceId;
    }
}
