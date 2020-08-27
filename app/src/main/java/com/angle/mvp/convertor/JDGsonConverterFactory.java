package com.angle.mvp.convertor;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.m.k.anotaion.GsonConverter;
import com.m.k.mvp.data.ok.converter.MvpGsonConverterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

@GsonConverter
public class JDGsonConverterFactory extends MvpGsonConverterFactory {


    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new JDGsonResponseBodyConverter<>(gson, adapter,type);


    }
}
