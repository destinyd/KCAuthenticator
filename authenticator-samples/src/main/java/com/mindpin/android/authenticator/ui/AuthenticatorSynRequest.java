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
public class AuthenticatorSynRequest extends Activity {
    private static final String TAG = "AuthenticatorSynRequest";
    MyAuthenticator myAuthenticator;
    User current_user;
    TextView tv_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.request);
        myAuthenticator = new MyAuthenticator();
        current_user = User.current();
        if (current_user == null) {
            startActivity(new Intent(this, AuthenticatorSignIn.class));
            finish();
        } else {
            tv_request = (TextView) findViewById(R.id.tv_request);

            MyAuthenticator auth1 = new MyAuthenticator();
            //test for 500
//            HttpRequest request = HttpRequest.get("http://192.168.0.8:4000")
            HttpRequest request = HttpRequest.get(auth1.get_user_info_url())
                    .connectTimeout(15000) //15s
                    .readTimeout(15000) //15s
                    .authorization("");
            new SynRequestTask().execute(request);
        }
    }

    private class SynRequestTask extends AsyncTask<HttpRequest, Long, RequestResult> {
        final MyAuthenticator auth1 = new MyAuthenticator();

        @Override
        protected void onPreExecute() {
            tv_request.setText("正在拼命的读取...");
        }

        @Override
        protected RequestResult doInBackground(HttpRequest... params) {
            return auth1.syn_request(params[0]);
        }

        @Override
        protected void onPostExecute(RequestResult requestResult) {
            if(requestResult == null) {
                tv_request.setText("error");
                Toast.makeText(AuthenticatorSynRequest.this, "error", Toast.LENGTH_LONG).show();
            }
            else {
                if (requestResult.status == 200) {
                    tv_request.setText(requestResult.body);
                } else {
                    tv_request.setText("not_200");
                    Toast.makeText(AuthenticatorSynRequest.this, "not 200", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "requestResult.status:" + requestResult.status);
                    Log.e(TAG, "requestResult.body:" + requestResult.body);
                }
            }
        }
    }
}