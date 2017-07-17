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
package com.baomidou.kisso.common.parser.api;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.exception.AESException;

/**
 * <p>
 * 提供提取消息格式中的密文及生成回复消息格式
 * </p>
 * 
 * @author hubin
 * @Date 2015-01-09
 */
public class JSONParser {

	/**
	 * 
	 * <p>
	 * 提取出 JSON 数据包中的加密消息
	 * </p>
	 * 
	 * @param jsontext
	 * 				待提取的JSON字符串
	 * @return 提取出的加密消息字符串
	 * @throws AESException {@link AESException}
	 */
	public static EncryptMsg extract( String jsontext ) throws AESException {
		try {
			SSOConfig config = SSOConfig.getInstance();
			return config.getParser().parseObject(jsontext, EncryptMsg.class);
		} catch (Exception e) {
			throw new AESException(AESException.ERROR_PARSE_JSON, e);
		}
	}


	/**
	 * 
	 * <p>
	 * 生成 JSON 消息
	 * </p>
	 * 
	 * @param encrypt
	 * 					加密后的消息密文
	 * @param signature
	 * 					安全签名
	 * @param timeStamp
	 * 					时间戳
	 * @param nonce
	 * 					随机字符串
	 * @return 生成的 JSON 字符串
	 */
	public static String generate( String encrypt, String signature, String timeStamp, String nonce ) {
		SSOConfig config = SSOConfig.getInstance();
		return config.getParser().toJson(new EncryptMsg(encrypt, signature, timeStamp, nonce));
	}

}