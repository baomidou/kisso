/*
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
package com.baomidou.kisso;

import java.util.List;

import com.baomidou.kisso.common.CookieHelper;
import com.baomidou.kisso.common.SSOConstants;
import com.baomidou.kisso.exception.KissoException;
import com.baomidou.kisso.security.token.SSOToken;

import lombok.Data;
import lombok.experimental.Accessors;

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

    private String encoding = SSOConstants.ENCODING;
    private String signkey = "3QD2j1B1s6Uj1jx6q8";
    private String signAlgorithm = "HS512";
    /**
     * 公钥 public.cert
     */
    private String rsaCertstore;
    /**
     * 私钥 jwt.jks
     */
    private String rsaKeystore;
    private String rsaAlias = "jwtkey";
    private String rsaKeypass = "keypassword";
    private String rsaStorepass = "letkisso";
    private String accessTokenName = "accessToken";
    private String cookieName = "uid";
    private String cookieDomain;
    private String cookiePath = "/";
    private boolean cookieSecure = false;
    private boolean cookieHttponly = true;
    private int cookieMaxage = -1;
    private boolean cookieBrowser = false;
    private boolean cookieCheckip = false;
    private String loginUrl = "";
    private String logoutUrl = "";
    private String paramReturl = "ReturnURL";
    private int cacheExpires = CookieHelper.CLEAR_BROWSER_IS_CLOSED;
    private SSOToken ssoToken;

    /**
     * 权限认证（默认 false）
     */
    private boolean permissionUri = false;

    /**
     * 插件列表
     */
    private List<SSOPlugin> pluginList;
    private SSOCache cache;

    private static SSOConfig SSO_CONFIG = null;

    private static SSOAuthorization SSO_AUTHORIZATION;

    public SSOConfig() {
        /* 支持 setInstance 设置初始化 */
    }

    /**
     * new 当前对象
     */
    public static SSOConfig getInstance() {
        if (SSO_CONFIG == null) {
            SSO_CONFIG = new SSOConfig();
        }
        return SSO_CONFIG;
    }

    public static SSOConfig init(SSOConfig ssoConfig) {
        SSO_CONFIG = ssoConfig;
        return ssoConfig;
    }

    public static String getSSOEncoding() {
        return getInstance().getEncoding();
    }

    public String getRsaCertstore() {
        if (null == rsaCertstore) {
            throw new KissoException("public.cert not found");
        }
        return rsaCertstore;
    }

    public String getRsaKeystore() {
        if (null == rsaKeystore) {
            throw new KissoException("jwt.jks not found");
        }
        return rsaKeystore;
    }


    public SSOAuthorization getAuthorization() {
        return SSO_AUTHORIZATION;
    }

    public SSOConfig setAuthorization(SSOAuthorization ssoAuthorization) {
        SSO_AUTHORIZATION = ssoAuthorization;
        return this;
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
