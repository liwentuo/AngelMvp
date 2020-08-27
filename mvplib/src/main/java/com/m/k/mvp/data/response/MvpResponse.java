package com.m.k.mvp.data.response;

import com.m.k.mvp.data.request.RequestType;

public class MvpResponse<D> {

    private ResponseType type = ResponseType.SERVER;// 数据从哪儿回来的？ 服务器，sdcard, 内存

    private RequestType requestType = RequestType.FIRST; // 是第一次请求回来？刷新回来？加载更多回来？

    private String msg; // 服务器给的消息提示

    private int code; // 服务器返回的状态码

    private D data; // 我们真正要的数据






    public boolean isOk(){
       return data != null;
    }


    public ResponseType getType() {
        return type;
    }

    public MvpResponse<D> responseType(ResponseType type) {
        this.type = type;
        return this;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public MvpResponse<D>  requestType(RequestType requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public MvpResponse<D>  message(String msg) {
        this.msg = msg;
        return this;
    }

    public int getCode() {
        return code;
    }

    public MvpResponse<D>  setCode(int code) {
        this.code = code;
        return this;
    }

    public D getData() {
        return data;
    }

    public MvpResponse<D>  setData(D data) {
        this.data = data;
        return this;
    }
}
