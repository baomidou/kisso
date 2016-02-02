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

import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.common.util.Base64Util;
import com.baomidou.kisso.exception.KissoException;

/**
 * <p>
 * AES encrypt util
 * </p>
 * 
 * @author hubin
 * @Date 2014-5-8
 */
public class AES implements SSOEncrypt {
	private static final Logger logger = Logger.getLogger("AES");
	private SecretKeySpec secretKey;
	private static AES aes;

	public static AES getInstance() {
		if (aes == null) {
			aes = new AES();
		}
		return aes;
	}

	public AES() {
		
	}

	public AES(String str) {
		setKey(str);// generate secret key
	}

	public SecretKey getSecretKey() {
		return secretKey;
	}

	/**
	 * generate KEY
	 */
	public void setKey(String strKey) {
		try {
			byte[] bk = MD5.md5Raw(strKey.getBytes(SSOConfig.getSSOEncoding()));
			this.secretKey = new SecretKeySpec(bk, Algorithm.AES.getKey());
		} catch (Exception e) {
			logger.severe("Encrypt setKey is exception.");
			e.printStackTrace();
		}
	}

	/**
	 * @Description AES encrypt
	 * @param str
	 * @return
	 */
	public String encryptAES(String str) {
		byte[] encryptBytes = null;
		String encryptStr = null;
		try {
			Cipher cipher = Cipher.getInstance(Algorithm.AES.getKey());
			cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
			encryptBytes = cipher.doFinal(str.getBytes());
			if (encryptBytes != null) {
				encryptStr = Base64Util.encryptBASE64(encryptBytes);
			}
		} catch (Exception e) {
			throw new KissoException(e);
		}
		return encryptStr;
	}

	/**
	 * @Description AES decrypt
	 * @param str
	 * @return
	 */
	public String decryptAES(String str) {
		byte[] decryptBytes = null;
		String decryptStr = null;
		try {
			Cipher cipher = Cipher.getInstance(Algorithm.AES.getKey());
			cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
			byte[] scrBytes = Base64Util.decryptBASE64(str);
			decryptBytes = cipher.doFinal(scrBytes);
		} catch (Exception e) {
			throw new KissoException(e);
		}
		if (decryptBytes != null) {
			decryptStr = new String(decryptBytes);
		}
		return decryptStr;
	}

	/**
	 * AES encrypt
	 */
	public String encrypt(String value, String key) throws Exception {
		setKey(key);
		return encryptAES(value);
	}

	/**
	 * AES decrypt
	 */
	public String decrypt(String value, String key) throws Exception {
		setKey(key);
		return decryptAES(value);
	}

}