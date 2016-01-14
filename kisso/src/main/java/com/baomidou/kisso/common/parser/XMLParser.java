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
package com.baomidou.kisso.common.parser;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.baomidou.kisso.exception.AesException;

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
	 * 提取出xml数据包中的加密消息
	 * </p>
	 * 
	 * @param xmltext
	 * 				待提取的xml字符串
	 * @return 提取出的加密消息字符串
	 * @throws AesException {@link AesException}
	 */
	public static Object[] extract( String xmltext ) throws AesException {
		Object[] result = new Object[3];
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(xmltext);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);
			Element root = document.getDocumentElement();
			NodeList nodelist1 = root.getElementsByTagName("Encrypt");
			NodeList nodelist2 = root.getElementsByTagName("ToUserName");
			result[0] = 0;
			result[1] = nodelist1.item(0).getTextContent();
			result[2] = nodelist2.item(0).getTextContent();
			return result;
		} catch ( Exception e ) {
			throw new AesException(AesException.ERROR_PARSE_XML, e);
		}
	}


	/**
	 * 
	 * <p>
	 * 生成 xml 消息
	 * </p>
	 * 
	 * @param encrypt
	 * 					加密后的消息密文
	 * @param signature
	 * 					安全签名
	 * @param timestamp
	 * 					时间戳
	 * @param nonce
	 * 					随机字符串
	 * @return 生成的xml字符串
	 */
	public static String generate( String encrypt, String signature, String timestamp, String nonce ) {
		StringBuffer format = new StringBuffer();
		format.append("<xml>\n");
		format.append("<Encrypt><![CDATA[%1$s]]></Encrypt>\n");
		format.append("<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n");
		format.append("<TimeStamp>%3$s</TimeStamp>\n");
		format.append("<Nonce><![CDATA[%4$s]]></Nonce>\n");
		format.append("</xml>");
		return String.format(format.toString(), encrypt, signature, timestamp, nonce);
	}

}