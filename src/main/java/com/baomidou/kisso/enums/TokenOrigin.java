/**
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baomidou.kisso.enums;

/**
 * <p>
 * Token 登录授权来源
 * </p>
 *
 * @author hubin
 * @since 2017-08-07
 */
public enum TokenOrigin {
    COOKIE("0", "cookie"),
    HTML5("1", "html5"),
    IOS("2", "apple ios"),
    ANDROID("3", "google android");

    /**
     * 主键
     */
    private final String value;

    /**
     * 描述
     */
    private final String desc;

    TokenOrigin(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static TokenOrigin fromValue(String value) {
        TokenOrigin[] its = TokenOrigin.values();
        for (TokenOrigin it : its) {
            if (it.value() == value) {
                return it;
            }
        }
        return COOKIE;
    }

    public String value() {
        return this.value;
    }

    public String desc() {
        return this.desc;
    }

}
