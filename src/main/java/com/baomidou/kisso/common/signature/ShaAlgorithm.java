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
package com.baomidou.kisso.common.signature;

import javax.crypto.Mac;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.kisso.exception.UnsupportedAlgorithmException;

/**
 * <p>
 * 签名算法
 * </p>
 *
 * @author hubin
 * @since 2019-04-05
 */
public enum ShaAlgorithm {

    // hmac
    HMAC_SHA1("HmacSHA1", "hmac-sha1", Mac.class),
    HMAC_SHA224("HmacSHA224", "hmac-sha224", Mac.class),
    HMAC_SHA256("HmacSHA256", "hmac-sha256", Mac.class),
    HMAC_SHA384("HmacSHA384", "hmac-sha384", Mac.class),
    HMAC_SHA512("HmacSHA512", "hmac-sha512", Mac.class),

    // rsa
    RSA_SHA1("SHA1withRSA", "rsa-sha1", java.security.Signature.class),
    RSA_SHA256("SHA256withRSA", "rsa-sha256", java.security.Signature.class),
    RSA_SHA384("SHA384withRSA", "rsa-sha384", java.security.Signature.class),
    RSA_SHA512("SHA512withRSA", "rsa-sha512", java.security.Signature.class),

    // dsa
    DSA_SHA1("SHA1withDSA", "dsa-sha1", java.security.Signature.class),
    DSA_SHA224("SHA224withDSA", "dsa-sha224", java.security.Signature.class),
    DSA_SHA256("SHA256withDSA", "dsa-sha256", java.security.Signature.class),

    // ecc
    ECDSA_SHA1("SHA1withECDSA", "ecdsa-sha1", java.security.Signature.class),
    ECDSA_SHA256("SHA256withECDSA", "ecdsa-sha256", java.security.Signature.class),
    ECDSA_SHA384("SHA384withECDSA", "ecdsa-sha384", java.security.Signature.class),
    ECDSA_SHA512("SHA512withECDSA", "ecdsa-sha512", java.security.Signature.class),
    ;

    private static final Map<String, ShaAlgorithm> aliases = new HashMap<String, ShaAlgorithm>();

    static {
        for (final ShaAlgorithm algorithm : ShaAlgorithm.values()) {
            aliases.put(normalize(algorithm.getJmvName()), algorithm);
            aliases.put(normalize(algorithm.getPortableName()), algorithm);
        }
    }

    private final String portableName;
    private final String jmvName;
    private final Class type;

    ShaAlgorithm(final String jmvName, final String portableName, final Class type) {
        this.portableName = portableName;
        this.jmvName = jmvName;
        this.type = type;
    }

    public String getPortableName() {
        return portableName;
    }

    public String getJmvName() {
        return jmvName;
    }

    public Class getType() {
        return type;
    }

    public static String toPortableName(final String name) {
        return get(name).getPortableName();
    }

    public static String toJvmName(final String name) {
        return get(name).getJmvName();
    }

    public static ShaAlgorithm get(String name) {
        final ShaAlgorithm algorithm = aliases.get(normalize(name));

        if (algorithm != null) {
            return algorithm;
        }

        throw new UnsupportedAlgorithmException(name);
    }

    private static String normalize(String algorithm) {
        return algorithm.replaceAll("[^A-Za-z0-9]+", "").toLowerCase();
    }


    @Override
    public String toString() {
        return getPortableName();
    }
}
