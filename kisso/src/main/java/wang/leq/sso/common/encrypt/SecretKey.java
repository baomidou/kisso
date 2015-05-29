/**
 * Copyright (c) 2011-2014, hubin (243194995@qq.com).
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
package wang.leq.sso.common.encrypt;

import wang.leq.sso.common.util.RandomUtil;


/**
 * sso 密钥生成类
 * <p>
 * @author   hubin
 * @date	 2015年5月29日 
 * @version  1.0.0
 */
public class SecretKey {

	/**
	 * 生成 18 位随机字符串密钥
	 * <p>
	 * 替换配置文件 sso.properties 属性 sso.secretkey=随机18位字符串
	 */
	public static String ssoSecretKey() {
		return RandomUtil.getCharacterAndNumber(18);
	}


	public static void main( String[] args ) {
		System.out.println(ssoSecretKey());
	}
}
