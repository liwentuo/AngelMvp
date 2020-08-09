package com.angle.mvp.base.presenter;

import com.angle.mvp.base.view.IBaseView;

public interface IBasePresenter<V extends IBaseView> {
    void onBindView(V view);

    void unBindView();
}
