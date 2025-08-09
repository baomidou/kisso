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
package com.baomidou.kisso.service;

import com.baomidou.kisso.SSOCache;
import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOPlugin;
import com.baomidou.kisso.common.Browser;
import com.baomidou.kisso.common.CookieHelper;
import com.baomidou.kisso.common.IpHelper;
import com.baomidou.kisso.common.SSOConstants;
import com.baomidou.kisso.common.util.HttpUtil;
import com.baomidou.kisso.common.util.StringPool;
import com.baomidou.kisso.common.util.StringUtils;
import com.baomidou.kisso.enums.TokenFlag;
import com.baomidou.kisso.enums.TokenOrigin;
import com.baomidou.kisso.exception.KissoException;
import com.baomidou.kisso.security.token.SSOToken;
import com.baomidou.kisso.security.token.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * SSO 单点登录服务抽象实现类
 * </p>
 *
 * @author hubin
 * @since 2015-12-03
 */
@Slf4j
public abstract class AbstractKissoService implements IKissoService {

    /**
     * 获取当前请求 SSOToken
     * <p>
     * 从 Cookie 解密 SSOToken 使用场景，拦截器，非拦截器建议使用 attrSSOToken 减少二次解密
     * </p>
     *
     * @param request {@link HttpServletRequest}
     * @return SSOToken {@link SSOToken}
     */
    @Override
    public SSOToken getSSOToken(HttpServletRequest request) {
        SSOToken tk = checkIpBrowser(request, cacheSSOToken(request, getSSOConfig().getCache()));
        /*
         * 执行插件逻辑
         */
        List<SSOPlugin> pluginList = getSSOConfig().getPluginList();
        if (pluginList != null) {
            for (SSOPlugin plugin : pluginList) {
                boolean valid = plugin.validateToken(request, tk);
                if (!valid) {
                    return null;
                }
            }
        }
        return tk;
    }

    /**
     * 踢出 指定用户 ID 的登录用户，退出当前系统。
     */
    @Override
    public boolean kickLogin(String userId, TokenOrigin tokenOrigin) {
        SSOCache cache = getSSOConfig().getCache();
        if (cache != null) {
            return cache.delete(SSOConfig.toCacheKey(userId, tokenOrigin));
        } else {
            log.info(" kickLogin! please implements SSOCache class.");
        }
        return false;
    }

    /**
     * 当前访问域下设置登录Cookie
     * <p>
     * <p>
     * request.setAttribute(SSOgetSSOConfig().SSO_COOKIE_MAXAGE, -1);
     * 可以设置 Cookie 超时时间 ，默认读取配置文件数据 。
     * -1 浏览器关闭时自动删除 0 立即删除 120 表示Cookie有效期2分钟(以秒为单位)
     * </p>
     *
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    @Override
    public void setCookie(HttpServletRequest request, HttpServletResponse response, SSOToken ssoToken) {
        /*
         * 设置加密 Cookie
         */
        SSOCookie ssoCookie = this.generateCookie(request, ssoToken);

        /*
         * 判断 SSOToken 是否缓存处理失效
         * <p>
         * cache 缓存宕机，flag 设置为失效
         * </p>
         */
        SSOCache cache = getSSOConfig().getCache();
        if (cache != null) {
            boolean rlt = cache.set(ssoToken.toCacheKey(), ssoToken, getSSOConfig().getCacheExpires());
            if (!rlt) {
                ssoToken.flag(TokenFlag.CACHE_SHUT);
            }
        }

        /*
         * 执行插件逻辑
         */
        List<SSOPlugin> pluginList = getSSOConfig().getPluginList();
        if (pluginList != null) {
            for (SSOPlugin plugin : pluginList) {
                boolean login = plugin.login(request, response);
                if (!login) {
                    plugin.login(request, response);
                }
            }
        }

