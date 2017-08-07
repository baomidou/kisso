/**
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
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
package com.baomidou.kisso.service;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.kisso.SSOCache;
import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOPlugin;
import com.baomidou.kisso.common.Browser;
import com.baomidou.kisso.common.CookieHelper;
import com.baomidou.kisso.common.IpHelper;
import com.baomidou.kisso.common.SSOConstants;
import com.baomidou.kisso.enums.TokenFlag;
import com.baomidou.kisso.exception.KissoException;
import com.baomidou.kisso.security.token.SSOToken;
import com.baomidou.kisso.security.token.Token;

/**
 * <p>
 * SSO 单点登录服务支持类
 * </p>
 *
 * @author hubin
 * @since 2015-12-03
 */
public class KissoServiceSupport {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected SSOConfig config;

    /**
     * ------------------------------- 客户端相关方法 -------------------------------
     */

    /**
     * 获取当前请求 SSOToken
     * <p>
     * 此属性在过滤器拦截器中设置，业务系统中调用有效
     * </p>
     *
     * @param request
     * @return SSOToken {@link SSOToken}
     */
    @SuppressWarnings("unchecked")
    public <T extends SSOToken> T attrSSOToken(HttpServletRequest request) {
        return (T) request.getAttribute(SSOConstants.SSO_TOKEN_ATTR);
    }

    /**
     * <p>
     * SSOToken 是否缓存处理逻辑
     * </p>
     * <p>
     * 判断 SSOToken 是否缓存 ， 如果缓存不存退出登录
     * </p>
     *
     * @param request
     * @return SSOToken {@link SSOToken}
     */
    protected SSOToken cacheSSOToken(HttpServletRequest request, SSOCache cache) {
        /**
         * 如果缓存不存退出登录
         */
        if (cache != null) {
            SSOToken cookieSSOToken = getSSOTokenFromCookie(request);
            if (cookieSSOToken == null) {
                /* 未登录 */
                return null;
            }

            SSOToken cacheSSOToken = cache.get(cookieSSOToken.toCacheKey(), config.getCacheExpires());
            if (cacheSSOToken == null) {
                /* 开启缓存且失效，返回 null 清除 Cookie 退出 */
                logger.debug("cacheSSOToken SSOToken is null.");
                return null;
            } else {
                /*
                 * 开启缓存，判断是否宕机：
				 * 1、缓存正常，返回 tk
				 * 2、缓存宕机，执行读取 Cookie 逻辑
				 */
                if (cacheSSOToken.getFlag() != TokenFlag.CACHE_SHUT) {
                    /*
                     * 验证 cookie 与 cache 中 SSOToken 登录时间是否<br>
					 * 不一致返回 null
					 */
                    if (cookieSSOToken.getTime() == cacheSSOToken.getTime()) {
                        return cacheSSOToken;
                    } else {
                        logger.debug("Login time is not consistent or kicked out.");
                        request.setAttribute(SSOConstants.SSO_KICK_FLAG, SSOConstants.SSO_KICK_USER);
                        return null;
                    }
                }
            }
        }

        /**
         * SSOToken 为 null 执行以下逻辑
         */
        return getSSOToken(request, config.getCookieName());
    }

    /**
     * <p>
     * 获取当前请求 SSOToken
     * </p>
     *
     * @param request
     * @param cookieName Cookie名称
     * @return SSOToken
     */
    protected SSOToken getSSOToken(HttpServletRequest request, String cookieName) {
        String accessToken = request.getHeader(config.getAccessTokenName());
        if (null == accessToken || "".equals(accessToken)) {
            Cookie uid = CookieHelper.findCookieByName(request, cookieName);
            if (null == uid) {
                logger.debug("Unauthorized login request, ip=" + IpHelper.getIpAddr(request));
                return null;
            }
            return SSOToken.parser(uid.getValue(), false);
        }
        return SSOToken.parser(accessToken, true);
    }

