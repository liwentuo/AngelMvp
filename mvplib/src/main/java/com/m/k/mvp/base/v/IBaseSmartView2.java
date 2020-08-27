package com.m.k.mvp.base.v;

import com.m.k.mvp.base.p.IBaseSmartPresenter2;
import com.m.k.mvp.data.response.MvpResponse;

public interface IBaseSmartView2<D1,D2,P extends IBaseSmartPresenter2<D1,D2,?>> extends IBaseSmartView1<D1,P> {
    void onResult2(MvpResponse<D2> response);
}
