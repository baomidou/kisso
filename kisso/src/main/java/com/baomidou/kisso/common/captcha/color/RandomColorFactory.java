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
package com.baomidou.kisso.common.captcha.color;

import java.awt.*;
import java.util.Random;

/**
 * 随机颜色
 */
public class RandomColorFactory implements ColorFactory {

	private Color min;
	private Color max;
	private Color color;

	public RandomColorFactory() {
		min = new Color(20,40,80);
		max = new Color(21,50,140);
	}
	
	public void setMin(Color min) {
		this.min = min;
	}

	public void setMax(Color max) {
		this.max = max;
	}

	public Color getColor(int index) {
		if (color == null) {
			Random r = new Random();
			color = new Color( min.getRed() + r.nextInt((max.getRed() - min.getRed())),
					min.getGreen() + r.nextInt((max.getGreen() - min.getGreen())),
					min.getBlue() + r.nextInt((max.getBlue() - min.getBlue())));
		}
		return color;
	}

}
