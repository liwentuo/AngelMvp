package com.m.k.mvp.base;

import com.m.k.mvp.data.response.MvpResponse;

import io.reactivex.rxjava3.disposables.Disposable;

public interface IBaseCallBack<T>{

    void onResult(MvpResponse<T> response);

    default void onStart(Disposable disposable){
    }

}
