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
package com.baomidou.kisso.web.waf.attack;

import com.baomidou.kisso.common.util.StringPool;

import java.util.regex.Pattern;

/**
 * XSS脚本攻击
 * <p>
 *
 * @author hubin
 * @since 2014-5-8
 */
public class XSS implements Istrip {

  /**
   * @param value 待处理内容
   * @since XSS脚本内容剥离
   */
  @Override
  public String strip(String value) {
    String rlt = null;

    if (value != null) {
      // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
      // avoid encoded attacks.
      // value = ESAPI.encoder().canonicalize(value);

      // Avoid null characters
      rlt = value.replaceAll(StringPool.EMPTY, StringPool.EMPTY);

      // Avoid anything between script tags
      Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
      rlt = scriptPattern.matcher(rlt).replaceAll(StringPool.EMPTY);

      // Avoid anything in a src='...' type of expression
      scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE
        | Pattern.MULTILINE | Pattern.DOTALL);
      rlt = scriptPattern.matcher(rlt).replaceAll(StringPool.EMPTY);

      scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE
        | Pattern.MULTILINE | Pattern.DOTALL);
      rlt = scriptPattern.matcher(rlt).replaceAll(StringPool.EMPTY);

      // Remove any lonesome </script> tag
      scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
      rlt = scriptPattern.matcher(rlt).replaceAll(StringPool.EMPTY);

      // Remove any lonesome <script ...> tag
      scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE
        | Pattern.MULTILINE | Pattern.DOTALL);
      rlt = scriptPattern.matcher(rlt).replaceAll(StringPool.EMPTY);

      // Avoid eval(...) expressions
      scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE
        | Pattern.MULTILINE | Pattern.DOTALL);
      rlt = scriptPattern.matcher(rlt).replaceAll(StringPool.EMPTY);

      // Avoid expression(...) expressions
      scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE
        | Pattern.MULTILINE | Pattern.DOTALL);
      rlt = scriptPattern.matcher(rlt).replaceAll(StringPool.EMPTY);

      // Avoid javascript:... expressions
      scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
      rlt = scriptPattern.matcher(rlt).replaceAll(StringPool.EMPTY);

      // Avoid vbscript:... expressions
      scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
      rlt = scriptPattern.matcher(rlt).replaceAll(StringPool.EMPTY);

      // Avoid onload= expressions
      scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE
        | Pattern.MULTILINE | Pattern.DOTALL);
      rlt = scriptPattern.matcher(rlt).replaceAll(StringPool.EMPTY);
    }

    return rlt;
  }

}
