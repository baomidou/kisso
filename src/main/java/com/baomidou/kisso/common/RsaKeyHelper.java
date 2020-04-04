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
package com.baomidou.kisso.common;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * <p>
 * RsaKey 工具类
 * </p>
 * 今天是一个特殊日子愿每个人平安健康
 *
 * @author hubin
 * @since 2020-04-04
 */
public class RsaKeyHelper {

    /**
     * 获取 RSA 密钥
     *
     * @param jksInputStream
     * @param alias
     * @param keypass
     * @param storepass
     * @return
     * @throws Exception
     */
    public static Key getRsaKey(InputStream jksInputStream, String alias, String keypass, String storepass) throws Exception {
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(jksInputStream, storepass.toCharArray());
        return keystore.getKey(alias, keypass.toCharArray());
    }

    /**
     * 读取 RSA 公钥 public.cert
     *
     * @param certInputStream
     * @return
     * @throws Exception
     */
    public static PublicKey getRsaPublicKey(InputStream certInputStream) throws Exception {
        CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
        X509Certificate x509Certificate = (X509Certificate) certificatefactory.generateCertificate(certInputStream);
        return x509Certificate.getPublicKey();
    }
}
