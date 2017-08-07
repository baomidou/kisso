package com.baomidou.kisso;

import org.junit.Assert;
import org.junit.Test;

import com.baomidou.kisso.security.token.SSOToken;

public class TestSSOToken {

    @Test
    public void getToken() {
        String token = SSOToken.create().setIp("127.0.0.1").setTime(1502085277L).setId(1)
                .setUserAgent("123").setIssuer("kisso").getToken();
        Assert.assertEquals(token, "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIxIiwiaXAiOiIxMjcuMC4wLjEiLCJpc3MiOi" +
                "JraXNzbyIsInVhIjoiMTIzIiwiaWF0IjoxNTAyMDg1fQ.pxPoqRVmwV0YN_UO-IiTQOeuR-ndtkQVEldVUQRAuLewaeOj" +
                "BxV3--kSGKTeY8wvQ1Ljb4BF2Ph6nZ5JQquH3Q");
    }
}
