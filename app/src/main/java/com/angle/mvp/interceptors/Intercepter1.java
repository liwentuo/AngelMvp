package com.angle.mvp.interceptors;

import com.m.k.anotaion.OkInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

@OkInterceptor(1)
public class Intercepter1 implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }
}
