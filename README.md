
![kisso图标](http://git.oschina.net/uploads/images/2015/1122/122054_3b6813fa_12260.png "爱心萝卜 kisso")

kisso  =  cookie sso 基于 Cookie 的 SSO 中间件，它是一把快速开发 java Web 登录系统（SSO）的瑞士军刀。


> 技术讨论 QQ 群 492238239 

http://baomidou.com/

http://www.oschina.net/p/kisso

[kisso 升级日志](http://git.oschina.net/juapk/kisso/wikis/kisso---%E5%8D%87%E7%BA%A7%E6%97%A5%E5%BF%97)

[kisso捐赠记录,感谢你们的支持！](http://git.oschina.net/juapk/kisso/wikis/%E6%8D%90%E8%B5%A0%E8%AE%B0%E5%BD%95)

ki﻿sso
===

kisso = java cookie sso framework


Maven 坐标
===
```
<dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>kisso</artifactId>
  <version>最新版本 maven 为准</version>
</dependency>
```

Usage
===========
[kisso 依赖 jars](http://git.oschina.net/juapk/kisso/wikis/kisso-%E4%BE%9D%E8%B5%96%E5%8C%85-jars)

[kisso-oauth2-demo 演示 demo](http://git.oschina.net/juapk/kisso-oauth2-demo)
由于 apache-oltu-oauth2 很灵活 kisso 只做辅助支持，不重新造轮子。

[kisso_ApiServer 移动端 演示 demo](http://git.oschina.net/juapk/kisso_apiserver)

  第一种、同一个根域名不同子域名，比如 my.baomidou.com 、  sso.baomidou.com  、other.baomidou.com 此时配置  domain 只需要配置   .baomidou.com  即可。 查看普通 demo ：

[kisso_Jfinal 演示 demo](http://git.oschina.net/juapk/kisso_jfinal)

[kisso_SpringMvc 演示 demo](http://git.oschina.net/juapk/kisso_springmvc)

第二种、完全不同的域名，比如  sso.baomidou.com     git.oschina.net 此时比较复杂 kisso 采用的是 rsa 加密询问验证（较复杂） 查看跨域 demo:

[kisso_crossdomain 跨域演示 demo](http://git.oschina.net/juapk/kisso_crossdomain)

捐赠 kisso
====================

![捐赠 kisso](http://git.oschina.net/uploads/images/2015/1222/211207_0acab44e_12260.png "支持一下kisso")


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

![crossdomain](http://git.oschina.net/uploads/images/2015/1208/101724_845ea916_12260.jpeg "Kisso,crossdomain login")

hosts:
--------------------------------------------
127.0.0.1 sso.test.com

127.0.0.1 my.web.com

--------------------------------------------

访问 my.web.com:8090/index.html  如果未登录会重定向至sso域登录页面

![kisso login](http://git.oschina.net/uploads/images/2015/1208/101635_99913d35_12260.jpeg "Kisso,crossdomain login")

登录成功 my.web.com 如图

![kisso login](http://git.oschina.net/uploads/images/2015/1208/101605_2037b254_12260.jpeg "Kisso,crossdomain login")


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

10、支持 app 移动端 api 服务验证，采用微信公众平台 api 验证机制认证

Futures
====================
1、欢迎提出更好的意见，帮助完善 KISSO 

copyright
====================
Apache License, Version 2.0


关注我
====================
![程序员日记](http://git.oschina.net/uploads/images/2016/0121/093728_1bc1658f_12260.png "程序员日记")

