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

import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.annotation.LoginIgnore;
import com.baomidou.kisso.common.SSOConstants;
import com.baomidou.kisso.common.util.HttpUtil;
import com.baomidou.kisso.security.token.SSOToken;
import com.baomidou.kisso.web.handler.KissoDefaultHandler;
import com.baomidou.kisso.web.handler.SSOHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 登录权限验证
 * <p>
 * kisso spring 拦截器，Controller 方法调用前处理。
 * </p>
 *
 * @author hubin
 * @since 2015-11-10
 */
@Slf4j
public class SSOSpringInterceptor implements AsyncHandlerInterceptor {
    private SSOHandlerInterceptor handlerInterceptor;

    /**
     * 登录权限验证
     * <p>
     * 方法拦截 Controller 处理之前进行调用。
     * </p>
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
         * 处理 Controller 方法
         * <p>
         * 判断 handler 是否为 HandlerMethod 实例
         * </p>
         */
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginIgnore loginIgnore = method.getAnnotation(LoginIgnore.class);
            if (null != loginIgnore) {
                /*
                 * 忽略拦截
                 */
                return true;
            }

            /*
             * 正常执行
             */
            SSOToken ssoToken = SSOHelper.getSSOToken(request);
            if (ssoToken == null) {
                if (HttpUtil.isAjax(request)) {
                    /*
                     * Handler 处理 AJAX 请求
                     */
                    this.getHandlerInterceptor().preTokenIsNullAjax(request, response);
                } else {
                    /*
                     * token 为空，调用 Handler 处理
                     * 返回 true 继续执行，清理登录状态并重定向至登录界面
                     */
                    if (this.getHandlerInterceptor().preTokenIsNull(request, response)) {
                        log.debug("logout. request url:" + request.getRequestURL());
                        SSOHelper.clearRedirectLogin(request, response);
                    }
                }
                return false;
            } else {
                if (this.getHandlerInterceptor().preToken(request, response, ssoToken)) {
                    /*
                     * 正常请求，request 设置 token 减少二次解密
                     */
                    request.setAttribute(SSOConstants.SSO_TOKEN_ATTR, ssoToken);
                } else {
                    // 预处理登录票据禁止进入系统
                    return false;
                }
            }
        }

        /*
         * 通过拦截
         */
        return true;
    }

    public SSOHandlerInterceptor getHandlerInterceptor() {
        if (handlerInterceptor == null) {
            return KissoDefaultHandler.getInstance();
        }
        return handlerInterceptor;
    }

    public void setHandlerInterceptor(SSOHandlerInterceptor handlerInterceptor) {
        this.handlerInterceptor = handlerInterceptor;
    }

}
