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
package com.baomidou.kisso.web.interceptor;

import com.baomidou.kisso.SSOAuthorization;
import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.annotation.Permission;
import com.baomidou.kisso.common.util.HttpUtil;
import com.baomidou.kisso.common.util.StringPool;
import com.baomidou.kisso.security.token.SSOToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * <p>
 * kisso 权限拦截器（必须在 kisso 拦截器之后执行）
 * </p>
 *
 * @author hubin
 * @since 2016-04-03
 */
@Slf4j
public class SSOPermissionInterceptor implements AsyncHandlerInterceptor {

    /*
     * 系统权限授权接口
     */
    private SSOAuthorization authorization;

    /*
     * 非法请求提示 URL
     */
    private String illegalUrl;

    /**
     * 无注解情况下，设置为true，不进行权限验证
     */
    private boolean nothingAnnotationPass = false;

    /**
     * <p>
     * 用户权限验证
     * </p>
     * <p>
     * 方法拦截 Controller 处理之前进行调用。
     * </p>
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            SSOToken token = SSOHelper.attrToken(request);
            if (null == token) {
                return true;
            }

            /*
             * 权限验证合法
             */
            if (isVerification(request, handler, token)) {
                return true;
            }

            /*
             * 无权限访问
             */
            return unauthorizedAccess(request, response);
        }

        return true;
    }


    /**
     * <p>
     * 判断权限是否合法，支持 1、请求地址 2、注解编码
     * </p>
     *
     * @param request
     * @param handler
     * @param token
     * @return
     */
    protected boolean isVerification(HttpServletRequest request, Object handler, SSOToken token) {
        /*
         * URL 权限认证
         */
        if (SSOConfig.getInstance().isPermissionUri()) {
            String uri = request.getRequestURI();
            if (uri == null || this.getAuthorization().isPermitted(token, uri)) {
                return true;
            }
        }
        /*
         * 注解权限认证
         */
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Permission pm = method.getAnnotation(Permission.class);
        if (pm != null) {
            if (pm.ignore()) {
                /**
                 * 忽略拦截
                 */
                return true;
            } else if (!StringPool.EMPTY.equals(pm.value()) && this.getAuthorization().isPermitted(token, pm.value())) {
                /**
                 * 权限合法
                 */
                return true;
            }
        } else if (this.isNothingAnnotationPass()) {
            /**
             * 无注解情况下，设置为true，不进行期限验证
             */
            return true;
        }
        /*
         * 非法访问
         */
        return false;
    }


    /**
     * <p>
     * 无权限访问处理，默认返回 403  ，illegalUrl 非空重定向至该地址
     * </p>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected boolean unauthorizedAccess(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug(" request 403 url: " + request.getRequestURI());
        if (HttpUtil.isAjax(request)) {
            /* AJAX 请求 403 未授权访问提示 */
            HttpUtil.ajaxStatus(response, 403, "ajax Unauthorized access.");
        } else {
            /* 正常 HTTP 请求 */
            if (this.getIllegalUrl() == null || "".equals(this.getIllegalUrl())) {
                response.sendError(403, "Forbidden");
            } else {
                response.sendRedirect(this.getIllegalUrl());
            }
        }
        return false;
    }


    public SSOAuthorization getAuthorization() {
        return authorization;
    }


    public void setAuthorization(SSOAuthorization authorization) {
        this.authorization = authorization;
    }


    public String getIllegalUrl() {
        return illegalUrl;
    }


    public void setIllegalUrl(String illegalUrl) {
        this.illegalUrl = illegalUrl;
    }


    public boolean isNothingAnnotationPass() {
        return nothingAnnotationPass;
    }


    public void setNothingAnnotationPass(boolean nothingAnnotationPass) {
        this.nothingAnnotationPass = nothingAnnotationPass;
    }

}
