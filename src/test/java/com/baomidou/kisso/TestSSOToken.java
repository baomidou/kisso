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
package com.baomidou.kisso;

import com.baomidou.kisso.enums.TokenFlag;
import com.baomidou.kisso.enums.TokenOrigin;
import com.baomidou.kisso.security.JwtHelper;
import com.baomidou.kisso.security.token.SSOToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    public void hs512Token() {
        String hs521SecretKey = JwtHelper.getHS512SecretKey();
        System.err.println("hs521SecretKey = " + hs521SecretKey);

        String token = SSOToken.create().ip("127.0.0.1").time(1502085277L).id(1).tenantId("123").flag(TokenFlag.NORMAL)
                .origin(TokenOrigin.IOS).userAgent("123").issuer("kisso").getToken();

        SSOToken ssoToken = SSOToken.parser(token, true);
        Assertions.assertEquals("kisso", ssoToken.getIssuer());
    }

    @Test
    public void illegalOrigin() {
        String token = SSOToken.create().getToken();
        System.out.println(token);
        SSOToken ssoToken = SSOToken.parser(token, true);
        Assertions.assertNotNull(ssoToken);
    }

    @Test
    public void rsaToken() {
        SSOConfig ssoConfig = SSOConfig.getInstance();
        ssoConfig.setSignAlgorithm("RS512");
        String token = SSOToken.create().ip("127.0.0.1").time(1502085277L).id(1).issuer("kisso").getToken();
        SSOToken ssoToken = SSOToken.parser(token, true);
        Assertions.assertEquals("kisso", ssoToken.getIssuer());
    }
}
