package com.m.k.mvp.base.v;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.m.k.mvp.base.BaseActivity;
import com.m.k.mvp.base.BaseView;
import com.m.k.mvp.base.p.IBasePresenter;
import com.m.k.mvp.widgets.MvpLoadingView;

public abstract class MvpBaseActivity<P extends IBasePresenter> extends BaseActivity implements BaseView,IBaseView<P> {

    private MvpLoadingView mLoadingView;

    protected P mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         mPresenter = createPresenter();
         mPresenter.bindView(this);

         loadData();
    }

    protected abstract void loadData();

    @Override
    public void setLoadView(MvpLoadingView loadView) {
        mLoadingView = loadView;
    }

    @Override
    public MvpLoadingView getLoadingView() {
        return mLoadingView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unBind();
    }
}
