package com.angle.mvp.common;

public interface Constrant {


    String BASE_URL = "https://www.seetao.com";
    String VALUE_FROM = "ios";
    String VALUE_LANG = "zh";



    interface  URL{
        String LOGIN = "app/v_1_7_2/user/login2";
    }


    interface  RequestKey{


        String KEY_FROM = "from";
        String KEY_LANG = "lang";
        String KEY_TIMESTAMP = "timestamp";
        String KEY_NONCE = "nonce";
        String KEY_SIGNATURE = "signature";


        String KEY_USER_ACCOUNT = "username";
        String KEY_USER_PASSWORD = "password";
        String KEY_CODE = "code";



    }
}
