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
public class AuthenticatorSynRequestPost extends Activity {
    private static final String TAG = "AuthenticatorSynRequest";
    MyAuthenticator myAuthenticator;
    User current_user;
    TextView tv_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.request);
        myAuthenticator = new MyAuthenticator();
        tv_request = (TextView) findViewById(R.id.tv_request);

        MyAuthenticator auth1 = new MyAuthenticator();
        //test for 500
//            HttpRequest request = HttpRequest.get("http://192.168.0.8:4000")
        Map<String, String> data = new HashMap<String, String>();
        data.put("learn_record[step_id]", "53fd9c6a6c696e1939bd0100");
        HttpRequest request = HttpRequest.post(auth1.get_user_step_url())
                .form(data)
//                .part("learn_record[step_id]", "53fd9c6a6c696e1939bd0100")
                .connectTimeout(15000) //15s
                .readTimeout(15000) //15s
//                .authorization("");
        ;
        new SynRequestTask().execute(request);
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
            if (requestResult == null) {
                tv_request.setText("error");
                Toast.makeText(AuthenticatorSynRequestPost.this, "error", Toast.LENGTH_LONG).show();
            } else {
                if (requestResult.status == 200) {
                    tv_request.setText(requestResult.body);
                } else {
                    Log.e(TAG, "request status:" + requestResult.status);
                    tv_request.setText("not_200");
                    Toast.makeText(AuthenticatorSynRequestPost.this, "not 200", Toast.LENGTH_LONG).show();
//                    Log.e(TAG, "requestResult.status:" + requestResult.status);
//                    Log.e(TAG, "requestResult.body:" + requestResult.body);
                }
            }
        }
    }
}