
-----------------------------------------------------------------
kisso 启动  web.xml 配置, spring 支持 bean注入启动， jfinal 支持插件启动。 具体查看具体 Demo
、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、

<!-- SSO 配置 -->
<context-param>
	<param-name>kissoConfigLocation</param-name>
	<param-value>classpath:properties/sso.properties</param-value>
</context-param>
<listener>
	<listener-class>com.baomidou.kisso.web.KissoConfigListener</listener-class>
</listener>

-----------------------------------------------------------------
Servlet 方式 SSO 拦截器  web.xml 配置 【可选】
、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、

<!-- SSOFilter use . -->
<filter>
	<filter-name>SSOFilter</filter-name>
	<filter-class>com.baomidou.kisso.web.filter.SSOFilter</filter-class>
	<init-param>
		<param-name>over.url</param-name>
		<param-value>/login.html</param-value>
	</init-param>
</filter>
<filter-mapping>
	<filter-name>SSOFilter</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>


-----------------------------------------------------------------
Spring SSO 拦截器配置 【选择这种方式，SSOFilter 就不需要配置了】
、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、

<mvc:interceptors>
	<!-- SSO 拦截器 -->
	<!-- path 对所有的请求拦截使用/**，对某个模块下的请求拦截使用：/myPath/* -->
	<mvc:interceptor>
		<mvc:mapping path="/**" />
		<bean class="com.baomidou.kisso.web.spring.SSOInterceptor" />
	</mvc:interceptor>
</mvc:interceptors>

、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、
Spring SSO 拦截器配置，此基础上支持方法注解，如下跳过该方法登录验证。
、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、

@Login(action = Action.Skip)


-----------------------------------------------------------------
Servlet 方式 SSO 防火墙滤器  web.xml 配置 【可选】
、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、

<!-- WafFilter use . -->
<filter>
	<filter-name>WafFilter</filter-name>
	<filter-class>com.baomidou.kisso.web.filter.WafFilter</filter-class>
	<init-param>
		<param-name>over.url</param-name>
		<param-value>/test/a.html;/test/b.html</param-value>
	</init-param>
	<init-param>
      <param-name>filter_xss</param-name>
      <param-value>true</param-value>
    </init-param>
	<init-param>
      <param-name>filter_sql_injection</param-name>
      <param-value>true</param-value>
    </init-param>
</filter>
<filter-mapping>
	<filter-name>WafFilter</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>


-------------------------------------------------------------------
sso.properties 配置说明
、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、

sso.run.mode		   模式配置，默认不带后缀为线上模式，
					   模式设置：_dev_mode 开发 ，_test_mode 测试 ，_online_mode 生产

sso.encoding		   编码格式： 默认 UTF-8

sso.secretkey		  加密密钥

------  cookie 设置部分 ------
sso.cookie.name			名称，默认 uid

sso.cookie.secure		是否只能HTTPS访问，默认 false 				【客户端配置可无】
sso.cookie.httponly 	是否设置 httponly脚本无法访问，默认 true   	【客户端配置可无】
sso.cookie.maxage		过期时间，默认 -1 关闭浏览器失效 				【客户端配置可无】
sso.cookie.domain		所在域，请设置根域，如 .baomidou.com 		【客户端配置可无】
sso.cookie.path			路径，默认 / 							【客户端配置可无】

sso.cookie.browser		是否检查浏览器，默认 true
sso.cookie.checkip		是否检查登录IP，默认 false
sso.encrypt.class		自定义对称加密类，默认AES，自定义例如：com.baomidou.my.DES
sso.parser.class		自定义解析类，默认 FastJsonParser			【必须加入 fastjson 依赖】

sso.statistic.class		自定义在线人数统计类，必须实现  SSOStatistic 接口
sso.token.class			自定义票据，默认SSOToken，自定义例如：com.baomidou.bo.LoginToken

------  缓存部分 ------
sso.cache.class	自定义缓存实现：com.baomidou.cache.RedisCache
sso.cache.expires  单位s秒，设置 -1永不失效，大于 0 失效时间

------  SSO 请求地址设置 ------
sso.login.url_online_mode		线上模式，登录地址：http://sso.testdemo.com/login.html
sso.login.url_dev_mode			开发模式，登录地址：http://localhost:8080/login.html

sso.logout.url_online_mode		线上模式，退出地址：http://sso.testdemo.com/logout.html
sso.logout.url_dev_mode			开发模式，退出地址：http://localhost:8080/logout.html

sso.param.returl				重定向地址参数配置，默认：ReturnURL

------  跨域 cookie 设置部分，不开启跨域功能可不设置 ------
sso.role						应用角色名（根据该值判断对应系统 RSA 公钥私钥 ）
sso.authcookie.name				名称pid，请不要与登录 cookie 名称一致
sso.authcookie.maxage			过期时间，默认 -1 关闭浏览器失效
-----------------------------------------------------------------




