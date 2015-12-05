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
 * 图片波纹
 */
public class RippleImageOp extends AbstractTransformImageOp {
	protected double xWavelength;
	protected double yWavelength;
	protected double xAmplitude;
	protected double yAmplitude;
	protected double xRandom;
	protected double yRandom;

	public RippleImageOp() {
		xWavelength = 20;
		yWavelength = 10;
		xAmplitude = 5;
		yAmplitude = 5;
		xRandom = 5 * Math.random();
		yRandom = 5 * Math.random();
	}

	public double getxWavelength() {
		return xWavelength;
	}

	public void setxWavelength(double xWavelength) {
		this.xWavelength = xWavelength;
	}

	public double getyWavelength() {
		return yWavelength;
	}

	public void setyWavelength(double yWavelength) {
		this.yWavelength = yWavelength;
	}

	public double getxAmplitude() {
		return xAmplitude;
	}

	public void setxAmplitude(double xAmplitude) {
		this.xAmplitude = xAmplitude;
	}

	public double getyAmplitude() {
		return yAmplitude;
	}

	public void setyAmplitude(double yAmplitude) {
		this.yAmplitude = yAmplitude;
	}

	@Override
	protected void transform(int x, int y, double[] t) {
		double tx = Math.sin((double) y / yWavelength + yRandom);
		double ty = Math.cos((double) x / xWavelength + xRandom);
		t[0] = x + xAmplitude * tx;
		t[1] = y + yAmplitude * ty;
	}

}
