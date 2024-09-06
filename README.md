
![kisso图标](http://git.oschina.net/uploads/images/2015/1122/122054_3b6813fa_12260.png "爱心萝卜 kisso")

kisso  =  cookie sso 基于 Cookie 的 SSO 中间件，它是一把快速开发 java Web 登录系统（SSO）的瑞士军刀。

# 其他开源项目 | Other Project

- [Mybatis-Plus CRUD 快速开发框架](https://gitee.com/baomidou/mybatis-plus)

- [Mybatis-Plus 企业版高级特性示例](https://gitee.com/baomidou/mybatis-mate-examples)

- [AiZuDa 爱组搭](https://gitee.com/aizuda)


- 前后分离可选：请求 Header 票据模式, 请求 Cookie 模式

- 交流群 + 微信 `wx153666`

# 仓库
`https://search.maven.org/search?q=g:com.baomidou`

```
<dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>kisso</artifactId>
  <version>3.9.2</version>
</dependency>
```

> spring boot 2.x 使用 3.9.0 之前的版本

# 测试 demo

- [kisso-spring-boot](https://gitee.com/baomidou/kisso-spring-boot)
- [kisso_springmvc](https://gitee.com/jobob/kisso_springmvc)
- [kisso_jfinal](https://gitee.com/jobob/kisso_jfinal)
- [kisso_crossdomain](https://gitee.com/jobob/kisso_crossdomain)
- [kisso-oauth2-demo](https://gitee.com/jobob/kisso-oauth2-demo)


# 使用文档

```
// 生成 jwt 票据，访问请求头设置‘ accessToken=票据内容 ’ 适合前后分离模式单点登录
String jwtToken = SSOToken.create().setId(1).setIssuer("admin").setOrigin(TokenOrigin.HTML5).getToken();

// 解析票据
SSOToken ssoToken = SSOToken.parser(jwtToken);

// Cookie 模式设置
SSOHelper.setCookie(request, response,  new SSOToken().setId(String.valueOf(1)).setIssuer("admin"));

// 如果想同时支持 cookie 和 header 模式如下设置
ssoToken.setOrigin(TokenOrigin.HTML5);

// 登录权限拦截器类 SSOSpringInterceptor
// 注解不拦截 @LoginIgnore
// yml 配置 kisso.config....
```

- Spring Boot
```
@ControllerAdvice
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // SSO 授权拦截器
        SSOSpringInterceptor ssoInterceptor = new SSOSpringInterceptor();
        ssoInterceptor.setHandlerInterceptor(new LoginHandlerInterceptor());
        registry.addInterceptor(ssoInterceptor).addPathPatterns("/**").excludePathPatterns("/v1/sso/**");
    }
}
```


# 默认 HS512 算法

```
// HS512 密钥，配置参数 kisso.config.sign-key
SSOHelper.getHS512SecretKey()
```

# 切换 RS512 算法

- 1，配置算法 kisso.config.sign-algorithm = RS512
- 2，配置私钥公钥证书，默认放置 resources 目录即可

```
// RSA 密钥，配置参数 kisso.config.rsa-jks-store
// 其它参数 CN=Server,OU=Unit,O=Organization,L=City,S=State,C=US
// RSA 生成 jks 密钥
$ keytool -genkeypair -alias jwtkey -keyalg RSA -dname "CN=llt" -keypass keypassword -keystore key.jks -storepass jkspassword

// ---  RSA 生成证书，默认 cert 格式  ---

// 公钥 cert 格式，配置参数 kisso.config.rsa-cert-store
$ keytool -export -alias jwtkey -file public.cert -keystore key.jks -storepass jkspassword

// 公钥 cer 格式，用于 .net 语言
$ keytool -alias jwtkey -exportcert -keystore key.jks -file public.cer -storepass jkspassword

// 公钥 pem 格式，用于 go 语言
keytool -export -rfc -keystore key.jks --alias jwtkey -file public.pem -storepass jkspassword
```

# 常见安全策略

- Secure
标记为 Secure 的 Cookie 只应通过被HTTPS协议加密过的请求发送给服务端。使用 HTTPS 安全协议，可以保护 Cookie 在浏览器和 Web 服务器间的传输过程中不被窃取和篡改。

- HTTPOnly
设置 HTTPOnly 属性可以防止客户端脚本通过 document.cookie 等方式访问 Cookie，有助于避免 XSS 攻击。

- SameSite
SameSite 属性可以让 Cookie 在跨站请求时不会被发送，从而可以阻止跨站请求伪造攻击（CSRF）。

```
SameSite 可以有下面三种值：
1、Strict仅允许一方请求携带 Cookie，即浏览器将只发送相同站点请求的 Cookie，即当前网页 URL 与请求目标 URL 完全一致。
2、Lax允许部分第三方请求携带 Cookie
3、None无论是否跨站都会发送 Cookie
造成现在无法获取cookie是因为之前默认是 None 的，Chrome80 后默认是 Lax
```

- 安全配置如下：

```
kisso:
  config:
    # 开启 https 有效，传输更安全
    cookie-secure: true
    # 防止 XSS 防止脚本攻击
    cookie-http-only: true
    # 防止 CSRF 跨站攻击
    cookie-same-site: Lax
    # 加密算法 RSA
    sign-algorithm: RS512
    ...
```

# 支持 actuator basic 权限认证

> 注入以下配置，用户名密码切勿泄露

- Spring Boot Web

```
@Bean
@ConditionalOnMissingBean
public FilterRegistrationBean basicAuthenticateFilter() {
  FilterRegistrationBean registrationBean = new FilterRegistrationBean();
  registrationBean.setFilter(new BasicAuthenticateFilter("用户名", "密码"));
  registrationBean.addUrlPatterns("/actuator", "/actuator/**");
  registrationBean.setOrder(Integer.MAX_VALUE);
  return registrationBean;
}
```

- Spring Cloud Gateway Webflux

```
@Bean
public BasicAuthenticateWebfluxFilter BasicAuthenticateWebfluxFilter() {
    return new BasicAuthenticateWebfluxFilter("用户名", "密码");
}
```

捐赠 kisso
====================

![捐赠 kisso](http://git.oschina.net/uploads/images/2015/1222/211207_0acab44e_12260.png "支持一下kisso")

- 欢迎提出更好的意见，帮助完善 KISSO 

copyright
====================
Apache License, Version 2.0

关注我
====================
![程序员日记](http://git.oschina.net/uploads/images/2016/0121/093728_1bc1658f_12260.png "程序员日记")