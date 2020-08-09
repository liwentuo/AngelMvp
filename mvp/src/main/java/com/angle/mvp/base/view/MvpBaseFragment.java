package com.angle.mvp.base.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.angle.mvp.base.presenter.IBasePresenter;

public abstract class MvpBaseFragment<P extends IBasePresenter> extends BaseFragmet implements IBaseView<P>{
    protected P mPresnter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresnter=createPresenter();
        mPresnter.onBindView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresnter.unBindView();
    }
}
