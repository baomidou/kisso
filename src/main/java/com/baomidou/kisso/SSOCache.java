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
package com.baomidou.kisso;

/**
 * <p>
 * SSO 缓存接口
 * </p>
 * 
 * @author hubin
 * @Date 2015-11-03
 */
public interface SSOCache {

	/**
	 * <p>
	 * 根据key获取SSO票据
	 * </p>
	 * <p>
	 * 如果缓存服务宕机，返回 token 设置 flag 为 Token.FLAG_CACHE_SHUT
	 * </p>
	 * 
	 * @param key
	 *            关键词
	 * @param expires
	 *            过期时间（延时心跳时间）
	 * @return Token SSO票据
	 */
	Token get(String key, int expires);

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
	boolean set(String key, Token token, int expires);

	/**
	 * 删除SSO票据
	 * <p>
	 * 
	 * @param key
	 *            关键词
	 */
	boolean delete(String key);
	
}
