package com.m.k.mvp.base.p;

import com.m.k.mvp.data.request.MvpRequest;
import com.m.k.mvp.base.IBaseCallBack;
import com.m.k.mvp.base.m.IBaseMode;
import com.m.k.mvp.base.v.IBaseSmartView2;
import com.m.k.mvp.data.response.MvpResponse;

import io.reactivex.rxjava3.disposables.Disposable;

public class BaseSmartPresenter2<D1,D2,V extends IBaseSmartView2<D1,D2,?>> extends BaseSmartPresenter1<D1,V> implements IBaseSmartPresenter2<D1,D2,V> {

    protected Class<D2> mType2;

    public BaseSmartPresenter2() {
    }

    public BaseSmartPresenter2(IBaseMode baseMode) {
        super(baseMode);
    }

    @Override
    public void setType2(Class<D2> type) {
        mType2 = type;
    }

    @Override
    public void doRequest2(MvpRequest<D2> request) {
        mMode.doRequest(getLifecycleProvider(),request, new IBaseCallBack<D2>() {
            @Override
            public void onStart(Disposable disposable) {
               handStart(disposable);
            }
            @Override
            public void onResult(MvpResponse<D2> response) {
                if(mView != null){
                    mView.onResult2(response);
                }
            }
        });
    }

}
