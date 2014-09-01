Android KCAuthenticator
============================
knowledge camp Android登录验证组件

## 版本(last version)
### 最新稳定版(release)
0.0.6版（tag：v0.0.6）
### 最新开发版(SNAPSHOT)
0.0.7-SNAPSHOT版（branch：master）

## 如何引用此组件：
### 首先要安装ActiveAndroid
```
git clone https://github.com/pardom/ActiveAndroid
cd ActiveAndroid
mvn clean install -Dmaven.test.skip=true
```

### 安装KCAuthenticator
```
git clone https://github.com/mindpin/KCAuthenticator
cd KCAuthenticator
mvn clean install
```

### pom.xml添加以下依赖引用：
```
<dependency>
  <groupId>com.github.destinyd</groupId>
  <artifactId>authenticator</artifactId>
  <version>(last version)</version>
  <type>apklib</type>
</dependency>
```

## 应用所需权限
```
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

## 注意事项
### 表名
```
@Table(name = "Users")
public class User extends IUser {
}
```
**@Table(name = "Users")**
为设置表名称

### 属性
```
@SerializedName("id")
@Column(name = "Server_user_id")
public String server_user_id;
```
**@SerializedName("id")**
为对应服务器返回键值

```@Column(name = "Server_user_id")```
为Sqlite列名（不能为Id）
```public String server_user_id;```
**server_user_id** 为自定义属性名

### 继承
extends IUser必须重新定义find()、current()，否则使用时会报错

### 其他
**sign_in()** 、 **sign_out()**可能会使用到HttpRequest，请使用线程，否则4.0+会报错，具体请参考samples

### 0.0.6-SNAPSHOT使用说明
* 通过get_http_request(String url,String method)获取HttpRequest对象
* 对HttpRequest进行操作，例如part(xxx,xxx)
* syn_request(request)、request(request)

get_http_request会赋予cookie，所以必须在request之前执行
