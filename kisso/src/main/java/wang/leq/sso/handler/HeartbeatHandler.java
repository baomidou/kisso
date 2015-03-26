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
package wang.leq.sso.handler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wang.leq.sso.SSOConfig;
import wang.leq.sso.common.CookieHelper;
import wang.leq.sso.common.util.RandomUtil;


/**
 * 心跳控制器
 * <p>
 * @author   hubin
 * @date	 2015年3月26日 
 * @version  1.0.0
 */
public class HeartbeatHandler extends SSOHandler {

	private final static String HEARTBEAT_COOKIENAME = "hid";

	protected HttpServletRequest request;

	protected HttpServletResponse response;


	public HeartbeatHandler( HttpServletRequest request, HttpServletResponse response ) {
		this.request = request;
		this.response = response;
	}


	@Override
	public boolean forceOut() {
		/**
		 * 心跳cookie存在不判断缓存心跳状态
		 * 减少缓存访问次数
		 */
		Cookie bc = CookieHelper.findCookieByName(request, HEARTBEAT_COOKIENAME);
		if ( bc != null ) {
			return true;
		} else {
			/**
			 * cookie 比心跳提前
			 * 2分钟失效
			 */
			int maxAge = 120;
			CookieHelper.addCookie(response, SSOConfig.getCookieDomain(), SSOConfig.getCookiePath(),
				HEARTBEAT_COOKIENAME, RandomUtil.getCharacterAndNumber(10), maxAge, true, false);
			return true;
		}
	}
}
