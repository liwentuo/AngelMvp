package com.angle.mvp.base.view;

import com.angle.mvp.base.presenter.IBasePresenter;

public interface IBaseView<P extends IBasePresenter> {
    P createPresenter();
}
