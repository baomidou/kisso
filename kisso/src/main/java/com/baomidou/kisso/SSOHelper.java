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

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.baomidou.kisso.common.Browser;
import com.baomidou.kisso.common.CookieHelper;
import com.baomidou.kisso.common.IpHelper;
import com.baomidou.kisso.common.encrypt.AES;
import com.baomidou.kisso.common.encrypt.Encrypt;
import com.baomidou.kisso.common.encrypt.MD5;
import com.baomidou.kisso.common.util.HttpUtil;
import com.baomidou.kisso.common.util.RandomUtil;
import com.baomidou.kisso.common.util.ReflectUtil;
import com.baomidou.kisso.exception.KissoException;

/**
 * <p>
 * SSO 帮助类
 * </p>
 * 
 * @author hubin
 * @Date 2015-11-30
 */
public class SSOHelper {

	private final static Logger logger = LoggerFactory.getLogger(SSOHelper.class);

	/**
	 * 生成 18 位随机字符串密钥
	 * <p>
	 * 替换配置文件 sso.properties 属性 sso.secretkey=随机18位字符串
	 */
	public static String getSecretKey() {
		return RandomUtil.getCharacterAndNumber(18);
	}

	/**
	 * @Description 加密Token信息
	 * @param request
	 * @param token
	 *            SSO 登录信息票据
	 * @param encrypt
	 *            对称加密算法类
	 * @return Cookie 登录信息Cookie
	 */
	private static String encryptCookie(HttpServletRequest request, Token token, Encrypt encrypt) throws Exception {
		if (token == null) {
			throw new KissoException(" Token not for null.");
		}
		/**
		 * token加密混淆
		 */
		String jt = token.jsonToken();
		StringBuffer sf = new StringBuffer();
		sf.append(jt);
		sf.append(SSOConstant.CUT_SYMBOL);
		/**
		 * 判断是否认证浏览器信息 否取8位随机数混淆
		 */
		if (SSOConfig.getCookieBrowser()) {
			sf.append(Browser.getUserAgent(request, jt));
		} else {
			sf.append(RandomUtil.getCharacterAndNumber(8));
		}
		return encrypt.encrypt(sf.toString(), SSOConfig.getSecretKey());
	}

	/**
	 * 校验Token IP 与登录 IP 是否一致
	 * <p>
	 * 
	 * @param request
	 * @param token
	 *            登录票据
	 * @return Token
	 */
	private static Token checkIp(HttpServletRequest request, Token token) {
		/**
		 * 判断是否检查 IP 一致性
		 */
		if (SSOConfig.getCookieCheckip()) {
			String ip = IpHelper.getIpAddr(request);
			if (token != null && ip != null && !ip.equals(token.getIp())) {
				/**
				 * 检查 IP 与登录IP 不一致返回 null
				 */
				logger.info("ip inconsistent! return token null, token userIp:{}, reqIp:{}",
						new Object[] { token.getIp(), ip });
				return null;
			}
		}
		return token;
	}

	/**
	 * 获取当前请求 JsonToken
	 * <p>
	 * 
	 * @param request
	 * @param encrypt
	 *            对称加密算法类
	 * @param cookieName
	 *            Cookie名称
	 * @return String 当前Token的json格式值
	 */
	private static String getJsonToken(HttpServletRequest request, Encrypt encrypt, String cookieName) {
		Cookie uid = CookieHelper.findCookieByName(request, cookieName);
		if (uid != null) {
			String jsonToken = uid.getValue();
			String[] tokenAttr = new String[2];
			try {
				jsonToken = encrypt.decrypt(jsonToken, SSOConfig.getSecretKey());
				tokenAttr = jsonToken.split(SSOConstant.CUT_SYMBOL);
			} catch (Exception e) {
				logger.error("jsonToken decrypt error.", e);
			}
			/**
			 * 判断是否认证浏览器 混淆信息
			 */
			if (SSOConfig.getCookieBrowser()) {
				if (Browser.isLegalUserAgent(request, tokenAttr[0], tokenAttr[1])) {
					return tokenAttr[0];
				} else {
					/**
					 * 签名验证码失败
					 */
					logger.error("SSOHelper getToken, find Browser is illegal.");
				}
			} else {
				/**
				 * 不需要认证浏览器信息混淆 返回JsonToken
				 */
				return tokenAttr[0];
			}
		}

		return null;
	}

