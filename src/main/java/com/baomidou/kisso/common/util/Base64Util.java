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
package com.baomidou.kisso.common.util;

import java.util.Base64;

/**
 * BASE64编码解码工具包
 *
 * @author hubin
 * @since 2014-6-17
 */
public class Base64Util {

    /**
     * base64 encode
     *
     * @param src 编码内容
     * @return
     * @throws Exception
     */
    public static String encode(byte[] src) {
        return Base64.getEncoder().encodeToString(src);
    }

    /**
     * base64 decode
     *
     * @param src 解码内容
     * @return
     * @throws Exception
     */
    public static byte[] decode(String src) {
        return Base64.getDecoder().decode(src);
    }

    /**
     * base64 url encode
     *
     * @param src 编码内容
     * @return
     * @throws Exception
     */
    public static String urlEncode(byte[] src) throws Exception {
        return Base64.getUrlEncoder().encodeToString(src);
    }

    /**
     * base64 url decode
     *
     * @param src 解码内容
     * @return
     * @throws Exception
     */
    public static byte[] urlDecode(String src) throws Exception {
        return Base64.getUrlDecoder().decode(src);
    }

}
