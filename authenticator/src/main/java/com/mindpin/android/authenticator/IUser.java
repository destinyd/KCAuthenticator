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
    public static IUser find(String id) {
        throw new Error("Subclass find() no defined");
    }

    public static IUser current(){
        throw new Error("Subclass current() no defined");
    }
}
