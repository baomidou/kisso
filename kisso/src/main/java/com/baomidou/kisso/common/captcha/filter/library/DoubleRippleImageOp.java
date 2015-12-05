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
package com.baomidou.kisso.common.captcha.filter.library;

/**
 * 图片双波纹
 */
public class DoubleRippleImageOp extends RippleImageOp {

	@Override
	protected void transform(int x, int y, double[] t) {
		double tx = Math.sin((double) y / yWavelength + yRandom) + 1.3 * Math.sin((double) 0.6 * y / yWavelength  + yRandom);
		double ty = Math.cos((double) x / xWavelength + xRandom) + 1.3 * Math.cos((double) 0.6 * x / xWavelength + xRandom);
		t[0] = x + xAmplitude * tx;
		t[1] = y + yAmplitude * ty;
	}

}
