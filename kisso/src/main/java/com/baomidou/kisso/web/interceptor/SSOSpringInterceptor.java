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
package com.baomidou.kisso.web.interceptor;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.Token;
import com.baomidou.kisso.annotation.Action;
import com.baomidou.kisso.annotation.Login;
import com.baomidou.kisso.common.util.HttpUtil;
import com.baomidou.kisso.web.handler.KissoDefaultHandler;
import com.baomidou.kisso.web.handler.SSOHandlerInterceptor;

/**
 * 登录权限验证
 * <p>
 * kisso spring 拦截器，Controller 方法调用前处理。
 * </p>
 * 
 * @author hubin
 * @Date 2015-11-10
 */
public class SSOSpringInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger.getLogger("SSOInterceptor");
	private SSOHandlerInterceptor handlerInterceptor;

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
				}
			}

			/**
			 * 正常执行
			 */
			Token token = SSOHelper.getToken(request);
			if (token == null) {
				if ( HttpUtil.isAjax(request) ) {
					/*
					 * Handler 处理 AJAX 请求
					 */
					this.getHandlerInterceptor().preTokenIsNullAjax(request, response);
					return false;
				} else {
					/*
					 * token 为空，调用 Handler 处理
					 * 返回 true 继续执行，清理登录状态并重定向至登录界面
					 */
					if (this.getHandlerInterceptor().preTokenIsNull(request, response)) {
						logger.fine("logout. request url:" + request.getRequestURL());
						SSOHelper.clearRedirectLogin(request, response);
					}
					return false;
				}
			} else {
				/*
				 * 正常请求，request 设置 token 减少二次解密
				 */
				request.setAttribute(SSOConfig.SSO_TOKEN_ATTR, token);
			}
		}

		/**
		 * 通过拦截
		 */
		return true;
	}

	public SSOHandlerInterceptor getHandlerInterceptor() {
		if (handlerInterceptor == null) {
			return KissoDefaultHandler.getInstance();
		}
		return handlerInterceptor;
	}

	public void setHandlerInterceptor(SSOHandlerInterceptor handlerInterceptor) {
		this.handlerInterceptor = handlerInterceptor;
	}

}
