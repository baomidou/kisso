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

import java.security.Key;
import java.security.Provider;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.junit.Assert;
import org.junit.Test;

import com.baomidou.kisso.common.signature.Signature;
import com.baomidou.kisso.common.signature.Signer;
import com.baomidou.kisso.exception.MissingRequiredHeaderException;
import com.baomidou.kisso.exception.UnsupportedAlgorithmException;

public class SignerTest extends Assert {

    @Test
    public void validSigner() {
        final Signature signature = new Signature("hmac-key-1", "hmac-sha256", null, "content-length", "host", "date", "(request-target)");
        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        new Signer(key, signature);
    }

    @Test(expected = NullPointerException.class)
    public void nullKey() {
        final Signature signature = new Signature("hmac-key-1", "hmac-sha256", null, "content-length", "host", "date", "(request-target)");
        new Signer(null, signature);
    }

    @Test(expected = NullPointerException.class)
    public void nullSignature() {
        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        new Signer(key, null);
    }

    @Test(expected = UnsupportedAlgorithmException.class)
    public void unsupportedAlgorithm() {
        final Signature signature = new Signature("hmac-key-1", "should fail because of this", null, "content-length", "host", "date", "(request-target)");
        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        new Signer(key, signature);
    }

    @Test(expected = UnsupportedAlgorithmException.class)
    public void algoNotImplemented() {
        final Provider p = new Provider("Tribe", 1.0, "Only for mock") {{
            clear();
        }};

        final Signature signature = new Signature("hmac-key-1", "hmac-sha256", null, "content-length", "host", "date", "(request-target)");
        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        new Signer(key, signature, p);
    }

    /**
     * It is an intentional part of the design that the same Signer instance
     * can be reused on several HTTP Messages in a multi-threaded fashion
     * <p/>
     * Reuse is tested here
     * <p/>
     * TODO test threading
     */
    @Test
    public void testSign() throws Exception {

        final Signature signature = new Signature("hmac-key-1", "hmac-sha256", null, "content-length", "host", "date", "(request-target)");

        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        final Signer signer = new Signer(key, signature);

        {
            final String method = "GET";
            final String uri = "/foo/Bar";
            final Map<String, String> headers = new HashMap<String, String>() {
                {
                    put("Host", "example.org");
                    put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
                    put("Content-Type", "application/json");
                    put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=");
                    put("Accept", "*/*");
                    put("Content-Length", "18");
                }
            };
            final Signature signed = signer.sign(method, uri, headers);
            assertEquals("yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=", signed.getSignature());
        }

        { // method changed.  should get a different signature
            final String method = "PUT";
            final String uri = "/foo/Bar";
            final Map<String, String> headers = new HashMap<String, String>() {{
                put("Host", "example.org");
                put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
                put("Content-Type", "application/json");
                put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=");
                put("Accept", "*/*");
                put("Content-Length", "18");
            }};
            final Signature signed = signer.sign(method, uri, headers);
            assertEquals("DPIsA/PWeYjySmfjw2P2SLJXZj1szDOei/Hh8nTcaPo=", signed.getSignature());
        }

        { // only Digest changed.  not part of the signature, should have no effect
            final String method = "PUT";
            final String uri = "/foo/Bar";
            final Map<String, String> headers = new HashMap<String, String>() {{
                put("Host", "example.org");
                put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
                put("Content-Type", "application/json");
                put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu8DBPE=");
                put("Accept", "*/*");
                put("Content-Length", "18");

            }};
            final Signature signed = signer.sign(method, uri, headers);
            assertEquals("DPIsA/PWeYjySmfjw2P2SLJXZj1szDOei/Hh8nTcaPo=", signed.getSignature());
        }

        { // uri changed.  should get a different signature
            final String method = "PUT";
            final String uri = "/foo/bar";
            final Map<String, String> headers = new HashMap<String, String>() {{
                put("Host", "example.org");
                put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
                put("Content-Type", "application/json");
                put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu8DBPE=");
                put("Accept", "*/*");
                put("Content-Length", "18");

            }};
            final Signature signed = signer.sign(method, uri, headers);
            assertEquals("IWTDxmOoEJI67YxY3eDIRzxrsAtlYYCuGZxKlkUSYdA=", signed.getSignature());
        }
    }

