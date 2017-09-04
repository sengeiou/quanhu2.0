//package com.rz.circled.presenter;
//
//import com.rz.common.ui.inter.IViewController;
//import com.rz.httpapi.api.ApiService;
//import com.rz.httpapi.api.Http;
//import com.rz.httpapi.api.callback.RequestCallback;
//import com.rz.httpapi.model.BaseRequestModel;
//
///**
// * Created by Gsm on 2017/8/24.
// */
//public class UserPresenter implements IPresenter {
//
//    private ApiService mApiService;
//    private BaseRequestModel mRequestModel;
//
//    @Override
//    public void attachView(IViewController view) {
//        mApiService = Http.getApiService(ApiService.class);
//        mRequestModel = new BaseRequestModel();
//    }
//
//    @Override
//    public void loadData() {
//
//    }
//
//    @Override
//    public void detachView() {
//
//    }
//
//    public void login(int act, String phone, String psw) {
//        mRequestModel.request(mApiService.login(act, phone, psw), new RequestCallback() {
//
//            @Override
//            public void onSuccess(Object o) {
//                if (o != null) {
//                }
//            }
//
//            @Override
//            public void onFailure(int code, String errorInfo) {
//            }
//        });
//    }
//
//
//}
