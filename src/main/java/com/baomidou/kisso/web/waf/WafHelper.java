/*
 * Copyright (c) 2017-2022, hubin (jobob@qq.com).
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
package com.baomidou.kisso.web.waf;

import com.baomidou.kisso.web.waf.attack.SqlInjection;
import com.baomidou.kisso.web.waf.attack.XSS;

/**
 * Web防火墙工具类
 * <p>
 *
 * @author hubin
 * @since 2014-5-8
 */
public class WafHelper {

  /**
   * @param value 待处理内容
   * @return
   * @since 过滤XSS脚本内容
   */
  public static String stripXSS(String value) {
    if (value == null) {
      return null;
    }

    return new XSS().strip(value);
  }

  /**
   * @param value 待处理内容
   * @return
   * @since 过滤SQL注入内容
   */
  public static String stripSqlInjection(String value) {
    if (value == null) {
      return null;
    }

    return new SqlInjection().strip(value);
  }

  /**
   * @param value 待处理内容
   * @return
   * @since 过滤SQL/XSS注入内容
   */
  public static String stripSqlXSS(String value) {
    if (value == null) {
      return null;
    }

    return stripXSS(stripSqlInjection(value));
  }

}
