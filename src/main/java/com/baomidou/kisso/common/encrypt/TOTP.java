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

import java.lang.reflect.UndeclaredThrowableException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.baomidou.kisso.common.util.RandomUtil;
import com.baomidou.kisso.common.util.StringUtils;


/**
 * <p>
 * Google TOTP 算法
 * </p>
 *
 * @author hubin
 * @since 2018-08-05
 */
public class TOTP {

    /**
     * 0 1 2 3 4 5 6 7 8
     */
    private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};
    /**
     * 验证码有效时间间隔
     */
    private static final int INTERVAL = 30;
    /**
     * 有效时间范围 3
     */
    public static final int WINDOW = 3;
    /**
     * 验证码长度 6.
     */
    public static final int PASS_CODE_LENGTH = 6;

    private TOTP() {
        // to do nothing
    }

    /**
     * <p>
     * 生成 TOTP 验证码
     * </p>
     *
     * @param secretKey Base32 密钥
     * @return TOTP 验证码
     */
    public static long getCode(String secretKey) {
        byte[] decodedKey = decodeSecretKey(secretKey);
        long currentInterval = getCurrentInterval();
        return generate(decodedKey, currentInterval, PASS_CODE_LENGTH, Algorithm.HMACSHA1.getKey());
    }

    /**
     * <p>
     * otp auth url
     * </p>
     *
     * @param user      用户名或邮箱等
     * @param issuer    发布者
     * @param secretKey 密钥
     * @return otp auth url
     */
    public static String getOtpAuthUrl(String user, String issuer, String secretKey) {
        return "otpauth://totp/" + user + "?secret=" + secretKey + "&issuer=" + issuer;
    }

    /**
     * <p>
     * 校验验证码是否正确
     * </p>
     *
     * @param secretKey Base32 加密密钥
     * @param totpCode  TOTP 验证码
     * @return true 合法 false 非法
     */
    public static boolean isValidCode(String secretKey, long totpCode) {
        if (StringUtils.isEmpty(secretKey) || totpCode < 0) {
            return false;
        }
        byte[] decodedKey = decodeSecretKey(secretKey);

        int window = WINDOW;
        long currentInterval = getCurrentInterval();

        for (int i = -window; i <= window; ++i) {
            long hash = generate(decodedKey, currentInterval + i,
                    PASS_CODE_LENGTH, Algorithm.HMACSHA1.getKey());
            if (hash == totpCode) {
                return true;
            }
        }
        return false;
    }


    /**
     * <p>
     * 生成密钥字节
     * </p>
     */
    public static byte[] generateKey() throws Exception {
        byte[] key = HmacSHA256.generate(RandomUtil.get32UUID(), RandomUtil.getCharacterAndNumber(20));
        return Arrays.copyOf(key, 20);
    }

    /**
     * <p>
     * 字节转换为 Base32 加密字符串
     * </p>
     *
     * @param key 20字节非加密密钥
     * @return BASE32 加密字符串 32 byte (256-bits).
     */
    public static String getSecretKey(byte[] key) {
        return Base32.encode(key);
    }

    /**
     * <p>
     * 生成 Base32 加密字符串密钥
     * </p>
     *
     * @return Base32 加密字符串密钥
     */
    public static String getSecretKey() {
        try {
            return getSecretKey(generateKey());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * <p>
     * Base32 密钥解密字节
     * </p>
     *
     * @param secretKey Base32 密钥
     * @return Base 32 密码字节
     */
    public static byte[] decodeSecretKey(String secretKey) {
        return Base32.decode(secretKey);
    }

    /**
     * <p>
     * 生成  Google Authenticator 格式密钥
     * </p>
     *
     * @param secretKey Base32 密钥
     * @return 格式化后的密钥
     */
    public static String getFormatedKey(String secretKey) {
        return secretKey.toUpperCase().replaceAll("(.{4})(?=.{4})", "$1 ");
    }

    /**
     * <p>
     * 生成 TOTP 有效值
     * </p>
     *
     * @param key    共享密钥
     * @param time   有效时间
     * @param digits 返回数字长度
     * @param crypto 使用的加密算法
     * @return digits
     */
    private static int generate(byte[] key, long time, int digits, String crypto) {
        byte[] msg = ByteBuffer.allocate(8).putLong(time).array();
        byte[] hash = hmacSha(crypto, key, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
        int otp = binary % DIGITS_POWER[digits];
        return (otp);
    }

    /**
     * <p>
     * 该方法使用JCE来提供密码算法。<br>
     * HMAC计算以密码散列算法为参数的散列消息认证码。
     * </p>
     *
     * @param crypto   加密算法 (HmacSHA1, HmacSHA256, HmacSHA512)
     * @param keyBytes 用于HMAC密钥的字节
     * @param text     要认证的消息或文本
     */
    private static byte[] hmacSha(String crypto, byte[] keyBytes, byte[] text) throws UndeclaredThrowableException {
        try {
            Mac hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

    /**
     * 当前时间间隔
     */
    private static long getCurrentInterval() {
        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        return currentTimeSeconds / INTERVAL;
    }
}
