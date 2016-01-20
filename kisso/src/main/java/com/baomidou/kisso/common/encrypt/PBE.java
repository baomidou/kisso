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

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.baomidou.kisso.common.util.Base64Util;
import com.baomidou.kisso.exception.KissoException;

/**
 * <p>
 * PBE 对称加密算法
 * </p>
 * 
 * @author hubin
 * @Date	 2014-6-17
 */
public class PBE implements SSOEncrypt {
	public static final String ALGORITHM = "PBEWITHMD5andDES";
	
	/** 迭代次数 */
	public static final int ITERATION_COUNT = 100;
	
	private static PBE pbe;

	public static PBE getInstance() {
		if (pbe == null) {
			pbe = new PBE();
		}
		return pbe;
	}

	public PBE() {

	}

	/**
	 * 盐初始化<br>
	 * 盐长度必须为8字节
	 * 
	 * @return byte[] 盐
	 */
	public static byte[] initSalt() {
		SecureRandom random = new SecureRandom();
		return random.generateSeed(8);
	}

	/**
	 * 转换密钥
	 * 
	 * @param password
	 *            密码
	 * @return key 密钥
	 * @throws Exception
	 */
	private static Key toKey(String password) throws Exception {
		PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(keySpec);
		return secretKey;
	}

	/**
	 * 加密
	 * 
	 * @param data
	 *            数据
	 * @param password
	 *            密码
	 * @param salt
	 *            盐
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, String password, byte[] salt) throws Exception {
		Key key = toKey(password);
		PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
		return cipher.doFinal(data);
	}

	/**
	 * 解密
	 * 
	 * @param data
	 *            数据
	 * @param password
	 *            密码
	 * @param salt
	 *            盐
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, String password, byte[] salt) throws Exception {
		Key key = toKey(password);
		PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
		return cipher.doFinal(data);
	}

	private static byte[] generateSalt(String key) {
		return MD5.toMD5(key).substring(0, 8).getBytes();
	}

	public static String encryptPBE(String value, String key) {
		try {
			return Base64Util.encryptBASE64(encrypt(value.getBytes(), key, generateSalt(key)));
		} catch (Exception e) {
			throw new KissoException(e);
		}
	}

	public static String decryptPBE(String value, String key) {
		try {
			return new String(decrypt(Base64Util.decryptBASE64(value), key, generateSalt(key)));
		} catch (Exception e) {
			throw new KissoException(e);
		}
	}

	public String encrypt(String value, String key) throws Exception {
		return encryptPBE(value, key);
	}

	public String decrypt(String value, String key) throws Exception {
		return decryptPBE(value, key);
	}

}
