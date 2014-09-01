package com.mindpin.android.authenticator.core;

import static com.github.kevinsawicki.http.HttpRequest.*;

import android.os.AsyncTask;
import android.util.Log;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.mindpin.android.authenticator.*;

import java.util.Map;

/**
 * Created by dd on 14-6-10.
 */
public class MyAuthenticator extends Authenticator<User> {
    private static final String TAG = "MyAuthenticator";

    public String get_sign_in_url() {
        return "http://kc-alpha.4ye.me/account/sign_in.json";
    }

    public String get_login_param() {
        return "user[login]";
    }

    public String get_password_param() {
        return "user[password]";
    }

    public User on_auth_success_build_user(String response) {
        //  根据 response 构建出 user 对象
        return new Gson().fromJson(response, User.class);
    }

    public String get_user_info_url() {
        return "http://kc-alpha.4ye.me/api/nets";
    }

    public String get_user_step_url() {
        return "http://kc-alpha.4ye.me/api/learn_records.json";
    }

    @Override
    public IUser current_user() {
        return User.current();
    }
}
