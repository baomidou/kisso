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

import java.io.Serializable;
import java.util.UUID;

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
    private Serializable id; // 主键
    private String ip; // IP 地址
    private long time; // 创建 token 当前系统时间


    public SSOToken() {
        // TO DO NOTHING
    }

    public static SSOToken create(HttpServletRequest request, Serializable id, Claims claims) {
        SSOToken ssoToken = new SSOToken();
        ssoToken.setIp(IpHelper.getIpAddr(request));
        ssoToken.setId(id);
        ssoToken.setTime(System.currentTimeMillis());
        claims.put(SSOConstants.TOKEN_USER_IP, ssoToken.getIp());
        claims.put(SSOConstants.TOKEN_USER_AGENT, Browser.getUserAgent(request));
        claims.put(SSOConstants.TOKEN_CREATE_TIME, ssoToken.getTime());
        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(claims)
//                .setIssuer(settings.getTokenIssuer())
                .setId(UUID.randomUUID().toString());
//                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
//                .setExpiration(Date.from(currentTime
//                        .plusMinutes(settings.getRefreshTokenExpTime())
//                        .atZone(ZoneId.systemDefault()).toInstant()))
//                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
        ssoToken.setToken(jwtBuilder.compact());
        return ssoToken;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Serializable getId() {
        return id;
    }

    public void setId(Serializable id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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
        ssoToken.setId((Serializable) claims.get(SSOConstants.TOKEN_USER_ID));
        ssoToken.setIp(String.valueOf(claims.get(SSOConstants.TOKEN_USER_IP)));
        ssoToken.setTime(Long.valueOf(String.valueOf(claims.get(SSOConstants.TOKEN_CREATE_TIME))));
        return ssoToken;
    }
}
