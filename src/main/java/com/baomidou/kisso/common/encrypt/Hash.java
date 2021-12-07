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

import com.baomidou.kisso.common.util.StringPool;
import com.baomidou.kisso.exception.AESException;
import com.baomidou.kisso.exception.KissoException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * <p>
 * HASH 算法类
 * </p>
 *
 * @author hubin
 * @since 2014-5-9
 */
public class Hash {

    /**
     * <p>
     * 连续 HASH KEY
     * </p>
     * <p>
     * MD2 + MD5 + HASH1 -> 104
     * https://venishjoe.net/post/serial-key-generation-and-validation-in-java/
     * </p>
     *
     * @param stringInput
     * @return
     */
    public static String getSerialKey(String stringInput) {
        try {
            StringBuffer out = new StringBuffer();
            String serialNumberEncoded = calculate(stringInput, "MD2") +
                    calculate(stringInput, "MD5") +
                    calculate(stringInput, "SHA1");
            out.append(serialNumberEncoded.charAt(32));
            out.append(serialNumberEncoded.charAt(76));
            out.append(serialNumberEncoded.charAt(100));
            out.append(serialNumberEncoded.charAt(50));
            out.append(StringPool.DASH);
            out.append(serialNumberEncoded.charAt(2));
            out.append(serialNumberEncoded.charAt(91));
            out.append(serialNumberEncoded.charAt(73));
            out.append(serialNumberEncoded.charAt(72));
            out.append(serialNumberEncoded.charAt(98));
            out.append(StringPool.DASH);
            out.append(serialNumberEncoded.charAt(47));
            out.append(serialNumberEncoded.charAt(65));
            out.append(serialNumberEncoded.charAt(18));
            out.append(serialNumberEncoded.charAt(85));
            out.append(StringPool.DASH);
            out.append(serialNumberEncoded.charAt(27));
            out.append(serialNumberEncoded.charAt(53));
            out.append(serialNumberEncoded.charAt(102));
            out.append(serialNumberEncoded.charAt(15));
            out.append(serialNumberEncoded.charAt(99));
            return out.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new KissoException(e);
        }
    }

    /**
     * <p>
     * HASH 计算
     * </p>
     *
     * @param stringInput   输入字符串
     * @param algorithmName HASH 算法
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String calculate(String stringInput, String algorithmName) throws NoSuchAlgorithmException {
        StringBuilder hexMessageEncode = new StringBuilder();
        byte[] buffer = stringInput.getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance(algorithmName);
        messageDigest.update(buffer);
        byte[] messageDigestBytes = messageDigest.digest();
        for (byte messageDigestByte : messageDigestBytes) {
            int countEncode = messageDigestByte & 0xff;
            if (Integer.toHexString(countEncode).length() == 1) {
                hexMessageEncode.append("0");
            }
            hexMessageEncode.append(Integer.toHexString(countEncode));
        }
        return hexMessageEncode.toString();
    }


    /**
     * <p>
     * 用SHA1算法生成安全签名
     * </p>
     *
     * @param token     票据
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @param encrypt   密文
     * @return 安全签名
     * @throws AESException {@link AESException}
     */
    public static String getSHA1(String token, String timestamp, String nonce, String encrypt) throws AESException {
        try {
            String[] array = new String[]{token, timestamp, nonce, encrypt};
            StringBuffer sb = new StringBuffer();

            /* 字符串排序 */
            Arrays.sort(array);
            for (int i = 0; i < 4; i++) {
                sb.append(array[i]);
            }

            /* SHA1签名生成 */
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(sb.toString().getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = StringPool.EMPTY;
            for (byte b : digest) {
                shaHex = Integer.toHexString(b & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }

            return hexstr.toString();
        } catch (Exception e) {
            throw new AESException(AESException.ERROR_COMPUTE_SIGNATURE, e);
        }
    }

}
