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

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.baomidou.kisso.Token;

/**
 * <p>
 * kisso 信任票据域
 * </p>
 * 
 * @author hubin
 * @Date 2016-03-07
 */
public class SSOAuthRealm extends AuthorizingRealm {

	private ShiroPermission shiroPermission;


	/**
	 * 根据用户身份获取授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo( PrincipalCollection principals ) {
		if ( principals == null ) {
			throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
		}
		Token token = (Token) getAvailablePrincipal(principals);
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		List<String> permissions = shiroPermission.getPermissions(token);
		if ( permissions != null ) {
			authorizationInfo.addStringPermissions(permissions);
		}
		return authorizationInfo;
	}


	@Override
	public boolean supports( AuthenticationToken token ) {
		return token != null && SSOAuthToken.class.isAssignableFrom(token.getClass());
	}


	@Override
	public Class<?> getAuthenticationTokenClass() {
		return SSOAuthToken.class;
	}


	/**
	 * 获取身份验证信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo( AuthenticationToken token ) throws AuthenticationException {
		return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), getName());
	}


	public ShiroPermission getShiroPermission() {
		return shiroPermission;
	}


	public void setShiroPermission( ShiroPermission shiroPermission ) {
		this.shiroPermission = shiroPermission;
	}

}
