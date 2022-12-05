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
package com.baomidou.kisso.web;

import com.baomidou.kisso.common.util.StringUtils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterConfig;
import java.util.function.Consumer;

/**
 * <p>
 * 基础过滤包含公共方法
 * </p>
 *
 * @author hubin
 * @since 2022-10-11
 */
public interface BaseFilter extends Filter {


    /**
     * 初始化配置参数
     *
     * @param filterConfig      {@link FilterConfig}
     * @param parameter         配置参数
     * @param parameterConsumer 参数处理函数
     * @return
     */
    default void initParameter(FilterConfig filterConfig, String parameter, Consumer<String> parameterConsumer) {
        String value = filterConfig.getInitParameter(parameter);
        if (StringUtils.isNotEmpty(value)) {
            parameterConsumer.accept(value);
        }
    }


    /**
     * 获取 Boolean 值配置参数
     *
     * @param filterConfig {@link FilterConfig}
     * @param parameter    配置参数
     * @param defVal       默认值
     * @return
     */
    default boolean getBoolParameter(FilterConfig filterConfig, String parameter, Boolean defVal) {
        String value = filterConfig.getInitParameter(parameter);
        return StringUtils.isEmpty(value) ? defVal : Boolean.parseBoolean(value);
    }
}
