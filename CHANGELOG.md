# CHANGELOG

## [v3.9.5] 2025.08.09

- 修复自定义配置初始化 bug

## [v3.9.4] 2025.08.09

- 优化减少服务类及插件获取机制
- 优化插件token验证
- 拦截处理器新增Token预处理拦截方法preToken
- SSOToken票据新增timeExpired判断时间是否过期方法
- 其它优化

## [v3.9.3] 2024.09.09

- 优化 jwt 最新算法使用
- ssoToken 支持 map data 参数传递
- 其它优化

## [v3.9.2] 2024.09.06

- 开放 RSA 根据 Key 加解密方法
- 升级 jjwt 为 0.12.6
- 升级 gradle 编译依赖
- 完善代码注释其它优化

## [v3.9.1] 2023.03.01

- 支持 webflux basic 认证
- 其它代码优化

## [v3.9.0] 2022.12.07

- 支持 spring boot 3.0 jdk 17+

## [v3.8.3] 2022.10.11

- 支持 HTTP basic 认证
- 允许 Cookie 请求头设置
- jjwt 升级为 0.11.5
- spring-boot 编译依赖升级为 2.7.0
- 优化调整自适应 https
- base64 change java.util.base64
- 测试调整为 junit5
- 优化注释新增说文档

## [v3.8.2] 2022.10.11

- 发布失败版本号

## [v3.8.1] 2021.11.23

- 新增 SameSite 特性防止 CSRF


## [v3.8.0] 2021.10.13

- 升级 springboot 2.5.5 依赖
- 废除 @Login 注解修改为 @LoginIgnore 注解
- 注解 @Permission 属性 action 修改为 ignore 默认 false

