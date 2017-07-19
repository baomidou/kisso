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
package com.baomidou.kisso.security;

import java.security.Key;
import java.security.KeyStore;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.common.RsaKeyHelper;
import com.baomidou.kisso.common.SSOConstants;
import com.baomidou.kisso.exception.KissoException;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
     * <p>
     * 签名并生成 Token
     * </p>
     */
    public static String signCompact(JwtBuilder jwtBuilder) {
        SSOConfig config = SSOConfig.getInstance();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.forName(config.getSignAlgorithm());
        if (SSOConstants.SIGN_RSA.equals(signatureAlgorithm.getFamilyName())) {
            try {
                ClassPathResource resource = new ClassPathResource(config.getRsaKeystore());
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                keystore.load(resource.getInputStream(), config.getRsaStorepass().toCharArray());
                Key key = keystore.getKey(config.getRsaAlias(), config.getRsaKeypass().toCharArray());
                // RSA 签名
                return jwtBuilder.signWith(signatureAlgorithm, key).compact();
            } catch (Exception e) {
                throw new KissoException("signCompact error.", e);
            }
        }
        // 普通签名
        return jwtBuilder.signWith(signatureAlgorithm, config.getSignkey()).compact();
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
            if (SSOConstants.SIGN_RSA.equals(signatureAlgorithm.getFamilyName())) {
                ClassPathResource resource = new ClassPathResource(config.getRsaCertstore());
                String publicKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
                // RSA 签名验证
                return Jwts.parser().setSigningKey(RsaKeyHelper.parsePublicKey(publicKey));
            }
            // 普通签名验证
            return Jwts.parser().setSigningKey(config.getSignkey());
        } catch (Exception e) {
            throw new KissoException("verifyParser error.", e);
        }
    }
}
