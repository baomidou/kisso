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
package com.baomidou.kisso.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.kisso.SSOConstant;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.Token;
import com.baomidou.kisso.common.util.HttpUtil;

/**
 * SSO 过滤器验证登录状态
 * <p>
 * @author   hubin
 * @Date	 2014-5-8 	 
 */
public class SSOFilter implements Filter {
	private final static Logger logger = LoggerFactory.getLogger(SSOFilter.class);
	private static String OVERURL = null;

	public void init(FilterConfig config) throws ServletException {
		/**
		 * 从应用 web.xml 配置参数中
		 * 获取不需要拦截URL
		 */
		OVERURL = config.getInitParameter("over.url");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		boolean isOver = HttpUtil.inContainURL(req, OVERURL);
		/** 非拦截URL、安全校验Cookie */
		if (!isOver) {
			Token token = SSOHelper.getToken(req);
			if (token == null) {
				/**
				 * 重新登录
				 */
				logger.debug("logout. request url:{}", req.getRequestURL());
				SSOHelper.login(req, res);
				return ;
			} else {
				req.setAttribute(SSOConstant.SSO_TOKEN_ATTR, token);
			}
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
		OVERURL = null;
	}
}
