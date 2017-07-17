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
package com.baomidou.kisso.common.parser;

import com.alibaba.fastjson.JSON;

/**
 * <p>
 * SSO Token 解析接口
 * </p>
 * 
 * @author hubin
 * @Date 2014-5-9
 */
public class FastJsonParser implements SSOParser {

	public String toJson(Object token) {
		return JSON.toJSONString(token);
	}

	public <T> T parseObject(String text, Class<? extends T> clazz) {
		return JSON.parseObject(text, clazz);
	}
	
}
