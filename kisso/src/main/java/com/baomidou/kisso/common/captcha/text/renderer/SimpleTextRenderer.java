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
package com.baomidou.kisso.common.captcha.text.renderer;

/**
 * 简单文本渲染
 */
public class SimpleTextRenderer extends AbstractTextRenderer {

	@Override
	protected void arrangeCharacters(int width, int height, TextString ts) {
		double x = leftMargin;
		for (TextCharacter tc : ts.getCharacters()) {
			double y = topMargin + (height + tc.getAscent() * 0.7) / 2;
			tc.setX(x);
			tc.setY(y);
			x += tc.getWidth();
		}
	}

}
