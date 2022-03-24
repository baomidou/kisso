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

import com.baomidou.kisso.common.signature.Signature;
import com.baomidou.kisso.common.signature.Verifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Provider;
import java.util.HashMap;
import java.util.Map;

public class VerifierTest {

    @Test
    public void validVerifier() {
        final Signature signature = new Signature("hmac-key-1", "hmac-sha256", null, "content-length", "host", "date", "(request-target)");
        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        new Verifier(key, signature);
    }

    @Test
    public void nullKey() {
        final Signature signature = new Signature("hmac-key-1", "hmac-sha256", null, "content-length", "host", "date", "(request-target)");
        new Verifier(null, signature);
    }

    @Test
    public void nullSignature() {
        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        new Verifier(key, null);
    }

    @Test
    public void unsupportedAlgorithm() {
        final Signature signature = new Signature("hmac-key-1", "should fail because of this", null, "content-length", "host", "date", "(request-target)");
        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        new Verifier(key, signature);
    }

    @Test
    public void algoNotImplemented() {
        final Provider p = new Provider("Tribe", 1.0, "Only for mock") {{
            clear();
        }};

        final Signature signature = new Signature("hmac-key-1", "hmac-sha256", null, "content-length", "host", "date", "(request-target)");
        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        new Verifier(key, signature, p);
    }

    /**
     * It is an intentional part of the design that the same Signer instance
     * can be reused on several HTTP Messages in a multi-threaded fashion
     * <p/>
     * Reuse is tested here
     * <p/>
     */
    @Test
    public void testVerifyThreading() throws Exception {

        final String authorization = "Signature keyId=\"hmac-key-1\",shaAlgorithm=\"hmac-sha256\",headers=\"content-length host date (request-target)\",signature=\"yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=\"";
        final Signature signature = Signature.fromString(authorization);

        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        final Verifier verifier = new Verifier(key, signature);

        {
            final String method = "GET";
            final String uri = "/foo/Bar";
            final Map<String, String> headers = new HashMap<String, String>();
            headers.put("Host", "example.org");
            headers.put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
            headers.put("Content-Type", "application/json");
            headers.put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=");
            headers.put("Accept", "*/*");
            headers.put("Content-Length", "18");
            boolean verifies = verifier.verify(method, uri, headers);
            Assertions.assertTrue(verifies);
        }

        { // method changed.  should get a different signature
            final String method = "PUT";
            final String uri = "/foo/Bar";
            final Map<String, String> headers = new HashMap<String, String>();
            headers.put("Host", "example.org");
            headers.put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
            headers.put("Content-Type", "application/json");
            headers.put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=");
            headers.put("Accept", "*/*");
            headers.put("Content-Length", "18");
            boolean verifies = verifier.verify(method, uri, headers);
            Assertions.assertFalse(verifies);
        }

        { // only Digest changed.  not part of the signature, should have no effect
            final String method = "GET";
            final String uri = "/foo/Bar";
            final Map<String, String> headers = new HashMap<String, String>();
            headers.put("Host", "example.org");
            headers.put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
            headers.put("Content-Type", "application/json");
            headers.put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu8DBPE=");
            headers.put("Accept", "*/*");
            headers.put("Content-Length", "18");
            boolean verifies = verifier.verify(method, uri, headers);
            Assertions.assertTrue(verifies);
        }

        { // uri changed.  should get a different signature
            final String method = "GET";
            final String uri = "/foo/bar";
            final Map<String, String> headers = new HashMap<String, String>();
            headers.put("Host", "example.org");
            headers.put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
            headers.put("Content-Type", "application/json");
            headers.put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu8DBPE=");
            headers.put("Accept", "*/*");
            headers.put("Content-Length", "18");
            boolean verifies = verifier.verify(method, uri, headers);
            Assertions.assertFalse(verifies);
        }
    }

    @Test
    public void defaultHeaderList() throws Exception {
        final String authorization = "Signature keyId=\"hmac-key-1\",shaAlgorithm=\"hmac-sha256\",headers=\"date\",signature=\"WbB9VXuVdRt1LKQ5mDuT+tiaChn8R7WhdAWAY1lhKZQ=\"";
        final Signature signature = Signature.fromString(authorization);

        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        final Verifier verifier = new Verifier(key, signature);

        { // just date should be required
            final Map<String, String> headers = new HashMap<String, String>();
            headers.put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");

            boolean verifies = verifier.verify("GET", "/foo/Bar", headers);
            Assertions.assertTrue(verifies);
        }

        { // adding other headers shouldn't matter
            final Map<String, String> headers = new HashMap<String, String>();
            headers.put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
            headers.put("Content-Type", "application/json");
            headers.put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu8DBPE=");
            headers.put("Accept", "*/*");
            headers.put("Content-Length", "18");

            boolean verifies = verifier.verify("GET", "/foo/Bar", headers);
            Assertions.assertTrue(verifies);
        }
    }

