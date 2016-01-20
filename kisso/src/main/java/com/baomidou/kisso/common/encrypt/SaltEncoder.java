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

import java.security.MessageDigest;
import java.util.logging.Logger;

import com.baomidou.kisso.SSOConfig;

/**
 * <p>
 * 盐值加密工具类（常用登录密码加密）
 * </p>
 * 
 * @author hubin
 * @Date 2016-01-20
 */
public class SaltEncoder {

	private static final Logger logger = Logger.getLogger("PasswordEncoder");

	/**
	 * 盐值
	 */
	private String salt;

	/**
	 * 算法
	 */
	private String algorithm;


	protected SaltEncoder() {
		/* 保护 */
	}


	public SaltEncoder( String salt, Algorithm algorithm ) {
		this.salt = salt;
		this.algorithm = algorithm.getKey();
	}


	/**
	 * 
	 * 字符串盐值加密
	 * 
	 * @param rawText
	 *            需要加密的字符串
	 * @return
	 */
	public String encode( String rawText ) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			//加密后的字符串  
			return Byte2Hex.byte2Hex(md.digest(mergeRawTextAndSalt(rawText).getBytes(SSOConfig.getSSOEncoding())));
		} catch ( Exception e ) {
			logger.severe(" PasswordEncoder encode exception.");
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 
	 * 判断加密内容是否正确
	 * 
	 * @param encodeText
	 * 				加密后的文本内容
	 * @param rawText
	 * 				加密前的文本内容
	 * @return
	 */
	public boolean isValid( String encodeText, String rawText ) {
		return this.encode(rawText).equals(encodeText);
	}


	private String mergeRawTextAndSalt( String rawText ) {
		if ( rawText == null ) {
			rawText = "";
		}

		if ( salt == null || "".equals(salt) ) {
			return rawText;
		} else {
			return rawText + "#" + salt;
		}
	}


	public String getSalt() {
		return salt;
	}


	public void setSalt( String salt ) {
		this.salt = salt;
	}


	public String getAlgorithm() {
		return algorithm;
	}


	public void setAlgorithm( String algorithm ) {
		this.algorithm = algorithm;
	}

}