    /**
     * <p>
     * 校验SSOToken IP 浏览器 与登录一致
     * </p>
     *
     * @param request
     * @param ssoToken 登录票据
     * @return SSOToken {@link SSOToken}
     */
    protected SSOToken checkIpBrowser(HttpServletRequest request, SSOToken ssoToken) {
        if (null == ssoToken) {
            return null;
        }
        /**
         * 判断请求浏览器是否合法
         */
        if (config.isCookieBrowser() && !Browser.isLegalUserAgent(request, ssoToken.getUserAgent())) {
            logger.info("The request browser is inconsistent.");
            return null;
        }
        /**
         * 判断请求 IP 是否合法
         */
        if (config.isCookieCheckip()) {
            String ip = IpHelper.getIpAddr(request);
            if (ssoToken != null && ip != null && !ip.equals(ssoToken.getIp())) {
                logger.info(String.format("ip inconsistent! return SSOToken null, SSOToken userIp:%s, reqIp:%s",
                        ssoToken.getIp(), ip));
                return null;
            }
        }
        return ssoToken;
    }


    /**
     * cookie 中获取 SSOToken, 该方法未验证 IP 等其他信息。
     * <p>
     * <p>
     * 1、自动设置
     * 2、拦截器 request 中获取
     * 3、解密 Cookie 获取
     * </p>
     *
     * @param request HTTP 请求
     * @return
     */
    public SSOToken getSSOTokenFromCookie(HttpServletRequest request) {
        SSOToken tk = this.attrSSOToken(request);
        if (tk == null) {
            tk = this.getSSOToken(request, config.getCookieName());
        }
        return tk;
    }

    /**
     * ------------------------------- 登录相关方法 -------------------------------
     */

    /**
     * <p>
     * 根据SSOToken生成登录信息Cookie
     * </p>
     *
     * @param request
     * @param token   SSO 登录信息票据
     * @return Cookie 登录信息Cookie {@link Cookie}
     */
    protected Cookie generateCookie(HttpServletRequest request, Token token) {
        try {
            Cookie cookie = new Cookie(config.getCookieName(), token.getToken());
            cookie.setPath(config.getCookiePath());
            cookie.setSecure(config.isCookieSecure());
            /**
             * domain 提示
             * <p>
             * 有些浏览器 localhost 无法设置 cookie
             * </p>
             */
            String domain = config.getCookieDomain();
            cookie.setDomain(domain);
            if ("".equals(domain) || domain.contains("localhost")) {
                logger.warn("if you can't login, please enter normal domain. instead:" + domain);
            }

            /**
             * 设置Cookie超时时间
             */
            int maxAge = config.getCookieMaxage();
            Integer attrMaxAge = (Integer) request.getAttribute(SSOConstants.SSO_COOKIE_MAXAGE);
            if (attrMaxAge != null) {
                maxAge = attrMaxAge;
            }
            if (maxAge >= 0) {
                cookie.setMaxAge(maxAge);
            }
            return cookie;
        } catch (Exception e) {
            throw new KissoException("Generate sso cookie exception ", e);
        }
    }


    /**
     * <p>
     * 退出当前登录状态
     * </p>
     *
     * @param request
     * @param response
     * @return boolean true 成功, false 失败
     */
    protected boolean logout(HttpServletRequest request, HttpServletResponse response, SSOCache cache) {
        /**
         * SSOToken 如果开启了缓存，删除缓存记录
         */
        if (cache != null && !SSOConstants.SSO_KICK_USER.equals(request.getAttribute(SSOConstants.SSO_KICK_FLAG))) {
            SSOToken tk = getSSOTokenFromCookie(request);
            if (tk != null) {
                boolean rlt = cache.delete(tk.toCacheKey());
                if (!rlt) {
                    cache.delete(tk.toCacheKey());
                }
            }
        }

        /**
         * 执行插件逻辑
         */
        List<SSOPlugin> pluginList = config.getPluginList();
        if (pluginList != null) {
            for (SSOPlugin plugin : pluginList) {
                boolean logout = plugin.logout(request, response);
                if (!logout) {
                    plugin.logout(request, response);
                }
            }
        }

        /**
         * 删除登录 Cookie
         */
        return CookieHelper.clearCookieByName(request, response, config.getCookieName(), config.getCookieDomain(),
                config.getCookiePath());
    }
}
