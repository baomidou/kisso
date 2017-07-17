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
package com.baomidou.kisso.common.captcha.font;

import java.util.Random;

import com.baomidou.kisso.common.captcha.word.RandomWordFactory;

/**
 * 随机字符大小写
 */
public class UpperRandomWordFactory extends RandomWordFactory {

	/**
	 * 重载父类获取字符方法
	 * 支持随机大小写字符
	 */
	@Override
	public String getNextWord() {
		Random rnd = new Random();
		StringBuffer sb = new StringBuffer();
		int l = this.minLength + (this.maxLength > this.minLength ? rnd.nextInt(this.maxLength - this.minLength) : 0);
		for (int i = 0; i < l; i++) {
			int j = rnd.nextInt(this.characters.length());
			if (rnd.nextBoolean()) {
				sb.append(this.characters.toUpperCase().charAt(j));
			} else {
				sb.append(this.characters.charAt(j));
			}
		}
		return sb.toString();
	}
}
