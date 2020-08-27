package com.angle.mvp.convertor;

import android.text.TextUtils;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.m.k.mvp.data.ok.converter.MvpGsonResponseBodyConverter;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class JDGsonResponseBodyConverter<T> extends MvpGsonResponseBodyConverter<T> {


    public JDGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, Type type) {
        super(gson, adapter, type);
    }

    @Override
    public String handJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            if(!jsonObject.isNull("code")){
                int code = jsonObject.getInt("code");
                if(code != 1){ // 失败
                    if(!jsonObject.isNull("data")){
                        String data = jsonObject.getString("data");
                        if(TextUtils.isEmpty(data)){ // 如果 data 是一个空字符串
                            jsonObject.remove("data");
                            return jsonObject.toString();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;

    }
}
