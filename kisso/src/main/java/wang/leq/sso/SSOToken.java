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
package wang.leq.sso;

import javax.servlet.http.HttpServletRequest;

import wang.leq.sso.common.IpHelper;

/**
 * SSO登录标记Cookie基本信息对象 
 * <p>
 * @author   hubin
 * @Date	 2014-5-8 	 
 */
public class SSOToken extends Token {
	private final static String DEFAULT_VALUE = "0";//默认参数值
	private String appId;//应用系统 ID
	private String userId;//用户 ID
	private String loginType;//登录类型
	private long loginTime;//登录时间

	public SSOToken() {

	}

	public SSOToken(HttpServletRequest request) {
		this.appId = DEFAULT_VALUE;
		setUserIp(IpHelper.getIpAddr(request));
		this.loginType = DEFAULT_VALUE;
		this.loginTime = System.currentTimeMillis();
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

}
