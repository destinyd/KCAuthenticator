package com.mindpin.android.authenticator;

/**
 * Created by dd on 14-6-12.
 */
public class SignParams {
    public String login, password;
    public AuthCallback authCallback;

    public SignParams(String login, String password, AuthCallback authCallback) {
        this.login = login;
        this.password = password;
        this.authCallback = authCallback;
    }
}
