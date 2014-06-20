package com.mindpin.android.authenticator;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import com.github.kevinsawicki.http.HttpRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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


    // 使用组件时，需要继承 Authenticator 并实现这个方法
    // 获取用户信息的 url
    public abstract String get_user_info_url();


    // 开发组件时，需要实现这个方法的逻辑
    // 根据 get_sign_in_url get_login_param get_password_param 三个方法返回的内容发起登录验证请求
    // 登录验证成功并把信息保存到 sqlite 和
    // 当前登陆 user_id ( sqlite 数据库中 iuser 表的 主键)
    // 保存到 preference 后，会运行 AuthSuccessCallback
    // 对象的 callback 方法
    public void sign_in(String login, String password, AuthSuccessCallback callback) {
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

        @Override
        protected M doInBackground(SignParams... signParamses) {
            signParams = signParamses[0];
            try {
                HttpRequest request = post(get_sign_in_url()).
                        part(get_login_param(), signParams.login).part(get_password_param(), signParams.password);
                if (request.ok()) {
                    M user = on_auth_success_build_user(request.body());
                    user.strCookies = request.header("Set-Cookie");
                    user.strCookies = user.strCookies.replace("; path=/", "");
                    user.strCookies = user.strCookies.replace("; HttpOnly", "");
                    user.save();
                    return user;
                } else {
                    //throw error?
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(M user) {
            signParams.authSuccessCallback.callback(user);
        }
    }

    public abstract IUser current_user();

    public void request(HttpRequest request, RequestCallback requestCallback) {
        IUser user = current_user();
        if (user == null) {
            Log.e(TAG, "request user is null");
            return; // throw error
        }
        request.header("Cookie", user.strCookies);
        RequestParams requestParams = new RequestParams(request, requestCallback);
        new RequestTask().execute(requestParams);
    }


    private class RequestTask extends AsyncTask<RequestParams, Long, Boolean> {
        RequestCallback requestCallback;
        HttpRequest request;
        RequestResult requestResult;
        @Override
        protected Boolean doInBackground(RequestParams... requestParams) {
            if(requestParams.length > 0) {
                RequestParams requestParam = requestParams[0];
                requestCallback = requestParam.requestCallback;
                request = requestParam.httpRequest;
                if(request.ok()) {
                    requestResult = new RequestResult(request.code(), request.body(), request.headers());
                    return true;
                }
                else{
                    requestResult = new RequestResult(request.code(), "", request.headers());
                    return false;
                }
            }
            requestResult = null;
            return false;
        }


        @Override
        protected void onPostExecute(Boolean is_200) {
            if (is_200) {
                requestCallback.is_200(requestResult);
            } else {
                requestCallback.not_200(requestResult);
            }
        }
    }
}
