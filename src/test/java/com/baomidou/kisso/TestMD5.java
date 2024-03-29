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
package com.baomidou.kisso;

import com.baomidou.kisso.common.IpHelper;
import com.baomidou.kisso.common.encrypt.MD5;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <p>
 * 测试 MD5加密工具类
 * </p>
 *
 * @author hubin
 * @since 2014-5-12
 */
public class TestMD5 {

    /**
     * 测试
     */
    @Test
    public void md5() {
        System.out.println(IpHelper.LOCAL_IP);
        System.out.println(IpHelper.HOST_NAME);
        Assertions.assertEquals("202cb962ac59075b964b07152d234b70", MD5.toMD5("123"));
    }
}