    @Test
    public void defaultHeaderList() throws Exception {
        final Signature signature = new Signature("hmac-key-1", "hmac-sha256", null);

        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        final Signer signer = new Signer(key, signature);

        { // just date should be required
            final Map<String, String> headers = new HashMap<String, String>() {{
                put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
            }};

            final Signature signed = signer.sign("GET", "/foo/Bar", headers);
            assertEquals("WbB9VXuVdRt1LKQ5mDuT+tiaChn8R7WhdAWAY1lhKZQ=", signed.getSignature());
        }

        { // one second later
            final Map<String, String> headers = new HashMap<String, String>() {{
                put("Date", "Tue, 07 Jun 2014 20:51:36 GMT");
            }};

            final Signature signed = signer.sign("GET", "/foo/Bar", headers);
            assertEquals("kRkh0bV1wKZSXBgexUB+zlPU88/za5K/gk/F0Aikg7Q=", signed.getSignature());
        }

        { // adding other headers shouldn't matter
            final Map<String, String> headers = new HashMap<String, String>() {{
                put("Date", "Tue, 07 Jun 2014 20:51:36 GMT");
                put("Content-Type", "application/json");
                put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu8DBPE=");
                put("Accept", "*/*");
                put("Content-Length", "18");

            }};

            final Signature signed = signer.sign("GET", "/foo/Bar", headers);
            assertEquals("kRkh0bV1wKZSXBgexUB+zlPU88/za5K/gk/F0Aikg7Q=", signed.getSignature());
        }
    }

    @Test(expected = MissingRequiredHeaderException.class)
    public void missingDefaultHeader() throws Exception {
        final Signature signature = new Signature("hmac-key-1", "hmac-sha256", null);

        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        final Signer signer = new Signer(key, signature);

        final Map<String, String> headers = new HashMap<String, String>();
        signer.sign("GET", "/foo/Bar", headers);
    }

    @Test(expected = MissingRequiredHeaderException.class)
    public void missingExplicitHeader() throws Exception {
        final Signature signature = new Signature("hmac-key-1", "hmac-sha256", null, "date", "accept");

        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        final Signer signer = new Signer(key, signature);

        final Map<String, String> headers = new HashMap<String, String>(){{
            put("Date", "Tue, 07 Jun 2014 20:51:36 GMT");
        }};
        signer.sign("GET", "/foo/Bar", headers);
    }

    @Test
    public void testSign1() throws Exception {
        final Signature signature = new Signature("hmac-key-1", "hmac-sha256", null, "content-length", "host", "date", "(request-target)");

        final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
        final Signer signer = new Signer(key, signature);

        final Map<String, String> headers = new HashMap<String, String>() {{
            put("Host", "example.org");
            put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
            put("Content-Type", "application/json");
            put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=");
            put("Accept", "*/*");
            put("Content-Length", "18");
        }};

        { // Assert the Signing String

            final String signingString = "" +
                    "content-length: 18\n" +
                    "host: example.org\n" +
                    "date: Tue, 07 Jun 2014 20:51:35 GMT\n" +
                    "(request-target): get /foo/Bar";

            assertEquals(signingString, signer.createSigningString("GET", "/foo/Bar", headers));
        }

        { // Assert the signature

            final String encodedSignature = signer.sign("GET", "/foo/Bar", headers).getSignature();

            assertEquals("yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=", encodedSignature);
        }
    }

    @Test
    public void testCreateSingingString() throws Exception {
        {
            final String method = "POST";
            final String uri = "/foo";
            final Map<String, String> headers = new HashMap<String, String>() {{
                put("Host", "example.org");
                put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
                put("Content-Type", "application/json");
                put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=");
                put("Accept", "*/*");
                put("Content-Length", "18");
            }};

            final Signature signature = new Signature("hmac-key-1", "hmac-sha256", null, "(request-target)", "host", "date", "digest", "content-length");
            final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
            final Signer signer = new Signer(key, signature);

            final String string = signer.createSigningString(method, uri, headers);
            assertEquals("(request-target): post /foo\n" +
                    "host: example.org\n" +
                    "date: Tue, 07 Jun 2014 20:51:35 GMT\n" +
                    "digest: SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=\n" +
                    "content-length: 18", string);
        }

        {
            final String method = "GET";
            final String uri = "/foo/Bar";
            final Map<String, String> headers = new HashMap<String, String>() {{
                put("Host", "example.org");
                put("Date", "Tue, 07 Jun 2014 20:51:35 GMT");
                put("Content-Type", "application/json");
                put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=");
                put("Accept", "*/*");
                put("Content-Length", "18");
            }};

            final Signature signature = new Signature("hmac-key-1", "hmac-sha256", null, "content-length", "host", "date", "(request-target)");
            final Key key = new SecretKeySpec("don't tell".getBytes(), "HmacSHA256");
            final Signer signer = new Signer(key, signature);

            final String string = signer.createSigningString(method, uri, headers);
            assertEquals("content-length: 18\n" +
                            "host: example.org\n" +
                            "date: Tue, 07 Jun 2014 20:51:35 GMT\n" +
                            "(request-target): get /foo/Bar"
                    , string);
        }
    }


}
