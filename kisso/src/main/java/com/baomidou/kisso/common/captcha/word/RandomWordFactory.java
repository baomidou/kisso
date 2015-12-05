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
 * 随机文字内容
 */
public class RandomWordFactory implements WordFactory {

	protected String characters;

	protected int minLength;

	protected int maxLength;


	public void setCharacters( String characters ) {
		this.characters = characters;
	}


	public void setMinLength( int minLength ) {
		this.minLength = minLength;
	}


	public void setMaxLength( int maxLength ) {
		this.maxLength = maxLength;
	}


	public RandomWordFactory() {
		characters = "absdegkmnopwx23456789";
		minLength = 4;
		maxLength = 4;
	}


	public String getNextWord() {
		Random rnd = new Random();
		StringBuffer sb = new StringBuffer();
		int l = minLength + (maxLength > minLength ? rnd.nextInt(maxLength - minLength) : 0);
		for ( int i = 0 ; i < l ; i++ ) {
			int j = rnd.nextInt(characters.length());
			sb.append(characters.charAt(j));
		}
		return sb.toString();
	}


}
