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
package com.baomidou.kisso.security;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.common.RsaKeyHelper;
import com.baomidou.kisso.common.SSOConstants;
import com.baomidou.kisso.exception.KissoException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
     * 获取一个随机的 HS512 算法签名密钥
     *
     * @return
     */
    public static String getHS512SecretKey() {
        return getSecretKey(SignatureAlgorithm.HS512);
    }

    /**
     * 获取对应签名算法的字符串密钥
     *
     * @param signatureAlgorithm 签名算法
     * @return
     */
    public static String getSecretKey(SignatureAlgorithm signatureAlgorithm) {
        Key key = Keys.secretKeyFor(signatureAlgorithm);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 字符串密钥生成加密 Key
     *
     * @param signKey            密钥
     * @param signatureAlgorithm 签名算法
     * @return
     */
    public static SecretKey getSecretKey(String signKey, SignatureAlgorithm signatureAlgorithm) {
        return new SecretKeySpec(signKey.getBytes(), signatureAlgorithm.getJcaName());
    }

    /**
     * <p>
     * 签名并生成 Token
     * </p>
     */
    public static String signCompact(JwtBuilder jwtBuilder) {
        SSOConfig config = SSOConfig.getInstance();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.forName(config.getSignAlgorithm());
        if (SSOConstants.RSA.equals(signatureAlgorithm.getFamilyName())) {
            try {
                ClassPathResource resource = new ClassPathResource(config.getRsaJksStore());
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                keystore.load(resource.getInputStream(), config.getRsaStorepass().toCharArray());
                Key key = keystore.getKey(config.getRsaAlias(), config.getRsaKeypass().toCharArray());
                // RSA 签名
                return jwtBuilder.signWith(key, signatureAlgorithm).compact();
            } catch (Exception e) {
                throw new KissoException("signCompact error.", e);
            }
        }
        // 普通签名
        SecretKey secretKey = getSecretKey(config.getSignKey(), signatureAlgorithm);
        return jwtBuilder.signWith(secretKey, signatureAlgorithm).compact();
    }


    /**
     * <p>
     * 验证签名并解析
     * </p>
     */
    public static JwtParser verifyParser() {
        try {
            SSOConfig config = SSOConfig.getInstance();
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.forName(config.getSignAlgorithm());
            if (SSOConstants.RSA.equals(signatureAlgorithm.getFamilyName())) {
                ClassPathResource resource = new ClassPathResource(config.getRsaCertStore());
                PublicKey publicKey = RsaKeyHelper.getRsaPublicKey(resource.getInputStream());
                // RSA 签名验证
                return Jwts.parserBuilder().setSigningKey(publicKey).build();
            }
            // 普通签名验证
            SecretKey secretKey = getSecretKey(config.getSignKey(), signatureAlgorithm);
            return Jwts.parserBuilder().setSigningKey(secretKey).build();
        } catch (Exception e) {
            throw new KissoException("verifyParser error.", e);
        }
    }
}
