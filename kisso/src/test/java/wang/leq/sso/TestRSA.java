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

import java.util.Map;

import wang.leq.sso.common.encrypt.RSA;
import wang.leq.sso.common.util.Base64Util;

/**
 * 测试RSA
 * <p>
 * @author   hubin
 * @Date	 2014-6-17
 */
public class TestRSA {

	static String publicKey;
	static String privateKey;

	static {
		try {
			Map<String, Object> keyMap = RSA.genKeyPair();
			publicKey = RSA.getPublicKey(keyMap);
			privateKey = RSA.getPrivateKey(keyMap);
			System.err.println("公钥: \n\r" + publicKey);
			System.err.println("私钥： \n\r" + privateKey);

			//Base64Util.decodeToFile(Base64Util.filePath("/home", "d://rsa", "publicKey.rsa"), publicKey);
			//Base64Util.decodeToFile(Base64Util.filePath("/home", "d://rsa", "privateKey.rsa"), privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		test();
		testSign();
	}

	static void test() throws Exception {
		System.err.println("公钥加密——私钥解密");
		String source = "静夜思-床前看月光，疑是地上霜。抬头望山月，低头思故乡。";
		System.out.println("\r加密前文字：\r\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = RSA.encryptByPublicKey(data, publicKey);
		System.out.println("加密后文字：\r\n" + new String(encodedData));
		System.out.println("bs::" + Base64Util.encode(encodedData));
		byte[] decodedData = RSA.decryptByPrivateKey(encodedData, privateKey);
		String target = new String(decodedData);
		System.out.println("解密后文字: \r\n" + target);
	}

	static void testSign() throws Exception {
		System.err.println("私钥加密——公钥解密");
		String source = "这是一行测试RSA数字签名的无意义文字";
		System.out.println("原文字：\r\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = RSA.encryptByPrivateKey(data, privateKey);
		System.out.println("加密后：\r\n" + new String(encodedData));
		byte[] decodedData = RSA.decryptByPublicKey(encodedData, publicKey);
		String target = new String(decodedData);
		System.out.println("解密后: \r\n" + target);
		System.err.println("私钥签名——公钥验证签名");
		String sign = RSA.sign(encodedData, privateKey);
		System.err.println("签名:\r" + sign);
		boolean status = RSA.verify(encodedData, publicKey, sign);
		System.err.println("验证结果:\r" + status);
	}

}
