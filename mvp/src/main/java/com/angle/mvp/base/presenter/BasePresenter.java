package com.angle.mvp.base.presenter;

import com.angle.mvp.base.view.IBaseView;

public abstract class BasePresenter<V extends IBaseView>implements IBasePresenter<V> {
    protected V mView;

    @Override
    public void onBindView(V view) {
        mView=view;
    }

    @Override
    public void unBindView() {
        mView=null;
    }
}
