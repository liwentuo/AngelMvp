package com.angle.mcmvp.vedio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.angle.mcmvp.R;
import com.angle.mvp.base.view.MvpBaseFragment;
import com.angle.mvp.data.response.MvpResposn;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends MvpBaseFragment<VedioConstract.IVedioPresenter>implements VedioConstract.IVideoView {

    @Override
    public void onResut(MvpResposn<Video> data) {
        if (data.getCode()==1){
            Toast.makeText(getActivity(), data.getData().getList().get(1).getShare_link(), Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void initView() {
   mPresnter.deRequest();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    public VedioConstract.IVedioPresenter createPresenter() {
        return new VedioPresenter();
    }
}
