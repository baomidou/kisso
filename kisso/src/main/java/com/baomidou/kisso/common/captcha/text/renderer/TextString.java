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
package com.baomidou.kisso.common.captcha.text.renderer;

import java.util.ArrayList;

/**
 * 字符串文本
 */
public class TextString {

	private ArrayList<TextCharacter> characters = new ArrayList<TextCharacter>();
	
	public void clear() {
		characters.clear();
	}
	
	public void addCharacter(TextCharacter tc) {
		characters.add(tc);
	}
	
	public ArrayList<TextCharacter> getCharacters() {
		return characters;
	}
	
	public double getWidth() {
		double minx = 0;
		double maxx = 0;
		boolean first = true;
		for (TextCharacter tc : characters) {
			if (first) {
				minx = tc.getX();
				maxx = tc.getX() + tc.getWidth();
				first = false;
			} else {
				if (minx > tc.getX()) {
					minx = tc.getX(); 
				}
				if (maxx < tc.getX() + tc.getWidth()) {
					maxx = tc.getX() + tc.getWidth();
				}
			}
			
		}
		return maxx - minx;
	}
	
	public double getHeight() {
		double miny = 0;
		double maxy = 0;
		boolean first = true;
		for (TextCharacter tc : characters) {
			if (first) {
				miny = tc.getY();
				maxy = tc.getY() + tc.getHeight();
				first = false;
			} else {
				if (miny > tc.getY()) {
					miny = tc.getY(); 
				}
				if (maxy < tc.getY() + tc.getHeight()) {
					maxy = tc.getY() + tc.getHeight();
				}
			}
			
		}
		return maxy - miny;
	}

}
