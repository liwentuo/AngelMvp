package com.angle.mvp.data.request;

public class PostRequst<T> extends MvpRequest<T> {
    public PostRequst(String url) {
        super(url);
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }
}
