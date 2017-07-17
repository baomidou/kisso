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
package com.baomidou.kisso.common.captcha.background;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 椭圆形噪点干扰背景
 */
public class OvalNoiseBackgroundFactory implements BackgroundFactory {
	private static final Random rand = new Random();
	
	/** 噪点数量 */
	private int noises = 20;

	public OvalNoiseBackgroundFactory() {
		
	}
	
	public OvalNoiseBackgroundFactory( int noises ) {
		this.noises = noises;
	}

	public void fillBackground( BufferedImage image ) {
		Graphics graphics = image.getGraphics();

		//验证码图片的宽高
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();

		//填充为白色背景
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, imgWidth, imgHeight);

		/**
		 * 画 20 个椭圆形噪点(颜色及位置随机)
		 */
		for ( int i = 0 ; i < getNoises() ; i++ ) {
			graphics.setColor(_getRandColor(150, 250));
			graphics.drawOval(rand.nextInt(imgWidth), rand.nextInt(imgHeight), 5 + rand.nextInt(10),
				5 + rand.nextInt(10));
		}
	}

	/**
	 * 给定范围获得随机颜色
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	protected static Color _getRandColor( int fc, int bc ) {
		if ( fc > 255 ) {
			fc = 255;
		}
		if ( bc > 255 ) {
			bc = 255;
		}
		int r = fc + rand.nextInt(bc - fc);
		int g = fc + rand.nextInt(bc - fc);
		int b = fc + rand.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	public int getNoises() {
		return noises;
	}

	public void setNoises( int noises ) {
		this.noises = noises;
	}
}
