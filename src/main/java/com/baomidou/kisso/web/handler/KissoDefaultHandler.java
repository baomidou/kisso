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
package com.baomidou.kisso.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.kisso.common.util.HttpUtil;

/**
 * <p>
 * SSO 默认拦截处理器，自定义 Handler 可继承该类。
 * </p>
 * 
 * @author hubin
 * @Date 2015-12-19
 */
public class KissoDefaultHandler implements SSOHandlerInterceptor {
	private static KissoDefaultHandler handler;

	/**
	 * new 当前对象
	 */
	public static KissoDefaultHandler getInstance() {
		if (handler == null) {
			handler = new KissoDefaultHandler();
		}
		return handler;
	}

	/**
	 * 未登录时，处理 AJAX 请求。
	 * <p>
	 * 返回 HTTP 状态码 401（未授权） 请求要求身份验证。 对于需要登录的网页，服务器可能返回此响应。
	 * </p>
	 */
	public boolean preTokenIsNullAjax(HttpServletRequest request, HttpServletResponse response) {
		HttpUtil.ajaxStatus(response, 401, "prompt login exception, please login again.");
		return false;
	}

	public boolean preTokenIsNull(HttpServletRequest request, HttpServletResponse response) {
		/* 预留子类处理 */
		return true;
	}

}
