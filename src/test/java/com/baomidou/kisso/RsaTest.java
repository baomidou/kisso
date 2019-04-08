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
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.baomidou.kisso;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.baomidou.kisso.common.encrypt.PEM;
import com.baomidou.kisso.common.signature.ShaAlgorithm;
import com.baomidou.kisso.common.signature.Signature;
import com.baomidou.kisso.common.signature.Signer;

public class RsaTest extends Assert {


    private final String privateKeyPem = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIICXgIBAAKBgQDCFENGw33yGihy92pDjZQhl0C36rPJj+CvfSC8+q28hxA161QF\n" +
            "NUd13wuCTUcq0Qd2qsBe/2hFyc2DCJJg0h1L78+6Z4UMR7EOcpfdUE9Hf3m/hs+F\n" +
            "UR45uBJeDK1HSFHD8bHKD6kv8FPGfJTotc+2xjJwoYi+1hqp1fIekaxsyQIDAQAB\n" +
            "AoGBAJR8ZkCUvx5kzv+utdl7T5MnordT1TvoXXJGXK7ZZ+UuvMNUCdN2QPc4sBiA\n" +
            "QWvLw1cSKt5DsKZ8UETpYPy8pPYnnDEz2dDYiaew9+xEpubyeW2oH4Zx71wqBtOK\n" +
            "kqwrXa/pzdpiucRRjk6vE6YY7EBBs/g7uanVpGibOVAEsqH1AkEA7DkjVH28WDUg\n" +
            "f1nqvfn2Kj6CT7nIcE3jGJsZZ7zlZmBmHFDONMLUrXR/Zm3pR5m0tCmBqa5RK95u\n" +
            "412jt1dPIwJBANJT3v8pnkth48bQo/fKel6uEYyboRtA5/uHuHkZ6FQF7OUkGogc\n" +
            "mSJluOdc5t6hI1VsLn0QZEjQZMEOWr+wKSMCQQCC4kXJEsHAve77oP6HtG/IiEn7\n" +
            "kpyUXRNvFsDE0czpJJBvL/aRFUJxuRK91jhjC68sA7NsKMGg5OXb5I5Jj36xAkEA\n" +
            "gIT7aFOYBFwGgQAQkWNKLvySgKbAZRTeLBacpHMuQdl1DfdntvAyqpAZ0lY0RKmW\n" +
            "G6aFKaqQfOXKCyWoUiVknQJAXrlgySFci/2ueKlIE1QqIiLSZ8V8OlpFLRnb1pzI\n" +
            "7U1yQXnTAEFYM560yJlzUpOb1V4cScGd365tiSMvxLOvTA==\n" +
            "-----END RSA PRIVATE KEY-----\n";

    private final String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCFENGw33yGihy92pDjZQhl0C3\n" +
            "6rPJj+CvfSC8+q28hxA161QFNUd13wuCTUcq0Qd2qsBe/2hFyc2DCJJg0h1L78+6\n" +
            "Z4UMR7EOcpfdUE9Hf3m/hs+FUR45uBJeDK1HSFHD8bHKD6kv8FPGfJTotc+2xjJw\n" +
            "oYi+1hqp1fIekaxsyQIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private final String method = "POST";
    private final String uri = "/foo?param=value&pet=dog";
    private final Map<String, String> headers = new HashMap<String, String>();

    {
        headers.put("Host", "example.org");
        headers.put("Date", "Thu, 05 Jan 2012 21:31:40 GMT");
        headers.put("Content-Type", "application/json");
        headers.put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=");
        headers.put("Accept", "*/*");
        headers.put("Content-Length", "18");
    }


    public RsaTest() throws Exception {
        privateKey = PEM.readPrivateKey(new ByteArrayInputStream(privateKeyPem.getBytes()));
        publicKey = PEM.readPublicKey(new ByteArrayInputStream(publicKeyPem.getBytes()));
    }

