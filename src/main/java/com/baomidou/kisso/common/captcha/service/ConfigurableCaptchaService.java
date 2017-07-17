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
package com.baomidou.kisso.common.captcha.service;

import com.baomidou.kisso.common.captcha.background.SingleColorBackgroundFactory;
import com.baomidou.kisso.common.captcha.color.SingleColorFactory;
import com.baomidou.kisso.common.captcha.filter.predefined.CurvesRippleFilterFactory;
import com.baomidou.kisso.common.captcha.font.RandomFontFactory;
import com.baomidou.kisso.common.captcha.font.UpperRandomWordFactory;
import com.baomidou.kisso.common.captcha.text.renderer.BestFitTextRenderer;

/**
 * 默认配置验证码服务
 */
public class ConfigurableCaptchaService extends AbstractCaptchaService {

	public ConfigurableCaptchaService() {
		backgroundFactory = new SingleColorBackgroundFactory();
		wordFactory = new UpperRandomWordFactory();
		fontFactory = new RandomFontFactory();
		textRenderer = new BestFitTextRenderer();
		colorFactory = new SingleColorFactory();
		filterFactory = new CurvesRippleFilterFactory(colorFactory);
		textRenderer.setLeftMargin(10);
		textRenderer.setRightMargin(10);
		width = 160;
		height = 70;
	}

}
