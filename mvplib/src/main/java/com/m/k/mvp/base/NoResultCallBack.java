package com.m.k.mvp.base;

import com.m.k.mvp.data.response.MvpResponse;

import io.reactivex.rxjava3.disposables.Disposable;

public class NoResultCallBack<T> implements IBaseCallBack<T> {
    @Override
    public void onResult(MvpResponse<T> response) {

    }

    @Override
    public void onStart(Disposable disposable) {

    }



}
