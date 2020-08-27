package com.m.k.mvp.data.request;


import com.m.k.mvp.manager.MvpManager;

import java.util.HashMap;

public class MvpRequest<T> {

    protected String url;
    protected RequestType requestType = RequestType.FIRST;  // 第一次请求 0，刷新1 加载更多2
    protected RequestMethod requestMethod; // 1 post,2 get
    protected HashMap<String, Object> params; // 请求参数
    protected HashMap<String, Object> headers; // 请求头
    private Class<T> type;

    protected boolean isEnableCancel;  // 网络请求是否支持取消


    public MvpRequest() {
        if (MvpManager.getParamsGetter() != null) {
            params = MvpManager.getParamsGetter().getParams();
            headers = MvpManager.getParamsGetter().getHeaders();
        }

    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public MvpRequest(String url) {
        this();
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public HashMap<String, Object> getParams() {
        return params == null ? new HashMap<>() : params;
    }

    public void setParams(HashMap<String, Object> params) {
        if (this.params != null) {
            this.params.putAll(params);
        } else {
            this.params = params;
        }
    }


    public MvpRequest<T> putParams(String key, Object value) {
        if (params == null) {
            params = new HashMap<>();
        }

        params.put(key, value);
        return this;
    }


    public HashMap<String, Object> getHeaders() {
        return headers == null ? new HashMap<>() : headers;
    }

    public void setHeaders(HashMap<String, Object> headers) {
        this.headers = headers;
    }

    public boolean isEnableCancel() {
        return isEnableCancel;
    }

    public void setEnableCancel(boolean enableCancel) {
        isEnableCancel = enableCancel;
    }
}
