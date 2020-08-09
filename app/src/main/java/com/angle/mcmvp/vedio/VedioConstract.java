package com.angle.mcmvp.vedio;

import com.angle.mvp.base.presenter.IBasePresenter;
import com.angle.mvp.base.view.IBaseView;
import com.angle.mvp.data.request.MvpRequest;
import com.angle.mvp.data.response.MvpResposn;

public interface VedioConstract {
      interface IVedioPresenter extends IBasePresenter<IVideoView>{
           void deRequest();
      }
      interface IVideoView extends IBaseView<IVedioPresenter>{
          void onResut(MvpResposn<Video> data);

      }
}
