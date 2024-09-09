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

import com.baomidou.kisso.common.RsaKeyHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.security.Key;
import java.security.PublicKey;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 测试 RSA 工具类
 * </p>
 *
 * @author hubin
 * @since 2020-04-04
 */
public class TestJwtRsa {

    @Test
    public void testRsa() throws Exception {
        SSOConfig ssoConfig = SSOConfig.getInstance();
        Key key = RsaKeyHelper.getRsaKey(new ClassPathResource(ssoConfig.getRsaJksStore()).getInputStream(),
                ssoConfig.getRsaAlias(), ssoConfig.getRsaKeypass(), ssoConfig.getRsaStorepass());
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", "cope");
        Calendar expires = Calendar.getInstance();
        expires.add(Calendar.HOUR, 2);

        // 加密
        String token = Jwts.builder()
                .claims(claims)
                .subject("test rsa jwt")
                .issuedAt(new Date())
                .expiration(expires.getTime())
                .signWith(key, SignatureAlgorithm.RS512)
                .compact();
        System.out.println(token);

        // CRT 证书中读取公钥解密
        PublicKey publicKey = RsaKeyHelper.getRsaPublicKey(new ClassPathResource(ssoConfig.getRsaCertStore()).getInputStream());
        Jws<Claims> crtClaimsJws = Jwts.parser().require("user", "cope")
                .setSigningKey(publicKey).build().parseClaimsJws(token);
        System.out.println("crt subject: " + crtClaimsJws.getBody().getSubject());
    }
}
