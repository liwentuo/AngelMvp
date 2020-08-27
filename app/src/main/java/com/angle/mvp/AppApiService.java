package com.angle.mvp;


import com.angle.mvp.entitiy.HttpResult;
import com.angle.mvp.entitiy.User;
import com.m.k.anotaion.ApiService;

import java.util.HashMap;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.POST;


@ApiService
public interface AppApiService {


    @POST()
    Observable<HttpResult<User>> getUser(@FieldMap HashMap<String, Object> map);

}


