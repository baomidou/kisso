/**
 * Copyright (c) 2011-2014, hubin (jobob@qq.com).
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
package com.baomidou.kisso.common;

import com.baomidou.kisso.SSOAuthorization;
import com.baomidou.kisso.SSOCache;
import com.baomidou.kisso.common.auth.AuthDefaultImpl;
import com.baomidou.kisso.exception.KissoException;
import com.baomidou.kisso.security.token.SSOToken;
import com.baomidou.kisso.security.token.Token;

/**
 * <p>
 * SSO 反射辅助类
 * </p>
 *
 * @author hubin
 * @Date 2015-12-05
 */
public class SSOReflectHelper {

    private static SSOCache cache = null;

    private static SSOAuthorization authorization = null;

    /**
     * <p>
     * 反射获取自定义Token
     * </p>
     *
     * @return {@link SSOToken}
     */
    public static SSOToken getConfigSSOToken(String tokenClass) {
        /**
         * 判断是否自定义 Token 默认 SSOToken
         */
        SSOToken token = null;
        if (tokenClass == null || "".equals(tokenClass)) {
            token = new SSOToken();
        } else {
            try {
                Class<?> tc = Class.forName(tokenClass);
                if (tc.newInstance() instanceof Token) {
                    token = (SSOToken) tc.newInstance();
                }
            } catch (Exception e) {
                throw new KissoException(e);
            }
        }
        return token;
    }

    /**
     * /**
     * <p>
     * 反射获取自定义SSOCache
     * </p>
     *
     * @return {@link SSOCache}
     */
    public static SSOCache getConfigCache(String cacheClass) {

        if (cache != null) {
            return cache;
        }

        /**
         * 反射获得缓存类
         */
        if (cacheClass != null && !"".equals(cacheClass)) {
            try {
                Class<?> tc = Class.forName(cacheClass);
                if (tc.newInstance() instanceof SSOCache) {
                    cache = (SSOCache) tc.newInstance();
                }
            } catch (Exception e) {
                throw new KissoException(e);
            }
        }
        return cache;
    }


    public static void setConfigCache(SSOCache configcache) {
        cache = configcache;
    }

    /**
     * 反射获取自定义SSOAuthorization
     *
     * @return {@link SSOAuthorization}
     */
    public static SSOAuthorization getAuthorization(String authorizationClass) {
        if (authorization != null) {
            return authorization;
        }

        /**
         * 反射获得权限类
         */
        if (authorizationClass != null && !"".equals(authorizationClass)) {
            try {
                Class<?> tc = Class.forName(authorizationClass);
                if (tc.newInstance() instanceof SSOAuthorization) {
                    authorization = (SSOAuthorization) tc.newInstance();
                }
            } catch (Exception e) {
                throw new KissoException(e);
            }
        } else {
            authorization = new AuthDefaultImpl();
        }

        return authorization;
    }

    public static void setAuthorization(SSOAuthorization authorization) {
        SSOReflectHelper.authorization = authorization;
    }

}
