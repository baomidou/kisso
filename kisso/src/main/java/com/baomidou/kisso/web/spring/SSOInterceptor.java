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
package com.baomidou.kisso.web.spring;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baomidou.kisso.SSOConstant;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.Token;
import com.baomidou.kisso.annotation.Action;
import com.baomidou.kisso.annotation.Login;

/**
 * 登录权限验证
 * <p>
 * spring 拦截器，Controller 方法调用前处理。
 * </p>
 * 
 * @author hubin
 * @Date 2015-11-10
 */
public class SSOInterceptor extends HandlerInterceptorAdapter {
	private final static Logger logger = LoggerFactory.getLogger(SSOInterceptor.class);

	/**
	 * 登录权限验证
	 * <p>
	 * 方法拦截 Controller 处理之前进行调用。
	 * </p>
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		/**
		 * 处理 Controller 方法
		 * <p>
		 * 判断 handler 是否为 HandlerMethod 实例
		 * </p>
		 */
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			Login login = method.getAnnotation(Login.class);
			if (login != null) {
				if (login.action() == Action.Skip) {
					/**
					 * 忽略拦截
					 */
					return true;
				} else {
					/**
					 * 正常执行
					 */
					Token token = SSOHelper.getToken(request);
					if (token == null) {
						/**
						 * 重新登录
						 */
						logger.debug("logout. request url:{}", request.getRequestURL());
						SSOHelper.login(request, response);
						return false;
					} else {
						request.setAttribute(SSOConstant.SSO_TOKEN_ATTR, token);
					}
				}
			}
		}

		/**
		 * 通过拦截
		 */
		return true;
	}

}
