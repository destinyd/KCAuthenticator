package com.mindpin.android.authenticator.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.github.kevinsawicki.http.HttpRequest;
import com.mindpin.android.authenticator.R;
import com.mindpin.android.authenticator.RequestCallback;
import com.mindpin.android.authenticator.RequestParams;
import com.mindpin.android.authenticator.RequestResult;
import com.mindpin.android.authenticator.core.MyAuthenticator;
import com.mindpin.android.authenticator.core.User;

/**
 * Created by dd on 14-6-12.
 */
public class AuthenticatorSynRequest extends ActivityBase{
    @Override
    protected void process() {
        new Task().execute(request);
    }

    @Override
    protected HttpRequest build_request() {
        return myAuthenticator.get_http_request(myAuthenticator.get_user_info_url(), "GET")
                .connectTimeout(15000) //15s
                .readTimeout(15000) //15s
                .authorization("");
    }
}