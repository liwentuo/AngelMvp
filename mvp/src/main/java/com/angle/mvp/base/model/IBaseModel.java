package com.angle.mvp.base.model;

import com.angle.mvp.data.request.MvpRequest;

public interface IBaseModel {
    <T> void doRequest(MvpRequest<T> request,IBaseCallBack<T> callBack);
}
