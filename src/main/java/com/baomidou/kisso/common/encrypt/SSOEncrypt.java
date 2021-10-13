/*
 * Copyright (c) 2017-2022, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baomidou.kisso.common.encrypt;

/**
 * <p>
 * SSO 对称加密接口
 * </p>
 *
 * @author hubin
 * @since 2014-5-9
 */
public interface SSOEncrypt {

    /**
     * 字符串内容加密
     * <p>
     *
     * @param value 加密内容
     * @param key   密钥
     * @return
     * @throws Exception
     */
    String encrypt(String value, String key) throws Exception;

    /**
     * 字符串内容解密
     * <p>
     *
     * @param value 解密内容
     * @param key   密钥
     * @return
     * @throws Exception
     */
    String decrypt(String value, String key) throws Exception;
}
