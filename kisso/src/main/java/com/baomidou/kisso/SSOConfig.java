/**
 * Copyright (c) 2011-2014, hubin (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.kisso;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.baomidou.kisso.common.SSOReflectHelper;
import com.baomidou.kisso.common.encrypt.SSOEncrypt;
import com.baomidou.kisso.common.parser.SSOParser;
import com.baomidou.kisso.common.util.PropertiesUtil;
import com.baomidou.kisso.exception.KissoException;

/**
 * <p>
 * SSO 配置文件解析
 * </p>
 * <p>
 * 按照不同的运行模式启用相应的配置 默认开发环境， 调试方式 Eclipse 环境的 VM 参数中加上
 * -Dsso.run.mode=dev_mode 例如：
 * ------------------------------------------------------------------
 * sso.login.url_online_mode=http://sso.online.com/login.html
 * sso.login.url_test_mode=http://sso.test.com/login.html
 * sso.login.url_dev_mode=http://localhost:8080/login.html
 * ------------------------------------------------------------------
 * </p>
 * 
 * @author hubin
 * @Date 2015-12-05
 */
public class SSOConfig {
	private static final Logger logger = Logger.getLogger("SSOConfig");
	private static final String SSO_ENCODING = "UTF-8";
	private String runMode = "online_mode";
	private String role = "";
	private String encoding = SSO_ENCODING;
	private String secretkey = "p00Tm071X992t3Eg05";
	private String cookieName = "uid";
	private String cookieDomain = "";
	private String cookiePath = "/";
	private boolean cookieSecure = false;
	private boolean cookieHttponly = true;
	private int cookieMaxage = -1;
	private boolean cookieBrowser = true;
	private boolean cookieCheckip = false;
	private String loginUrl = "";
	private String logoutUrl = "";
	private String paramReturl = "ReturnURL";
	private String authCookieName = "pid";
	private int authCookieMaxage = 180;
	private Token token = null;
	private SSOParser parser = null;
	private SSOEncrypt encrypt = null;
	private SSOCache cache = null;
	private int cacheExpires = -1;
	private SSOStatistic statistic = null;
	private List<SSOPlugin> pluginList = null;
	
	/**
	 * <p>
	 * 拦截器判断后设置 Token至当前请求，减少Token解密次数：
	 * request.setAttribute("ssotoken_attr", token)
	 * </p>
	 * <p>
	 * 使用获取方式： SSOHelper.attrToken(request)
	 * </p>
	 */
	public static final String SSO_TOKEN_ATTR = "ssotoken_attr";
	
	/**
	 * SSO 动态设置 Cookie 参数
	 * <p>
	 * -1 浏览器关闭时自动删除 0 立即删除 120 表示Cookie有效期2分钟(以秒为单位)
	 * </p>
	 */
	public static final String SSO_COOKIE_MAXAGE = "sso_cookie_maxage";
	
	/**
	 * Charset 类型编码格式
	 */
	public static final Charset CHARSET_ENCODING = Charset.forName(getSSOEncoding());
	
	public static final String CUT_SYMBOL = "#";
	
	/**
	 * 运行模式
	 */
	private static final String SSO_RUN_MODE = "sso.run.mode";
	private static PropertiesUtil prop = null;
	private static SSOConfig ssoConfig = null;
	
	protected SSOConfig() {
		/* 保护 */
	}
	
	/**
	 * new 当前对象
	 */
	public static SSOConfig getInstance() {
		if ( ssoConfig == null ) {
			if ( prop == null ) {
				/*
				 * 如果不是配置文件启动
				 * <p>
				 * 初始化空 Properties
				 * </p>
				 */
				prop = new PropertiesUtil(new Properties());
			}
			ssoConfig = new SSOConfig();
		}
		return ssoConfig;
	}

	/**
	 * SSO 资源文件初始化
	 */
	public static void init(Properties props) {
		if (props != null) {
			prop = new PropertiesUtil(props, SSO_RUN_MODE);
			logger.config("loading kisso config.");
			logger.info("kisso init success.");
		} else {
			throw new KissoException(" cannot load kisso config. ");
		}
	}
	
	/**
	 * SSO 配置工具类
	 * @return {@link PropertiesUtil}
	 */
	public static PropertiesUtil getSSOProperties() {
		return prop;
	}
	
