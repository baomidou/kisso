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

import com.baomidou.kisso.AuthToken;
import com.baomidou.kisso.Token;

/**
 * <p>
 * SSO 单点登录服务
 * </p>
 * 
 * @author hubin
 * @Date 2015-12-03
 */
public interface KissoService {

	/**
	 * 获取登录 Token
	 * 
	 * <p>
	 * 登录
	 * </p>
	 */
	Token getToken(HttpServletRequest request);

	/**
	 * 在线人数（总数）
	 */
	String getLoginCount(HttpServletRequest request);

	/**
	 * <p>
	 * 踢出 指定用户 ID 的登录用户，退出当前系统。
	 * </p>
	 * 
	 * @param userId
	 * 				用户ID
	 * @return
	 */
	boolean kickLogin(Object userId);

	/**
	 * 设置登录 Cookie
	 * 
	 * <p>
	 * 登录
	 * </p>
	 */
	void setSSOCookie(HttpServletRequest request, HttpServletResponse response, Token token);

	/**
	 * 清理登录状态
	 * 
	 * <p>
	 * 退出
	 * </p>
	 */
	boolean clearLogin(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 退出并跳至登录页
	 * 
	 * <p>
	 * 退出
	 * </p>
	 */
	void clearRedirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException;

	/**
	 * 生成跨域询问票据
	 * 
	 * <p>
	 * 跨域
	 * </p>
	 */
	AuthToken askCiphertext(HttpServletRequest request, HttpServletResponse response, String privateKey);

	/**
	 * 生成跨域回复票据
	 * 
	 * <p>
	 * 跨域
	 * </p>
	 */
	AuthToken replyCiphertext(HttpServletRequest request, String askData);

	/**
	 * 验证跨域回复密文，成功!
	 * <p>
	 * 返回 绑定用户ID 等信息
	 * </p>
	 * 
	 * <p>
	 * 跨域
	 * </p>
	 */
	AuthToken ok(HttpServletRequest request, HttpServletResponse response, String replyData, String atPk, String ssoPrk);
}
