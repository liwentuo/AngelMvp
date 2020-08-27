package com.angle.mvp;

import com.m.k.anotaion.BaseUrl;

public interface Constrant {

    @BaseUrl
    String BASE_URL = "https://www.seetao.com";
    String BASE_URL_Test = "https://www.seetao.com";

    String VALUE_FROM = "ios";
    String VALUE_LANG = "zh";


    interface URL {
        String LOGIN = "app/v_1_7_2/user/login2";
        String GET_USER = "/api/user/getuserinfo";
        String COLUMN_MANAGER = "/api/column/columnmanagelist";
        String RECOMMEND_LIST = "/app/v_1_6/article/recommendlist";
    }


    interface RequestKey {


        String KEY_FROM = "from";
        String KEY_LANG = "lang";
        String KEY_TIMESTAMP = "timestamp";
        String KEY_NONCE = "nonce";
        String KEY_SIGNATURE = "signature";


        String KEY_USER_ACCOUNT = "username";
        String KEY_USER_PASSWORD = "password";
        String KEY_CODE = "code";
        String KEY_TOKEN = "token";

        String KEY_START = "start";
        String KEY_NUMBER = "number";
        String KEY_POINT_TIME = "point_time";
        String KEY_COLUMN_ID = "id";


    }
}
