package com.mindpin.android.authenticator;

import java.util.List;
import java.util.Map;

/**
 * Created by dd on 14-6-20.
 */
public class RequestResult {
    public int status;
    public String body;
    public Map<String, List<String>> headers;

    public RequestResult(int status, String body, Map<String, List<String>> headers) {
        this.status = status;
        this.body = body;
        this.headers = headers;
    }

    public boolean is_ok(){
        return status >= 200 && status < 300;
    }
}
