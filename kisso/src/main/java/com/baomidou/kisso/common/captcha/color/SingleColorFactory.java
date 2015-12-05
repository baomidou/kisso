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

/**
 * 单一颜色
 */
public class SingleColorFactory implements ColorFactory {

	private Color color;
	
	public SingleColorFactory() {
		color = Color.BLACK;
	}

	public SingleColorFactory(Color color) {
		this.color = color;
	}
	
	public Color getColor(int index) {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
