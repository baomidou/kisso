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

import wang.leq.sso.common.encrypt.PBE;

/**
 * PBE 加密测试
 * @author hubin
 *
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
	}

}
