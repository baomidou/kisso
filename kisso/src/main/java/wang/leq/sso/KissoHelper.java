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
package wang.leq.sso;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.leq.sso.common.Browser;
import wang.leq.sso.common.CookieHelper;
import wang.leq.sso.common.IpHelper;
import wang.leq.sso.common.encrypt.Encrypt;
import wang.leq.sso.common.util.RandomUtil;
import wang.leq.sso.exception.KissoException;

/**
 * Kisso 帮助类
 * <p>
 * @author   hubin
 * @Date	 2014-06-27 	 
 */
public class KissoHelper {
	private final static Logger logger = LoggerFactory.getLogger(KissoHelper.class);

	/**
	 * @Description 加密Token信息
	 * @param request
	 * @param token
	 * 				SSO 登录信息票据
	 * @param encrypt
	 * 				对称加密算法类
	 * @return	Cookie 登录信息Cookie 
	 */
	public static String encryptCookie(HttpServletRequest request, Token token,
			Encrypt encrypt) throws Exception {
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
		 * 判断是否认证浏览器信息
		 * 否取8位随机数混淆
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
	 * @param request
	 * @param token
	 * 				登录票据
	 * @return Token
	 */
	public static Token checkIp(HttpServletRequest request, Token token) {
		/**
		 * 判断是否检查 IP 一致性
		 */
		if (SSOConfig.getCookieCheckip()) {
			String ip = IpHelper.getIpAddr(request);
			if (token != null && ip != null && !ip.equals(token.getUserIp())) {
				/**
				 * 检查 IP 与登录IP 不一致返回 null
				 */
				logger.info("ip inconsistent! return token null, token userIp:{}, reqIp:{}",
						new Object[] { token.getUserIp(), ip });
				return null;
			}
		}
		return token;
	}

	/**
	 * 获取当前请求 JsonToken
	 * <p>
	 * @param request
	 * @param encrypt
	 * 				对称加密算法类
	 * @param cookieName
	 * 				Cookie名称
	 * @return String 当前Token的json格式值
	 */
	public static String getJsonToken(HttpServletRequest request, Encrypt encrypt, String cookieName) {
		Cookie uid = CookieHelper.findCookieByName(request, cookieName);
		if (uid != null) {
			String jsonToken = uid.getValue();
			String[] tokenAttr = new String[2];
			try {
				jsonToken = encrypt.decrypt(jsonToken, SSOConfig.getSecretKey());
				tokenAttr = jsonToken.split(SSOConstant.CUT_SYMBOL);
			} catch (Exception e) {
				logger.info("jsonToken decrypt error.");
				e.printStackTrace();
			}
			/**
			 * 判断是否认证浏览器
			 * 混淆信息
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
				 * 不需要认证浏览器信息混淆
				 * 返回JsonToken
				 */
				return tokenAttr[0];
			}
		}

		return null;
	}

}
