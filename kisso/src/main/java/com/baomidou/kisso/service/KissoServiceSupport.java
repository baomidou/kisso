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
package com.baomidou.kisso.service;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.kisso.SSOCache;
import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOPlugin;
import com.baomidou.kisso.SSOStatistic;
import com.baomidou.kisso.Token;
import com.baomidou.kisso.common.Browser;
import com.baomidou.kisso.common.CookieHelper;
import com.baomidou.kisso.common.IpHelper;
import com.baomidou.kisso.common.encrypt.SSOEncrypt;
import com.baomidou.kisso.common.util.RandomUtil;
import com.baomidou.kisso.exception.KissoException;

/**
 * <p>
 * SSO 单点登录服务支持类
 * </p>
 * 
 * @author hubin
 * @Date 2015-12-03
 */
public class KissoServiceSupport {
	protected final Logger logger = Logger.getLogger("KissoServiceSupport");
	protected SSOConfig config;
	
	public SSOConfig getConfig() {
		return config;
	}
	
	public void setConfig( SSOConfig config ) {
		this.config = config;
	}

	/**
	 * ------------------------------- 客户端相关方法 -------------------------------
	 */
	
	/**
	 * 获取当前请求 Token
	 * <p>
	 * 此属性在过滤器拦截器中设置，业务系统中调用有效
	 * </p>
	 * 
	 * @param request
	 * @return
	 * @return Token {@link Token}
	 */
	@SuppressWarnings("unchecked")
	public <T extends Token> T attrToken( HttpServletRequest request ) {
		return (T) request.getAttribute(SSOConfig.SSO_TOKEN_ATTR);
	}

