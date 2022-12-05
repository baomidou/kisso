/*
 * Copyright (c) 2017-2022, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baomidou.kisso.common;

import com.baomidou.kisso.common.util.StringPool;
import com.baomidou.kisso.common.util.StringUtils;
import com.baomidou.kisso.service.SSOCookie;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Cookie工具类
 * <p>
 * 注意：在cookie的名或值中不能使用分号（;）、逗号（,）、等号（=）以及空格
 * </p>
 *
 * @author hubin
 * @since 2014-5-8
 */
@Slf4j
public class CookieHelper {

    /**
     * 浏览器关闭时自动删除
     */
    public final static int CLEAR_BROWSER_IS_CLOSED = -1;

    /**
     * 立即删除
     */
    public final static int CLEAR_IMMEDIATELY_REMOVE = 0;

    /**
     * <p>
     * 防止伪造SESSIONID攻击. 用户登录校验成功销毁当前JSESSIONID. 创建可信的JSESSIONID
     * </p>
     *
     * @param request 当前HTTP请求
     * @param value   用户ID等唯一信息
     */
    public static void authJSESSIONID(HttpServletRequest request, String value) {
        request.getSession().invalidate();
        request.getSession().setAttribute("KISSO-" + value, true);
    }

    /**
     * <p>
     * 根据cookieName获取Cookie
     * </p>
     *
     * @param request
     * @param cookieName Cookie name
     * @return Cookie
     */
    public static Cookie findCookieByName(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(cookieName)) {
                return cookies[i];
            }
        }
        return null;
    }

    /**
     * <p>
     * 根据 cookieName 清空 Cookie【默认域下】
     * </p>
     *
     * @param response
     * @param cookieName
     */
    public static void clearCookieByName(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, StringPool.EMPTY);
        cookie.setMaxAge(CLEAR_IMMEDIATELY_REMOVE);
        response.addCookie(cookie);
    }

    /**
     * <p>
     * 清除指定doamin的所有Cookie
     * </p>
     *
     * @param request
     * @param response
     * @param domain   Cookie所在的域
     * @param path     Cookie 路径
     * @return
     */
    public static void clearAllCookie(HttpServletRequest request, HttpServletResponse response, String domain,
                                      String path) {
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            clearCookie(response, cookies[i].getName(), domain, path);
        }
        log.info("clearAllCookie in  domain " + domain);
    }

    /**
     * <p>
     * 根据cookieName清除指定Cookie
     * </p>
     *
     * @param request
     * @param response
     * @param cookieName cookie name
     * @param domain     Cookie所在的域
     * @param path       Cookie 路径
     * @return boolean
     */
    public static boolean clearCookieByName(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                            String domain, String path) {
        boolean result = false;
        Cookie ck = findCookieByName(request, cookieName);
        if (ck != null) {
            result = clearCookie(response, cookieName, domain, path);
        }
        return result;
    }

    /**
     * <p>
     * 清除指定Cookie 等同于 clearCookieByName(...)
     * </p>
     * <p>
     * <p>
     * 该方法不判断Cookie是否存在,因此不对外暴露防止Cookie不存在异常.
     * </p>
     *
     * @param response
     * @param cookieName cookie name
     * @param domain     Cookie所在的域
     * @param path       Cookie 路径
     * @return boolean
     */
    private static boolean clearCookie(HttpServletResponse response, String cookieName, String domain, String path) {
        boolean result = false;
        try {
            Cookie cookie = new Cookie(cookieName, StringPool.EMPTY);
            cookie.setMaxAge(CLEAR_IMMEDIATELY_REMOVE);
            if (StringUtils.isNotEmpty(domain)) {
                cookie.setDomain(domain);
            }
            cookie.setPath(path);
            response.addCookie(cookie);
            log.debug("clear cookie " + cookieName);
            result = true;
        } catch (Exception e) {
            log.error("clear cookie " + cookieName + " is exception!\n" + e.toString());
        }
        return result;
    }

    /**
     * <p>
     * 添加 Cookie
     * </p>
     *
     * @param response
     * @param domain   所在域
     * @param path     域名路径
     * @param name     名称
     * @param value    内容
     * @param maxAge   生命周期参数
     * @param httpOnly 只读
     * @param secure   Https协议下安全传输
     */
    public static void addCookie(HttpServletResponse response, String domain, String path, String name, String value,
                                 int maxAge, boolean httpOnly, boolean secure) {
        SSOCookie ssoCookie = new SSOCookie(name, value);
        /**
         * 不设置该参数默认 当前所在域
         */
        if (StringUtils.isNotEmpty(domain)) {
            ssoCookie.setDomain(domain);
        }
        ssoCookie.setPath(path);
        ssoCookie.setMaxAge(maxAge);
        if (httpOnly) {
            ssoCookie.setHttpOnly(true);
        }

        /** Cookie 只在Https协议下传输设置 */
        if (secure) {
            ssoCookie.setSecure(secure);
        }
        addSSOCookie(response, ssoCookie);
    }

    /**
     * 设置 SSO Cookie
     *
     * @param response  HttpServletResponse类型的响应
     * @param ssoCookie {@link SSOCookie}
     */
    public static void addSSOCookie(HttpServletResponse response, SSOCookie ssoCookie) {
        if (null == ssoCookie) {
            return;
        }
        /**
         * 依次取得cookie中的名称、值、 最大生存时间、路径、域和是否为安全协议信息
         */
        String cookieName = ssoCookie.getName();
        String cookieValue = ssoCookie.getValue();
        StringBuffer sf = new StringBuffer();
        sf.append(cookieName).append(StringPool.EQUALS).append(cookieValue).append(StringPool.SEMICOLON);
        int maxAge = ssoCookie.getMaxAge();
        if (maxAge >= 0) {
            sf.append("Max-Age=").append(maxAge).append(StringPool.SEMICOLON);
        }
        String domain = ssoCookie.getDomain();
        if (null != domain) {
            sf.append("domain=").append(domain).append(StringPool.SEMICOLON);
        }
        String path = ssoCookie.getPath();
        if (null != path) {
            sf.append("path=").append(path).append(StringPool.SEMICOLON);
        }
        if (ssoCookie.getSecure()) {
            sf.append("secure;");
        }
        if (ssoCookie.isHttpOnly()) {
            sf.append("HTTPOnly;");
        }
        String sameSite = ssoCookie.getSameSite();
        if (null != sameSite) {
            sf.append("SameSite=").append(sameSite).append(StringPool.SEMICOLON);
        }
        response.addHeader("Set-Cookie", sf.toString());
    }

}
