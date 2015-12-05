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

import java.util.Random;

/**
 * 随机Y轴更好的填充文本渲染
 */
public class RandomYBestFitTextRenderer extends AbstractTextRenderer {

	@Override
	protected void arrangeCharacters(int width, int height, TextString ts) {
		double widthRemaining = (width - ts.getWidth() - leftMargin - rightMargin) / ts.getCharacters().size();
		double vmiddle = height / 2;
		double x = leftMargin + widthRemaining / 2;
		Random r = new Random();
		height -= topMargin + bottomMargin;
		for (TextCharacter tc : ts.getCharacters()) {
			double heightRemaining = height - tc.getHeight();
			double y = vmiddle + 0.35 * tc.getAscent() + (1 - 2 * r.nextDouble()) * heightRemaining;
			tc.setX(x);
			tc.setY(y);
			x += tc.getWidth() + widthRemaining;
		}
	}

}
