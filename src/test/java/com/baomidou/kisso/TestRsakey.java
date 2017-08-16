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

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import com.baomidou.kisso.common.RsaKeyHelper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * <p>
 * RSA 密钥测试
 * </p>
 *
 * @author hubin
 * @since 2017-07-19
 */
public class TestRsakey {

    @Test
    public void test() throws Exception {
        ClassPathResource resource = new ClassPathResource("jwt.jks");
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(resource.getInputStream(), "letkisso".toCharArray());
        Key key = keystore.getKey("jwtkey", "keypassword".toCharArray());
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", "cope");
        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.RS512, key)
                .compact();
        System.out.println(jwtToken);

        ClassPathResource resourcePk = new ClassPathResource("public.cert");
        String publicKey;
        try {
            publicKey = new String(FileCopyUtils.copyToByteArray(resourcePk.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JwtParser jwtParser = Jwts.parser().setSigningKey(RsaKeyHelper.parsePublicKey(publicKey));
        Claims claimsPk = jwtParser.parseClaimsJws(jwtToken).getBody();
        Assert.assertNotNull(claimsPk);
        Assert.assertEquals(claimsPk.get("user"), "cope");
    }
}
