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
package com.baomidou.kisso.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.kisso.security.token.SSOToken;

/**
 * <p>
 * SSO 单点登录服务
 * </p>
 *
 * @author hubin
 * @since 2015-12-03
 */
public interface IKissoService {

    /**
     * <p>
     * 获取登录 SSOToken
     * </p>
     */
    SSOToken getSSOToken(HttpServletRequest request);

    /**
     * <p>
     * 踢出 指定用户 ID 的登录用户，退出当前系统。
     * </p>
     *
     * @param userId 用户ID
     * @return
     */
    boolean kickLogin(Object userId);

    /**
     * <p>
     * 设置登录 Cookie
     * </p>
     */
    void setCookie(HttpServletRequest request, HttpServletResponse response, SSOToken SSOToken);

    /**
     * <p>
     * 清理登录状态
     * </p>
     */
    boolean clearLogin(HttpServletRequest request, HttpServletResponse response);

    /**
     * <p>
     * 退出并跳至登录页
     * </p>
     */
    void clearRedirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
