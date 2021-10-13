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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Map;

import javax.crypto.Mac;

import com.baomidou.kisso.exception.UnsupportedAlgorithmException;

/**
 * <p>
 * 验签者
 * </p>
 *
 * @since hubin
 * @since 2019-04-05
 */
public class Verifier {
    private final IVerify verify;
    private final Signature signature;
    private final ShaAlgorithm shaAlgorithm;
    private final Provider provider;

    public Verifier(final Key key, final Signature signature) {
        this(key, signature, null);
    }

    public Verifier(final Key key, final Signature signature, final Provider provider) {
        requireNonNull(key, "Key cannot be null");
        this.signature = requireNonNull(signature, "Signature cannot be null");
        this.shaAlgorithm = signature.getShaAlgorithm();
        this.provider = provider;
        if (java.security.Signature.class.equals(shaAlgorithm.getType())) {
            this.verify = new VerifyAsymmetric(PublicKey.class.cast(key));
        } else if (Mac.class.equals(shaAlgorithm.getType())) {
            this.verify = new VerifySymmetric(key);
        } else {
            throw new UnsupportedAlgorithmException(String.format("Unknown Algorithm type %s %s",
                    shaAlgorithm.getPortableName(), shaAlgorithm.getType().getName()));
        }

        // check that the JVM really knows the algorithm we are going to use
        try {
            verify.verify(provider, shaAlgorithm, "validation".getBytes(), signature.getSignature());
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new IllegalStateException("Can't initialise the Signer using the provided algorithm and key", e);
        }
    }

    public boolean verify(final String method, final String uri, final Map<String, String> headers) throws IOException, NoSuchAlgorithmException, SignatureException {
        final String signingString = createSigningString(method, uri, headers);
        return verify.verify(provider, shaAlgorithm, signingString.getBytes(), signature.getSignature());
    }

    public String createSigningString(final String method, final String uri, final Map<String, String> headers) throws IOException {
        return Signature.createSigningString(signature.getHeaders(), method, uri, headers);
    }
}
