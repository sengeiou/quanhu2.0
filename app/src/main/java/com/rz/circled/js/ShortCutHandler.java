package com.rz.circled.js;

import android.app.Activity;

import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

/**
 * Created by Administrator on 2017/3/13/013.
 */

public class ShortCutHandler extends ServerHandler {

    public ShortCutHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "circleShortcut";
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        return null;
    }

    @Override
    public boolean isUi() {
        return false;
    }

//        final String circleImageUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489408539810&di=98e8aaffd325f7336c6fc9cf736459dd&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D13f84e0b5b82b2b7a79f39cc01accb0a%2F896f5e600c3387445944f973530fd9f9d62aa08c.jpg";
//        final String circleUrl = "https://www.baidu.com/";
//        final String circleName = "圈子入口";
    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
//        checkPremission();
//        getAppPemission();//this only just log
//        Log.e("fenganjs", "onResourceReady: shortCutModel" + paramObj.toJson());
//        Gson gson = new Gson();
//        String dataJson = gson.toJson(paramObj.getData());
//        ShortCutModel shortCutModel = gson.fromJson(dataJson, ShortCutModel.class);
//        Log.e("fenganjs", "onResourceReady: params" + params);
//        final String circleImageUrl = shortCutModel.getCircleImageUrl();
//        final String circleUrl = shortCutModel.getCircleUrl();
//        final String circleName = shortCutModel.getCircleName();
//        final Long invokeId = paramObj.getInvokeId();
//        Log.e("fenganjs", "onResourceReady: circleImageUrl" + circleImageUrl);
//        Log.e("fenganjs", "onResourceReady: circleUrl" + circleUrl);
//        Log.e("fenganjs", "onResourceReady: circleName" + circleName);
//        final Intent intent = new Intent(mActivity, WebContainerAty.class);
//        intent.setAction("android.intent.action.MAIN");//确保卸载应用可以同时卸载圈子快捷方式
//        intent.addCategory("android.intent.category.LAUNCHER");
//        intent.putExtra(IntentKey.Js.JS_CIRCLEURL, circleUrl);
//        if (Protect.checkLoadImageStatus(webContainerAty)) {
//            webContainerAty.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Glide.with(webContainerAty)
//                            .load(circleImageUrl)
//                            .asBitmap()
//                            .centerCrop()
//                            .into(new SimpleTarget<Bitmap>(500, 500) {
//                                @Override
//                                public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
//                                    //得到bitmap
//                                    Log.e("fenganjs", "onResourceReady: bitmap" + bitmap);
//                                    if (null != bitmap && !TextUtils.isEmpty(circleUrl) && !TextUtils.isEmpty(circleName)) {
//                                        ShortcutUtils.addShortcut(webContainerAty, intent, circleName, false, bitmap);
//                                    } else {
//                                        ToastUtil.showToast("参数有误");
//                                    }
//                                }
//                            });
//                }
//            });
//        }
    }

//    @TargetApi(Build.VERSION_CODES.M)
//    private void checkPremission() {
//        if (webContainerAty.checkSelfPermission("com.android.launcher.permission.INSTALL_SHORTCUT") != PackageManager.PERMISSION_GRANTED) {
//            ToastUtil.showToast("没有权限,请手动添加权限");
//        } else {
//            ToastUtil.showToast("已有权限");
//        }
//    }

//    public void getAppPemission() {
//        PackageManager pm = webContainerAty.getPackageManager();
//        PackageInfo pi;
//        try {
//            // 参数2必须是PackageManager.GET_PERMISSIONS
//            pi = pm.getPackageInfo(webContainerAty.getPackageName(), PackageManager.GET_PERMISSIONS);
//            String[] permissions = pi.requestedPermissions;
//            Log.e("fenganjs", "permissions数量" + permissions.length);
//            if (permissions != null) {
//                for (String str : permissions) {
//                    Log.e("fenganjs", "permissions==" + str);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


//    public void toSetting() {
//        //设置界面
////        Intent intent =  new Intent(Settings.ACTION_SETTINGS);
////        webContainerAty.startActivity(intent);
//        //本应用权限设置界面
//        Intent intent = new Intent();
//        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        intent.setData(Uri.fromParts("package", webContainerAty.getPackageName(), null));
//        webContainerAty.startActivity(intent);
//    }
}