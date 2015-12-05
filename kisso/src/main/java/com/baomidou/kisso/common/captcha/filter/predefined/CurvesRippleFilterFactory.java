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
package com.baomidou.kisso.common.captcha.filter.predefined;

import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.kisso.common.captcha.color.ColorFactory;
import com.baomidou.kisso.common.captcha.filter.library.CurvesImageOp;

/**
 * 曲线波纹过滤器
 */
public class CurvesRippleFilterFactory extends RippleFilterFactory {

	protected CurvesImageOp curves = new CurvesImageOp();

	public CurvesRippleFilterFactory() {
	}

	public CurvesRippleFilterFactory(ColorFactory colorFactory) {
		setColorFactory(colorFactory);
	}

	@Override
	protected List<BufferedImageOp> getPreRippleFilters() {
		List<BufferedImageOp> list = new ArrayList<BufferedImageOp>();
		list.add(curves);
		return list;
	}

	public void setStrokeMin(float strokeMin) {
		curves.setStrokeMin(strokeMin);
	}

	public void setStrokeMax(float strokeMax) {
		curves.setStrokeMax(strokeMax);
	}

	public void setColorFactory(ColorFactory colorFactory) {
		curves.setColorFactory(colorFactory);
	}

}
