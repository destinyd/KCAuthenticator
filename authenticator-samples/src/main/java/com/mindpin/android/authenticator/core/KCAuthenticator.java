package com.mindpin.android.authenticator.core;

import static com.github.kevinsawicki.http.HttpRequest.*;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mindpin.android.authenticator.AuthSuccessCallback;
import com.mindpin.android.authenticator.Authenticator;
import com.mindpin.android.authenticator.IUser;
import com.sun.org.apache.bcel.internal.generic.IUSHR;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by dd on 14-6-10.
 */
public class KCAuthenticator extends Authenticator {
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

    @Override
    public void sign_in(String login, String password, AuthSuccessCallback callback) {
        SignParams signParams = new SignParams(login, password, callback);
        new DownloadTask().execute(signParams);
    }

    @Override
    public void sign_out(IUser iuser) {
        User user = (User)iuser;
        user.delete();
    }

    private class DownloadTask extends AsyncTask<SignParams, Long, User> {
        SignParams signParams;

        @Override
        protected User doInBackground(SignParams... signParamses) {
            signParams = signParamses[0];
            try {
                HttpRequest request = post(get_sign_in_url()).
                        part(get_login_param(), signParams.login).part(get_password_param(), signParams.password);
                if (request.ok()) {
                    User user = on_auth_success_build_user(request.body());
                    user.save();
                    return user;
                } else {
                    //throw error?
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(User user) {
            signParams.authSuccessCallback.callback(user);
        }
    }
}
