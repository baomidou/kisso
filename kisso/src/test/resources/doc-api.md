
# 一、 Api 授权认证方式 - 采用微信公众平台 api 认证机制

> 使用AES加密时，当密钥大于128时，代码会抛出异常：java.security.InvalidKeyException: Illegal key size

> 是指密钥长度是受限制的，java运行时环境读到的是受限的policy文件。文件位于${java_home}/jre/lib/security 
这种限制是因为美国对软件出口的控制。 

> 解决办法：http://www.oracle.com/technetwork/java/javaseproducts/downloads/index.html
  进入 Oracle JDK 下载 Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files
  for JDK/JRE 8 【下载对应 JDK 版本的 Policy 文件】
  JDK8 下载地址：http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
  下载包的readme.txt 有安装说明。就是替换${java_home}/jre/lib/security/ 下面的local_policy.jar和US_export_policy.jar 


# 二、 Api 授权认证方式 - Oauth2 认证机制

官方地址：http://oauth.net/2/
kisso 支持 oauth2 依赖 jars http://oltu.apache.org/





 