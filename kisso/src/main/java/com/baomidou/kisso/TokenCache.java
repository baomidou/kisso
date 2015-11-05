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

/**
 * <p>
 * Token 缓存父类
 * </p>
 * 
 * @author hubin
 * @Date 2015-11-30
 */
public abstract class TokenCache {

	/**
	 * <p>
	 * 根据key获取SSO票据
	 * </p>
	 * <p>
	 * 如果缓存服务宕机，返回 token 设置 flag 为 Token.Flag.CACHE_SHUT
	 * </p>
	 * 
	 * @param key
	 *            关键词
	 * @return Token SSO票据
	 */
	public abstract Token get(String key);

	/**
	 * 设置SSO票据
	 * 
	 * @param key
	 *            关键词
	 * @param token
	 *            SSO票据
	 * @param expires
	 *            过期时间
	 */
	public abstract boolean set(String key, Token token, long expires);

	/**
	 * 删除SSO票据
	 * <p>
	 * 
	 * @param key
	 *            关键词
	 */
	public abstract boolean delete(String key);
}
