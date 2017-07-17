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
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.common.encrypt.base64.Base64;
import com.baomidou.kisso.common.parser.api.JSONParser;
import com.baomidou.kisso.common.parser.api.XMLParser;
import com.baomidou.kisso.common.util.RandomUtil;
import com.baomidou.kisso.exception.AESException;

/**
 * <p>
 * AES 消息加解密
 * </p>
 * 
 * 说明：异常java.security.InvalidKeyException:illegal Key Size的解决方案
 * <ol>
 * <li>在官方网站下载JCE无限制权限策略文件</li>
 * <li>JDK1.7
 * 下载地址：http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-
 * 432124.html</li>
 * <li>JDK1.8
 * 下载地址：http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-
 * 2133166.html</li>
 * <li>下载后解压，可以看到local_policy.jar和US_export_policy.jar以及readme.txt</li>
 * <li>如果安装了JRE，将两个jar文件放到%JRE_HOME%\lib\security目录下覆盖原来的文件</li>
 * <li>如果安装了JDK，将两个jar文件放到%JDK_HOME%\jre\lib\security目录下覆盖原来文件</li>
 * </ol>
 * 
 * @author hubin
 * @Date 2015-01-09
 */
public class AESMsgCrypt {
	private static final Logger logger = Logger.getLogger("AseMsgCrypt");
	private byte[] aesKey;
	private String token;
	private String appId;

	/**
	 * 
	 * <p>
	 * 构造函数
	 * </p>
	 * 
	 * @param token
	 *            API 平台上，开发者设置的token
	 * @param encodingAesKey
	 *            API 平台上，开发者设置的EncodingAESKey
	 * @param appId
	 *            API 平台 appid
	 * @throws AESException
	 *             {@link AESException}
	 */
	public AESMsgCrypt(String token, String encodingAesKey, String appId) throws AESException {
		if (encodingAesKey.length() != 43) {
			throw new AESException(AESException.ERROR_ILLEGAL_AESKEY);
		}

		this.token = token;
		this.appId = appId;
		aesKey = Base64.decode(encodingAesKey + "=");
	}

	/**
	 * 
	 * <p>
	 * 生成4个字节的网络字节序
	 * </p>
	 * 
	 * @param sourceNumber
	 * @return
	 */
	public byte[] getNetworkBytesOrder(int sourceNumber) {
		byte[] orderBytes = new byte[4];
		orderBytes[3] = (byte) (sourceNumber & 0xFF);
		orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
		orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
		orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
		return orderBytes;
	}

	/**
	 * 
	 * <p>
	 * 还原4个字节的网络字节序
	 * </p>
	 * 
	 * @param orderBytes
	 * @return
	 */
	public int recoverNetworkBytesOrder(byte[] orderBytes) {
		int sourceNumber = 0;
		for (int i = 0; i < 4; i++) {
			sourceNumber <<= 8;
			sourceNumber |= orderBytes[i] & 0xff;
		}
		return sourceNumber;
	}

	/**
	 * 
	 * <p>
	 * 对明文进行加密 16 位随机数加密混淆
	 * </p>
	 * 
	 * @param text
	 *            需要加密的明文
	 * @return 加密后base64编码的字符串
	 * @throws AESException
	 *             {@link AESException}
	 */
	public String encrypt(String text) throws AESException {
		ByteGroup byteCollector = new ByteGroup();
		byte[] randomStrBytes = RandomUtil.getCharacterAndNumber(16).getBytes(SSOConfig.CHARSET_ENCODING);
		byte[] textBytes = text.getBytes(SSOConfig.CHARSET_ENCODING);
		byte[] networkBytesOrder = getNetworkBytesOrder(textBytes.length);
		byte[] appidBytes = appId.getBytes(SSOConfig.CHARSET_ENCODING);

		/* randomStr + networkBytesOrder + text + appid */
		byteCollector.addBytes(randomStrBytes);
		byteCollector.addBytes(networkBytesOrder);
		byteCollector.addBytes(textBytes);
		byteCollector.addBytes(appidBytes);

		/* ... + pad: 使用自定义的填充方式对明文进行补位填充 */
		byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
		byteCollector.addBytes(padBytes);

		/* 获得最终的字节流, 未加密 */
		byte[] unencrypted = byteCollector.toBytes();

		try {
			/* 设置加密模式为AES的CBC模式 */
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

			/* 加密 */
			byte[] encrypted = cipher.doFinal(unencrypted);

			/* 使用BASE64对加密后的字符串进行编码 */
			return new String(Base64.encode(encrypted));
		} catch (Exception e) {
			e.printStackTrace();
			throw new AESException(AESException.ERROR_ENCRYPT_AES);
		}
	}

