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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.method.HandlerMethod;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.SSOToken;
import com.baomidou.kisso.annotation.Action;
import com.baomidou.kisso.annotation.Permission;
import com.baomidou.kisso.common.shiro.SSOAuthToken;

/**
 * <p>
 * kisso_shiro 权限拦截器（必须在  kisso 拦截器之后执行）
 * </p>
 * 
 * @author hubin
 * @Date 2016-03-07
 */
public class SSOShiroInterceptor extends SSOPermissionInterceptor {

	private static final Logger logger = Logger.getLogger("SSOShiroInterceptor");


	/**
	 * 用户权限验证
	 * <p>
	 * 方法拦截 Controller 处理之前进行调用。
	 * </p>
	 */
	public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler )
		throws Exception {
		if ( handler instanceof HandlerMethod ) {
			SSOToken token = SSOHelper.attrToken(request);
			if ( token == null ) {
				return true;
			}

			/**
			 * shiro 会话管理
			 */
			Subject currentUser = SecurityUtils.getSubject();
			Session session = currentUser.getSession(false);
			if ( session != null ) {
				session.touch();
			}

			/**
			 * shiro 登录认证
			 */
			if ( !currentUser.isAuthenticated() ) {
				currentUser.login(new SSOAuthToken(token));
				logger.fine(" shiro login success. ");
			}

			/**
			 * URL 权限认证
			 */
			if ( SSOConfig.getInstance().isPermissionUri() ) {
				String uri = request.getRequestURI();
				if ( uri == null || currentUser.isPermitted(uri) ) {
					return true;
				}
			}

			/**
			 * 注解权限认证
			 */
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			Permission pm = method.getAnnotation(Permission.class);
			if ( pm != null ) {
				if ( pm.action() == Action.Skip ) {
					/**
					 * 忽略拦截
					 */
					return true;
				} else if ( !"".equals(pm.value()) && currentUser.isPermitted(pm.value()) ) {
					/**
					 * 权限合法
					 */
					return true;
				}
			}

			/**
			 * 无权限访问
			 */
			return unauthorizedAccess(request, response);
		}

		return true;
	}

}
