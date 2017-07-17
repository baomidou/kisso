/**
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
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
package com.baomidou.kisso.common.captcha;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 抽象图片验证码接口
 * </p>
 * 
 * @author hubin
 * @Date 2016-08-13
 */
public abstract class AbstractCaptcha implements ICaptcha {

	/*
	 * 图片验证码票据存储接口
	 */
	private ICaptchaStore captchaStore;

	/*
	 * 是否忽略验证内容大小写，默认 true
	 */
	private boolean ignoreCase = true;

	protected AbstractCaptcha() {
		/* 保护 */
	}

	public AbstractCaptcha(ICaptchaStore captchaStore) {
		this.captchaStore = captchaStore;
	}

	/**
	 * <p>
	 * 生成图片验证码
	 * </p>
	 * 
	 * @param request
	 * @param out
	 *            输出流
	 * @return String 验证码内容
	 */
	public abstract String writeImage(HttpServletRequest request, OutputStream out);

	//@Override
	public void generate(HttpServletRequest request, OutputStream out, String ticket) {
		String captcha = writeImage(request, out);
		if (captcha != null) {
			captchaStore.put(ticket, captcha);
		}
	}

	//@Override
	public boolean verification(HttpServletRequest request, String ticket, String captcha) {
		String tc = captchaStore.get(ticket);
		if (tc != null) {
			if (ignoreCase) {
				return tc.equalsIgnoreCase(captcha);
			} else {
				return tc.equals(captcha);
			}
		}
		return false;
	}

	public ICaptchaStore getCaptchaStore() {
		return captchaStore;
	}

	public void setCaptchaStore(ICaptchaStore captchaStore) {
		this.captchaStore = captchaStore;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

}
