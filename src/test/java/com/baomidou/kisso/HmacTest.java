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

import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.junit.Assert;
import org.junit.Test;

import com.baomidou.kisso.common.signature.ShaAlgorithm;
import com.baomidou.kisso.common.signature.Signature;
import com.baomidou.kisso.common.signature.Signer;

public class HmacTest extends Assert {

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


    @Test
    public void hmacSha1() throws Exception {

        final ShaAlgorithm algorithm = ShaAlgorithm.HMAC_SHA1;

        assertSignature(algorithm, "DMP1G2BKLf1o9iKg0NvPZo8RigY=", "don't tell", "content-length", "host", "date", "(request-target)");
        assertSignature(algorithm, "P6FNqBvdGQcaNTecru8KR1ObHLY=", "another key", "content-length", "host", "date", "(request-target)");
        assertSignature(algorithm, "VPEKHCIXUlAqXNCmoB+aSelBZkU=", "don't tell", "content-length", "host", "date");
        assertSignature(algorithm, "mhWHbBqk3ArpoYlT60VING3P1gQ=", "another key", "content-length", "host", "date");
    }

    @Test
    public void hmacSha224() throws Exception {

        final ShaAlgorithm algorithm = ShaAlgorithm.HMAC_SHA224;

        assertSignature(algorithm, "QrMmo4aTqzGj7YN1GG3YSuzuIcwzxd3ju5QiMQ==", "don't tell", "content-length", "host", "date", "(request-target)");
        assertSignature(algorithm, "QEnSlbDpKTLkz/eQbtg/00O6/yf/KxGV2XIJOA==", "another key", "content-length", "host", "date", "(request-target)");
        assertSignature(algorithm, "cXuvhp1zBiNXV2bbtdCK2bAubdCBYPyeC7XmCA==", "don't tell", "content-length", "host", "date");
        assertSignature(algorithm, "nOmoaNCYeEA8v/NZSmCRA+XHuEoxvufVm6OU/g==", "another key", "content-length", "host", "date");
    }

    @Test
    public void hmacSha256() throws Exception {

        final ShaAlgorithm hmacSha256 = ShaAlgorithm.HMAC_SHA256;

        assertSignature(hmacSha256, "yT/NrPI9mKB5R7FTLRyFWvB+QLQOEAvbGmauC0tI+Jg=", "don't tell", "content-length", "host", "date", "(request-target)");
        assertSignature(hmacSha256, "sr+ungXeJjxCEJvJSFS0o+P9deafROte/1n3q+Ig6mg=", "another key", "content-length", "host", "date", "(request-target)");
        assertSignature(hmacSha256, "R6gbUcVfoGGkCy//JjBSF7jkD9wIQA4DKruUgtv/P1M=", "don't tell", "content-length", "host", "date");
        assertSignature(hmacSha256, "KrB+54zf29LFZrkwgVTUlJOyOeBtl0BzOp6FdjbDo70=", "another key", "content-length", "host", "date");
    }

    @Test
    public void hmacSha384() throws Exception {

        final ShaAlgorithm algorithm = ShaAlgorithm.HMAC_SHA384;

        assertSignature(algorithm, "9YsBcpHITHOBbqf0TrcMl5OlWF/qxPVNpIy8EUviDSWLkpDDkSmWSoTicACzZmoV", "don't tell", "content-length", "host", "date", "(request-target)");
        assertSignature(algorithm, "bY/D5QJFyOVKZVHsuL6e9LsOOEGgC3s2GUspvHVNjNHfDNwMi0ZlFuPXcPCIQArz", "another key", "content-length", "host", "date", "(request-target)");
        assertSignature(algorithm, "KH54rBfQcOE6GAwIvIVVAiYnld0Lru5/5ujiV+ebCeL0wJXDhAybmiXCYZ3efYUl", "don't tell", "content-length", "host", "date");
        assertSignature(algorithm, "cih898vWdxaF5T3r3m8iKCOiW7sAbBGESuzNnI+Ips1rQelWZJHOe71MZKQuFc1V", "another key", "content-length", "host", "date");
    }


    @Test
    public void hmacSha512() throws Exception {

        final ShaAlgorithm algorithm = ShaAlgorithm.HMAC_SHA512;

        assertSignature(algorithm, "HKObooU+mlMdFoxn29Yk0U8JZlFMXlpLqdcVd4OyJHInyjbJNTtkSoVFU4EgogzGTQRLed9Ja8+SxwGS8Lw2UQ==", "don't tell", "content-length", "host", "date", "(request-target)");
        assertSignature(algorithm, "qxy0NC6BelTV0O8eVWYyyptgsVx/UjAorCLDjirznEEC6ay4orgGvmCFHlz1O1uYEY7t2xTCk5Dw5Rhnf20bEA==", "another key", "content-length", "host", "date", "(request-target)");
        assertSignature(algorithm, "WHVT+7KsEIzmJ69ujXuRXPzRvb4yfrZZSXlxnnnuRR5r3H9MyJ7y1t1xWgu9pEmj41gzdogF4pF3hR0Z7f62Jw==", "don't tell", "content-length", "host", "date");
        assertSignature(algorithm, "+w0a5Sw0+pOzA0OHXiVD5DDx2xdOqZcebhFtaXINUPWFhf68pVdWSzOPjGGHa/zIhsE152FT4E1mHbrPZO71Eg==", "another key", "content-length", "host", "date");
    }


    private void assertSignature(final ShaAlgorithm algorithm, final String expected, final String keyString, final String... sign) throws Exception {

        final Signer signer = new Signer(
                new SecretKeySpec(keyString.getBytes(), algorithm.getJmvName()),
                new Signature("foo-key-1", algorithm, null, sign)
        );

        final Signature signed = signer.sign(method, uri, headers);
        assertEquals(expected, signed.getSignature());
    }
}
