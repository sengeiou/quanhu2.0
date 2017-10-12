package com.rz.circled.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.telecom.Call;
import android.view.View;

import com.rz.common.ui.inter.IViewController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/13 0013.
 */
public abstract class GeneralPresenter<T> extends AbsPresenter {


    public List<Call> calls = new ArrayList<>();

    /**
     * 带缓存的数据访问
     */
//    public abstract T loadDataReturn();
    public abstract T getCacheData();

    public void loadData(boolean loadMore) {

    }

    public void loadData() {
    }

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
