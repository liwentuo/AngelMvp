package com.angle.mvp.data.request;

public class GetRequst<T> extends MvpRequest<T> {
//    创建GET请求调用父类构造
    public GetRequst(String url) {
        super(url);
    }
// 默认请求类型为GET
    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.GET;
    }
}
