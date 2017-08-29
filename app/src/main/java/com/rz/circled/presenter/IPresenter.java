package com.rz.circled.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.rz.common.ui.inter.IViewController;

/**
 * Created by JS01 on 2016/6/7.
 * MVP Presenter层，加载数据并控制View层
 */
public abstract class IPresenter {

    public static String TAG = "IPresenter";

    /**
     * 与View（Activity或Fragment）关联，在onCreate中调用，
     *
     * @param view
     */
    public abstract void attachView(IViewController view);

    /**
     * 与View分离，在onDestory调用
     */
    public abstract void detachView();

    public Context getContext(IViewController viewController) {
        if (viewController != null) {
            if (viewController instanceof Context) {
                return (Context) viewController;
            } else if (viewController instanceof Fragment) {
                return ((Fragment) viewController).getActivity();
            } else if (viewController instanceof Activity) {
                return (Activity) viewController;
            } else if (viewController instanceof View) {
                return ((View) viewController).getContext();
            }
        }
        return null;
    }

}
