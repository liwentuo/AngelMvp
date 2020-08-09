package com.angle.mvp.data.response;

import com.angle.mvp.data.request.RequestType;

public class MvpResposn <D>{
  private String msg;
  private int code;
  private D data;
  private RequestType requestType=RequestType.FIRST;
  private ResposneType resposneType=ResposneType.SERVICE;

    public String getMsg() {
        return msg;
    }

    public MvpResposn<D> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public int getCode() {
        return code;
    }

    public MvpResposn <D> setCode(int code) {
        this.code = code;
        return this;
    }

    public D getData() {
        return data;
    }

    public MvpResposn <D> setData(D data) {
        this.data = data;
        return this;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public MvpResposn <D> setRequestType(RequestType requestType) {
        this.requestType = requestType;
        return this;
    }

    public ResposneType getResposneType() {
        return resposneType;
    }

    public MvpResposn <D> setResposneType(ResposneType resposneType) {
        this.resposneType = resposneType;
        return this;
    }
}
