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
package wang.leq.sso.handler;


/**
 * SSO 控制器
 * <p>
 * @author   hubin
 * @date	 2015年3月26日 
 * @version  1.0.0
 */
public abstract class SSOHandler {

	/**
	 * 是否强制退出
	 * ture 退出， false不退出
	 */
	public boolean forceOut() {
		return false;
	}
}