	/**
	 * ------------------------------- 登录相关方法
	 */

	/**
	 * @Description 根据Token生成登录信息Cookie
	 * @param request
	 * @param token
	 *            SSO 登录信息票据
	 * @param encrypt
	 *            对称加密算法类
	 * @return Cookie 登录信息Cookie
	 */
	private static Cookie generateCookie(HttpServletRequest request, Token token, Encrypt encrypt) {
		try {
			Cookie cookie = new Cookie(SSOConfig.getCookieName(), encryptCookie(request, token, encrypt));
			cookie.setPath(SSOConfig.getCookiePath());
			cookie.setSecure(SSOConfig.getCookieSecure());
			cookie.setDomain(SSOConfig.getCookieDomain());
			/**
			 * 设置Cookie超时时间
			 */
			int maxAge = SSOConfig.getCookieMaxage();
			if (maxAge >= 0) {
				cookie.setMaxAge(maxAge);
			}
			return cookie;
		} catch (Exception e) {
			logger.error("generateCookie is exception!", e);
			return null;
		}
	}

	/**
	 * @Description 当前访问域下设置登录Cookie
	 * @param request
	 * @param response
	 * @param encrypt
	 *            对称加密算法类
	 */
	private static void setSSOCookie(HttpServletRequest request, HttpServletResponse response, Token token,
			Encrypt encrypt) {
		if (encrypt == null) {
			throw new KissoException(" Encrypt not for null.");
		}
		try {
			/**
			 * 判断 Token 是否缓存处理失效
			 * <p>
			 * cache 缓存宕机，flag 设置为失效
			 * </p>
			 */
			TokenCache cache = ReflectUtil.getConfigTokenCache();
			if (cache != null) {
				boolean rlt = cache.set(hashCookie(request), token, SSOConfig.getTokenCacheExpires());
				if (!rlt) {
					token.setFlag(Token.Flag.CACHE_SHUT);
				}
			}
			/**
			 * 设置加密 Cookie
			 */
			Cookie ck = generateCookie(request, token, encrypt);
			if (SSOConfig.getCookieHttponly()) {
				/**
				 * Cookie设置HttpOnly
				 */
				CookieHelper.addHttpOnlyCookie(response, ck);
			} else {
				response.addCookie(ck);
			}
		} catch (Exception e) {
			logger.error("set HTTPOnly cookie createAUID is exception! ", e);
		}
	}

	/**
	 * @Description 当前访问域下设置登录Cookie
	 * @param request
	 * @param response
	 */
	public static void setSSOCookie(HttpServletRequest request, HttpServletResponse response, Token token) {
		setSSOCookie(request, response, token, ReflectUtil.getConfigEncrypt());
	}

	/**
	 * @Description 当前访问域下设置登录Cookie 设置防止伪造SESSIONID攻击
	 * @param request
	 * @param response
	 */
	public static void authSSOCookie(HttpServletRequest request, HttpServletResponse response, Token token) {
		CookieHelper.authJSESSIONID(request, RandomUtil.getCharacterAndNumber(8));
		setSSOCookie(request, response, token);
	}

	/**
	 * ------------------------------- 客户端相关方法
	 */
	/**
	 * 获取当前请求 Token
	 * <p>
	 * 
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
	 * 
	 * @param request
	 * @param encrypt
	 *            对称加密算法类
	 * @return Token
	 */
	private static Token getToken(HttpServletRequest request, Encrypt encrypt, TokenCache cache) {
		if (cache == null) {
			throw new KissoException(" TokenCache not for null.");
		}
		if (encrypt == null) {
			throw new KissoException(" Encrypt not for null.");
		}
		return checkIp(request, cacheToken(request, encrypt, cache));
	}

