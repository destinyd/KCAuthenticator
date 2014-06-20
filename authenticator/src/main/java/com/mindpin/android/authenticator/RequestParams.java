package com.mindpin.android.authenticator;

import com.github.kevinsawicki.http.HttpRequest;

/**
 * Created by dd on 14-6-11.
 */
public class RequestParams {
    public HttpRequest httpRequest;
    public RequestCallback requestCallback;

    public RequestParams(HttpRequest httpRequest, RequestCallback requestCallback) {
        this.httpRequest = httpRequest;
        this.requestCallback = requestCallback;
    }
}
