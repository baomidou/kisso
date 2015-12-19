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

/**
 * <p>
 * SSO Handler 拦截器接口
 * </p>
 * 
 * @author hubin
 * @Date 2015-12-19
 */
public interface SSOHandlerInterceptor {

	/**
	 * 
	 * token 为空未登录, 拦截到 AJAX 方法时
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	boolean preTokenIsNullAjax(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 
	 * token 为空未登录, 自定义处理逻辑
	 * <p>
	 * 返回 true 继续执行（清理登录状态，重定向至登录界面），false 停止执行
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @return true 继续执行，false 停止执行
	 */
	boolean preTokenIsNull(HttpServletRequest request, HttpServletResponse response);

}
