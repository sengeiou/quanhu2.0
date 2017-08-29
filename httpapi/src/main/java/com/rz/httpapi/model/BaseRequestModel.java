package com.rz.httpapi.model;

import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.api.callback.RequestCallback;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Gsm on 2017/8/24.
 */
public class BaseRequestModel {

    public void request(Observable observable, final RequestCallback callback) {
        observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null)
                            callback.onFailure(RequestCallback.ERROR_THROWABLE, e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseData responseData) {
                        processSuccess(callback, responseData);
                    }
                });
    }

    private void processSuccess(RequestCallback callback, ResponseData responseData) {
        if (callback == null) return;
        if (responseData.isSuccessful())
            callback.onSuccess(responseData.getData());
        else
            callback.onFailure(responseData.getRet(), responseData.getMsg());
    }

}
