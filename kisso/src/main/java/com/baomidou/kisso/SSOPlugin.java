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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * SSO 插件接口
 * </p>
 * 
 * @author hubin
 * @Date 2016-02-24
 */
public interface SSOPlugin {

	/**
	 * <p>
	 * 登录时调用该方法
	 * </p>
	 */
	boolean login(HttpServletRequest request, HttpServletResponse response);

	/**
	 * <p>
	 * 退出时调用该方法
	 * </p>
	 */
	boolean logout(HttpServletRequest request, HttpServletResponse response);

}
