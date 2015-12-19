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
package com.baomidou.kisso.common;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.kisso.common.encrypt.MD5;

/**
 * <p>
 * 验证浏览器基本信息
 * </p>
 * 
 * @author hubin
 * @Date 2014-5-8
 */
public class Browser {

	private static final Logger logger = Logger.getLogger("Browser");

	/**
	 * 混淆浏览器版本信息
	 * 
	 * @Description 获取浏览器客户端信息签名值
	 * @param request
	 * @return
	 */
	public static String getUserAgent(HttpServletRequest request, String value) {
		StringBuffer sf = new StringBuffer();
		sf.append(value);
		sf.append("-");
		sf.append(request.getHeader("user-agent"));

		/**
		 * MD5 浏览器版本信息
		 */
		logger.fine("Browser info:" + sf.toString());
		return MD5.toMD5(sf.toString());
	}

	/**
	 * <p>
	 * 请求浏览器是否合法 (只校验客户端信息不校验domain)
	 * </p>
	 * @param request
	 * @param userAgent
	 *            浏览器客户端信息
	 * @return
	 */
	public static boolean isLegalUserAgent(HttpServletRequest request, String value, String userAgent) {
		String rlt = getUserAgent(request, value);

		if (rlt.equalsIgnoreCase(userAgent)) {
			logger.fine("Browser isLegalUserAgent is legal. Browser getUserAgent:" + rlt);
			return true;
		}

		logger.fine("Browser isLegalUserAgent is illegal. Browser getUserAgent:" + rlt);
		return false;
	}
	
}
