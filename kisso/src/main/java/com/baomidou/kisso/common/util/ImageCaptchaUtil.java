/**
 * Copyright (c) 2011-2014, hubin (243194995@qq.com).
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
package com.baomidou.kisso.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * <p>
 * 图片验证码工具类
 * </p>
 * 
 * @author hubin
 * @Date 2015-11-17
 */
public class ImageCaptchaUtil {
	/***
	 * 去掉了0,1,4,i,l,o几个容易混淆的字符
	 */
	private static final String CAPTCHA_CHARS = "abcdefghjkmnpqrstuvwxyz2356789";
	private static Random rand = new Random();

	/**
	 * 输出图片验证码
	 * 
	 * @param out
	 *            输出流
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 * @param length
	 *            长度
	 * @return String 验证码
	 * @throws IOException
	 */
	public static String outputImage(OutputStream out, int width, int height, int captchaLength) throws IOException {
		return outputImage(out, _getRandColor(10, 240), "Arial", Font.ITALIC, width, height, captchaLength);
	}

	/**
	 * 输出图片验证码
	 * 
	 * @param out
	 *            输出流
	 * @param fontColor
	 *            字体颜色
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 * @param length
	 *            长度
	 * @return String 验证码
	 * @throws IOException
	 */
	public static String outputImage(OutputStream out, Color fontColor, int width, int height, int captchaLength)
			throws IOException {
		return outputImage(out, fontColor, "Verdana", Font.PLAIN, width, height, captchaLength);
	}

	/**
	 * 输出图片验证码
	 * 
	 * @param out
	 *            输出流
	 * @param fontColor
	 *            字体颜色
	 * @param fontName
	 *            字体名称
	 * @param fontStyle
	 *            字体样式
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 * @param length
	 *            长度
	 * @return String 验证码
	 * @throws IOException
	 */
	public static String outputImage(OutputStream out, Color fontColor, String fontName, int fontStyle, int width,
			int height, int length) throws IOException {
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) bi.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		/* 绘制干扰线 */
		for (int i = 0; i < 20; i++) {
			g.setColor(_getRandColor(150, 250));
			g.drawOval(rand.nextInt(width), rand.nextInt(height), 5 + rand.nextInt(10), 5 + rand.nextInt(10));
		}
		int fontSize = height - 4;
		g.setFont(new Font(fontName, fontStyle, fontSize));
		g.setColor(fontColor);
		String captcha = _getCaptcha(length);
		for (int i = 0; i < length; i++) {
			AffineTransform affine = new AffineTransform();
			affine.setToRotation(Math.PI / length * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1),
					(width / length) * i + fontSize / 2, height / 2);
			g.setTransform(affine);
			g.drawChars(captcha.toCharArray(), i, 1, ((width - 10) / length) * i + 5, height / 2 + fontSize / 2 - 10);
		}
		g.dispose();
		ImageIO.write(bi, "png", out);
		return captcha;
	}

	/**
	 * 使用系统默认字符源生成验证码
	 * 
	 * @param length
	 *            长度
	 * @return
	 */
	protected static String _getCaptcha(int length) {
		int codesLen = CAPTCHA_CHARS.length();
		Random rand = new Random(System.currentTimeMillis());
		StringBuilder verifyCode = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int j = rand.nextInt(codesLen);
			/* 0、随机大小写 */
			if (rand.nextBoolean()) {
				verifyCode.append(CAPTCHA_CHARS.toUpperCase().charAt(j));
			} else {
				verifyCode.append(CAPTCHA_CHARS.charAt(j));
			}
		}
		return verifyCode.toString();
	}

	/**
	 * 给定范围获得随机颜色
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	protected static Color _getRandColor(int fc, int bc) {
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + rand.nextInt(bc - fc);
		int g = fc + rand.nextInt(bc - fc);
		int b = fc + rand.nextInt(bc - fc);
		return new Color(r, g, b);
	}

}
