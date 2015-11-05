
SSOFilter.java SSO滤器配置
-----------------------------------------------
web.xml配置
-----------------------------------------------
<!-- SSOFilter use . -->
<filter>
	<filter-name>SSOFilter</filter-name>
	<filter-class>wang.leq.sso.filter.SSOFilter</filter-class>
	<init-param>
		<param-name>over.url</param-name>
		<param-value>/login.html</param-value>
	</init-param>
</filter>
<filter-mapping>
	<filter-name>SSOFilter</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>
-----------------------------------------------


WafFilter.java 防火墙过滤器配置
-----------------------------------------------
web.xml配置
-----------------------------------------------
<!-- WafFilter use . -->
<filter>
	<filter-name>WafFilter</filter-name>
	<filter-class>wang.leq.sso.filter.WafFilter</filter-class>
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

