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

import wang.leq.sso.common.encrypt.AES;
import wang.leq.sso.common.encrypt.MD5;

/**
 * 测试JsonToken加解密
 * <p>
 * @author   Administrator
 * @Date	 2014-5-12 	 
 */
public class TestJsonToken {

	public static void main(String[] args) {
		SSOToken st = new SSOToken();
		st.setUserId("123");
		st.setUserIp("127.0.0.1");
		String jsonObj = st.jsonToken();
		System.out.println("==jsonObj=" + jsonObj);
		String md5 = MD5.toMD5(jsonObj);
		System.out.println("====toMD5===" + md5);
		try {
			System.out.println("=====AESDecrypt=====" + new AES().encrypt(jsonObj + "-" + md5, "123154456565546"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		SSOToken mt = (SSOToken) st.parseToken(jsonObj);
		if (null != mt) {
			System.out.println("==parse==" + mt.getLoginTime());
		}
	}
}
