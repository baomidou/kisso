/*
 * Copyright (c) 2017-2020, hubin (jobob@qq.com).
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
package com.baomidou.kisso.security.token;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.common.Browser;
import com.baomidou.kisso.common.IpHelper;
import com.baomidou.kisso.common.SSOConstants;
import com.baomidou.kisso.common.util.StringUtils;
import com.baomidou.kisso.enums.TokenFlag;
import com.baomidou.kisso.enums.TokenOrigin;
import com.baomidou.kisso.security.JwtHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 * SSO Token
 * </p>
 *
 * @author hubin
 * @since 2017-07-17
 */
@Slf4j
public class SSOToken extends AccessToken {
    private TokenFlag flag = TokenFlag.NORMAL;
    private TokenOrigin origin = TokenOrigin.COOKIE;
    /**
     * 主键
     */
    private String id;
    /**
     * 主键
     */
    private String tenantId;
    /**
     * 发布者
     */
    private String issuer;
    /**
     * IP 地址
     */
    private String ip;
    /**
     * 创建日期
     */
    private long time = System.currentTimeMillis();
    /**
     * 请求头信息
     */
    private String userAgent;
    /**
     * 预留扩展、配合缓存使用
     */
    private Object data;
    private JwtBuilder jwtBuilder;
    private Claims claims;

    public SSOToken() {
        // TO DO NOTHING
    }

    public SSOToken(JwtBuilder jwtBuilder) {
        this.jwtBuilder = jwtBuilder;
    }

    public static SSOToken create() {
        return new SSOToken();
    }

    public static SSOToken create(JwtBuilder jwtBuilder) {
        return create().setJwtBuilder(jwtBuilder);
    }

    @Override
    public String getToken() {
        if (null == this.jwtBuilder) {
            this.jwtBuilder = Jwts.builder();
        }
        if (null != this.getId()) {
            this.jwtBuilder.setId(this.getId());
        }
        if (null != this.getTenantId()) {
            this.jwtBuilder.claim(SSOConstants.TOKEN_TENANT_ID, this.getTenantId());
        }
        if (null != this.getIp()) {
            this.jwtBuilder.claim(SSOConstants.TOKEN_USER_IP, this.getIp());
        }
        if (null != this.getIssuer()) {
            this.jwtBuilder.setIssuer(this.getIssuer());
        }
        if (null != this.getUserAgent()) {
            this.jwtBuilder.claim(SSOConstants.TOKEN_USER_AGENT, this.getUserAgent());
        }
        if (null != this.getClaims()) {
            this.jwtBuilder.setClaims(this.getClaims());
        }
        if (TokenFlag.NORMAL != this.getFlag()) {
            this.jwtBuilder.claim(SSOConstants.TOKEN_FLAG, this.getFlag().value());
        }
        if (TokenOrigin.COOKIE != this.getOrigin()) {
            this.jwtBuilder.claim(SSOConstants.TOKEN_ORIGIN, this.getOrigin().value());
        }
        this.jwtBuilder.setIssuedAt(new Date(time));
        return JwtHelper.signCompact(jwtBuilder);
    }

    public TokenFlag getFlag() {
        return flag;
    }

    public SSOToken setFlag(TokenFlag flag) {
        this.flag = flag;
        return this;
    }

    public TokenOrigin getOrigin() {
        return origin;
    }

    public SSOToken setOrigin(TokenOrigin origin) {
        this.origin = origin;
        return this;
    }

    public String getId() {
        return id;
    }

    public SSOToken setId(Object id) {
        this.id = String.valueOf(id);
        return this;
    }

    public SSOToken setId(String id) {
        this.id = id;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public SSOToken setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public SSOToken setTenantId(Object tenantId) {
        this.tenantId = String.valueOf(tenantId);
        return this;
    }

    public String getIssuer() {
        return issuer;
    }

    public SSOToken setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public SSOToken setIp(HttpServletRequest request) {
        this.ip = IpHelper.getIpAddr(request);
        return this;
    }

    public SSOToken setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public SSOToken setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public SSOToken setUserAgent(HttpServletRequest request) {
        this.userAgent = Browser.getUserAgent(request);
        return this;
    }

    public long getTime() {
        return time;
    }

    public SSOToken setTime(long time) {
        this.time = time;
        return this;
    }

    public Object getData() {
        return data;
    }

    public SSOToken setData(Object data) {
        this.data = data;
        return this;
    }

    public JwtBuilder getJwtBuilder() {
        return jwtBuilder;
    }

    // Jwts.builder()
    public SSOToken setJwtBuilder(JwtBuilder jwtBuilder) {
        this.jwtBuilder = jwtBuilder;
        return this;
    }

    public Claims getClaims() {
        return claims;
    }

    public SSOToken setClaims(Claims claims) {
        this.claims = claims;
        return this;
    }

    public String toCacheKey() {
        return SSOConfig.toCacheKey(this.getId());
    }

    public static SSOToken parser(String jwtToken) {
        return parser(jwtToken, false);
    }

    public static SSOToken parser(String jwtToken, boolean header) {
        Claims claims = JwtHelper.verifyParser().parseClaimsJws(jwtToken).getBody();
        if (null == claims) {
            return null;
        }
        String origin = claims.get(SSOConstants.TOKEN_ORIGIN, String.class);
        if (header && StringUtils.isEmpty(origin)) {
            log.warn("illegal token request orgin.");
            return null;
        }
        SSOToken ssoToken = new SSOToken();
        ssoToken.setId(claims.getId());
        ssoToken.setIssuer(claims.getIssuer());
        String ip = claims.get(SSOConstants.TOKEN_USER_IP, String.class);
        if (StringUtils.isNotEmpty(ip)) {
            ssoToken.setIp(ip);
        }
        String userAgent = claims.get(SSOConstants.TOKEN_USER_AGENT, String.class);
        if (StringUtils.isNotEmpty(userAgent)) {
            ssoToken.setUserAgent(userAgent);
        }
        String flag = claims.get(SSOConstants.TOKEN_FLAG, String.class);
        if (StringUtils.isNotEmpty(flag)) {
            ssoToken.setFlag(TokenFlag.fromValue(flag));
        }
        String tenantId = claims.get(SSOConstants.TOKEN_TENANT_ID, String.class);
        if (StringUtils.isNotEmpty(tenantId)) {
            ssoToken.setTenantId(tenantId);
        }
        // TOKEN 来源
        if (StringUtils.isNotEmpty(origin)) {
            ssoToken.setOrigin(TokenOrigin.fromValue(origin));
        }
        ssoToken.setTime(claims.getIssuedAt().getTime());
        ssoToken.setClaims(claims);
        return ssoToken;
    }
}
