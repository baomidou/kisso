/*
 * Copyright (c) 2017-2022, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.kisso.web.auth;

import com.baomidou.kisso.common.SSOConstants;
import com.baomidou.kisso.common.util.Base64Util;
import com.baomidou.kisso.common.util.StringPool;
import com.baomidou.kisso.common.util.StringUtils;
import com.baomidou.kisso.web.BaseFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;

/**
 * <p>
 * 资源授权访问 Basic 认证过滤器
 * </p>
 * <p>
 * HTTP WWW-Authenticate响应标头定义了应该用来访问资源的认证方法。<br/>
 * 所述WWW-Authenticate报头与一个一起发送401 Unauthorized响应。
 * </p>
 * <p>
 * WWW-Authenticate: <type> realm=<realm><br/>
 * 例如: WWW-Authenticate: Basic realm="Access to the staging site"
 * </p>
 *
 * @author hubin
 * @since 2022-10-11
 */
@Getter
@Setter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class BasicAuthenticateFilter implements BaseFilter {
    private String username;
    private String password;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Enumeration<String> enumeration = filterConfig.getInitParameterNames();
        if (enumeration.hasMoreElements()) {
            this.initParameter(filterConfig, SSOConstants.BASIC_AUTHENTICATE_USERNAME, username -> this.username = username);
            this.initParameter(filterConfig, SSOConstants.BASIC_AUTHENTICATE_PASSWORD, password -> this.password = password);
        }
        log.info("BasicAuthenticateFilter init success");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /**
         * Basic 认证
         */
        boolean certified = false;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession httpSession = request.getSession();
        Object bas = httpSession.getAttribute(SSOConstants.BASIC_AUTHENTICATE_SESSION);
        if (null != bas) {
            certified = Objects.equals(bas, username);
        }
        if (!certified) {
            String authorization = request.getHeader(SSOConstants.AUTHORIZATION);
            if (StringUtils.isNotEmpty(authorization)) {
                String auth = new String(Base64Util.decode(authorization.substring(6)));
                String[] arr = auth.split(StringPool.COLON);
                if (2 == arr.length) {
                    if (Objects.equals(arr[0], this.username) && Objects.equals(arr[1], this.password)) {
                        httpSession.setAttribute(SSOConstants.BASIC_AUTHENTICATE_SESSION, this.username);
                        certified = true;
                    }
                }
            }
        }
        if (certified) {
            filterChain.doFilter(request, servletResponse);
            return;
        }

        /**
         * 返回 401 无权限访问
         */
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setStatus(401);
        response.setHeader("WWW-Authenticate", "Basic realm=\"no auth\"");
        response.getWriter().write("No permission to access the current resource");
    }

    @Override
    public void destroy() {
        log.warn("BasicAuthenticateFilter destroy");
    }
}
