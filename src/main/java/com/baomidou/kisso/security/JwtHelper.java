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
package com.baomidou.kisso.security;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.common.RsaKeyHelper;
import com.baomidou.kisso.exception.KissoException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.util.Base64;

/**
 * <p>
 * SSO Token 签名验证辅助类
 * </p>
 *
 * @author hubin
 * @since 2017-07-19
 */
public class JwtHelper {
    /**
     * RSA 密钥
     */
    private static Key RSA_KEY;
    /**
     * RSA 公钥
     */
    private static PublicKey RSA_PUBLICKEY;

    /**
     * 获取一个随机的 HS512 算法签名密钥
     */
    public static String getHS512SecretKey() {
        return getSecretKey(Jwts.SIG.HS512.key().build());
    }

    /**
     * 获取对应签名算法的字符串密钥
     *
     * @param key {@link Key}
     */
    public static String getSecretKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static SecretKey getSecretKey(SSOConfig config) {
        return Keys.hmacShaKeyFor(config.getSignKey().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * <p>
     * 签名并生成 Token
     * </p>
     */
    public static String signCompact(JwtBuilder jwtBuilder) {
        SSOConfig config = SSOConfig.getInstance();
        if (config.rsa()) {
            try {
                if(null == RSA_KEY) {
                    ClassPathResource resource = new ClassPathResource(config.getRsaJksStore());
                    KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                    keystore.load(resource.getInputStream(), config.getRsaStorepass().toCharArray());
                    RSA_KEY = keystore.getKey(config.getRsaAlias(), config.getRsaKeypass().toCharArray());
                }
                // RSA 签名
                return jwtBuilder.signWith(RSA_KEY).compact();
            } catch (Exception e) {
                throw new KissoException("signCompact error.", e);
            }
        }
        // 普通签名
        return jwtBuilder.signWith(getSecretKey(config)).compact();
    }


    /**
     * <p>
     * 验证签名并解析
     * </p>
     */
    public static JwtParser verifyParser() {
        try {
            SSOConfig config = SSOConfig.getInstance();
            if (config.rsa()) {
                if(null == RSA_PUBLICKEY) {
                    ClassPathResource resource = new ClassPathResource(config.getRsaCertStore());
                    RSA_PUBLICKEY = RsaKeyHelper.getRsaPublicKey(resource.getInputStream());
                }
                // RSA 签名验证
                return Jwts.parser().verifyWith(RSA_PUBLICKEY).build();
            }
            // 普通签名验证
            return Jwts.parser().verifyWith(getSecretKey(config)).build();
        } catch (Exception e) {
            throw new KissoException("verifyParser error.", e);
        }
    }
}
