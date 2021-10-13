/**
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
package com.baomidou.kisso.captcha;

/**
 * <p>
 * 图片验证码票据存储接口
 * </p>
 *
 * @author hubin
 * @since 2018-08-11
 */
public interface ICaptchaStore {

  /**
   * <p>
   * 获取验证码票据的验证内容
   * </p>
   *
   * @param ticket
   *            验证码票据
   * @return String 验证码内容
   */
  String get(String ticket);

  /**
   * <p>
   * 设置验证码票据的验证内容
   * </p>
   *
   * @param ticket
   *            验证码票据
   * @param captcha
   *            验证码内容
   * @return boolean true 正确，false 错误
   */
  boolean put(String ticket, String captcha);

}