    @Test
    public void rsaSha1() throws Exception {
        final ShaAlgorithm algorithm = ShaAlgorithm.RSA_SHA1;

        assertSignature(algorithm, "kcX/cWMRQEjUPfF6AO7ANZ/eQkpRd" +
                "/4+dr3g1B5HZBn3vRDxGFbDRY19HeJUUlBAgmvolRwLlrVkz" +
                "LOmYdug6Ff01UUl6gX+TksGbsxagbNUNoEx0hrX3+8Jbd+x8" +
                "gx9gZxA7DwXww1u3bGrmChnfkdOofY52KhUllUox4mmBeI=",
                "date");

        assertSignature(algorithm, "F6g4qdBSHBcWo1iMsHetQU9TnPF39" +
                "naVHQogAhgvY6wh0/cdkquN4D6CInTyEHtMuv7xlOt0yBaVt" +
                "brrNP5JZKquYMW2JC3FXdtIiaYWhLUb/Nmb+JPr6C8AnxMzc" +
                "fNfuOZFn3X7ekA32qbfnYr7loHqpEGUr+G1NYsckEXdlM4=",
                "(request-target)", "host", "date");
    }


    @Test
    public void rsaSha256() throws Exception {
        final ShaAlgorithm algorithm = ShaAlgorithm.RSA_SHA256;

        assertSignature(algorithm, "ATp0r26dbMIxOopqw0OfABDT7CKMI" +
                "oENumuruOtarj8n/97Q3htHFYpH8yOSQk3Z5zh8UxUym6FYT" +
                "b5+A0Nz3NRsXJibnYi7brE/4tx5But9kkFGzG+xpUmimN4c3" +
                "TMN7OFH//+r8hBf7BT9/GmHDUVZT2JzWGLZES2xDOUuMtA=",
                "date");

        assertSignature(algorithm, "DT9vcDFbit2ahGZowjUzzih+sVpKM" +
                "IPZrXy1DMljImYNSJ3UEweTMfF3MUFjdNwYH59IDJoB+QTg3" +
                "Rpm5xLvMWD7tql/Ng/NCJs8gYSNjOQidArEpWp88c5IQPDXn" +
                "1lnJMU6dNXZNxc8Yqj+mIYhwHpKEKTqnvEtnCvB/6y/dIM=",
                "(request-target)", "host", "date");
    }


    @Test
    public void rsaSha384() throws Exception {
        final ShaAlgorithm algorithm = ShaAlgorithm.RSA_SHA384;

        assertSignature(algorithm, "AYtR6NQy+59Ta3X1GYNlfOzJo4Sg+" +
                "aB+ulDkR6Q2/8egvByRx5l0+t/2abAaFHf33SDojHYWPlpuj" +
                "HM26ExZPFXeYzG9sRctKD7XKrA/F6LRXEm1RXLFvfvLXQw4P" +
                "4HE1PMH+gCw2E+6IoTnbcimQtZ82SkF1uDRtLDhR6iqpFI="
                , "date");

        assertSignature(algorithm, "mRaP0Z5lh9XKGDahdsomoKR9Kjsj9" +
                "a/lgUEpZDQpvSZq5NhODEjmQh1qRn6Sx/c+AFl67yzDYAMXx" +
                "9h49ZOpKpuj4FGrz5/DIK7cdn9wXBKqDYgDfwOF9O5jNOE1r" +
                "9zbORTH0XxA8WE9H/MXoOrDIH1NjM5o9I4ErT4zKnD5OsQ="
                , "(request-target)", "host", "date");
    }

    @Test
    public void rsaSha512() throws Exception {
        final ShaAlgorithm algorithm = ShaAlgorithm.RSA_SHA512;

        assertSignature(algorithm, "IItboA8OJgL8WSAnJa8MND04s9j7d" +
                "B6IJIBVpOGJph8Tmkc5yUAYjvO/UQUKytRBe5CSv2GLfTAmE" +
                "7SuRgGGMwdQZubNJqRCiVPKBpuA47lXrKgC/wB0QAMkPHI6c" +
                "PllBZRixmjZuU9mIbuLjXMHR+v/DZwOHT9k8x0ILUq2rKE="
                , "date");

        assertSignature(algorithm, "ggIa4bcI7q377gNoQ7qVYxTA4pEOl" +
                "xlFzRtiQV0SdPam4sK58SFO9EtzE0P1zVTymTnsSRChmFU2p" +
                "n+R9VzkAhQ+yEbTqzu+mgHc4P1L5IeeXQ5aAmGENfkRbm2vd" +
                "OZzP5j6ruB+SJXIlhnaum2lsuyytSS0m/GkWvFJVZFu33M="
                , "(request-target)", "host", "date");
    }

    private void assertSignature(final ShaAlgorithm algorithm, final String expected, final String... sign) throws Exception {

        final Signer signer = new Signer(privateKey, new Signature("some-key-1", algorithm, null, sign));

        final Signature signed = signer.sign(method, uri, headers);

        assertEquals(expected, signed.getSignature());
    }

}
