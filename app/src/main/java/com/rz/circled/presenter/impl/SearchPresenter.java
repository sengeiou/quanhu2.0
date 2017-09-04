package com.rz.circled.presenter.impl;

import android.content.Context;

import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.ui.inter.IViewController;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.Http;

/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchPresenter extends GeneralPresenter {

    private ApiService mUserService;
    private IViewController mView;
    private Context mContext;

    @Override
    public Object getCacheData() {
        return null;
    }

    @Override
    public void attachView(IViewController view) {
        this.mView = view;
        mUserService = Http.getApiService(ApiService.class);
        mContext = getContext(mView);
    }

    @Override
    public void detachView() {

    }

    public void searchPerson(String keyWord) {

    }
}
