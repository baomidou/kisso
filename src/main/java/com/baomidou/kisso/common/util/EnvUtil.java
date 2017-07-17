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
package com.baomidou.kisso.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * <p>
 * 获取系统环境变量工具类
 * </p>
 * 
 * @author hubin
 * @Date 2015-1-13
 */
public class EnvUtil {

	private static final Logger logger = Logger.getLogger("EnvUtil");

	private static Boolean OS_LINUX = null;

	/**
	 * 判断当前系统是否为 linux
	 * 
	 * @return true linux, false windows
	 */
	public static boolean isLinux() {
		if (OS_LINUX == null) {
			String OS = System.getProperty("os.name").toLowerCase();
			logger.info("os.name: " + OS);
			if (OS != null && OS.contains("windows")) {
				OS_LINUX = false;
			} else {
				OS_LINUX = true;
			}
		}
		return OS_LINUX;
	}

	/**
	 * 返回当前系统变量的函数 结果放至 Properties
	 */
	public static Properties getEnv() {
		Properties prop = new Properties();
		try {
			Process p = null;
			if (isLinux()) {
				p = Runtime.getRuntime().exec("sh -c set");
			} else {
				// windows
				p = Runtime.getRuntime().exec("cmd /c set");
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				int i = line.indexOf("=");
				if (i > -1) {
					String key = line.substring(0, i);
					String value = line.substring(i + 1);
					prop.setProperty(key, value);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
	}

}
