package com.angle.mvp.data.ok.converter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

public class MvpGsonResponseBodyConverter <T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private final  Type type;

    MvpGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, Type type) {
        this.gson = gson;
        this.adapter = adapter;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        if(type == String.class){
            try{
                MediaType  mediaType = value.contentType();
                Charset charset = null;
                if(mediaType != null){
                    charset = mediaType.charset();
                }
                String json = new String(value.bytes(),charset == null ? StandardCharsets.UTF_8 : charset);

                return (T) handJson(json);
            }finally {
                value.close();
            }

        }else{
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            try {
                T result = adapter.read(jsonReader);
                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    throw new JsonIOException("JSON document was not fully consumed.");
                }
                return result;
            } finally {
                value.close();
            }
        }
    }

    public String handJson(String json){
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