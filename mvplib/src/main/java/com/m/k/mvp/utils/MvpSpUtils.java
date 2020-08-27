package com.m.k.mvp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.m.k.mvp.manager.MvpManager;

import java.lang.reflect.Type;
import java.security.KeyException;

public class MvpSpUtils {

    private static final String   DEFAULT_SP_NAME = "mvp_sp_config";
    private static final int TYPE_COMMIT = 0X100;
    private static final int TYPE_APPLY = 0X101;


    public static String getString(String key){
       return getString(DEFAULT_SP_NAME, key);
    }

    public static String getString(String spName,String key){
        return MvpManager.getContext().getSharedPreferences(spName,Context.MODE_PRIVATE).getString(key,null);
    }


    public static long getLong(String key){
        return getLong(DEFAULT_SP_NAME, key);
    }

    public static long getLong(String spName,String key){
        return MvpManager.getContext().getSharedPreferences(spName,Context.MODE_PRIVATE).getLong(key,-1);
    }


    public static int getInt(String key){
        return getInt(DEFAULT_SP_NAME, key);
    }

    public static int getInt(String spName,String key){
        return MvpManager.getContext().getSharedPreferences(spName,Context.MODE_PRIVATE).getInt(key,-1);
    }

    public static void remove(String key){

        remove(DEFAULT_SP_NAME,key);
    }


    public static void remove(String spName,String key){
       SharedPreferences preferences =  MvpManager.getContext().getSharedPreferences(spName,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.remove(key);
        editor.commit();
    }


    public static void saveCommit(String key, Object value){
        saveString(DEFAULT_SP_NAME,key,value,TYPE_COMMIT);
    }


    public static void saveCommit(String spName, String key, Object value){
        saveString(spName,key,value,TYPE_COMMIT);
    }

    public static void saveApply(String spName, String key, Object value){
        saveString(spName,key,value,TYPE_APPLY);
    }

    public static void saveApply(String key, Object value){
        saveString(DEFAULT_SP_NAME,key,value,TYPE_APPLY);
    }

    private static void saveString(String spName,String key,Object value,int type){

        SharedPreferences preferences =  MvpManager.getContext().getSharedPreferences(spName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if(value instanceof String){
            editor.putString(key,value.toString());
        }else if(value instanceof Float){
            editor.putFloat(key, (Float) value);
        }else if(value instanceof Integer){
            editor.putInt(key, (Integer) value);
        }else if(value instanceof Boolean){
            editor.putBoolean(key, (Boolean) value);
        }else if(value instanceof Long){
            editor.putLong(key, (Long) value);
        }

        if(type == TYPE_APPLY){
            editor.apply();
        }else{
            editor.commit();
        }
    }
}