    @Test
    public void missingDefaultHeader() throws Exception {
        final String authorization = "Signature keyId=\"hmac-key-1\",shaAlgorithm=\"hmac-sha256\",headers=\"\",signature=\"WbB9VXuVdRt1LKQ5mDuT+tiaChn8R7WhdAWAY1lhKZQ=\"";
        final Signature signature = Signature.fromString(authorization);

        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        final Verifier verifier = new Verifier(key, signature);

        final Map<String, String> headers = new HashMap<String, String>();
        verifier.verify("GET", "/foo/Bar", headers);
    }

    @Test
    public void missingExplicitHeader() throws Exception {
        final String authorization = "Signature keyId=\"hmac-key-1\",shaAlgorithm=\"hmac-sha256\",headers=\"date accept\",signature=\"WbB9VXuVdRt1LKQ5mDuT+tiaChn8R7WhdAWAY1lhKZQ=\"";
        final Signature signature = Signature.fromString(authorization);

        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        final Verifier verifier = new Verifier(key, signature);

        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("Date", "Tue, 07 Jun 2014 20:51:36 GMT");
        verifier.verify("GET", "/foo/Bar", headers);
    }

    @Test
    public void testVerify() throws Exception {
        final String authorization = "Signature keyId=\"hmac-key-1\",shaAlgorithm=\"hmac-sha256\",headers=\"content-length host date (request-target)\",signature=\"yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=\"";
        final Signature signature = Signature.fromString(authorization);

        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        final Verifier verifier = new Verifier(key, signature);

        //final Signature signature = new Signature("hmac-key-1", "hmac-sha256", null, "content-length", "host", "date", "(request-target)");

        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("Host", "example.org");
        headers.put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
        headers.put("Content-Type", "application/json");
        headers.put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=");
        headers.put("Accept", "*/*");
        headers.put("Content-Length", "18");

        { // Assert the Signing String

            final String signingString = "" +
                    "content-length: 18\n" +
                    "host: example.org\n" +
                    "date: Tue, 07 Jun 2014 20:51:35 GMT\n" +
                    "(request-target): get /foo/Bar";

            Assertions.assertEquals(signingString, verifier.createSigningString("GET", "/foo/Bar", headers));
        }

    }

    @Test
    public void testCreateSingingString() throws Exception {
        {
            final String method = "POST";
            final String uri = "/foo";
            final Map<String, String> headers = new HashMap<String, String>();
            headers.put("Host", "example.org");
            headers.put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
            headers.put("Content-Type", "application/json");
            headers.put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=");
            headers.put("Accept", "*/*");
            headers.put("Content-Length", "18");

            final String authorization = "Signature keyId=\"hmac-key-1\",shaAlgorithm=\"hmac-sha256\",headers=\"(request-target) host date digest content-length\",signature=\"yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=\"";
            final Signature signature = Signature.fromString(authorization);

            final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
            final Verifier verifier = new Verifier(key, signature);

            final String string = verifier.createSigningString(method, uri, headers);
            Assertions.assertEquals("(request-target): post /foo\n" +
                    "host: example.org\n" +
                    "date: Tue, 07 Jun 2014 20:51:35 GMT\n" +
                    "digest: SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=\n" +
                    "content-length: 18", string);
        }

        {
            final String method = "GET";
            final String uri = "/foo/Bar";
            final Map<String, String> headers = new HashMap<String, String>();
            headers.put("Host", "example.org");
            headers.put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
            headers.put("Content-Type", "application/json");
            headers.put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=");
            headers.put("Accept", "*/*");
            headers.put("Content-Length", "18");

            final String authorization = "Signature keyId=\"hmac-key-1\",shaAlgorithm=\"hmac-sha256\",headers=\"content-length host date (request-target)\",signature=\"yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=\"";
            final Signature signature = Signature.fromString(authorization);

            final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
            final Verifier verifier = new Verifier(key, signature);

            final String string = verifier.createSigningString(method, uri, headers);
            Assertions.assertEquals("content-length: 18\n" +
                            "host: example.org\n" +
                            "date: Tue, 07 Jun 2014 20:51:35 GMT\n" +
                            "(request-target): get /foo/Bar"
                    , string);
        }
    }
}
