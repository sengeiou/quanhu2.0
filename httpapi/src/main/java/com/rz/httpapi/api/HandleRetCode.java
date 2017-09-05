//package com.rz.httpapi.api;
//
//import android.app.Activity;
//import android.app.Application;
//import android.content.Context;
//import android.content.Intent;
//
//import com.bigkoo.svprogresshud.SVProgressHUD;
//import com.rz.rz_rrz.constant.IntentKey;
//import com.rz.rz_rrz.constant.ReturnCode;
//import com.rz.rz_rrz.model.ResponseData;
//import com.rz.rz_rrz.view.activity.MainAty;
//
///**
// * 作者：Administrator on 2016/8/20 0020 16:07
// * 功能：
// * 说明：
// */
//public class HandleRetCode {
//
//    public static boolean handler(final Context context, ResponseData responseData) {
//        if (responseData.getRet() == ReturnCode.FAIL_REMIND_1) {
//            if (context instanceof Activity) {
//                SVProgressHUD.showInfoWithStatus(context, responseData.getMsg());
//            }
//            return true;
//        }
////        else if (responseData.getRet() == ReturnCode.USER_EXPIRE) {
////            Session.clearShareP();
////            if (context == null) {
////                return true;
////            }
////            Intent intent = new Intent(context, MainAty.class);
////            if (context instanceof Application) {
////                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            }
////            intent.putExtra(IntentKey.KEY_TYPE, ReturnCode.USER_EXPIRE);
////            context.startActivity(intent);
////            return true;
////        }
//        return false;
//    }
//
//    public static boolean handlerExpire(final Context context, ResponseData responseData) {
//        if (responseData.getRet() == ReturnCode.USER_EXPIRE) {
//            if (context == null) {
//                return true;
//            }
//            Intent intent = new Intent(context, MainAty.class);
//            if (context instanceof Application) {
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
//            intent.putExtra(IntentKey.KEY_TYPE, ReturnCode.USER_EXPIRE);
//            context.startActivity(intent);
//            return true;
//        }
//        return false;
//    }
//}
