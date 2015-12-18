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

import com.baomidou.kisso.common.IpHelper;

/**
 * <p>
 * SSO登录标记Cookie基本信息对象
 * </p>
 * 
 * @author hubin
 * @Date 2014-5-8
 */
public class SSOToken extends Token {

	/* 登录类型 */
	private int type;

	/* 登录时间 */
	private long time;
	
	public SSOToken() {

	}

	public SSOToken(HttpServletRequest request) {
		this.setIp(IpHelper.getIpAddr(request));
		this.type = 0;
		this.time = System.currentTimeMillis();
	}
	
	public int getType() {
		return type;
	}
	
	public void setType( int type ) {
		this.type = type;
	}
	
	public long getTime() {
		return time;
	}

	public void setTime( long time ) {
		this.time = time;
	}

}
