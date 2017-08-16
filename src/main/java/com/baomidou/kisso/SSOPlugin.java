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
package com.baomidou.kisso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.kisso.security.token.SSOToken;

/**
 * <p>
 * SSO 插件接口
 * </p>
 *
 * @author hubin
 * @since 2017-0805
 */
public abstract class SSOPlugin {

    /**
     * <p>
     * 登录时调用该方法
     * </p>
     *
     * @param request
     * @param response
     * @return
     */
    public abstract boolean login(HttpServletRequest request, HttpServletResponse response);


    /**
     * <p>
     * 登录后获取 Token 时调用该方法
     * </p>
     * <p>
     * 用来验证 Token 合法性（例如 time 超时验证）
     * </p>
     *
     * @param ssoToken 登录票据
     * @return
     */
    public boolean validateToken(SSOToken ssoToken) {
        return true;
    }


    /**
     * <p>
     * 退出时调用该方法
     * </p>
     *
     * @param request
     * @param response
     * @return
     */
    public abstract boolean logout(HttpServletRequest request, HttpServletResponse response);
}
