
SSOFilter.java SSO滤器配置
-----------------------------------------------
web.xml配置
-----------------------------------------------
<!-- SSOFilter use . -->
<filter>
	<filter-name>SSOFilter</filter-name>
	<filter-class>com.github.abci.kisso.filter.SSOFilter</filter-class>
	<init-param>
		<param-name>over.url</param-name>
		<param-value>/test/xx.html</param-value>
	</init-param>
</filter>
<filter-mapping>
	<filter-name>SSOFilter</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>
-----------------------------------------------


log4j.properties配置
-----------------------------------------------
log4j.appender.sso = org.apache.log4j.RollingFileAppender
log4j.appender.sso.MaxFileSize=1MB
log4j.appender.sso.MaxBackupIndex=7
log4j.appender.sso.file = ${sso.root}/logs/sso.log
log4j.appender.sso.layout = org.apache.log4j.PatternLayout
log4j.appender.sso.layout.conversionPattern = %d [%t] %-5p %c - %m%n
log4j.appender.sso.append = false
-----------------------------------------------


WafFilter.java 防火墙过滤器配置
-----------------------------------------------
web.xml配置
-----------------------------------------------
<!-- WafFilter use . -->
<filter>
	<filter-name>WafFilter</filter-name>
	<filter-class>com.github.abci.kisso.filter.WafFilter</filter-class>
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


log4j.properties配置
-----------------------------------------------
log4j.appender.waf = org.apache.log4j.RollingFileAppender
log4j.appender.waf.MaxFileSize=1MB
log4j.appender.waf.MaxBackupIndex=7
log4j.appender.waf.file = ${sso.root}/logs/waf.log
log4j.appender.waf.layout = org.apache.log4j.PatternLayout
log4j.appender.waf.layout.conversionPattern = %d [%t] %-5p %c - %m%n
log4j.appender.waf.append = false
-----------------------------------------------

