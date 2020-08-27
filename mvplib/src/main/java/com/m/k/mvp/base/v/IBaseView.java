package com.m.k.mvp.base.v;

import android.content.Context;

import com.m.k.mvp.base.p.IBasePresenter;

public interface IBaseView<P extends IBasePresenter>  {
    P createPresenter();
    Context getMvpContent();

}
