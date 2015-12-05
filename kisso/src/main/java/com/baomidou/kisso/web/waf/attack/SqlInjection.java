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
package com.baomidou.kisso.web.waf.attack;

/**
 * SQL注入攻击
 * <p>
 * @author   hubin
 * @Date	 2014-5-8 	 
 */
public class SqlInjection implements Istrip {

	/**
	 * @Description SQL注入内容剥离
	 * @param value
	 * 				待处理内容
	 * @return
	 */
	public String strip(String value) {

		//剥离SQL注入部分代码
		return value.replaceAll("('.+--)|(--)|(\\|)|(%7C)", "");
	}
}
