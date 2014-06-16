package com.mindpin.android.authenticator.core;

import android.util.Log;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;
import com.mindpin.android.authenticator.IUser;

/**
 * Created by dd on 14-6-10.
 */
@Table(name = "Users")
public class User extends IUser {
    private static final String TAG = "User";

    // 下面几个字段根据具体业务逻辑确定
    @SerializedName("id")
    @Column(name = "Server_user_id")
    public String server_user_id;

    @Column(name = "Name")
    public String name;
    @Column(name = "Email")
    public String email;
    @Column(name = "Login")
    public String login;

    @Column(name = "Update_at", index = true)
    public long update_at;

    public static User find(String server_user_id) {
        return new Select()
                .from(User.class)
                .where("Server_user_id = ?", server_user_id)
                .executeSingle();
    }

    public static User current(){
        return new Select()
                .from(User.class)
                .orderBy("Update_at DESC")
                .executeSingle();
    }

    @Override
    public boolean equals(Object o) {
        try {
            User tmp = (User) o;
            return tmp.server_user_id.equals(server_user_id);
        } catch (Exception ex) {
            return super.equals(o);
        }
    }
}
