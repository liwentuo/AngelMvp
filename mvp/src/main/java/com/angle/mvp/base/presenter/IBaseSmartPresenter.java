package com.angle.mvp.base.presenter;

import com.angle.mvp.base.view.IBaseSmartView;
import com.angle.mvp.data.request.MvpRequest;

public interface IBaseSmartPresenter<D,V extends IBaseSmartView<D,?>> extends IBasePresenter<V> {
    void setType(Class<D> type);
    void doRequest(MvpRequest<D> request);
}
