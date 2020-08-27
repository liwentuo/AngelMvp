package com.m.k.mvp.base.v;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.m.k.mvp.data.request.MvpRequest;
import com.m.k.mvp.base.p.BaseSmartPresenter2;

import java.lang.reflect.ParameterizedType;

public abstract class BaseSmartFragment2<D1,D2> extends MvpBaseFragment<BaseSmartPresenter2<D1,D2,?>> implements IBaseSmartView2<D1,D2, BaseSmartPresenter2<D1,D2,?>>{


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();

        Class<D1> aClass = (Class<D1>) superClass.getActualTypeArguments()[0];
        Class<D2> aClass2 = (Class<D2>) superClass.getActualTypeArguments()[1];

        mPresenter.setType(aClass);
        mPresenter.setType2(aClass2);
    }

    protected void doRequest1(MvpRequest<D1> request){
        mPresenter.doRequest(request);
    }

    protected void doRequest2(MvpRequest<D2> request){
        mPresenter.doRequest2(request);
    }

    @Override
    public BaseSmartPresenter2<D1, D2, ?> createPresenter() {
        return new BaseSmartPresenter2<>();
    }
}
