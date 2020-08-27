package com.m.k.mvp.data.ok;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.cunoraz.gifview.library.BuildConfig;
import com.m.k.mvp.data.ok.converter.MvpGsonConverterFactory;
import com.m.k.mvp.manager.MvpManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;




public class MvpDataService {

    private static final long TIME_OUT = 20000;


    private static volatile MvpApiService mService;
    private static volatile Object mAppApiService;


    public static MvpApiService getMvpApiService() {
        if (mService == null) {
            synchronized (MvpDataService.class) {
                if (mService == null) {

                    String baseUrl = "";
                    Class appApiServiceClass = null;
                    Converter.Factory factory = null;

                    ArrayList<Interceptor> interceptors = null;



                    try {

                        ApplicationInfo appInfo = MvpManager.getContext().getPackageManager().getApplicationInfo(MvpManager.getContext().getPackageName(), PackageManager.GET_META_DATA);


                        Class config = Class.forName(appInfo.metaData.getString("mvpConfigPackageName") + "." + appInfo.metaData.getString("mvpConfigClassName"));

                        Field urlField =  config.getDeclaredField(appInfo.metaData.getString("baseUrl"));
                        baseUrl = (String) urlField.get(null);


                        Field serviceField = config.getField(appInfo.metaData.getString("appApiService"));
                        appApiServiceClass = (Class) serviceField.get(null);



                        Field converterField = config.getField(appInfo.metaData.getString("appGSONConverter"));
                        String converterName = (String) converterField.get(null);
                        factory = (Converter.Factory) Class.forName(converterName).newInstance();



                        interceptors = createInterceptors(config.getFields(),appInfo.metaData.getString("appOkInterceptorPrefix"));



                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    /**
                     * 注意，如果有大文件下载，或者 response 里面的body 很大，要么不加HttpLoggingInterceptor 拦截器
                     * 如果非要加，日志级别不能是 BODY,否则容易内存溢出。
                     */
                    if (BuildConfig.DEBUG) {
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                    } else {
                        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
                    }

                    OkHttpClient.Builder builder = new OkHttpClient.Builder();

                    if(interceptors != null){

                        for(Interceptor interceptor : interceptors){
                            builder.addInterceptor(interceptor);
                        }
                    }

                    builder.addInterceptor(logging);
                    builder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                            .writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                            .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                            .build();
//

                    Retrofit mRetrofit = new Retrofit.Builder()
                            .client(builder.build())
                            .addConverterFactory(factory == null ? new MvpGsonConverterFactory() : factory) // 帮我们把json 窜转为 entity 对象
                            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // 结合 rxjava 使用
                            .baseUrl(baseUrl)
                            .build();

                    mService = mRetrofit.create(MvpApiService.class);

                    if(appApiServiceClass != null){
                        mAppApiService = mRetrofit.create(appApiServiceClass);
                    }



                }

            }
        }

        return mService;
    }


    public static Object getAppApiService(){

        return mAppApiService;
    }


    private static  ArrayList<Interceptor> createInterceptors(Field [] fields,String prefix){
        ArrayList<Field> fieldArrayList = new ArrayList<>();
        for(Field field : fields){
            if(field.getName().contains(prefix)){
                fieldArrayList.add(field);
            }
        }


        Collections.sort(fieldArrayList, new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });


        ArrayList<Interceptor> interceptors = new ArrayList<>(fieldArrayList.size());

        Interceptor interceptor;
        for(Field field : fieldArrayList){

            try {
                interceptor = (Interceptor) Class.forName(field.get(null).toString()).newInstance();
                interceptors.add(interceptor);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

        }

        return interceptors;
    }

}
