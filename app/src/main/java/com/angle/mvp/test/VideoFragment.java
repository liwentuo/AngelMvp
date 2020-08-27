package com.angle.mvp.test;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.angle.mvp.R;
import com.m.k.mvp.base.v.BaseSmartFragment1;
import com.m.k.mvp.data.request.GetRequest;
import com.m.k.mvp.data.response.MvpResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends BaseSmartFragment1<VideoBean> {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    public void onResult1(MvpResponse<VideoBean> data) {
        if (data.getCode() == 1) {

            Toast.makeText(getActivity(), data.getData().getList().get(1).getShare_link(), Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();


        }
    }

    @Override
    protected void initView() {
        super.initView();
        GetRequest<VideoBean> request = new GetRequest<>("/app/v_1_6/article/videolist");
        request.setType(VideoBean.class);
        doRequest(request);


    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);

    }
}
