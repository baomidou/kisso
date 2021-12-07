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

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.common.util.StringPool;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

/**
 * <p>
 * 盐值加密工具类（常用登录密码加密）
 * </p>
 *
 * @author hubin
 * @since 2016-01-20
 */
@Slf4j
public class MD5Salt {

    /**
     * md5 盐值加密字符串
     *
     * @param salt    盐值
     * @param rawText 需要加密的字符串
     * @return
     */
    @Deprecated
    public static String md5SaltEncode(String salt, String rawText) {
        return encode(salt, rawText);
    }

    /**
     * 判断md5 盐值加密内容是否正确
     *
     * @param salt       盐值
     * @param encodeText 加密后的文本内容
     * @param rawText    加密前的文本内容
     * @return
     */
    @Deprecated
    public static boolean md5SaltValid(String salt, String encodeText, String rawText) {
        return isValid(salt, encodeText, rawText);
    }

    /**
     * 字符串盐值加密
     *
     * @param salt    盐值
     * @param rawText 需要加密的字符串
     * @return
     */
    public static String encode(String salt, String rawText) {
        try {
            MessageDigest md = MessageDigest.getInstance(MD5.ALGORITHM);
            //加密后的字符串
            return Byte2Hex.byte2Hex(md.digest(mergeRawTextAndSalt(salt, rawText).getBytes(SSOConfig.getSSOEncoding())));
        } catch (Exception e) {
            log.error(" MD5Salt encode exception.");
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断加密内容是否正确
     *
     * @param encodeText 加密后的文本内容
     * @param rawText    加密前的文本内容
     * @return
     */
    public static boolean isValid(String salt, String encodeText, String rawText) {
        return encode(salt, rawText).equals(encodeText);
    }

    /**
     * 合并混淆盐值至加密内容
     *
     * @param salt    盐值
     * @param rawText 需要加密的字符串
     * @return
     */
    private static String mergeRawTextAndSalt(String salt, String rawText) {
        if (rawText == null) {
            rawText = StringPool.EMPTY;
        }

        if (null == salt || StringPool.EMPTY.equals(salt)) {
            return rawText;
        } else {
            StringBuffer mt = new StringBuffer();
            mt.append(rawText);
            mt.append(StringPool.HASH);
            mt.append(salt);
            return mt.toString();
        }
    }
}
