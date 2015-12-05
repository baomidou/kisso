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
package com.baomidou.kisso.common.captcha.filter;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.List;

/**
 * 过滤器抽象工厂
 */
public abstract class AbstractFilterFactory implements FilterFactory {

	protected abstract List<BufferedImageOp> getFilters();

	public BufferedImage applyFilters(BufferedImage source) {
		BufferedImage dest = source;
		for (BufferedImageOp filter : getFilters()) {
			dest = filter.filter(dest, null);
		}
		int x = (source.getWidth() - dest.getWidth()) / 2;
		int y = (source.getHeight() - dest.getHeight()) / 2;
		source = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
		source.getGraphics().drawImage(dest, x, y, null);
		return source;
	}

}
