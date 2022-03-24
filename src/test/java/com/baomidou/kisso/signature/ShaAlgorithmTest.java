/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baomidou.kisso.signature;

import com.baomidou.kisso.common.signature.ShaAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.baomidou.kisso.common.signature.ShaAlgorithm.*;

public class ShaAlgorithmTest {

    @Test
    public void portableName() throws Exception {
        Assertions.assertEquals("hmac-sha1", HMAC_SHA1.getPortableName());
        Assertions.assertEquals("hmac-sha224", HMAC_SHA224.getPortableName());
        Assertions.assertEquals("hmac-sha256", HMAC_SHA256.getPortableName());
        Assertions.assertEquals("hmac-sha384", HMAC_SHA384.getPortableName());
        Assertions.assertEquals("hmac-sha512", HMAC_SHA512.getPortableName());
        Assertions.assertEquals("rsa-sha1", RSA_SHA1.getPortableName());
        Assertions.assertEquals("rsa-sha256", RSA_SHA256.getPortableName());
        Assertions.assertEquals("rsa-sha384", RSA_SHA384.getPortableName());
        Assertions.assertEquals("rsa-sha512", RSA_SHA512.getPortableName());
        Assertions.assertEquals("dsa-sha1", DSA_SHA1.getPortableName());
        Assertions.assertEquals("dsa-sha224", DSA_SHA224.getPortableName());
        Assertions.assertEquals("dsa-sha256", DSA_SHA256.getPortableName());
        Assertions.assertEquals("ecdsa-sha1", ECDSA_SHA1.getPortableName());
        Assertions.assertEquals("ecdsa-sha256", ECDSA_SHA256.getPortableName());
        Assertions.assertEquals("ecdsa-sha384", ECDSA_SHA384.getPortableName());
        Assertions.assertEquals("ecdsa-sha512", ECDSA_SHA512.getPortableName());
    }

    @Test
    public void jvmNames() {
        Assertions.assertEquals("HmacSHA1", HMAC_SHA1.getJmvName());
        Assertions.assertEquals("HmacSHA224", HMAC_SHA224.getJmvName());
        Assertions.assertEquals("HmacSHA256", HMAC_SHA256.getJmvName());
        Assertions.assertEquals("HmacSHA384", HMAC_SHA384.getJmvName());
        Assertions.assertEquals("HmacSHA512", HMAC_SHA512.getJmvName());
        Assertions.assertEquals("SHA1withRSA", RSA_SHA1.getJmvName());
        Assertions.assertEquals("SHA256withRSA", RSA_SHA256.getJmvName());
        Assertions.assertEquals("SHA384withRSA", RSA_SHA384.getJmvName());
        Assertions.assertEquals("SHA512withRSA", RSA_SHA512.getJmvName());
        Assertions.assertEquals("SHA1withDSA", DSA_SHA1.getJmvName());
        Assertions.assertEquals("SHA224withDSA", DSA_SHA224.getJmvName());
        Assertions.assertEquals("SHA256withDSA", DSA_SHA256.getJmvName());
        Assertions.assertEquals("SHA1withECDSA", ECDSA_SHA1.getJmvName());
        Assertions.assertEquals("SHA256withECDSA", ECDSA_SHA256.getJmvName());
        Assertions.assertEquals("SHA384withECDSA", ECDSA_SHA384.getJmvName());
        Assertions.assertEquals("SHA512withECDSA", ECDSA_SHA512.getJmvName());
    }

    @Test
    public void getWithPortableName() throws Exception {
        for (final ShaAlgorithm algorithm : ShaAlgorithm.values()) {
            Assertions.assertEquals(algorithm, ShaAlgorithm.get(algorithm.getPortableName()));
        }
    }

    @Test
    public void getWithJvmName() throws Exception {
        for (final ShaAlgorithm algorithm : ShaAlgorithm.values()) {
            Assertions.assertEquals(algorithm, ShaAlgorithm.get(algorithm.getJmvName()));
        }
    }

    @Test
    public void getNotCaseSensitive() throws Exception {
        for (final ShaAlgorithm algorithm : ShaAlgorithm.values()) {
            Assertions.assertEquals(algorithm, ShaAlgorithm.get(algorithm.getJmvName().toLowerCase()));
            Assertions.assertEquals(algorithm, ShaAlgorithm.get(algorithm.getJmvName().toUpperCase()));

            Assertions.assertEquals(algorithm, ShaAlgorithm.get(algorithm.getPortableName().toLowerCase()));
            Assertions.assertEquals(algorithm, ShaAlgorithm.get(algorithm.getPortableName().toUpperCase()));
        }
    }

    @Test
    public void nonAlphaNumericsIgnored() throws Exception {
        for (final ShaAlgorithm algorithm : ShaAlgorithm.values()) {
            Assertions.assertEquals(algorithm, ShaAlgorithm.get(algorithm.getPortableName().replace("-", " :-./ ")));
            Assertions.assertEquals(algorithm, ShaAlgorithm.get(algorithm.getJmvName().replace("with", " -/with:.")));
        }
    }

    @Test
    public void unsupportedAlgorithmException() throws Exception {
        ShaAlgorithm.get("HmacMD256");
    }
}
