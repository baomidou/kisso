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
package com.baomidou.kisso;

import com.baomidou.kisso.common.CookieHelper;
import com.baomidou.kisso.common.util.RandomUtil;
import com.baomidou.kisso.enums.TokenOrigin;
import com.baomidou.kisso.security.JwtHelper;
import com.baomidou.kisso.security.token.SSOToken;
import com.baomidou.kisso.service.AbstractKissoService;
import com.baomidou.kisso.service.IKissoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <p>
 * SSO 帮助类
 * </p>
 *
 * @author hubin
 * @since 2016-01-21
 */
public class SSOHelper {
    protected static SSOConfig SSO_CONFIG;
    protected static IKissoService SSO_SERVICE;

    public static SSOConfig getSsoConfig() {
        if (null == SSO_CONFIG) {
            SSO_CONFIG = new SSOConfig();
        }
        return SSO_CONFIG;
    }

    public static SSOConfig setSsoConfig(SSOConfig ssoConfig) {
        if (null != ssoConfig) {
            SSO_CONFIG = ssoConfig;
        }
        return SSO_CONFIG;
    }

    /**
     * Kisso 服务初始化
     */
    public static IKissoService getKissoService() {
        if (SSO_SERVICE == null) {
            SSO_SERVICE = new AbstractKissoService() {
                // 不处理
            };
        }
        return SSO_SERVICE;
    }

    public static IKissoService setKissoService(IKissoService kissoService) {
        if (null != kissoService) {
            SSO_SERVICE = kissoService;
        }
        return SSO_SERVICE;
    }

    /**
     * HS512 密钥
     */
    public static String getHS512SecretKey() {
        return JwtHelper.getHS512SecretKey();
    }

    /*
     * ------------------------------- 登录相关方法 -------------------------------
     */

    /**
     * <p>
     * 设置加密 Cookie（登录验证成功）<br>
     * 最后一个参数 true 销毁当前JSESSIONID. 创建可信的 JSESSIONID 防止伪造 SESSIONID 攻击
     * </p>
     * <p>
     * 最后一个参数 false 只设置 cookie
     * </p>
     * request.setAttribute(SSOConfig.SSO_COOKIE_MAXAGE, maxAge);<br>
     * 可以动态设置 Cookie maxAge 超时时间 ，优先于配置文件的设置，无该参数 - 默认读取配置文件数据 。<br>
     * maxAge 定义：-1 浏览器关闭时自动删除 0 立即删除 120 表示Cookie有效期2分钟(以秒为单位)
     * </p>
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @param ssoToken   SSO 票据
     * @param invalidate 销毁当前 JSESSIONID
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, SSOToken ssoToken, boolean invalidate) {
        if (invalidate) {
            // 当前访问域下设置登录Cookie 设置防止伪造SessionID攻击
            CookieHelper.authJSESSIONID(request, RandomUtil.getCharacterAndNumber(8));
        }
        getKissoService().setCookie(request, response, ssoToken);
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, SSOToken ssoToken) {
        setCookie(request, response, ssoToken, false);
    }

    /*
     * ------------------------------- 客户端相关方法 -------------------------------
     */

    /**
     * <p>
     * 获取当前请求 token<br>
     * 该方法直接从 cookie 中解密获取 token, 常使用在登录系统及拦截器中使用 getToken(request)
     * </p>
     * <p>
     * 如果该请求在登录拦截器之后请使用 attrToken(request) 防止二次解密
     * </p>
     *
     * @param request HttpServletRequest
     */
    @SuppressWarnings("unchecked")
    public static <T extends SSOToken> T getSSOToken(HttpServletRequest request) {
        return (T) getKissoService().getSSOToken(request);
    }


    /**
     * <p>
     * 从请求中获取 token 通过登录拦截器之后使用<br>
     * 该数据为登录拦截器放入 request 中，防止二次解密
     * </p>
     *
     * @param request HttpServletRequest
     */
    public static <T extends SSOToken> T attrToken(HttpServletRequest request) {
        return getKissoService().attrSSOToken(request);
    }

    /**
     * <p>
     * 退出登录， 并且跳至 sso.properties 配置的属性 sso.logout.url 地址
     * </p>
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        getKissoService().logout(request, response);
    }

    /**
     * <p>
     * 清理当前登录状态<br>
     * 清理 Cookie、缓存、统计、等数据
     * </p>
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public static boolean clearLogin(HttpServletRequest request, HttpServletResponse response) {
        return getKissoService().clearLogin(request, response);
    }

    /**
     * <p>
     * 退出重定向登录页，跳至 sso.properties 配置的属性 sso.login.url 地址
     * </p>
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void clearRedirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        getKissoService().clearRedirectLogin(request, response);
    }

    /**
     * <p>
     * 获取 token 的缓存主键
     * </p>
     *
     * @param request HttpServletRequest
     * @return 缓存 KEY
     */
    public static String getTokenCacheKey(HttpServletRequest request) {
        return getSSOToken(request).toCacheKey();
    }

    /**
     * <p>
     * 获取 token 的缓存主键
     * </p>
     *
     * @param userId 用户ID
     * @return 缓存 KEY
     */
    public static String getTokenCacheKey(String userId, TokenOrigin tokenOrigin) {
        return SSOConfig.toCacheKey(userId, tokenOrigin);
    }

    /**
     * <p>
     * 踢出 指定用户 ID 的登录用户，退出当前系统。
     * </p>
     *
     * @param userId      用户ID
     * @param tokenOrigin {@link TokenOrigin}
     * @return true 成功 false 失败
     */
    public static boolean kickLogin(String userId, TokenOrigin tokenOrigin) {
        return getKissoService().kickLogin(userId, tokenOrigin);
    }

}
