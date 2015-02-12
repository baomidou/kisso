/**
 * Copyright (c) 2011-2014, hubin (243194995@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wang.leq.sso.common;

import javax.servlet.http.HttpServletRequest;

import nl.bitwalker.useragentutils.UserAgent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.leq.sso.common.encrypt.MD5;

/**
 * 验证浏览器基本信息
 * <p>
 * @author   hubin
 * @Date	 2014-5-8
 */
public class Browser {
	private final static Logger logger = LoggerFactory.getLogger(Browser.class);

	/**
	 * @Description 获取浏览器客户端信息签名值
	 * @param request
	 * @return
	 */
	public static String getUserAgent(HttpServletRequest request, String value) {
		StringBuffer sf = new StringBuffer();
		sf.append(value);
		sf.append("-");
		/**
		 * 混淆浏览器版本信息
		 */
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
		sf.append(userAgent.getBrowser());
		sf.append("-");
		sf.append(userAgent.getBrowserVersion());

		/**
		 * MD5 浏览器版本信息
		 */
		return MD5.toMD5(sf.toString());
	}

	/**
	 * @Description 请求浏览器是否合法
	 * 				(只校验客户端信息不校验domain)
	 * @param request
	 * @param userAgent
	 * 				浏览器客户端信息
	 * @return
	 */
	public static boolean isLegalUserAgent(HttpServletRequest request, String value, String userAgent) {
		String rlt = getUserAgent(request, value);
		logger.debug("Browser getUserAgent:{}", rlt);

		if (rlt.equalsIgnoreCase(userAgent)) {
			logger.debug("Browser isLegalUserAgent is legal.");
			return true;
		}

		logger.debug("Browser isLegalUserAgent is illegal.");
		return false;
	}
}
