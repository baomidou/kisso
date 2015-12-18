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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.baomidou.kisso.AuthToken;
import com.baomidou.kisso.SSOStatistic;
import com.baomidou.kisso.Token;
import com.baomidou.kisso.common.CookieHelper;
import com.baomidou.kisso.common.util.HttpUtil;
import com.baomidou.kisso.common.util.RandomUtil;

/**
 * <p>
 * SSO 单点登录服务抽象实现类
 * </p>
 * 
 * @author hubin
 * @Date 2015-12-03
 */
public class AbstractKissoService extends KissoServiceSupport implements KissoService {

	/**
	 * 获取当前请求 Token
	 * <p>
	 * 从 Cookie 解密 token 使用场景，拦截器，非拦截器建议使用 attrToken 减少二次解密
	 * </p>
	 * 
	 * @param request
	 * @return
	 * @return Token
	 */
	public Token getToken( HttpServletRequest request ) {
		return getToken(request, config.getEncrypt(), config.getCache());
	}

	/**
	 * 在线人数（总数）
	 * 
	 * @param request
	 *            查询请求
	 * @return
	 */
	public String getLoginCount( HttpServletRequest request ) {
		SSOStatistic statistic = config.getStatistic();
		if ( statistic != null ) {
			return statistic.count(request);
		} else {
			logger.warning("please instanceof SSOStatistic.");
		}
		return null;
	}
	
	/**
	 * 
	 * 当前访问域下设置登录Cookie
	 * 
	 * @param request
	 * @param response
	 */
	public void setSSOCookie( HttpServletRequest request, HttpServletResponse response, Token token ) {
		setSSOCookie(request, response, token, config.getEncrypt());
	}

	/**
	 * 
	 * 当前访问域下设置登录Cookie 设置防止伪造SESSIONID攻击
	 * 
	 * @param request
	 * @param response
	 */
	public void authSSOCookie( HttpServletRequest request, HttpServletResponse response, Token token ) {
		CookieHelper.authJSESSIONID(request, RandomUtil.getCharacterAndNumber(8));
		setSSOCookie(request, response, token);
	}

	/**
	 * 清除登录状态
	 * 
	 * @param request
	 * @param response
	 * @return boolean true 成功, false 失败
	 */
	public boolean loginClear( HttpServletRequest request, HttpServletResponse response ) {
		return logout(request, response, config.getCache());
	}
	
	/**
	 * <p>
	 * 重新登录 退出当前登录状态、重定向至登录页.
	 * </p>
	 * 
	 * @param request
	 * @param response
	 */
	public void login( HttpServletRequest request, HttpServletResponse response ) throws IOException {
		/* 清理当前登录 Cookie 重新登录 */
		loginClear(request, response);

		/* redirect login page */
		String loginUrl = config.getLoginUrl();
		if ( "".equals(loginUrl) ) {
			response.getWriter().write("sso.properties Must include: sso.login.url");
		} else {
			String retUrl = HttpUtil.getQueryString(request, config.getEncoding());
			logger.fine("loginAgain redirect pageUrl.." + retUrl);
			response.sendRedirect(HttpUtil.encodeRetURL(loginUrl, config.getParamReturl(), retUrl));
		}
	}
	
