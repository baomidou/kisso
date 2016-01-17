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

import com.baomidou.kisso.common.encrypt.AESMsgCrypt;
import com.baomidou.kisso.common.encrypt.SHA1;
import com.baomidou.kisso.common.parser.api.EncryptMsg;
import com.baomidou.kisso.common.parser.api.JSONParser;
import com.baomidou.kisso.common.parser.api.XMLParser;

/**
 * <p>
 * 测试 AESMsgCrypt 消息加密类（采用微信公众平台API请求方式）
 * </p>
 * 
 * @author hubin
 * @Date 2015-01-09
 */
public class TestAESMsgCrypt {

	/**
	 * 
	 * 测试
	 * 
	 * <p>
	 * 使用AES加密时，当密钥大于128时，代码会抛出异常： java.security.InvalidKeyException: Illegal
	 * key size
	 * 
	 * 是指密钥长度是受限制的，java运行时环境读到的是受限的policy文件。文件位于${java_home}/jre/lib/security
	 * 这种限制是因为美国对软件出口的控制。
	 * 
	 * 解决办法：http://www.oracle.com/technetwork/java/javaseproducts/downloads/
	 * index.html
	 * 
	 * 进入 Oracle JDK 下载 Java Cryptography Extension (JCE) Unlimited Strength
	 * Jurisdiction Policy Files for JDK/JRE 8 【下载对应 JDK 版本的 Policy 文件】
	 * 
	 * JDK8 下载地址：http://www.oracle.com/technetwork/java/javase/downloads/jce8-
	 * download-2133166.html
	 * 
	 * 下载包的readme.txt 有安装说明。就是替换${java_home}/jre/lib/security/
	 * 下面的local_policy.jar和US_export_policy.jar
	 * </p>
	 */
	public static void main(String[] args) throws Exception {

		// 需要加密的明文
		String encodingAesKey = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFG";
		String token = "pamtest";
		String timestamp = "1409304348";
		String nonce = "xxxxxx";
		String appId = "wxb11529c136998cb6";
		String replyXMLMsg = " 中文<xml><ToUserName><![CDATA[oia2TjjewbmiOUlr6X-1crbLOvLw]]></ToUserName><FromUserName><![CDATA[gh_7f083739789a]]></FromUserName><CreateTime>1407743423</CreateTime><MsgType><![CDATA[video]]></MsgType><Video><MediaId><![CDATA[eYJ1MbwPRJtOvIEabaxHs7TX2D-HV71s79GUxqdUkjm6Gs2Ed1KF3ulAOA9H1xG0]]></MediaId><Title><![CDATA[testCallBackReplyVideo]]></Title><Description><![CDATA[testCallBackReplyVideo]]></Description></Video></xml>";
		String replyJSONMsg = " 中文{\"Description\":\"testCallBackReplyVideo\",\"MediaId\":\"eYJ1MbwPRJtOvIEabaxHs7TX2D-HV71s79GUxqdUkjm6Gs2Ed1KF3ulAOA9H1xG0\",\"CreateTime\":\"1407743423\",\"Title\":\"testCallBackReplyVideo\",\"ToUserName\":\"oia2TjjewbmiOUlr6X-1crbLOvLw\",\"FromUserName\":\"gh_7f083739789a\",\"MsgType\":\"video\"}";

		AESMsgCrypt pc = new AESMsgCrypt(token, encodingAesKey, appId);

		// 解密JSON消息
		String jsonEncrypt = pc.encryptXMLMsg(replyJSONMsg, timestamp, nonce);
		System.out.println("\n JSON 加密后: " + jsonEncrypt);
		EncryptMsg jsonEncryptMsg = XMLParser.extract(jsonEncrypt);
		String resultJson = pc.decryptMsg(jsonEncryptMsg.getMsgSignature(), timestamp, nonce,
				jsonEncryptMsg.getEncrypt());
		System.out.println("\n JSON 解密后明文: " + resultJson);

		// 解密XML消息
		String xmlEncrypt = pc.encryptJSONMsg(replyXMLMsg, timestamp, nonce);
		System.out.println("\n XML 加密后: " + xmlEncrypt);
		EncryptMsg xmlEncryptMsg = JSONParser.extract(xmlEncrypt);
		String resultXml = pc.decryptMsg(xmlEncryptMsg.getMsgSignature(), timestamp, nonce, xmlEncryptMsg.getEncrypt());
		System.out.println("\n XML 解密后明文: " + resultXml);

		// URL地址验证
		String echoStr = pc.encrypt("测试URL地址签名test-url");
		String signature = SHA1.getSHA1(token, timestamp, nonce, echoStr);
		System.out.println("\n URL 参数加密后：" + echoStr);
		String resultEchoStr = pc.verifyUrl(signature, timestamp, nonce, echoStr);
		System.out.println("\n URL 参数解密后明文: " + resultEchoStr);
	}

}
