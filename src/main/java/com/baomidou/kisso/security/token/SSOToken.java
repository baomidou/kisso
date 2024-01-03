/*
 * Copyright (c) 2017-2021, hubin (jobob@qq.com).
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
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

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
    @Getter
    private TokenFlag flag = TokenFlag.NORMAL;
    @Getter
    private TokenOrigin origin = TokenOrigin.COOKIE;
    /**
     * 主键
     */
    @Getter
    private String id;
    /**
     * 租户 ID
     */
    @Getter
    private String tenantId;
    /**
     * 发布者
     */
    @Getter
    private String issuer;
    /**
     * IP 地址
     */
    @Getter
    private String ip;
    /**
     * 创建日期
     */
    @Getter
    private long time;
    /**
     * 请求头信息
     */
    @Getter
    private String userAgent;
    /**
     * 预留扩展参数
     */
    @Getter
    private Map<String, Object> data;

    public SSOToken() {
        // TO DO NOTHING
    }

    public static SSOToken create() {
        return new SSOToken().setTime(System.currentTimeMillis());
    }

    @Override
    public String getToken() {
        JwtBuilder jwtBuilder = Jwts.builder();
        if (null != this.getId()) {
            jwtBuilder.setId(this.getId());
        }
        if (null != this.getTenantId()) {
            jwtBuilder.claim(SSOConstants.TOKEN_TENANT_ID, this.getTenantId());
        }
        if (null != this.getIp()) {
            jwtBuilder.claim(SSOConstants.TOKEN_USER_IP, this.getIp());
        }
        if (null != this.getIssuer()) {
            jwtBuilder.setIssuer(this.getIssuer());
        }
        if (null != this.getUserAgent()) {
            jwtBuilder.claim(SSOConstants.TOKEN_USER_AGENT, this.getUserAgent());
        }
        if (TokenFlag.NORMAL != this.getFlag()) {
            jwtBuilder.claim(SSOConstants.TOKEN_FLAG, this.getFlag().value());
        }
        if (null != this.getOrigin()) {
            jwtBuilder.claim(SSOConstants.TOKEN_ORIGIN, this.getOrigin().value());
        }
        jwtBuilder.setIssuedAt(new Date(time));
        if (null != data) {
            data.forEach(jwtBuilder::claim);
        }
        return JwtHelper.signCompact(jwtBuilder);
    }

    public SSOToken setFlag(TokenFlag flag) {
        this.flag = flag;
        return this;
    }

    public SSOToken setOrigin(TokenOrigin origin) {
        this.origin = origin;
        return this;
    }

    public SSOToken setId(Object id) {
        this.id = String.valueOf(id);
        return this;
    }

    public SSOToken setId(String id) {
        this.id = id;
        return this;
    }

    public SSOToken setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public SSOToken setTenantId(Object tenantId) {
        this.tenantId = String.valueOf(tenantId);
        return this;
    }

    public SSOToken setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public SSOToken setIp(HttpServletRequest request) {
        this.ip = IpHelper.getIpAddr(request);
        return this;
    }

    public SSOToken setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public SSOToken setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public SSOToken setUserAgent(HttpServletRequest request) {
        this.userAgent = Browser.getUserAgent(request);
        return this;
    }

    public SSOToken setTime(long time) {
        this.time = time;
        return this;
    }

    public SSOToken setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public String toCacheKey() {
        return SSOConfig.toCacheKey(id, origin);
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
            log.warn("illegal token request origin.");
            return null;
        }
        SSOToken ssoToken = SSOToken.create();
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
        return ssoToken;
    }
}
