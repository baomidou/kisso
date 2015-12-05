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
package com.baomidou.kisso.common.captcha.utils.encoder;

import javax.imageio.ImageIO;

import com.baomidou.kisso.common.captcha.service.Captcha;
import com.baomidou.kisso.common.captcha.service.CaptchaService;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 验证码编码辅助类
 */
public class EncoderHelper {

	public static String getChallangeAndWriteImage(CaptchaService service, String format, OutputStream os) throws IOException {
		Captcha captcha = service.getCaptcha();
		ImageIO.write(captcha.getImage(), format, os); 
		return captcha.getChallenge();
	}
	
}

