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
 * @Date 2016-01-21
 */
public class SSOHelper {

	protected static final Logger logger = Logger.getLogger("SSOHelper");
	protected static ConfigurableAbstractKissoService kissService = null;

	/**
	 * Kisso 服务初始化
	 */
	public static ConfigurableAbstractKissoService getKissoService() {
		if ( kissService == null ) {
			kissService = new ConfigurableAbstractKissoService();
		}
		return kissService;
	}

	/**
	 * 生成 18 位随机字符串密钥
	 * <p>
	 * 替换配置文件 sso.properties 属性 sso.secretkey=随机18位字符串
	 * </p>
	 */
	public static String getSecretKey() {
		return RandomUtil.getCharacterAndNumber(18);
	}

	/**
	 * 
	 * 当前登录人总数（合计多少个）
	 * 
	 * @param request
	 * @return
	 */
	public static String getLoginCount( HttpServletRequest request ) {
		return getKissoService().getLoginCount(request);
	}


	/**
	 * ------------------------------- 登录相关方法 -------------------------------
	 */
	/**
	 * 
	 * 设置加密 Cookie（登录验证成功） 
	 * 
	 * <p>
	 * 最后一个参数 true 销毁当前JSESSIONID. 创建可信的 JSESSIONID 防止伪造 SESSIONID 攻击
	 * </p>
	 * <p>
	 * 最后一个参数 false 只设置 cookie 
	 * </p>
	 * 
	 * <p>
	 * request.setAttribute(SSOConfig.SSO_COOKIE_MAXAGE, maxAge);<br>
	 * 可以动态设置 Cookie maxAge 超时时间 ，优先于配置文件的设置，无该参数 - 默认读取配置文件数据 。<br>
	 * maxAge 定义：-1 浏览器关闭时自动删除 0 立即删除 120 表示Cookie有效期2分钟(以秒为单位)
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * 					SSO 票据
	 * @param invalidate
	 * 					销毁当前 JSESSIONID
	 */
	public static void setSSOCookie( HttpServletRequest request, HttpServletResponse response, Token token, boolean invalidate ) {
		if(invalidate){
			getKissoService().authSSOCookie(request, response, token);
		} else {
			getKissoService().setSSOCookie(request, response, token);
		}
	}

	
	/**
	 * ------------------------------- 客户端相关方法 -------------------------------
	 */
	/**
	 * 
	 * 获取当前请求 token
	 * 
	 * <p>
	 * 该方法直接从 cookie 中解密获取 token, 常使用在登录系统及拦截器中使用 getToken(request)
	 * </p>
	 * <p>
	 * 如果该请求在登录拦截器之后请使用 attrToken(request) 防止二次解密
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Token> T getToken( HttpServletRequest request ) {
		return (T) getKissoService().getToken(request);
	}
	

	/**
	 * 
	 * 从请求中获取 token 通过登录拦截器之后使用
	 * 
	 * <p>
	 * 该数据为登录拦截器放入 request 中，防止二次解密
	 * </p>
	 * 
	 * @param request
	 * 				访问请求
	 * @return
	 */
	public static <T extends Token> T attrToken( HttpServletRequest request ) {
		return getKissoService().attrToken(request);
	}

	/**
	 * 
	 * 退出登录， 并且跳至 sso.properties 配置的属性 sso.logout.url 地址
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void logout( HttpServletRequest request, HttpServletResponse response ) throws IOException {
		getKissoService().logout(request, response);
	}

	/**
	 * 
	 * 清理当前登录状态
	 * 
	 * <p>
	 * 清理 Cookie、缓存、统计、等数据
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static boolean clearLogin( HttpServletRequest request, HttpServletResponse response ) {
		return getKissoService().clearLogin(request, response);
	}

	/**
	 * 
	 * <p>
	 * 退出重定向登录页，跳至 sso.properties 配置的属性 sso.login.url 地址
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void clearRedirectLogin( HttpServletRequest request, HttpServletResponse response ) throws IOException {
		getKissoService().clearRedirectLogin(request, response);
	}

	/**
	 * 
	 * <p>
	 * 获取 token 的缓存主键
	 * </p>
	 * 
	 * @param request
	 * 				当前请求
	 * @return
	 */
	public static String getTokenCacheKey( HttpServletRequest request ) {
		return getToken(request).toCacheKey();
	}
	
	/**
	 * 
	 * <p>
	 * 获取 token 的缓存主键
	 * </p>
	 * 
	 * @param userId
	 * 				用户ID
	 * @return
	 */
	public static String getTokenCacheKey( Object userId ) {
		return SSOConfig.toCacheKey(userId);
	}

	/**
	 * <p>
	 * 踢出 指定用户 ID 的登录用户，退出当前系统。
	 * </p>
	 * 
	 * @param userId
	 * 				用户ID
	 * @return
	 */
	public static boolean kickLogin( Object userId ) {
		return getKissoService().kickLogin(userId);
	}

	/**
	 * ------------------------------- 跨域相关方法 -------------------------------
	 */
	/**
	 * <p>
	 * 生成跨域询问票据
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @param privateKey
	 *            RSA 私钥（业务系统私钥，用于签名）
	 * @return AuthToken {@link AuthToken}
	 */
	public static AuthToken askCiphertext( HttpServletRequest request, HttpServletResponse response, String privateKey ) {
		return getKissoService().askCiphertext(request, response, privateKey);
	}

	/**
	 * <p>
	 * 生成跨域回复票据
	 * </p>
	 * 
	 * @param authToken
	 *            跨域信任 Token
	 * @param userId
	 *            用户ID
	 * @param askTxt
	 *            询问密文
	 * @return AuthToken {@link AuthToken}
	 */
	public static AuthToken replyCiphertext( HttpServletRequest request, String askData) {
		return getKissoService().replyCiphertext(request, askData);
	}


	/**
	 * <p>
	 * 验证回复密文，成功! 返回 绑定用户ID 等信息
	 * </p>
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
	 * @return AuthToken {@link AuthToken}
	 */
	public static AuthToken ok( HttpServletRequest request, HttpServletResponse response, String replyData, String atPk,
			String ssoPrk ) {
		return getKissoService().ok(request, response, replyData, atPk, ssoPrk);
	}

}
