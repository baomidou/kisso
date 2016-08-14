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
package com.baomidou.kisso.common;

import com.baomidou.kisso.SSOCache;
import com.baomidou.kisso.SSOStatistic;
import com.baomidou.kisso.SSOToken;
import com.baomidou.kisso.Token;
import com.baomidou.kisso.common.encrypt.Algorithm;
import com.baomidou.kisso.common.encrypt.SSOEncrypt;
import com.baomidou.kisso.common.encrypt.SSOSymmetrical;
import com.baomidou.kisso.common.parser.FastJsonParser;
import com.baomidou.kisso.common.parser.SSOParser;
import com.baomidou.kisso.exception.KissoException;

/**
 * <p>
 * SSO 反射辅助类
 * </p>
 * 
 * @author hubin
 * @Date 2015-12-05
 */
public class SSOReflectHelper {

	private static SSOEncrypt encrypt = null;

	private static SSOCache cache = null;

	private static SSOStatistic statistic = null;

	private static SSOParser parser = null;


	/**
	 * <p>
	 * 反射获取自定义Token
	 * </p>
	 * 
	 * @return {@link Token}
	 */
	public static Token getConfigToken( String tokenClass ) {
		/**
		 * 判断是否自定义 Token 默认 SSOToken
		 */
		Token token = null;
		if ( tokenClass == null || "".equals(tokenClass) ) {
			token = new SSOToken();
		} else {
			try {
				Class<?> tc = Class.forName(tokenClass);
				if ( tc.newInstance() instanceof Token ) {
					token = (Token) tc.newInstance();
				}
			} catch ( Exception e ) {
				throw new KissoException(e);
			}
		}

		return token;
	}


	/**
	 * <p>
	 * 反射获取自定义SSOParser
	 * </p>
	 * 
	 * @return {@link Token}
	 */
	public static SSOParser getConfigParser( String parserClass ) {

		if ( parser != null ) {
			return parser;
		}

		/**
		 * 获取自定义 SSOParser
		 */
		if ( parserClass == null || "".equals(parserClass) ) {
			parser = new FastJsonParser();
		} else {
			try {
				Class<?> tc = Class.forName(parserClass);
				if ( tc.newInstance() instanceof SSOParser ) {
					parser = (SSOParser) tc.newInstance();
				}
			} catch ( Exception e ) {
				throw new KissoException(" kisso Config 【 sso.parser.class 】 not find. or not instanceof SSOParser", e);
			}
		}

		return parser;
	}


	public static void setConfigParser( SSOParser configParser ) {
		parser = configParser;
	}


	/**
	 * <p>
	 * 反射获取自定义Encrypt
	 * </p>
	 * 
	 * @return {@link Encrypt}
	 */
	public static SSOEncrypt getConfigEncrypt( String encryptClass, String encryptAlgorithm) {

		if ( encrypt != null ) {
			return encrypt;
		}

		/**
		 * 判断是否自定义 Encrypt 默认 RC4
		 */
		if ( encryptClass == null || "".equals(encryptClass) ) {
			encrypt = new SSOSymmetrical(Algorithm.convert(encryptAlgorithm));
		} else {
			try {
				Class<?> tc = Class.forName(encryptClass);
				if ( tc.newInstance() instanceof SSOEncrypt ) {
					encrypt = (SSOEncrypt) tc.newInstance();
				}
			} catch ( Exception e ) {
				throw new KissoException(e);
			}
		}

		return encrypt;
	}


	public static void setConfigEncrypt( SSOEncrypt configEncrypt ) {
		encrypt = configEncrypt;
	}


	/**
	 * <p>
	 * 反射获取自定义SSOCache
	 * </p>
	 * 
	 * @return {@link SSOCache}
	 */
	public static SSOCache getConfigCache( String cacheClass ) {

		if ( cache != null ) {
			return cache;
		}

		/**
		 * 反射获得缓存类
		 */
		if ( cacheClass != null && !"".equals(cacheClass) ) {
			try {
				Class<?> tc = Class.forName(cacheClass);
				if ( tc.newInstance() instanceof SSOCache ) {
					cache = (SSOCache) tc.newInstance();
				}
			} catch ( Exception e ) {
				throw new KissoException(e);
			}
		}
		return cache;
	}


	public static void setConfigCache( SSOCache configcache ) {
		cache = configcache;
	}


	/**
	 * 反射获取自定义SSOStatistic
	 * 
	 * @return {@link SSOStatistic}
	 */
	public static SSOStatistic getConfigStatistic( String encryptClass ) {

		if ( statistic != null ) {
			return statistic;
		}

		/**
		 * 反射获得统计类
		 */
		if ( encryptClass != null && !"".equals(encryptClass) ) {
			try {
				Class<?> tc = Class.forName(encryptClass);
				if ( tc.newInstance() instanceof SSOStatistic ) {
					statistic = (SSOStatistic) tc.newInstance();
				}
			} catch ( Exception e ) {
				throw new KissoException(e);
			}
		}

		return statistic;
	}


	public static void setConfigStatistic( SSOStatistic configStatistic ) {
		statistic = configStatistic;
	}

}
