/**
 * Copyright (c) 2011-2014, hubin (243194995@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baomidou.kisso;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.baomidou.kisso.common.util.ImageCaptchaUtil;

/**
 * <p>
 * 测试图片验证码工具类
 * </p>
 * 
 * @author hubin
 * @Date 2015-11-17
 */
public class TestImageCaptchaUtil {

	/**
	 * 测试
	 */
	public static void main(String[] args) throws IOException {
		File dir = new File(System.getProperty("user.dir") + "/verifies");
		if (!dir.exists()) {
			dir.mkdir();
		}

		/* 1、输出随机颜色字体验证码 */
		String c1 = ImageCaptchaUtil.outputImage(new FileOutputStream(new File(dir, "test1.png")), 200, 80, 4);
		System.out.println("验证码 c1 = " + c1);

		/* 2、输出随机绿色字体验证码 */
		String c2 = ImageCaptchaUtil.outputImage(new FileOutputStream(new File(dir, "test2.png")), Color.GREEN, 200, 80, 4);
		System.out.println("验证码 c2 = " + c2);

		/* 3、输出随机蓝色指定样式字体验证码 */
		String c3 = ImageCaptchaUtil.outputImage(new FileOutputStream(new File(dir, "test3.png")), Color.blue, "Algerian",
				Font.ITALIC, 200, 80, 4);
		System.out.println("验证码 c3 = " + c3);
	}

}
