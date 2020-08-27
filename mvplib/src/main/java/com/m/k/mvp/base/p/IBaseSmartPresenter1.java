package com.m.k.mvp.base.p;

import com.m.k.mvp.data.request.MvpRequest;
import com.m.k.mvp.base.v.IBaseSmartView1;

public interface IBaseSmartPresenter1<D,V extends IBaseSmartView1<D,?>> extends IBasePresenter<V>{
    void setType(Class<D> type);
    void doRequest(MvpRequest<D> request);



}
