package com.angle.mcmvp.vedio;

import com.angle.mvp.base.model.BaseModel;
import com.angle.mvp.base.model.IBaseCallBack;
import com.angle.mvp.base.model.IBaseModel;
import com.angle.mvp.base.presenter.BasePresenter;
import com.angle.mvp.data.request.GetRequst;
import com.angle.mvp.data.response.MvpResposn;
import com.angle.mvp.util.ParamsUtils;

import io.reactivex.rxjava3.disposables.Disposable;

public class VedioPresenter extends BasePresenter<VedioConstract.IVideoView> implements VedioConstract.IVedioPresenter {
  private IBaseModel model;

    public VedioPresenter() {
        model=new BaseModel();
    }

    @Override
    public void deRequest() {
        GetRequst<Video> requst = new GetRequst<>("/app/v_1_6/article/videolist");
        requst.setParams(ParamsUtils.getCommonParams());
        requst.setType(Video.class);
        model.doRequest(requst, new IBaseCallBack<Video>() {
            @Override
            public void onResutl(MvpResposn<Video> data) {
                mView.onResut(data);
            }

            @Override
            public void dealWithDisposable(Disposable disposable) {

            }
        });
    }
}
