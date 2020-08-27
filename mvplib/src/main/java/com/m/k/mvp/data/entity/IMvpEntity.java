package com.m.k.mvp.data.entity;


public interface IMvpEntity<T> {

    boolean isOk(); //
    String getMessage();
    T getData();
    int getCode(); //


}
