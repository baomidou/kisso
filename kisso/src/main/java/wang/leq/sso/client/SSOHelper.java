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
package wang.leq.sso.client;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.leq.sso.KissoHelper;
import wang.leq.sso.SSOConfig;
import wang.leq.sso.SSOConstant;
import wang.leq.sso.Token;
import wang.leq.sso.TokenCache;
import wang.leq.sso.common.CookieHelper;
import wang.leq.sso.common.encrypt.Encrypt;
import wang.leq.sso.common.encrypt.MD5;
import wang.leq.sso.common.util.HttpUtil;
import wang.leq.sso.common.util.ReflectUtil;
import wang.leq.sso.exception.KissoException;

/**
 * SSO客户端帮助类
 * <p>
 * @author   hubin
 * @Date	 2014-5-8 	 
 */
public class SSOHelper {
	private final static Logger logger = LoggerFactory.getLogger(SSOHelper.class);

	/**
	 * 获取当前请求 Token
	 * <p>
	 * @param request
	 * @return 
	 * @return Token
	 */
	public static Token getToken(HttpServletRequest request) {
		return getToken(request, ReflectUtil.getConfigEncrypt(), ReflectUtil.getConfigTokenCache());
	}

	/**
	 * 获取当前请求 Token
	 * <p>
	 * @param request
	 * @param encrypt
	 * 				对称加密算法类
	 * @return Token
	 */
	private static Token getToken(HttpServletRequest request, Encrypt encrypt, TokenCache cache) {
		if (cache == null) {
			throw new KissoException(" TokenCache not for null.");
		}
		if (encrypt == null) {
			throw new KissoException(" Encrypt not for null.");
		}
		return KissoHelper.checkIp(request, cacheToken(request, encrypt, cache));
	}

	/**
	 * Token 是否缓存至 session处理逻辑
	 * <p>
	 * @param request
	 * @param encrypt
	 * 				对称加密算法类
	 * @return Token
	 */
	private static Token cacheToken(HttpServletRequest request, Encrypt encrypt, TokenCache cache) {
		Token token = null;
		/**
		 * 判断 Token 是否缓存至 Map
		 * 减少Cookie解密耗时
		 */
		if (SSOConfig.getCookieCache() && cache != null) {
			token = cache.get(hashCookie(request));
		}

		/**
		 * Token 为 null
		 * 执行以下逻辑
		 */
		if (token == null) {
			String jsonToken = KissoHelper.getJsonToken(request, encrypt, SSOConfig.getCookieName());
			if (jsonToken == null || "".equals(jsonToken)) {
				/**
				 * 未登录请求
				 */
				logger.info("jsonToken is null.");
				return null;
			} else {
				token = ReflectUtil.getConfigToken();
				token = token.parseToken(jsonToken);

				/**
				 * 判断 Token 是否缓存至 session
				 * 减少解密次数、提高访问速度
				 */
				if (SSOConfig.getCookieCache() && cache != null) {
					cache.set(hashCookie(request), token);
				}
			}
		}
		return token;
	}

	/**
	 * SSO 退出登录
	 */
	public static void logout( HttpServletRequest request, HttpServletResponse response ) throws IOException {
		//delete cookie
		logout(request, response, ReflectUtil.getConfigTokenCache());

		//redirect logout page
		response.sendRedirect(SSOConfig.getLogoutUrl());
	}

	/**
	 * 退出当前登录状态
	 * <p>
	 * @param request
	 * @param response
	 * @param TokenCache
	 * @return boolean <p>true 成功, false 失败</p>
	 */
	private static boolean logout(HttpServletRequest request, HttpServletResponse response, TokenCache cache) {
		if (cache == null) {
			throw new KissoException(" TokenCache not for null.");
		}
		/**
		 * Token 如果开启了session缓存
		 * 删除缓存记录
		 */
		if (SSOConfig.getCookieCache()) {
			cache.delete(hashCookie(request));
		}
		/**
		 * 删除登录 Cookie
		 */
		return CookieHelper.clearCookieByName(request, response, SSOConfig.getCookieName(),
				SSOConfig.getCookieDomain(), SSOConfig.getCookiePath());
	}

	/**
	 * 清除登录状态
	 * <p>
	 * @param request
	 * @param response
	 * @return boolean <p>true 成功, false 失败</p>
	 */
	public static boolean loginClear( HttpServletRequest request, HttpServletResponse response ) {
		//delete cookie
		return logout(request, response, ReflectUtil.getConfigTokenCache());
	}
	
	/**
	 * 重新登录
	 * <p>
	 * 退出当前登录状态、重定向至登录页.
	 * @param request
	 * @param response
	 */
	public static void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String retUrl = HttpUtil.getQueryString(request, SSOConfig.getEncoding());
		logger.debug("loginAgain redirect pageUrl.." + retUrl);

		//redirect login page
		response.sendRedirect(HttpUtil.encodeRetURL(SSOConfig.getLoginUrl(), "ReturnURL", retUrl));
	}

	/**
	 * Cookie加密值 Hash
	 * <p>
	 * @param request
	 * @return String
	 */
	public static String hashCookie(HttpServletRequest request) {
		Cookie uid = CookieHelper.findCookieByName(request, SSOConfig.getCookieName());
		if (uid != null) {
			/**
			 * MD5 Cookie
			 */
			StringBuffer cmd5 = new StringBuffer();
			cmd5.append("ssocookie_");
			cmd5.append(MD5.toMD5(uid.getValue()));
			return cmd5.toString();
		}
		return null;
	}

	/**
	 * 获取当前请求 Token
	 * <p>
	 * 		此属性在过滤器拦截器中设置，业务系统中调用有效
	 * @param request
	 * @return 
	 * @return Token
	 */
	public static Object attrToken( HttpServletRequest request ) {
		return request.getAttribute(SSOConstant.SSO_TOKEN_ATTR);
	}
}
