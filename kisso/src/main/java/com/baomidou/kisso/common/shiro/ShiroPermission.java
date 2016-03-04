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

import com.baomidou.kisso.Token;

/**
 * <p>
 * shiro 权限接口类
 * </p>
 * 
 * @author hubin
 * @Date 2016-03-07
 */
public interface ShiroPermission {

	/**
	 * <p>
	 * 字符串类型权限列表
	 * <p>
	 * @param token
	 *			kisso Token 票据
	 * @return
	 */
	public List<String> getPermissions( Token token );
}
