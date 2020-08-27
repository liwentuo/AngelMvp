package com.m.k.mvp.data.request;

public class PostRequest<T> extends MvpRequest<T> {
    public PostRequest(String url) {
        super(url);




    }

    public PostRequest() {
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }
}
