package com.mindpin.android.authenticator;

import android.os.AsyncTask;
import android.util.Log;
import com.github.kevinsawicki.http.HttpRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.kevinsawicki.http.HttpRequest.post;

/**
 * Created by dd on 14-6-10.
 */
// 发起登录验证请求的类
public abstract class Authenticator<M extends IUser> {

    private static final String TAG = "Authenticator";

    // 使用组件时，需要继承 Authenticator 并实现这个方法
    // 登录验证的 url
    public abstract String get_sign_in_url();


    // 使用组件时，需要继承 Authenticator 并实现这个方法
    // 发起登录验证请求时，用户名的 param name
    public abstract String get_login_param();

    // 使用组件时，需要继承 Authenticator 并实现这个方法
    // 发起登录验证请求时，密码的 param name
    public abstract String get_password_param();

    // 使用组件时，需要继承 Authenticator 并实现这个方法
    //  response 是登录验证成功后服务器返回的用户 json 信息
    // 把 response 转换成 具体逻辑中的 IUser 对象
    // 并把 IUser 对象返回
    // 不需要运行 IUser 的 save
    public abstract M on_auth_success_build_user(String response);

    // 开发组件时，需要实现这个方法的逻辑
    // 根据 get_sign_in_url get_login_param get_password_param 三个方法返回的内容发起登录验证请求
    // 登录验证成功并把信息保存到 sqlite 和
    // 当前登陆 user_id ( sqlite 数据库中 iuser 表的 主键)
    // 保存到 preference 后，会运行 AuthSuccessCallback
    // 对象的 callback 方法
    public void sign_in(String login, String password, AuthCallback callback) {
        SignParams signParams = new SignParams(login, password, callback);
        new SignInTask().execute(signParams);
    }

    // 登出
    public void sign_out(M user) {
        if (user != null)
            user.delete();
    }

    private class SignInTask extends AsyncTask<SignParams, Long, M> {
        SignParams signParams;
        int code;
        boolean loginFailure = false;

        @Override
        protected M doInBackground(SignParams... signParamses) {
            signParams = signParamses[0];
            try {
                HttpRequest request = post(get_sign_in_url())
                        .connectTimeout(15000) //15s
                        .readTimeout(15000) //15s
                        .part(get_login_param(), signParams.login)
                        .part(get_password_param(), signParams.password);
                int code = request.code();
                if (code == 200 || code == 201) {
                    String body = request.body();
                    M user = on_auth_success_build_user(body);
                    user.strCookies = request.header("Set-Cookie");
                    user.strCookies = user.strCookies.replace("; path=/", "");
                    user.strCookies = user.strCookies.replace("; HttpOnly", "");
                    user.save();
                    return user;
                } else {
                    if (request.code() == 401) {
                        loginFailure = true;
                    }
                }
            } catch (HttpRequest.HttpRequestException ex) {
                // for 2.2 登录失败: "java.io.IOException: Received authentication challenge is null"
                // 4.1 4.2登录失败未加WWW-Authentication: "java.io.IOException: No authentication challenges found"
                if(ex.getMessage() != null && ex.getMessage().indexOf("authentication challenge") >= 0)
                    loginFailure = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(M user) {
            if (user != null)
                signParams.authCallback.success(user);
            else {
                if (loginFailure)
                    signParams.authCallback.failure();
                else
                    signParams.authCallback.error();
            }
        }
    }

    public abstract IUser current_user();

    public void request(HttpRequest request, RequestCallback requestCallback) {
        IUser user = current_user();
        if (user == null) {
            RequestResult requestResult = new RequestResult(401, "", new HashMap<String, List<String>>());
            run_request_callback(requestResult, requestCallback, false);
            return; // throw error
        }
        RequestParams requestParams = new RequestParams(request, requestCallback);
        new RequestTask().execute(requestParams);
    }

    private class RequestTask extends AsyncTask<RequestParams, Long, Boolean> {
        RequestCallback requestCallback;
        HttpRequest request;
        RequestResult requestResult;

        @Override
        protected Boolean doInBackground(RequestParams... requestParams) {
            requestResult = null;
            if (requestParams.length > 0) {
                try {
                    RequestParams requestParam = requestParams[0];
                    requestCallback = requestParam.requestCallback;
                    request = requestParam.httpRequest;
                    if (request.ok()) {
                        requestResult = new RequestResult(request.code(), request.body(), request.headers());
                        return true;
                    } else {
                        requestResult = new RequestResult(request.code(), request.body(), request.headers());
                        return false;
                    }
                } catch (Exception ex) {
                    // 2.2 login failure
                    if (ex.getMessage() == null) {
                        requestResult = new RequestResult(401, "", new HashMap<String, List<String>>());
                    }
                    // 其他错误 返回
                    else {
                        // 4.1 4.2登录失败未加WWW-Authentication: "java.io.IOException: No authentication challenges found"
                        if(ex.getMessage() != null && ex.getMessage().indexOf("authentication challenge") >= 0)
                            requestResult = new RequestResult(401, "", new HashMap<String, List<String>>());
                    }
                }
            }
            return false;
        }


        @Override
        protected void onPostExecute(Boolean is_200) {
            run_request_callback(requestResult, requestCallback, is_200);
        }
    }

    public RequestResult syn_request(HttpRequest request) {
        IUser user = current_user();
        if (user == null) {
            return new RequestResult(401, "", new HashMap<String, List<String>>());
        }

        RequestResult requestResult = null;
        try {
            if (request.ok()) {
                requestResult = new RequestResult(request.code(), request.body(), request.headers());
            } else {
                requestResult = new RequestResult(request.code(), request.body(), request.headers());
            }
            return requestResult;
        } catch (Exception ex) {
            // 2.2 login failure
            if (ex.getMessage() == null) {
                requestResult = new RequestResult(401, "", new HashMap<String, List<String>>());
            }
            // 其他错误 返回
            else {
                // 4.1 4.2登录失败未加WWW-Authentication: "java.io.IOException: No authentication challenges found"
                if (ex.getMessage() != null && ex.getMessage().indexOf("authentication challenge") >= 0)
                    requestResult = new RequestResult(401, "", new HashMap<String, List<String>>());
            }
        }
        Log.d(TAG, "syn_request error");
        return requestResult;
    }

    protected void run_request_callback(RequestResult requestResult,RequestCallback requestCallback, Boolean is_200) {
        if (requestResult != null) {
            if (is_200) {
                requestCallback.is_200(requestResult);
            } else {
                requestCallback.not_200(requestResult);
            }
        } else {
            requestCallback.error();
        }
    }

    public HttpRequest get_http_request(String url, String method){
        HttpRequest request = new HttpRequest(url, method);
        IUser user = current_user();
        if (user != null) {
            request.header("Cookie", user.strCookies);
            return request;
        }
        else{
            return null;
        }
    }
}
