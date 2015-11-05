/**
 * Copyright (c) 2011-2014, hubin (243194995@qq.com).
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
package com.baomidou.kisso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * <p>
 * SSO票据顶级父类
 * </p>
 * 
 * @author hubin
 * @Date 2015-11-30
 */
public class Token {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 登录 IP
	 */
	private String ip;
	/**
	 * Token 状态标示
	 * <p>
	 * 默认正常执行
	 * </p>
	 */
	private Flag flag = Flag.NORMAL;

	/**
	 * Token转为JSON格式
	 * <p>
	 * 
	 * @return JSON格式Token值
	 */
	public String jsonToken() {
		return JSON.toJSONString(this);
	}

	/**
	 * JSON格式Token值转为Token对象
	 * <p>
	 * 
	 * @param jsonToken
	 *            JSON格式Token值
	 * @return Token对象
	 */
	public Token parseToken(String jsonToken) {
		return JSON.parseObject(jsonToken, this.getClass());
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Flag getFlag() {
		return flag;
	}

	public void setFlag(Flag flag) {
		this.flag = flag;
	}

	/**
	 * <p>
	 * Token状态标示
	 * </p>
	 */
	public enum Flag {
		/* 正常 */
		NORMAL("0", "正常执行"),

		/* 缓存宕机 */
		CACHE_SHUT("1", "缓存可能关闭或宕机");

		private final String key;
		private final String desc;

		private Flag(String key, String desc) {
			this.key = key;
			this.desc = desc;
		}

		public String key() {
			return this.key;
		}

		public String desc() {
			return this.desc;
		}
	}
}
