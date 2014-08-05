package com.mindpin.android.authenticator.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.github.kevinsawicki.http.HttpRequest;
import com.mindpin.android.authenticator.R;
import com.mindpin.android.authenticator.RequestCallback;
import com.mindpin.android.authenticator.RequestResult;
import com.mindpin.android.authenticator.core.MyAuthenticator;
import com.mindpin.android.authenticator.core.User;

/**
 * Created by dd on 14-6-12.
 */
public class AuthenticatorRequest extends Activity {
    private static final String TAG = "AuthenticatorSignOut";
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
            HttpRequest request = HttpRequest.get(auth1.get_user_info_url())
                    .connectTimeout(15000) //15s
                    .readTimeout(15000) //15s
                    .authorization("");
            auth1.request(request, new RequestCallback() {
                public void is_200(RequestResult request) {
                    tv_request.setText(request.body);
//                    Toast.makeText(AuthenticatorRequest.this, request.body(), Toast.LENGTH_LONG).show();
                }

                public void not_200(RequestResult request) {
                    tv_request.setText("not_200");
                    Toast.makeText(AuthenticatorRequest.this, "not 200", Toast.LENGTH_LONG).show();
                }

                @Override
                public void error() {
                    tv_request.setText("error");
                    Toast.makeText(AuthenticatorRequest.this, "error", Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}