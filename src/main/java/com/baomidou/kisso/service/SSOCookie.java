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
package com.baomidou.kisso.service;

import lombok.Getter;
import lombok.Setter;

import jakarta.servlet.http.Cookie;

/**
 * SSO Cookie
 *
 * @author hubin
 * @since 2021-11-23
 */
@Getter
@Setter
public class SSOCookie extends Cookie {
    /**
     * cookie的SameSite属性用来限制第三方Cookie，从而减少安全风险(防止CSRF)
     * <p>
     * {@link com.baomidou.kisso.SSOConfig#cookieSameSite}
     */
    private String sameSite;
    /**
     * cookie 是否为只读状态，设置 js 无法获取
     */
    private boolean httpOnly;

    public SSOCookie(String name, String value) {
        super(name, value);
    }
}
