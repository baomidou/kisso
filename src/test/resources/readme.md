

# 生成JWT秘钥

- CN=Web Server,OU=Unit,O=Organization,L=City,S=State,C=US

```
$ keytool -genkeypair -alias jwtkey -keyalg RSA -dname "CN=kissojwt" -keypass keypassword -keystore jwt.jks -storepass letkisso
```


# 根据秘钥生成公钥

```
$ keytool -list -rfc --keystore jwt.jks | openssl x509 -inform pem -pubkey
```
例如：
```
$ keytool -list -rfc --keystore jwt.jks | openssl x509 -inform pem -pubkey
▒▒▒▒▒▒Կ▒▒▒▒▒:  letkisso
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj3dg4e1QYPhG+4skQtHw
fpxSD4z++78snOg0a4zvp21jzOJOLEF0HAcypOZ0iXyiizGv9IF6RAcfOPeffXUF
5aaWgm0FbxfCGDFtMo5zvpegpEzKgR1kAgXNUCrKRYXXT5P9z92A4iq6pVPy3xFF
iRd8jecOFlUcnUjtR+swlM+srey7PyVffDhfgPQbLkmxmqsNJQVRkwPEEifuzKzy
eN0ILKZadIs036VyfeJ1aTMO21XMvII9evNA25c8vh8FBQqtkNXlgjFkUCmTNF5U
o20UD4ZaEsnLSNQsniGyR3I4Bm0Bg+HPw7zFfVmENNS/ZM1MW2M1eAu9t8u2rSJg
lwIDAQAB
-----END PUBLIC KEY-----
-----BEGIN CERTIFICATE-----
MIICxTCCAa2gAwIBAgIEXsqfDjANBgkqhkiG9w0BAQsFADATMREwDwYDVQQDEwhr
aXNzb2p3dDAeFw0xNzA3MTkwMjAxMDhaFw0xNzEwMTcwMjAxMDhaMBMxETAPBgNV
BAMTCGtpc3Nvand0MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj3dg
4e1QYPhG+4skQtHwfpxSD4z++78snOg0a4zvp21jzOJOLEF0HAcypOZ0iXyiizGv
9IF6RAcfOPeffXUF5aaWgm0FbxfCGDFtMo5zvpegpEzKgR1kAgXNUCrKRYXXT5P9
z92A4iq6pVPy3xFFiRd8jecOFlUcnUjtR+swlM+srey7PyVffDhfgPQbLkmxmqsN
JQVRkwPEEifuzKzyeN0ILKZadIs036VyfeJ1aTMO21XMvII9evNA25c8vh8FBQqt
kNXlgjFkUCmTNF5Uo20UD4ZaEsnLSNQsniGyR3I4Bm0Bg+HPw7zFfVmENNS/ZM1M
W2M1eAu9t8u2rSJglwIDAQABoyEwHzAdBgNVHQ4EFgQUNm2MGxKhudBSfnkHa+bt
+pjtZ80wDQYJKoZIhvcNAQELBQADggEBAA9D9EgfFPjE5U2sGZLmq8MW9LQsmdvL
FVO5k5BwTPjanO6tePAWLejYlTfAT7KNDGif4x6EmiOKx1lc7EuFzEYHIkGPkIGY
IYMc7iVv65JWwzkK0/5v7gqvOYnu5uDinHuskDv39dv1DroSGsqJ3yswAQNux0YL
Hq2yBrMDfW4oJN9VitlrPxQPu/QURlaKAdIq6t5WhF4jFp2it9LCiPHamsep8Exc
UkggrM/JovASv5ake7wmpyuh3QYtIP8qYLtPLl8oK/STOndryUDX96grfbJeKm4r
HG5aDfWXXSCXQ8BIsOup31GN2NyZJMLv4JA6DkWeMOxuZWTv8/ldCF0=
-----END CERTIFICATE-----
```

# 发布仓库
http://central.sonatype.org/pages/gradle.html
https://docs.gradle.org/current/userguide/maven_plugin.html
https://docs.gradle.org/3.3/userguide/signing_plugin.html
https://github.com/neurite/development-workflows/wiki/Gradle%2C-Travis-CI%2C-and-OSSRH


