kisso  =  cookie sso

基于 Cookie 的 SSO 中间件，欢迎大家使用 kisso !! 

http://www.oschina.net/p/kisso

﻿sso
===

kisso cookie sso framework


Usage
====================

[kisso 依赖 jars](http://git.oschina.net/juapk/kisso-deplibs)

[kisso 验证码字体](http://git.oschina.net/juapk/kisso-files/tree/master/kisso-files/font)

[kisso_JFnal 演示 demo](http://git.oschina.net/juapk/kisso_jfinal)

[kisso_SpringMvc 演示 demo](http://git.oschina.net/juapk/kisso_springmvc)

跨域演示demo待定！！！！

（1）sso 登录状态
--------------------------------------------

 #登录

<img alt="welcome" width="800" height="500" src="http://git.oschina.net/uploads/images/2015/0302/180314_ddd9616f_12260.png">

 #登录成功

<img alt="welcome" width="800" height="500" src="http://git.oschina.net/uploads/images/2015/0302/180138_590ee527_12260.png">



（2）跨域登录
--------------------------------------------

![GitHub](https://raw.githubusercontent.com/leqwang/kisso/master/images/cl.jpg "Kisso,crossdomain login")

hosts:
--------------------------------------------
127.0.0.1 sso.test.com
127.0.0.1 my.web.com

--------------------------------------------

访问 my.web.com:8090/index.html  如果未登录会重定向至sso域登录页面
![GitHub](https://raw.githubusercontent.com/leqwang/kisso/master/images/nologin.jpg "Kisso,crossdomain login")

登录成功 my.web.com 如图
![GitHub](https://raw.githubusercontent.com/leqwang/kisso/master/images/login.jpg "Kisso,crossdomain login")


Supports
====================
1、支持单点登录

2、支持登录Cookie缓存

3、支持防止 XSS攻击, SQL注入，脚本注入

4、支持Base64 / MD5 / AES / RSA 算法

5、支持浏览器客户端校验

6、支持Cookie参数配置及扩展

7、支持跨域登录，模拟登录

Futures
====================
1、实现完整 sso 工程实例


开源赞助我(支付宝)
====================
![GitHub](https://raw.githubusercontent.com/leqwang/kisso/master/images/donate.png "开源赞助我(支付宝)")

copyright
====================
Apache License, Version 2.0