	/**
	 * SSO 当前编码格式
	 */
	public static String getSSOEncoding() {
		if ( prop == null ) {
			return SSO_ENCODING;
		}
		return getInstance().getEncoding();
	}
	
	/**
	 * SSO 配置模式 
	 * <p>
	 * -------------------- 
	 * dev_mode 开发模式
	 * test_mode 测试模式 
	 * online_mode 生产模式 
	 * --------------------
	 * </p>
	 */
	public String getRunMode() {
		return prop.get(SSO_RUN_MODE, runMode);
	}
	
	public void setRunMode( String runMode ) {
		this.runMode = runMode;
	}
	
	/**
	 * 系统角色（默认 空）
	 * <p>
	 * 该属性为跨域区分当前系统使用，与 token 变量应用系统 app 参数自动设置为该属性值。
	 * </p>
	 * <p>
	 * 该值非空则自动开启跨域功能，单点登录系统名必须设置为 sso.role=sso
	 * </p>
	 */
	public String getRole() {
		return prop.get("sso.role", role);
	}

	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * 编码格式默认 UTF-8
	 */
	public String getEncoding() {
		return prop.get("sso.encoding", encoding);
	}
	
	public void setEncoding( String encoding ) {
		this.encoding = encoding;
	}
	
	/**
	 * 密钥
	 */
	public String getSecretkey() {
		return prop.get("sso.secretkey", secretkey);
	}
	
	public void setSecretkey( String secretkey ) {
		this.secretkey = secretkey;
	}
	
	/**
	 * Cookie 名称
	 */
	public String getCookieName() {
		return prop.get("sso.cookie.name", cookieName);
	}
	
	public void setCookieName( String cookieName ) {
		this.cookieName = cookieName;
	}
	
	/**
	 * Cookie 所在域
	 */
	public String getCookieDomain() {
		return prop.get("sso.cookie.domain", cookieDomain);
	}
	
	public void setCookieDomain( String cookieDomain ) {
		this.cookieDomain = cookieDomain;
	}
	
	/**
	 * Cookie 域路径
	 */
	public String getCookiePath() {
		return prop.get("sso.cookie.path", cookiePath);
	}
	
	public void setCookiePath( String cookiePath ) {
		this.cookiePath = cookiePath;
	}
	
	/**
	 * Cookie 只允许https协议传输
	 */
	public boolean getCookieSecure() {
		return prop.getBoolean("sso.cookie.secure", cookieSecure);
	}
	
	public void setCookieSecure( boolean cookieSecure ) {
		this.cookieSecure = cookieSecure;
	}
	
	/**
	 * Cookie 只读, 不允许 Js访问
	 */
	public boolean getCookieHttponly() {
		return prop.getBoolean("sso.cookie.httponly", cookieHttponly);
	}
	
	public void setCookieHttponly( boolean cookieHttponly ) {
		this.cookieHttponly = cookieHttponly;
	}
	
	/**
	 * Cookie 超时时间
	 * <p>
	 * -1 浏览器关闭时自动删除 0 立即删除 120 表示Cookie有效期2分钟(以秒为单位)
	 * </p>
	 */
	public int getCookieMaxage() {
		return prop.getInt("sso.cookie.maxage", cookieMaxage);
	}
	
	public void setCookieMaxage( int cookieMaxage ) {
		this.cookieMaxage = cookieMaxage;
	}
	
	/**
	 * Cookie 开启浏览器版本校验
	 */
	public boolean getCookieBrowser() {
		return prop.getBoolean("sso.cookie.browser", cookieBrowser);
	}
	
	public void setCookieBrowser( boolean cookieBrowser ) {
		this.cookieBrowser = cookieBrowser;
	}
	
	/**
	 * Cookie 开启IP校验
	 */
	public boolean getCookieCheckip() {
		return prop.getBoolean("sso.cookie.checkip", cookieCheckip);
	}
	
	public void setCookieCheckip( boolean cookieCheckip ) {
		this.cookieCheckip = cookieCheckip;
	}
	
	/**
	 * SSO 登录地址
	 */
	public String getLoginUrl() {
		return prop.get("sso.login.url", loginUrl);
	}
	
	public void setLoginUrl( String loginUrl ) {
		this.loginUrl = loginUrl;
	}
	
