package com.m.k.mvp.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class MvpDataFileCacheUtils {


    private static final String SECRET_KEY = "tnKqXmiVZFYXIit9uYmnrg==";
    //

    /**
     * 把 json 转出指定的对象并返回
     *
     * @param tClass  需要转的对象的class
     * @param jsonStr json 串
     */

    public static <T> T convertToDataFromJson(Class<T> tClass, String jsonStr) {

        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }

        Gson gson = new Gson();

        return gson.fromJson(jsonStr, tClass);
    }




    /**
     * 把 json 转出指定的对象List<T> 这种类型并返回，比如 List<Person>
     *
     * @param tClass  ：List里面泛型的class,比如 Person.class
     * @param jsonStr ： json 串
     */

    public static <T> List<T> convertToListFromJson(Class<T> tClass, String jsonStr) {

        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }


        Gson gson = new Gson();


        ParameterizedTypeImpl parameterizedType = new ParameterizedTypeImpl(List.class, new Type[]{tClass});

        return gson.fromJson(jsonStr, parameterizedType);
    }


    /**
     * Map<String,Map<String,List<String>>>
     * <p>
     * ParameterizedTypeImpl listType = new ParameterizedTypeImpl(List.class,new Type[]{String.class}); // List<String>
     * <p>
     * ParameterizedTypeImpl mapInnerType = new ParameterizedTypeImpl(Map.class,new Type[]{String.class,listType}); // Map<String,List<String>
     * <p>
     * ParameterizedTypeImpl parameterizedType = new ParameterizedTypeImpl(Map.class,new Type[]{String.class,mapInnerType}); Map<String,Map<String,List<String>>
     */

    //


    ///
    public static <K, V> Map<K, V> convertToMapFromJson(Class<K> keyClass, Class<V> vclass, String jsonStr) {


        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }

        Gson gson = new Gson();

        ParameterizedTypeImpl parameterizedType = new ParameterizedTypeImpl(Map.class, new Type[]{keyClass, vclass});


        return gson.fromJson(jsonStr, parameterizedType);
    }


    // 把一个对象转出json
    public static String convertToJsonFromData(Object data) {

        if (data == null) return null;

        Gson gson = new Gson();

        return gson.toJson(data);

    }


    /**
     * 把一个对象保存到文件里面，
     *
     * @param file 保存文件
     * @param data 需要保存的对象
     */
    public static void saveDataToFile(File file, Object data) {

        String json = convertToJsonFromData(data);

        if (TextUtils.isEmpty(json)) {
            return;
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);

            outputStream.write(json.getBytes("utf-8"));


            outputStream.flush();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void saveEncryptedDataToFile(File file, Object data) {

        String json = convertToJsonFromData(data);

        // 加密





        if (TextUtils.isEmpty(json)) {
            return;
        }

        FileOutputStream outputStream = null;
        try {

            json = EncryptUtils.encrypt(SECRET_KEY,json); // 加密

            outputStream = new FileOutputStream(file);

            outputStream.write(json.getBytes("utf-8"));


            outputStream.flush();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 从文件里面读取出对象
     *
     * @param file
     * @param tClass
     * @param <T>
     * @return
     */

    public static <T> T getencryptedDataFromFile(File file, Class<T> tClass) {

        String json= readFromFile(file);


        try {

            json = EncryptUtils.decrypt(SECRET_KEY,json);// 解密
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

        return convertToDataFromJson(tClass, json);
    }

    public static <T> T getDataFromFile(File file, Class<T> tClass) {

        return convertToDataFromJson(tClass, readFromFile(file));
    }


    /**
     * 从文件里面读取出对象，这个对象是一个List<T>
     *
     * @param file
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> getListFromFile(File file, Class<T> tClass) {

        return convertToListFromJson(tClass, readFromFile(file));
    }


    private static String readFromFile(File file) {
        if (file == null || !file.exists()) {
            return null;
        }

        BufferedReader reader = null;


        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));

            int c;
            StringBuilder buffer = new StringBuilder();
            while ((c = reader.read()) != -1) {
                buffer.append((char) c);
            }

            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    public static class ParameterizedTypeImpl implements ParameterizedType {


        // List<Column>

        private final Class raw; // List.class
        private final Type[] args; // {Column.class}


        public ParameterizedTypeImpl(Class raw, Type[] args) {
            this.raw = raw;
            this.args = args;
        }

        @NonNull
        @Override
        public Type[] getActualTypeArguments() {
            return args;// { Column.class}
        }

        @NonNull
        @Override
        public Type getRawType() {
            return raw; // List.class
        }

        @Nullable
        @Override
        public Type getOwnerType() {
            return null;
        }
    }


}
