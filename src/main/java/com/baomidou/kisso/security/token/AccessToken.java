/*
 * Copyright (c) 2017-2021, hubin (jobob@qq.com).
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
package com.baomidou.kisso.security.token;

import io.jsonwebtoken.JwtBuilder;

import java.io.Serializable;

/**
 * <p>
 * JWT 访问票据
 * </p>
 *
 * @author hubin
 * @since 2017-07-17
 */
public class AccessToken implements Token, Serializable {

    private String token;

    public AccessToken() {
        // TO DO NOTHING
    }

    public AccessToken(JwtBuilder jwtBuilder) {
        this.token = jwtBuilder.compact();
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return this.token;
    }
}
