package com.angle.mvp.data.ok;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {
    @POST
    @FormUrlEncoded
    Observable<String> doPost(@Url String url, @FieldMap HashMap<String, Object> params);

    @GET
    Observable<String> doGet(@Url String url, @QueryMap HashMap<String, Object> params);
}
