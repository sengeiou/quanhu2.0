package com.rz.sgt.jsbridge;


import org.greenrobot.eventbus.EventBus;

/**
 * 服务完成后的消息发送类
 */

public class JsEvent {

    public long invokeId;
    public String invokeName;
    public Object object;

    /**
     * 0成功，1失败，2取消
     */
    public int resultCode = -1;


    /**
     * 主要由回调调用(非ui服务事件以及可能会交叉的服务事件如充值和打赏（打赏中包含充值），必须传入invokeId)
     * 注意，当该服务被其他服务嵌套调用的时候，需要指定invokeId,防止服务回调交叉。
     *
     * @param invokeId
     * @param object
     * @param resultCode
     */
    public static void callJsEvent(long invokeId, Object object, int resultCode) {
        if (invokeId == 0) {
            return;
        }
        JsEvent jsEvent = new JsEvent();
        jsEvent.invokeId = invokeId;
        jsEvent.object = object;
        jsEvent.resultCode = resultCode;
        EventBus.getDefault().post(jsEvent);
    }

    /**
     * 主要由回调调用,Ui服务组件可以调用该方法，否则必须传入invokeId
     *
     * @param object
     * @param resultCode
     */
    public static void callJsEvent(Object object, int resultCode) {
        callJsEvent(-1, object, resultCode);
    }

    public static void callJsEvent(Object object, boolean result) {
        callJsEvent(object, result ? BaseParamsObject.RESULT_CODE_SUCRESS : BaseParamsObject.RESULT_CODE_FAILED);
    }

//    /**
//     * 主要由主动调用
//     *
//     * @param invokeName
//     * @param object
//     * @param resultCode
//     */
//    public static void callJsEvent(String invokeName, Object object, int resultCode) {
//        JsEvent jsEvent = new JsEvent();
//        jsEvent.invokeName = invokeName;
//        jsEvent.object = object;
//        jsEvent.resultCode = resultCode;
//        EventBus.getDefault().post(jsEvent);
//    }

//    public static void callJsEventByResult(Activity activity, long invokeId, String object, int resultCode) {
//        Intent i = new Intent();
//        i.putExtra("invokeId", invokeId);
//        i.putExtra("info", object);
//        i.putExtra("resultCode", resultCode);
//        activity.setResult(resultCode, i);
//    }

}
