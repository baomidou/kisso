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

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/**
 * 字符文本渲染
 */
public class TextCharacter {

	private double x;
	private double y;
	private double width;
	private double height;
	private double ascent;
	private double descent;
	private char character;
	private Font font;
	private Color color;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public double getAscent() {
		return ascent;
	}

	public void setAscent(double ascent) {
		this.ascent = ascent;
	}

	public double getDescent() {
		return descent;
	}

	public void setDescent(double descent) {
		this.descent = descent;
	}

	public AttributedCharacterIterator iterator() {
		AttributedString aString = new AttributedString(String
				.valueOf(character));
		aString.addAttribute(TextAttribute.FONT, font, 0, 1);
		return aString.getIterator();
	}

}
