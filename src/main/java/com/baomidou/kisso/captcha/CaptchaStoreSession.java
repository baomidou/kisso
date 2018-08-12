/**
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 图片验证码内容存储 Session 设置 1 分钟过期
 * </p>
 *
 * @author hubin
 * @since 2018-08-11
 */
public class CaptchaStoreSession implements ICaptchaStore {

  private HttpSession httpSession;

  private CaptchaStoreSession() {
    // to do nothing
  }

  public CaptchaStoreSession(HttpServletRequest request) {
    this.httpSession = request.getSession();
  }

  @Override
  public String get(String ticket) {
    return String.valueOf(httpSession.getAttribute(ticket));
  }

  @Override
  public boolean put(String ticket, String captcha) {
    httpSession.setMaxInactiveInterval(60);
    httpSession.setAttribute(ticket, captcha);
    return true;
  }
}
