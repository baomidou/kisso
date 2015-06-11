/**
 * Copyright (c) 2011-2014, hubin (243194995@qq.com).
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
package wang.leq.sso.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.leq.sso.SSOConfig;

/**
 * HTTP工具类
 * <p>
 * @author   hubin
 * @Date	 2014-5-8 	 
 */
public class HttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);


	/**
	 * @Description 获取URL查询条件
	 * @param request
	 * @param encode
	 * 				URLEncoder编码格式
	 * @return
	 * @throws IOException
	 */
	public static String getQueryString( HttpServletRequest request, String encode ) throws IOException {
		StringBuffer sb = new StringBuffer(request.getRequestURL());
		String query = request.getQueryString();
		if ( query != null && query.length() > 0 ) {
			sb.append("?").append(query);
		}
		return URLEncoder.encode(sb.toString(), encode);
	}


	/**
	 * @Description getRequestURL是否包含在URL之内
	 * @param request
	 * @param url
	 *            参数为以';'分割的URL字符串
	 * @return
	 */
	public static boolean inContainURL( HttpServletRequest request, String url ) {
		boolean result = false;
		if ( url != null && !"".equals(url.trim()) ) {
			String[] urlArr = url.split(";");
			StringBuffer reqUrl = new StringBuffer(request.getRequestURL());
			for ( int i = 0 ; i < urlArr.length ; i++ ) {
				if ( reqUrl.indexOf(urlArr[i]) > 1 ) {
					result = true;
					break;
				}
			}
		}
		return result;
	}


	/**
	 * @Description URLEncoder返回地址
	 * @param url
	 * 				跳转地址
	 * @param retParam
	 * 				返回地址参数名
	 * @param retUrl
	 * 				返回地址
	 * @return
	 */
	public static String encodeRetURL( String url, String retParam, String retUrl ) {
		if ( url == null ) {
			return null;
		}
		StringBuffer retStr = new StringBuffer(url);
		retStr.append("?");
		retStr.append(retParam);
		retStr.append("=");
		try {
			retStr.append(URLEncoder.encode(retUrl, SSOConfig.getEncoding()));
		} catch ( UnsupportedEncodingException e ) {
			logger.error("encodeRetURL error: ", e);
		}
		return retStr.toString();
	}


	/**
	 * GET 请求
	 * <p>
	 * @param request
	 * @return boolean
	 */
	public static boolean isGet( HttpServletRequest request ) {
		if ( "GET".equalsIgnoreCase(request.getMethod()) ) {
			return true;
		}
		return false;
	}


	/**
	 * POST 请求
	 * <p>
	 * @param request
	 * @return boolean
	 */
	public static boolean isPost( HttpServletRequest request ) {
		if ( "POST".equalsIgnoreCase(request.getMethod()) ) {
			return true;
		}
		return false;
	}


	/**
	 * 获取Request Playload 内容
	 * <p>
	 * @param request
	 * @return Request Playload 内容
	 */
	public static String requestPlayload( HttpServletRequest request ) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if ( inputStream != null ) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ( (bytesRead = bufferedReader.read(charBuffer)) > 0 ) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch ( IOException ex ) {
			throw ex;
		} finally {
			if ( bufferedReader != null ) {
				try {
					bufferedReader.close();
				} catch ( IOException ex ) {
					throw ex;
				}
			}
		}
		return stringBuilder.toString();
	}


	/**
	 * 获取当前完整请求地址
	 * <p>
	 * @param request
	 * @return 请求地址
	 */
	public static String getRequestUrl( HttpServletRequest request ) {
		StringBuffer url = new StringBuffer(request.getScheme());
		//请求协议 http,https
		url.append("://");
		url.append(request.getHeader("host"));//请求服务器
		url.append(request.getRequestURI());//工程名
		if ( request.getQueryString() != null ) {
			//请求参数 
			url.append("?").append(request.getQueryString());
		}
		return url.toString();
	}
}
