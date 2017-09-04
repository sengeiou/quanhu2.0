package com.rz.circled.presenter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.rz.common.ui.inter.IViewController;
import com.rz.httpapi.api.CallManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by JS01 on 2016/6/7.
 */
public abstract class AbsPresenter implements IPresenter {

    public String TAG;

    protected IViewController view;
    protected Activity activity;
    protected List<Call> callList;

    public AbsPresenter() {
        TAG = getClass().getSimpleName();
    }

    @Override
    public void attachView(IViewController view) {
        Log.d(TAG, "attachView");
        this.view = view;
        if (view instanceof Activity) {
            this.activity = (Activity) view;
        } else if (view instanceof Fragment) {
            this.activity = ((Fragment) view).getActivity();
        } else {
            throw new RuntimeException("Unsupported view type");
        }
        callList = new ArrayList<>();
    }

    @Override
    public void loadData() {
        Log.d(TAG, "loadData");
    }

    @Override
    public void detachView() {
//        Log.d(TAG, "detachView");
        CallManager.cancelAll();
//        activity = null;
//        view = null;
    }
}