	/**
	 * SSO 退出地址
	 */
	public String getLogoutUrl() {
		return prop.get("sso.logout.url", logoutUrl);
	}
	
	public void setLogoutUrl( String logoutUrl ) {
		this.logoutUrl = logoutUrl;
	}
	
	/**
	 * SSO 跳转参数命名
	 * <p>
	 * 默认：ReturnURL
	 * </p>
	 */
	public String getParamReturl() {
		return prop.get("sso.param.returl", paramReturl);
	}
	
	public void setParamReturl( String paramReturl ) {
		this.paramReturl = paramReturl;
	}
	
	/**
	 * 跨域 AuthCookie 名称
	 */
	public String getAuthCookieName() {
		return prop.get("sso.authcookie.name", authCookieName);
	}
	
	public void setAuthCookieName( String authCookieName ) {
		this.authCookieName = authCookieName;
	}
	
	/**
	 * 跨域 AuthCookie 超时时间
	 * <p>
	 * -1 浏览器关闭时自动删除 0 立即删除 120 表示Cookie有效期2分钟(以秒为单位)
	 * </p>
	 */
	public int getAuthCookieMaxage() {
		return prop.getInt("sso.authcookie.maxage", authCookieMaxage);
	}
	
	public void setAuthCookieMaxage( int authCookieMaxage ) {
		this.authCookieMaxage = authCookieMaxage;
	}
	
	/**
	 * 自定义 Token Class
	 */
	public Token getToken() {
		if ( token != null ) {
			return token;
		}
		return SSOReflectHelper.getConfigToken(prop.get("sso.token.class", ""));
	}
	
	public void setToken( Token token ) {
		this.token = token;
	}
	
	/**
	 * 自定义 SSOParser Class
	 */
	public SSOParser getParser() {
		if ( parser != null ) {
			return parser;
		}
		return SSOReflectHelper.getConfigParser(prop.get("sso.parser.class", ""));
	}
	
	public void setParser( SSOParser parser ) {
		this.parser = parser;
	}
	
	/**
	 * 自定义 SSOEncrypt Class
	 */
	public SSOEncrypt getEncrypt() {
		if ( encrypt != null ) {
			return encrypt;
		}
		return SSOReflectHelper.getConfigEncrypt(prop.get("sso.encrypt.class", ""));
	}
	
	public void setEncrypt( SSOEncrypt encrypt ) {
		this.encrypt = encrypt;
	}
	
	/**
	 * 自定义 SSOCache Class
	 */
	public SSOCache getCache() {
		if ( cache != null ) {
			return cache;
		}
		return SSOReflectHelper.getConfigCache(prop.get("sso.cache.class", ""));
	}
	
	public void setCache( SSOCache cache ) {
		this.cache = cache;
	}
	
	/**
	 * 自定义SSOCache Expires
	 * <P>
	 * 缓存过期时间，小于0不过期，单位时间 s 秒
	 * </p>
	 * <p>
	 * 设置缓存Token 如缓存不存在将自动退出登录
	 * </p>
	 */
	public int getCacheExpires() {
		return prop.getInt("sso.cache.expires", cacheExpires);
	}
	
	public void setCacheExpires( int cacheExpires ) {
		this.cacheExpires = cacheExpires;
	}
	
	/**
	 * 自定义 SSOStatistic Class
	 */
	public SSOStatistic getStatistic() {
		if ( statistic != null ) {
			return statistic;
		}
		return SSOReflectHelper.getConfigStatistic(prop.get("sso.statistic.class", ""));
	}
	
	public void setStatistic( SSOStatistic statistic ) {
		this.statistic = statistic;
	}
	
	/**
	 * 自定义插件列表
	 */
	public List<SSOPlugin> getPluginList() {
		return pluginList;
	}

	public void setPluginList( List<SSOPlugin> pluginList ) {
		this.pluginList = pluginList;
	}

	/**
	 * 
	 * <p>
	 * 生成 Token 缓存主键
	 * </p>
	 * 
	 * @param uid
	 * 			用户主键ID
	 * @return
	 */
	public static String toCacheKey(String uid) {
		StringBuffer ck = new StringBuffer();
		ck.append("ssoTokenKey_");
		ck.append(uid);
		return ck.toString();
	}
}
