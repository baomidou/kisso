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
package com.baomidou.kisso;

import com.baomidou.kisso.common.encrypt.AES;

/**
 * <p>
 * 测试 AES 加密
 * </p>
 * 
 * @author   hubin
 * @Date	 2014-5-12
 */
public class TestAES {

	/**
	 * 测试
	 * blgXhbGmmno-qiVk6epGkeVYFM4EeXKS_O0O5mLWLeCoGVvpeFAAaUs6tmirfCNF35RfBaNI5bf97g7Jc9gTOg..
	 * @param args
	 * @throws Exception
	 */
	public static void main( String[] args ) throws Exception {
		String txt = "落霞与孤鹜齐飞，秋水共长天一色。";
		String key = "kisso2015014082159";
		AES pbe = new AES();
		String enc = pbe.encrypt(txt, key);
		System.out.println("加密前：" + txt);
		System.out.println("加密：" + enc);
		System.out.println("解密后：" + pbe.decrypt(enc, key));
	}

}
