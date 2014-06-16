package com.mindpin.android.authenticator;

/**
 * Created by dd on 14-6-10.
 */
// 发起登录验证请求的类
public abstract class Authenticator {

    // 使用组件时，需要继承 Authenticator 并实现这个方法
    // 登录验证的 url
    public abstract String get_sign_in_url();

    // 使用组件时，需要继承 Authenticator 并实现这个方法
    // 发起登录验证请求时，用户名的 param name
    public abstract String get_login_param();

    // 使用组件时，需要继承 Authenticator 并实现这个方法
    // 发起登录验证请求时，密码的 param name
    public abstract String get_password_param();

    // 使用组件时，需要继承 Authenticator 并实现这个方法
    //  response 是登录验证成功后服务器返回的用户 json 信息
    // 把 response 转换成 具体逻辑中的 IUser 对象
    // 并把 IUser 对象返回
    // 不需要运行 IUser 的 save
    public abstract IUser on_auth_success_build_user(String response);


    // 开发组件时，需要实现这个方法的逻辑
    // 根据 get_sign_in_url get_login_param get_password_param 三个方法返回的内容发起登录验证请求
    // 登录验证成功并把信息保存到 sqlite 和
    // 当前登陆 user_id ( sqlite 数据库中 iuser 表的 主键)
    // 保存到 preference 后，会运行 AuthSuccessCallback
    // 对象的 callback 方法
    public abstract void sign_in(String login, String password, AuthSuccessCallback callback);

    // 登出
    public abstract void sign_out(IUser user);

//    // 开发组件时，需要实现这个方法的逻辑
//    // 获取 preference 中存储的 user_id( sqlite 数据库中 iuser 表的 主键)
//    public static int current_user_id(){
//        return 0;
//    };
}
