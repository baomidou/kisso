
![kisso图标](http://git.oschina.net/uploads/images/2015/1122/122054_3b6813fa_12260.png "爱心萝卜 kisso")

kisso  =  cookie sso 基于 Cookie 的 SSO 中间件，它是一把快速开发 java Web 登录系统（SSO）的瑞士军刀。


# 其他开源项目 | Other Project

- [Mybatis-Plus CRUD 快速开发框架](https://gitee.com/baomidou/mybatis-plus)

- [Mybatis-Plus 企业版高级特性示例](https://gitee.com/baomidou/mybatis-mate-examples)


- 前后分离可选：请求 Header 票据模式, 请求 Cookie 模式

- QQ 群 1064693672

# 仓库
`https://search.maven.org/search?q=g:com.baomidou`

```
<dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>kisso</artifactId>
  <version>3.7.7</version>
</dependency>
```


# 测试 demo

- [kisso-spring-boot](https://gitee.com/baomidou/kisso-spring-boot)
- [kisso_springmvc](https://gitee.com/jobob/kisso_springmvc)
- [kisso_jfinal](https://gitee.com/jobob/kisso_jfinal)
- [kisso_crossdomain](https://gitee.com/jobob/kisso_crossdomain)


# 使用文档

```
// 生成 jwt 票据，访问请求头设置‘ accessToken=票据内容 ’ 适合前后分离模式单点登录
String jwtToken = SSOToken.create().setId(1).setIssuer("admin").setOrigin(TokenOrigin.HTML5).getToken();

// 解析票据
SSOToken ssoToken = SSOToken.parser(jwtToken);

// Cookie 模式设置
SSOHelper.setCookie(request, response,  new SSOToken().setId(String.valueOf(1)).setIssuer("admin"));

// 登录权限拦截器类 SSOSpringInterceptor
// 注解不拦截 @LoginIgnore
// yml 配置 kisso.config....
```

- Spring Boot
```
@ControllerAdvice
@Configuration
public class WebConfig extends WebServiceConfigurer {

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

// RSA 生成证书
// RSA 公钥，配置参数 kisso.config.rsa-cert-store
$ keytool -export -alias jwtkey -file public.cert -keystore key.jks -storepass jkspassword
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