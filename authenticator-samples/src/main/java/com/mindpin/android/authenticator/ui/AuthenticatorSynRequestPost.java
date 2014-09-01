package com.mindpin.android.authenticator.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.github.kevinsawicki.http.HttpRequest;
import com.mindpin.android.authenticator.R;
import com.mindpin.android.authenticator.RequestResult;
import com.mindpin.android.authenticator.core.MyAuthenticator;
import com.mindpin.android.authenticator.core.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dd on 14-9-1.
 */
public class AuthenticatorSynRequestPost extends ActivityBase {
    private static final String TAG = "AuthenticatorSynRequest";

    @Override
    protected void process() {
        new SynRequestTask().execute(request);
    }

    @Override
    protected HttpRequest build_request() {
        return myAuthenticator.get_http_request(myAuthenticator.get_user_step_url(), "POST")
                .connectTimeout(15000) //15s
                .readTimeout(15000) //15s
                .authorization("");
    }

    private class SynRequestTask extends Task {
        @Override
        protected RequestResult doInBackground(HttpRequest... params) {
            HttpRequest request =params[0];
            request.part("learn_record[step_id]", "53fdc6bc286e6f4d70bd0100");
            return myAuthenticator.syn_request(request);
        }
    }
}