package com.rz.sgt.jsbridge;

import android.app.Activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注册清单
 */

public class RegisterList {

    public static List<Class<? extends ServerHandler>> list = new ArrayList();

    /**
     * 获取并实例化所有的注册对象
     *
     * @param mActivity
     * @return
     */
    public static Map<String, ServerHandler> getAllRegisterHandler(Activity mActivity) {
        Map<String, ServerHandler> handlers = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            ServerHandler serverHandler = instance(list.get(i), mActivity);
            if (serverHandler != null) {
                handlers.put(serverHandler.getInvokeName(), serverHandler);
            }
        }
        return handlers;
    }

    /**
     * 获取部分实例化所有的注册对象,根据注入的position来获取
     *
     * @param mActivity
     * @return
     */
    public static Map<String, ServerHandler> getAllRegisterByIndexs(Activity mActivity, int[] indexs) {
        Map<String, ServerHandler> handlers = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            for (int index : indexs) {
                if (index == i) {
                    ServerHandler serverHandler = instance(list.get(i), mActivity);
                    if (serverHandler != null) {
                        handlers.put(serverHandler.getInvokeName(), serverHandler);
                    }
                }
            }
        }
        return handlers;
    }

    /**
     * 注册一个服务类
     *
     * @param serverHandlerClass
     */
    public static void registerServerHandlerClass(Class<? extends ServerHandler> serverHandlerClass) {
        list.add(serverHandlerClass);
    }

    /**
     * 根据服务类生成服务对象
     *
     * @param cls
     * @param mActivity
     * @return
     */
    public static ServerHandler instance(Class<? extends ServerHandler> cls, Activity mActivity) {
        Constructor<? extends ServerHandler> constructor = null;
        ServerHandler serverHandler = null;
        try {
            constructor = cls.getConstructor(Activity.class);
            constructor.setAccessible(true);
            serverHandler = constructor.newInstance(mActivity);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return serverHandler;
    }

}