	/**
	 * <p>
	 * 获取当前请求 Token
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @param encrypt
	 *            对称加密算法类
	 * @return Token {@link Token}
	 */
	protected Token getToken( HttpServletRequest request, HttpServletResponse response, SSOEncrypt encrypt,
			SSOCache cache ) {
		if ( encrypt == null ) {
			throw new KissoException(" Encrypt not for null.");
		}
		Token tk = checkIp(request, cacheToken(request, encrypt, cache));
		/**
		 * 执行插件逻辑
		 */
		List<SSOPlugin> pluginList = config.getPluginList();
		if ( pluginList != null ) {
			for ( SSOPlugin plugin : pluginList ) {
				boolean valid = plugin.validateToken(tk);
				if (!valid) {
					return null;
				}
			}
		}
		return tk;
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
	 * @return Token {@link Token}
	 */
	private Token cacheToken( HttpServletRequest request, SSOEncrypt encrypt, SSOCache cache ) {
		/**
		 * 如果缓存不存退出登录
		 */
		if ( cache != null ) {
			Token cookieToken = getTokenFromCookie(request);
			if ( cookieToken == null ) {
				/* 未登录 */
				return null;
			}
			
			Token cacheToken = cache.get(cookieToken.toCacheKey());
			if ( cacheToken == null ) {
				/* 开启缓存且失效，返回 null 清除 Cookie 退出 */
				logger.fine("cacheToken token is null.");
				return null;
			} else {
				/*
				 * 开启缓存，判断是否宕机：
				 * 1、缓存正常，返回 tk
				 * 2、缓存宕机，执行读取 Cookie 逻辑
				 */
				if ( cacheToken.getFlag() != Token.FLAG_CACHE_SHUT ) {
					/*
					 * 验证 cookie 与 cache 中 token 登录时间是否<br>
					 * 不一致返回 null
					 */
					if(cookieToken.getTime() == cacheToken.getTime()){
						return cacheToken;
					} else {
						logger.severe("Login time is not consistent.");
						return null;
					}
				}
			}
		}

		/**
		 * Token 为 null 执行以下逻辑
		 */
		return getToken(request, encrypt, config.getCookieName());
	}
	
	/**
	 * <p>
	 * 获取当前请求 Token
	 * </p>
	 * 
	 * @param request
	 * @param encrypt
	 *            对称加密算法类
	 * @param cookieName
	 *            Cookie名称
	 * @return Token ${Token}
	 */
	protected Token getToken( HttpServletRequest request, SSOEncrypt encrypt, String cookieName ) {
		String jsonToken = this.getJsonToken(request, null, encrypt, cookieName);
		if ( jsonToken == null || "".equals(jsonToken) ) {
			/**
			 * 未登录请求
			 */
			logger.fine("jsonToken is null.");
			return null;
		} else {
			return config.getToken().parseToken(jsonToken);
		}
	}
	
	/**
	 * <p>
	 * 获取当前请求 JsonToken
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @param encrypt
	 *            对称加密算法类
	 * @param cookieName
	 *            Cookie名称
	 * @return String 当前Token的json格式值
	 */
	protected String getJsonToken( HttpServletRequest request, HttpServletResponse response, SSOEncrypt encrypt,
			String cookieName ) {
		Cookie uid = CookieHelper.findCookieByName(request, cookieName);
		if ( uid != null ) {
			String jsonToken = uid.getValue();
			String[] tokenAttr = new String[2];
			try {
				jsonToken = encrypt.decrypt(jsonToken, config.getSecretkey());
				tokenAttr = jsonToken.split(SSOConfig.CUT_SYMBOL);
			} catch ( Exception e ) {
				logger.severe("jsonToken decrypt error." + e.toString());
				//此处可能是伪造 Cookie 清理，返回 403
				CookieHelper.clearCookieByName(request, response, config.getCookieName(), config.getCookieDomain(),
					config.getCookiePath());
				try {
					response.sendError(403, "Forbidden");
				} catch (Exception ex) {
					//to do
				}
				
			}
			/**
			 * 判断是否认证浏览器 混淆信息
			 */
			if ( config.getCookieBrowser() ) {
				if ( Browser.isLegalUserAgent(request, tokenAttr[0], tokenAttr[1]) ) {
					return tokenAttr[0];
				} else {
					/**
					 * 签名验证码失败
					 */
					logger.severe("SSOHelper getToken, find Browser is illegal.");
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
	 * <p>
	 * 校验Token IP 与登录 IP 是否一致
	 * </p>
	 * 
	 * @param request
	 * @param token
	 *            登录票据
	 * @return Token {@link Token}
	 */
	protected Token checkIp( HttpServletRequest request, Token token ) {
		/**
		 * 判断是否检查 IP 一致性
		 */
		if ( config.getCookieCheckip() ) {
			String ip = IpHelper.getIpAddr(request);
			if ( token != null && ip != null && !ip.equals(token.getIp()) ) {
				/**
				 * 检查 IP 与登录IP 不一致返回 null
				 */
				logger.info(String.format("ip inconsistent! return token null, token userIp:%s, reqIp:%s", 
					token.getIp(), ip));
				return null;
			}
		}
		return token;
	}


	/**
	 * 
	 * cookie 中获取 Token, 该方法未验证 IP 等其他信息。
	 * 
	 * <p>
	 * 1、自动设置
	 * 2、拦截器 request 中获取
	 * 3、解密 Cookie 获取
	 * </p>
	 * 
	 * @param request
	 * 				HTTP 请求
	 * @param token
	 * 				SSO 票据
	 * @return
	 */
	public Token getTokenFromCookie(HttpServletRequest request) {
		Token tk = this.attrToken(request);
		if (tk == null) {
			tk = this.getToken(request, config.getEncrypt(), config.getCookieName());
		}
		if (tk == null) {
			logger.severe("please login to use.");
			return null;
		}
		return tk;
	}
	
	
	/**
	 * ------------------------------- 登录相关方法 -------------------------------
	 */

	/**
	 * <p>
	 * 根据Token生成登录信息Cookie
	 * </p>
	 * 
	 * @param request
	 * @param token
	 *            SSO 登录信息票据
	 * @param encrypt
	 *            对称加密算法类
	 * @return Cookie 登录信息Cookie {@link Cookie}
	 */
	private Cookie generateCookie( HttpServletRequest request, Token token, SSOEncrypt encrypt ) {
		try {
			Cookie cookie = new Cookie(config.getCookieName(), encryptCookie(request, token, encrypt));
			cookie.setPath(config.getCookiePath());
			cookie.setSecure(config.getCookieSecure());
			/**
			 * domain 提示
			 * <p>
			 * 有些浏览器 localhost 无法设置 cookie
			 * </p>
			 */
			String domain = config.getCookieDomain();
			cookie.setDomain(domain);
			if ( "".equals(domain) || domain.contains("localhost") ) {
				logger.warning("if you can't login, please enter normal domain. instead:" + domain);
			}

			/**
			 * 设置Cookie超时时间
			 */
			int maxAge = config.getCookieMaxage();
			Integer attrMaxAge = (Integer) request.getAttribute(SSOConfig.SSO_COOKIE_MAXAGE);
			if (attrMaxAge != null) {
				maxAge = attrMaxAge;
			}
			if ( maxAge >= 0 ) {
				cookie.setMaxAge(maxAge);
			}
			return cookie;
		} catch ( Exception e ) {
			logger.severe("generateCookie is exception!");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * <p>
	 * 加密Token信息
	 * </p>
	 * 
	 * @param request
	 * @param token
	 *            SSO 登录信息票据
	 * @param encrypt
	 *            对称加密算法类
	 * @return Cookie 登录信息Cookie {@link Cookie}
	 */
	protected String encryptCookie( HttpServletRequest request, Token token, SSOEncrypt encrypt ) throws Exception {
		if ( token == null ) {
			throw new KissoException(" Token not for null.");
		}
		/**
		 * token加密混淆
		 */
		String jt = token.jsonToken();
		StringBuffer sf = new StringBuffer();
		sf.append(jt);
		sf.append(SSOConfig.CUT_SYMBOL);
		/**
		 * 判断是否认证浏览器信息 否取8位随机数混淆
		 */
		if ( config.getCookieBrowser() ) {
			sf.append(Browser.getUserAgent(request, jt));
		} else {
			sf.append(RandomUtil.getCharacterAndNumber(8));
		}
		return encrypt.encrypt(sf.toString(), config.getSecretkey());
	}
	
	/**
	 * <p>
	 * 当前访问域下设置登录Cookie
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @param encrypt
	 *            对称加密算法类
	 */
	protected void setSSOCookie( HttpServletRequest request, HttpServletResponse response, Token token,
			SSOEncrypt encrypt ) {
		if ( encrypt == null ) {
			throw new KissoException(" Encrypt not for null.");
		}
		try {
			/**
			 * 设置加密 Cookie
			 */
			Cookie ck = generateCookie(request, token, encrypt);

			/**
			 * 判断 Token 是否缓存处理失效
			 * <p>
			 * cache 缓存宕机，flag 设置为失效
			 * </p>
			 */
			SSOCache cache = config.getCache();
			if ( cache != null ) {
				boolean rlt = cache.set(token.toCacheKey(), token, config.getCacheExpires());
				if ( !rlt ) {
					token.setFlag(Token.FLAG_CACHE_SHUT);
				}
			}

			/**
			 * 在线人数统计 +1
			 */
			SSOStatistic statistic = config.getStatistic();
			if ( statistic != null ) {
				boolean rlt = statistic.increase(request);
				if ( !rlt ) {
					statistic.increase(request);
				}
			}
			
			/**
			 * 执行插件逻辑
			 */
			List<SSOPlugin> pluginList = config.getPluginList();
			if ( pluginList != null ) {
				for ( SSOPlugin plugin : pluginList ) {
					boolean login = plugin.login(request, response);
					if ( !login ) {
						plugin.login(request, response);
					}
				}
			}

			/**
			 * Cookie设置HttpOnly
			 */
			if ( config.getCookieHttponly() ) {
				CookieHelper.addHttpOnlyCookie(response, ck);
			} else {
				response.addCookie(ck);
			}
		} catch ( Exception e ) {
			logger.severe("set HTTPOnly cookie createAUID is exception! ");
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 退出当前登录状态
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @param SSOCache
	 * @return boolean true 成功, false 失败
	 */
	protected boolean logout( HttpServletRequest request, HttpServletResponse response, SSOCache cache ) {
		/**
		 * Token 如果开启了缓存，删除缓存记录
		 */
		if ( cache != null ) {
			Token tk = getTokenFromCookie(request);
			if ( tk != null ) {
				boolean rlt = cache.delete(tk.toCacheKey());
				if ( !rlt ) {
					cache.delete(tk.toCacheKey());
				}
			}
		}

		/**
		 * 在线人数统计 -1
		 */
		SSOStatistic statistic = config.getStatistic();
		if ( statistic != null ) {
			boolean rlt = statistic.decrease(request);
			if ( !rlt ) {
				statistic.decrease(request);
			}
		}
		
		/**
		 * 执行插件逻辑
		 */
		List<SSOPlugin> pluginList = config.getPluginList();
		if ( pluginList != null ) {
			for ( SSOPlugin plugin : pluginList ) {
				boolean logout = plugin.logout(request, response);
				if ( !logout ) {
					plugin.logout(request, response);
				}
			}
		}

		/**
		 * 删除登录 Cookie
		 */
		return CookieHelper.clearCookieByName(request, response, config.getCookieName(), config.getCookieDomain(),
			config.getCookiePath());
	}
}
