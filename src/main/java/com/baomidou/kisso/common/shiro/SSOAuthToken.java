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
package com.baomidou.kisso.common.shiro;

import org.apache.shiro.authc.AuthenticationToken;

import com.baomidou.kisso.Token;

/**
 * <p>
 * kisso 票据凭证
 * </p>
 * 
 * @author hubin
 * @Date 2016-03-07
 */
public class SSOAuthToken implements AuthenticationToken {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Kisso 访问票据
	 * </p>
	 * 
	 * {@link Token}
	 */
	private final Token token;


	public SSOAuthToken( Token token ) {
		this.token = token;
	}


	/**
	 * 实现父类，获取身份方法
	 */
	public Object getPrincipal() {
		return token;
	}


	/**
	 * 实现父类，获取凭证方法
	 */
	public Object getCredentials() {
		return token;
	}

}
