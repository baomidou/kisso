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
package com.baomidou.kisso.common;

import com.baomidou.kisso.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>
 * 获取IP地址类
 * </p>
 *
 * @author hubin
 * @since 2014-5-8
 */
@Slf4j
public class IpHelper {

    /**
     * 系统的本地IP地址
     */
    public static String LOCAL_IP;

    /**
     * 系统的本地服务器名
     */
    public static String HOST_NAME;

    static {
        String ip = "";
        try {
            InetAddress inetAddr = InetAddress.getLocalHost();
            HOST_NAME = inetAddr.getHostName();
            byte[] addr = inetAddr.getAddress();
            for (int i = 0; i < addr.length; i++) {
                if (i > 0) {
                    ip += ".";
                }
                ip += addr[i] & 0xFF;
            }
        } catch (UnknownHostException e) {
            ip = "unknown";
            log.error(e.getMessage());
        } finally {
            LOCAL_IP = ip;
        }
    }


    /**
     * <p>
     * 获取客户端的IP地址的方法是：request.getRemoteAddr()，这种方法在大部分情况下都是有效的。
     * 但是在通过了Apache,Squid等反向代理软件就不能获取到客户端的真实IP地址了，如果通过了多级反向代理的话，
     * X-Forwarded-For的值并不止一个，而是一串IP值， 究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * 例如：X-Forwarded-For：192.168.1.110, 192.168.1.120,
     * 192.168.1.130, 192.168.1.100 用户真实IP为： 192.168.1.110
     * </p>
     *
     * @param request 当前请求
     * @return IP 地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                /** 根据网卡取本机配置的IP */
                try {
                    ip = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    log.error("IpHelper error." + e.getMessage());
                }
            }
        }
        /**
         * 对于通过多个代理的情况， 第一个IP为客户端真实IP,多个IP按照','分割 "***.***.***.***".length() =
         * 15
         */
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    /**
     * <p>
     * 判断是否为本地 IP
     * </p>
     *
     * @param ip 待判断 IP
     * @return
     */
    public static boolean isLocalIp(String ip) {
        if (StringUtils.isNotEmpty(ip)) {
            try {
                InetAddress inetAddress = InetAddress.getLocalHost();
                InetAddress[] ias = InetAddress.getAllByName(inetAddress.getHostName());
                for (InetAddress ia : ias) {
                    if (ip.equals(ia.getHostAddress())) {
                        return true;
                    }
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
