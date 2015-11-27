/**
 * Copyright (c) 2011-2014, hubin (243194995@qq.com).
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

import java.util.Properties;
import java.util.logging.Logger;

import com.baomidou.kisso.common.util.PropertiesUtil;
import com.baomidou.kisso.common.util.ReflectUtil;
import com.baomidou.kisso.exception.KissoException;

/**
 * <p>
 * SSO 配置文件解析
 * </p>
 * 按照不同的运行模式启用相应的配置 默认开发环境， 调试方式 Eclipse 环境的 VM 参数中加上
 * -Dsso.production.mode=dev_mode 例如：
 * ------------------------------------------------------------------
 * sso.login.url_online_mode=http://sso.online.com/login.html
 * sso.login.url_test_mode=http://sso.test.com/login.html
 * sso.login.url_dev_mode=http://localhost:8080/login.html
 * ------------------------------------------------------------------
 * 
 * @author hubin
 * @Date 2015-11-30
 */
public class SSOConfig {

	private static final Logger logger = Logger.getLogger("SSOConfig");

	/**
	 * 运行模式
	 */
	private static final String SSO_PRODUCTION_MODE = "sso.production.mode";

	private static PropertiesUtil prop = null;

	/**
	 * SSO 资源文件初始化
	 */
	public static void init(Properties props) {
		if (props != null) {
			prop = new PropertiesUtil(props, SSO_PRODUCTION_MODE);
			logger.config("loading kisso config.");
			// 初始化反射配置
			ReflectUtil.init();
			logger.info("kisso init success.");
		} else {
			throw new KissoException(" cannot load kisso config. ");
		}
	}

	/**
	 * SSO 配置模式 -------------------- 
	 * dev_mode 开发模式 test_mode 测试模式 online_mode
	 * 生产模式 --------------------
	 */
	public String getProductionMode() {
		return prop.get(SSO_PRODUCTION_MODE, "online_mode");
	}

	/**
	 * PropertiesUtil
	 */
	public static PropertiesUtil getSSOProperties() {
		return prop;
	}

	/**
	 * 编码格式默认 UTF-8
	 */
	public static String getEncoding() {
		if (prop == null) {
			return SSOConstant.ENCODING;
		}
		return prop.get("sso.encoding", SSOConstant.ENCODING);
	}

	/**
	 * 密钥
	 */
	public static String getSecretKey() {
		return prop.get("sso.secretkey", SSOConstant.SSO_SECRET_KEY);
	}

	/**
	 * Cookie 只允许https协议传输
	 */
	public static boolean getCookieSecure() {
		return prop.getBoolean("sso.cookie.secure", SSOConstant.SSO_COOKIE_SECURE);
	}

	/**
	 * Cookie 只读,不允许 Js访问
	 */
	public static boolean getCookieHttponly() {
		return prop.getBoolean("sso.cookie.httponly", SSOConstant.SSO_COOKIE_HTTPONLY);
	}

	/**
	 * Cookie 超时时间
	 */
	public static int getCookieMaxage() {
		return prop.getInt("sso.cookie.maxage", SSOConstant.SSO_COOKIE_MAXAGE);
	}

	/**
	 * Cookie 名称
	 */
	public static String getCookieName() {
		return prop.get("sso.cookie.name", SSOConstant.SSO_COOKIE_NAME);
	}

	/**
	 * Cookie 所在域
	 */
	public static String getCookieDomain() {
		return prop.get("sso.cookie.domain", SSOConstant.SSO_COOKIE_DOMAIN);
	}

	/**
	 * Cookie 域路径
	 */
	public static String getCookiePath() {
		return prop.get("sso.cookie.path", SSOConstant.SSO_COOKIE_PATH);
	}

	/**
	 * Cookie 开启浏览器版本校验
	 */
	public static boolean getCookieBrowser() {
		return prop.getBoolean("sso.cookie.browser", SSOConstant.SSO_COOKIE_BROWSER);
	}

	/**
	 * Cookie 开启IP校验
	 */
	public static boolean getCookieCheckip() {
		return prop.getBoolean("sso.cookie.checkip", SSOConstant.SSO_COOKIE_CHECKIP);
	}

	/**
	 * 自定义Encrypt Class
	 */
	public static String getEncryptClass() {
		return prop.get("sso.encrypt.class", SSOConstant.SSO_ENCRYPT_CLASS);
	}

	/**
	 * 自定义Token Class
	 */
	public static String getTokenClass() {
		return prop.get("sso.token.class", SSOConstant.SSO_TOKEN_CLASS);
	}
	
	/**
	 * 自定义SSOStatistic Class
	 */
	public static String getStatisticClass() {
		return prop.get("sso.statistic.class", SSOConstant.SSO_STATISTIC_CLASS);
	}

	/**
	 * 自定义SSOCache Class
	 */
	public static String getCacheClass() {
		return prop.get("sso.cache.class", SSOConstant.SSO_CACHE_CLASS);
	}
	
	/**
	 * 自定义SSOCache Expires
	 */
	public static int getSSOCacheExpires() {
		return prop.getInt("sso.cache.expires", SSOConstant.SSO_CACHE_EXPIRES);
	}

	/**
	 * SSO 登录地址
	 */
	public static String getLoginUrl() {
		return prop.get("sso.login.url", SSOConstant.SSO_LOGIN_URL);
	}

	/**
	 * SSO 退出地址
	 */
	public static String getLogoutUrl() {
		return prop.get("sso.logout.url", SSOConstant.SSO_LOGOUT_URL);
	}

	/**
	 * 跨域信任 Cookie 名称
	 */
	public static String getAuthCookieName() {
		return prop.get("sso.crossdomain.cookie.name", SSOConstant.SSO_AUTH_COOKIE_NAME);
	}

	/**
	 * 跨域信任 Cookie 超时时间
	 */
	public static int getAuthCookieMaxage() {
		return prop.getInt("sso.crossdomain.cookie.maxage", SSOConstant.SSO_AUTH_COOKIE_MAXAGE);
	}

	/**
	 * 跳转参数 默认：ReturnURL
	 */
	public static String getParamReturl() {
		return prop.get("sso.param.returl", SSOConstant.SSO_PARAM_RETURL);
	}
}
