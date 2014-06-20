package com.mindpin.android.authenticator;

import android.util.Log;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dd on 14-6-13.
 */
public abstract class IUser extends Model {
    @Column(name = "StrCookies")
    public String strCookies;

    public static <T> T find(String id) {
        throw new Error("Subclass find() no defined");
    }

    public static <T> T current(){
        throw new Error("Subclass current() no defined");
    }
}
