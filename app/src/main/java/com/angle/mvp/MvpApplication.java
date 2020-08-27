package com.angle.mvp;

import android.app.Application;

import com.angle.mvp.util.ParamsUtils;
import com.m.k.mvp.manager.MvpManager;

public class MvpApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MvpManager.setParamsGetter(ParamsUtils::getCommonParams);
    }
}
