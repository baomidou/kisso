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
package wang.leq.sso.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import wang.leq.sso.SSOConfig;

/**
 * HTTP工具类
 * <p>
 * @author   hubin
 * @Date	 2014-5-8 	 
 */
public class HttpUtil {

	/**
	 * @Description 获取URL查询条件
	 * @param request
	 * @param encode
	 * 				URLEncoder编码格式
	 * @return
	 * @throws IOException
	 */
	public static String getQueryString(HttpServletRequest request, String encode) throws IOException {
		StringBuffer sb = request.getRequestURL();
		String query = request.getQueryString();
		if (query != null && query.length() > 0) {
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
	public static boolean inContainURL(HttpServletRequest request, String url) {
		boolean result = false;
		if (url != null && !"".equals(url.trim())) {
			String[] urlArr = url.split(";");
			StringBuffer reqUrl = request.getRequestURL();
			for (int i = 0; i < urlArr.length; i++) {
				if (reqUrl.indexOf(urlArr[i]) > 1) {
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
	public static String encodeRetURL(String url, String retParam, String retUrl) {
		if (url == null) {
			return null;
		}
		StringBuffer retStr = new StringBuffer(url);
		retStr.append("?");
		retStr.append(retParam);
		retStr.append("=");
		try {
			retStr.append(URLEncoder.encode(retUrl, SSOConfig.getEncoding()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return retStr.toString();
	}

	/**
	 * GET 请求
	 * <p>
	 * @param request
	 * @return boolean
	 */
	public static boolean isGet(HttpServletRequest request) {
		if ("GET".equalsIgnoreCase(request.getMethod())) {
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
	public static boolean isPost(HttpServletRequest request) {
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			return true;
		}
		return false;
	}
}
