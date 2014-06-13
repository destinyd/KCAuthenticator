package com.mindpin.android.authenticator;

import static com.github.kevinsawicki.http.HttpRequest.*;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by dd on 14-6-10.
 */
public class KCAuthenticator extends Authenticator {
    private static final String TAG = "MyAuthenticator";

    ArrayList<User> users;
    Type listType;
    Context context;

    public KCAuthenticator(Context context) {
        this.context = context;
        listType = new TypeToken<ArrayList<User>>() {
        }.getType();
        try {
            users = PropertiesController.readConfiguration(context, listType);
        } catch (FileNotFoundException e) {
            users = new ArrayList<User>();
        }
    }

    //    public String get_sign_in_url() {
//        return "http://4ye.mindpin.com/account/sign_in";
//    }
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
    public void sign_out(User user) {
        Log.e(TAG, "before sign_out users.size():" + users.size());
        if (users.contains(user)) {
            users.remove(user);
            Log.e(TAG, "after sign_out users.size():" + users.size());
            save_to_file();
        }
    }

    private void save(User user) {
        sign_out(user);
        users.add(0, user);
        save_to_file();
    }

    private void save_to_file() {
        try {
            PropertiesController.writeConfiguration(context, users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User find(int index) {
        if (index < users.size())
            return users.get(index);
        else
            return null;
    }

    public static int current_user_id() {
        return 0;
    }

    public User current_user() {
        return find(current_user_id());
    }

    private class DownloadTask extends AsyncTask<SignParams, Long, User> {
        SignParams signParams;

        @Override
        protected User doInBackground(SignParams... signParams) {
            this.signParams = signParams[0];
            HttpRequest request = post(get_sign_in_url()).
                    part(get_login_param(), this.signParams.login).part(get_password_param(), this.signParams.password);
            if (request.ok()) {
                User user = on_auth_success_build_user(request.body());
                save(user);
                return user;
            } else {
                //throw error?
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            signParams.authSuccessCallback.callback(user);
        }
    }
}
