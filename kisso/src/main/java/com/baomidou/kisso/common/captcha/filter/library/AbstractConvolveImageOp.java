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
 * 图片卷积绘制抽象类
 */
public abstract class AbstractConvolveImageOp extends AbstractImageOp {

	private float[][] matrix; 
	
	protected AbstractConvolveImageOp(float[][] matrix) {
		this.matrix = matrix;
	}

	@Override
	protected void filter(int[] inPixels, int[] outPixels, int width, int height) {
		//long time1 = System.currentTimeMillis();
		int matrixWidth = matrix[0].length;
		int matrixHeight = matrix.length;
		int mattrixLeft = - matrixWidth / 2; 
		int matrixTop = - matrixHeight / 2;
		for (int y = 0; y < height; y++) {
			int ytop = y + matrixTop;
			int ybottom = y + matrixTop + matrixHeight; 
			for (int x = 0; x < width; x++) {
				float[] sum = {0.5f, 0.5f, 0.5f, 0.5f};
				int xleft = x + mattrixLeft;
				int xright = x + mattrixLeft + matrixWidth;
				int matrixY = 0;
				for (int my = ytop; my < ybottom; my ++, matrixY++) {
					int matrixX = 0;
					for (int mx = xleft; mx < xright; mx ++, matrixX ++) {
						int pixel = getPixel(inPixels, mx, my, width, height, EDGE_ZERO);
						float m = matrix[matrixY][matrixX];
						sum[0] += m * ((pixel >> 24) & 0xff);
						sum[1] += m * ((pixel >> 16) & 0xff);
						sum[2] += m * ((pixel >> 8) & 0xff);
						sum[3] += m * (pixel & 0xff);
					}
				}
				outPixels[x + y * width] = (limitByte((int)sum[0]) << 24) | (limitByte((int)sum[1]) << 16) | (limitByte((int)sum[2]) << 8) | (limitByte((int)sum[3]));				
			}
		}
		//long time2 = System.currentTimeMillis() - time1;
		//System.out.println("AbstractConvolveImageOp " + time2);
		
	}

	
}
