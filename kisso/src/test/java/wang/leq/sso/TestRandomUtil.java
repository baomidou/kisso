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

import wang.leq.sso.common.util.RandomUtil;

/**
 * 测网随机数工具类
 * <p>
 * @author   Administrator
 * @Date	 2014-5-12 	 
 */
public class TestRandomUtil {

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		System.out.println("====6位随机数====" + RandomUtil.getCharacterAndNumber(6));
	}
}
