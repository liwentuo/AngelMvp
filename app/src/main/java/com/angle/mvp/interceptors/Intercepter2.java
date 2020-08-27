package com.angle.mvp.interceptors;

import com.m.k.anotaion.OkInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

@OkInterceptor(2)
public class Intercepter2 implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

      return chain.proceed(chain.request());
    }
}
