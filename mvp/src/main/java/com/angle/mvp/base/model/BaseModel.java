package com.angle.mvp.base.model;

import com.angle.mvp.data.entity.HttpResult;
import com.angle.mvp.data.ok.DataService;
import com.angle.mvp.data.request.MvpRequest;
import com.angle.mvp.data.response.MvpResposn;
import com.angle.mvp.util.ParameterizedTypeImpl;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public  class BaseModel implements IBaseModel {
    public Consumer consumer = o -> { };

    @Override
    public <T> void doRequest(MvpRequest<T> request, IBaseCallBack<T> callBack) {
        doRequest(request, callBack, consumer);
    }

    public <T> void doRequest(MvpRequest<T> request, IBaseCallBack<T> callBack, Consumer<MvpResposn<T>> dobackground) {
       switch (request.getRequestMethod()){
           case GET:
              doObserverable(request,callBack,dobackground, DataService.getApiService().doGet(request.getUrl(),request.getParams()));
               break;
           case POST:
               doObserverable(request,callBack,dobackground, DataService.getApiService().doPost(request.getUrl(),request.getParams()));
               break;
       }
    }

    private   <T> void doObserverable(MvpRequest<T> request, IBaseCallBack<T> callBack, Consumer<MvpResposn<T>> dobackground, Observable<String> observable){
        observable
                .map(jsonData(request))
                .doOnNext(dobackground)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MvpResposn<T>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        callBack.dealWithDisposable(d);
                    }

                    @Override
                    public void onNext(@NonNull MvpResposn<T> tMvpResposn) {
                        callBack.onResutl(tMvpResposn);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    callBack.onResutl(new MvpResposn<T>().setMsg(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private <T> Function<String,MvpResposn<T>> jsonData(MvpRequest<T> request) {
        return new Function<String, MvpResposn<T>>() {
            @Override
            public MvpResposn<T> apply(String s) throws Throwable {
//                HttRequest<T>
                ParameterizedTypeImpl type = new ParameterizedTypeImpl(HttpResult.class, new Type[]{request.getType()});
                HttpResult<T> data = new Gson().fromJson(s, type);
                if (data.getCode()==1){

                    if (data.getData()!=null){
                        return new MvpResposn<T>().setCode(data.getCode()).setData(data.getData());
                    }else {
                        return new MvpResposn<T>().setCode(data.getCode()).setMsg("服务器异常");
                    }
                }else{
                    return new MvpResposn<T>().setMsg(data.getMessage());
                }
            }
        };
    }
}