	/**
	 * SSO 退出登录
	 */
	public void logout( HttpServletRequest request, HttpServletResponse response ) throws IOException {
		/* delete cookie */
		logout(request, response, config.getCache());

		/* redirect logout page */
		String logoutUrl = config.getLogoutUrl();
		if ( "".equals(logoutUrl) ) {
			response.getWriter().write("sso.properties Must include: sso.logout.url");
		} else {
			response.sendRedirect(logoutUrl);
		}
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
	 * 生成跨域询问密文
	 * 
	 * @param request
	 * @param response
	 * @param privateKey
	 *            RSA 私钥（业务系统私钥，用于签名）
	 * @return
	 */
	public String askCiphertext( HttpServletRequest request, HttpServletResponse response, String privateKey ) {
		try {
			/**
			 * 跨域临时信任 Cookie
			 */
			AuthToken at = new AuthToken(request, privateKey);
			setAuthCookie(request, response, at);
			return config.getEncrypt().encrypt(at.jsonToken(), config.getSecretkey());
		} catch ( Exception e ) {
			logger.severe("askCiphertext AES encrypt error.");
			e.printStackTrace();
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
	 * @param tokenPk
	 *            RSA 公钥 (业务系统 AuthToken加密公钥)
	 * @param ssoPrk
	 *            RSA 私钥 (SSO私钥再次签名回复)
	 */
	public String replyCiphertext( HttpServletRequest request, String userId, String askTxt, String tokenPk,
			String ssoPrk ) {
		String at = null;
		try {
			at = config.getEncrypt().decrypt(askTxt, config.getSecretkey());
		} catch ( Exception e ) {
			logger.severe("replyCiphertext AES decrypt error.");
			e.printStackTrace();
		}
		if ( at != null ) {
			/**
			 * <p>
			 * 使用业务系统公钥验证签名
			 * </p>
			 * <p>
			 * 验证 IP 地址是否合法
			 * </p>
			 */
			AuthToken authToken = JSON.parseObject(at, AuthToken.class);
			if ( checkIp(request, authToken.verify(tokenPk)) != null ) {
				authToken.setUid(userId);
				try {
					/**
					 * <p>
					 * 使用 SSO 私钥签名
					 * </p>
					 * <p>
					 * 然后再对称加密
					 * </p>
					 */
					authToken.sign(ssoPrk);
					return config.getEncrypt().encrypt(authToken.jsonToken(), config.getSecretkey());
				} catch ( Exception e ) {
					logger.severe("replyCiphertext AES encrypt error.");
					e.printStackTrace();
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
	 * @param replyTxt
	 *            回复密文
	 * @param atPk
	 *            RSA 公钥 (业务系统公钥，验证authToken签名)
	 * @param ssoPrk
	 *            RSA 公钥 (SSO 回复密文公钥验证签名)
	 */
	public String ok( HttpServletRequest request, HttpServletResponse response, String replyTxt, String atPk,
			String ssoPrk ) {
		AuthToken token = getAuthToken(request, atPk);
		if ( token != null ) {
			String rt = null;
			try {
				rt = config.getEncrypt().decrypt(replyTxt, config.getSecretkey());
			} catch ( Exception e ) {
				logger.severe("kisso AES decrypt error.");
				e.printStackTrace();
			}
			if ( rt != null ) {
				AuthToken atk = JSON.parseObject(rt, AuthToken.class);
				if ( atk != null && atk.getUuid().equals(token.getUuid()) ) {
					if ( atk.verify(ssoPrk) != null ) {
						/**
						 * 删除跨域信任Cookie 返回 userId
						 */
						CookieHelper.clearCookieByName(request, response, config.getAuthCookieName(),
							config.getCookieDomain(), config.getCookiePath());
						return atk.getUid();
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 设置跨域信任 Cookie
	 * 
	 * @param authToken
	 *            跨域信任 Token
	 */
	private void setAuthCookie( HttpServletRequest request, HttpServletResponse response, AuthToken authToken ) {
		try {
			CookieHelper.addCookie(response, config.getCookieDomain(), config.getCookiePath(),
				config.getAuthCookieName(), encryptCookie(request, authToken, config.getEncrypt()),
				config.getAuthCookieMaxage(), true, config.getCookieSecure());
		} catch ( Exception e ) {
			logger.severe("AuthToken encryptCookie error.");
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>
	 * 获取跨域信任 AuthToken
	 * </p>
	 * <p>
	 * 验证存在并且 IP 地址正确，签名合法
	 * </p>
	 * 
	 * @param request
	 * @param publicKey
	 *            RSA 公钥（业务系统公钥验证签名合法）
	 * @return
	 */
	private AuthToken getAuthToken( HttpServletRequest request, String publicKey ) {
		String jsonToken = getJsonToken(request, config.getEncrypt(), config.getAuthCookieName());
		if ( jsonToken == null || "".equals(jsonToken) ) {
			logger.info("jsonToken is null.");
			return null;
		} else {
			/**
			 * 校验 IP 合法返回 AuthToken
			 */
			AuthToken at = JSON.parseObject(jsonToken, AuthToken.class);
			if ( checkIp(request, at) == null ) {
				return null;
			}
			return at;
		}
	}

}
