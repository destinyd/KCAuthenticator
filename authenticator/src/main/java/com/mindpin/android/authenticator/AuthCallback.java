package com.mindpin.android.authenticator;

/**
 * Created by dd on 14-6-11.
 */
public interface AuthCallback {
    // 用户和密码验证成功
    public void success(IUser user);

    // 用户和密码不正确
    public void failure();

    // 连接服务器出错
    public void error();
}
