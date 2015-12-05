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
 * 图片大理石纹
 */
public class MarbleImageOp extends AbstractTransformImageOp {
	double scale;
	double amount;
	double turbulence;
	double[] tx;
	double[] ty;
	double randomX;
	double randomY;
	
	public MarbleImageOp() {
		scale = 15;
		amount = 1.1;
		turbulence = 6.2;
		randomX = 256 * Math.random();
		randomY = 256 * Math.random();
	}
	
	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getTurbulence() {
		return turbulence;
	}

	public void setTurbulence(double turbulence) {
		this.turbulence = turbulence;
	}

	@Override
	protected synchronized void init() {
		tx = new double[256];
		ty = new double[256];
		for (int i = 0; i < 256; i++) {
			double angle = 2 * Math.PI * i * turbulence / 256;
			tx[i] = amount * Math.sin(angle);
			ty[i] = amount * Math.cos(angle);
		}
	}
	
	@Override
	protected void transform(int x, int y, double[] t) {
		int d = limitByte((int) (127 * (1 + PerlinNoise.noise2D(((double)x) / scale + randomX, ((double)y) / scale + randomY))));
		t[0] = x + tx[d];
		t[1] = y + ty[d];
	}
	
	protected void filter2(int[] inPixels, int[] outPixels, int width, int height) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = limitByte((int) (127 * (1 + PerlinNoise.noise2D(((double)x) / scale + randomX, ((double)y) / scale + randomY))));
				outPixels[x + y * width] = (limitByte((int)255) << 24) | (limitByte((int)pixel) << 16) | (limitByte((int)pixel) << 8) | (limitByte((int)pixel));				
			}
		}
	}
	

}
