package com.mindpin.android.authenticator;

/**
 * Created by dd on 14-6-12.
 */
public class SignParams {
    public String login, password;
    public AuthSuccessCallback authSuccessCallback;

    public SignParams(String login, String password, AuthSuccessCallback authSuccessCallback) {
        this.login = login;
        this.password = password;
        this.authSuccessCallback = authSuccessCallback;
    }
}
