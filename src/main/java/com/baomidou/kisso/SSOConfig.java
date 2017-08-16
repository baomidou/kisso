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
package com.baomidou.kisso;

import java.util.List;

import com.baomidou.kisso.common.SSOConstants;
import com.baomidou.kisso.exception.KissoException;
import com.baomidou.kisso.security.token.SSOToken;

/**
 * <p>
 * SSO 配置文件解析
 * </p>
 *
 * @author hubin
 * @since 2015-12-05
 */
public class SSOConfig {

    private String encoding = SSOConstants.ENCODING;
    private String signkey = "3QD2j1B1s6Uj1jx6q8";
    private String signAlgorithm = "HS512";
    private String rsaCertstore; // 公钥 public.cert
    private String rsaKeystore; // 私钥 jwt.jks
    private String rsaAlias = "jwtkey";
    private String rsaKeypass = "keypassword";
    private String rsaStorepass = "letkisso";
    private String accessTokenName = "accessToken";
    private String cookieName = "uid";
    private String cookieDomain = "";
    private String cookiePath = "/";
    private boolean cookieSecure = false;
    private boolean cookieHttponly = true;
    private int cookieMaxage = -1;
    private boolean cookieBrowser = false;
    private boolean cookieCheckip = false;
    private String loginUrl = "";
    private String logoutUrl = "";
    private String paramReturl = "ReturnURL";
    private int cacheExpires = -1;
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

    private static SSOAuthorization authorization;

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

    public String getEncoding() {
        return encoding;
    }

    public SSOConfig setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public String getSignkey() {
        return signkey;
    }

    public SSOConfig setSignkey(String signkey) {
        this.signkey = signkey;
        return this;
    }

    public String getSignAlgorithm() {
        return signAlgorithm;
    }

    public SSOConfig setSignAlgorithm(String signAlgorithm) {
        this.signAlgorithm = signAlgorithm;
        return this;
    }

    public String getRsaCertstore() {
        if (null == rsaCertstore) {
            throw new KissoException("public.cert not found");
        }
        return rsaCertstore;
    }

    public void setRsaCertstore(String rsaCertstore) {
        this.rsaCertstore = rsaCertstore;
    }

    public String getRsaKeystore() {
        if (null == rsaKeystore) {
            throw new KissoException("jwt.jks not found");
        }
        return rsaKeystore;
    }

    public void setRsaKeystore(String rsaKeystore) {
        this.rsaKeystore = rsaKeystore;
    }

    public String getRsaAlias() {
        return rsaAlias;
    }

    public void setRsaAlias(String rsaAlias) {
        this.rsaAlias = rsaAlias;
    }

    public String getRsaKeypass() {
        return rsaKeypass;
    }

    public void setRsaKeypass(String rsaKeypass) {
        this.rsaKeypass = rsaKeypass;
    }

    public String getRsaStorepass() {
        return rsaStorepass;
    }

    public void setRsaStorepass(String rsaStorepass) {
        this.rsaStorepass = rsaStorepass;
    }

    public String getCookieName() {
        return cookieName;
    }

    public String getAccessTokenName() {
        return accessTokenName;
    }

    public void setAccessTokenName(String accessTokenName) {
        this.accessTokenName = accessTokenName;
    }

    public SSOConfig setCookieName(String cookieName) {
        this.cookieName = cookieName;
        return this;
    }

    public String getCookieDomain() {
        return cookieDomain;
    }

    public SSOConfig setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
        return this;
    }

    public String getCookiePath() {
        return cookiePath;
    }

    public SSOConfig setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
        return this;
    }

    public boolean isCookieSecure() {
        return cookieSecure;
    }

    public SSOConfig setCookieSecure(boolean cookieSecure) {
        this.cookieSecure = cookieSecure;
        return this;
    }

    public boolean isCookieHttponly() {
        return cookieHttponly;
    }

    public SSOConfig setCookieHttponly(boolean cookieHttponly) {
        this.cookieHttponly = cookieHttponly;
        return this;
    }

    public int getCookieMaxage() {
        return cookieMaxage;
    }

    public SSOConfig setCookieMaxage(int cookieMaxage) {
        this.cookieMaxage = cookieMaxage;
        return this;
    }

    public boolean isCookieBrowser() {
        return cookieBrowser;
    }

    public SSOConfig setCookieBrowser(boolean cookieBrowser) {
        this.cookieBrowser = cookieBrowser;
        return this;
    }

    public boolean isCookieCheckip() {
        return cookieCheckip;
    }

    public SSOConfig setCookieCheckip(boolean cookieCheckip) {
        this.cookieCheckip = cookieCheckip;
        return this;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public SSOConfig setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
        return this;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public SSOConfig setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
        return this;
    }

    public String getParamReturl() {
        return paramReturl;
    }

    public SSOConfig setParamReturl(String paramReturl) {
        this.paramReturl = paramReturl;
        return this;
    }

    public int getCacheExpires() {
        return cacheExpires;
    }

    public SSOConfig setCacheExpires(int cacheExpires) {
        this.cacheExpires = cacheExpires;
        return this;
    }

    public boolean isPermissionUri() {
        return permissionUri;
    }

    public SSOConfig setPermissionUri(boolean permissionUri) {
        this.permissionUri = permissionUri;
        return this;
    }

    public SSOToken getSsoToken() {
        return ssoToken;
    }

    public SSOConfig setSsoToken(SSOToken ssoToken) {
        this.ssoToken = ssoToken;
        return this;
    }

    public List<SSOPlugin> getPluginList() {
        return pluginList;
    }

    public SSOConfig setPluginList(List<SSOPlugin> pluginList) {
        this.pluginList = pluginList;
        return this;
    }

    public SSOCache getCache() {
        return cache;
    }

    public SSOConfig setCache(SSOCache cache) {
        this.cache = cache;
        return this;
    }

    public SSOAuthorization getAuthorization() {
        return authorization;
    }

    public SSOConfig setAuthorization(SSOAuthorization ssoAuthorization) {
        this.authorization = authorization;
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
