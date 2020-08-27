package com.m.k.mvp.data;

import com.google.gson.Gson;
import com.m.k.mvp.data.entity.IMvpEntity;
import com.m.k.mvp.base.IBaseCallBack;
import com.m.k.mvp.base.m.IBaseMode;
import com.m.k.mvp.data.ok.MvpDataService;
import com.m.k.mvp.data.request.MvpRequest;
import com.m.k.mvp.data.response.MvpResponse;
import com.m.k.mvp.utils.ParameterizedTypeImpl;
import com.trello.rxlifecycle4.LifecycleProvider;
import com.trello.rxlifecycle4.RxLifecycle;
import com.trello.rxlifecycle4.android.ActivityEvent;
import com.trello.rxlifecycle4.android.FragmentEvent;
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public  class BaseRepository implements IBaseMode {

    @SuppressWarnings("ALL")
    public Consumer empty = o -> { };
    @SuppressWarnings("ALL")
    @Override
       public <T> void doRequest(LifecycleProvider lifecycleProvider, MvpRequest<T> request, IBaseCallBack<T> callBack) {
        doRequest(lifecycleProvider,request, empty, callBack);
    }


    public <T> void doRequest(LifecycleProvider lifecycleProvider, MvpRequest<T> request, Consumer<MvpResponse<T>> doBackground, IBaseCallBack<T> callBack) {



        switch (request.getRequestMethod()) {
            case GET: {
                doObserverString(lifecycleProvider,request, MvpDataService.getMvpApiService().doGet(request.getUrl(), request.getHeaders(), request.getParams()), doBackground, callBack);
                break;
            }
            case POST: {
                doObserverString(lifecycleProvider,request, MvpDataService.getMvpApiService().doPost(request.getUrl(), request.getHeaders(), request.getParams()), doBackground, callBack);
                break;
            }
        }
    }

    protected  <T> void doObserverString(LifecycleProvider lifecycleProvider, MvpRequest<T> request, Observable<String> observable, Consumer<MvpResponse<T>> consumer, IBaseCallBack<T> callBack) {


        if(request.getType() == null && callBack != null){
            Type [] interfaces =  callBack.getClass().getGenericInterfaces();
            ParameterizedType parameterizedType = (ParameterizedType) interfaces[0];
            request.setType((Class<T>) parameterizedType.getActualTypeArguments()[0]);

        }

        Observable<IMvpEntity<T>> observable1 =  observable.map(json2Data(request));

        doObserver(lifecycleProvider,request,observable1,consumer,callBack);

    }
    protected  <T> void doObserver(LifecycleProvider lifecycleProvider, MvpRequest<T> request, Observable< ? extends IMvpEntity<T>> observable, IBaseCallBack<T> callBack) {
        doObserver(lifecycleProvider,request,observable,empty,callBack);
    }


    @SuppressWarnings("ALL")
    protected  <T> void doObserver(LifecycleProvider lifecycleProvider, MvpRequest<T> request, Observable< ? extends IMvpEntity<T>> observable, Consumer<MvpResponse<T>> consumer, IBaseCallBack<T> callBack) {



        Observable<MvpResponse<T>> observable1 =  observable.map(data2Response(request))
                .doOnNext(consumer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        if(lifecycleProvider != null){

            if(lifecycleProvider instanceof RxAppCompatActivity){
                observable1 = observable1.compose(RxLifecycle.bindUntilEvent(lifecycleProvider.lifecycle(),ActivityEvent.DESTROY));
            }else{
               observable1 =  observable1.compose(RxLifecycle.bindUntilEvent(lifecycleProvider.lifecycle(),FragmentEvent.DESTROY));
        }
        }

        observable1.subscribe(new Observer<MvpResponse<T>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        if(request.isEnableCancel() && callBack != null){
                            callBack.onStart(d);
                        }
                    }

                    @Override
                    public void onNext(@NonNull MvpResponse<T> data) {

                        if(callBack != null)
                            callBack.onResult(data);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        if(callBack != null)
                            callBack.onResult(new MvpResponse<T>().message(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public <T> Function<IMvpEntity<T>, MvpResponse<T>> data2Response(MvpRequest<T> request) {
        return new Function<  IMvpEntity<T>, MvpResponse<T>>() {
            @Override
            public MvpResponse<T> apply(IMvpEntity<T> entity) throws Throwable {

                if (entity.isOk()) {
                    if (entity.getData() != null) {
                        return new MvpResponse<T>().setData(entity.getData()).setCode(entity.getCode()).requestType(request.getRequestType());
                    } else {
                        return new MvpResponse<T>().setCode(entity.getCode()).message("服务器异常").requestType(request.getRequestType());
                    }
                } else {
                    return new MvpResponse<T>().setCode(entity.getCode()).message(entity.getMessage()).requestType(request.getRequestType());
                }
            }
        };
    }


    public <T> Function<String, IMvpEntity<T>> json2Data(MvpRequest<T> request) {
        return new Function<String, IMvpEntity<T>>() {
            @Override
            public IMvpEntity<T> apply(String s) throws Throwable {

                Gson gson = new Gson();
                Class aClass = Class.forName("com.m.k.mvp.data.entity.ProxyEntity");
                ParameterizedTypeImpl type = new ParameterizedTypeImpl(aClass, new Type[]{request.getType()});
                IMvpEntity<T> data = gson.fromJson(s, type);
                return data;
            }
        };
    }

}
