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
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * SSO Token
 * </p>
 *
 * @author hubin
 * @since 2017-07-17
 */
@Getter
@Slf4j
public class SSOToken extends AccessToken {
    private TokenFlag flag = TokenFlag.NORMAL;
    private TokenOrigin origin = TokenOrigin.COOKIE;
    /**
     * 主键
     */
    private String id;
    /**
     * 租户 ID
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
    private long time;
    /**
     * 请求头信息
     */
    private String userAgent;
    /**
     * 预留扩展参数
     */
    private Map<String, Object> data;

    public SSOToken() {
        // TO DO NOTHING
    }

    public static SSOToken create() {
        return new SSOToken().time(System.currentTimeMillis());
    }

    @Override
    public String getToken() {
        JwtBuilder jwtBuilder = Jwts.builder();
        if (null != id) {
            jwtBuilder.id(id);
        }
        if (null != tenantId) {
            jwtBuilder.claim(SSOConstants.TOKEN_TENANT_ID, tenantId);
        }
        if (null != ip) {
            jwtBuilder.claim(SSOConstants.TOKEN_USER_IP, ip);
        }
        if (null != issuer) {
            jwtBuilder.issuer(issuer);
        }
        if (null != userAgent) {
            jwtBuilder.claim(SSOConstants.TOKEN_USER_AGENT, userAgent);
        }
        if (TokenFlag.NORMAL != this.getFlag()) {
            jwtBuilder.claim(SSOConstants.TOKEN_FLAG, this.getFlag().value());
        }
        if (null != origin) {
            jwtBuilder.claim(SSOConstants.TOKEN_ORIGIN, origin.value());
        }
        jwtBuilder.issuedAt(new Date(time));
        if (null != data) {
            data.forEach(jwtBuilder::claim);
        }
        return JwtHelper.signCompact(jwtBuilder);
    }

    public SSOToken flag(TokenFlag flag) {
        this.flag = flag;
        return this;
    }

    public SSOToken origin(TokenOrigin origin) {
        this.origin = origin;
        return this;
    }

    public SSOToken id(Object id) {
        this.id = String.valueOf(id);
        return this;
    }

    public SSOToken id(String id) {
        this.id = id;
        return this;
    }

    public SSOToken tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public SSOToken tenantId(Object tenantId) {
        this.tenantId = String.valueOf(tenantId);
        return this;
    }

    public SSOToken issuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public SSOToken ip(HttpServletRequest request) {
        this.ip = IpHelper.getIpAddr(request);
        return this;
    }

    public SSOToken ip(String ip) {
        this.ip = ip;
        return this;
    }

    public SSOToken userAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public SSOToken userAgent(HttpServletRequest request) {
        this.userAgent = Browser.getUserAgent(request);
        return this;
    }

    public SSOToken time(long time) {
        this.time = time;
        return this;
    }

    public SSOToken data(Map<String, Object> data) {
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
        Claims claims = JwtHelper.verifyParser().parseSignedClaims(jwtToken).getPayload();
        if (null == claims) {
            return null;
        }
        String origin = claims.get(SSOConstants.TOKEN_ORIGIN, String.class);
        if (header && StringUtils.isEmpty(origin)) {
            log.warn("illegal token request origin.");
            return null;
        }
        SSOToken ssoToken = SSOToken.create().id(claims.getId()).issuer(claims.getIssuer());
        String ip = claims.get(SSOConstants.TOKEN_USER_IP, String.class);
        if (StringUtils.isNotEmpty(ip)) {
            ssoToken.ip(ip);
        }
        String userAgent = claims.get(SSOConstants.TOKEN_USER_AGENT, String.class);
        if (StringUtils.isNotEmpty(userAgent)) {
            ssoToken.userAgent(userAgent);
        }
        String flag = claims.get(SSOConstants.TOKEN_FLAG, String.class);
        if (StringUtils.isNotEmpty(flag)) {
            ssoToken.flag(TokenFlag.fromValue(flag));
        }
        String tenantId = claims.get(SSOConstants.TOKEN_TENANT_ID, String.class);
        if (StringUtils.isNotEmpty(tenantId)) {
            ssoToken.tenantId(tenantId);
        }
        // TOKEN 来源
        if (StringUtils.isNotEmpty(origin)) {
            ssoToken.origin(TokenOrigin.fromValue(origin));
        }
        return ssoToken.time(claims.getIssuedAt().getTime())
                // claims 值转化为 data
                .data(new HashMap<>(claims));
    }
}
