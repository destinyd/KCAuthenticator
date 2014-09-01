package com.mindpin.android.authenticator.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.github.kevinsawicki.http.HttpRequest;
import com.mindpin.android.authenticator.R;
import com.mindpin.android.authenticator.RequestCallback;
import com.mindpin.android.authenticator.RequestResult;
import com.mindpin.android.authenticator.core.MyAuthenticator;

/**
 * Created by dd on 14-9-1.
 */
abstract class ActivityBase extends Activity {
    private static final String TAG = "ActivityBase";
    protected MyAuthenticator myAuthenticator;
    protected TextView tv_request;
    protected HttpRequest request;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getClass().getName());

        setContentView(R.layout.request);
        myAuthenticator = new MyAuthenticator();
        tv_request = (TextView) findViewById(R.id.tv_request);

        request = build_request();
        process();
    }

    protected abstract void process();

    protected abstract HttpRequest build_request();

    protected class Task extends AsyncTask<HttpRequest, Long, RequestResult> {
        @Override
        protected void onPreExecute() {
            tv_request.setText("正在拼命的读取...");
        }

        @Override
        protected RequestResult doInBackground(HttpRequest... params) {
//            Log.e("Task", "Task");
            return myAuthenticator.syn_request(params[0]);
        }

        @Override
        protected void onPostExecute(RequestResult requestResult) {
            if (requestResult == null) {
                tv_request.setText("error");
                Toast.makeText(ActivityBase.this, "error", Toast.LENGTH_LONG).show();
            } else {
                if (requestResult.status == 200) {
                    tv_request.setText(requestResult.body);
                } else {
                    tv_request.setText("not_200\n" + requestResult.body);
                    Toast.makeText(ActivityBase.this, "not 200", Toast.LENGTH_LONG).show();
//                    Log.e(TAG, "requestResult.status:" + requestResult.status);
//                    Log.e(TAG, "requestResult.body:" + requestResult.body);
                }
            }
        }
    }

    protected RequestCallback build_request_callback() {
        return new RequestCallback() {
            public void is_200(RequestResult request) {
                tv_request.setText(request.body);
            }

            public void not_200(RequestResult requestResult) {
                tv_request.setText("not_200\n" + requestResult.body);
                Toast.makeText(ActivityBase.this, "not 200", Toast.LENGTH_LONG).show();
//                Log.d(TAG, "requestResult status:" + requestResult.status);
//                Log.d(TAG, "requestResult body:" + requestResult.body);
            }

            @Override
            public void error() {
                tv_request.setText("error");
                Toast.makeText(ActivityBase.this, "error", Toast.LENGTH_LONG).show();
            }
        };
    }
}