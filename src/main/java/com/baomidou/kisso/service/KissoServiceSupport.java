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
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.kisso.SSOCache;
import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOPlugin;
import com.baomidou.kisso.common.Browser;
import com.baomidou.kisso.common.CookieHelper;
import com.baomidou.kisso.common.IpHelper;
import com.baomidou.kisso.common.SSOConstants;
import com.baomidou.kisso.common.encrypt.SSOEncrypt;
import com.baomidou.kisso.exception.KissoException;
import com.baomidou.kisso.security.token.SSOToken;

/**
 * <p>
 * SSO 单点登录服务支持类
 * </p>
 *
 * @author hubin
 * @Date 2015-12-03
 */
public class KissoServiceSupport {

    protected final Logger logger = Logger.getLogger("KissoServiceSupport");
    protected SSOConfig config;

    public SSOConfig getConfig() {
        return config;
    }

    public void setConfig(SSOConfig config) {
        this.config = config;
    }

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
        return (T) request.getAttribute(SSOConfig.SSO_TOKEN_ATTR);
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
     * @param encrypt 对称加密算法类
     * @return SSOToken {@link SSOToken}
     */
    protected SSOToken cacheSSOToken(HttpServletRequest request, SSOEncrypt encrypt, SSOCache cache) {
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
                logger.fine("cacheSSOToken SSOToken is null.");
                return null;
            } else {
                /*
				 * 开启缓存，判断是否宕机：
				 * 1、缓存正常，返回 tk
				 * 2、缓存宕机，执行读取 Cookie 逻辑
				 */
                if (cacheSSOToken.getFlag() != SSOConstants.TOKEN_FLAG_CACHE_SHUT) {
					/*
					 * 验证 cookie 与 cache 中 SSOToken 登录时间是否<br>
					 * 不一致返回 null
					 */
                    if (cookieSSOToken.getTime() == cacheSSOToken.getTime()) {
                        return cacheSSOToken;
                    } else {
                        logger.severe("Login time is not consistent or kicked out.");
                        request.setAttribute(SSOConfig.SSO_KICK_FLAG, SSOConfig.SSO_KICK_USER);
                        return null;
                    }
                }
            }
        }

        /**
         * SSOToken 为 null 执行以下逻辑
         */
        return getSSOToken(request, encrypt, config.getCookieName());
    }

    /**
     * <p>
     * 获取当前请求 SSOToken
     * </p>
     *
     * @param request
     * @param encrypt    对称加密算法类
     * @param cookieName Cookie名称
     * @return SSOToken ${SSOToken}
     */
    protected SSOToken getSSOToken(HttpServletRequest request, SSOEncrypt encrypt, String cookieName) {
        Cookie uid = CookieHelper.findCookieByName(request, cookieName);
        if (uid == null) {
            /**
             * 未登录请求
             */
            logger.fine("jsonSSOToken is null.");
            return null;
        } else {
            return config.getSSOToken().parseSSOToken(uid.getValue());
        }
    }

    /**
     * <p>
     * 获取当前请求 JsonSSOToken
     * </p>
     *
     * @param request
     * @param cookieName Cookie名称
     * @return String 当前SSOToken的json格式值
     */
    protected String getJsonSSOToken(HttpServletRequest request, String cookieName) {
        Cookie uid = CookieHelper.findCookieByName(request, cookieName);
        if (uid != null) {
            String jsonSSOToken = uid.getValue();
            /**
             * 判断是否认证浏览器 混淆信息
             */
            if (config.getCookieBrowser()) {
                if (Browser.isLegalUserAgent(request, SSOTokenAttr[0], SSOTokenAttr[1])) {
                    return SSOTokenAttr[0];
                } else {
                    /**
                     * 签名验证码失败
                     */
                    logger.severe("SSOHelper getSSOToken, find Browser is illegal.");
                }
            } else {
                /**
                 * 不需要认证浏览器信息混淆 返回JsonSSOToken
                 */
                return SSOTokenAttr[0];
            }
        }

        return null;
    }

    /**
     * <p>
     * 校验SSOToken IP 与登录 IP 是否一致
     * </p>
     *
     * @param request
     * @param SSOToken 登录票据
     * @return SSOToken {@link SSOToken}
     */
    protected SSOToken checkIp(HttpServletRequest request, SSOToken SSOToken) {
        /**
         * 判断是否检查 IP 一致性
         */
        if (config.getCookieCheckip()) {
            String ip = IpHelper.getIpAddr(request);
            if (SSOToken != null && ip != null && !ip.equals(SSOToken.getIp())) {
                /**
                 * 检查 IP 与登录IP 不一致返回 null
                 */
                logger.info(String.format("ip inconsistent! return SSOToken null, SSOToken userIp:%s, reqIp:%s",
                        SSOToken.getIp(), ip));
                return null;
            }
        }
        return SSOToken;
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
            tk = this.getSSOToken(request, config.getEncrypt(), config.getCookieName());
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
     * @param ssoToken SSO 登录信息票据
     * @return Cookie 登录信息Cookie {@link Cookie}
     */
    protected Cookie generateCookie(HttpServletRequest request, SSOToken ssoToken) {
        try {
            Cookie cookie = new Cookie(config.getCookieName(), ssoToken.getToken());
            cookie.setPath(config.getCookiePath());
            cookie.setSecure(config.getCookieSecure());
            /**
             * domain 提示
             * <p>
             * 有些浏览器 localhost 无法设置 cookie
             * </p>
             */
            String domain = config.getCookieDomain();
            cookie.setDomain(domain);
            if ("".equals(domain) || domain.contains("localhost")) {
                logger.warning("if you can't login, please enter normal domain. instead:" + domain);
            }

            /**
             * 设置Cookie超时时间
             */
            int maxAge = config.getCookieMaxage();
            Integer attrMaxAge = (Integer) request.getAttribute(SSOConfig.SSO_COOKIE_MAXAGE);
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
        if (cache != null && !SSOConfig.SSO_KICK_USER.equals(request.getAttribute(SSOConfig.SSO_KICK_FLAG))) {
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
