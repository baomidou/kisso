
-----------------------------------------------------------------
kisso SSO滤器  web.xml 配置
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
kisso 防火墙滤器  web.xml 配置
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


-----------------------------------------------

