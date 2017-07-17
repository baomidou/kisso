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

/**
 * <p>
 * SSO Token 解析接口
 * </p>
 * 
 * @author hubin
 * @Date 2014-5-9
 */
public interface SSOParser {

	/**
	 * Object 对象转换为 json 字符串
	 * 
	 * @param obj
	 *            转换对象
	 * @return
	 */
	String toJson(Object obj);

	/**
	 * json 格式字符串转换为 T 对象
	 * 
	 * @param <T>
	 * 
	 * @param <T>
	 * @param text
	 *            json 字符串
	 * @param clazz
	 *            Object 对象类
	 * @return
	 */
	<T> T parseObject(String text, Class<? extends T> clazz);
}
