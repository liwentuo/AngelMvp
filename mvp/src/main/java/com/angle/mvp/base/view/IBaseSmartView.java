package com.angle.mvp.base.view;

import com.angle.mvp.base.presenter.IBasePresenter;
import com.angle.mvp.base.presenter.IBaseSmartPresenter;
import com.angle.mvp.data.response.MvpResposn;

public interface IBaseSmartView<D,P extends IBaseSmartPresenter> extends IBaseView<P> {
    void onResult(MvpResposn<D> data);

}
