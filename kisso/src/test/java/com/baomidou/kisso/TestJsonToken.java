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

import java.util.logging.Logger;

import com.baomidou.kisso.common.encrypt.MD5;
import com.baomidou.kisso.common.encrypt.SSOSymmetrical;

/**
 * <p>
 * 测试JsonToken加解密
 * </p>
 * 
 * @author   hubin
 * @Date	 2014-5-12
 */
public class TestJsonToken {

	protected static final Logger logger = Logger.getLogger("TestJsonToken");


	public static void main( String[] args ) {
		SSOToken st = new SSOToken();
		st.setApp("sso");
		
		//长整型、用户ID
		st.setId(123L);
		
		//字符串类型、用户ID，这里不设置将不序列化
		//st.setUid("123");
		st.setIp("127.0.0.1");
		
		/**
		 * object 对象不参与序列化
		 */
		Token token = new Token();
		token.setApp("test");
		token.setTime(System.currentTimeMillis());
		st.setObject(token);
		
		String jsonObj = st.jsonToken();
		System.out.println("==jsonObj=" + jsonObj);
		String md5 = MD5.toMD5(jsonObj);
		System.out.println("====toMD5===" + md5);
		try {
			System.out.println("=====AESDecrypt=====" + new SSOSymmetrical().encrypt(jsonObj + "-" + md5, "123154456565546"));
		} catch ( Exception e ) {
			logger.severe(" TestJsonToken AESDecrypt error.");
			e.printStackTrace();
		}
		SSOToken mt = (SSOToken) st.parseToken(jsonObj);
		if ( null != mt ) {
			System.out.println("==parse==" + mt.getTime());
		}
	}
}
