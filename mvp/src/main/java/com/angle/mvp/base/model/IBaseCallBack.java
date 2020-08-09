package com.angle.mvp.base.model;

import com.angle.mvp.data.response.MvpResposn;

import io.reactivex.rxjava3.disposables.Disposable;

public interface IBaseCallBack <T>{
    void onResutl(MvpResposn<T> data);//接口回掉传递数据

    void dealWithDisposable(Disposable disposable);//处理medal与view之间联系
}
