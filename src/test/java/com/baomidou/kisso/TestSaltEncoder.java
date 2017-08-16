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

import com.baomidou.kisso.common.encrypt.MD5Salt;
import com.baomidou.kisso.common.util.RandomUtil;

/**
 * <p>
 * 测试盐值加密工具类
 * </p>
 *
 * @author hubin
 * @since 2016-01-20
 */
public class TestSaltEncoder {

    /**
     * 测试
     */
    public static void main(String[] args) {

		/* 生成 10 位随机盐值，生产环境盐值为固定值，您也可以使用  “ 登录用户名  ” 作为盐值，这样好处是每个用户加密的盐都不一样。 */
        String salt = RandomUtil.getCharacterAndNumber(10);
        System.err.println("盐值 salt=" + salt);
		
		/* 登录密码 */
        String pwd = "1q2=3e!$-Qde";
		
		/* MD5 盐加密 */
        MD5Salt md5 = new MD5Salt(salt, "MD5");
        String et1 = md5.encode(pwd);
        System.out.println(et1);
        boolean passwordValid = md5.isValid(et1, pwd);
        System.err.println(passwordValid);

		/* SHA 盐加密 */
        MD5Salt sha = new MD5Salt(salt, "SHA");
        String et2 = sha.encode(pwd);
        System.out.println(et2);
        boolean passwordValid2 = sha.isValid(et2, pwd);
        System.err.println(passwordValid2);

    }
}
