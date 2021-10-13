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
package com.baomidou.kisso.captcha;

/**
 * <p>
 * 颜色色系分类<br>
 * http://tool.c7sky.com/webcolor/
 * </p>
 *
 * @author hubin
 * @since 2018-08-13
 */
public interface ColorType {

  /**
   * 柔和、明亮、温柔
   */
  int[][] SOFT = {{255, 255, 204}, {204, 255, 255}, {255, 204, 204}, {255, 255, 153}, {204, 204, 255}, {153, 204, 153}};

  /**
   * 活泼、快乐、有趣
   */
  int[][] LIVELY = {{255, 102, 102}, {51, 153, 204}, {0, 153, 153}, {0, 153, 51}, {255, 102, 0}, {102, 102, 153}};

}
