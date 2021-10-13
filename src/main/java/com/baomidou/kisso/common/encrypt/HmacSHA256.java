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
package com.baomidou.kisso.common.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.baomidou.kisso.common.SSOConstants;


/**
 * <p>
 * HmacSHA256 算法
 * </p>
 *
 * @author hubin
 * @since 2018-08-05
 */
public class HmacSHA256 {

    /**
     * <p>
     * HmacSHA256 加密
     * </p>
     *
     * @param data 内容
     * @param key  密钥
     * @return
     */
    public static byte[] generate(String data, String key) throws UnsupportedEncodingException,
            NoSuchAlgorithmException, InvalidKeyException {
        return generate(data.getBytes(SSOConstants.ENCODING),
                key.getBytes(SSOConstants.ENCODING));
    }

    /**
     * <p>
     * HmacSHA256 加密
     * </p>
     *
     * @param data 内容
     * @param key  密钥
     * @return
     */
    public static byte[] generate(byte[] data, byte[] key) throws NoSuchAlgorithmException,
            InvalidKeyException {
        Mac mac = Mac.getInstance(Algorithm.HMACSHA256.getKey());
        SecretKeySpec signingKey = new SecretKeySpec(key, Algorithm.HMACSHA256.getKey());
        mac.init(signingKey);
        return mac.doFinal(data);
    }
}
