package com.m.k.mvp.base.v;

import com.m.k.mvp.base.p.IBaseSmartPresenter1;
import com.m.k.mvp.data.response.MvpResponse;

public interface IBaseSmartView1<D,P extends IBaseSmartPresenter1> extends IBaseView<P> {
    void onResult1(MvpResponse<D> response);
}
