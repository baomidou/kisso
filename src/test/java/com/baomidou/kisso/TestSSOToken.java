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

import org.junit.Assert;
import org.junit.Test;

import com.baomidou.kisso.enums.TokenOrigin;
import com.baomidou.kisso.security.token.SSOToken;

/**
 * <p>
 * 测试 SSOToken
 * </p>
 *
 * @author hubin
 * @since 2017-08-07
 */
public class TestSSOToken {

    @Test
    public void getToken() {
        String token = SSOToken.create().setIp("127.0.0.1").setTime(1502085277L).setId(1)
                .setOrigin(TokenOrigin.IOS).setUserAgent("123").setIssuer("kisso").getToken();
        Assert.assertEquals(token, "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIxIiwiaXAiOiIxMjcuM" +
                "C4wLjEiLCJpc3MiOiJraXNzbyIsInVhIjoiMTIzIiwib2ciOiIyIiwiaWF0IjoxNTAyMDg1fQ.Yii" +
                "xGmdPOvCHE5kQ47jR0TCFpd68vBNVyyO2sxxwyXyQVRpJcTECqjtWawd3Gog4OWOg78C0FHvOZtjpBBM1gw");

        SSOToken ssoToken = SSOToken.parser(token, true);
        Assert.assertEquals("kisso", ssoToken.getIssuer());
    }


    @Test
    public void illegalOrigin() {
        String token = SSOToken.create().getToken();
        System.out.println(token);
        SSOToken ssoToken = SSOToken.parser(token, true);
        Assert.assertNull(ssoToken);
    }
}
