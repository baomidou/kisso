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

import java.util.Properties;

import com.baomidou.kisso.common.util.EnvUtil;

/**
 * <p>
 * 测试系统环境探测工具类
 * </p>
 *
 * @author hubin
 * @since 2016-01-21
 */
public class TestEnvUtil {

    /**
     * LINUX 编辑用法：
     * #############################################
     * vi /etc/profile
     * ---------------------------------------------
     * SSO_LOGIN=0 export
     * SSO_LOGIN
     * ---------------------------------------------
     * source /etc/profile
     * #############################################
     * 程序输入结果：0
     */
    public static void main(String[] args) {
        /*
		 * 注意大小写，比如读取PATH，
		 * Linux下为PATH；
		 * Windows为Path
		 */
        Properties p = EnvUtil.getEnv();
        System.err.println(p.getProperty("SSO_LOGIN"));
    }

}
