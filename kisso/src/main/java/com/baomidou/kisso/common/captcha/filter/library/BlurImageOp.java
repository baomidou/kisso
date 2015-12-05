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
 * 图片模糊
 */
public class BlurImageOp extends AbstractConvolveImageOp {

	private static final float[][] matrix = { { 1 / 16f, 2 / 16f, 1 / 16f },
			{ 2 / 16f, 4 / 16f, 2 / 16f }, { 1 / 16f, 2 / 16f, 1 / 16f } };

	public BlurImageOp() {
		super(matrix);
	}

}
