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
package com.baomidou.kisso.common.encrypt;

import java.util.Formatter;

/**
 * <p>
 * 字节 16进制字串转换工具类
 * </p>
 * 
 * @author hubin
 * @Date 2016-01-20
 */
public class Byte2Hex {

	/**
	 * 
	 * 字节转换为 16 进制字符串
	 * 
	 * @param b
	 * 			字节
	 * @return
	 */
	public static String byte2Hex( byte b ) {
		String hex = Integer.toHexString(b);
		if ( hex.length() > 2 ) {
			hex = hex.substring(hex.length() - 2);
		}
		while ( hex.length() < 2 ) {
			hex = "0" + hex;
		}
		return hex;
	}


	/**
	 * 
	 * 字节数组转换为 16 进制字符串
	 * 
	 * @param bytes
	 * 			字节数组
	 * @return
	 */
	public static String byte2Hex( byte[] bytes ) {
		Formatter formatter = new Formatter();
		for ( byte b : bytes ) {
			formatter.format("%02x", b);
		}
		String hash = formatter.toString();
		formatter.close();
		return hash;
	}
}
