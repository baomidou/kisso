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

import com.baomidou.kisso.common.encrypt.base64.Base64;
import com.baomidou.kisso.exception.UnsupportedAlgorithmException;

import javax.crypto.Mac;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.PrivateKey;
import java.security.Provider;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * <p>
 * 签名者
 * </p>
 *
 * @author hubin
 * @since 2019-04-05
 */
public class Signer {

    private final ISign sign;
    private final Signature signature;
    private final ShaAlgorithm shaAlgorithm;
    private final Provider provider;

    public Signer(final Key key, final Signature signature) {
        this(key, signature, null);
    }

    public Signer(final Key key, final Signature signature, final Provider provider) {
        requireNonNull(key, "Key cannot be null");
        this.signature = requireNonNull(signature, "Signature cannot be null");
        this.shaAlgorithm = signature.getShaAlgorithm();
        this.provider = provider;

        if (java.security.Signature.class.equals(shaAlgorithm.getType())) {
            this.sign = new SignAsymmetric(PrivateKey.class.cast(key));
        } else if (Mac.class.equals(shaAlgorithm.getType())) {
            this.sign = new SignSymmetric(key);
        } else {
            throw new UnsupportedAlgorithmException(String.format("Unknown Algorithm type %s %s",
                    shaAlgorithm.getPortableName(), shaAlgorithm.getType().getName()));
        }

        // check that the JVM really knows the algorithm we are going to use
        try {
            sign.sign(provider, shaAlgorithm, "validation".getBytes());
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new IllegalStateException("Can't initialise the Signer using the provided algorithm and key", e);
        }
    }

    public Signature sign(final String method, final String uri, final Map<String, String> headers) throws IOException {
        final String signingString = createSigningString(method, uri, headers);
        final byte[] binarySignature = sign.sign(provider, shaAlgorithm, signingString.getBytes(StandardCharsets.UTF_8));
        final byte[] encoded = Base64.encode(binarySignature);
        final String signedAndEncodedString = new String(encoded, StandardCharsets.UTF_8);
        return new Signature(signature.getKeyId(), signature.getShaAlgorithm(), signedAndEncodedString, signature.getHeaders());
    }

    public String createSigningString(final String method, final String uri, final Map<String, String> headers) throws IOException {
        return Signature.createSigningString(signature.getHeaders(), method, uri, headers);
    }
}
