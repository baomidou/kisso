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

import java.awt.image.BufferedImage;

import com.baomidou.kisso.common.captcha.background.BackgroundFactory;
import com.baomidou.kisso.common.captcha.color.ColorFactory;
import com.baomidou.kisso.common.captcha.filter.FilterFactory;
import com.baomidou.kisso.common.captcha.font.FontFactory;
import com.baomidou.kisso.common.captcha.text.renderer.TextRenderer;
import com.baomidou.kisso.common.captcha.word.WordFactory;

/**
 * 验证码服务抽象类
 */
public abstract class AbstractCaptchaService implements CaptchaService {

	protected FontFactory fontFactory;

	protected WordFactory wordFactory;

	protected ColorFactory colorFactory;

	protected BackgroundFactory backgroundFactory;

	protected TextRenderer textRenderer;

	protected FilterFactory filterFactory;

	/** 宽 */
	protected int width;

	/** 高 */
	protected int height;


	public void setFontFactory( FontFactory fontFactory ) {
		this.fontFactory = fontFactory;
	}


	public void setWordFactory( WordFactory wordFactory ) {
		this.wordFactory = wordFactory;
	}


	public void setColorFactory( ColorFactory colorFactory ) {
		this.colorFactory = colorFactory;
	}


	public void setBackgroundFactory( BackgroundFactory backgroundFactory ) {
		this.backgroundFactory = backgroundFactory;
	}


	public void setTextRenderer( TextRenderer textRenderer ) {
		this.textRenderer = textRenderer;
	}


	public void setFilterFactory( FilterFactory filterFactory ) {
		this.filterFactory = filterFactory;
	}


	public FontFactory getFontFactory() {
		return fontFactory;
	}


	public WordFactory getWordFactory() {
		return wordFactory;
	}


	public ColorFactory getColorFactory() {
		return colorFactory;
	}


	public BackgroundFactory getBackgroundFactory() {
		return backgroundFactory;
	}


	public TextRenderer getTextRenderer() {
		return textRenderer;
	}


	public FilterFactory getFilterFactory() {
		return filterFactory;
	}


	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}


	public void setWidth( int width ) {
		this.width = width;
	}


	public void setHeight( int height ) {
		this.height = height;
	}


	public Captcha getCaptcha() {
		BufferedImage bufImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		backgroundFactory.fillBackground(bufImage);
		String word = wordFactory.getNextWord();
		textRenderer.draw(word, bufImage, fontFactory, colorFactory);
		bufImage = filterFactory.applyFilters(bufImage);
		return new Captcha(word, bufImage);
	}

}
