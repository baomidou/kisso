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
package com.baomidou.kisso.common.captcha.word;

import java.util.Random;

/**
 * 自适应随机文字内容
 */
public class AdaptiveRandomWordFactory extends RandomWordFactory {

	protected String wideCharacters;

	public void setWideCharacters(String wideCharacters) {
		this.wideCharacters = wideCharacters;
	}

	/***
	 * 去掉了0,1,4,i,l,o几个容易混淆的字符
	 */
	public AdaptiveRandomWordFactory() {
		characters = "abcdefghjkmnpqrstuvwxyz2356789";
		wideCharacters = "mw";
	}
	
	@Override
	public String getNextWord() {
		Random rnd = new Random();
		StringBuffer sb = new StringBuffer();
		StringBuffer chars = new StringBuffer(characters);
		int l = minLength + (maxLength > minLength ? rnd.nextInt(maxLength - minLength) : 0);
		for (int i = 0; i < l; i++) {
			int j = rnd.nextInt(chars.length());
			char c = chars.charAt(j);
			if (wideCharacters.indexOf(c) != -1) {
				for (int k = 0; k < wideCharacters.length(); k++) {
					int idx = chars.indexOf(String.valueOf(wideCharacters.charAt(k)));
					if (idx != -1) {
						chars.deleteCharAt(idx);
					}
				}
			}
			sb.append(c);
		}
		return sb.toString();
	}

}
