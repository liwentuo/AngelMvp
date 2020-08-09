package com.angle.mvp.base.view;

import com.angle.mvp.base.presenter.IBaseSmartPresenter;
import com.angle.mvp.data.response.MvpResposn;

public abstract class BaseSmartFragment<D> extends MvpBaseFragment<IBaseSmartPresenter<D,?>> implements IBaseSmartView<D,IBaseSmartPresenter<D,?>>{




    @Override
    public void onResult(MvpResposn<D> data) {
       
    }

    @Override
    public IBaseSmartPresenter<D, ?> createPresenter() {
        return null;
    }
}
