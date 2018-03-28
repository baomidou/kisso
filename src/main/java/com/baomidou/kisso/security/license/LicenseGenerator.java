/*
 * Copyright (c) 2017-2020, hubin (jobob@qq.com).
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
package com.baomidou.kisso.security.license;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;

import com.baomidou.kisso.common.util.IoUtil;
import com.baomidou.kisso.common.util.OrderedProperties;
import com.baomidou.kisso.exception.LicenseException;

/**
 * <p>
 * License 许可证生成器
 * </p>
 *
 * @author hubin
 * @since 2018-03-28
 */
public class LicenseGenerator {

    /**
     * <p>
     * License.dat 文件生成
     * </p>
     *
     * @param features       相关属性
     * @param output         输出流
     * @param privateKeyFile 私钥文件
     * @return
     * @throws LicenseException
     */
    public static String generateLicense(Properties features, OutputStream output, String privateKeyFile) throws LicenseException {
        try {
            PrivateKey privateKey = readPrivateKey(privateKeyFile);
            String encoded = features.toString();
            String signature = sign(encoded.getBytes(), privateKey);
            Properties properties = new OrderedProperties();
            properties.putAll(features);
            properties.setProperty(LicenseManager.SIGNATURE, signature);
            properties.store(output, "License file");
            return signature;
        } catch (Exception e) {
            throw new LicenseException(e);
        }
    }

    /**
     * <p>
     * License.dat 文件生成
     * </p>
     *
     * @param features       相关属性
     * @param privateKeyFile 私钥文件
     * @return
     * @throws LicenseException
     */
    public static String generateLicense(Properties features, String privateKeyFile) throws LicenseException {
        OutputStream output = null;
        try {
            output = new FileOutputStream(LicenseManager.LICENSE_FILE);
            return generateLicense(features, output, privateKeyFile);
        } catch (FileNotFoundException e) {
            throw new LicenseException(e);
        } finally {
            IoUtil.close(output);
        }
    }

    /**
     * <p>
     * 读取私钥文件
     * </p>
     *
     * @param uri 私钥文件路径
     * @return
     * @throws LicenseException
     */
    private static PrivateKey readPrivateKey(String uri) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(IoUtil.getBytesFromFile(uri));
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * <p>
     * 私钥签名
     * </p>
     *
     * @param message    信息字节组
     * @param privateKey 私钥文件
     * @return
     * @throws LicenseException
     */
    private static String sign(byte[] message, PrivateKey privateKey) throws Exception {
        Signature dsa = Signature.getInstance("SHA/DSA");
        dsa.initSign(privateKey);
        dsa.update(message);
        return DatatypeConverter.printBase64Binary(dsa.sign());
    }

}
