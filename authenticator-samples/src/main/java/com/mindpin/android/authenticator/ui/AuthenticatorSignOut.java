package com.mindpin.android.authenticator.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.mindpin.android.authenticator.core.KCAuthenticator;
import com.mindpin.android.authenticator.R;
import com.mindpin.android.authenticator.core.User;

/**
 * Created by dd on 14-6-12.
 */
public class AuthenticatorSignOut extends Activity {
    KCAuthenticator KCAuthenticator;
    User current_user;
    TextView id, login, email, name;
    Button btn_signout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_out);
        KCAuthenticator = new KCAuthenticator(this);
        current_user = KCAuthenticator.current_user();
        if(current_user == null){
            startActivity(new Intent(this, AuthenticatorSignIn.class));
            finish();
        }
        else {
            id = (TextView) findViewById(R.id.tv_id);
            login = (TextView) findViewById(R.id.tv_login);
            email = (TextView) findViewById(R.id.tv_email);
            name = (TextView) findViewById(R.id.tv_name);
            btn_signout = (Button) findViewById(R.id.btn_signout);

            id.setText(current_user.id);
            login.setText(current_user.login);
            email.setText(current_user.email);
            name.setText(current_user.name);
            btn_signout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    KCAuthenticator.sign_out(current_user);
                    finish();
                }
            });
        }
    }
}