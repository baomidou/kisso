/**
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
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
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.common.encrypt.base64.UrlBase64;
import com.baomidou.kisso.exception.KissoException;

/**
 * <p>
 * SSO 对称加密, 支持DES、AES、Blowfish、RC2、RC4的加密解密
 * </p>
 * <p>
 * Standard，即数据加密标准，是一种使用密钥加密的块算法，1976年被美国联邦政府的国家标准局确定为联邦资料处理标准（FIPS），
 * 随后在国际上广泛流传开来。
 * </p>
 * <p>
 * AES，高级加密标准（英语：Advanced Encryption
 * Standard，缩写：AES），在密码学中又称Rijndael加密法，是美国联邦政府采用的一种区块加密标准。这个标准用来替代原先的DES
 * ，已经被多方分析且广为全世界所使用。经过五年的甄选流程，高级加密标准由美国国家标准与技术研究院（NIST） 于2001年11月26日发布于FIPS PUB
 * 197，并在2002年5月26日成为有效的标准。2006年，高级加密标准已然成为对称密钥加密中最流行的算法之一。
 * </p>
 * <p>
 * Blowfish算法是一个64位分组及可变密钥长度的对称密钥分组密码算法，可用来加密64比特长度的字符串。32位处理器诞生后，
 * Blowfish算法因其在加密速度上超越了DES而引起人们的关注。Blowfish算法具有加密速度快、紧凑、密钥长度可变、可免费使用等特点
 * ，已被广泛使用于众多加密软件。
 * </p>
 * <p>
 * RC2是由著名密码学家Ron Rivest设计的一种传统对称分组加密算法，它可作为DES算法的建议替代算法。它的输入和输出都是64比特。
 * 密钥的长度是从1字节到128字节可变，但目前的实现是8字节（1998年）。
 * </p>
 * <p>
 * RC4加密算法是大名鼎鼎的RSA三人组中的头号人物Ronald
 * Rivest在1987年设计的密钥长度可变的流加密算法簇。之所以称其为簇，是由于其核心部分的S-box长度可为任意，但一般为256字节。
 * 该算法的速度可以达到DES加密的10倍左右，且具有很高级别的非线性。RC4起初是用于保护商业机密的。但是在1994年9月，
 * 它的算法被发布在互联网上，也就不再有什么商业机密了。RC4也被叫做ARC4（Alleged
 * RC4——所谓的RC4），因为RSA从来就没有正式发布过这个算法。
 * </p>
 * 
 * @author hubin
 * @Date 2016-08-11
 */
public class SSOSymmetrical implements SSOEncrypt {
	private static final Logger logger = Logger.getLogger("SSOSymmetrical");
	private Algorithm algorithm = Algorithm.RC4;

	public SSOSymmetrical() {

	}

	public SSOSymmetrical(Algorithm algorithm) {
		this.algorithm = algorithm;
		System.err.println("Your current encryption algorithm is " + algorithm.getKey());
	}

	public String encrypt(String value, String key) throws Exception {
		byte[] b = UrlBase64.encode(encrypt(algorithm, value.getBytes(), key));
		return new String(b, SSOConfig.getSSOEncoding());
	}

	public String decrypt(String value, String key) throws Exception {
		byte[] b = decrypt(algorithm, UrlBase64.decode(value.getBytes()), key);
		return new String(b, SSOConfig.getSSOEncoding());
	}

	/**
	 * generate KEY
	 */
	private Key toKey(Algorithm algorithm, String strKey) throws Exception {
		/*
		 * MD5 处理密钥
		 */
		byte[] key = MD5.md5Raw(strKey.getBytes(SSOConfig.getSSOEncoding()));
		if (Algorithm.DES == algorithm) {
			DESKeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm.getKey());
			SecretKey secretKey = keyFactory.generateSecret(dks);
			return secretKey;
		}
		return new SecretKeySpec(key, algorithm.getKey());
	}

	/**
	 * 解密
	 * 
	 * @param algorithm
	 * @param data
	 * @param key
	 * @return
	 */
	public byte[] decrypt(Algorithm algorithm, byte[] data, String key) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm.toString());
			cipher.init(Cipher.DECRYPT_MODE, this.toKey(algorithm, key));
			return cipher.doFinal(data);
		} catch (Exception e) {
			logger.severe("Encrypt setKey is exception.");
			throw new KissoException(e);
		}
	}

	/**
	 * 加密
	 * 
	 * @param algorithm
	 * @param data
	 * @param key
	 * @return
	 */
	public byte[] encrypt(Algorithm algorithm, byte[] data, String key) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm.toString());
			cipher.init(Cipher.ENCRYPT_MODE, this.toKey(algorithm, key));
			return cipher.doFinal(data);
		} catch (Exception e) {
			logger.severe("Encrypt setKey is exception.");
			throw new KissoException(e);
		}
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

}
