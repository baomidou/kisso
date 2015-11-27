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
package com.baomidou.kisso.common.util;

import com.baomidou.kisso.SSOCache;
import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOStatistic;
import com.baomidou.kisso.SSOToken;
import com.baomidou.kisso.Token;
import com.baomidou.kisso.common.encrypt.AES;
import com.baomidou.kisso.common.encrypt.SSOEncrypt;
import com.baomidou.kisso.common.parser.FastJsonParser;
import com.baomidou.kisso.common.parser.SSOParser;
import com.baomidou.kisso.exception.KissoException;

/**
 * <p>
 * 反射工具类
 * </p>
 * 
 * @author hubin
 * @Date 2014-6-27
 */
public class ReflectUtil {
	private static SSOEncrypt encrypt = null;
	private static SSOCache cache = null;
	private static SSOStatistic statistic = null;
	private static SSOParser parser = null;

	/**
	 * 反射初始化
	 */
	public static void init() {
		getConfigEncrypt();
		getConfigStatistic();
		getConfigCache();
	}

	/**
	 * 反射获取自定义Encrypt
	 * 
	 * @return {@link Encrypt}
	 */
	public static SSOEncrypt getConfigEncrypt() {

		if (encrypt != null) {
			return encrypt;
		}

		/**
		 * 判断是否自定义 Encrypt 默认 AES
		 */
		if ("".equals(SSOConfig.getEncryptClass())) {
			encrypt = new AES();
		} else {
			try {
				Class<?> tc = Class.forName(SSOConfig.getEncryptClass());
				if (tc.newInstance() instanceof SSOEncrypt) {
					encrypt = (SSOEncrypt) tc.newInstance();
				}
			} catch (Exception e) {
				throw new KissoException(e);
			}
		}
		return encrypt;
	}

	/**
	 * 反射获取自定义Token
	 * 
	 * @return {@link Token}
	 */
	public static Token getConfigToken() {
		/**
		 * 判断是否自定义 Token 默认 SSOToken
		 */
		Token token = null;
		if ("".equals(SSOConfig.getTokenClass())) {
			token = new SSOToken();
		} else {
			try {
				Class<?> tc = Class.forName(SSOConfig.getTokenClass());
				if (tc.newInstance() instanceof Token) {
					token = (Token) tc.newInstance();
				}
			} catch (Exception e) {
				throw new KissoException(e);
			}
		}
		return token;
	}
	/**
	 * 反射获取自定义SSOParser
	 * 
	 * @return {@link Token}
	 */
	public static SSOParser getConfigParser() {
		
		if (parser != null) {
			return parser;
		}
		
		/**
		 * 获取自定义 SSOParser
		 */
		if ("".equals(SSOConfig.getParserClass())) {
			parser = new FastJsonParser();
		} else {
			try {
				Class<?> tc = Class.forName(SSOConfig.getTokenClass());
				if (tc.newInstance() instanceof Token) {
					parser = (SSOParser) tc.newInstance();
				}
			} catch (Exception e) {
				throw new KissoException(" kisso Config 【 sso.parser.class 】 not find. or not instanceof SSOParser", e);
			}
		}
		return parser;
	}
	

	/**
	 * 反射获取自定义SSOStatistic
	 * 
	 * @return {@link SSOStatistic}
	 */
	public static SSOStatistic getConfigStatistic() {
		
		if (statistic != null) {
			return statistic;
		}
		
		/**
		 * 反射获得统计类
		 */
		String statisticClass = SSOConfig.getStatisticClass();
		if (!"".equals(statisticClass)) {
			try {
				Class<?> tc = Class.forName(statisticClass);
				if (tc.newInstance() instanceof SSOCache) {
					statistic = (SSOStatistic) tc.newInstance();
				}
			} catch (Exception e) {
				throw new KissoException(e);
			}
		}
		return statistic;
	}

	/**
	 * 反射获取自定义SSOCache
	 * 
	 * @return {@link SSOCache}
	 */
	public static SSOCache getConfigCache() {

		if (cache != null) {
			return cache;
		}

		/**
		 * 反射获得缓存类
		 */
		String cacheClass = SSOConfig.getCacheClass();
		if (!"".equals(cacheClass)) {
			try {
				Class<?> tc = Class.forName(cacheClass);
				if (tc.newInstance() instanceof SSOCache) {
					cache = (SSOCache) tc.newInstance();
				}
			} catch (Exception e) {
				throw new KissoException(e);
			}
		}
		return cache;
	}
}
