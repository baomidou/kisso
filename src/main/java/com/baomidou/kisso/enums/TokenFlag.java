/*
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
 * Token 状态标记
 * </p>
 *
 * @author hubin
 * @since 2017-08-07
 */
public enum TokenFlag {
    NORMAL("0", "正常"),
    CACHE_SHUT("1", "缓存宕机");

    /**
     * 主键
     */
    private final String value;

    /**
     * 描述
     */
    private final String desc;

    TokenFlag(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static TokenFlag fromValue(String value) {
        TokenFlag[] its = TokenFlag.values();
        for (TokenFlag it : its) {
            if (it.value().equals(value)) {
                return it;
            }
        }
        return NORMAL;
    }

    public String value() {
        return this.value;
    }

    public String desc() {
        return this.desc;
    }

}
