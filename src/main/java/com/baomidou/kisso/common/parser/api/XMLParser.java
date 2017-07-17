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
package com.baomidou.kisso.common.parser.api;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.baomidou.kisso.exception.AESException;

/**
 * <p>
 * 提供提取消息格式中的密文及生成回复消息格式
 * </p>
 * 
 * @author hubin
 * @Date 2015-01-09
 */
public class XMLParser {

	/**
	 * 
	 * <p>
	 * 提取出XML数据包中的加密消息
	 * </p>
	 * 
	 * @param xmltext
	 *            待提取的XML字符串
	 * @return 提取出的加密消息字符串
	 * @throws AESException
	 *             {@link AESException}
	 */
	public static EncryptMsg extract(String xmltext) throws AESException {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(xmltext);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);
			Element root = document.getDocumentElement();
			NodeList nodelist1 = root.getElementsByTagName("Encrypt");
			NodeList nodelist2 = root.getElementsByTagName("MsgSignature");
			NodeList nodelist3 = root.getElementsByTagName("TimeStamp");
			NodeList nodelist4 = root.getElementsByTagName("Nonce");

			/* 解析XML内容 */
			String encrypt = nodelist1.item(0).getTextContent();
			String signature = nodelist2.item(0).getTextContent();
			String timestamp = nodelist3.item(0).getTextContent();
			String nonce = nodelist4.item(0).getTextContent();
			return new EncryptMsg(encrypt, signature, timestamp, nonce);
		} catch (Exception e) {
			throw new AESException(AESException.ERROR_PARSE_XML, e);
		}
	}

	/**
	 * 
	 * <p>
	 * 生成 XML 消息
	 * </p>
	 * 
	 * @param encrypt
	 *            加密后的消息密文
	 * @param signature
	 *            安全签名
	 * @param timeStamp
	 *            时间戳
	 * @param nonce
	 *            随机字符串
	 * @return 生成的XML字符串
	 */
	public static String generate(String encrypt, String signature, String timeStamp, String nonce) {
		StringBuffer format = new StringBuffer();
		format.append("<xml>\n");
		format.append("<Encrypt><![CDATA[%1$s]]></Encrypt>\n");
		format.append("<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n");
		format.append("<TimeStamp>%3$s</TimeStamp>\n");
		format.append("<Nonce><![CDATA[%4$s]]></Nonce>\n");
		format.append("</xml>");
		return String.format(format.toString(), encrypt, signature, timeStamp, nonce);
	}

}