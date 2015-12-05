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

import java.util.Random;

/**
 * 图片扩散
 */
public class DiffuseImageOp extends AbstractTransformImageOp {
	double[] tx;
	double[] ty;
	double amount;

	public DiffuseImageOp() {
		amount = 1.6;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	protected synchronized void init() {
		tx = new double[256];
		ty = new double[256];
		for (int i = 0; i < 256; i++) {
			double angle = 2 * Math.PI * i / 256;
			tx[i] = amount * Math.sin(angle);
			ty[i] = amount * Math.cos(angle);
		}
	}

	@Override
	protected void transform(int x, int y, double[] t) {
		Random r = new Random();
		int angle = (int) (r.nextFloat() * 255);
		t[0] = x + tx[angle];
		t[1] = y + ty[angle];
	}

}
