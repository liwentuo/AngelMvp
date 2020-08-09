package com.angle.mvp.data.request;

import java.util.HashMap;

//封装请求对象
public class MvpRequest<T> {
    protected String url;//后缀Url
    protected HashMap<String, Object> params;//请求参数
    protected RequestMethod requestMethod;//请求方式GET?还是Post?
    protected RequestType requestType = RequestType.FIRST;//请求类型是第一次请求？还是加载更多？还是刷新？
    public Class<T> type;//请求具体类型

    public MvpRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }
}
