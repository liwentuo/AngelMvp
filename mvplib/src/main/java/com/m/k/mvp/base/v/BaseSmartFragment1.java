package com.m.k.mvp.base.v;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.m.k.mvp.data.request.MvpRequest;
import com.m.k.mvp.base.p.BaseSmartPresenter1;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseSmartFragment1<D> extends MvpBaseFragment<BaseSmartPresenter1<D,?>> implements IBaseSmartView1<D, BaseSmartPresenter1<D,?>> {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Type type = getClass().getGenericSuperclass();
        if(type instanceof ParameterizedType){
            ParameterizedType superClass = (ParameterizedType) type;
            Class<D> aClass = (Class<D>) superClass.getActualTypeArguments()[0];
            mPresenter.setType(aClass);
        }


    }

    public void doRequest(MvpRequest<D> request) {
        mPresenter.doRequest(request);
    }

    @Override
    public BaseSmartPresenter1<D,?> createPresenter() {
        return new BaseSmartPresenter1<>();
    }


}
