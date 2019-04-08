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

import static com.baomidou.kisso.common.util.JoinUtil.join;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.baomidou.kisso.common.signature.Signature;
import com.baomidou.kisso.exception.AuthenticationException;
import com.baomidou.kisso.exception.MissingAlgorithmException;
import com.baomidou.kisso.exception.MissingKeyIdException;
import com.baomidou.kisso.exception.MissingSignatureException;

public class SignatureTest {

    @Test
    public void validSignature() {
        new Signature("somekey", "hmac-sha256", "yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=", "date", "accept");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullKey() {
        new Signature(null, "hmac-sha256", "yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=", "date", "accept");
    }

    @Test
    public void nullSignature() {
        new Signature("somekey", "hmac-sha256", null, "date", "accept");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullAlgorithm() {
        new Signature("somekey", (String) null, "yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=", "date", "accept");
    }

    @Test
    public void nullHeaders() {
        final Signature signature = new Signature("somekey", "hmac-sha256", "yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=");
        assertEquals(1, signature.getHeaders().size()); // should contain at least the Date which is required
        assertEquals("date", signature.getHeaders().get(0).toLowerCase());
    }


    @Test
    public void roundTripTest() throws Exception {
        final Signature expected = new Signature("somekey", "hmac-sha256", "yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=", "date", "accept");
        final Signature actual = Signature.fromString(expected.toString());

        assertSignature(expected, actual);
    }

    @Test
    public void testFromString() throws Exception {
        String authorization = "Signature keyId=\"hmac-key-1\",shaAlgorithm=\"hmac-sha256\",\n" +
                "   headers=\"(request-target) host date digest content-length\",\n" +
                "   signature=\"yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=\"";

        final Signature signature = Signature.fromString(authorization);

        assertEquals("hmac-key-1", signature.getKeyId());
        assertEquals("hmac-sha256", signature.getShaAlgorithm().toString());
        assertEquals("yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=", signature.getSignature());
        assertEquals("(request-target)\n" +
                "host\n" +
                "date\n" +
                "digest\n" +
                "content-length", join("\n", signature.getHeaders()));
    }

    @Test
    public void testFromStringWithLdapDNKeyId() throws Exception {
        String authorization = "Signature keyId=\"UID=jsmith,DC=example,DC=net\",shaAlgorithm=\"hmac-sha256\",\n" +
                "   headers=\"(request-target) host date digest content-length\",\n" +
                "   signature=\"yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=\"";

        final Signature signature = Signature.fromString(authorization);

        assertEquals("UID=jsmith,DC=example,DC=net", signature.getKeyId());
    }

    /**
     * Authorization header parameters (keyId, algorithm, headers, signature)
     * may legally not include 'headers'
     */
    @Test
    public void noHeaders() throws Exception {
        String authorization = "Signature keyId=\"hmac-key-1\",shaAlgorithm=\"hmac-sha256\",\n" +
                "   signature=\"Base64(HMAC-SHA256(signing string))\"";

        final Signature signature = Signature.fromString(authorization);

        assertEquals("hmac-key-1", signature.getKeyId());
        assertEquals("hmac-sha256", signature.getShaAlgorithm().toString());
        assertEquals("Base64(HMAC-SHA256(signing string))", signature.getSignature());
        assertEquals("date", join("\n", signature.getHeaders()));
    }

    /**
     * Order in headers parameter must be retained
     */
    @Test
    public void headersOrder() throws Exception {
        String authorization = "Signature keyId=\"hmac-key-1\",shaAlgorithm=\"hmac-sha256\"," +
                "headers=\"one two three four five six\"" +
                ",signature=\"Base64(HMAC-SHA256(signing string))\"";

        final Signature signature = Signature.fromString(authorization);

        assertEquals("hmac-key-1", signature.getKeyId());
        assertEquals("hmac-sha256", signature.getShaAlgorithm().toString());
        assertEquals("Base64(HMAC-SHA256(signing string))", signature.getSignature());
        assertEquals("one\n" +
                "two\n" +
                "three\n" +
                "four\n" +
                "five\n" +
                "six", join("\n", signature.getHeaders()));
    }

    @Test
    public void noSignaturePrefix() throws Exception {
        String authorization = "keyId=\"hmac-key-1\",shaAlgorithm=\"hmac-sha256\",\n" +
                "   signature=\"Base64(HMAC-SHA256(signing string))\"";

        final Signature signature = Signature.fromString(authorization);

        assertEquals("hmac-key-1", signature.getKeyId());
        assertEquals("hmac-sha256", signature.getShaAlgorithm().toString());
        assertEquals("Base64(HMAC-SHA256(signing string))", signature.getSignature());
        assertEquals("date", join("\n", signature.getHeaders()));
    }

    /**
     * Authorization header parameters (keyId, algorithm, headers, signature)
     * may have whitespace between them
     */
    @Test
    public void whitespaceTolerance() throws Exception {
        String authorization = "  \nkeyId=\"hmac-key-1\",shaAlgorithm=\"hmac-sha256\",\n" +
                "   signature=\"Base64(HMAC-SHA256(signing string))\"  \n";

        final Signature signature = Signature.fromString(authorization);

        assertEquals("hmac-key-1", signature.getKeyId());
        assertEquals("hmac-sha256", signature.getShaAlgorithm().toString());
        assertEquals("Base64(HMAC-SHA256(signing string))", signature.getSignature());
        assertEquals("date", join("\n", signature.getHeaders()));
    }

    /**
     * Authorization header parameters (keyId, algorithm, headers, signature)
     * can be in any order
     */
    @Test
    public void orderTolerance() throws Exception {

        final Signature expected = new Signature("hmac-key-1", "hmac-sha256", "yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=", "date", "accept");

        final List<String> input = Arrays.asList(
                "keyId=\"hmac-key-1\"",
                "shaAlgorithm=\"hmac-sha256\"",
                "headers=\"date accept\"",
                "signature=\"yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=\""
        );

        for (int tries = 10; tries > 0; tries--) {
            Collections.shuffle(input);

            final String authorization = join(",", input);

            parseAndAssert(authorization, expected);
        }
    }

    /**
     * Headers supplied in the constructor should be lowercased
     * Algorithm supplied in the constructor should be lowercased
     */
    @Test
    public void caseNormalization() throws Exception {

        final Signature signature = new Signature("hmac-key-1", "hMaC-ShA256", "yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=", "dAte", "aCcEpt");

        assertEquals("hmac-key-1", signature.getKeyId());
        assertEquals("hmac-sha256", signature.getShaAlgorithm().toString());
        assertEquals("yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=", signature.getSignature());
        assertEquals("date\naccept", join("\n", signature.getHeaders()));
    }

    /**
     * 2.2.  Ambiguous Parameters
     * <p/>
     * If any of the parameters listed above are erroneously duplicated in
     * the associated header field, then the last parameter defined MUST be
     * used.  Any parameter that is not recognized as a parameter, or is not
     * well-formed, MUST be ignored.
     */
    @Test
    public void ambiguousParameters() throws Exception {

        final Signature expected = new Signature("hmac-key-3", "dsa-sha1", "DPIsA/PWeYjySmfjw2P2SLJXZj1szDOei/Hh8nTcaPo=", "date");

        final List<String> input = Arrays.asList(
                "keyId=\"hmac-key-1\"",
                "keyId=\"hmac-key-2\"",
                "keyId=\"hmac-key-3\"",
                "shaAlgorithm=\"hmac-sha256\"",
                "headers=\"date accept content-length\"",
                "shaAlgorithm=\"dsa-sha1\"",
                "headers=\"date\"",
                "signature=\"yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=\"",
                "signature=\"DPIsA/PWeYjySmfjw2P2SLJXZj1szDOei/Hh8nTcaPo=\""
        );

        final String authorization = join(",", input);

        parseAndAssert(authorization, expected);
    }

    @Test
    public void parameterCaseTolerance() throws Exception {

        final Signature expected = new Signature("hmac-key-3", "rsa-sha256", "DPIsA/PWeYjySmfjw2P2SLJXZj1szDOei/Hh8nTcaPo=", "date");

        final List<String> input = Arrays.asList(
                "keyId=\"hmac-key-1\"",
                "keyId=\"hmac-key-2\"",
                "KeyId=\"hmac-key-3\"",
                "shaAlgorithm=\"hmac-sha256\"",
                "headers=\"date accept content-length\"",
                "shaAlgorithm=\"rsa-sha256\"",
                "headers=\"date\"",
                "signature=\"yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=\"",
                "SIGNATURE=\"DPIsA/PWeYjySmfjw2P2SLJXZj1szDOei/Hh8nTcaPo=\""
        );

        final String authorization = join(",", input);

        parseAndAssert(authorization, expected);
    }

    @Test
    public void unknownParameters() throws Exception {

        final Signature expected = new Signature("hmac-key-3", "rsa-sha256", "PIft5ByT/Nr5RWvB+QLQRyFAvbGmauCOE7FTL0tI+Jg=", "date");

        final List<String> input = Arrays.asList(
                "scopeId=\"hmac-key-1\"",
                "keyId=\"hmac-key-2\"",
                "keyId=\"hmac-key-3\"",
                "precision=\"hmac-sha256\"",
                "shaAlgorithm=\"rsa-sha256\"",
                "topics=\"date accept content-length\"",
                "headers=\"date\"",
                "signature=\"PIft5ByT/Nr5RWvB+QLQRyFAvbGmauCOE7FTL0tI+Jg=\"",
                "signage=\"DPIsA/PWeYjySmfjw2P2SLJXZj1szDOei/Hh8nTcaPo=\""
        );

        final String authorization = join(",", input);

        parseAndAssert(authorization, expected);
    }

    @Test
    public void trailingCommaTolerance() throws Exception {
        String authorization = "" +
                "keyId=\"hmac-key-1\"," +
                "shaAlgorithm=\"hmac-sha256\"," +
                "headers=\"date accept\"," +
                "signature=\"yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=\"" +
                " , ";

        parseAndAssert(authorization, new Signature("hmac-key-1", "hmac-sha256", "yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=", "date", "accept"));
    }

    @Test
    public void testToString() throws Exception {

        final Signature signature = new Signature("hmac-key-1", "hmac-sha256", "Base64(HMAC-SHA256(signing string))", "(request-target)", "host", "date", "digest", "content-length");

        String authorization = "Signature keyId=\"hmac-key-1\"," +
                "shaAlgorithm=\"hmac-sha256\"," +
                "headers=\"(request-target) host date digest content-length\"," +
                "signature=\"Base64(HMAC-SHA256(signing string))\"";

        assertEquals(authorization, signature.toString());
    }


    /**
     * Parsing should only ever throw SignatureHeaderFormatException
     * <p/>
     * We want to avoid NullPointerException, StringIndexOutOfBoundsException and
     * any other exception that might result.
     */
    @Test
    public void throwsAuthorizationException() {

        final Random random = new Random();

        final StringBuilder authorization = new StringBuilder("Signature keyId=\"hmac-key-1\",algorithm=\"hmac-sha256\",\n" +
                "   headers=\"(request-target) host date digest content-length\",\n" +
                "   signature=\"yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=\"");

        while (authorization.length() > 0) {

            // Delete a random character and parse again
            authorization.deleteCharAt(random.nextInt(authorization.length()));

            try {

                Signature.fromString(authorization.toString());

            } catch (AuthenticationException e) {
                // pass
            } catch (Throwable e) {
                fail("SignatureHeaderFormatException should be the only possible exception type: caught " + e.getClass().getName());
            }
        }
    }

    @Test(expected = MissingKeyIdException.class)
    public void missingKeyId() {
        String authorization = "" +
//                "keyId=\"hmac-key-1\"," +
                "algorithm=\"hmac-sha256\"," +
                "headers=\"(request-target) host date digest content-length\"," +
                "signature=\"yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=\"";

        Signature.fromString(authorization);
    }

    @Test(expected = MissingAlgorithmException.class)
    public void missingAlgorithm() {
        String authorization = "" +
                "keyId=\"hmac-key-1\"," +
//                "algorithm=\"hmac-sha256\"," +
                "headers=\"(request-target) host date digest content-length\"," +
                "signature=\"yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=\"";

        Signature.fromString(authorization);
    }


    @Test(expected = MissingSignatureException.class)
    public void missingSignature() {
        String authorization = "" +
                "keyId=\"hmac-key-1\"," +
                "shaAlgorithm=\"hmac-sha256\"," +
                "headers=\"(request-target) host date digest content-length\"";
//                "signature=\"yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=\"";

        Signature.fromString(authorization);
    }

    @Test
    public void md5Signing() {
        String accessSecret = "123456";
        String md5SigningString = Signature.md5Signing(accessSecret,
                new HashMap<String, Object>() {
                    {
                        put("username", "张san");
                        put("password", "1q2w3e");
                        put("age", 1);
                    }
                });
        assertEquals("5390444a44003408cb2b3eab95a84ad3", md5SigningString);
        assertEquals(md5SigningString, Signature.md5Signing(accessSecret,
                new HashMap<String, Object>() {
                    {
                        put("age", 1);
                        put("password", "1q2w3e");
                        put("username", "张san");
                    }
                }));
    }


    private static void parseAndAssert(final String authorization, final Signature expected) {
        final Signature actual = Signature.fromString(authorization);
        assertSignature(expected, actual);
    }

    private static void assertSignature(Signature expected, Signature actual) {
        assertEquals(expected.getKeyId(), actual.getKeyId());
        assertEquals(expected.getShaAlgorithm(), actual.getShaAlgorithm());
        assertEquals(expected.getSignature(), actual.getSignature());
        assertEquals(join("\n", expected.getHeaders()), join("\n", actual.getHeaders()));
    }
}
