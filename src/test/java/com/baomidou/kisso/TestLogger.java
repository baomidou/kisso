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
package com.baomidou.kisso;

import java.util.logging.Logger;


/**
 * <p>
 * 测试 java 原生日志工具类
 *
 * java.util.logging.Logger
 * </p>
 *
 * @author hubin
 * @since 2015-11-24
 */
public class TestLogger {

    private static final Logger logger = Logger.getLogger("TestLogger");

    public static void main(String[] args) {
        logger.severe("严重信息");
        logger.warning("警示信息");
        logger.info("一般信息");
        logger.config("设定方面的信息");
        logger.fine("细微的信息");
        logger.finer("更细微的信息");
        logger.finest("最细微的信息");
    }
}
