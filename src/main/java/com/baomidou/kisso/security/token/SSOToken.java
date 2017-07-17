/**
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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.common.Browser;
import com.baomidou.kisso.common.IpHelper;
import com.baomidou.kisso.common.SSOConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

/**
 * <p>
 * SSO Token
 * </p>
 *
 * @author hubin
 * @since 2017-07-17
 */
public class SSOToken extends JwtAccessToken {

    private int flag = SSOConstants.TOKEN_FLAG_NORMAL; // 状态标记
    private String id; // 主键
    private String ip; // IP 地址
    private String userAgent; // 请求头信息
    private String subject;
    private String audience;
    private String issuer;
    private String payload;
    private Date expiration;
    private Date notBefore;
    private Date issuedAt;
    private JwtBuilder jwtBuilder;


    public SSOToken() {
        // TO DO NOTHING
    }

    public static SSOToken create(Claims claims) {
        SSOToken ssoToken = new SSOToken();
//        claims.put(SSOConstants.TOKEN_USER_IP, ssoToken.getIp());
//        claims.put(SSOConstants., Browser.getUserAgent(request));
//        LocalDate currentTime = LocalDate.now();
//        Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
//        this.jwtBuilder = Jwts.builder()
        ssoToken.setJwtBuilder(Jwts.builder().setClaims(claims));
        return ssoToken;
    }

    public void build() {
        if (null != this.getId()) {
            this.jwtBuilder.setId(this.getId());
        }
        if (null != this.getIp()) {
            this.jwtBuilder.claim(SSOConstants.TOKEN_USER_IP, this.getIp());
        }
        if (null != this.getUserAgent()) {
            this.jwtBuilder.claim(SSOConstants.TOKEN_USER_AGENT, this.getUserAgent());
        }
        if (null != this.getSubject()) {
            this.jwtBuilder.setSubject(this.getSubject());
        }
        if (null != this.getAudience()) {
            this.jwtBuilder.setAudience(this.getAudience());
        }
        if (null != this.getIssuer()) {
            this.jwtBuilder.setIssuer(this.getIssuer());
        }
        if (null != this.getPayload()) {
            this.jwtBuilder.setPayload(this.getPayload());
        }
        if (null != this.getExpiration()) {
            this.jwtBuilder.setExpiration(this.getExpiration());
        }
        if (null != this.getNotBefore()) {
            this.jwtBuilder.setNotBefore(this.getNotBefore());
        }
        if (null != this.getIssuedAt()) {
            this.jwtBuilder.setIssuedAt(this.getIssuedAt());
        }

//                .setIssuer(settings.getTokenIssuer())
//                .setExpiration(Date.from(currentTime
//                        .plusMinutes(settings.getRefreshTokenExpTime())
//                        .atZone(ZoneId.systemDefault()).toInstant()))
//                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
        this.setToken(this.jwtBuilder.compact());
    }

    public int getFlag() {
        return flag;
    }

    public SSOToken setFlag(int flag) {
        this.flag = flag;
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

    public String getSubject() {
        return subject;
    }

    public SSOToken setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getAudience() {
        return audience;
    }

    public SSOToken setAudience(String audience) {
        this.audience = audience;
        return this;
    }

    public String getIssuer() {
        return issuer;
    }

    public SSOToken setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public String getPayload() {
        return payload;
    }

    public SSOToken setPayload(String payload) {
        this.payload = payload;
        return this;
    }

    public Date getExpiration() {
        return expiration;
    }

    public SSOToken setExpiration(Date expiration) {
        this.expiration = expiration;
        return this;
    }

    public Date getNotBefore() {
        return notBefore;
    }

    public SSOToken setNotBefore(Date notBefore) {
        this.notBefore = notBefore;
        return this;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public SSOToken setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
        return this;
    }


    public SSOToken setJwtBuilder(JwtBuilder jwtBuilder) {
        this.jwtBuilder = jwtBuilder;
        return this;
    }

    public String toCacheKey() {
        return SSOConfig.toCacheKey(this.getId());
    }

    public SSOToken parseToken(String jwtToken) {
        JwtParser jwtParser = Jwts.parser().setSigningKey(SECRET);
        Claims claims = jwtParser.parseClaimsJws(jwtToken).getBody();
        if (null == claims) {
            return null;
        }
        SSOToken ssoToken = new SSOToken();
        ssoToken.setId(claims.getId());
        ssoToken.setIp(String.valueOf(claims.get(SSOConstants.TOKEN_USER_IP)));
//        ssoToken.setUserAgent(claims)
        ssoToken.setSubject(claims.getSubject());
        ssoToken.setAudience(claims.getAudience());
        ssoToken.setIssuer(claims.getIssuer());
        ssoToken.setExpiration(claims.getExpiration());
        ssoToken.setNotBefore(claims.getNotBefore());
        ssoToken.setIssuedAt(claims.getIssuedAt());
        return ssoToken;
    }
}
