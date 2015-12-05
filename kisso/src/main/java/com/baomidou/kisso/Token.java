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

/**
 * <p>
 * SSO 票据顶级父类
 * </p>
 * 
 * @author hubin
 * @Date 2015-11-30
 */
public class Token {
	/* 正常 */
	public final static int FLAG_NORMAL = 0;

	/* 缓存宕机 */
	public final static int FLAG_CACHE_SHUT = 1;

	/**
	 * 登录 IP
	 */
	private String ip;
	/**
	 * Token 状态标示
	 * <p>
	 * 默认正常执行
	 * </p>
	 */
	private int flag = FLAG_NORMAL;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	/**
	 * Token转为JSON格式
	 * <p>
	 * 
	 * @return JSON格式Token值
	 */
	public String jsonToken() {
		return SSOConfig.newInstance().getParser().toJson(this);
	}

	/**
	 * JSON格式Token值转为Token对象
	 * <p>
	 * 
	 * @param jsonToken
	 *            JSON格式Token值
	 * @return Token对象
	 */
	public Token parseToken(String jsonToken) {
		return SSOConfig.newInstance().getParser().parseToken(jsonToken, this.getClass());
	}
	
}
