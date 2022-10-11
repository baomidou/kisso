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
package com.baomidou.kisso.web.waf;

import com.baomidou.kisso.common.util.HttpUtil;
import com.baomidou.kisso.web.BaseFilter;
import com.baomidou.kisso.web.waf.request.WafRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>
 * Waf防火墙过滤器
 * </p>
 *
 * @author hubin
 * @since 2014-5-8
 */
@Slf4j
public class WafFilter implements BaseFilter {
    /**
     * 非过滤地址
     */
    private static String OVER_URL = null;
    /**
     * 开启XSS脚本过滤
     */
    private static boolean FILTER_XSS = true;
    /**
     * 开启SQL注入过滤
     */
    private static boolean FILTER_SQL = true;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //读取Web.xml配置地址
        OVER_URL = filterConfig.getInitParameter("over.url");

        FILTER_XSS = getBoolParameter(filterConfig, "filter_xss", true);
        FILTER_SQL = getBoolParameter(filterConfig, "filter_sql_injection", true);
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        boolean isOver = HttpUtil.inContainURL(req, OVER_URL);

        /** 非拦截URL、直接通过. */
        if (!isOver) {
            try {
                //Request请求XSS过滤
                chain.doFilter(new WafRequestWrapper(req, FILTER_XSS, FILTER_SQL), response);
            } catch (Exception e) {
                log.error(" wafxx.jar WafFilter exception , requestURL: " + req.getRequestURL());
            }
            return;
        }

        chain.doFilter(request, response);
    }


    @Override
    public void destroy() {
        log.warn("WafFilter destroy");
    }
}