	/**
	 * 
	 * <p>
	 * 对密文进行解密
	 * </p>
	 * 
	 * @param text
	 *            需要解密的密文
	 * @return 解密得到的明文
	 * @throws AESException
	 *             {@link AESException}
	 */
	public String decrypt(String text) throws AESException {
		byte[] original;
		try {
			/* 设置解密模式为AES的CBC模式 */
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
			cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

			/* 使用BASE64对密文进行解码 */
			byte[] encrypted = Base64.decode(text);

			/* 解密 */
			original = cipher.doFinal(encrypted);
		} catch (Exception e) {
			throw new AESException(AESException.ERROR_DECRYPT_AES, e);
		}

		String xmlContent, from_appid;
		try {
			/* 去除补位字符 */
			byte[] bytes = PKCS7Encoder.decode(original);

			/* 分离16位随机字符串,网络字节序和AppId */
			byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

			int xmlLength = recoverNetworkBytesOrder(networkOrder);

			xmlContent = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), SSOConfig.CHARSET_ENCODING);
			from_appid = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length),
					SSOConfig.CHARSET_ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AESException(AESException.ERROR_ILLEGAL_BUFFER);
		}

		/* appid不相同的情况 */
		if (!from_appid.equals(appId)) {
			throw new AESException(AESException.ERROR_VALIDATE_APPID);
		}

		return xmlContent;
	}

	public String encryptXMLMsg(String replyMsg, String timeStamp, String nonce) throws AESException {
		return encryptMsg(replyMsg, timeStamp, nonce, true);
	}

	public String encryptJSONMsg(String replyMsg, String timeStamp, String nonce) throws AESException {
		return encryptMsg(replyMsg, timeStamp, nonce, false);
	}

	/**
	 * 
	 * <p>
	 * 将公众平台回复用户的消息加密打包.
	 * <ol>
	 * <li>对要发送的消息进行AES-CBC加密</li>
	 * <li>生成安全签名</li>
	 * <li>将消息密文和安全签名打包成xml格式</li>
	 * </ol>
	 * </p>
	 * 
	 * @param replyMsg
	 *            公众平台待回复用户的消息，xml格式的字符串
	 * @param timeStamp
	 *            时间戳，可以自己生成，也可以用URL参数的timestamp
	 * @param nonce
	 *            随机串，可以自己生成，也可以用URL参数的nonce
	 * @param isXMLParser
	 *            true XML 解析 ，false JSON 解析
	 * @return 加密后的可以直接回复用户的密文，包括msg_signature, timestamp, nonce,
	 *         encrypt的xml格式的字符串
	 * @throws AESException
	 *             {@link AESException}
	 */
	protected String encryptMsg(String replyMsg, String timeStamp, String nonce, boolean isXMLParser) throws AESException {

		/* 随机生成16位字符串混淆加密 */
		String encrypt = encrypt(replyMsg);

		/* 时间戳 */
		if (timeStamp == null || "".equals(timeStamp)) {
			timeStamp = Long.toString(System.currentTimeMillis());
		}

		/* 生成安全签名 */
		String signature = SHA1.getSHA1(token, timeStamp, nonce, encrypt);
		logger.fine("encryptMsg sha1: " + signature);

		/* 判断 XML 还是 JSON 解析 */
		if (isXMLParser) {
			return XMLParser.generate(encrypt, signature, timeStamp, nonce);
		} else {
			return JSONParser.generate(encrypt, signature, timeStamp, nonce);
		}
	}

	/**
	 * <p>
	 * 检验消息的真实性，并且获取解密后的明文.
	 * <ol>
	 * <li>利用收到的密文生成安全签名，进行签名验证</li>
	 * <li>若验证通过，则提取xml中的加密消息</li>
	 * <li>对消息进行解密</li>
	 * </ol>
	 * </p>
	 * 
	 * @param msgSignature
	 *            签名串，对应URL参数的msg_signature
	 * @param timeStamp
	 *            时间戳，对应URL参数的timestamp
	 * @param nonce
	 *            随机串，对应URL参数的nonce
	 * @param encrypt
	 *            密文，对应POST请求的数据
	 * @return 解密后的原文
	 * @throws AESException
	 *             {@link AESException}
	 */
	public String decryptMsg(String msgSignature, String timeStamp, String nonce, String encrypt)
			throws AESException {
		/* 验证安全签名 */
		String signature = SHA1.getSHA1(token, timeStamp, nonce, encrypt);
		logger.fine("decryptMsg msgSignature sha1: " + msgSignature + ", decryptMsg signature sha1: " + signature);

		/* 与URL中的签名比较是否相等 */
		if (!signature.equals(msgSignature)) {
			throw new AESException(AESException.ERROR_VALIDATE_SIGNATURE);
		}

		/* 解密 */
		return decrypt(encrypt);
	}

	/**
	 * 
	 * <p>
	 * 验证 URL
	 * </p>
	 * 
	 * @param msgSignature
	 *            签名串，对应URL参数的msg_signature
	 * @param timeStamp
	 *            时间戳，对应URL参数的timestamp
	 * @param nonce
	 *            随机串，对应URL参数的nonce
	 * @param echoStr
	 *            随机串，对应URL参数的echostr
	 * @return 解密之后的echostr
	 * @throws AESException
	 *             {@link AESException}
	 */
	public String verifyUrl(String msgSignature, String timeStamp, String nonce, String echoStr) throws AESException {
		String signature = SHA1.getSHA1(token, timeStamp, nonce, echoStr);
		if (!signature.equals(msgSignature)) {
			throw new AESException(AESException.ERROR_VALIDATE_SIGNATURE);
		}
		return decrypt(echoStr);
	}

}