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
package com.baomidou.kisso;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * SSO 在线人数统计接口
 * </p>
 * 
 * @author hubin
 * @Date 2015-11-17
 */
public interface SSOStatistic {

	/**
	 * 在线人数 +1
	 * 
	 * @param request
	 *            登录请求
	 * @return boolean
	 */
	boolean increase(HttpServletRequest request);

	/**
	 * 在线人数 -1
	 * 
	 * @param request
	 *            退出请求
	 * @return boolean
	 */
	boolean decrease(HttpServletRequest request);

	/**
	 * 在线人数（总数）
	 * 
	 * @param request
	 *            查询请求
	 * @return String
	 */
	String count(HttpServletRequest request);

}
