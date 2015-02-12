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
package wang.leq.sso.common.encrypt;

/**
 * SSO 对称加密父类
 * <p>
 * @author   hubin
 * @Date	 2014-5-9 	 
 */
public abstract class Encrypt {

	/**
	 * 字符串内容加密
	 * <p>
	 * @param value 加密内容
	 * @param key 密钥
	 * @return
	 * @throws Exception
	 */
	public abstract String encrypt(String value, String key) throws Exception;

	/**
	 * 字符串内容解密
	 * <p>
	 * @param value 解密内容
	 * @param key 密钥
	 * @return
	 * @throws Exception
	 */
	public abstract String decrypt(String value, String key) throws Exception;
}
