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
package com.baomidou.kisso.common;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.kisso.common.encrypt.MD5;

/**
 * <p>
 * 验证浏览器基本信息
 * </p>
 *
 * @author hubin
 * @since 2017-08-05
 */
public class Browser {

    /**
     * <p>
     * 混淆浏览器版本信息，取 MD5 中间部分字符
     * </p>
     *
     * @param request
     * @return
     * @Description 获取浏览器客户端信息签名值
     */
    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = MD5.toMD5(request.getHeader("user-agent"));
        if (null == userAgent) {
            return null;
        }
        return userAgent.substring(3, 8);
    }

    /**
     * <p>
     * 请求浏览器是否合法 (只校验客户端信息不校验domain)
     * </p>
     *
     * @param request
     * @param userAgent 浏览器客户端信息
     * @return
     */
    public static boolean isLegalUserAgent(HttpServletRequest request, String userAgent) {
        String ua = getUserAgent(request);
        if (null == ua) {
            return false;
        }
        return ua.equals(userAgent);
    }

}
