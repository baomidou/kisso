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

import java.security.Provider;

/**
 * <p>
 * 签名接口
 * </p>
 *
 * @author hubin
 * @since 2019-04-08
 */
public interface ISign {

  /**
   * <p>
   * 签名
   * </p>
   *
   * @param provider           提供本服务的 Provider
   * @param shaAlgorithm       SHA 算法
   * @param signingStringBytes 待签名字节数组
   * @return
   */
  byte[] sign(Provider provider, ShaAlgorithm shaAlgorithm, byte[] signingStringBytes);
}
