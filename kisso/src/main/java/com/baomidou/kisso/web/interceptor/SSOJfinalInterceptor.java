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

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.Token;
import com.baomidou.kisso.web.handler.KissoDefaultHandler;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * 登录权限验证
 * <p>
 * kisso jfinal 拦截器，Controller 方法调用前处理。
 * </p>
 * 
 * @author hubin
 * @Date 2015-12-23
 */
public class SSOJfinalInterceptor implements Interceptor {
	private static final Logger logger = Logger.getLogger("SSOJfinalInterceptor");

	public void intercept( Invocation inv ) {
		/**
		 * 正常执行
		 */
		HttpServletRequest request = inv.getController().getRequest();
		HttpServletResponse response = inv.getController().getResponse();
		Token token = SSOHelper.getToken(request);
		if (token == null) {
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				/*
				 * Handler 处理 AJAX 请求
				 */
				KissoDefaultHandler.getInstance().preTokenIsNullAjax(request, response);
			} else {
				try {
					logger.fine("logout. request url:" + request.getRequestURL());
					SSOHelper.clearRedirectLogin(request, response);
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		} else {
			/*
			 * 正常请求，request 设置 token 减少二次解密
			 */
			request.setAttribute(SSOConfig.SSO_TOKEN_ATTR, token);
			inv.invoke();
		}
	}

}
