package com.mindpin.android.authenticator.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.mindpin.android.authenticator.AuthSuccessCallback;
import com.mindpin.android.authenticator.KCAuthenticator;
import com.mindpin.android.authenticator.R;
import com.mindpin.android.authenticator.User;

/**
 * Created by dd on 14-6-12.
 */
public class AuthenticatorSignIn extends Activity {
    KCAuthenticator KCAuthenticator;
    User current_user;
    EditText et_login, et_password;
    Button btn_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_in);
        KCAuthenticator = new KCAuthenticator(this);
        current_user = KCAuthenticator.current_user();
        et_login = (EditText) findViewById(R.id.et_login);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_signin = (Button) findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KCAuthenticator.sign_in(
                        et_login.getText().toString(),
                        et_password.getText().toString(),
                        new AuthSuccessCallback() {
                            @Override
                            public void callback(User user) {
                                to_signout();
                            }
                        });
            }
        });
    }

    private void to_signout() {
        startActivity(new Intent(this, AuthenticatorSignOut.class));
        finish();
    }
}
