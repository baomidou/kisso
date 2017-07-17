/**
 * Copyright (c) 2011-2014, hubin (jobob@qq.com).
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
package com.baomidou.kisso.annotation;

/**
 * <p>
 * SSO 执行状态
 * </p>
 * 
 * @author hubin
 * @Date 2015-11-10
 */
public enum Action {
	/** 正常（默认） */
	Normal("0", "执行权限验证"),
	
	/** 跳过 */
	Skip("1", "跳过权限验证");

	/** 主键 */
	private final String key;

	/** 描述 */
	private final String desc;

	Action(final String key, final String desc) {
		this.key = key;
		this.desc = desc;
	}

	public String getKey() {
		return this.key;
	}

	public String getDesc() {
		return this.desc;
	}

}
