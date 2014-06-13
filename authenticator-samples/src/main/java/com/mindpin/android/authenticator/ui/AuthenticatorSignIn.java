package com.mindpin.android.authenticator.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mindpin.android.authenticator.AuthSuccessCallback;
import com.mindpin.android.authenticator.IUser;
import com.mindpin.android.authenticator.core.KCAuthenticator;
import com.mindpin.android.authenticator.R;
import com.mindpin.android.authenticator.core.User;

/**
 * Created by dd on 14-6-12.
 */
public class AuthenticatorSignIn extends Activity {
    KCAuthenticator kcAuthenticator;
    User current_user;
    EditText et_login, et_password;
    Button btn_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_in);
        kcAuthenticator = new KCAuthenticator(this);
        current_user = kcAuthenticator.current_user();
        et_login = (EditText) findViewById(R.id.et_login);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_signin = (Button) findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kcAuthenticator.sign_in(
                        et_login.getText().toString(),
                        et_password.getText().toString(),
                        new AuthSuccessCallback() {
                            @Override
                            public void callback(IUser user) {
                                if (user == null) {
                                    Toast.makeText(AuthenticatorSignIn.this, "登录失败!", Toast.LENGTH_LONG).show();
                                } else {
                                    to_signout();
                                }
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
