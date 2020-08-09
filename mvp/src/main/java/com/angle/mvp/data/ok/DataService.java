package com.angle.mvp.data.ok;

import com.angle.mvp.BuildConfig;
import com.angle.mvp.common.Constrant;
import com.angle.mvp.data.ok.converter.MvpGsonConverterFactory;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

public class DataService {
    private static final int WAIT_TIME = 10;
    private static volatile ApiService sApiService;

    public static ApiService getApiService() {
        if (sApiService == null) {
            synchronized (DataService.class) {
                if (sApiService == null) {
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    if (BuildConfig.DEBUG) {
//                        如果为DEBUG状态输出日志
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    } else {
//                        如果打包上线不输出日志
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
                    }

//                    创建Ok请求设置连接时间，添加日志拦截器
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(WAIT_TIME, TimeUnit.SECONDS)
                            .readTimeout(WAIT_TIME, TimeUnit.SECONDS)
                            .writeTimeout(WAIT_TIME, TimeUnit.SECONDS)
                            .addInterceptor(loggingInterceptor)
                            .build();

//                 创建Retforit返回ApiService
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Constrant.BASE_URL)
                            .addConverterFactory(MvpGsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                            .client(okHttpClient)
                            .build();
                    return retrofit.create(ApiService.class);


                }
            }
        }
        return sApiService;
    }
}
