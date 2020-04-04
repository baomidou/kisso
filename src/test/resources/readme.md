# 生成 jks 密钥

- CN=Server,OU=Unit,O=Organization,L=City,S=State,C=US

```
$ keytool -genkeypair -alias jwtkey -keyalg RSA -dname "CN=llt" -keypass keypassword -keystore server.jks -storepass jkspassword
```

# 生成证书

```
$ keytool -export -alias jwtkey -file jwt.crt -keystore server.jks -storepass jkspassword
```

# 发布仓库
http://central.sonatype.org/pages/gradle.html
https://docs.gradle.org/current/userguide/maven_plugin.html
https://docs.gradle.org/3.3/userguide/signing_plugin.html
https://github.com/neurite/development-workflows/wiki/Gradle%2C-Travis-CI%2C-and-OSSRH


