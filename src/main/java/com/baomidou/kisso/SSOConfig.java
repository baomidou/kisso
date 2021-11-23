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
import com.baomidou.kisso.common.SSOConstants;
import com.baomidou.kisso.exception.KissoException;
import com.baomidou.kisso.security.token.SSOToken;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * SSO 配置文件解析
 * </p>
 *
 * @author hubin
 * @since 2015-12-05
 */
@Data
@Accessors(chain = true)
public class SSOConfig {
    /**
     * 编码格式，默认 UTF-8
     */
    private String encoding = SSOConstants.ENCODING;
    /**
     * 签名密钥（用于对此算法）
     */
    private String signKey;
    /**
     * 签名算法
     *
     * @see io.jsonwebtoken.SignatureAlgorithm
     */
    private String signAlgorithm = "HS512";
    /**
     * RSA 私钥 key.jks 存储路径
     */
    private String rsaJksStore = "key.jks";
    /**
     * RSA 公钥 public.cert 存储路径
     */
    private String rsaCertStore = "public.cert";
    /**
     * RSA 密钥 Alias
     */
    private String rsaAlias = "jwtkey";
    /**
     * RSA 密钥 keypass
     */
    private String rsaKeypass = "llTs1p68K";
    /**
     * RSA 密钥 storepass
     */
    private String rsaStorepass = "lLt66Y8L321";
    /**
     * 访问票据名
     */
    private String accessTokenName = "accessToken";
    /**
     * cookie 名称
     */
    private String cookieName = "uid";
    /**
     * cookie 所在有效域名，不设置为当前访问域名
     */
    private String cookieDomain;
    /**
     * cookie 路径
     */
    private String cookiePath = "/";
    /**
     * cookie 是否设置安全，设置 true 那么只能为 https 协议访问
     */
    private boolean cookieSecure = false;
    /**
     * cookie 是否为只读状态，设置 js 无法获取
     */
    private boolean cookieHttpOnly = true;
    /**
     * cookie 有效期 -1 关闭浏览器失效
     */
    private int cookieMaxAge = -1;
    /**
     * cookie的SameSite属性用来限制第三方Cookie，从而减少安全风险(防止CSRF) 支持三种模式：
     * <p>
     * Strict 仅允许一方请求携带Cookie，即浏览器将只发送相同站点请求的Cookie，即当前网页URL与请求目标URL完全一致，浏览器默认该模式。
     * <p>
     * Lax 允许部分第三方请求携带Cookie
     * <p>
     * None 无论是否跨站都会发送Cookie
     */
    private String cookieSameSite;
    /**
     * 是否验证 cookie 设置时浏览器信息
     */
    private boolean cookieBrowser = false;
    /**
     * 是否验证 cookie 设置时 IP 信息
     */
    private boolean cookieCheckIp = false;
    /**
     * 登录地址
     */
    private String loginUrl = "";
    /**
     * 退出地址
     */
    private String logoutUrl = "";
    /**
     * 登录成功回调地址
     */
    private String paramReturnUrl = "ReturnURL";
    /**
     * 缓存有效期设置
     */
    private int cacheExpires = CookieHelper.CLEAR_BROWSER_IS_CLOSED;
    /**
     * 访问票据
     */
    private SSOToken ssoToken;

    /**
     * 权限认证（默认 false）
     */
    private boolean permissionUri = false;

    /**
     * 插件列表
     */
    private List<SSOPlugin> pluginList;
    /**
     * SSO 缓存
     */
    private SSOCache cache;
    /**
     * SSO 权限授权
     */
    private SSOAuthorization authorization;


    public SSOConfig() {
        /* 支持 setInstance 设置初始化 */
    }

    /**
     * new 当前对象
     */
    public static SSOConfig getInstance() {
        return SSOHelper.getSsoConfig();
    }

    public static String getSSOEncoding() {
        return getInstance().getEncoding();
    }

    public String getRsaCertStore() {
        if (null == rsaCertStore) {
            throw new KissoException("public.cert not found");
        }
        return rsaCertStore;
    }

    /**
     * 签名密钥
     */
    public String getSignKey() {
        if (null == signKey) {
            return "Janfv5UgKhoDrH73EZT7m+81pgqLN3EjWKXZtqF9lQHH9WruxqX0+FkQys6XK0QXzSUckseOAZGeQyvfreA3tw==";
        }
        return signKey;
    }

    public String getRsaJksStore() {
        if (null == rsaJksStore) {
            throw new KissoException("jwt.jks not found");
        }
        return rsaJksStore;
    }


    public SSOAuthorization getAuthorization() {
        return authorization;
    }

    public SSOConfig setAuthorization(SSOAuthorization authorization) {
        this.authorization = authorization;
        return this;
    }

    public String getCookieSameSite() {
        if (null != cookieSameSite && ("Strict".equalsIgnoreCase(cookieSameSite)
                || "Lax".equalsIgnoreCase(cookieSameSite)
                || "None".equalsIgnoreCase(cookieSameSite))) {
            return cookieSameSite;
        }
        return null;
    }

    /**
     * <p>
     * 生成 Token 缓存主键
     * </p>
     *
     * @param userId 用户ID
     * @return
     */
    public static String toCacheKey(Object userId) {
        StringBuffer ck = new StringBuffer();
        ck.append("ssoTokenKey_");
        ck.append(userId);
        return ck.toString();
    }
}
