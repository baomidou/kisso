/**
 * Copyright (c) 2011-2014, hubin (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.kisso.web.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.kisso.SSOConfig;

/**
 * <p>
 * SSO 默认拦截处理器，自定义 Handler 可继承该类。
 * </p>
 * 
 * @author hubin
 * @Date 2015-12-19
 */
public class KissoDefaultHandler implements SSOHandlerInterceptor {
	private static KissoDefaultHandler handler;

	/**
	 * new 当前对象
	 */
	public static KissoDefaultHandler getInstance() {
		if (handler == null) {
			handler = new KissoDefaultHandler();
		}
		return handler;
	}

	public boolean preTokenIsNullAjax(HttpServletRequest request, HttpServletResponse response) {
		/*
		 * 未登录时，处理 AJAX 请求。
		 */
		try {
			response.setContentType("text/html;charset=" + SSOConfig.getSSOEncoding());
			PrintWriter out = response.getWriter();
			out.print("{flag:\"-1\",msg:\"login timeout, please login again.\"}");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean preTokenIsNull(HttpServletRequest request, HttpServletResponse response) {
		/* 预留子类处理 */
//		/*
//		 * 当前角色非空，并且为业务系统, 执行跨域逻辑。
//		 * <p>
//		 * 1、发出请求询问 SSO 是否登录, 私钥签名 authToken 并设置临时 cookie
//		 * </p>
//		 */
//		String role = SSOConfig.getInstance().getRole();
//		if (!"".equals(role) && !SSOConfig.SSO_ROLE.equals(role)) {
//			String myPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMp4ORF5r+mBfYrpX944JDyQiaKof5CQ43H65tWS8wT2avQjiUSU2vweXVDbMRD7PPjOAPI5aiIIHFLBqAE9Nq17Xloyz66mUkvvhJxbmB24FhyhB8dREL85EIUahrljrTj2nWWGjDfXIqQoY1NhBi6l2VHgcVuoRI48UzAWcbSBAgMBAAECgYAtLeaOH7lBQcPh23GpBJ4RZa9QvIi6mZonNPWNct0HnnT/RW67/vtehugLwt2QDH/uhQlxA57LOUQYs13p6N7qMZ+4YY592hw4hrJUEAuuORU+wKWnr+wVQNm6Qc9Qf7axM6B5NgtLPbf0R7M53vgHHMyJh2tJKrY3RUdBbsUugQJBAObj3+B7v2QVKKPZlYvICwbKZAUcb1qZtPjtw7+aDah0EEqkaYD0ytmjl2esoknPySN2gbouc+nDvYZopFLgiDMCQQDgfRqCYfMHhjHPHoOwco3ZAevDDe22QksBIkfgFB9srEJCWauFyvB5PTG6+wFv94zqy3R92C6AVaWn8Ae8uqx7AkBkroWXfB7PY7KfEGh31bmJMoQ+/lFIbrJNwlCTonfGNyZLhjpDc3tpQD7rhIoYKbWJ80lKiKsfCq4AiGzvft2lAkEAqcBQDGmu0XC7N2hWolVtR7x5H8znhNuKRfg7K4lr3cxAalXOKuSzhKoucbqecqFZsK5aj1Kqjya0llIeN6tdAwJAImLxsxLxhk6dc8slEo8ObLAWWWkRZNiXCpr+2aWspVx1cK3GRtAa+0Q7X0TiA62/CrlWR/xJHvDI/+I9mcxJKg==";
//			String axt = SSOHelper.askCiphertext(request, response, myPrivateKey);
//			HttpUtil.sendRedirect(response, "http://sso.test.com:8080/replylogin.html?askTxt=" + axt);
//		} else {
//			/*
//			 * 清理登录状态并重定向至登录界面
//			 */
//			logger.fine("logout. request url:" + request.getRequestURL());
//			SSOHelper.clearRedirectLogin(request, response);
//		}
		return true;
	}

}
