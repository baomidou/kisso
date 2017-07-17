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

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.kisso.common.captcha.service.AbstractCaptchaService;
import com.baomidou.kisso.common.captcha.service.ConfigurableCaptchaService;
import com.baomidou.kisso.common.captcha.utils.encoder.EncoderHelper;

/**
 * <p>
 * 默认图片验证码实现
 * </p>
 * 
 * @author hubin
 * @Date 2016-08-13
 */
public class DefaultCaptcha extends AbstractCaptcha {

	/*
	 * 抽象验证码服务
	 */
	private AbstractCaptchaService captchaService;

	/*
	 * 图片格式，默认 png
	 */
	private String format = "png";

	@Override
	public String writeImage(HttpServletRequest request, OutputStream out) {
		if (captchaService == null) {
			captchaService = new ConfigurableCaptchaService();
		}
		try {
			return EncoderHelper.getChallangeAndWriteImage(captchaService, format, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public AbstractCaptchaService getCaptchaService() {
		return captchaService;
	}

	public void setCaptchaService(AbstractCaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
