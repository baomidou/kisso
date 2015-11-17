kisso  =  cookie sso

基于 Cookie 的 SSO 中间件，欢迎大家使用 kisso !! 
> 技术讨论 QQ 群 492238239

> kisso 版本升级，不再兼容之前版本， 旧版本 demo 源码查看，请检出分支  v1.0-old


http://www.oschina.net/p/kisso

[kisso 升级日志](http://git.oschina.net/juapk/kisso/wikis/kisso---%E5%8D%87%E7%BA%A7%E6%97%A5%E5%BF%97)

[kisso捐赠记录,感谢你们的支持！](http://git.oschina.net/juapk/kisso/wikis/%E6%8D%90%E8%B5%A0%E8%AE%B0%E5%BD%95)

﻿sso
===

kisso cookie sso framework


Maven 坐标
===
```
<dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>kisso</artifactId>
  <version>2.2</version>
</dependency>
```

Usage
===========
[kisso 依赖 jars](http://git.oschina.net/juapk/kisso-deplibs)

[kisso 验证码字体](http://git.oschina.net/juapk/kisso-files/tree/master/kisso-files/font)

[kisso_Jfinal 演示 demo](http://git.oschina.net/juapk/kisso_jfinal)

[kisso_SpringMvc 演示 demo](http://git.oschina.net/juapk/kisso_springmvc)

[kisso_crossdomain 跨域演示 demo](http://git.oschina.net/juapk/kisso_crossdomain)


说明文档
===========
[sso.properties 配置说明](http://git.oschina.net/juapk/kisso/blob/master/kisso/src/main/resources/doc.md/?dir=0&filepath=kisso/src/main/resources/doc.md&oid=6376fee85d0184f0cb4957f928f47508f7140e0f&sha=47f98ea6f97eb195b6fe01459b665cb1f5d1f6d9)

[SSO 跨域登录实现方案](http://git.oschina.net/juapk/kisso/blob/master/kisso/src/main/resources/crossdomain.md/?dir=0&filepath=kisso/src/main/resources/crossdomain.md&oid=e6a5e090f661fafa8a6b739152bdd3b4bb1cd248&sha=47f98ea6f97eb195b6fe01459b665cb1f5d1f6d9)


（1）sso 登录状态
--------------------------------------------

 #登录

<img alt="welcome" width="800" height="500" src="http://git.oschina.net/uploads/images/2015/0309/094616_1cf45332_12260.png">

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

4、支持 Base64 / MD5 / AES / PBE / RSA 算法

5、支持浏览器客户端校验

6、支持Cookie参数配置及扩展

7、支持跨域登录，模拟登录

8、支持在线人数统计

9、支持生成动态图片验证码

Futures
====================
1、欢迎提出更好的意见，帮助完善 KISSO 


开源赞助我(支付宝)
====================
![GitHub](https://raw.githubusercontent.com/leqwang/kisso/master/images/donate.png "开源赞助我(支付宝)")

copyright
====================
Apache License, Version 2.0