/**
 * Copyright (c) 2011-2014, hubin (243194995@qq.com).
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
package wang.leq.sso;

import wang.leq.sso.common.util.DateUtil;

/**
 * 日期工具类测试
 * <p>
 * @author   Administrator
 * @Date	 2014-5-12 	 
 */
public class TestDateUtil {

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		long c = System.currentTimeMillis();
		System.out.println(c);
		System.out.println(DateUtil.formatDate(DateUtil.formatDate(c), "yyyyMMddHHmmss"));
	}
}
