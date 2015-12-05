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

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.kisso.common.util.RandomUtil;
import com.baomidou.kisso.service.ConfigurableAbstractKissoService;

/**
 * <p>
 * SSO 帮助类
 * </p>
 * 
 * @author hubin
 * @Date 2015-12-05
 */
public class SSOHelper {

	protected static final Logger logger = Logger.getLogger("SSOHelper");
	protected static ConfigurableAbstractKissoService kissService = null;

	/**
	 * 创建 Kisso 服务
	 */
	protected static ConfigurableAbstractKissoService getKissoService() {
		if ( kissService == null ) {
			kissService = new ConfigurableAbstractKissoService();
		}
		return kissService;
	}

	/**
	 * 生成 18 位随机字符串密钥
	 * <p>
	 * 替换配置文件 sso.properties 属性 sso.secretkey=随机18位字符串
	 */
	public static String getSecretKey() {
		return RandomUtil.getCharacterAndNumber(18);
	}

	public String getLoginCount( HttpServletRequest request ) {
		return getKissoService().getLoginCount(request);
	}


	/**
	 * ------------------------------- 登录相关方法
	 */
	public static void setSSOCookie( HttpServletRequest request, HttpServletResponse response, Token token ) {
		getKissoService().setSSOCookie(request, response, token);
	}

	public static void authSSOCookie( HttpServletRequest request, HttpServletResponse response, Token token ) {
		getKissoService().authSSOCookie(request, response, token);
	}


	/**
	 * ------------------------------- 客户端相关方法
	 */
	public static Token getToken( HttpServletRequest request ) {
		return getKissoService().getToken(request);
	}

	public static void logout( HttpServletRequest request, HttpServletResponse response ) throws IOException {
		getKissoService().logout(request, response);
	}

	public static boolean loginClear( HttpServletRequest request, HttpServletResponse response ) {
		return getKissoService().loginClear(request, response);
	}

	public static void login( HttpServletRequest request, HttpServletResponse response ) throws IOException {
		getKissoService().login(request, response);
	}

	public static String hashCookie( HttpServletRequest request ) {
		return getKissoService().hashCookie(request);
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
	public static Object attrToken( HttpServletRequest request ) {
		return request.getAttribute(SSOConfig.SSO_TOKEN_ATTR);
	}


	/**
	 * ------------------------------- 跨域相关方法
	 */
	public static String askCiphertext( HttpServletRequest request, HttpServletResponse response, String privateKey ) {
		return getKissoService().askCiphertext(request, response, privateKey);
	}

	public static String replyCiphertext( HttpServletRequest request, String userId, String askTxt, String tokenPk,
			String ssoPrk ) {
		return getKissoService().replyCiphertext(request, userId, askTxt, tokenPk, ssoPrk);
	}


	public static String ok( HttpServletRequest request, HttpServletResponse response, String replyTxt, String atPk,
			String ssoPrk ) {
		return getKissoService().ok(request, response, replyTxt, atPk, ssoPrk);
	}

}
