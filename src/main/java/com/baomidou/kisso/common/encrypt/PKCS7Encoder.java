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
package com.baomidou.kisso.common.encrypt;

import java.util.Arrays;

import com.baomidou.kisso.SSOConfig;

/**
 * <p>
 * 提供基于 PKCS7 算法的加解密
 * </p>
 * 
 * @author hubin
 * @Date 2015-01-09
 */
public class PKCS7Encoder {
	private static final int BLOCK_SIZE = 32;

	/**
	 * 
	 * <p>
	 * 获得对明文进行补位填充的字节
	 * </p>
	 * 
	 * @param count
	 * 				需要进行填充补位操作的明文字节个数
	 * @return 补齐用的字节数组
	 */
	public static byte[] encode(int count) {
		/* 计算需要填充的位数 */
		int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
		if (amountToPad == 0) {
			amountToPad = BLOCK_SIZE;
		}
		
		/* 获得补位所用的字符 */
		char padChr = chr(amountToPad);
		String tmp = new String();
		for (int index = 0; index < amountToPad; index++) {
			tmp += padChr;
		}
		return tmp.getBytes(SSOConfig.CHARSET_ENCODING);
	}

	/**
	 * 
	 * <p>
	 * 删除解密后明文的补位字符
	 * </p>
	 * 
	 * @param decrypted
	 * 					解密后的明文
	 * @return 删除补位字符后的明文
	 */
	public static byte[] decode(byte[] decrypted) {
		int pad = (int) decrypted[decrypted.length - 1];
		if (pad < 1 || pad > 32) {
			pad = 0;
		}
		return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
	}

	/**
	 * 
	 * <p>
	 * 将数字转化成ASCII码对应的字符，用于对明文进行补码
	 * </p>
	 * 
	 * @param a
	 * 			需要转化的数字
	 * @return 转化得到的字符
	 */
	public static char chr(int a) {
		byte target = (byte) (a & 0xFF);
		return (char) target;
	}

}