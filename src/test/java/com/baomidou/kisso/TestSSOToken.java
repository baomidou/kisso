/*
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

import com.baomidou.kisso.enums.TokenOrigin;
import com.baomidou.kisso.security.JwtHelper;
import com.baomidou.kisso.security.token.SSOToken;
import org.junit.Assert;
import org.junit.Test;

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
    public void getHS512SecretKey() {
        System.out.println(JwtHelper.getHS512SecretKey());
    }

    @Test
    public void hs512Token() {
        String token = SSOToken.create().setIp("127.0.0.1").setTime(1502085277L).setId(1)
                .setOrigin(TokenOrigin.IOS).setUserAgent("123").setIssuer("kisso").getToken();
        Assert.assertEquals(token, "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIxIiwiaXAiOiIxMjcuMC4wLjEiLCJpc3MiOiJra" +
                "XNzbyIsInVhIjoiMTIzIiwib2ciOiIyIiwiaWF0IjoxNTAyMDg1fQ.dVhcDtXYayHsYcw7_eMOtJeuR5xoNMzGty44TxcH7m" +
                "v2FNFYdrj0pKkLEy4j5kt8i7YUatcRtkTmf7BV_Tpk1Q");
        SSOToken ssoToken = SSOToken.parser(token, true);
        Assert.assertEquals("kisso", ssoToken.getIssuer());

        this.rsaToken();
    }

    @Test
    public void illegalOrigin() {
        String token = SSOToken.create().getToken();
        System.out.println(token);
        SSOToken ssoToken = SSOToken.parser(token, true);
        Assert.assertNull(ssoToken);
    }

    public void rsaToken() {
        SSOConfig ssoConfig = SSOConfig.getInstance();
        ssoConfig.setSignAlgorithm("RS512");
        String token = SSOToken.create().setIp("127.0.0.1").setTime(1502085277L).setId(1)
                .setOrigin(TokenOrigin.IOS).setUserAgent("123").setIssuer("kisso").getToken();
        Assert.assertEquals(token, "eyJhbGciOiJSUzUxMiJ9.eyJqdGkiOiIxIiwiaXAiOiIxMjcuMC4w" +
                "LjEiLCJpc3MiOiJraXNzbyIsInVhIjoiMTIzIiwib2ciOiIyIiwiaWF0IjoxNTAyMDg1fQ.TrfBLtwc" +
                "GDeq-buzqTQjtBzX0bWX_aOOda78gnGdemOb_zjf_stHVgsaqSB42AvZvz3DEn9yMzRFcz5FwYKdc-g" +
                "Dwn02IZ-0VFtQCXA2HO4UGCa0ipMGLaTe8lujSxMhwcqFxgZAa87MUzst-Ddd516DGvvuX7vZTiw0qA" +
                "Elk_HsUCULeJXrHLIb4BxGymyIi0gUI-G9l15omJyq0GIdvWAViOOhIDGdTBG6zH77xcnceRrHz3ylT" +
                "dFMyLIqkX5A3G-wAfPMZ7tpNBeCiS9OKNpWkM1gexVLzN7l6m7J5Qj04x17UFNiiw1S5HHgo6oTz_K3i" +
                "jZPIF0DwGmhTk0DnQ");
        SSOToken ssoToken = SSOToken.parser(token, true);
        Assert.assertEquals("kisso", ssoToken.getIssuer());
    }
}
