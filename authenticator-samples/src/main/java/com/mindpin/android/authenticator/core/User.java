package com.mindpin.android.authenticator.core;

import com.mindpin.android.authenticator.IUser;

/**
 * Created by dd on 14-6-10.
 */
public class User implements IUser {
    private static final String TAG = "User";
    // 下面几个字段根据具体业务逻辑确定
    public String id;
    public String name;
    public String email;
    public String login;

    @Override
    public boolean equals(Object o) {
        try{
            User tmp = (User)o;
            return tmp.id.equals(id);
        }catch (Exception ex) {
            return super.equals(o);
        }
    }

    @Override
    public void save() {

    }
}
