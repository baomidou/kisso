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
package com.baomidou.kisso;

import org.bouncycastle.util.encoders.Base64;

import com.baomidou.kisso.common.encrypt.PBE;
import com.baomidou.kisso.common.util.Base64Util;

/**
 * <p>
 * 测试 PBE 加密
 * </p>
 * 
 * @author   hubin
 * @Date	 2014-5-12
 */
public class TestPBE {
	
	/**
	 * 测试
	 * h153cYrvsECx63t7LWzyBsXWvRSMwM14eoxa1N1wTa8kcvvvLVGWYfvL08X4Aqy0xDQjmt7ZevI.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String txt = "落霞与孤鹜齐飞，秋水共长天一色。";
		String key = "kisso2015014082159";
		PBE pbe = new PBE();
		String enc = pbe.encrypt(txt, key);
		System.out.println("加密前：" + txt);
		System.out.println("加密：" + enc);
		System.out.println("解密后：" + pbe.decrypt(enc, key));
		
		testPBE();
	}

	/**
	 * 测试 PBE 加密算法
	 */
	public static void testPBE() throws Exception {
		String str = "kisso";// 加密内容
		String password = "iloveu";// 加密口令

		System.out.println("原文：" + str);
		System.out.println("密码：" + password);

		byte[] salt = PBE.initSalt();// 初始化盐
		System.out.println("盐：" + Base64.encode(salt));

		byte[] data = PBE.encrypt(str.getBytes(), password, salt);// 加密
		System.out.println("加密后：" + Base64Util.encryptBASE64(data));

		data = PBE.decrypt(data, password, salt);// 解密
		System.out.println("解密后：" + new String(data));
	}
}
