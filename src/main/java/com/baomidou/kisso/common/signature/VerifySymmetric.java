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
package com.baomidou.kisso.common.signature;

import com.baomidou.kisso.common.util.Base64Util;
import com.baomidou.kisso.exception.UnsupportedAlgorithmException;

import javax.crypto.Mac;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.Objects;

/**
 * <p>
 * 对称验签
 * </p>
 *
 * @author hubin
 * @since 2019-04-08
 */
public class VerifySymmetric implements IVerify {

    private final Key key;

    public VerifySymmetric(final Key key) {
        this.key = key;
    }

    @Override
    public boolean verify(Provider provider, ShaAlgorithm shaAlgorithm, byte[] signingStringBytes, String verifyString) {
        try {
            final Mac mac = provider == null ? Mac.getInstance(shaAlgorithm.getJmvName())
                    : Mac.getInstance(shaAlgorithm.getJmvName(), provider);
            mac.init(key);
            byte[] hash = mac.doFinal(signingStringBytes);
            return Objects.equals(Base64Util.encode(hash), verifyString);
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedAlgorithmException(shaAlgorithm.getJmvName());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
