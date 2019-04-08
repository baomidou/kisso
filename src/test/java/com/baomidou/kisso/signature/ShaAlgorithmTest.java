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

import static com.baomidou.kisso.common.signature.ShaAlgorithm.DSA_SHA1;
import static com.baomidou.kisso.common.signature.ShaAlgorithm.DSA_SHA224;
import static com.baomidou.kisso.common.signature.ShaAlgorithm.DSA_SHA256;
import static com.baomidou.kisso.common.signature.ShaAlgorithm.ECDSA_SHA1;
import static com.baomidou.kisso.common.signature.ShaAlgorithm.ECDSA_SHA256;
import static com.baomidou.kisso.common.signature.ShaAlgorithm.ECDSA_SHA384;
import static com.baomidou.kisso.common.signature.ShaAlgorithm.ECDSA_SHA512;
import static com.baomidou.kisso.common.signature.ShaAlgorithm.HMAC_SHA1;
import static com.baomidou.kisso.common.signature.ShaAlgorithm.HMAC_SHA224;
import static com.baomidou.kisso.common.signature.ShaAlgorithm.HMAC_SHA256;
import static com.baomidou.kisso.common.signature.ShaAlgorithm.HMAC_SHA384;
import static com.baomidou.kisso.common.signature.ShaAlgorithm.HMAC_SHA512;
import static com.baomidou.kisso.common.signature.ShaAlgorithm.RSA_SHA1;
import static com.baomidou.kisso.common.signature.ShaAlgorithm.RSA_SHA256;
import static com.baomidou.kisso.common.signature.ShaAlgorithm.RSA_SHA384;
import static com.baomidou.kisso.common.signature.ShaAlgorithm.RSA_SHA512;

import org.junit.Assert;
import org.junit.Test;

import com.baomidou.kisso.common.signature.ShaAlgorithm;
import com.baomidou.kisso.exception.UnsupportedAlgorithmException;

public class ShaAlgorithmTest extends Assert {

    @Test
    public void portableName() throws Exception {
        assertEquals("hmac-sha1", HMAC_SHA1.getPortableName());
        assertEquals("hmac-sha224", HMAC_SHA224.getPortableName());
        assertEquals("hmac-sha256", HMAC_SHA256.getPortableName());
        assertEquals("hmac-sha384", HMAC_SHA384.getPortableName());
        assertEquals("hmac-sha512", HMAC_SHA512.getPortableName());
        assertEquals("rsa-sha1", RSA_SHA1.getPortableName());
        assertEquals("rsa-sha256", RSA_SHA256.getPortableName());
        assertEquals("rsa-sha384", RSA_SHA384.getPortableName());
        assertEquals("rsa-sha512", RSA_SHA512.getPortableName());
        assertEquals("dsa-sha1", DSA_SHA1.getPortableName());
        assertEquals("dsa-sha224", DSA_SHA224.getPortableName());
        assertEquals("dsa-sha256", DSA_SHA256.getPortableName());
        assertEquals("ecdsa-sha1", ECDSA_SHA1.getPortableName());
        assertEquals("ecdsa-sha256", ECDSA_SHA256.getPortableName());
        assertEquals("ecdsa-sha384", ECDSA_SHA384.getPortableName());
        assertEquals("ecdsa-sha512", ECDSA_SHA512.getPortableName());
    }

    @Test
    public void jvmNames() {
        assertEquals("HmacSHA1", HMAC_SHA1.getJmvName());
        assertEquals("HmacSHA224", HMAC_SHA224.getJmvName());
        assertEquals("HmacSHA256", HMAC_SHA256.getJmvName());
        assertEquals("HmacSHA384", HMAC_SHA384.getJmvName());
        assertEquals("HmacSHA512", HMAC_SHA512.getJmvName());
        assertEquals("SHA1withRSA", RSA_SHA1.getJmvName());
        assertEquals("SHA256withRSA", RSA_SHA256.getJmvName());
        assertEquals("SHA384withRSA", RSA_SHA384.getJmvName());
        assertEquals("SHA512withRSA", RSA_SHA512.getJmvName());
        assertEquals("SHA1withDSA", DSA_SHA1.getJmvName());
        assertEquals("SHA224withDSA", DSA_SHA224.getJmvName());
        assertEquals("SHA256withDSA", DSA_SHA256.getJmvName());
        assertEquals("SHA1withECDSA", ECDSA_SHA1.getJmvName());
        assertEquals("SHA256withECDSA", ECDSA_SHA256.getJmvName());
        assertEquals("SHA384withECDSA", ECDSA_SHA384.getJmvName());
        assertEquals("SHA512withECDSA", ECDSA_SHA512.getJmvName());
    }

    @Test
    public void getWithPortableName() throws Exception {
        for (final ShaAlgorithm algorithm : ShaAlgorithm.values()) {
            assertEquals(algorithm, ShaAlgorithm.get(algorithm.getPortableName()));
        }
    }

    @Test
    public void getWithJvmName() throws Exception {
        for (final ShaAlgorithm algorithm : ShaAlgorithm.values()) {
            assertEquals(algorithm, ShaAlgorithm.get(algorithm.getJmvName()));
        }
    }

    @Test
    public void getNotCaseSensitive() throws Exception {
        for (final ShaAlgorithm algorithm : ShaAlgorithm.values()) {
            assertEquals(algorithm, ShaAlgorithm.get(algorithm.getJmvName().toLowerCase()));
            assertEquals(algorithm, ShaAlgorithm.get(algorithm.getJmvName().toUpperCase()));

            assertEquals(algorithm, ShaAlgorithm.get(algorithm.getPortableName().toLowerCase()));
            assertEquals(algorithm, ShaAlgorithm.get(algorithm.getPortableName().toUpperCase()));
        }
    }

    @Test
    public void nonAlphaNumericsIgnored() throws Exception {
        for (final ShaAlgorithm algorithm : ShaAlgorithm.values()) {
            assertEquals(algorithm, ShaAlgorithm.get(algorithm.getPortableName().replace("-", " :-./ ")));
            assertEquals(algorithm, ShaAlgorithm.get(algorithm.getJmvName().replace("with", " -/with:.")));
        }
    }

    @Test(expected = UnsupportedAlgorithmException.class)
    public void unsupportedAlgorithmException() throws Exception {
        ShaAlgorithm.get("HmacMD256");
    }
}