	/**
	 * <p>
	 * Token 是否缓存处理逻辑
	 * </p>
	 * <p>
	 * 判断 Token 是否缓存 ， 如果缓存不存退出登录
	 * </p>
	 * 
	 * @param request
	 * @param encrypt
	 *            对称加密算法类
	 * @return Token
	 */
	private static Token cacheToken(HttpServletRequest request, Encrypt encrypt, TokenCache cache) {
		/**
		 * 如果缓存不存退出登录
		 */
		if (cache != null) {
			Token tk = cache.get(hashCookie(request));
			if (tk != null && tk.getFlag() != Token.Flag.CACHE_SHUT) {
				return tk;
			}
		}

		/**
		 * Token 为 null 执行以下逻辑
		 */
		Token token = null;
		String jsonToken = getJsonToken(request, encrypt, SSOConfig.getCookieName());
		if (jsonToken == null || "".equals(jsonToken)) {
			/**
			 * 未登录请求
			 */
			logger.info("jsonToken is null.");
		} else {
			token = ReflectUtil.getConfigToken();
			token = token.parseToken(jsonToken);
		}
		return token;
	}

	/**
	 * SSO 退出登录
	 */
	public static void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// delete cookie
		logout(request, response, ReflectUtil.getConfigTokenCache());

		// redirect logout page
		response.sendRedirect(SSOConfig.getLogoutUrl());
	}

	/**
	 * 退出当前登录状态
	 * 
	 * @param request
	 * @param response
	 * @param TokenCache
	 * @return boolean true 成功, false 失败
	 */
	private static boolean logout(HttpServletRequest request, HttpServletResponse response, TokenCache cache) {
		if (cache == null) {
			throw new KissoException(" TokenCache not for null.");
		}
		/**
		 * Token 如果开启了缓存，删除缓存记录
		 */
		if (cache != null) {
			boolean rlt = cache.delete(hashCookie(request));
			if (!rlt) {
				cache.delete(hashCookie(request));
			}
		}
		/**
		 * 删除登录 Cookie
		 */
		return CookieHelper.clearCookieByName(request, response, SSOConfig.getCookieName(), SSOConfig.getCookieDomain(),
				SSOConfig.getCookiePath());
	}

	/**
	 * 清除登录状态
	 * 
	 * @param request
	 * @param response
	 * @return boolean true 成功, false 失败
	 */
	public static boolean loginClear(HttpServletRequest request, HttpServletResponse response) {
		// delete cookie
		return logout(request, response, ReflectUtil.getConfigTokenCache());
	}

	/**
	 * <p>
	 * 重新登录 退出当前登录状态、重定向至登录页.
	 * </p>
	 * 
	 * @param request
	 * @param response
	 */
	public static void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String retUrl = HttpUtil.getQueryString(request, SSOConfig.getEncoding());
		logger.debug("loginAgain redirect pageUrl.." + retUrl);

		// redirect login page
		response.sendRedirect(HttpUtil.encodeRetURL(SSOConfig.getLoginUrl(), SSOConfig.getParamReturl(), retUrl));
	}

	/**
	 * <p>
	 * Cookie加密值 Hash
	 * </p>
	 * 
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
	 * 此属性在过滤器拦截器中设置，业务系统中调用有效
	 * </p>
	 * 
	 * @param request
	 * @return
	 * @return Token
	 */
	public static Object attrToken(HttpServletRequest request) {
		return request.getAttribute(SSOConstant.SSO_TOKEN_ATTR);
	}

	/**
	 * ------------------------------- 跨域相关方法
	 * <p>
	 * 1、业务系统访问 SSO 保护系统、 验证未登录跳转至 SSO系统。 2、SSO 设置信任Cookie 生成询问密文，通过代理页面 JSONP
	 * 询问业务系统是否允许登录。 2、业务系统验证询问密文生成回复密文。 3、代理页面 getJSON SSO 验证回复密文，SSO 根据 ok 返回
	 * userId 查询绑定关系进行登录，通知代理页面重定向到访问页面。
	 * </p>
	 */

	/**
	 * 设置跨域信任 Cookie
	 * 
	 * @param authToken
	 *            跨域信任 Token
	 */
	public static void setAuthCookie(HttpServletRequest request, HttpServletResponse response, AuthToken authToken) {
		try {
			CookieHelper.addCookie(response, SSOConfig.getCookieDomain(), SSOConfig.getCookiePath(),
					SSOConfig.getAuthCookieName(), encryptCookie(request, authToken, ReflectUtil.getConfigEncrypt()),
					SSOConfig.getAuthCookieMaxage(), true, SSOConfig.getCookieSecure());
		} catch (Exception e) {
			logger.error("AuthToken encryptCookie error.", e);
		}
	}

	/**
	 * 获取跨域信任 AuthToken
	 * 
	 * @param request
	 * @param publicKey
	 *            RSA 公钥 (SSO)
	 * @return
	 */
	public static AuthToken getAuthToken(HttpServletRequest request, String publicKey) {
		String jsonToken = getJsonToken(request, ReflectUtil.getConfigEncrypt(), SSOConfig.getAuthCookieName());
		if (jsonToken == null || "".equals(jsonToken)) {
			logger.info("jsonToken is null.");
			return null;
		} else {
			/**
			 * 校验 IP 合法返回 AuthToken
			 */
			AuthToken at = JSON.parseObject(jsonToken, AuthToken.class);
			if (checkIp(request, at) == null) {
				return null;
			}
			return at.verify(publicKey);
		}
	}

	/**
	 * 生成跨域询问密文
	 * 
	 * @param authToken
	 *            跨域信任 Token
	 * @param aesKey
	 *            AES 密钥
	 */
	public static String askCiphertext(AuthToken authToken, String aesKey) {
		try {
			return AES.getInstance().encrypt(authToken.jsonToken(), aesKey);
		} catch (Exception e) {
			logger.info("askCiphertext AES encrypt error.", e);
		}
		return null;
	}

	/**
	 * 生成跨域回复密文
	 * 
	 * @param authToken
	 *            跨域信任 Token
	 * @param userId
	 *            用户ID
	 * @param askTxt
	 *            询问密文
	 * @param privateKey
	 *            RSA 密钥 (业务系统)
	 * @param publicKey
	 *            RSA 公钥 (SSO)
	 * @param aesKey
	 *            AES 密钥
	 */
	public static String replyCiphertext(HttpServletRequest request, String userId, String askTxt, String privateKey,
			String publicKey, String aesKey) {
		String at = null;
		try {
			at = AES.getInstance().decrypt(askTxt, aesKey);
		} catch (Exception e) {
			logger.info("replyCiphertext AES decrypt error.", e);
		}
		if (at != null) {
			AuthToken authToken = JSON.parseObject(at, AuthToken.class);
			if (checkIp(request, authToken.verify(publicKey)) != null) {
				authToken.setUserId(userId);
				try {
					/**
					 * 使用业务系统密钥 重新签名
					 */
					authToken.sign(privateKey);
					return AES.getInstance().encrypt(authToken.jsonToken(), aesKey);
				} catch (Exception e) {
					logger.info("replyCiphertext AES encrypt error.", e);
				}
			}
		}
		return null;
	}

	/**
	 * 验证回复密文，成功! 返回绑定用户ID
	 * 
	 * @param request
	 * @param response
	 * @param authToken
	 *            跨域信任 Token
	 * @param askTxt
	 *            询问密文
	 * @param tokenPk
	 *            RSA 公钥 (业务系统 AuthToken加密公钥)
	 * @param replyPk
	 *            RSA 公钥 (SSO 回复密文公钥)
	 * @param aesKey
	 *            AES 密钥
	 * @return 用户ID
	 */
	public static String ok(HttpServletRequest request, HttpServletResponse response, String replyTxt, String tokenPk,
			String replyPk, String aesKey) {
		AuthToken authToken = getAuthToken(request, tokenPk);
		if (authToken != null) {
			String rt = null;
			try {
				rt = AES.getInstance().decrypt(replyTxt, aesKey);
			} catch (Exception e) {
				logger.error("kisso AES decrypt error.", e);
			}
			if (rt != null) {
				AuthToken atk = JSON.parseObject(rt, AuthToken.class);
				if (atk != null && atk.getUuid().equals(authToken.getUuid())) {
					if (atk.verify(replyPk) != null) {
						/**
						 * 删除跨域信任Cookie 返回 userId
						 */
						CookieHelper.clearCookieByName(request, response, SSOConfig.getAuthCookieName(),
								SSOConfig.getCookieDomain(), SSOConfig.getCookiePath());
						return atk.getUserId();
					}
				}
			}
		}
		return null;
	}

}