        /*
         * 设置 SSO Cookie
         */
        CookieHelper.addSSOCookie(response, ssoCookie);
    }

    /**
     * 清除登录状态
     *
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return boolean true 成功, false 失败
     */
    @Override
    public boolean clearLogin(HttpServletRequest request, HttpServletResponse response) {
        return logout(request, response, getSSOConfig().getCache());
    }

    /**
     * <p>
     * 重新登录 退出当前登录状态、重定向至登录页.
     * </p>
     *
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    @Override
    public void clearRedirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {

        /* 清理当前登录状态 */
        clearLogin(request, response);

        /* redirect login page */
        String loginUrl = getSSOConfig().getLoginUrl();
        if ("".equals(loginUrl)) {
            response.getWriter().write("{code:\"logout\", msg:\"Please login\"}");
        } else {
            String retUrl = HttpUtil.getQueryString(request, getSSOConfig().getEncoding());
            log.debug("loginAgain redirect pageUrl.." + retUrl);
            response.sendRedirect(HttpUtil.encodeRetURL(loginUrl, getSSOConfig().getParamReturnUrl(), retUrl));
        }
    }

    /**
     * SSO 退出登录
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /* delete cookie */
        logout(request, response, getSSOConfig().getCache());

        /* redirect logout page */
        String logoutUrl = getSSOConfig().getLogoutUrl();
        if ("".equals(logoutUrl)) {
            response.getWriter().write("sso.properties Must include: sso.logout.url");
        } else {
            response.sendRedirect(logoutUrl);
        }
    }


    /*
     * ------------------------------- 客户端相关方法 -------------------------------
     */

    /**
     * <p>
     * SSOToken 是否缓存处理逻辑
     * </p>
     * <p>
     * 判断 SSOToken 是否缓存 ， 如果缓存不存退出登录
     * </p>
     *
     * @param request {@link HttpServletRequest}
     * @return SSOToken {@link SSOToken}
     */
    protected SSOToken cacheSSOToken(HttpServletRequest request, SSOCache cache) {
        /*
         * 如果缓存不存退出登录
         */
        if (cache != null) {
            SSOToken cookieSSOToken = getSSOTokenFromCookie(request);
            if (cookieSSOToken == null) {
                /* 未登录 */
                return null;
            }

            SSOToken cacheSSOToken = cache.get(cookieSSOToken.toCacheKey(), getSSOConfig().getCacheExpires());
            if (cacheSSOToken == null) {
                /* 开启缓存且失效，返回 null 清除 Cookie 退出 */
                log.debug("cacheSSOToken SSOToken is null.");
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
                    if (cookieSSOToken.getTime() / SSOConstants.JWT_TIMESTAMP_CUT
                            == cacheSSOToken.getTime() / SSOConstants.JWT_TIMESTAMP_CUT) {
                        return cacheSSOToken;
                    } else {
                        log.debug("Login time is not consistent or kicked out.");
                        request.setAttribute(SSOConstants.SSO_KICK_FLAG, SSOConstants.SSO_KICK_USER);
                        return null;
                    }
                }
            }
        }

        /*
         * SSOToken 为 null 执行以下逻辑
         */
        return getSSOToken(request, getSSOConfig().getCookieName());
    }

    /**
     * <p>
     * 获取当前请求 SSOToken
     * </p>
     *
     * @param request    {@link HttpServletRequest}
     * @param cookieName Cookie名称
     * @return SSOToken
     */
    protected SSOToken getSSOToken(HttpServletRequest request, String cookieName) {
        String accessToken = request.getHeader(getSSOConfig().getAccessTokenName());
        if (StringUtils.isEmpty(accessToken)) {
            Cookie uid = CookieHelper.findCookieByName(request, cookieName);
            if (null == uid) {
                log.debug("Unauthorized login request, ip=" + IpHelper.getIpAddr(request));
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
     * @param request  {@link HttpServletRequest}
     * @param ssoToken 登录票据
     * @return SSOToken {@link SSOToken}
     */
    protected SSOToken checkIpBrowser(HttpServletRequest request, SSOToken ssoToken) {
        if (null == ssoToken) {
            return null;
        }
        /*
         * 判断请求浏览器是否合法
         */
        if (getSSOConfig().isCheckBrowser() && !Browser.isLegalUserAgent(request, ssoToken.getUserAgent())) {
            log.info("The request browser is inconsistent.");
            return null;
        }
        /*
         * 判断请求 IP 是否合法
         */
        if (getSSOConfig().isCheckIp()) {
            String ip = IpHelper.getIpAddr(request);
            if (ip != null && !ip.equals(ssoToken.getIp())) {
                log.info(String.format("ip inconsistent! return SSOToken null, SSOToken userIp:%s, reqIp:%s",
                        ssoToken.getIp(), ip));
                return null;
            }
        }
        return ssoToken;
    }


    /**
     * <p>
     * cookie 中获取 SSOToken, 该方法未验证 IP 等其他信息。
     * </p>
     * <p>
     * 1、自动设置
     * 2、拦截器 request 中获取
     * 3、解密 Cookie 获取
     * </p>
     *
     * @param request HTTP 请求
     * @return {@link SSOToken}
     */
    public SSOToken getSSOTokenFromCookie(HttpServletRequest request) {
        SSOToken tk = this.attrSSOToken(request);
        if (tk == null) {
            tk = this.getSSOToken(request, getSSOConfig().getCookieName());
        }
        return tk;
    }

    /*
     * ------------------------------- 登录相关方法 -------------------------------
     */

    /**
     * <p>
     * 根据SSOToken生成登录信息Cookie
     * </p>
     *
     * @param request {@link HttpServletRequest}
     * @param token   SSO 登录信息票据
     * @return Cookie 登录信息Cookie {@link Cookie}
     */
    protected SSOCookie generateCookie(HttpServletRequest request, Token token) {
        try {
            SSOCookie ssoCookie = new SSOCookie(getSSOConfig().getCookieName(), token.getToken());
            ssoCookie.setPath(getSSOConfig().getCookiePath());
            ssoCookie.setSecure(Objects.equals(request.getScheme(), SSOConstants.HTTPS));
            ssoCookie.setHttpOnly(getSSOConfig().isCookieHttpOnly());
            ssoCookie.setSameSite(getSSOConfig().getCookieSameSite());
            /*
             * domain 提示
             * <p>
             * 有些浏览器 localhost 无法设置 cookie
             * </p>
             */
            String domain = getSSOConfig().getCookieDomain();
            if (null != domain) {
                ssoCookie.setDomain(domain);
                if (StringPool.EMPTY.equals(domain) || domain.contains("localhost")) {
                    log.warn("if you can't login, please enter normal domain. instead:" + domain);
                }
            }

            /*
             * 设置Cookie超时时间
             */
            int maxAge = getSSOConfig().getCookieMaxAge();
            Integer attrMaxAge = (Integer) request.getAttribute(SSOConstants.SSO_COOKIE_MAXAGE);
            if (attrMaxAge != null) {
                maxAge = attrMaxAge;
            }
            if (maxAge >= 0) {
                ssoCookie.setMaxAge(maxAge);
            }
            return ssoCookie;
        } catch (Exception e) {
            throw new KissoException("Generate sso cookie exception ", e);
        }
    }


    /**
     * <p>
     * 退出当前登录状态
     * </p>
     *
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return boolean true 成功, false 失败
     */
    protected boolean logout(HttpServletRequest request, HttpServletResponse response, SSOCache cache) {
        /*
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

        /*
         * 执行插件逻辑
         */
        List<SSOPlugin> pluginList = getSSOConfig().getPluginList();
        if (pluginList != null) {
            for (SSOPlugin plugin : pluginList) {
                boolean logout = plugin.logout(request, response);
                if (!logout) {
                    plugin.logout(request, response);
                }
            }
        }

        /*
         * 删除登录 Cookie
         */
        return CookieHelper.clearCookieByName(request, response, getSSOConfig().getCookieName(),
                getSSOConfig().getCookieDomain(), getSSOConfig().getCookiePath());
    }
}
