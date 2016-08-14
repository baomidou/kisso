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

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.kisso.common.IpHelper;

/**
 * <p>
 * SSO登录标记Cookie基本信息对象
 * </p>
 * 
 * @author hubin
 * @Date 2014-5-8
 */
@SuppressWarnings("serial")
public class SSOToken extends Token {

	/* 登录类型 */
	private Integer type;

	/* 预留 */
	private String data;

	/**
	 * <p>
	 * 预留对象，默认 fastjson 不参与序列化（也就是不存放在 cookie 中 ）
	 * </p>
	 * <p>
	 * 此处配合分布式缓存使用，可以存放用户常用基本信息
	 * </p>
	 */
	@JSONField(serialize = false)
	private Object object;

	public SSOToken() {
		this.setApp(SSOConfig.getInstance().getRole());
	}

	public SSOToken(HttpServletRequest request) {
		this.setIp(IpHelper.getIpAddr(request));
		this.setApp(SSOConfig.getInstance().getRole());
	}

	public SSOToken(HttpServletRequest request, String uid) {
		this(request);
		this.setUid(uid);
	}

	public SSOToken(HttpServletRequest request, String uid, Integer type) {
		this(request, uid);
		this.setType(type);
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	/**
	 * 缓存用户信息，自动类型转换
	 */
	@SuppressWarnings("unchecked")
	public <T> T getCacheObject() {
		return (T) this.getObject();
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
