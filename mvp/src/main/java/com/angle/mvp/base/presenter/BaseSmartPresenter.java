package com.angle.mvp.base.presenter;

import com.angle.mvp.base.model.BaseModel;
import com.angle.mvp.base.model.IBaseCallBack;
import com.angle.mvp.base.model.IBaseModel;
import com.angle.mvp.base.view.IBaseSmartView;
import com.angle.mvp.data.request.MvpRequest;
import com.angle.mvp.data.response.MvpResposn;

import io.reactivex.rxjava3.disposables.Disposable;

public abstract class BaseSmartPresenter<D ,V extends IBaseSmartView<D,?>>extends BasePresenter<V> implements IBaseSmartPresenter<D,V> {
    private IBaseModel model;
    private Class<D> type;

    public BaseSmartPresenter() {
        model=new BaseModel();
    }

    @Override
    public void setType(Class<D> type) {
        this.type=type;
    }

    @Override
    public void doRequest(MvpRequest<D> request) {
        request.setType(type);

         model.doRequest(request, new IBaseCallBack<D>() {
             @Override
             public void onResutl(MvpResposn<D> data) {
                   mView.onResult(data);
             }

             @Override
             public void dealWithDisposable(Disposable disposable) {

             }
         });


    }


}
