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
package com.baomidou.kisso.web.auth;

import com.baomidou.kisso.common.SSOConstants;
import com.baomidou.kisso.common.util.Base64Util;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * webflux 资源授权访问 Basic 认证过滤器
 *
 * @author hubin
 * @since 2023-02-28
 */
public class BasicAuthenticateWebfluxFilter implements WebFilter, Ordered {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 路径拦截处理函数
     */
    private Function<String, Boolean> pathIntercept;

    public BasicAuthenticateWebfluxFilter(String username, String password) {
        this(username, password, null);
    }

    public BasicAuthenticateWebfluxFilter(String username, String password, Function<String, Boolean> pathIntercept) {
        this.username = username;
        this.password = password;
        if (null == pathIntercept) {
            // 默认拦截 /actuator 健康检查地址
            this.pathIntercept = path -> path.contains(SSOConstants.BASIC_ACTUATOR);
        } else {
            this.pathIntercept = pathIntercept;
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Boolean filter = pathIntercept.apply(request.getURI().getPath());
        if (null == filter || !filter) {
            // 非健康检查地址直接跳过
            return chain.filter(exchange);
        }
        // 请求头票据合法性验证
        boolean certified = false;
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (null != authorization && authorization.startsWith(SSOConstants.BASIC)
                && authorization.length() > 5) {
            // 认证用户名密码
            String token = authorization.substring(5).trim();
            String content = username + ":" + password;
            certified = Base64Util.encode(content.getBytes()).equalsIgnoreCase(token);
        }
        if (!certified) {
            // 非法访问返回 401
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return Mono.empty();
        }
        // 认证合法
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
