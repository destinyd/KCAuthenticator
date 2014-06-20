package com.mindpin.android.authenticator;

import com.github.kevinsawicki.http.HttpRequest;

/**
 * Created by dd on 14-6-11.
 */
public interface RequestCallback {
    // 请求是200时的回调，该方法运行在UI线程
    public void is_200(RequestResult request);

    // 请求不是200时的回调，该方法运行在UI线程
    public void not_200(RequestResult request);
}
