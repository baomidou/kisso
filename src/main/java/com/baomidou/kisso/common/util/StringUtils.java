/*
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
package com.baomidou.kisso.common.util;


/**
 * <p>
 * String 工具类
 * </p>
 *
 * @author D.Yang
 * @since 2016-08-18
 */
public class StringUtils {


    private StringUtils() {
        // to do nothing
    }

    /**
     * <p>
     * 判断字符串是否为空
     * </p>
     *
     * @param cs 需要判断字符串
     * @return 判断结果
     */
    public static boolean isEmpty(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * 判断字符串是否不为空
     * </p>
     *
     * @param cs 需要判断字符串
     * @return 判断结果
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }


}
