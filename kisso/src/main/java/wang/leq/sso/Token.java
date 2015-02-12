/**
 * Copyright (c) 2011-2014, hubin (243194995@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wang.leq.sso;

import com.alibaba.fastjson.JSON;

/**
 * SSO票据顶级父类
* <p>
 * @author   hubin
 * @Date	 2014-5-9 	 
 */
public class Token {
	/**
	 * 登录 IP
	 */
	private String userIp;
	
	/**
	 * Token转为JSON格式
	 * <p>
	 * @return JSON格式Token值
	 */
	public String jsonToken() {
		return JSON.toJSONString(this);
	}

	/**
	 * JSON格式Token值转为Token对象
	 * <p>
	 * @param jsonToken
	 * 				JSON格式Token值
	 * @return Token对象
	 */
	public Token parseToken(String jsonToken) {
		return JSON.parseObject(jsonToken, this.getClass());
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
}
