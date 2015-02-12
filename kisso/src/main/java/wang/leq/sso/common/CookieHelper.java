/**
 * Copyright (c) 2011-2014, hubin (243194995@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wang.leq.sso.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.leq.sso.SSOConstant;

/**
 * Cookie工具类 
 * 注意：在cookie的名或值中不能使用分号（;）、逗号（,）、等号（=）以及空格
 * <p>
 * @author   hubin
 * @Date	 2014-5-8 	 
 */
public class CookieHelper {
	private final static Logger logger = LoggerFactory.getLogger(CookieHelper.class);
	public final static int CLEAR_BROWSER_IS_CLOSED = -1;//浏览器关闭时自动删除
	public final static int CLEAR_IMMEDIATELY_REMOVE = 0;//立即删除

	/**
	 * @Description 防止伪造SESSIONID攻击. 
	 * 				用户登录校验成功销毁当前JSESSIONID.
	 * 				创建可信的JSESSIONID
	 * @param request
	 * 				当前HTTP请求
	 * @param value
	 * 				用户ID等唯一信息
	 */
	public static void authJSESSIONID(HttpServletRequest request, String value) {
		request.getSession().invalidate();
		request.getSession().setAttribute("KISSO-" + value, true);
	}
	
	/**
	 * @Description 根据cookieName获取Cookie
	 * @param request
	 * @param cookieName
	 * 			Cookie name
	 * @return Cookie
	 */
	public static Cookie findCookieByName(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals(cookieName)) {
				return cookies[i];
			}
		}
		return null;
	}

	/**
	 * 根据 cookieName 清空 Cookie【默认域下】 
	 * @param response
	 * @param cookieName
	 */
	public static void clearCookieByName(HttpServletResponse response, String cookieName){
		Cookie cookie = new Cookie(cookieName, "");
		cookie.setMaxAge(CLEAR_IMMEDIATELY_REMOVE);
		response.addCookie(cookie);
	}
	
	/**
	 * @Description 清除指定doamin的所有Cookie
	 * @param request
	 * @param response
	 * @param cookieName
	 *            cookie name
	 * @param domain
	 *            Cookie所在的域
	 * @param path
	 *            Cookie 路径
	 * @return 
	 */
	public static void clearAllCookie(HttpServletRequest request, HttpServletResponse response, String domain,
			String path) {
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			clearCookie(response, cookies[i].getName(), domain, path);
		}
		logger.info("clearAllCookie in  domain " + domain);
	}

	/**
	 * @Description 根据cookieName清除指定Cookie
	 * @param request
	 * @param response
	 * @param cookieName
	 *            cookie name
	 * @param domain
	 *            Cookie所在的域
	 * @param path
	 *            Cookie 路径
	 * @return boolean
	 */
	public static boolean clearCookieByName(HttpServletRequest request, HttpServletResponse response,
			String cookieName, String domain, String path) {
		boolean result = false;
		Cookie ck = findCookieByName(request, cookieName);
		if (ck != null) {
			result = clearCookie(response, cookieName, domain, path);
		}
		return result;
	}

	/**
	 * @Description 清除指定Cookie 等同于 clearCookieByName(...)
	 * 该方法不判断Cookie是否存在,因此不对外暴露防止Cookie不存在异常.
	 * @param response
	 * @param cookieName
	 *            cookie name
	 * @param domain
	 *            Cookie所在的域
	 * @param path
	 *            Cookie 路径
	 * @return boolean
	 */
	private static boolean clearCookie(HttpServletResponse response, String cookieName,
			String domain, String path) {
		boolean result = false;
		try {
			Cookie cookie = new Cookie(cookieName, "");
			cookie.setMaxAge(CLEAR_IMMEDIATELY_REMOVE);
			cookie.setDomain(domain);
			cookie.setPath(path);
			response.addCookie(cookie);
			logger.info("clear cookie" + cookieName);
			result = true;
		} catch (Exception e) {
			logger.error("clear cookie" + cookieName + " is exception!", e);
		}
		return result;
	}

	/**
	 * 当前域下添加 Cookie 关闭浏览器失效
	 * <p>
	 * @param response
	 * @param name 
	 * 				名称
	 * @param value 
	 * 				内容
	 */
	public static void addCookie(HttpServletResponse response, String name, String value) {
		addCookie(response, null, name, value);
	}

	/**
	 * 添加 Cookie 关闭浏览器失效
	 * <p>
	 * @param response
	 * @param domain 
	 * 				所在域
	 * @param name 
	 * 				名称
	 * @param value 
	 * 				内容
	 */
	public static void addCookie(HttpServletResponse response, String domain, String name, String value) {
		addCookie(response, domain, SSOConstant.SSO_COOKIE_PATH, name, value, CLEAR_BROWSER_IS_CLOSED, false, false);
	}

	/**
	 * 添加 Cookie
	 * <p>
	 * @param response
	 * @param domain 
	 * 				所在域
	 * @param path	 
	 * 				域名路径
	 * @param name 
	 * 				名称
	 * @param value 
	 * 				内容
	 * @param maxAge 
	 * 				生命周期参数
	 * @param httpOnly 
	 * 				只读
	 * @param secured 
	 * 				Https协议下安全传输
	 */
	public static void addCookie(HttpServletResponse response, String domain, String path, String name, String value,
			int maxAge, boolean httpOnly, boolean secured) {
		Cookie cookie = new Cookie(name, value);
		/**
		 * 不设置该参数默认
		 * 当前所在域
		 */
		if (domain != null && !"".equals(domain)) {
			cookie.setDomain(domain);
		}
		cookie.setPath(path);
		cookie.setMaxAge(maxAge);

		/** Cookie 只在Https协议下传输设置 */
		if (secured) {
			cookie.setSecure(secured);
		}

		/** Cookie 只读设置 */
		if (httpOnly) {
			addHttpOnlyCookie(response, cookie);
		} else {
			//cookie.setHttpOnly(httpOnly);//servlet 3.0 support
			response.addCookie(cookie);
		}

	}

	/**
	 * 解决 servlet 3.0 以下版本不支持 HttpOnly
	 * <p>
	 * @param response HttpServletResponse类型的响应 
	 * @param cookie 要设置httpOnly的cookie对象 
	 */
	public static void addHttpOnlyCookie(HttpServletResponse response, Cookie cookie) {
		if (cookie == null) {
			return;
		}
		/**
		 * 依次取得cookie中的名称、值、
		 * 最大生存时间、路径、域和是否为安全协议信息 
		 */
		String cookieName = cookie.getName();
		String cookieValue = cookie.getValue();
		int maxAge = cookie.getMaxAge();
		String path = cookie.getPath();
		String domain = cookie.getDomain();
		boolean isSecure = cookie.getSecure();
		StringBuffer sf = new StringBuffer();
		sf.append(cookieName + "=" + cookieValue + ";");

		if (maxAge >= 0) {
			sf.append("Max-Age=" + cookie.getMaxAge() + ";");
		}

		if (domain != null) {
			sf.append("domain=" + domain + ";");
		}

		if (path != null) {
			sf.append("path=" + path + ";");
		}

		if (isSecure) {
			sf.append("secure;HTTPOnly;");
		} else {
			sf.append("HTTPOnly;");
		}

		response.addHeader("Set-Cookie", sf.toString());
	}

}
