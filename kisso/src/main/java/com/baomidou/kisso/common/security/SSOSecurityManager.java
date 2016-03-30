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
package com.baomidou.kisso.common.security;

import java.util.Collection;

/**
 * <p>
 * SSO 安全授权管理
 * </p>
 * 
 * @author hubin
 * @Date 2016-03-30
 */
public class SSOSecurityManager {

	private SSOAuthorization authorization;


	protected boolean isPermitted( String permission ) {
		Collection<String> perms = authorization.getStringPermissions();
		if ( perms != null && !perms.isEmpty() ) {
			for ( String perm : perms ) {
				if ( perm.equals(permission) ) {
					return true;
				}
			}
		}
		return false;
	}


	public SSOAuthorization getAuthorization() {
		return authorization;
	}


	public void setAuthorization( SSOAuthorization authorization ) {
		this.authorization = authorization;
	}


}
