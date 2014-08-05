package com.mindpin.android.authenticator.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mindpin.android.authenticator.AuthCallback;
import com.mindpin.android.authenticator.IUser;
import com.mindpin.android.authenticator.core.MyAuthenticator;
import com.mindpin.android.authenticator.R;
import com.mindpin.android.authenticator.core.User;

/**
 * Created by dd on 14-6-12.
 */
public class AuthenticatorSignIn extends Activity {
    MyAuthenticator myAuthenticator;
    User current_user;
    EditText et_login, et_password;
    Button btn_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_in);
        myAuthenticator = new MyAuthenticator();
        current_user = User.current();
        et_login = (EditText) findViewById(R.id.et_login);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_signin = (Button) findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAuthenticator.sign_in(
                        et_login.getText().toString(),
                        et_password.getText().toString(),
                        new AuthCallback() {
                            @Override
                            public void success(IUser user) {
                                to_signout();
                            }

                            @Override
                            public void failure() {
                                Toast.makeText(AuthenticatorSignIn.this, "用户和密码不正确", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void error() {
                                Toast.makeText(AuthenticatorSignIn.this, "连接服务器出错", Toast.LENGTH_LONG).show();
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
